package com.neotys.coap.CoapSend;

import java.util.concurrent.LinkedBlockingQueue;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;



public class NeotysCoapHandler implements CoapHandler {
	public NeotysCoapHandler(LinkedBlockingQueue<CoapResponse> queue) {
		super();
		this.queue = queue;
	}

	String content;
	
	private final LinkedBlockingQueue<CoapResponse> queue;

	
	@Override
	public void onError() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoad(CoapResponse response) {
		// TODO Auto-generated method stub
		queue.add(response);
		content = response.getResponseText();
	}

}
