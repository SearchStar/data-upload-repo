package com.google.upload;

import java.io.IOException;
import javax.servlet.http.*;

import java.util.ArrayList;

import com.google.analytics.AnalyticsProfile;
import com.google.analytics.DataImport;
import com.google.analytics.UploadFormater;
import com.google.bing.KeywordPerformanceReportBuilder;

import com.googlecode.objectify.cmd.LoadType;

import static com.google.service.OfyService.ofy;

@SuppressWarnings("serial")
public class Google_Analytics_Data_UploadServlet extends HttpServlet {
	

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		LoadType<AnalyticsProfile> analytics_profiles = ofy().load().type(AnalyticsProfile.class);
		
		for(AnalyticsProfile analytics_profile : analytics_profiles) {

			String reporturl = null;
	
			try {
				reporturl = new KeywordPerformanceReportBuilder().runReport(analytics_profile);
			} catch (Exception e) {
				e.printStackTrace();
			}
	
			String printmyarray = null;
			try {
				printmyarray = new UploadFormater().unzipAndFormatData(reporturl);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			new DataImport().newUpload(printmyarray, analytics_profile);
		}
	}
}
