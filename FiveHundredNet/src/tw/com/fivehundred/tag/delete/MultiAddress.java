package tw.com.fivehundred.tag.delete;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts2.ServletActionContext;

import tw.com.fivehundred.log.LogMian;
import tw.com.fivehundred.tool.Tools;

import com.bluecatnetworks.proteus.api.client.java.constants.ObjectTypes;
import com.bluecatnetworks.proteus.api.client.java.proxy.APIEntity;
import com.bluecatnetworks.proteus.api.client.java.proxy.ProteusAPI_PortType;
import com.opensymphony.xwork2.ActionContext;

public class MultiAddress extends TagSupport {
	private String file_path;
	private String title[] = { "MAC Address", "IP Address",
			"choose"};
//	private String vauleName[] = { "MAC_Address", "IP_Address",
//			"choose"};
	private String read_error_message = "";

	@Override
	public int doStartTag() throws JspException {
		ServletContext servletContext = ServletActionContext
				.getServletContext();
		HttpServletRequest request = (HttpServletRequest) this.pageContext
				.getRequest();
		Map session = ActionContext.getContext().getSession();
		file_path = (String) session.get("file_path");
		String select_config = (String) session.get("select_config");
		session.remove("select_config");
		String[] file_data = readFile();
		JspWriter out = this.pageContext.getOut();
		String[] data = null;
		APIEntity[] fields = null;
		ProteusAPI_PortType service = (ProteusAPI_PortType) session
				.get("ready_server");
		request.setAttribute("select_config", select_config);

		APIEntity config;
		long id = 0;
		try {
			config = service.getEntityByName(0, select_config,
					ObjectTypes.Configuration);
			id = config.getId();
			// 抓取Configuration裡所有的mac address
			fields = Tools.getAllMacAddress(service, id);
			String check_MACAddress = "";
			boolean show_ip = true;
			boolean show_mac = true;
			
			out.write("<table><tr><td>Bluecat servers : BDDS1 & BDDS2");
			/*
			APIEntity[] servers;
			servers = service.getEntities(id, ObjectTypes.Server, 0, 999);
			out.write("<select name='select_servers'>");
			for (int i = 0; i < servers.length; i++) {
				out.write("<option vaule='" + servers[i].getName() + "'>"
						+ servers[i].getName() + "</option>");
			}
			out.write("</select>");
			*/
			out.write("</td></tr></table>");
			
			// ip or mac
			String ip_choice = (String)session.get("ip_choice");
			
			out.write("<table cellspacing='0' width='100%' border='1' borderColor='#DEE6EE'><tr>");
			//for (int i = 0; i < title.length; i++) {
				//out.write("<td align=left>" + title[i] + "</td>");
				//out.write("<td align=left>" + file_data[0] + "</td>");
			//}
			if (ip_choice.equals("ip")){
				out.write("<td align=left>IP Address</td>");
				out.write("<td align=left>Associated MAC Address</td>");
				out.write("<td align=left>Select to delete</td>");  
			}else{
				out.write("<td align=left>MAC Address</td>");
				out.write("<td align=left>Associated IP Address</td>");
				out.write("<td align=left>Select to delete</td>");  
			}
			out.write("</tr>");
			int erro_number = 0;
			if (file_data != null) {
				if (file_data.length > 1) {
					for (int i = 1; i < file_data.length; i++) {
						data = Tools.readData(file_data[i]);
						show_ip = true;
						show_mac = true;
						String message_mac = "";
						String message_ip = "";
						check_MACAddress = "";
						boolean mac_repeat = false;
						
						if (ip_choice.equals("mac")){
						// 檢查Mac
						if (data[0].trim().length() > 0) {  
							// 檢查Mac Address
							String[] mac_array = data[0].trim().split("-");
							if (mac_array.length == 1) {
								mac_array = data[0].split(":");
							}
							if (mac_array.length == 1 && data[0].length() == 12) {
								mac_array = new String[6];
								for (int k = 0; k < mac_array.length; k++) {
									mac_array[k] = "";
								}
								int a = 0;
								for (int k = 0; k < data[0].length(); k++) {
									if (k != 0 && k % 2 == 0) {
										a++;
									}
									mac_array[a] = mac_array[a]
											+ data[0].charAt(k);
								}
							}
							if (mac_array.length != 6) {
								message_mac = "Incorrect MAC Address: Please check MAC address format";
								//show_ip = false;
								//show_mac = false;
							} else {
								if (Tools.checkMacAddress(mac_array)) {
									message_mac = "Incorrect MAC Address: Please check MAC address format";
									//show_ip = false;
									//show_mac = false;
								} else {
									check_MACAddress = checkMACAddress(fields,data[0].trim());
									// 開始顯示輸出
									if (check_MACAddress.equals("repeat")) {
										//要再找出關聯得ip
										APIEntity config7 = service.getMACAddress(id, data[0].trim()); 
										//找出相關ip
										APIEntity[] mac_ip_array=service.getLinkedEntities(config7.getId(),ObjectTypes.IP4Address,0,100);
										if (mac_ip_array!=null && mac_ip_array.length>0){
										String IP_addr = Tools.getIPbyADDRESSstring(mac_ip_array[0].getProperties()); 
										data[1]=IP_addr;
										}
										message_mac = "Duplicated MAC Address";
									}
									if (check_MACAddress.equals("one")) {
										//show_mac = false;
										message_mac = "Record not found"; //"Unique MAC Address";
										erro_number++;
									}
								}
							}
						} else {
							message_mac = "Incorrect MAC Address: Please check MAC address format";
							//show_ip = false;
							//show_mac = false;
						}
						}
						
						if (ip_choice.equals("ip")){
						// 檢查ip 原本檢查 data[1]全改為檢查data[0]
						if (data[0].trim().length() > 0) {
							String[] ip_marray = data[0].trim().split("\\.");
							if (ip_marray.length != 4) {
								message_ip = "Incorrect IP Address: Please check IP address format";
								//show_ip = false;
								//show_mac = false;
							} else {
								if (Tools.checkIPAddress(ip_marray)) {
									message_ip = "Incorrect IP Address: Please check IP address format";
									//show_ip = false;
									//show_mac = false;
								} else {
									try {
										config = service.getEntityByName(0,
												select_config,
												ObjectTypes.Configuration);
										id = config.getId();
										try {
											APIEntity config_ip = service
													.getIP4Address(id, data[0]);
											if (config_ip != null) {
												//show_ip = true;
												//要再找出關聯得mac 
												String mac_addr= Tools.getMACbyMACADDRESSstring(config_ip.getProperties());
												data[1] =mac_addr;
												
												message_ip = "Duplicated IP Address";
											} else {
												//show_ip = false;
												message_ip = "Record not found"; //"IP address record not found";//"Free IP Address";
												erro_number++;
											}
										} catch (RemoteException e2) {
											message_ip = "IP Address Check Failure: Please check input IP address ";
											//show_ip = false;
											//show_mac = false;
										}
									} catch (RemoteException e1) {
										e1.printStackTrace();
									}
								}
							}
						} else {
							message_ip = "Incorrect IP Address: Please check IP address format";
							//show_ip = false;
							//show_mac = false;
						}
						}
						
						if (i % 2 == 0) {
							out.write("<tr  style='background:#CDFEFF'>");
						} else {
							out.write("<tr  style='background:#FFFEFF'>");
						}
						for (int j = 0; j < 3; j++) {
							if (j < 2) {
								out.write("<td align=left>" + data[j]);
								// 1=> 0 都改在第一欄位了
								if (j == 0 && message_ip.length() != 0) {
									out.write("<br><FONT COLOR='#FF0000'>("
											+ message_ip + ")</FONT>");
								}
								if (j == 0 && message_mac.length() != 0) {
									out.write("<br><FONT COLOR='#FF0000'>("
											+ message_mac + ")</FONT>");
								}
								out.write("</td>");
							} else {
								if (j == 2) {
									if (show_mac || show_ip) {
										out.write("<td>");
										out.write("<input type='checkbox' name='choose_delete' value='"
												+ i + "' checked='checked'>");
										out.write("</td>");
									} else {
										out.write("<td>");
										out.write("</td>");
									}
								}
							}
						}
						out.write("</tr>");
					}
					System.out.println(erro_number+"|"+((file_data.length-1)*2));
					if(erro_number!=((file_data.length-1)*2)){						
						out.write("</table><input type='button' value='Delete' onclick=\"return checkalldel();\">");
						out.write("</table><input type='button' value='Back' onclick=\"top.location='/FiveHundredNet/BlueCat/DeletePage?choose=MultiMACAddress'\">");
						
					}else{
						out.write("</table><input type='submit' value='Back' onclick=\"form1.action='/FiveHundredNet/BlueCat/BackPage?choose=DeletePage&log=noData'\">");
					}
				} else {
					out.write("</table><input type='submit' value='Back' onclick=\"form1.action='/FiveHundredNet/BlueCat/BackPage?choose=DeletePage&log=noData'\">");
				}
				out.write("<input type='hidden' name='ip_choice' id='ip_choice' value='"+ip_choice+"' />");
			} else {
				out.write("<tr><td>" + read_error_message + "</td></tr>");
				String userName = (String) session.get("userName");
				String log_Content = userName + "," + "" + "," + "" + ","
						+ "Read File" + "," + read_error_message + ","
						+ Tools.getTime() + "," + "";
				LogMian.writeLog(log_Content, userName);
			}
		} catch (IOException e) {
			String userName = (String) session.get("userName");
			String log_Content = userName + "," + "" + "," + "" + "," + ""
					+ "," + "Bulk read system error" + "," + Tools.getTime()
					+ "," + "";
			LogMian.writeLog(log_Content, userName);
			try {
				out.write("Bulk read system error");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
		try {
			String[] data = new String[fileData.size()];
			data = fileData.toArray(data);
			return data;
		} catch (Exception e) {
			return null;
		}
	}

	private String checkMACAddress(APIEntity[] fields, String mac_address) {
		String ans = "one";
		String container = "";
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].getProperties().indexOf("address") != -1) {
				container = fields[i]
						.getProperties()
						.substring(
								fields[i].getProperties().indexOf("address=") + 8,
								fields[i].getProperties().indexOf("address=") + 25)
						.replace("-", "");
				if (changUpper(mac_address.replace("-", "")).equals(container)) {
					ans = "repeat";
					break;
				}
			}
		}
		return ans;
	}

	private String changUpper(String old) {
		String new_word = "";
		for (int i = 0; i < old.length(); i++) {
			if (!Character.isDigit(old.charAt(i))) {
				switch (old.charAt(i)) {
				case 'a':
					new_word = new_word + "A";
					break;
				case 'b':
					new_word = new_word + "B";
					break;
				case 'c':
					new_word = new_word + "C";
					break;
				case 'd':
					new_word = new_word + "D";
					break;
				case 'e':
					new_word = new_word + "E";
					break;
				case 'f':
					new_word = new_word + "F";
					break;
				default:
					break;
				}
			} else {
				new_word = new_word + old.charAt(i);
			}
		}
		return new_word;
	}
}
