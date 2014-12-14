package com.google.analytics;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class AnalyticsProfile {
	String account_manager_email;
	String account_manager_name;
	String bing_account_id;
	String bing_account_name;
	String bing_api_name;
	String bing_password;
	String bing_developer_token;
	String bing_customerid;
	String effective_from_date;
	String ga_account_id;
	String ga_custom_data_source_id;
	String ga_web_property;
	@Index
	String googleId;
	@Id
	Long id;
	String medium_format;
	String source_format;

	public AnalyticsProfile() {
	}

	public AnalyticsProfile(String googleId, String ga_account_id,
			String bing_account_id, String bing_account_name,
			String bing_api_name, String bing_password,
			String bing_developer_token, String bing_customerid,
			String ga_web_property, String ga_custom_data_source_id,
			String account_manager_email, String source_format,
			String medium_format, String account_manager_name,
			String effective_from_date) {

		this.googleId = googleId;
		this.ga_account_id = ga_account_id;
		this.bing_account_id = bing_account_id;
		this.bing_account_name = bing_account_name;
		this.bing_api_name = bing_api_name;
		this.bing_password = bing_password;
		this.bing_developer_token = bing_developer_token;
		this.bing_customerid = bing_customerid;
		this.ga_web_property = ga_web_property;
		this.ga_custom_data_source_id = ga_custom_data_source_id;
		this.account_manager_email = account_manager_email;
		this.source_format = source_format;
		this.medium_format = medium_format;
		this.account_manager_name = account_manager_name;
	}

	/**
	 * 
	 * @return returns the account manager name as a string for this Analytics
	 *         Profile instance.
	 */
	public String getName() {
		return this.account_manager_name;
	}

	/**
	 * 
	 * @return returns the account manager name as a string for this Analytics
	 *         Profile instance.
	 */
	public String getAccountManagerEmail() {
		return this.account_manager_name;
	}

	/**
	 * 
	 * @return returns the account manager name as a string for this Analytics
	 *         Profile instance.
	 */
	public String getAccountManagerName() {
		return this.account_manager_email;
	}

	/**
	 * 
	 * @return returns the account manager name as a string for this Analytics
	 *         Profile instance.
	 */
	public String getBingAccountId() {
		return this.bing_account_id;
	}

	/**
	 * 
	 * @return returns the account manager name as a string for this Analytics
	 *         Profile instance.
	 */
	public String getBingAccountName() {
		return this.bing_account_name;
	}

	/**
	 * 
	 * @return returns the bing api username as a string for this Analytics
	 *         Profile instance.
	 */
	public String getBingApiName() {
		return this.bing_api_name;
	}
	
	/**
	 * 
	 * @return returns the bing api password as a string for this Analytics
	 *         Profile instance.
	 */
	public String getBingPassword() {
		return this.bing_password;
	}

	/**
	 * 
	 * @return returns the bing api password as a string for this Analytics
	 *         Profile instance.
	 */
	public String getBingDeveloperToken() {
		return this.bing_developer_token;
	}

	/**
	 * 
	 * @return returns the bing api password as a string for this Analytics
	 *         Profile instance.
	 */
	public String getBingCustomerId() {
		return this.bing_customerid;
	}
	/**
	 * 
	 * @return returns the date that the first cost data was pulled from as a
	 *         string for this Analytics Profile instance.
	 */
	public String getEffectiveFromDate() {
		return this.effective_from_date;
	}

	/**
	 * 
	 * @return returns the Google Analytics Account Id as a string for this
	 *         Analytics Profile instance.
	 */
	public String getGaAccountId() {
		return this.ga_account_id;
	}

	/**
	 * 
	 * @return returns the Google Analytics Custom Source Id as a string for
	 *         this Analytics Profile instance.
	 */
	public String getGaCustomDataSourceId() {
		return this.ga_custom_data_source_id;
	}

	/**
	 * 
	 * @return returns the Google Analytics Web Property (UA-#######-#) as a
	 *         string for this Analytics Profile instance.
	 */
	public String getGaWebProperty() {
		return this.ga_web_property;
	}

	/**
	 * 
	 * @return returns the Google Id as a string for this Analytics Profile
	 *         instance.
	 */
	public String getGoogleId() {
		return this.googleId;
	}

	/**
	 * 
	 * @return returns the id key as a string for this Analytics Profile
	 *         instance.
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * 
	 * @return returns the medium in the format and case expected by Google
	 *         Analytics as a string for this Analytics Profile instance.
	 */
	public String getMediumFormat() {
		return this.medium_format;
	}

	/**
	 * 
	 * @return returns the source in the format and case expected by Google
	 *         Analytics as a string for this Analytics Profile instance.
	 */
	public String getSourceFormat() {
		return this.source_format;
	}

}
