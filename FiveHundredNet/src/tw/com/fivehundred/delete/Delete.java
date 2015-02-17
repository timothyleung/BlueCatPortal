package tw.com.fivehundred.delete;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.RespectBinding;
import javax.xml.ws.Response;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import tw.com.fivehundred.tool.Tools;

import com.bluecatnetworks.proteus.api.client.java.proxy.ProteusAPI_PortType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class Delete extends ActionSupport {
	private File fileData;
	private String fileDataFileName;
	private String fileDataContentType;
	private String MAC_Address;
	private String IP_Address;
	private String choose_delete;
	private String select_config, select_servers;
	private String erroMessage;

	@SuppressWarnings("unchecked")
	public String execute() {
		boolean jump_login = true;
		HttpServletRequest requst = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ServletContext servletContext = ServletActionContext
				.getServletContext();
		Map session = ActionContext.getContext().getSession();
		ProteusAPI_PortType service = (ProteusAPI_PortType) session
				.get("ready_server");
		erroMessage = (String) session.get("erroMessage");
		if (erroMessage != null) {
			session.remove("erroMessage");
		}
		
		/**
		 * Setting attributes to display highlighted menu 
		 */
		if (service != null) {
			requst.setAttribute("add", "");
			requst.setAttribute("delete", "style='background-color:#E7CDFF;'");
			requst.setAttribute("history", "");
			requst.setAttribute("seach", "");
			requst.setAttribute("whichPage",
					"/WEB-INF/Menu/LeftMenuForDelete.jsp");
			String choose = requst.getParameter("choose");
			String ip_choice = requst.getParameter("ip_choice");
			if (ip_choice!=null){
				session.put("ip_choice", ip_choice);
			}
			
			/**
			 * Depends on the choices you chose, the corresponding section header will
			 * be highlighted in purple. 
			 */
			jump_login = left_menu_highlighting(requst, choose);

			if (choose.equals("check")) {
				session.put("select_config", select_config);
				String jump = requst.getParameter("jump");
				requst.setAttribute("ip_choice", ip_choice);
				if (jump.equals("oneIP")) {
					requst.setAttribute("ContentPage",
							"/WEB-INF/ContentPage/Delete/OneIPAddress.jsp");
					requst.setAttribute("oneIP",
							"style='background-color:#E7CDFF;'");
					requst.setAttribute("oneMac", "");
					requst.setAttribute("multiAddress", "");
					jump_login = false;
				}
				if (jump.equals("oneIPCheck")) {
					session.put("IP_Address", IP_Address);
					requst.setAttribute("ContentPage",
							"/WEB-INF/ContentPage/Delete/OneIPAddressCheck.jsp");
					requst.setAttribute("oneIP",
							"style='background-color:#E7CDFF;'");
					requst.setAttribute("oneMac", "");
					requst.setAttribute("multiAddress", "");
					jump_login = false;
				}
				if (jump.equals("oneIPDelete")) {
					session.put("select_servers", select_servers);
					session.put("IP_Address", IP_Address);
					requst.setAttribute("ContentPage",
							"/WEB-INF/ContentPage/Delete/OneIPAddressDelete.jsp");
					requst.setAttribute("oneIP",
							"style='background-color:#E7CDFF;'");
					requst.setAttribute("oneMac", "");
					requst.setAttribute("multiAddress", "");
					jump_login = false;
				}
				if (jump.equals("oneMAC")) {
					requst.setAttribute("ContentPage",
							"/WEB-INF/ContentPage/Delete/OneMACAddress.jsp");
					requst.setAttribute("oneIP", "");
					requst.setAttribute("oneMac",
							"style='background-color:#E7CDFF;'");
					requst.setAttribute("multiAddress", "");
					jump_login = false;
				}
				if (jump.equals("oneMACCheck")) {
					session.put("MAC_Address", MAC_Address);
					requst.setAttribute("ContentPage",
							"/WEB-INF/ContentPage/Delete/OneMACAddressCheck.jsp");
					requst.setAttribute("oneIP", "");
					requst.setAttribute("oneMac",
							"style='background-color:#E7CDFF;'");
					requst.setAttribute("multiAddress", "");
					jump_login = false;
				}
				if (jump.equals("oneMACDelete")) {
					session.put("select_servers", select_servers);
					session.put("MAC_Address", MAC_Address);
					requst.setAttribute("ContentPage",
							"/WEB-INF/ContentPage/Delete/OneMACAddressDelete.jsp");
					requst.setAttribute("oneIP", "");
					requst.setAttribute("oneMac",
							"style='background-color:#E7CDFF;'");
					requst.setAttribute("multiAddress", "");
					jump_login = false;
				}
				if (jump.equals("multi")) {
					session.put("select_servers", select_servers);
					session.put("choose", choose_delete);
					requst.setAttribute("ContentPage",
							"/WEB-INF/ContentPage/Delete/MultiAddressDelete.jsp");
					requst.setAttribute("oneIP", "");
					requst.setAttribute("oneMac", "");
					requst.setAttribute("multiAddress",
							"style='background-color:#E7CDFF;'");
					jump_login = false;
				}
				if (jump.equals("read")) {
					if (fileData != null) {
						String realpath = ServletActionContext
								.getServletContext().getRealPath(
										"/file/" + session.get("userName")
												+ "/Delete/");
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
									// ActionContext.getContext().put("message",
									// "�����W�����\");
									session.put("file_path",
											savefile.getAbsolutePath());
									requst.setAttribute("ContentPage",
											"/WEB-INF/ContentPage/Delete/MultiAddressRead.jsp");
								} catch (IOException e) {
									requst.setAttribute("erroMessage",
											"File Upload Failure");
									requst.setAttribute("ContentPage",
											"/WEB-INF/ContentPage/Add/MultiMACAddress.jsp");
								}
							} else {
								requst.setAttribute("erroMessage",
										"�W���������~�A������csv����");
								requst.setAttribute("ContentPage",
										"/WEB-INF/ContentPage/Add/MultiMACAddress.jsp");
							}
						}
					} else {
						requst.setAttribute("erroMessage", "Please select file");
						requst.setAttribute("ContentPage",
								"/WEB-INF/ContentPage/Delete/MultiAddress.jsp");
					}
					requst.setAttribute("oneIP", "");
					requst.setAttribute("oneMac", "");
					requst.setAttribute("multiAddress",
							"style='background-color:#E7CDFF;'");
					jump_login = false;
				}
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
		} else {
			return "DeletePage";
		}
	}
	
	private boolean left_menu_highlighting(HttpServletRequest request, String action){
		String[] actions = {"DeleteData", "OneMACAddress", "OneIPAddress", "MultiMACAddress"};
		String[] color_menu = {"oneIP", "oneMac", "oneIP", "multiAddress"};
		String[] content_page = {"OneIPAddress", "OneMACAddress", "OneIPAddress", "MultiAddress"};
		boolean jump_login = true;
		
		final int i = Arrays.asList(actions).indexOf(action);
		if ( i < 0 ) { 
			return jump_login;
		};
		System.out.println(action + ":" + i);
		request.setAttribute("ContentPage", "/WEB-INF/ContentPage/Delete/" + content_page[i] + ".jsp");
		request.setAttribute(color_menu[i], "style='background-color:#E7CDFF;'");

		return false;
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

	public String getMAC_Address() {
		return MAC_Address;
	}

	public void setMAC_Address(String mAC_Address) {
		if (mAC_Address.equals("xx-xx-xx-xx-xx-xx")) {
			MAC_Address = "";
		} else {
			MAC_Address = mAC_Address.trim();
		}
	}

	public String getIP_Address() {
		return IP_Address;
	}

	public void setIP_Address(String iP_Address) {
		if (iP_Address.equals("xxx.xxx.xxx.xxx")) {
			IP_Address = "";
		} else {
			IP_Address = iP_Address.trim();
		}
	}

	public String getChoose_delete() {
		return choose_delete;
	}

	public void setChoose_delete(String choose_delete) {
		this.choose_delete = choose_delete;
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