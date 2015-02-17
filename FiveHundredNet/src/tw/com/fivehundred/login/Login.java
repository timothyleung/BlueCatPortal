package tw.com.fivehundred.login;

import java.rmi.RemoteException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;
import javax.xml.rpc.ServiceException;
import javax.xml.ws.RespectBinding;
import javax.xml.ws.Response;

import org.apache.struts2.ServletActionContext;

import tw.com.fivehundred.log.LogMian;
import tw.com.fivehundred.tool.Tools;

import com.bluecatnetworks.proteus.api.client.java.ProteusAPIUtils;
import com.bluecatnetworks.proteus.api.client.java.proxy.ProteusAPI_PortType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class Login extends ActionSupport {
	private String userName;
	private String passWord;
	private String serverIP; 
	@SuppressWarnings("unchecked")
	public String execute() {
		HttpServletRequest requst = ServletActionContext.getRequest();
		ServletContext servletContext = ServletActionContext
				.getServletContext();
		
		Map session=ActionContext.getContext().getSession();
		ProteusAPI_PortType service;
		boolean ans = true;
		String message = "Below field can not blank";
		if (userName != null && passWord != null && serverIP != null) {
			try {
				if (userName.trim().length() == 0) {
					message = message + " Username  ";
					ans = false;
				}
				if (passWord.trim().length() == 0) {
					message = message + " Password  ";
					ans = false;
				}
				if (serverIP.trim().length() == 0) {
					message = message + " Bluecat Address Manager IP ";
					ans = false;
				}
				if (ans) {
					
					HttpServletResponse response = ServletActionContext.getResponse();
					//Cookie cookie = new Cookie("serverIP",serverIP); 
					Tools.addCookie(response,"serverIP",serverIP);	
					//response.addCookie(cookie); 
					
					service = ProteusAPIUtils.connect(serverIP);
					service.login(userName.trim().trim(), passWord.trim()
							.trim());
					HttpServletRequest resqust = ServletActionContext
							.getRequest();
					
					
					
					resqust.setAttribute("whichPage",
							"/WEB-INF/Menu/HomeMenu.jsp");
					message = "";
					requst.setAttribute("erroMessage", message);
					session.put("ready_server",service);
					session.put("userName",userName);
					LogMian.checkLogFile(userName);
					String log_Content=userName+","+""+","+""+","+"Login"+","+"Success"+","+Tools.getTime()+","+"";
					LogMian.writeLog(log_Content, userName);
					return "success";
				} else {
					requst.setAttribute("erroMessage", message);
					return "Login";
				}
			} catch (ServiceException e) {
				message = "Connection Problem: Incorrect server IP address";
				requst.setAttribute("erroMessage", message);
				LogMian.checkLogFile(userName);
				String log_Content=userName+","+""+","+""+","+"Login"+","+"Fail (IP Address Error)"+","+Tools.getTime()+","+"";
				LogMian.writeLog(log_Content, userName);
				return "Login";
			} catch (RemoteException e) {
				message = "Authenication Error: Incorrect username and password";
				requst.setAttribute("erroMessage", message);
				LogMian.checkLogFile(userName);
				String log_Content=userName+","+""+","+""+","+"Login"+","+"Fail"+","+Tools.getTime()+","+"";
				LogMian.writeLog(log_Content, userName);
				return "Login";
			}
		} else {
			message = "";
			requst.setAttribute("erroMessage", message);
			return "Login";
		}
//		 HttpServletRequest resqust = ServletActionContext.getRequest();
//		 resqust.setAttribute("whichPage", "/WEB-INF/Menu/HomeMenu.jsp");
//		 return "success";
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getServerIP() {
		return serverIP;
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

}