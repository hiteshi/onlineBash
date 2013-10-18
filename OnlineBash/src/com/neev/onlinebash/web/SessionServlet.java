package com.neev.onlinebash.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

abstract public class SessionServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		
		this.doPost(req, res);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		
		clearCache(res);
		trackSession(req,res);
		execute(req,res);
	}
	
	private void clearCache(HttpServletResponse servletResponse) {
		servletResponse.addHeader("Pragma","no-cache");
		servletResponse.setHeader("Cache-Control","no-cache,no-store,must-revalidate");
		servletResponse.addHeader("Cache-Control","pre-check=0,post-check=0");
		servletResponse.setDateHeader("Expires",0);
	}
	
	public void trackSession(HttpServletRequest req, HttpServletResponse res) {
		
		HttpSession session = req.getSession();
		String userId = (String) session.getAttribute("userid");
		String workBase = (String) session.getAttribute("workbase");
		
		workBase = workBase.substring(0, workBase.length()-1);
		
		if(userId == null){
			try {
				res.sendRedirect("/OnlineBash/index");
			}catch (Exception ex){
				ex.printStackTrace();
			}
		}else{
			Bash bash = new Bash();
			
			try{
				bash.removeUserHome(userId, workBase); 
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	abstract public void execute(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException;
}
