package com.google.oauth;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class ClientCredentials {
	
	@Id
	Long id;
	String callback_uri;
	@Index
	String client_id;
	String client_secret;
		
	public ClientCredentials(String clientSecret, String clientId, String callbackUri) {
		this.callback_uri = callbackUri;
		this.client_id = clientId;
		this.client_secret = clientSecret;
	}
	
	public ClientCredentials() {
	}

	/**
	 * @return call back uri
	 */
	public String getCallBackUri() {
		return this.callback_uri;
	}

	/**
	 * @return client id
	 */
	public String getclientId() {
		return this.client_id;
	}

	/**
	 * @return client secret
	 */
	public String getClientSecret() {
		return this.client_secret;
	}
	
	/**
	 * @return call back uri of the application
	 */
	public void setCallBackUri(String callBackUri) {
		 this.callback_uri = callBackUri;;
	}

	/**
	 * @param clientId
	 */
	public void setclientId(String clientId) {
		this.client_id = clientId;
	}

	/**
	 * @param client secret
	 */
	public void setClientSecret(String clientSecret) {
		this.client_secret = clientSecret;
	}
}
