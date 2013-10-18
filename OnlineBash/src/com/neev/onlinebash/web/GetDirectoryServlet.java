package com.neev.onlinebash.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class GetDirectoryServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse res)
		      throws ServletException, java.io.IOException {
	    doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, java.io.IOException {
		
		HttpSession session = req.getSession(true);
		String curDir = (String) session.getAttribute("currentdir");
		String userId = (String) session.getAttribute("userid");
		String workBase = (String) session.getAttribute("workbase");

		String tabText = req.getParameter("tabEvent");
		
		String output = "";
		String cmd = "";
		
		Bash bash = new Bash();
		
		try{
			if(!tabText.equals("")){
				
				cmd = "ls | grep "+tabText;
				
				output = bash.executeTabCommand(curDir, workBase, userId, cmd);
				
			}else{
				cmd = "ls";	
				output = bash.executeTabCommand(curDir, workBase, userId, cmd);
			}
		} catch(IOException e){
			e.printStackTrace();
		} catch(InterruptedException e){
			e.printStackTrace();
		}

		res.setContentType("application/json");
		res.setCharacterEncoding("UTF-8");
		res.getWriter().println(output);
	}
}
