package tw.com.fivehundred.tag.delete;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

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

public class MultiAddressDelete extends TagSupport {
	private String file_path;
	private String title[] = { "MAC Address", "IP Address" };
	private String vauleName[] = { "MAC_Address", "IP_Address" };
	private String read_error_message = "";
	private String serverid1 = "BDDS1";
	private String serverid2 = "BDDS2";

	@SuppressWarnings("unchecked")
	@Override
	public int doStartTag() throws JspException {
		System.out.println("In multi address delete java");
		ServletContext servletContext = ServletActionContext
				.getServletContext();
		HttpServletRequest request = (HttpServletRequest) this.pageContext
				.getRequest();
		Map session = ActionContext.getContext().getSession();
		System.out.println(session);
		
		HttpServletResponse response = ServletActionContext.getResponse();
		file_path = (String) session.get("file_path");
		String choose = (String) session.get("choose");
		String select_config = (String) session.get("select_config");
		String select_servers = (String) session.get("select_servers");
		session.remove("select_config");
		session.remove("select_servers");
		session.remove("choose_data");
		session.remove("file_path");
		session.remove("choose");
		String[] file_data = readFile();
		String[] data = null;
		JspWriter out = this.pageContext.getOut();
		ProteusAPI_PortType service = (ProteusAPI_PortType) session
				.get("ready_server");
		request.setAttribute("select_config", select_config);

		try {
			APIEntity config = service.getEntityByName(0, select_config,
					ObjectTypes.Configuration);
			long id = config.getId();
			String userName = (String) session.get("userName");
			String ip_choice = (String) session.get("ip_choice");

			if (file_data != null) {
				// Multi IP delete
				System.out.println("Got File Data! Printing out... : " + file_data);
				if (ip_choice.equals("ip")) {
					if (choose != null) {
						System.out.println("Printing choose : " + choose);
						String[] choose_string_ip = choose.split(",");
						Integer[] choose_ip = new Integer[choose_string_ip.length];
						for (int i = 0; i < choose_string_ip.length; i++) {
							choose_ip[i] = Integer.valueOf(choose_string_ip[i].trim());
						}
						for (int i = 0; i < choose_ip.length; i++) {
							data = file_data[choose_ip[i]].split(",");
							// �R��IP
							APIEntity config7 = service.getIP4Address(id, data[0].trim());
							System.out.println("Pringint config7 & IP: " + config7 + " & " + data[0].trim());
							if (config7 != null) {
								service.delete(config7.getId());
								// didn't delete mac address here : ) 
								String macAddr = Tools.getMACbyMACADDRESSstring(config7.getProperties());
								System.out.println("Printing corresponding mac address : " + macAddr);
								// create a mac API entity
								APIEntity macConfig = service.getMACAddress(id, macAddr.trim());
								if(macConfig != null){
									// remove the MAC service as well
									service.delete(macConfig.getId());
								}
								String log_Content = userName + ","
										+ "Multi IP Address" + "," + data[0].trim()
										+ "," + "Delete" + "," + "Success"
										+ "," + Tools.getTime() + ","
										+ "IP Address Delete";
								System.out.println(log_Content);
								LogMian.writeLog(log_Content, userName);
							}
						}
					}
				}
				ArrayList<Integer> mac_cant_delete = new ArrayList<Integer>();
				
				// multi mac delete
				if (ip_choice.equals("mac") || ip_choice.equals("")) {
					if (choose != null) {
						String[] choose_string_mac = choose.split(",");
						Integer[] choose_mac = new Integer[choose_string_mac.length];
						for (int i = 0; i < choose_string_mac.length; i++) {
							choose_mac[i] = Integer.valueOf(choose_string_mac[i].trim());
						}
						for (int i = 0; i < choose_mac.length; i++) {
							data = file_data[choose_mac[i]].split(",");
							// �R��MAC
							APIEntity config7 = service.getMACAddress(id, data[0].trim()); 
							//�o���X��
							if (config7 != null) {
								
								//���X����ip
								APIEntity[] mac_ip_array=service.getLinkedEntities(config7.getId(),ObjectTypes.IP4Address,0,100);
								String IP_addr=""; 
								if (mac_ip_array.length>0){
									for (int x = 0; x < mac_ip_array.length; x++) {
										IP_addr = Tools
												.getIPbyADDRESSstring(mac_ip_array[x]
														.getProperties());
										APIEntity configtmp = service.getIP4Address(id,
												IP_addr);
										if (configtmp != null) {
											service.delete(configtmp.getId());
										}
									} 
								}  
								service.delete(config7.getId());
								config7 = service.getMACAddress(id, data[0].trim());
								if (config7 != null) {
									mac_cant_delete.add(choose_mac[i]);
									String log_Content = userName + ","
											+ "Multi Address" + "," + data[0]
											+ "," + "Delete" + "," + "Fail"
											+ "," + Tools.getTime() + ","
											+ "MAC Address Delete";
									LogMian.writeLog(log_Content, userName);
								} else {
									String log_Content = userName + ","
											+ "Multi Address" + "," + data[0]
											+ "," + "Delete" + "," + "Success"
											+ "," + Tools.getTime() + ","
											+ "MAC Address Delete";
									LogMian.writeLog(log_Content, userName);
								}
							}
						}
					}
				}
				if (mac_cant_delete.size() == 0) {

					APIEntity service_data = service.getEntityByName(id,
							serverid1, ObjectTypes.Server);
					service.deployServerServices(service_data.getId(),
							"services=DHCP");
					APIEntity service_data2 = service.getEntityByName(id,
							serverid2, ObjectTypes.Server);
					service.deployServerServices(service_data2.getId(),
							"services=DHCP");

					session.put("erroMessage", "Delete Completed");
					response.sendRedirect("/FiveHundredNet/BlueCat/DeletePage?choose=MultiMACAddress");
				} else {
					out.write("<form name='form1' method='post'	action='/FiveHundredNet/BlueCat/DeletePage?choose=MultiMACAddress'>");
					// out.write("<div>Bluecat configuration:" + select_config +
					// "</div>");
					out.write("<div>Bluecat DHCP Server: BDDS1 & BDDS2</div>");
					out.write("<table cellspacing='0' width='100%' border='1' borderColor='#DEE6EE'>");
					out.write("<tr>");
					for (int i = 0; i < title.length; i++) {
						out.write("<td align=left>" + title[i] + "</td>");
					}
					out.write("</tr>");
					for (int i = 0; i < mac_cant_delete.size(); i++) {
						data = file_data[mac_cant_delete.get(i)].split(",");
						if (i % 2 == 0) {
							out.write("<tr  style='background:#CDFEFF'>");
						} else {
							out.write("<tr  style='background:#FFFEFF'>");
						}
						for (int j = 0; j < 2; j++) {
							out.write("<td align=left>" + data[j]);
							if (j == 0) {
								out.write("<br><FONT COLOR='#FF0000'>("
										+ "This IP Address is being used can not delete the MAC Address"
										+ ")</FONT>");
							}
							out.write("</td>");
						}
						out.write("</tr>");
					}
					out.write("</table><input type='submit' value='Back'></form>");
				}
			} else {
				out.write("<tr><td>" + read_error_message + "</td></tr>");
				String log_Content = userName + "," + "" + "," + "" + ","
						+ "Read File" + "," + read_error_message + ","
						+ Tools.getTime() + "," + "";
				LogMian.writeLog(log_Content, userName);
			}

		} catch (Exception e1) {
			e1.printStackTrace();
			try {
				String userName = (String) session.get("userName");
				String log_Content = userName + "," + "" + "," + "" + "," + ""
						+ "," + "Bulk MAC address deletion system error" + ","
						+ Tools.getTime() + "," + "";
				LogMian.writeLog(log_Content, userName);
				out.write("<FONT COLOR='#FF0000'>"
						+ "Bulk MAC address deletion system error" + "</FONT>");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return super.doStartTag();
	}

	private String[] readFile() {
		BufferedReader dllRead = null;
		ArrayList<String> fileData = new ArrayList<String>();
		String aa = "";
		try {
			dllRead = new BufferedReader(new FileReader(new File(file_path)));
			try {
				while ((aa = dllRead.readLine()) != null) {
					fileData.add(aa);
				}
			} catch (IOException e) {
				read_error_message = read_error_message
						+ "File read failure<br>";
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			read_error_message = read_error_message + "File not find<br>";
			e.printStackTrace();
		} finally {
			try {
				dllRead.close();
			} catch (IOException e) {
				read_error_message = read_error_message
						+ "File close failure<br>";
				e.printStackTrace();
			}
		}
		String[] data = new String[fileData.size()];
		data = fileData.toArray(data);
		return data;
	}
}
