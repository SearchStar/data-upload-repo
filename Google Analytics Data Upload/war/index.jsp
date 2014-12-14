<%@ page import="pageNumber.*, java.util.*, java.io.*, com.google.upload.*,
com.google.oauth.GoogleAuthHelper,
com.google.analytics.AnalyticsProfile,
com.google.oauth.GoogleAuthHelper,
com.google.oauth.Profile,
com.google.api.client.auth.oauth2.Credential,
com.google.appengine.labs.repackaged.org.json.JSONException,
com.google.appengine.labs.repackaged.org.json.JSONObject"
%>
<%@ page import="static com.google.service.OfyService.ofy"  %>
<% final GoogleAuthHelper helper = new GoogleAuthHelper();  %>

<!DOCTYPE html>
<html>
<head>
</head>
<body>         
	<%
	Credential credential = null;
		boolean accessCookieStatus = false;
		Cookie cookie = null;
		Cookie[] cookies = null;
		cookies = request.getCookies();
		if(cookies != null){
			for (int i = 0; i < cookies.length; i++){
				cookie = cookies[i];
				if((cookie.getName( )).compareTo("gaccesstoken") == 0 ){
					accessCookieStatus = true;
					break;
				}
				
			}
		}
		
	if (accessCookieStatus != true && (request.getParameter("code") == null || request.getParameter("state") == null)) {			
		out.println("<a href='" + helper.buildLoginUrl() + "'>log in with google</a>");				
		session.setAttribute("state", helper.getStateToken());
		
	} else if (accessCookieStatus == true || (request.getParameter("code") != null && request.getParameter("state") != null && request.getParameter("state").equals(session.getAttribute("state")))) {	
		if (accessCookieStatus == true) {
			credential = helper.getUsercredentialwithAccessToken(cookie.getValue());
		}	
		else {
			session.removeAttribute("state");			
			credential = helper.getUsercredential(request.getParameter("code"));
		}
						
		String userDetails = helper.getUserInfoJson(credential);
		System.out.println(userDetails);
						
		JSONObject jsonObj = new JSONObject(userDetails);
		Profile GoogleProfile = new Profile(jsonObj);
						
		String googleId = GoogleProfile.getGoogleId();
						
		Profile user = null;
						
		user = ofy().load().type(Profile.class).filter("googleId", googleId).first().now();
		String refreshtoken = null;
						
		if(user == null){			
			refreshtoken = credential.getRefreshToken();
			GoogleProfile.setRefreshToken(refreshtoken);
			ofy().save().entity(GoogleProfile).now();
		}
		
		Cookie accessCookie = new Cookie("gaccesstoken", credential.getAccessToken());
		accessCookie.setMaxAge(60*60*1);
		response.addCookie(accessCookie);
	%>
    <header class="wrapper clearfix"></header>

	<form name="form1" action="/addaccount" method="post" >
    
        <h1>Add a Bing Account</h1>        
        Bing Account ID: <span class="info" style="font-style: italic">2613461..</span> <input name="bing_account_id" type="text"></br>        
        Bing Account Name: <span class="info" style="font-style: italic">Aquability..</span> <input name="bing_account_name" type="text"></br>
        Bing Api Username: <span class="info" style="font-style: italic">Username..</span> <input name="bing_api_name" type="text"></br>
        Bing Api Password: <span class="info" style="font-style: italic">Password..</span> <input name="bing_password" type="text"></br>
        Bing Developer Token: <span class="info" style="font-style: italic">6shen49cne..</span> <input name="bing_developer_token" type="text"></br> 
        Bing CustomerId: <span class="info" style="font-style: italic">878599..</span> <input name="bing_customerid" type="text"></br>        
        GA Account ID: <span class="info" style="font-style: italic">413414...</span> <input name="ga_account_id" type="text"></br>        
        GA Web Property: <span class="info" style="font-style: italic">UA-454361....</span> <input name="ga_web_property"type="text"></br>        
        GA Custom Data Source ID: <span class="info" style="font-style: italic">W-kEjp8aQfOTYIu...</span> <input name="ga_custom_data_source_id" type="text"></br>        
        Account Manager Email: <span class="info"></span> <input name="account_manager_email" type="text"></br>        
        Source Format: <span class="info" style="font-style: italic">Bing, BING, bing</span> <input name="source_format" type="text"></br>       
        Medium Format: <span class="info" style="font-style: italic">CPC, ppc, cpc, Cpc</span> <input name="medium_format" type="text"></br>
        Account Manager Name: <span class="info"></span> <input name="account_manager_name" type="text"></br>  
        Effective From Date: <span class="info"></span> <select name="effective_from_date"></br>
            <option value="Yesterday">Yesterday</option>
            <option value="LastSevenDays">Last Seven Days</option>
            <option value="LastFourWeeks">Last Four Weeks</option>
            <option value="LastThreeMonths">Last Three Months</option>
            <option value="LastSixMonths">Last Six Months</option>

            <option value="LastYear">Last Year</option>
        </select>

		<input type="hidden" name="urlCode" id="urlCode" value="<%=request.getParameter("code")%>">
		<input type="hidden" name="googleId" id="googleId" value="<%=GoogleProfile.getGoogleId()%>">
		<input type="submit" name="submit" value="Submit Form">
	</form>			
	<%}	%>	
</body>
</html>