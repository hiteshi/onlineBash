package com.neev.onlinebash.web;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class GetCommandServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws java.io.IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws IOException {

		HttpSession session = req.getSession(true);
		ArrayList<String> cmdList = (ArrayList<String>) session.getAttribute("commandhistory");
		int count = (int) session.getAttribute("count");
		
		String keyEvent = req.getParameter("keyText");
		
		if(keyEvent.equals("up")){
			
			if ((count > 1)) {
				session.removeAttribute("count");
				session.setAttribute(Constants.count, (count - 1));
				
				count--;
			}else {
				if(cmdList.size() > 1) {
					count = 1;
				}
			}
		}else if(keyEvent.equals("down")) {

			if(count < (cmdList.size()-1)){
				
				session.removeAttribute("count");
				session.setAttribute(Constants.count, (count + 1));
				
				count++;
			}else{
				count=0;
			}
		}
		
		res.setContentType("application/json"); 
	    res.setCharacterEncoding("UTF-8");
		res.getWriter().println(cmdList.get(count));
	}
}