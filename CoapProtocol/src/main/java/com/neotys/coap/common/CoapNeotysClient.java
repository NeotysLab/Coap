package com.neotys.coap.common;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;

import java.util.concurrent.LinkedBlockingQueue;

import com.google.common.base.Optional;
import com.neotys.extensions.action.engine.Context;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Option;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.scandium.DTLSConnector;
import org.eclipse.californium.scandium.config.DtlsConnectorConfig;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;


public class CoapNeotysClient extends CoapClient {

	private String uRl;
	private int accept;
	private String method;
	private int formatOfpayLoad;
	private String payload;
	private boolean asynchronous;
	private NeotysCoapHandler handler;

	private Optional<String> tracemode;
	private Optional<String> trustedkeyLocation;
	private Optional<String> trustedkeyPassword;
	private Optional<String> keyStoreLocation;
	private Optional<String> keyStorePassword;
	private Optional<String> keyStoreAlias;
	private Optional<String> keyStoreAliasPassword;
	private Context context;
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
	private static final String TRUE="true";
	private  static final String GET="GET";
	private  static final String POST="POST";
	private  static final String PUT="PUT";
	private  static final String DELETE="DELETE";

	private  static final String OBSERVE="OBSERVE";

	public CoapNeotysClient(Context context,CoapContext coapContext, Optional<String> tracemode, Optional<LinkedBlockingQueue<CoapResponse>> queue) throws NeoLoadCoapExeption {
		super(coapContext.getuRl());
		this.context=context;
		this.uRl=coapContext.getuRl();
		this.tracemode=tracemode;
		this.method=coapContext.getMethod();
		this.accept=getType(coapContext.getAccept());
		if(coapContext.getFormatOfpayLoad().isPresent())
		{
			//----case of post or put
			this.formatOfpayLoad=getType(coapContext.getFormatOfpayLoad());
			if(coapContext.getPayload().isPresent())
				this.payload=coapContext.getPayload().get();
			else
				this.payload=null;
		}

		if(coapContext.getTrustedkeyLocation().isPresent())
		{
			this.trustedkeyLocation=coapContext.getTrustedkeyLocation();
			this.trustedkeyPassword=coapContext.getTrustedkeyPassword();
			this.keyStoreLocation=coapContext.getKeyStoreLocation();
			this.keyStorePassword=coapContext.getKeyStorePassword();
			this.keyStoreAlias=coapContext.getKeyStoreAlias();
			this.keyStoreAliasPassword=coapContext.getKeyStoreAliasPassword();

			//--define the dtls endpoint
			DTLSConnector dtlsConnector = createDTLSConnector( keyStoreLocation.get(),keyStorePassword.get(),keyStoreAlias.get(),keyStoreAliasPassword.get(),trustedkeyLocation.get() ,trustedkeyPassword.get());
			setEndpoint(new CoapEndpoint(dtlsConnector, NetworkConfig.getStandard())).setTimeout(0).useCONs();
		}


		asynchronous=coapContext.isAsynchronous();

		if(asynchronous)
			handler=new NeotysCoapHandler(queue.get());
	}



	private  Certificate[] loadTrustStore(String trustkeystorelocation,String trustedPassword) throws Exception {
		// load client key store
		KeyStore trustStore = KeyStore.getInstance("JKS");
		InputStream in = new FileInputStream(trustkeystorelocation);
		trustStore.load(in, trustedPassword.toCharArray());

		// load trustStore containing Artik Verisign intermediary
		TrustManagerFactory trustMgrFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		trustMgrFactory.init(trustStore);
		TrustManager trustManagers[] = trustMgrFactory.getTrustManagers();
		X509TrustManager defaultTrustManager = null;
		traceInfo("trustmanager laoded "+trustMgrFactory.toString());

		for (TrustManager trustManager : trustManagers) {
			if (trustManager instanceof X509TrustManager) {
				defaultTrustManager = (X509TrustManager) trustManager;
			}
		}

		return (defaultTrustManager == null) ? null : defaultTrustManager.getAcceptedIssuers();
	}


	private  DTLSConnector createDTLSConnector(String keystorelocation,String keyStorePassword,String keystorealias,String keyStoreAliasPassword,String trustlocation,String trustpassword) throws NeoLoadCoapExeption {
		DTLSConnector dtlsConnector = null;

		try {
			// load Java trust store
			traceInfo("loading truststore "+trustlocation);
			Certificate[] trustedCertificates = loadTrustStore(trustlocation,trustpassword);

			// load client key store
			KeyStore keyStore = KeyStore.getInstance("JKS");
			InputStream in = new FileInputStream(keystorelocation);
			keyStore.load(in, keyStorePassword.toCharArray());

			// Build DTLS config
			traceInfo("Building DTLS config ");

			DtlsConnectorConfig.Builder builder = new DtlsConnectorConfig.Builder(new InetSocketAddress(0));
			builder.setIdentity((PrivateKey) keyStore.getKey(keystorealias, keyStoreAliasPassword.toCharArray()),
					keyStore.getCertificateChain(keystorealias), false);
			builder.setTrustStore(trustedCertificates);

			// Create DTLS endpoint
			traceInfo("Building DTLS endpoint");

			dtlsConnector = new DTLSConnector(builder.build());
		} catch (Exception e) {
			traceInfo(e.getMessage());
			throw new NeoLoadCoapExeption(e.getMessage());
		}

		return dtlsConnector;
	}


	private  void traceInfo(String log)
	{
		if(this.tracemode.isPresent())
		{
			if(this.tracemode.get().toLowerCase().equals(TRUE))
			{
				this.context.getLogger().info(log);
			}
		}
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
			if(accept !=-1)
				return super.get(accept);
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
		if(accept !=-1)
			 super.get(handler, accept);
		else
			super.get(handler);
		
	}
	public int getPayLoadFormat()
	{
		return formatOfpayLoad;
	}
	
	public int getAccept()
	{
		return accept;
	}
	
	private CoapResponse postCoapResponse()
	{
		if(!asynchronous)
		{
			traceInfo("Sending post request with payload "+payload);

			if(accept !=-1)
				return super.post(payload, formatOfpayLoad, accept);
			else
				return super.post(payload, formatOfpayLoad);
		}
		else
		{
			return null;
		}
		
	}
	
	private void postAsyncCoapResponse()
	{
		traceInfo("Sending post request with payload "+payload);

		if(accept !=-1)
			 super.post(handler, payload, formatOfpayLoad, accept);
		else
			 super.post(handler, payload, formatOfpayLoad);
	}
	
	private CoapResponse deleteCoapResponse()
	{
		traceInfo("Sending delete request ");

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
		traceInfo("Sending delete request ");

		super.delete(handler);
		
	}
	
	private CoapResponse putCoapResponse()
	{
		traceInfo("Sending put request with payload "+payload);

		if(!asynchronous)
		{
			return super.put(payload, formatOfpayLoad);
		}
		else
		{
			return null;
		}
		
	}
	
	private void putAsyncCoapResponse()
	{
		traceInfo("Sending put request with payload "+payload);

		super.put(handler, payload, formatOfpayLoad);
	}
	
	public String send()
	{
		CoapResponse response = null;
		String result = null;
		
		if(!asynchronous)
		{
			
			switch(method)
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
			switch(method)
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
			result="Asynchronous method send "+ method +" on the url :"+ uRl;
		}
		return result;

	}

	private void observe()
	{
		if(accept !=-1)
			super.observe(handler, accept);
		else
			super.observe(handler);
	}
	
	private void observeandwait()
	{
		if(accept !=-1)
			super.observeAndWait(handler, accept);
		else
			super.observeAndWait(handler);
	}
	
	 public static int getType(Optional<String> type)
	 {
		if(type.isPresent())
		{
			switch (type.get().toUpperCase()) {
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
		else
			return -1;
	 }
}
