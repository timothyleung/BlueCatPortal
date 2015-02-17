package tw.com.fivehundred.back;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.RespectBinding;
import javax.xml.ws.Response;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import tw.com.fivehundred.add.been.OneMacAddress;
import tw.com.fivehundred.log.LogMian;
import tw.com.fivehundred.tool.Tools;

import com.bluecatnetworks.proteus.api.client.java.EntityProperties;
import com.bluecatnetworks.proteus.api.client.java.constants.ObjectProperties;
import com.bluecatnetworks.proteus.api.client.java.constants.ObjectTypes;
import com.bluecatnetworks.proteus.api.client.java.proxy.APIEntity;
import com.bluecatnetworks.proteus.api.client.java.proxy.ProteusAPI_PortType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class Back extends ActionSupport {
	private OneMacAddress oneMacAddress = new OneMacAddress();
	private File fileData;
	private String fileDataFileName;
	private String fileDataContentType;
	private String choose_data, select_config;
	private String erroMessage;

	@SuppressWarnings("unchecked")
	public String execute() {
		boolean jump_login = true;
		ServletContext servletContext = ServletActionContext
				.getServletContext();
		HttpServletRequest requst = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		Map session = ActionContext.getContext().getSession();
		ProteusAPI_PortType service = (ProteusAPI_PortType) session
				.get("ready_server");
		erroMessage = (String) session.get("erroMessage");
		String userName = (String) session.get("userName");
		String log_Content = "";
		if (erroMessage != null) {
			session.remove("erroMessage");
		}
		if (service != null) {
			String choose = requst.getParameter("choose");
			String log = requst.getParameter("log");
			if (choose.equals("AddPage")) {
				if (log.equals("userCancel")) {
					log_Content = userName + "," + "MAC Address" + ","
							+ "" + "," + "Add"
							+ "," + "Fail" + "," + Tools.getTime()
							+ "," + "The User Cancels";
				}
				if (log.equals("checkErro")) {
					log_Content = userName + "," + "MAC Address" + ","
							+ "" + "," + "Add"
							+ "," + "Fail" + ","
							+ Tools.getTime() + "," + "Check The Error¡A The User Cancels";
				}
				if (log.equals("multiCheckErro")) {
					log_Content = userName + "," + "Multi Address" + ","
							+ "" + "," + "Add" + "," + "Fail" + ","
							+ Tools.getTime() + "," + "Multiple Mac Address Check The Error¡A The User Cancels";
				}
				if (log.equals("multiUserCancel")) {
					log_Content = userName + "," + "Multi Address" + ","
							+ "" + "," + "Add" + "," + "Fail" + ","
							+ Tools.getTime() + "," + "The User Cancels";
				}
				if (log.equals("noData")) {
					log_Content = userName + "," + "Multi Address" + ","
							+ "" + "," + "Add" + "," + "Fail" + ","
							+ Tools.getTime() + "," + "Multiple Mac Address Did Not Upload Data¡A The User Cancels";
				}
				LogMian.writeLog(log_Content, userName);
				try {
					response.sendRedirect("/FiveHundredNet/BlueCat/AddPage?choose=OneMACAddressShow");
					session.put("old_data", oneMacAddress);
				} catch (IOException e) {
					e.printStackTrace();
				}
				jump_login = false;
			}
			if (choose.equals("DeletePage")) {
				if (log.equals("noData")) {
					log_Content = userName + "," + "Multi Address" + ","
							+ "" + "," + "Delete" + "," + "Fail" + ","
							+ Tools.getTime() + "," + "Multiple Mac Address Check The Error¡A The User Cancels";
				}
				if (log.equals("ipCheckErro")) {
					log_Content = userName + "," + "IP Address" + ","
							+ "" + "," + "Delete"
							+ "," + "Fail" + ","
							+ Tools.getTime() + "," + "Check The Error¡A The User Cancels";
				}
				if (log.equals("ipUserCancels")) {
					log_Content = userName + "," + "IP Address" + ","
							+ "" + "," + "Delete"
							+ "," + "Fail" + ","
							+ Tools.getTime() + "," + "The User Cancels";
				}
				if (log.equals("macCheckErro")) {
					log_Content = userName + "," + "MAC Address" + ","
							+ "" + "," + "Delete"
							+ "," + "Fail" + ","
							+ Tools.getTime() + "," + "Check The Error¡A The User Cancels";
				}
				if (log.equals("macUserCancels")) {
					log_Content = userName + "," + "MAC Address" + ","
							+ "" + "," + "Delete"
							+ "," + "Fail" + ","
							+ Tools.getTime() + "," + "The User Cancels";
				}

				LogMian.writeLog(log_Content, userName);
				try {
					response.sendRedirect("/FiveHundredNet/BlueCat/DeletePage?choose=DeleteData");
				} catch (IOException e) {
					e.printStackTrace();
				}
				jump_login = false;
			}
		}
		if (erroMessage != null) {
			if (erroMessage.length() > 0) {
				requst.setAttribute("erroMessage", erroMessage);
			}
		}
		if (jump_login) {
			requst.setAttribute("erroMessage", "Please login system");
			return "Login";
		}
		return "home";
	}

	public File getFileData() {
		return fileData;
	}

	public void setFileData(File fileData) {
		this.fileData = fileData;
	}

	public String getFileDataFileName() {
		return fileDataFileName;
	}

	public void setFileDataFileName(String fileDataFileName) {
		this.fileDataFileName = fileDataFileName;
	}

	public String getFileDataContentType() {
		return fileDataContentType;
	}

	public void setFileDataContentType(String fileDataContentType) {
		this.fileDataContentType = fileDataContentType;
	}

	public OneMacAddress getOneMacAddress() {
		return oneMacAddress;
	}

	public void setOneMacAddress(OneMacAddress oneMacAddress) {
		this.oneMacAddress = oneMacAddress;
	}

	public String getChoose_data() {
		return choose_data;
	}

	public void setChoose_data(String choose_data) {
		this.choose_data = choose_data;
	}

	public String getSelect_config() {
		return select_config;
	}

	public void setSelect_config(String select_config) {
		this.select_config = select_config;
	}
	
}