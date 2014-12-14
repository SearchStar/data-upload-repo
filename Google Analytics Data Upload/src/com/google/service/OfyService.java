package com.google.service;

import com.google.analytics.AnalyticsProfile;
import com.google.oauth.ClientCredentials;
import com.google.oauth.Profile;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

/**
 * Custom Objectify Service that this application should use.
 */
public class OfyService {

	/**
	 * This static block ensure the entity registration.
	 */
	static {
		factory().register(Profile.class);
		factory().register(AnalyticsProfile.class);
		factory().register(ClientCredentials.class);
	}

	/**
	 * Use this static method for getting the Objectify service factory.
	 * 
	 * @return ObjectifyFactory.
	 */
	public static ObjectifyFactory factory() {
		return ObjectifyService.factory();
	}

	/**
	 * Use this static method for getting the Objectify service object in order
	 * to make sure the above static block is executed before using Objectify.
	 * 
	 * @return Objectify service object.
	 */
	public static Objectify ofy() {
		return ObjectifyService.ofy();
	}
}
