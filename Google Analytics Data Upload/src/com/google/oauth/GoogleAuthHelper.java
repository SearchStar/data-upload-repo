package com.google.oauth;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.google.analytics.AnalyticsProfile;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.googlecode.objectify.cmd.LoadType;

import static com.google.service.OfyService.ofy;

public final class GoogleAuthHelper {
	
	String CALLBACK_URI;
	String CLIENT_ID;
	String CLIENT_SECRET;
	
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final JacksonFactory JSON_FACTORY = JacksonFactory
			.getDefaultInstance();
	private static final Collection<String> SCOPE = Arrays
			.asList("https://www.googleapis.com/auth/userinfo.profile;https://www.googleapis.com/auth/userinfo.email;https://www.googleapis.com/auth/analytics.edit"
					.split(";"));
	
	private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo";

	private final GoogleAuthorizationCodeFlow flow;

	private String stateToken;

	/**
	 * Constructor initializes the Google Authorization Code Flow with CLIENT
	 * ID, SECRET, and SCOPE
	 * @return
	 * @param
	 * @throws
	 */
	public GoogleAuthHelper() {
		
		LoadType<ClientCredentials> credentials = ofy().load().type(ClientCredentials.class);
		
		for(ClientCredentials credential : credentials) {
			// static ClientCredentials credentials = new ClientCredentials();
			CALLBACK_URI = credential.getCallBackUri();
			CLIENT_ID = credential.getclientId();
			CLIENT_SECRET = credential.getClientSecret();
		}
		
		flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT,
				JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, SCOPE).setAccessType(
				"offline").build();
		generateStateToken();
	}

	/**
	 * 
	 * @return
	 * @param
	 * @throws
	 */
	public void addUser(Profile user) throws JSONException {
		ofy().save().entity(user).now();
	}

	/**
	 * Builds a login URL based on client ID, secret, callback URI, and scope
	 * @return
	 * @param
	 * @throws
	 */
	public String buildLoginUrl() {
		final GoogleAuthorizationCodeRequestUrl url = flow
				.newAuthorizationUrl();
		return url.setRedirectUri(CALLBACK_URI).setState(stateToken).build();
	}

	/**
	 * Generates a secure state token
	 * @return
	 * @param
	 * @throws
	 */
	private void generateStateToken() {
		SecureRandom sr1 = new SecureRandom();
		stateToken = "google;" + sr1.nextInt();
	}

	/**
	 * 
	 * @return
	 * @param
	 * @throws
	 */
	public String getAccessCookie(final String authCode) throws IOException,
			JSONException {
		Credential credential = getUsercredential(authCode);
		String accessToken = credential.getAccessToken();
		System.out.println(accessToken);
		return accessToken;
	}

	/**
	 * 
	 * @return
	 * @param
	 * @throws
	 */
	public String getJsonName(String value, JSONObject json)
			throws JSONException {
		String jsonValue = (String) json.get(value);
		return jsonValue;
	}

	/**
	 * 
	 * @return
	 * @param
	 * @throws
	 */
	public String getRefreshToken(Credential credential) throws IOException,
			JSONException {
		String refreshToken = credential.getRefreshToken();
		System.out.println(refreshToken);
		return refreshToken;
	}

	/**
	 * Accessor for state token
	 * @return
	 * @param
	 * @throws
	 */
	public String getStateToken() {
		return stateToken;
	}

	/**
	 * Expects an Authentication Code, and makes an authenticated request for
	 * the user's profile information
	 * 
	 * @return JSON formatted user profile information
	 * @param authCode
	 *            authentication code provided by google
	 * @throws JSONException
	 */
	public Credential getUsercredential(final String authCode)
			throws IOException, JSONException {
		final GoogleTokenResponse response = flow.newTokenRequest(authCode)
				.setRedirectUri(CALLBACK_URI).execute();
		final Credential credential = flow.createAndStoreCredential(response,
				null);
		return credential;
	}
	
	/**
	 * 
	 * @return
	 * @param
	 * @throws
	 */
	public Credential getCredentialRefTkn(String refreshToken){
	Credential credential = createCredentialWithRefreshToken(HTTP_TRANSPORT,
			JSON_FACTORY,
			new TokenResponse().setRefreshToken(refreshToken), CLIENT_ID,
			CLIENT_SECRET);
	return credential;
	}
	
	/**
	 * 
	 * @return
	 * @param
	 * @throws
	 */
	public Credential getUsercredentialwithAccessToken(String accessToken) {
		return new GoogleCredential.Builder().setTransport(HTTP_TRANSPORT)
				.setJsonFactory(new JacksonFactory())
				.setClientSecrets(CLIENT_ID, CLIENT_SECRET).build()
				.setAccessToken(accessToken);
	}
	
	/**
	 * 
	 * @return
	 * @param
	 * @throws
	 */
	public static GoogleCredential createCredentialWithRefreshToken(
			HttpTransport transport, JacksonFactory jsonFactory,
			TokenResponse tokenResponse, String clientId, String clientSecret) {
		return new GoogleCredential.Builder().setTransport(HTTP_TRANSPORT)
				.setJsonFactory(new JacksonFactory())
				.setClientSecrets(clientId, clientSecret).build()
				.setFromTokenResponse(tokenResponse);
	}

	/**
	 * 
	 * @return
	 * @param
	 * @throws
	 */
	public String getUserInfoJson(Credential credential) throws IOException,
			JSONException {
		final HttpRequestFactory requestFactory = HTTP_TRANSPORT
				.createRequestFactory(credential);
		final GenericUrl url = new GenericUrl(USER_INFO_URL);
		final HttpRequest request = requestFactory.buildGetRequest(url);
		request.getHeaders().setContentType("application/json");
		String jsonIdentity = request.execute().parseAsString();
		return jsonIdentity;
	}

	/**
	 * 
	 * @return
	 * @param
	 * @throws
	 */
	public String getUserInfoJson(final String authCode) throws IOException,
			JSONException {

		Credential credential = getUsercredential(authCode);

		final HttpRequestFactory requestFactory = HTTP_TRANSPORT
				.createRequestFactory(credential);
		// Make an authenticated request
		final GenericUrl url = new GenericUrl(USER_INFO_URL);
		final HttpRequest request = requestFactory.buildGetRequest(url);
		request.getHeaders().setContentType("application/json");
		String jsonIdentity = request.execute().parseAsString();
		
		return jsonIdentity;

	}
}