package com.google.oauth;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Entity
public class Profile {
	String email;
	String family_name;
	String gender;
	String given_name;
	@Index
	String googleId;
	@Id
	Long id;
	String link;
	String locale;
	String name;
	String picture;
	String refToken;

	public Profile() {

	}

	public Profile(JSONObject jsonObj) throws JSONException {

		this.name = getJsonValue("name", jsonObj);
		this.googleId = getJsonValue("id", jsonObj);
		this.given_name = getJsonValue("given_name", jsonObj);
		this.family_name = getJsonValue("family_name", jsonObj);
		this.link = getJsonValue("link", jsonObj);
		this.picture = getJsonValue("picture", jsonObj);
		this.gender = getJsonValue("gender", jsonObj);
		this.locale = getJsonValue("locale", jsonObj);
		this.email = getJsonValue("email", jsonObj);
	}

	/**
	 * 
	 * @return
	 * @param
	 * @throws
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * 
	 * @return
	 * @param
	 * @throws
	 */
	public String getFamily_name() {
		return this.family_name;
	}

	/**
	 * 
	 * @return
	 * @param
	 * @throws
	 */
	public String getGender() {
		return this.gender;
	}

	/**
	 * 
	 * @return
	 * @param
	 * @throws
	 */
	public String getGiven_name() {
		return this.given_name;
	}

	/**
	 * 
	 * @return
	 * @param
	 * @throws
	 */
	public String getGoogleId() {
		return this.googleId;
	}

	/**
	 * 
	 * @return
	 * @param
	 * @throws
	 */
	public String getJsonValue(String value, JSONObject json)
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
	public String getLink() {
		return this.link;
	}

	/**
	 * 
	 * @return
	 * @param
	 * @throws
	 */
	public String getLocale() {
		return this.locale;
	}

	/**
	 * 
	 * @return
	 * @param
	 * @throws
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 
	 * @return
	 * @param
	 * @throws
	 */
	public String getPicture() {
		return this.picture;
	}

	/**
	 * 
	 * @return
	 * @param
	 * @throws
	 */
	public String getRefToken() {
		return this.refToken;
	}

	/**
	 * 
	 * @return
	 * @param
	 * @throws
	 */
	public void setRefreshToken(String refreshtoken) {
		this.refToken = refreshtoken;
	}
}
