package tw.com.fivehundred.tag.add;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import javax.tools.Tool;

import tw.com.fivehundred.add.been.OneMacAddress;
import tw.com.fivehundred.tool.Tools;

import com.bluecatnetworks.proteus.api.client.java.constants.ObjectTypes;
import com.bluecatnetworks.proteus.api.client.java.proxy.APIEntity;
import com.bluecatnetworks.proteus.api.client.java.proxy.ProteusAPI_PortType;
import com.opensymphony.xwork2.ActionContext;

public class OneMACAddressCheck extends TagSupport {

	private String title[] = { "MAC Address", "IP Address", "Machine Type",
			"Location", "Owner", "Department", "Phone Number", "Input Date",
			"Reference" };
	private String vauleName[] = { "MAC_Address", "IP_Address", "Machine_Type",
			"Location", "Owner", "Department", "Phone_Number", "Input_Date",
			"Reference" };

	@SuppressWarnings("unused")
	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) this.pageContext
				.getRequest();
		JspWriter out = this.pageContext.getOut();
		Map session = ActionContext.getContext().getSession();
		OneMacAddress oneMacAddress = (OneMacAddress) session.get("one_data");
		String select_config = "hkbu";// (String) session.get("select_config");
		session.remove("one_data");
		session.remove("select_config");
		ProteusAPI_PortType service = (ProteusAPI_PortType) session
				.get("ready_server");
		request.setAttribute("select_config", select_config);
		String message_mac = "";
		String message_ip = "";
		String container_Status = "";
		boolean mac_repeat = false;
		// 檢查
		APIEntity config;
		String DuplicatedIP="";//當MAC重覆時,找出重覆的IP
		String DuplicatedMAC="";//當IP重覆時,找出重覆的MAC
		
		long id = 0;
		if (oneMacAddress.getMAC_Address().trim().length() > 0) {
			// 檢查Mac Address
			String[] mac_array = oneMacAddress.getMAC_Address().trim()
					.split("-");
			if (mac_array.length == 1) {
				mac_array = oneMacAddress.getMAC_Address().split(":");
			}
			if (mac_array.length == 1
					&& oneMacAddress.getMAC_Address().trim().length() == 12) {
				mac_array = new String[6];
				for (int k = 0; k < mac_array.length; k++) {
					mac_array[k] = "";
				}
				int a = 0;
				for (int k = 0; k < oneMacAddress.getMAC_Address().trim()
						.length(); k++) {
					if (k != 0 && k % 2 == 0) {
						a++;
					}
					mac_array[a] = mac_array[a]
							+ oneMacAddress.getMAC_Address().trim().charAt(k);
				}
			}
			if (mac_array.length != 6) {
				message_mac = "Incorrect MAC Address: Please check MAC address format";
			} else {
				if (Tools.checkMacAddress(mac_array)) {
					message_mac = "Incorrect MAC Address: Please check MAC address format";
				} else {
					try {
						config = service.getEntityByName(0, select_config,
								ObjectTypes.Configuration);
						id = config.getId();
						try {
							// 抓資料庫裡的mac address
							APIEntity config_mac = service.getMACAddress(id,
									oneMacAddress.getMAC_Address());
							if (config_mac != null) {
								
								//harry 讀取與mac相關的ip
								APIEntity[] mac_ip_array=service.getLinkedEntities(config_mac.getId(),ObjectTypes.IP4Address,0,100);
								
								DuplicatedIP="No Ip Data";
								//這邊要把所有mac_ip_array的值都印出來
								if (mac_ip_array.length>0){
									DuplicatedIP =Tools.getIPbyADDRESSstring(mac_ip_array[0].getProperties()) ;
								}
								
								//service.getEntityByName(0, select_config,
								//		ObjectTypes.Configuration);
								
								message_mac = "Duplicated MAC Address";
								mac_repeat = true;
							}
						} catch (RemoteException e) {
							message_mac = "MAC Address Check Failure: Please check input MAC address";
						}
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				}
			}
		} else {
			message_mac = "Incorrect MAC Address: Please check MAC address format";
		}
		// 檢查ip
		boolean check_ip = false;
		if (oneMacAddress.getIP_Address().trim().length() > 0) {
			String[] Network = oneMacAddress.getIP_Address().trim().split("/");
			String[] ip_marray = Network[0].split("\\.");
			if (ip_marray.length != 4) {
				message_ip = "Incorrect IP Address: Please check IP address format";
			} else {
				if (Tools.checkIPAddress(ip_marray)) {
					message_ip = "Incorrect IP Address: Please check IP address format";
				} else {
					try {
						config = service.getEntityByName(0, select_config,
								ObjectTypes.Configuration);
						id = config.getId();
						try {
							if (Network.length == 1) {
								String ip_adress = ip_marray[0] + "."
										+ ip_marray[1] + "." + ip_marray[2];
								if (oneMacAddress.getIP_Address().equals(
										ip_adress + ".0")
										|| oneMacAddress.getIP_Address()
												.equals(ip_adress + ".0")) {
									message_ip = "System Uses IP, Can Not Be Used";
								} else {
									//若單一ip, 就撿查該ip資訊
									APIEntity config_ip = service
											.getIP4Address(id, oneMacAddress
													.getIP_Address());
									if (config_ip != null) {
										//重覆mac
										DuplicatedMAC=Tools.getMACbyMACADDRESSstring(config_ip.getProperties()); 
										message_ip = "Duplicated IP Address";
									}
								}
							} else {
								check_ip = true;
							}
						} catch (RemoteException e2) {
							e2.printStackTrace();
							message_ip = "IP Address Check Failure: Please check input IP address ";
						}
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		if (oneMacAddress.getIP_Address().trim().length() == 0 || check_ip) { 
			//應該用 assignNextAvailableIP4Address() 
			String[] Network = oneMacAddress.getIP_Address_NetWork().trim().split("/");
			if (Network[1].indexOf("[")>0)
			{
				Network[1]=Network[1].substring(0, Network[1].indexOf("[")).trim(); 
			}
			String[] ip_marray = Network[0].split("\\.");
			if (ip_marray.length != 4) {
				message_ip = "Incorrect IP Address: Please check IP address format";
			} else {
				if (Tools.checkIPAddress(ip_marray)) {
					message_ip = "Incorrect IP Address: Please check IP address format";
				} else {
					try {
						config = service.getEntityByName(0, select_config,
								ObjectTypes.Configuration);
						id = config.getId();
						try {
							
							
							String ip_adress0 = ip_marray[0] + "."
									+ ip_marray[1] + "." + ip_marray[2];
							if (Network[0].equals(ip_adress0 + ".0")) {
								// 所有有用到的ipNetwork
								APIEntity[] fields = service
										.searchByObjectTypes(
												Tools.getIPNETWORKwithoutName(oneMacAddress.getIP_Address_NetWork()),
												ObjectTypes.IP4Network, 0, 9999);
								if (fields.length != 0) {
									
									String ip4Network = Tools.getIPNETWORKbyCIDRstring(fields[0].getProperties());
											 
									ip4Network = Tools.getIPNETWORKwithoutName(ip4Network);
									String[] ip_number = ip4Network.split("/");
									String[] ip_address = ip_number[0] 
											.split("\\.");
									
									String network_submast = "";
									if (ip_number[1].indexOf("[")>0)
										{
											network_submast=ip_number[1].substring(0, ip_number[1].indexOf("[")).trim(); 
										}else{
											network_submast=ip_number[1].trim();
										}
									
									ip4Network = ip_address[0] + "."
											+ ip_address[1] + "."
											+ ip_address[2];
									if (Integer.parseInt(Network[1]) == Integer
											.parseInt(network_submast)) {
										/*
										// 所有有用到的ip
										APIEntity[] fields1 = Tools
												.getAllIPAddress(service, id);
										String ip_adress = "";
										String mac_adress = "";
										String status_adress = "";
										ArrayList<String> ip_list = new ArrayList<String>();
										ArrayList<String> mac_list = new ArrayList<String>();
										ArrayList<String> status_list = new ArrayList<String>();
										for (int i = 0; i < fields1.length; i++) {
											if (fields1[i].getProperties().indexOf("address=") != -1) {
												ip_adress = Tools.getIPbyADDRESSstring(fields1[i].getProperties()).replace("-", "");
												 
											}
											if (fields1[i].getProperties().indexOf("state=") != -1) {
												status_adress = Tools.getIPbySTATEstring(fields1[i].getProperties());
														 
											}
											if (fields1[i].getProperties().indexOf("macAddress=") != -1) {
												mac_adress = Tools.getMACbyMACADDRESSstring(fields1[i].getProperties());
														 
											}
											ip_list.add(ip_adress);
											mac_list.add(mac_adress);
											status_list.add(status_adress);
										}*/
										//要改用 NEXT AVAILED FUNCTION
										/*String can_use_ip = "";
										boolean ip_ans = false;
										int cont = 0;
										for (int i = 1; i < 255; i++) {
											cont = 0;
											for (int j = 0; j < ip_list.size(); j++) {
												if (ip_list.get(j).equals(ip4Network + "." + i)) {
													if (mac_list.get(j).length() == 0) {
														if (status_list.get(j).length() != 0) {
															APIEntity config7 = service.getIP4Address(id,	ip4Network+ "."+ i);
															service.delete(config7.getId());
														}
														ip_ans = true;
														break;
													}
												} else {
													cont++;
												}
											}
											if (cont == ip_list.size()) {
												ip_ans = true;
											}
											if (ip_ans) {
												can_use_ip = ip4Network + "."
														+ i;
												break;
											}
										}*/
										
										String can_use_ip=service.getNextAvailableIP4Address(fields[0].getId());
										
										//String can_use_ip =NextAvailableIP;
										
										oneMacAddress.setIP_Address(can_use_ip);
										message_ip = "System Automatically Assigns IP";
										
									} else {
										message_ip = "IP Address Check Failure:Subnet Mask Error";
									}
								} else {
									message_ip = "IP Address Check Failure:System Automatically Assigns IP Error";
								}
							} else {
								message_ip = "IP Address Check Failure:IP Address Error";
							}
						} catch (RemoteException e2) {
							e2.printStackTrace();
							message_ip = "IP Address Check Failure: Please check input IP address ";
						}
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				}
			}
		}

		try {
			/*out.write("<table><tr><td>Bluecat servers :");
			out.write("<input type='text' name='oneMacAddress.Servers"
					+ "' readonly='readonly' value='"
					+ oneMacAddress.getServers()
					+ "'style='border:1px;border-bottom-style:none;border-top-style:none;border-left-style:none;border-right-style:none;'"
					+ "/>");
			out.write("</td></tr></table>");*/
			
			out.write("<table cellspacing='0' width='200%' border='1' borderColor='#DEE6EE'>");
			for (int i = 0; i < title.length; i++) {
				if (i == 2) {
					out.write("</table>");
					out.write("<div>Below data used user defined field (UDF)</div>");
					out.write("<table cellspacing='0' width='200%' border='1' borderColor='#DEE6EE'>");
				}
				out.write("<tr><th align=left>" + title[i] + "</th>");
				out.write("<td>");
				if (i == 0) {
					out.write("<input type='text' name='oneMacAddress."
							+ vauleName[i]
							+ "' readonly='readonly' value='"
							+ oneMacAddress.getMAC_Address()
							+ "'style='border:1px;border-bottom-style:none;border-top-style:none;border-left-style:none;border-right-style:none;'"
							+ "/>");

					if (message_mac.length() != 0) {
						out.write("<br><FONT COLOR='#FF0000'>(" + message_mac
								+ ")</FONT>");
					}
				}
				if (i == 1) {
					out.write("<input type='text' name='oneMacAddress."
							+ vauleName[i]
							+ "' readonly='readonly' value='"
							+ oneMacAddress.getIP_Address()
							+ "'style='border:1px;border-bottom-style:none;border-top-style:none;border-left-style:none;border-right-style:none;'"
							+ "/>");

					if (message_ip.length() != 0) {
						out.write("<br><FONT COLOR='#FF0000'>(" + message_ip
								+ ")</FONT>");
					}
					if (DuplicatedIP.length() != 0) {
					out.write("</td></tr>");
					out.write("<tr><th align=left>" + title[i] + " (In Used)</th>");
					out.write("<td>");
					out.write("<FONT COLOR='#FF0000'>" + DuplicatedIP + "</FONT><input type='hidden' id='DuplicatedIP' name='DuplicatedIP' value='" + DuplicatedIP + "' />");
					}
					if (DuplicatedMAC.length() != 0) {
						out.write("</td></tr>");
						out.write("<tr><th align=left>Mac Address (In Used)</th>");
						out.write("<td>");
						out.write("<FONT COLOR='#FF0000'>" + DuplicatedMAC + "</FONT><input type='hidden' id='DuplicatedMAC' name='DuplicatedMAC' value='" + DuplicatedMAC + "' />");
					}
					
					
				}
				if (i == 2) {
					out.write("<input type='text' name='oneMacAddress."
							+ vauleName[i]
							+ "' readonly='readonly' value='"
							+ oneMacAddress.getMachine_Type()
							+ "'style='border:1px;border-bottom-style:none;border-top-style:none;border-left-style:none;border-right-style:none;'/>");
				}
				if (i == 3) {
					out.write("<input type='text' name='oneMacAddress."
							+ vauleName[i]
							+ "' readonly='readonly' value='"
							+ oneMacAddress.getLocation()
							+ "'style='border:1px;border-bottom-style:none;border-top-style:none;border-left-style:none;border-right-style:none;'/>");
				}
				if (i == 4) {
					out.write("<input type='text' name='oneMacAddress."
							+ vauleName[i]
							+ "' readonly='readonly' value='"
							+ oneMacAddress.getOwner()
							+ "'style='border:1px;border-bottom-style:none;border-top-style:none;border-left-style:none;border-right-style:none;'/>");
				}
				if (i == 5) {
					out.write("<input type='text' name='oneMacAddress."
							+ vauleName[i]
							+ "' readonly='readonly' value='"
							+ oneMacAddress.getDepartment()
							+ "'style='border:1px;border-bottom-style:none;border-top-style:none;border-left-style:none;border-right-style:none;'/>");
				}
				if (i == 6) {
					out.write("<input type='text' name='oneMacAddress."
							+ vauleName[i]
							+ "' readonly='readonly' value='"
							+ oneMacAddress.getPhone_Number()
							+ "'style='border:1px;border-bottom-style:none;border-top-style:none;border-left-style:none;border-right-style:none;'/>");
				}
				if (i == 7) {
					out.write("<input type='text' name='oneMacAddress."
							+ vauleName[i]
							+ "' readonly='readonly' value='"
							+ oneMacAddress.getInput_Date()
							+ "'style='border:1px;border-bottom-style:none;border-top-style:none;border-left-style:none;border-right-style:none;'/>");
				}
				if (i == 8) {
					out.write("<input type='text' name='oneMacAddress."
							+ vauleName[i]
							+ "' readonly='readonly' value='"
							+ oneMacAddress.getReference()
							+ "'style='border:1px;border-bottom-style:none;border-top-style:none;border-left-style:none;border-right-style:none;'/>");
				}
				out.write("</td></tr>");
			}
			out.write("</table>");
			// mac 可以重複 但是 ip不行
			if (message_ip.length() == 0 && message_mac.length() == 0) {
				out.write("<input type='submit' value='Add' onclick=\"form1.action='/FiveHundredNet/BlueCat/AddPage?choose=check&jump=oneAdd'\">");
				out.write("<input type='submit' value='Back' onclick=\"form1.action='/FiveHundredNet/BlueCat/BackPage?choose=AddPage&log=userCancel'\">");
			} else if (message_ip.equals("System Automatically Assigns IP") && message_mac.length() == 0) {
				out.write("<input type='submit' value='Add' onclick=\"form1.action='/FiveHundredNet/BlueCat/AddPage?choose=check&jump=oneAdd'\">");
				out.write("<input type='submit' value='Back' onclick=\"form1.action='/FiveHundredNet/BlueCat/BackPage?choose=AddPage&log=userCancel'\">");
			} else if (mac_repeat && message_ip.length() == 0) {
				out.write("<input type='submit' value='Overwrite' onclick=\"form1.action='/FiveHundredNet/BlueCat/AddPage?choose=check&jump=oneAdd'\">");
				out.write("<input type='submit' value='Back' onclick=\"form1.action='/FiveHundredNet/BlueCat/BackPage?choose=AddPage&log=userCancel'\">");
			} else {
				out.write("<input type='submit' value='Overwrite' onclick=\"form1.action='/FiveHundredNet/BlueCat/AddPage?choose=check&jump=oneAdd&overwrite=1'\">");
				out.write("<input type='submit' value='Back' onclick=\"form1.action='/FiveHundredNet/BlueCat/BackPage?choose=AddPage&log=checkErro'\">");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.doStartTag();
	}
}
