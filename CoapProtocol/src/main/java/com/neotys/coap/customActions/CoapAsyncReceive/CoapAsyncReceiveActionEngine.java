package com.neotys.coap.customActions.CoapAsyncReceive;

import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Optional;
import com.neotys.action.result.ResultFactory;

import com.neotys.coap.common.Utils;
import com.neotys.extensions.action.engine.Logger;
import org.eclipse.californium.core.CoapResponse;

import com.google.common.base.Strings;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.SampleResult;

import static com.neotys.action.argument.Arguments.getArgumentLogString;
import static com.neotys.action.argument.Arguments.parseArguments;


public final class CoapAsyncReceiveActionEngine implements ActionEngine {

	private int NumberOfMessage;
	private int IntTimeout;
	private static final String STATUS_CODE_INVALID_PARAMETER = "NL-COAPRECEIVE_ACTION-01";
	private static final String STATUS_CODE_TECHNICAL_ERROR = "NL-COAPRECEIVE_ACTION-02";
	private static final String STATUS_CODE_BAD_CONTEXT = "NL-WB_COAPRECEIVE_ACTION-03";

	@Override
	public SampleResult execute(Context context, List<ActionParameter> parameters) {
		final SampleResult sampleResult = new SampleResult();
		final StringBuilder requestBuilder = new StringBuilder();
		final StringBuilder responseBuilder = new StringBuilder();

		
		CoapResponse message;
		LinkedBlockingQueue<CoapResponse> messagequeue;


		final Map<String, Optional<String>> parsedArgs;
		try {
			parsedArgs = parseArguments(parameters, CoapAsyncReceiveOption.values());
		} catch (final IllegalArgumentException iae) {
			return ResultFactory.newErrorResult(context, STATUS_CODE_INVALID_PARAMETER, "Could not parse arguments: ", iae);
		}



		final Logger logger = context.getLogger();
		if (logger.isDebugEnabled()) {
			logger.debug("Executing " + this.getClass().getName() + " with parameters: "
					+ getArgumentLogString(parsedArgs, CoapAsyncReceiveOption.values()));
		}

		final String NumberOfMessagevalue = parsedArgs.get(CoapAsyncReceiveOption.NumberOfMessage.getName()).get();
		final String IntTimeoutvalue=parsedArgs.get(CoapAsyncReceiveOption.IntTimeout.getName()).get();
		final Optional<String> tracemode=parsedArgs.get((CoapAsyncReceiveOption.TraceMode.getName()));



		
		try
		{
			NumberOfMessage=Integer.parseInt(NumberOfMessagevalue);
			IntTimeout=Integer.parseInt(IntTimeoutvalue);
			sampleResult.sampleStart();
			messagequeue= (LinkedBlockingQueue<CoapResponse>)context.getCurrentVirtualUser().get("Method.CoapResponse."+context.getCurrentVirtualUser().getName());
			for(int i=0;i<NumberOfMessage;i++)
			{
				message=messagequeue.poll(IntTimeout,TimeUnit.SECONDS);
				if (message != null)
				{
					appendLineToStringBuilder(responseBuilder, Utils.prettyPrint(message));
				}
			}
			sampleResult.sampleEnd();
				

		}
		catch(NumberFormatException e)
		{
			return ResultFactory.newErrorResult(context, STATUS_CODE_BAD_CONTEXT, "Error encountered : The parameter needs to be in a numerical format", e);
		}
		catch(Exception e)
		{
			return ResultFactory.newErrorResult(context, STATUS_CODE_TECHNICAL_ERROR, "Error encountered :", e);
		}
		sampleResult.setRequestContent(requestBuilder.toString());
		sampleResult.setResponseContent(responseBuilder.toString());
		return sampleResult;
	}

	private void appendLineToStringBuilder(final StringBuilder sb, final String line){
		sb.append(line).append("\n");
	}


	@Override
	public void stopExecute() {
		// TODO add code executed when the test have to stop.
	}

}
