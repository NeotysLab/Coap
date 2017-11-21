package com.neotys.coap.CoapSend;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import com.google.common.base.Strings;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.SampleResult;

public final class CoapSendActionEngine implements ActionEngine {

	private  String CoapURL;
	private  String Method;
	private  String IsAsynchronousvalue;
	private  boolean IsAsynchronous=false;
	private  String PayLoad;
	private  String Accept;
	private String FormatOfPayload;

	private  static final String GET="GET";
	private  static final String POST="POST";
	private  static final String PUT="PUT";
	private  static final String DELETE="DELETE";
	private  static final String OBSERVE="OBSERVE";
	private  CoapNeotysClient client;

	
	@Override
	public SampleResult execute(Context context, List<ActionParameter> parameters) {
		final SampleResult sampleResult = new SampleResult();
		final StringBuilder requestBuilder = new StringBuilder();
		final StringBuilder responseBuilder = new StringBuilder();
		int IntAccept=-1;
		int IntPayload=-1;
		LinkedBlockingQueue<CoapResponse> messagequeue=null;
		
		for(ActionParameter parameter:parameters) {
			switch(parameter.getName()) 
			{
			case  CoapSendAction.CoapURL:
				CoapURL = parameter.getValue();
				break;
			case  CoapSendAction.Method:
				Method = parameter.getValue().toUpperCase();
				break;
			case  CoapSendAction.IsAsynchronous:
				IsAsynchronousvalue = parameter.getValue();
				break;
			
			case  CoapSendAction.PayLoad:
				PayLoad = parameter.getValue();
				break;
			case  CoapSendAction.FormatOfPayload:
				FormatOfPayload = parameter.getValue();
				break;
			case  CoapSendAction.Accept:
				Accept = parameter.getValue();
				break;
			
			}
		}
		if (Strings.isNullOrEmpty(CoapURL)) {
			return getErrorResult(context, sampleResult, "Invalid argument: CoapURL cannot be null "
					+ CoapSendAction.CoapURL + ".", null);
		}
		if(!ValidCoapUrl())
			return getErrorResult(context, sampleResult, "Invalid argument: CoapURL is a invalid url "
					+ CoapSendAction.CoapURL + ".", null);
	
		if(!ValidMethod())
		{
			return getErrorResult(context, sampleResult, "Invalid Method: Method specified not supported :"	+ CoapSendAction.Method + ".", null);
		}
		if (Strings.isNullOrEmpty(IsAsynchronousvalue)) {
			return getErrorResult(context, sampleResult, "Invalid argument: IsAsynchronous cannot be null "
					+ CoapSendAction.IsAsynchronous + ".", null);
		}
		if(IsAsynchronousvalue.toUpperCase().equalsIgnoreCase("TRUE")||IsAsynchronousvalue.toUpperCase().equalsIgnoreCase("FALSE"))
		{
			return getErrorResult(context, sampleResult, "Invalid argument: IsAsynchronous can only be equal to false or true "
					+ CoapSendAction.IsAsynchronous + ".", null);
		}
		IsAsynchronous=Boolean.parseBoolean(IsAsynchronousvalue);
		
		try
		{
			if(IsAsynchronous)
			{
				messagequeue=new LinkedBlockingQueue<CoapResponse>();
				context.getCurrentVirtualUser().put("Method.CoapResponse."+context.getCurrentVirtualUser().getName(),messagequeue);
			}
			sampleResult.sampleStart();
			
			if(!Strings.isNullOrEmpty(FormatOfPayload)&&!Strings.isNullOrEmpty(PayLoad))
			{
				client = new CoapNeotysClient(CoapURL,Accept,Method,FormatOfPayload,PayLoad,IsAsynchronous,messagequeue);
				if(client.getPayLoadFormat()!=-1)
				{
					return getErrorResult(context, sampleResult, "Invalid argument: FormatOfPayload unsupported value"
							+ CoapSendAction.FormatOfPayload + ".", null);
				}
			}
			else
			{
				client=new CoapNeotysClient(CoapURL, Accept, Method,IsAsynchronous,messagequeue);
			}
				
			if(client.getAccept()!=-1)
			{
				return getErrorResult(context, sampleResult, "Invalid argument: Accept unsupported value"
						+ CoapSendAction.Accept + ".", null);
			}	
			appendLineToStringBuilder(responseBuilder, client.send());
			// TODO perform execution.
		}
		catch(Exception e)
		{
			return getErrorResult(context, sampleResult, "Technical error",e);
		}
		sampleResult.sampleEnd();

		sampleResult.setRequestContent(requestBuilder.toString());
		sampleResult.setResponseContent(responseBuilder.toString());
		return sampleResult;
	}
	private boolean ValidMethod()
	{
		boolean result= false;
		
		switch(Method)
		{
			case GET :
				result=true;
				break;
			case PUT :
				result=true;
				break;
			case POST:
				result=true;
				break;
			case DELETE:
				result=true;
				break;
			case OBSERVE:
				result=true;
				break;
		}
		return result;
	}
	private boolean ValidCoapUrl()
	{
		boolean result = false;
		
		if(CoapURL.startsWith("coap")||CoapURL.startsWith("coaps"))
			result=true;
		
		return result;
	}
	private void appendLineToStringBuilder(final StringBuilder sb, final String line){
		sb.append(line).append("\n");
	}

	/**
	 * This method allows to easily create an error result and log exception.
	 */
	private static SampleResult getErrorResult(final Context context, final SampleResult result, final String errorMessage, final Exception exception) {
		result.setError(true);
		result.setStatusCode("NL-CoapConnectAction_ERROR");
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
