package com.neotys.coap.CoapSend;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CoapConnectActionActionTest {
	@Test
	public void shouldReturnType() {
		final CoapSendAction action = new CoapSendAction();
		assertEquals("CoapSendAction", action.getType());
	}

}
