package com.neotys.coap.CoapSend;

import java.util.concurrent.LinkedBlockingQueue;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Response;


public class CoapNeotysClient extends CoapClient {

	private String URl;
	private int Accept;
	private String Method;
	private int FormatOfpayLoad;
	private String Payload;
	private boolean asynchronous;
	private  NeotysCoapHandler handler;
	public static final String TEXT_PLAIN = "TEXT_PLAIN";
	public static final String TEXT_XML = "TEXT_XML";
	public static final String TEXT_CSV = "TEXT_CSV";
	public static final String TEXT_HTML = "TEXT_HTML";
	public static final String IMAGE_GIF = "IMAGE_GIF"; 
	public static final String IMAGE_JPEG = "IMAGE_JPEG"; // 03
	public static final String IMAGE_PNG = "IMAGE_PNG"; // 03
	public static final String IMAGE_TIFF = "IMAGE_TIFF"; // 03
	public static final String AUDIO_RAW = "AUDIO_RAW"; // 03
	public static final String VIDEO_RAW = "VIDEO_RAW"; // 03
	public static final String APPLICATION_LINK_FORMAT = "APPLICATION_LINK_FORMAT";
	public static final String APPLICATION_XML = "APPLICATION_XML";
	public static final String APPLICATION_OCTET_STREAM = "APPLICATION_OCTET_STREAM";
	public static final String APPLICATION_RDF_XML = "APPLICATION_RDF_XML";
	public static final String APPLICATION_SOAP_XML = "APPLICATION_SOAP_XML";
	public static final String APPLICATION_ATOM_XML = "APPLICATION_ATOM_XML";
	public static final String APPLICATION_XMPP_XML = "APPLICATION_XMPP_XML";
	public static final String APPLICATION_EXI = "APPLICATION_EXI";
	public static final String APPLICATION_FASTINFOSET = "APPLICATION_FASTINFOSET"; // 04
	public static final String APPLICATION_SOAP_FASTINFOSET = "APPLICATION_SOAP_FASTINFOSET"; // 04
	public static final String APPLICATION_JSON = "APPLICATION_JSON"; // 04
	public static final String APPLICATION_X_OBIX_BINARY = "APPLICATION_X_OBIX_BINARY"; // 04
	
	private  static final String GET="GET";
	private  static final String POST="POST";
	private  static final String PUT="PUT";
	private  static final String DELETE="DELETE";

	private  static final String OBSERVE="OBSERVE";

	public CoapNeotysClient(String uRl, String accept, String method, String formatOfpayLoad, String payload, boolean async,LinkedBlockingQueue<CoapResponse> queue) {
		super(uRl);
		URl = uRl;
		Accept = getType(accept.toUpperCase());
		Method = method;
		FormatOfpayLoad = getType(formatOfpayLoad.toUpperCase());
		Payload = payload;
		asynchronous=async;
		if(asynchronous)
			handler=new NeotysCoapHandler(queue);
	}
	public CoapNeotysClient(String uRl, String accept, String method,boolean async,LinkedBlockingQueue<CoapResponse> queue) {
		super(uRl);
		URl = uRl;
		Accept = getType(accept.toUpperCase());
		Method = method;
		asynchronous=async;
		if(asynchronous)
			handler=new NeotysCoapHandler(queue);
		
	}
	
	private byte[] GetPayLoadByte()
	{
		//-----handle the case of hex data sent in the custom action-----image, video...etc
		byte[] result = null;
		
		return result;
	}
	
	private CoapResponse getCoapResponse()
	{
		if(!asynchronous)
		{
			if(Accept!=-1)
				return super.get(Accept);
			else
				return super.get();
		}
		else
		{
			return null;
		}
		
	}
	private void getCoapAsyncResponse()
	{
		if(Accept!=-1)
			 super.get(handler,Accept);	
		else
			super.get(handler);
		
	}
	public int getPayLoadFormat()
	{
		return FormatOfpayLoad;
	}
	
	public int getAccept()
	{
		return Accept;
	}
	
	private CoapResponse postCoapResponse()
	{
		if(!asynchronous)
		{
			if(Accept!=-1)
				return super.post(Payload,FormatOfpayLoad,Accept);
			else
				return super.post(Payload,FormatOfpayLoad);
		}
		else
		{
			return null;
		}
		
	}
	
	private void postAsyncCoapResponse()
	{
		if(Accept!=-1)
			 super.post(handler,Payload,FormatOfpayLoad,Accept);	
		else
			 super.post(handler,Payload,FormatOfpayLoad);
	}
	
	private CoapResponse deleteCoapResponse()
	{
		if(!asynchronous)
		{
			return super.delete();
		}
		else
		{
			return null;
		}
		
	}
	
	private void deleteAsyncCoapResponse()
	{
		
		 super.delete(handler);
		
	}
	
	private CoapResponse putCoapResponse()
	{
		if(!asynchronous)
		{
			return super.put(Payload,FormatOfpayLoad);
		}
		else
		{
			return null;
		}
		
	}
	
	private void putAsyncCoapResponse()
	{
		 super.put(handler,Payload,FormatOfpayLoad);
	}
	
	public String send()
	{
		CoapResponse response = null;
		String result = null;
		
		if(!asynchronous)
		{
			
			switch(Method)
			{
				case GET :
					response=getCoapResponse();
					break;
				case PUT :
					response=putCoapResponse();
					break;
				case POST:
					response=postCoapResponse();
					break;
				case DELETE:
					response=deleteCoapResponse();
					break;	
				case OBSERVE:
					observeandwait();
					response=null;
					break;
			}
			if(response!=null)
				result=Utils.prettyPrint(response);

		}
		else
		{
			switch(Method)
			{
				case GET :
					getCoapAsyncResponse();
					break;
				case PUT :
					putAsyncCoapResponse();
					break;
				case POST:
					postAsyncCoapResponse();
					break;
				case DELETE:
					deleteAsyncCoapResponse();
					break;		
				case OBSERVE:
					observe();
					break;
			}	
			result="Asynchronous Method send "+Method+" on the url :"+ URl;
		}
		return result;

	}

	private void observe()
	{
		if(Accept!=-1)
			super.observe(handler,Accept);
		else
			super.observe(handler);
	}
	
	private void observeandwait()
	{
		if(Accept!=-1)
			super.observeAndWait(handler,Accept);
		else
			super.observeAndWait(handler);
	}
	
	 public static int getType(String type) {
		switch (type) {
		case TEXT_PLAIN:
			return MediaTypeRegistry.TEXT_PLAIN;
		case TEXT_XML:
			return MediaTypeRegistry.TEXT_XML;
		case TEXT_CSV:
			return MediaTypeRegistry.TEXT_CSV;
		case TEXT_HTML:
			return MediaTypeRegistry.TEXT_HTML;
		case APPLICATION_LINK_FORMAT:
			return MediaTypeRegistry.APPLICATION_LINK_FORMAT;
		case APPLICATION_XML:
			return MediaTypeRegistry.APPLICATION_XML;
		case APPLICATION_RDF_XML:
			return MediaTypeRegistry.APPLICATION_RDF_XML;
		case APPLICATION_SOAP_XML:
			return MediaTypeRegistry.APPLICATION_SOAP_XML;
		case APPLICATION_ATOM_XML:
			return MediaTypeRegistry.APPLICATION_ATOM_XML;
		case APPLICATION_XMPP_XML:
			return MediaTypeRegistry.APPLICATION_XMPP_XML;
		case APPLICATION_JSON:
			return MediaTypeRegistry.APPLICATION_JSON;
		case IMAGE_GIF:
			return MediaTypeRegistry.IMAGE_GIF;
		case IMAGE_JPEG:
			return MediaTypeRegistry.IMAGE_PNG;
		case IMAGE_PNG:
			return MediaTypeRegistry.IMAGE_PNG;
		case IMAGE_TIFF:
			return MediaTypeRegistry.IMAGE_TIFF;
		case AUDIO_RAW:
			return MediaTypeRegistry.AUDIO_RAW;
		case VIDEO_RAW:
			return MediaTypeRegistry.VIDEO_RAW;
		case APPLICATION_OCTET_STREAM:
			return MediaTypeRegistry.APPLICATION_OCTET_STREAM;
		case APPLICATION_EXI:
			return MediaTypeRegistry.APPLICATION_EXI;
		case APPLICATION_FASTINFOSET:
			return MediaTypeRegistry.APPLICATION_FASTINFOSET;
		case APPLICATION_SOAP_FASTINFOSET:
			return MediaTypeRegistry.APPLICATION_SOAP_FASTINFOSET;
		case APPLICATION_X_OBIX_BINARY:
			return MediaTypeRegistry.APPLICATION_X_OBIX_BINARY;
		}
		return -1;
	}
}
