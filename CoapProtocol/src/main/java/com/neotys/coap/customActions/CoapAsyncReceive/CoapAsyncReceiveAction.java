package com.neotys.coap.customActions.CoapAsyncReceive;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;

import com.google.common.base.Optional;
import com.neotys.action.argument.Arguments;
import com.neotys.action.argument.Option;
import com.neotys.coap.common.CoapUtils;
import com.neotys.extensions.action.Action;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;

public final class CoapAsyncReceiveAction implements Action{
	private static final String BUNDLE_NAME = "com.neotys.coap.CoapAsyncReceive.bundle";
	private static final String DISPLAY_NAME = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displayName");
	private static final String DISPLAY_PATH = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displayPath");

	@Override
	public String getType() {
		return "CoapAsyncReceive";
	}

	@Override
	public List<ActionParameter> getDefaultActionParameters() {
		final ArrayList<ActionParameter> parameters = new ArrayList<>();

		for (final CoapAsyncReceiveOption option : CoapAsyncReceiveOption.values()) {
			if (Option.AppearsByDefault.True.equals(option.getAppearsByDefault())) {
				parameters.add(new ActionParameter(option.getName(), option.getDefaultValue(),
						option.getType()));
			}
		}

		return parameters;
	}

	@Override
	public Class<? extends ActionEngine> getEngineClass() {
		return CoapAsyncReceiveActionEngine.class;
	}

	@Override
	public boolean getDefaultIsHit() {
		return false;
	}

	@Override
	public Icon getIcon() {
		return CoapUtils.getCoapIconIcon();

	}

	@Override
	public String getDescription() {
		return "The CoapReceive action will pull the message received from the neoload buffer.\n Here is the expected parameters :  .\n\n" + Arguments.getArgumentDescriptions(CoapAsyncReceiveOption.values());

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
		return Optional.of("6.7");
	}

	@Override
	public Optional<String> getMaximumNeoLoadVersion() {
		return Optional.absent();
	}

}
