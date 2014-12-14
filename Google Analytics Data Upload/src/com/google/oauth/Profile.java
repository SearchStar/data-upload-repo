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
	 * @return users email
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * @return users family name
	 */
	public String getFamily_name() {
		return this.family_name;
	}

	/**
	 * @return users gender
	 */
	public String getGender() {
		return this.gender;
	}

	/**
	 * @return users first name
	 */
	public String getGiven_name() {
		return this.given_name;
	}

	/**
	 * @return users google id
	 */
	public String getGoogleId() {
		return this.googleId;
	}

	/**
	 * @param name of field from which value should be retrieved
	 * @param json object
	 * @return field value
	 * @throws JSONException
	 */
	public String getJsonValue(String value, JSONObject json)
			throws JSONException {
		String jsonValue = (String) json.get(value);
		return jsonValue;
	}

	/**
	 * @return users profile link
	 */
	public String getLink() {
		return this.link;
	}

	/**
	 * @return user location
	 */
	public String getLocale() {
		return this.locale;
	}

	/**
	 * @return users name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return users picture
	 */
	public String getPicture() {
		return this.picture;
	}

	/**
	 * @return users refresh token
	 */
	public String getRefToken() {
		return this.refToken;
	}

	/**
	 * sets the refresh token
	 * @param Refresh Token
	 */
	public void setRefreshToken(String refreshtoken) {
		this.refToken = refreshtoken;
	}
}
