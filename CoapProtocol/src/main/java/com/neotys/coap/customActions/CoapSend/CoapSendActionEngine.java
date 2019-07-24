package com.neotys.coap.customActions.CoapSend;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import com.google.common.base.Optional;
import com.neotys.action.argument.Option;
import com.neotys.action.result.ResultFactory;
import com.neotys.coap.common.CoapContext;
import com.neotys.coap.common.CoapNeotysClient;
import com.neotys.extensions.action.engine.Logger;
import org.eclipse.californium.core.CoapResponse;

import com.google.common.base.Strings;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.SampleResult;

import static com.neotys.action.argument.Arguments.getArgumentLogString;
import static com.neotys.action.argument.Arguments.parseArguments;

public final class CoapSendActionEngine implements ActionEngine {

	private  String CoapURL;
	private  String Method;
	private  String IsAsynchronousvalue;
	private  boolean IsAsynchronous=false;
	private Optional<String> PayLoad;
	private Optional<String> Accept;
	private Optional<String> FormatOfPayload;

	private  static final String GET="GET";
	private  static final String POST="POST";
	private  static final String PUT="PUT";
	private  static final String DELETE="DELETE";
	private  static final String OBSERVE="OBSERVE";
	private CoapNeotysClient client;

	private static final String STATUS_CODE_INVALID_PARAMETER = "NL-COAPSEND_ACTION-01";
	private static final String STATUS_CODE_TECHNICAL_ERROR = "NL-COAPSEND_ACTION-02";
	private static final String STATUS_CODE_BAD_CONTEXT = "NL-COAPSEND_ACTION-03";

	@Override
	public SampleResult execute(Context context, List<ActionParameter> parameters) {
		final SampleResult sampleResult = new SampleResult();
		final StringBuilder requestBuilder = new StringBuilder();
		final StringBuilder responseBuilder = new StringBuilder();
		int IntAccept=-1;
		int IntPayload=-1;
		LinkedBlockingQueue<CoapResponse> messagequeue=null;



		final Map<String, com.google.common.base.Optional<String>> parsedArgs;
		try {
			parsedArgs = parseArguments(parameters, CoapSendOption.values());
		} catch (final IllegalArgumentException iae) {
			return ResultFactory.newErrorResult(context, STATUS_CODE_INVALID_PARAMETER, "Could not parse arguments: ", iae);
		}



		final Logger logger = context.getLogger();
		if (logger.isDebugEnabled()) {
			logger.debug("Executing " + this.getClass().getName() + " with parameters: "
					+ getArgumentLogString(parsedArgs, CoapSendOption.values()));
		}



		CoapURL = parsedArgs.get(CoapSendOption.CoapURL.getName()).get();
		Method =parsedArgs.get(CoapSendOption.Method.getName()).get();
		IsAsynchronousvalue =parsedArgs.get(CoapSendOption.IsAsynchronous.getName()).get();
		PayLoad=parsedArgs.get(CoapSendOption.PayLoad.getName());
		Accept=parsedArgs.get(CoapSendOption.Accept.getName());
		FormatOfPayload=parsedArgs.get(CoapSendOption.FormatOfPayload.getName());
		final Optional<String> keytrustorePath=parsedArgs.get(CoapSendOption.TrustKeyPath.getName());
		final Optional<String> keytrustorePassword=parsedArgs.get(CoapSendOption.TrustKeyPassword.getName());
		final Optional<String> keystorepath=parsedArgs.get(CoapSendOption.KeyStoreLocation.getName());
		final Optional<String> keystorepassword=parsedArgs.get(CoapSendOption.KeyStorePassword.getName());
		final Optional<String> keystorealias=parsedArgs.get(CoapSendOption.KeyStoreAlias.getName());
		final Optional<String> keystorealiaspassword=parsedArgs.get(CoapSendOption.KeyStoreAliasPassword.getName());

		final Optional<String> tracemode=parsedArgs.get((CoapSendOption.TraceMode.getName()));




		if(!ValidMethod())
		{
			return ResultFactory.newErrorResult(context, STATUS_CODE_INVALID_PARAMETER, "Invalid Method: Method specified not supported Method:"	+ Method + ".", null);

		}

		IsAsynchronous=Boolean.parseBoolean(IsAsynchronousvalue);
		
		try
		{
			if(IsAsynchronous)
			{
				messagequeue=(LinkedBlockingQueue<CoapResponse>) context.getCurrentVirtualUser().get("Method.CoapResponse."+context.getCurrentVirtualUser().getName());
				if(messagequeue!=null) {
					messagequeue=new LinkedBlockingQueue<CoapResponse>();
					context.getCurrentVirtualUser().put("Method.CoapResponse." + context.getCurrentVirtualUser().getName(), messagequeue);

				}
			}

			boolean checkdtls=checkDTSLparameters(keystorealias,keystorealiaspassword,keystorepassword,keystorepath,keytrustorePassword,keytrustorePath);
			if(keytrustorePath.isPresent())
			{
				if(!checkdtls)
					return ResultFactory.newErrorResult(context, STATUS_CODE_INVALID_PARAMETER, "Invalid  the keystore paremeters needs to be defined", null);

				if(!fileExists(keytrustorePath))
					return ResultFactory.newErrorResult(context, STATUS_CODE_INVALID_PARAMETER, "Invalid  the keystore file doesn't exists", null);

			}
			if(keytrustorePassword.isPresent())
			{
				if(!checkdtls)
					return ResultFactory.newErrorResult(context, STATUS_CODE_INVALID_PARAMETER, "Invalid  the keystore paremeters needs to be defined", null);

			}
			if(keystorealias.isPresent())
			{
				if(!checkdtls)
					return ResultFactory.newErrorResult(context, STATUS_CODE_INVALID_PARAMETER, "Invalid  the keystore paremeters needs to be defined", null);

			}
			if(keystorealiaspassword.isPresent())
			{
				if(!checkdtls)
					return ResultFactory.newErrorResult(context, STATUS_CODE_INVALID_PARAMETER, "Invalid  the keystore paremeters needs to be defined", null);

			}
			if(keystorepath.isPresent())
			{
				if(!checkdtls)
					return ResultFactory.newErrorResult(context, STATUS_CODE_INVALID_PARAMETER, "Invalid  the keystore paremeters needs to be defined", null);

				if(!fileExists(keystorepath))
					return ResultFactory.newErrorResult(context, STATUS_CODE_INVALID_PARAMETER, "Invalid  the keystore file doesn't exists", null);

			}
			if(keystorepassword.isPresent())
			{
				if(!checkdtls)
					return ResultFactory.newErrorResult(context, STATUS_CODE_INVALID_PARAMETER, "Invalid  the keystore paremeters needs to be defined", null);

			}

			sampleResult.sampleStart();
			CoapContext coapContext = new CoapContext(CoapURL,Accept,Method,FormatOfPayload,PayLoad,IsAsynchronous,keytrustorePath,keytrustorePassword,keystorepath,keystorepassword,keystorealias,keystorealias);
			client=new CoapNeotysClient(context,coapContext,tracemode, Optional.fromNullable(messagequeue));

			if(FormatOfPayload.isPresent() && PayLoad.isPresent())
			{
				if(client.getPayLoadFormat()==-1)
				{
					return ResultFactory.newErrorResult(context, STATUS_CODE_INVALID_PARAMETER, "Invalid  "+CoapSendOption.FormatOfPayload.getName()+" specified not supported Method:"	+ FormatOfPayload.get() + ".", null);

				}
			}

				
			if(Accept.isPresent() && client.getAccept()==-1)
			{
				return ResultFactory.newErrorResult(context, STATUS_CODE_INVALID_PARAMETER, "Invalid  "+CoapSendOption.Accept.getName()+" specified not supported Method:"	+ Accept.get() + ".", null);

			}
			appendLineToStringBuilder(responseBuilder, client.send());

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
	private boolean checkDTSLparameters(Optional<String> param1,Optional<String> param2,Optional<String> param3,Optional<String> param4,Optional<String> param5,Optional<String> param6)
		{
			List<Optional<String>> optionals= new ArrayList<Optional<String>>(Arrays.asList(param1,param2,param3,param4,param5,param6));
			if(optionals.stream().filter(stringOptional -> !stringOptional.isPresent()).collect(Collectors.toList()).size()>0)
				return false;
			else
				return true;

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

	private boolean fileExists(Optional<String> filepath)
	{
		if(filepath.isPresent())
		{
			File file=new File(filepath.get());
			if(file.exists())
				return true;
			else
				return false;

		}
		else
			return true;
	}

	@Override
	public void stopExecute() {
		// TODO add code executed when the test have to stop.
	}

}
