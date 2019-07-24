package com.neotys.coap.CoapAsyncReceive;

import static org.junit.Assert.assertEquals;

import com.neotys.coap.customActions.CoapAsyncReceive.CoapAsyncReceiveAction;
import org.junit.Test;

public class CoapAsyncReceiveActionTest {
	@Test
	public void shouldReturnType() {
		final CoapAsyncReceiveAction action = new CoapAsyncReceiveAction();
		assertEquals("CoapAsyncReceive", action.getType());
	}

}
