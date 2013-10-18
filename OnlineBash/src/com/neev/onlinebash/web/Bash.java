package com.neev.onlinebash.web;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

class Bash {

	public ArrayList<Object> executeCommand(String curDir, String workBase, String userId, String command)
			throws IOException, InterruptedException {

		String json = "[";
		String line = "";
		String temp = "";
		int status = -100;
		int len = workBase.length()+userId.length();
		
		ArrayList<Object> ret = new ArrayList<Object>();
		
		File file = new File(curDir);

		try {
			Process process = Runtime.getRuntime().exec("/bin/bash", null, file);
			
			if (process != null) {
			
				PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(process.getOutputStream())), true);
			
				out.println(command);
				
				out.println("exit");
				out.flush();
				out.close();
				
				status = process.waitFor();
				
				if(status == 0){
					
					BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
				
					while ((line = in.readLine()) != null) {
					
						if(command.equals("pwd")) {
							line = "/guestUser"+ curDir.substring(len);
						}

						json += "\"" + line + "\",";
					}
					
					if(command.startsWith("cd ")){
						if(command.endsWith("..")){
							if(curDir.length() == len){
								temp = "";
							}else{
								temp = "***"+(curDir.substring(0,curDir.lastIndexOf("/"))).substring(len);
							}
						}
						else if(command.length() > 3){
							temp = "***"+curDir.substring(len)+"/"+command.substring(3).trim();
						}
						json += "\"" + temp + "\",";
					}
					json += "]";
					
					in.close();
					
				} else{
					
					BufferedReader err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
					
					while ((line = err.readLine()) != null){
						if(line.startsWith("/bin/bash")){
							
							line = line.substring(line.indexOf(":", line.indexOf(":")+1)+2);
							System.out.println("line: "+line);
							if(line.startsWith("cd")){
								line = "bash: "+line;
							}
						}
						json += "\"" + line + "\",";
					}
					
					if(command.startsWith("cd ")){
						if(command.endsWith("..")){
							if(curDir.length() == len){
								temp = "";
							}else{
								temp = "***"+(curDir.substring(0,curDir.lastIndexOf("/"))).substring(len);
							}
						}
						else if(command.length() > 3){
							temp = "***"+curDir.substring(len);
						}
						json += "\"" + temp + "\",";
					}
					json += "]";
					
					err.close();
				}
			}	
			process.destroy();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ret.add(json);
		ret.add(status);
		
		return ret;
	}
	
	public String whoAmI() {

		String line = "";
		
		try {
			Process process = Runtime.getRuntime().exec("whoami");
			
			if (process != null) {

				BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
				line = in.readLine();
				
				process.waitFor();
			}
			process.destroy();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return line;
	}

	public void createUserHome(String userId, String who) {

		File file = new File("/home/"+who+"/OnlineBashWorkspace");
		
		try {
			Process process = Runtime.getRuntime().exec("/bin/bash", null, file);
			
			if (process != null) {

				PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(process.getOutputStream())), true);

				out.println("mkdir " + userId);
				
				out.println("exit");
				out.flush();
				out.close();
				
				process.waitFor();
			}
			process.destroy();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void removeUserHome(String userId, String workBase) {

		File file = new File(workBase);
		
		try {
			Process process = Runtime.getRuntime().exec("/bin/bash", null, file);
			
			if (process != null) {

				PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(process.getOutputStream())), true);

				out.println("rm -r " + userId);
				
				out.println("exit");
				out.flush();
				out.close();
				
				process.waitFor();
			}
			process.destroy();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public String executeTabCommand(String curDir, String workBase, String userId, String command)
			throws IOException, InterruptedException {

		String json = "[";
		String line = "";
		int status = -100;
		
		File file = new File(curDir);
		
		try {
			Process process = Runtime.getRuntime().exec("/bin/bash", null, file);
			
			if (process != null) {
			
				PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(process.getOutputStream())), true);
			
				out.println(command);
				
				out.println("exit");
				out.flush();
				out.close();
				
				status = process.waitFor();
				
				if(status == 0){
					
					BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
				
					while ((line = in.readLine()) != null){
						
						json += "\"" + line + "\",";
					}
					json += "]";
					
					in.close();
				}
			}
			process.destroy();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}			
		System.out.println(json);
		
		return json;
	}
}
