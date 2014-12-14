package com.google.serverlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

import java.io.IOException;

import com.google.oauth.ClientCredentials;

import static com.google.service.OfyService.ofy;

public class NewSetup extends HttpServlet {
	
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
		
			String clientId = req.getParameter("client_id");
			String clientSecret = req.getParameter("client_secret");
			String redirectUri = req.getParameter("redirect_uri");

			ClientCredentials newClient = new ClientCredentials(clientSecret, clientId, redirectUri);
			
			ofy().save().entity(newClient).now();
			
			resp.sendRedirect("/index.jsp");
	}
}
