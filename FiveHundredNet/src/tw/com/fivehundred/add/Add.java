package tw.com.fivehundred.add;

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

public class Add extends ActionSupport {
	private OneMacAddress oneMacAddress = new OneMacAddress();
	private File fileData;
	private String fileDataFileName;
	private String fileDataContentType;
	private String choose_data, select_config,select_servers;
	private String erroMessage;
	@SuppressWarnings("unchecked")
	public String execute() {
		System.out.println("IN add/Add.java");
		boolean jump_login = true;
		ServletContext servletContext = ServletActionContext
				.getServletContext();
		HttpServletRequest requst = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		Map session = ActionContext.getContext().getSession();
		ProteusAPI_PortType service = (ProteusAPI_PortType) session
				.get("ready_server");
		erroMessage=(String) session.get("erroMessage");
		if(erroMessage!=null){
			session.remove("erroMessage");
		}
		if (service != null) {
			requst.setAttribute("add", "style='background-color:#E7CDFF;'");
			requst.setAttribute("delete", "");
			requst.setAttribute("history", "");
			requst.setAttribute("seach", "");
			String choose = requst.getParameter("choose");
			String ip_choice = (requst.getParameter("ip_choice")!=null)?requst.getParameter("ip_choice"):""; 
			String ip_choice_network=requst.getParameter("MultiMacAddress.IP_Address_NetWork");
			session.put("ip_choice", ip_choice);
			session.put("ip_choice_network", ip_choice_network);
			
			if (choose.equals("AddData")) {
				requst.setAttribute("ContentPage",
						"/WEB-INF/ContentPage/Add/OneMACAddressChoose.jsp");
				requst.setAttribute("oneMac", "style='background-color:#E7CDFF;'");
				requst.setAttribute("multiMac", "");
				jump_login = false;
			}
			if (choose.equals("OneMACAddressShow")) {
				session.put("select_config", "hkbu"); //BDDS1
				OneMacAddress oneMacAddress1=(OneMacAddress) session.get("old_data");
				if(oneMacAddress1!=null){	
					session.remove("old_data");
					session.put("one_data", oneMacAddress1);
				}else{
					System.out.println("Add.java storing oneMacAddress tp one_data, onemacaddress os : " + oneMacAddress.toString());
					session.put("one_data", oneMacAddress);
				}
				requst.setAttribute("ContentPage",
						"/WEB-INF/ContentPage/Add/OneMACAddress.jsp");
				requst.setAttribute("oneMac", "style='background-color:#E7CDFF;'"); //ignore
				requst.setAttribute("multiMac", ""); // ignore
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
				//harry
				String overwrite = requst.getParameter("overwrite"); 
				
				session.put("overwrite", overwrite);
				session.put("select_config", select_config);
				if (jump.equals("one")) {
					// send an empty oneMacAddress structure. 
					session.put("one_data", oneMacAddress);
					requst.setAttribute("ContentPage",
							"/WEB-INF/ContentPage/Add/OneMACAddressCheck.jsp");
					requst.setAttribute("oneMac", "style='background-color:#E7CDFF;'");
					requst.setAttribute("multiMac", "");
					jump_login = false;
				}
				//harry check and rewrite 
				if (jump.equals("oneAdd")) {
					System.out.println("Session in add.java : " + session.toString());
					session.put("select_config", "hkbu"); //BDDS1
					session.put("overwrite", "1");
					OneMacAddress oneMacAddress1=(OneMacAddress) session.get("old_data");
					if(oneMacAddress1!=null){	
						System.out.println("OLD DATA ? Add.java jump euqlas oneAdd print oneMacAddress : " +  oneMacAddress1.toString());

						session.remove("old_data");
						session.put("one_data", oneMacAddress1);
					}else{
						System.out.println("NEW DATA (EMPTY) Add.java jump euqlas oneAdd print oneMacAddress : " +  oneMacAddress.toString());

						session.put("one_data", oneMacAddress);
					}
					requst.setAttribute("ContentPage",
							"/WEB-INF/ContentPage/Add/OneMACAddressAdd.jsp");
					requst.setAttribute("oneMac", "style='background-color:#E7CDFF;'");
					requst.setAttribute("multiMac", "");
					jump_login = false;
				}
				if (jump.equals("multi")) {
					session.put("select_servers", select_servers);
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
		if(erroMessage!=null){
			if(erroMessage.length()>0){
				requst.setAttribute("erroMessage", erroMessage);
			}
		}
		if (jump_login) {
			requst.setAttribute("erroMessage", "Please login system");
			return "Login";
		} else {
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

	public String getSelect_servers() {
		return select_servers;
	}
	public void setSelect_servers(String select_servers) {
		this.select_servers = select_servers;
	}
	public String getSelect_config() {
		return select_config;
	}
	public void setSelect_config(String select_config) {
		this.select_config = select_config;
	}

}