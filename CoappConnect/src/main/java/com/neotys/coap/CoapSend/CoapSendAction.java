package com.neotys.coap.CoapSend;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;

import com.google.common.base.Optional;
import com.neotys.extensions.action.Action;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;

public final class CoapSendAction implements Action{
	private static final String BUNDLE_NAME = "com.neotys.coap.CoapSend.bundle";
	private static final String DISPLAY_NAME = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displayName");
	private static final String DISPLAY_PATH = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displayPath");
	public static final String CoapURL="CoapURL";
	public static final String Method="Method";
	public static final String IsAsynchronous="IsAsynchronous";
	public static final String PayLoad="PayLoad";
	public static final String FormatOfPayload="FormatOfPayload";
	public static final String Accept="Accept";
	
	@Override
	public String getType() {
		return "CoapSendAction";
	}

	@Override
	public List<ActionParameter> getDefaultActionParameters() {
		final List<ActionParameter> parameters = new ArrayList<ActionParameter>();
		// TODO Add default parameters.
		parameters.add(new ActionParameter(CoapURL,"Coap Url "));
		parameters.add(new ActionParameter(Method,"GET, POST, PUT,DELETE"));
		parameters.add(new ActionParameter(IsAsynchronous,"false"));
	
		

		return parameters;
	}

	@Override
	public Class<? extends ActionEngine> getEngineClass() {
		return CoapSendActionEngine.class;
	}

	@Override
	public Icon getIcon() {
		// TODO Add an icon
		return null;
	}
	@Override
	public boolean getDefaultIsHit(){
		return true;
	}

	@Override
	public String getDescription() {
		final StringBuilder description = new StringBuilder();
		// TODO Add description
		description.append("CoapSendAction description.\n");
		description.append("CoapSendAction parameters are : .\n");
		description.append("- CoapURL : Url of the CoapRequest\n");
		description.append("- Method :method of the coap request : GET, POST, PUT, DELETE,OBSERVE\n");
		description.append("- Optionnal PayLoad :Payload for  POST and PUT requests\n");
		description.append("- IsAsynchronous : true of false to define the request is asynchronous\n");
		description.append("- Optionnal FormatOfPayload : format of the payload : TEXT_PLAIN, TEXT_HTML, TEXT_XML, IMAGE_GIF,IMAGE_JPEG,IMAGE_PNG,VIDEO_RAW,AUDIO_RAW\n");
		description.append("                                           APPLICATION_LINK_FORMAT, APPLICATION_XML, APPLICATION_OCTET_STREAM, APPLICATION_RDF_XML,APPLICATION_SOAP_XML,APPLICATION_XMPP_XML,APPLICATION_JSON");
		description.append("- Optionnal Accept : Accept header of the response : TEXT_PLAIN, TEXT_HTML, TEXT_XML, IMAGE_GIF,IMAGE_JPEG,IMAGE_PNG,VIDEO_RAW,AUDIO_RAW\n");
		description.append("                                           APPLICATION_LINK_FORMAT, APPLICATION_XML, APPLICATION_OCTET_STREAM, APPLICATION_RDF_XML,APPLICATION_SOAP_XML,APPLICATION_XMPP_XML,APPLICATION_JSON\n");
		
		return description.toString();
	}

	@Override
	public String getDisplayName() {
		return DISPLAY_NAME;
	}

	@Override
	public String getDisplayPath() {
		return DISPLAY_PATH;
	}

	@Override
	public Optional<String> getMinimumNeoLoadVersion() {
		return Optional.of("6.1");
	}

	@Override
	public Optional<String> getMaximumNeoLoadVersion() {
		return Optional.absent();
	}
}
