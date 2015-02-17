package tw.com.fivehundred.logout;

import java.rmi.RemoteException;
import java.util.Map;

import javax.servlet.ServletContext;
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
public class LogOut extends ActionSupport {
	@SuppressWarnings("unchecked")
	public String execute() {
		Map session=ActionContext.getContext().getSession();
		ProteusAPI_PortType service;
		String userName=(String) session.get("userName");
		LogMian.checkLogFile(userName);
		String log_Content=userName+","+""+","+""+","+"Logout"+","+"Success"+","+Tools.getTime()+","+"";
		LogMian.writeLog(log_Content, userName);
		session.remove("ready_server");
		session.remove("userName");
		 return "Login";
	}
}