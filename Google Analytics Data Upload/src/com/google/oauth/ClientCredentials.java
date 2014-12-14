package com.google.oauth;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class ClientCredentials {

	// private static final String callback_uri = "http://localhost:8888/add.jsp";
	// private static final String client_id = "443326352718-rfot1orr5871mnketgptf1kojv39p38k.apps.googleusercontent.com";
	// private static final String client_secret = "FueWXE10CTyyLOgnsuAd-1ZF";
	
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
	
	/*public ClientCredentials(){
		this.callback_uri = "http://localhost:8888/add.jsp";
		this.client_id = "443326352718-rfot1orr5871mnketgptf1kojv39p38k.apps.googleusercontent.com";
		this.client_secret = "FueWXE10CTyyLOgnsuAd-1ZF";
	}*/
	
	public ClientCredentials() {
	}

	/**
	 * 
	 * @return
	 * @param
	 */
	public String getCallBackUri() {
		return this.callback_uri;
	}

	/**
	 * get the client id
	 * 
	 * @return
	 * @param
	 */
	public String getclientId() {
		return this.client_id;
	}

	/**
	 * 
	 * @return
	 * @param
	 */
	public String getClientSecret() {
		return this.client_secret;
	}
	
	/**
	 * 
	 * @return
	 * @param
	 */
	public void setCallBackUri(String callBackUri) {
		 this.callback_uri = callBackUri;;
	}

	/**
	 * get the client id
	 * 
	 * @return
	 * @param
	 */
	public void setclientId(String clientId) {
		this.client_id = clientId;
	}

	/**
	 * 
	 * @return
	 * @param
	 */
	public void setClientSecret(String clientSecret) {
		this.client_secret = clientSecret;
	}
}
