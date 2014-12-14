package com.google.oauth;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;

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
	 * adds users google profile to the data store
	 * @param user Profile
	 * @throws JSONException
	 */
	public void addUser(Profile profile) throws JSONException {
		ofy().save().entity(profile).now();
	}

	/**
	 * Builds a login URL based on client ID, secret, callback URI, and scope
	 * @return login url
	 */
	public String buildLoginUrl() {
		final GoogleAuthorizationCodeRequestUrl url = flow
				.newAuthorizationUrl();
		return url.setRedirectUri(CALLBACK_URI).setState(stateToken).build();
	}

	/**
	 * Generates a secure state token
	 */
	private void generateStateToken() {
		SecureRandom sr1 = new SecureRandom();
		stateToken = "google;" + sr1.nextInt();
	}

	/**
	 * returns a valid google access token, valid for 60 mins
	 * @return valid google access token
	 * @param google auth code from athorization flow
	 * @throws IOException
	 * @throws JSONException
	 */
	public String getAccessCookie(final String authCode) throws IOException,
			JSONException {
		Credential credential = getUsercredential(authCode);
		String accessToken = credential.getAccessToken();
		System.out.println(accessToken);
		return accessToken;
	}

	/**
	 * @param name of field from which value should be retrieved
	 * @param json object
	 * @return field value
	 * @throws JSONException
	 */
	public String getJsonName(String value, JSONObject json)
			throws JSONException {
		String jsonValue = (String) json.get(value);
		return jsonValue;
	}

	/**
	 * on first submision you may request a refresh token
	 * @return refresh token
	 * @param valid google credential
	 * @throws IOException
	 * @throws JSONException
	 */
	public String getRefreshToken(Credential credential) throws IOException,
			JSONException {
		String refreshToken = credential.getRefreshToken();
		System.out.println(refreshToken);
		return refreshToken;
	}

	/**
	 * Accessor for state token
	 */
	public String getStateToken() {
		return stateToken;
	}

	/**
	 * Expects an Authentication Code, and makes an authenticated request for the user's profile information
	 * @return JSON formatted user profile information
	 * @param authCodeauthentication code provided by google
	 * @throws JSONException
	 * @throws IOException
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
	 * returns a google credential from a refresh token for accessing api services
	 * @return valid google credential
	 * @param Google refresh token
	 */
	public Credential getCredentialRefTkn(String refreshToken){
	Credential credential = createCredentialWithRefreshToken(HTTP_TRANSPORT,
			JSON_FACTORY,
			new TokenResponse().setRefreshToken(refreshToken), CLIENT_ID,
			CLIENT_SECRET);
	return credential;
	}
	
	/**
	 * returns a google credential from an access token for accessing api services
	 * @return valid google credential
	 * @param google acces token
	 */
	public Credential getUsercredentialwithAccessToken(String accessToken) {
		return new GoogleCredential.Builder().setTransport(HTTP_TRANSPORT)
				.setJsonFactory(new JacksonFactory())
				.setClientSecrets(CLIENT_ID, CLIENT_SECRET).build()
				.setAccessToken(accessToken);
	}
	
	/**
	 * returns a google credential from a refresh token for accessing api services
	 * @return valid google credential
	 * @param HttpTransport
	 * @param JacksonFactory
	 * @param google refresh token
	 * @param client id
	 * @param client secret
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
	 * gets the users google profile with a valid google credntial and returns it in a json object
	 * @return json object containing the users google profile
	 * @param valid Google credential
	 * @throws IOException
	 * @throws JSONException
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
	 * gets the users google profile with a valid google auth code and returns it in a json object
	 * @return json object containing the users google profile
	 * @param Google authorization code
	 * @throws IOException
	 * @throws JSONException
	 */
	public String getUserInfoJson(final String authCode) throws IOException,
			JSONException {
		Credential credential = getUsercredential(authCode);
		final HttpRequestFactory requestFactory = HTTP_TRANSPORT
				.createRequestFactory(credential);
		final GenericUrl url = new GenericUrl(USER_INFO_URL);
		final HttpRequest request = requestFactory.buildGetRequest(url);
		request.getHeaders().setContentType("application/json");
		String jsonIdentity = request.execute().parseAsString();
		
		return jsonIdentity;

	}
}