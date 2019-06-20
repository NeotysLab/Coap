package com.neotys.coap.CoapAsyncReceive;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;

import com.google.common.base.Optional;
import com.neotys.extensions.action.Action;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;

public final class CoapAsyncReceiveAction implements Action{
	private static final String BUNDLE_NAME = "com.neotys.coap.CoapAsyncReceive.bundle";
	private static final String DISPLAY_NAME = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displayName");
	private static final String DISPLAY_PATH = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displayPath");
	public static final String NumberOfMessage="NumberOfMessage";
	public static final String IntTimeout="IntTimeout";
	@Override
	public String getType() {
		return "CoapAsyncReceive";
	}

	@Override
	public List<ActionParameter> getDefaultActionParameters() {
		final List<ActionParameter> parameters = new ArrayList<ActionParameter>();
		parameters.add(new ActionParameter(NumberOfMessage,"1"));
		parameters.add(new ActionParameter(IntTimeout,"10"));
		
		// TODO Add default parameters.
		return parameters;
	}

	@Override
	public Class<? extends ActionEngine> getEngineClass() {
		return CoapAsyncReceiveActionEngine.class;
	}

	@Override
	public Icon getIcon() {
		// TODO Add an icon
		return null;
	}

	@Override
	public boolean getDefaultIsHit(){
		return false;
	}

	@Override
	public String getDescription() {
		final StringBuilder description = new StringBuilder();
		// TODO Add description
		description.append("CoapAsyncReceive is retrieving the response from ansynchronous coap request.\n");
		description.append("CoapAsyncReceive requires the following parameter.\n");
		description.append("- NumberOfMessage :  Number of async CoapResponse to retrieve.\n");
		description.append("- IntTimeout :  Number of seconds between messages.\n");
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
