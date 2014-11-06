package com.garethahealy.wssecurity.https.cxf.client.config.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;

import com.garethahealy.helloworld.HelloWorldEndpoint;

public class WsInterceptorFactory {

	private WsEndpointConfiguration<HelloWorldEndpoint> config;
	
	public WsInterceptorFactory(WsEndpointConfiguration<HelloWorldEndpoint> config) {
		this.config = config;
	}
	
	public List<Interceptor<? extends Message>> getInInterceptors() {
		List<Interceptor<? extends Message>> inInterceptors = new ArrayList<Interceptor<? extends Message>>();
		if (config.isCxfDebug()) {
			LoggingInInterceptor loggingInInterceptor = new LoggingInInterceptor();
			loggingInInterceptor.setPrettyLogging(true);
			
			inInterceptors.add(loggingInInterceptor);
		}
		
		return inInterceptors;
	}
	
	public List<Interceptor<? extends Message>> getOutInterceptors() {
		List<Interceptor<? extends Message>> outInterceptors = new ArrayList<Interceptor<? extends Message>>();
		if (config.isCxfDebug()) {	
			LoggingOutInterceptor loggingOutInterceptor = new LoggingOutInterceptor();
			loggingOutInterceptor.setPrettyLogging(true);
			
			outInterceptors.add(loggingOutInterceptor);
		}
		
		outInterceptors.add(getWSS4JOutInterceptor());
		
		return outInterceptors;
	}
	
	private WSS4JOutInterceptor getWSS4JOutInterceptor() {
        Map<String, Object> outProps = new HashMap<String, Object>();
        outProps.put("action", "Timestamp Signature");
		outProps.put("signaturePropRefId", "wsCryptoProperties");
		outProps.put("wsCryptoProperties", getWSCryptoProperties());
        outProps.put("signatureUser", config.getCertifactionAlias());
        outProps.put("passwordType", "PasswordText");
        outProps.put("passwordCallbackClass", config.getPasswordCallbackClass());
   
        WSS4JOutInterceptor wss4j = new WSS4JOutInterceptor(outProps);
        return wss4j;
	}
	
	private WSCryptoProperties getWSCryptoProperties() {
		Map<String, String> map = new HashMap<String,String>();
		map.put("org.apache.ws.security.crypto.provider", "org.apache.ws.security.components.crypto.Merlin");
		map.put("org.apache.ws.security.crypto.merlin.keystore.type", "jks");
		map.put("org.apache.ws.security.crypto.merlin.keystore.password", config.getSignatureKeystorePassword());
		map.put("org.apache.ws.security.crypto.merlin.keystore.file", config.getSignatureKeystoreFilename());
		
		return new WSCryptoProperties(map);
	}
}
