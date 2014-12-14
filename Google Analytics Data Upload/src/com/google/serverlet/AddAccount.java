package com.google.serverlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

import java.io.IOException;

import com.google.analytics.AnalyticsProfile;

import static com.google.service.OfyService.ofy;

@SuppressWarnings("serial")
public class AddAccount extends HttpServlet {
	
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

		String bing_account_id = req.getParameter("bing_account_id");
		String bing_account_name = req.getParameter("bing_account_name");
		String bing_api_name = req.getParameter("bing_api_name");
		String bing_password = req.getParameter("bing_password");
		String bing_developer_token = req.getParameter("bing_developer_token");
		String bing_customerid = req.getParameter("bing_customerid");
		String ga_account_id = req.getParameter("ga_account_id");
		String ga_web_property = req.getParameter("ga_web_property");
		String ga_custom_data_source_id = req.getParameter("ga_custom_data_source_id");
		String account_manager_email = req.getParameter("account_manager_email");
		String source_format = req.getParameter("source_format");
		String medium_format = req.getParameter("medium_format");
		String account_manager_name = req.getParameter("account_manager_name");
		String effective_from_date = req.getParameter("effective_from_date");
		String googleId = req.getParameter("googleId");
		
		System.out.println(bing_account_id);

		AnalyticsProfile analyticsProfile = new AnalyticsProfile(googleId,
				ga_account_id, bing_account_id, bing_account_name,
				bing_api_name, bing_password, bing_developer_token,
				bing_customerid, ga_web_property, ga_custom_data_source_id,
				account_manager_email, source_format, medium_format,
				account_manager_name, effective_from_date);

		ofy().save().entity(analyticsProfile).now();

		resp.sendRedirect("/index.jsp?status=data-added");

	}
}
