package com.neev.onlinebash.web;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class HomeServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse res)
		      throws java.io.IOException {
	    doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws IOException{
		
		HttpSession session = req.getSession();
		
		String userId = (String) session.getAttribute("userid");
		
		if(userId == null ){
			
				res.addHeader("Pragma", "no-cache");
				res.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
				res.addHeader("Cache-Control", "pre-check=0,post-check=0");
				res.setDateHeader("Expires", 0);
				
				session = req.getSession(true);
				
				double random = Math.random() * (Math.random() * (Math.random() * 1000000000));
				userId = Double.toString(random);
				
				ArrayList<String> cmdHistory=new ArrayList<String>();
				
				int count = 0;
				cmdHistory.add("");
				
				Bash obj = new Bash();
				
				try{
					String who = obj.whoAmI();
					
					obj.createUserHome(userId, who);
					
					String workBase = "/home/"+who+"/OnlineBashWorkspace/"; 
					String curDir = workBase+userId;
				
					session.setAttribute(Constants.userId, userId);
					session.setAttribute(Constants.currentDir, curDir);
					session.setAttribute(Constants.workBase, workBase);
					
					session.setAttribute(Constants.commandHistory, cmdHistory);
					session.setAttribute(Constants.count, count);
				
					RequestDispatcher dispatch = this.getServletContext()
							.getRequestDispatcher("/index");
					dispatch.forward(req, res);
				}catch(Exception e){
					e.printStackTrace();
				}
				
		}else{
			try{
				RequestDispatcher dispatch = this.getServletContext()
						.getRequestDispatcher("/index");
				dispatch.forward(req, res);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
