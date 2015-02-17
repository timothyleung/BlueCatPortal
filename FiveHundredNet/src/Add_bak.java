

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
import tw.com.fivehundred.tool.Tools;

import com.bluecatnetworks.proteus.api.client.java.EntityProperties;
import com.bluecatnetworks.proteus.api.client.java.constants.ObjectProperties;
import com.bluecatnetworks.proteus.api.client.java.constants.ObjectTypes;
import com.bluecatnetworks.proteus.api.client.java.proxy.APIEntity;
import com.bluecatnetworks.proteus.api.client.java.proxy.ProteusAPI_PortType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class Add_bak extends ActionSupport {
	private OneMacAddress oneMacAddress = new OneMacAddress();
	private File fileData;
	private String fileDataFileName;
	private String fileDataContentType;
	private String choose_data, select_config;

	@SuppressWarnings("unchecked")
	public String execute() {
		String message = "";
		boolean jump_login = true;
		ServletContext servletContext = ServletActionContext
				.getServletContext();
		HttpServletRequest requst = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		Map session = ActionContext.getContext().getSession();
		ProteusAPI_PortType service = (ProteusAPI_PortType) session
				.get("ready_server");
		if (service != null) {
			requst.setAttribute("add", "style='background-color:#E7CDFF;'");
			requst.setAttribute("delete", "");
			requst.setAttribute("history", "");
			requst.setAttribute("seach", "");
			requst.setAttribute("whichPage", "/WEB-INF/Menu/LeftMenuForAdd.jsp");
			String choose = requst.getParameter("choose");
			if (choose.equals("AddData")) {
				requst.setAttribute("ContentPage",
						"/WEB-INF/ContentPage/Add/OneMACAddress.jsp");
				requst.setAttribute("oneMac", "style='background-color:#E7CDFF;'");
				requst.setAttribute("multiMac", "");
				jump_login = false;
			}
			if (choose.equals("OneMACAddress")) {
				requst.setAttribute("ContentPage",
						"/WEB-INF/ContentPage/Add/OneMACAddress.jsp");
				requst.setAttribute("oneMac", "style='background-color:#E7CDFF;'");
				requst.setAttribute("multiMac", "");
				jump_login = false;
			}
			if (choose.equals("MultiMACAddress")) {
				requst.setAttribute("ContentPage",
						"/WEB-INF/ContentPage/Add/MultiMACAddress.jsp");
				requst.setAttribute("multiMac", "style='background-color:#E7CDFF;'");
				requst.setAttribute("oneMac", "");
				jump_login = false;
			}
			if (choose.equals("check")) {
				String jump = requst.getParameter("jump");
				session.put("select_config", select_config);
				if (jump.equals("one")) {
					session.put("one_data", oneMacAddress);
					requst.setAttribute("ContentPage",
							"/WEB-INF/ContentPage/Add/OneMACAddressCheck.jsp");
					requst.setAttribute("oneMac", "style='background-color:#E7CDFF;'");
					requst.setAttribute("multiMac", "");
					jump_login = false;
				}
				if (jump.equals("oneAdd")) {
					session.put("one_data", oneMacAddress);
					requst.setAttribute("ContentPage",
							"/WEB-INF/ContentPage/Add/OneMACAddressAdd.jsp");
					requst.setAttribute("oneMac", "style='background-color:#E7CDFF;'");
					requst.setAttribute("multiMac", "");
					jump_login = false;
				}
				if (jump.equals("multi")) {
					session.put("choose_data", choose_data);
					requst.setAttribute("ContentPage",
							"/WEB-INF/ContentPage/Add/MultiMACAddressAdd.jsp");
					requst.setAttribute("multiMac", "style='background-color:#E7CDFF;'");
					requst.setAttribute("oneMac", "");
					jump_login = false;
				}
				if (jump.equals("read")) {
					if (fileData != null) {
						String realpath = ServletActionContext
								.getServletContext().getRealPath("/file/"+session.get("userName")+"/Add/");
						if (fileData != null) {
							if (fileDataFileName.indexOf(".csv") != -1) {
								Date date = new Date();
								SimpleDateFormat format = new SimpleDateFormat(
										"yyyyMMddHHmmss");
								String file_name = format.format(date) + "_"
										+ fileDataFileName;
								File savefile = new File(new File(realpath),
										file_name);
								if (!savefile.getParentFile().exists()) {
									savefile.getParentFile().mkdirs();
								}
								Tools.clean_all_file(realpath);								
								try {
									FileUtils.copyFile(fileData, savefile);
									session.put("file_path",
											savefile.getAbsolutePath());
									requst.setAttribute("ContentPage",
											"/WEB-INF/ContentPage/Add/MultiMACAddressRead.jsp");
								} catch (IOException e) {
									requst.setAttribute("erroMessage", "File Upload Failure");
									requst.setAttribute("ContentPage",
											"/WEB-INF/ContentPage/Add/MultiMACAddress.jsp");
								}
							} else {
								requst.setAttribute("erroMessage",
										"File Extension Failure: Please select csv file");
								requst.setAttribute("ContentPage",
										"/WEB-INF/ContentPage/Add/MultiMACAddress.jsp");
							}
						}
					} else {
						requst.setAttribute("erroMessage", "Please select file");
						requst.setAttribute("ContentPage",
								"/WEB-INF/ContentPage/Add/MultiMACAddress.jsp");
					}
					requst.setAttribute("multiMac", "style='background-color:#E7CDFF;'");
					requst.setAttribute("oneMac", "");
					jump_login = false;
				}
			}
		}
		if (jump_login) {
			requst.setAttribute("erroMessage", "Please login system");
			return "Login";
		} else {
			requst.setAttribute("erroMessage", message);
			return "AddPage";
		}
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