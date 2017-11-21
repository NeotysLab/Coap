package com.neotys.coap.CoapAsyncReceive;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.eclipse.californium.core.CoapResponse;

import com.google.common.base.Strings;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.SampleResult;


public final class CoapAsyncReceiveActionEngine implements ActionEngine {

	private int NumberOfMessage;
	private int IntTimeout;
	@Override
	public SampleResult execute(Context context, List<ActionParameter> parameters) {
		final SampleResult sampleResult = new SampleResult();
		final StringBuilder requestBuilder = new StringBuilder();
		final StringBuilder responseBuilder = new StringBuilder();
		String NumberOfMessagevalue = null;
		String IntTimeoutvalue=null;
		
		CoapResponse message;
		LinkedBlockingQueue<CoapResponse> messagequeue;
		
		for(ActionParameter parameter:parameters) 
		{
			switch(parameter.getName()) 
			{
				case  CoapAsyncReceiveAction.NumberOfMessage:
					NumberOfMessagevalue = parameter.getValue();
					break;
				case  CoapAsyncReceiveAction.IntTimeout:
					IntTimeoutvalue = parameter.getValue();
					break;
			}
		}
			
		if (Strings.isNullOrEmpty(NumberOfMessagevalue)) 
		{
			return getErrorResult(context, sampleResult, "Invalid argument: NumberOfMessage cannot be null "
					+ CoapAsyncReceiveAction.NumberOfMessage + ".", null);
		}
		if (Strings.isNullOrEmpty(IntTimeoutvalue)) 
		{
			return getErrorResult(context, sampleResult, "Invalid argument: IntTimeout cannot be null "
					+ CoapAsyncReceiveAction.IntTimeout + ".", null);
		}

		
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
			return getErrorResult(context, sampleResult, "Technical error : NumberOfMessage needs to be a digit ",null);
		}
		catch(Exception e)
		{
			return getErrorResult(context, sampleResult, "Technical error",e);
		}
		sampleResult.setRequestContent(requestBuilder.toString());
		sampleResult.setResponseContent(responseBuilder.toString());
		return sampleResult;
	}

	private void appendLineToStringBuilder(final StringBuilder sb, final String line){
		sb.append(line).append("\n");
	}

	/**
	 * This method allows to easily create an error result and log exception.
	 */
	private static SampleResult getErrorResult(final Context context, final SampleResult result, final String errorMessage, final Exception exception) {
		result.setError(true);
		result.setStatusCode("NL-CoapAsyncReceive_ERROR");
		result.setResponseContent(errorMessage);
		if(exception != null){
			context.getLogger().error(errorMessage, exception);
		} else{
			context.getLogger().error(errorMessage);
		}
		return result;
	}

	@Override
	public void stopExecute() {
		// TODO add code executed when the test have to stop.
	}

}
