package com.neev.onlinebash.web;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CommandServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse res)
		      throws java.io.IOException {
	    doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws IOException{
		
		HttpSession session = req.getSession(true);
		String curDir = (String) session.getAttribute("currentdir");
		String userId = (String) session.getAttribute("userid");
		String workBase = (String) session.getAttribute("workbase");
		
		String cmd = req.getParameter("commandText").trim();
		
		ArrayList<Object> ret = new ArrayList<Object>();
		ArrayList<String> cmdHistory = (ArrayList<String>) session.getAttribute("commandhistory");
		
		String output = "";
		int status = -100;
		
		if(!cmd.equals("")){
			
			cmdHistory.add(cmd);
			
			session.removeAttribute("commandhistory");
			session.setAttribute("commandhistory", cmdHistory);
			
			session.removeAttribute("count");
			session.setAttribute("count", cmdHistory.size());
		}
		
		if(curDir.equals(workBase+userId) && 
				cmd.startsWith("cd") && cmd.endsWith("..")){
			
			status = 100;
		}else{
		
			Bash bash = new Bash();
		
			try{
				ret = bash.executeCommand(curDir, workBase, userId, cmd); 
			
				output = ret.get(0).toString();
				status = Integer.parseInt(ret.get(1).toString());
			
			} catch(IOException e){
				e.printStackTrace();
			} catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		
		if(status == 0){
			if(cmd.startsWith("cd ")){
				if(cmd.endsWith("..")){
					curDir=curDir.substring(0,curDir.lastIndexOf("/"));
				}
				else if(cmd.length() > 3){
					curDir+="/"+cmd.substring(3).trim();
				}
			}
			session.removeAttribute("currentdir");
			session.setAttribute(Constants.currentDir, curDir);

		}else if(status == 100){
			
			output += "[\"You are already in your home directory. Permission Denied.\",]";
		}
		
		res.setContentType("application/json"); 
	    res.setCharacterEncoding("UTF-8");
	    res.getWriter().println(output);
	}
}
