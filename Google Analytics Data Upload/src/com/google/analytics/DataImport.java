package com.google.analytics;

import static com.google.service.OfyService.ofy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.oauth.Profile;

import com.google.oauth.GoogleAuthHelper;
import com.google.oauth.ClientCredentials;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.analytics.*;
import com.google.api.services.analytics.model.Upload;
import com.googlecode.objectify.cmd.LoadType;

public class DataImport {

	private static final String APPLICATION_NAME = "My Test Application";
	private static HttpTransport httpTransport;
	private static final JacksonFactory JSON_FACTORY = JacksonFactory
			.getDefaultInstance();
	
	private static String accountId;
	private static String accountWebProperty;
	private static String accountCustSrc;
	private String refreshToken;
	private String googleId;
	
	private String clientId;
	private String clientSecret;
	
	

	/**
	 * 
	 * @param
	 */
	private static void updateAnalytics(Analytics analytics, String printmyarray){
		InputStream is = new ByteArrayInputStream(printmyarray.getBytes() );
		InputStreamContent mediaContent = new InputStreamContent(
				"application/octet-stream", is);
		mediaContent.setLength(printmyarray.length());
		try {
			Upload upload = analytics
					.management()
					.uploads()
					.uploadData(accountId, accountWebProperty,
							accountCustSrc, mediaContent).execute();
		} catch (IOException e) {
			System.err.println("Upload Failed");
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param
	 */
	public void newUpload(String printmyarray, AnalyticsProfile analytics_profile) {
		setCredentials(analytics_profile);
		
		httpTransport = new NetHttpTransport();
		
		GoogleCredential googleCredential = GoogleAuthHelper.createCredentialWithRefreshToken(httpTransport,
				JSON_FACTORY, new TokenResponse().setRefreshToken(refreshToken), clientId, clientSecret);
		
		Analytics analytics = new Analytics.Builder(httpTransport,
				JSON_FACTORY, googleCredential).setApplicationName(APPLICATION_NAME)
				.build();
		
			updateAnalytics(analytics, printmyarray);

	}
	
	/**
	 * 
	 * @param
	 */
	public void setCredentials(AnalyticsProfile analytics_profile) {
		setAnalyticsCredentials(analytics_profile);
		setClientCredential();
	}
	
	/**
	 * 
	 * @param
	 */
	public void setAnalyticsCredentials(AnalyticsProfile analytics_profile) {
		this.accountId = analytics_profile.getGaAccountId();
		this.accountWebProperty = analytics_profile.getGaWebProperty();
		this.accountCustSrc = analytics_profile.getGaCustomDataSourceId();
		this.googleId = analytics_profile.getGoogleId();

		Profile profile = ofy().load().type(Profile.class).filter("googleId", this.googleId).first().now();
		
		this.refreshToken = profile.getRefToken();
	}
	
	/**
	 * 
	 * @param
	 */
	public void setClientCredential() {
		
		LoadType<ClientCredentials> credentials = ofy().load().type(ClientCredentials.class);
		
		for(ClientCredentials credential : credentials) {
			this.clientId = credential.getclientId();
			this.clientSecret = credential.getClientSecret();
		}
	}
}