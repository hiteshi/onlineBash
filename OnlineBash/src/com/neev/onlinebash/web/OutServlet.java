package com.neev.onlinebash.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class OutServlet extends SessionServlet {
	
	public void execute(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		
		try {
			HttpSession session = req.getSession();
			session.invalidate();
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}
}
