package tw.com.fivehundred.tag.add;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
import tw.com.fivehundred.tool.AddressType;
import tw.com.fivehundred.tool.ImportAttr;
import tw.com.fivehundred.tool.Tools;

import com.bluecatnetworks.proteus.api.client.java.constants.ObjectTypes;
import com.bluecatnetworks.proteus.api.client.java.proxy.APIEntity;
import com.bluecatnetworks.proteus.api.client.java.proxy.ProteusAPI_PortType;
import com.opensymphony.xwork2.ActionContext;

public class MultiMACAddress extends TagSupport {
	private String file_path;
	private SimpleDateFormat format = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm:ss");
	private String title[] = { "MAC Address", "IP Address", "Machine Type",
			"Location", "Owner", "Department", "Phone Number", "Reference",
			"Input Date","Overwrite" };

	private String read_error_message = "";
	final String page_title = "Multiple IP/MAC addresses Check";
	public static List<String> GLOBAL_MULTI_ADDRESS_STORER;
	@Override
	public int doStartTag() throws JspException {
		ServletContext servletContext = ServletActionContext
				.getServletContext();
		HttpServletRequest request = (HttpServletRequest) this.pageContext
				.getRequest();
		Map session = ActionContext.getContext().getSession();
		file_path = (String) session.get("file_path");
		String choose_data = (String) session.get("choose_data");
		String select_config = (String) session.get("select_config");

		String ip_choice = (String) session.get("ip_choice");
		String ip_choice_network = (String) session.get("ip_choice_network");

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
		String OldIP="";
		String OldDyIP="";

		try {
			/**
			 * Tim implementation
			 */
			System.out.println("Getting config : " + select_config);
			config = service.getEntityByName(0, select_config,
					ObjectTypes.Configuration);
			id = config.getId();
			out.write("<h1>" + page_title +"</h1><br>");
			
			out.write("<table><tr><td>Bluecat servers : BDDS1 & BDDS2");
			out.write("</td></tr></table>");
			
			List<OneMacAddress> oneMacAddresses = readImportCSVFile(file_path);
			out.write("<table name='table_data' class=\"table table-striped table-hover table-bordered\">");
			for (int i = 0; i < title.length; i++) {
				out.write("<th>" + title[i] + "</th>");
			}
			GLOBAL_MULTI_ADDRESS_STORER = new ArrayList<String>(); // empty storer
			for(int i=0; i < oneMacAddresses.size(); i++){
				OneMacAddress oneMacAddress = oneMacAddresses.get(i);
				System.out.println("After reading from CSV file, onemacaddress is : " + oneMacAddress.toString());
				String mac_address = oneMacAddress.getMAC_Address();
				String ip_address = oneMacAddress.getIP_Address();
				if(Tools.checkMacAddress(mac_address)){
					if(Tools.checkIPAddress(ip_address)){
						if (ip_choice.equals("2")) {
							oneMacAddress.setIP_Address_NetWork(ip_choice_network);
							Tools.single_addition_check(out, service, config, AddressType.IPV4_NETWORK, oneMacAddress, i+1);
						} else {
							Tools.single_addition_check(out, service, config, AddressType.IP, oneMacAddress, i+1);
						}
					} else {
						out.write("<tr><th span='100%'>" + ip_address + " is in invalid format.</th></tr>");
						// print row ip addr invalid
					}
				} else {
					//print row // mac address invalid
					out.write("<tr><th span='100%'>" + mac_address + " is in invalid format.</th></tr>");
				}

			}
			out.write("</table>");
			out.write("<input type='hidden' name='ip_choice' value='"+ ip_choice +"'>");
			out.write("<input class='btn btn-default' type='button' value='Add' onclick=\"return checkall();\">");
			out.write("<input class='btn btn-default' type='submit' value='Back' onclick=\"form1.action='/FiveHundredNet/BlueCat/BackPage?choose=MultiMACAddress&log=multiUserCancel'\">");

			//
			/**
			 * End of tim implementation 
			 */
			// read file 
			// create a oneMacAddress for each row.. 
			// call single_deletion_check_method... 
			
			// display page only.. 
//			
//			
//			fields = Tools.getAllMacAddress(service, id);
//			String check_MACAddress = "";
//			boolean lock = false;
//			boolean show = true;
//
//			out.write("<table><tr><td>Bluecat servers : BDDS1 & BDDS2");
//			out.write("</td></tr></table>");
//
//			out.write("<table cellspacing='0' width='100%' border='1' borderColor='#DEE6EE'><tr>");
//			for (int i = 0; i < title.length; i++) {
//				out.write("<td align=left>" + title[i] + "</td>");
//			}
//			out.write("</tr>");
//			int erro_ip_number = 1;
//			if (file_data != null) {
//				if (file_data.length > 1) {
//					for (int i = 1; i < file_data.length; i++) {
//						data = Tools.readData(file_data[i]);
//						lock = false;
//						show = true;
//						String message_mac = "";
//						String message_ip = "";
//						check_MACAddress = "";
//						boolean mac_repeat = false;
//						// ���d
//						if (data[0].trim().length() > 0) {
//							// ���dMac Address
//							String[] mac_array = data[0].trim().split("-");
//							if (mac_array.length == 1) {
//								mac_array = data[0].split(":");
//							}
//							if (mac_array.length == 1 && data[0].length() == 12) {
//								mac_array = new String[6];
//								for (int k = 0; k < mac_array.length; k++) {
//									mac_array[k] = "";
//								}
//								int a = 0;
//								for (int k = 0; k < data[0].length(); k++) {
//									if (k != 0 && k % 2 == 0) {
//										a++;
//									}
//									mac_array[a] = mac_array[a]
//											+ data[0].charAt(k);
//								}
//							}
//							if (mac_array.length != 6) {
//								message_mac = "Incorrect MAC Address: Please check MAC address format";
//								show = false;
//							} else {
//								if (Tools.checkMacAddress(mac_array)) {
//									message_mac = "Incorrect MAC Address: Please check MAC address format";
//									show = false;
//								} else {
//									check_MACAddress = checkMACAddress(fields,
//											data[0].trim());
//									// �}�l�������X
//									if (check_MACAddress.equals("repeat")) {
//										message_mac = "Record found"; // "Duplicated MAC Address";
//									}
//									if (check_MACAddress.equals("one")) {
//										message_mac = "MAC address record not found";// "Unique MAC Address";
//									}
//								}
//							}
//						} else {
//							message_mac = "Incorrect MAC Address: Please check MAC address format";
//							show = false;
//						}
//						
//						
//						// ���dip
//						if (ip_choice.equals("2")) {
//							// �Y�O�� ipnetwork �n�[�W������ip
//							
//							APIEntity[] networks = service.searchByObjectTypes(Tools.getIPNETWORKwithoutName(ip_choice_network),
//											ObjectTypes.IP4Network, 0, 9999);
//							String can_use_ip="";
//							if (networks !=null){
//								can_use_ip=service.getNextAvailableIP4Address(networks[0].getId());
//							 
//								//��assign ip�����P��, ������DIY����+1�� IP
//								if (!OldIP.equals(can_use_ip)){
//									//file_data[1]=can_use_ip;
//									data[1]=can_use_ip;
//									OldIP = can_use_ip;
//									OldDyIP=can_use_ip;
//								}else{ 
//									String[] ip_tmp_array = OldDyIP.split("\\.");
//									int ip3=Integer.valueOf(ip_tmp_array[2]);
//									int ip4=Integer.valueOf(ip_tmp_array[3]);
//									ip4=ip4+1;
//									if (ip4>255){
//										ip4=1;
//										ip3=ip3+1;
//									}
//									can_use_ip =ip_tmp_array[0]+"."+ip_tmp_array[1]+"."+ String.valueOf(ip3)+"."+String.valueOf(ip4);
//									data[1]=can_use_ip;
//									OldDyIP=can_use_ip;
//								}
//							}
//							message_ip = "System Assigned";		
//						} else {
//							// �Y�O�� ip �N���d�������e��
//							if (data[1].trim().length() > 0) {
//								String[] ip_marray = data[1].trim()
//										.split("\\.");
//								if (ip_marray.length != 4) {
//									message_ip = "Incorrect IP Address: Please check IP address format";
//									// show = false;
//								} else {
//									if (Tools.checkIPAddress(ip_marray)) {
//										message_ip = "Incorrect IP Address: Please check IP address format";
//										// show = false;
//									} else {
//										try {
//											config = service.getEntityByName(0,
//													select_config,
//													ObjectTypes.Configuration);
//											id = config.getId();
//											try {
//												APIEntity config_ip = service
//														.getIP4Address(id,
//																data[1]);
//												if (config_ip != null) {
//													// show = false;
//													message_ip = "Duplicated IP Address";
//													// harry
//													erro_ip_number++;
//												} else {
//													message_ip = "Free IP Address";
//												}
//											} catch (RemoteException e2) {
//												message_ip = "IP Address Check Failure: Please check input IP address ";
//												//show = false;
//											}
//										} catch (RemoteException e1) {
//											e1.printStackTrace();
//										}
//									}
//								}
//							} else {
//								message_ip = "Incorrect IP Address: Please check IP address format";
//								// show = false;
//							}
//						}
//
//						// �}�l�������X
//						try {
//							if (check_MACAddress.equals("one") && show) {
//								out.write("<tr  style='background:#FFCDE5'>");
//								//�n�A�P�_ip���L����
//								if (message_ip.equals("Duplicated IP Address")){
//									lock = false;
//								}else{
//									lock = true;
//								}
//							} else if (check_MACAddress.equals("repeat") && show) {
//								out.write("<tr  style='background:#CDFFCE'>");
//							} else {
//								if (i % 2 == 0) {
//									out.write("<tr  style='background:#CDFEFF'>");
//								} else {
//									out.write("<tr  style='background:#FFFEFF'>");
//								}
//							}
//						} catch (RemoteException e) {
//							message_mac = "MAC Address Check Failure: Please check input MAC address";
//							show = false;
//						}
//						// if (message_ip.length() != 0) {
//						// erro_ip_number++;
//						// }
//						for (int j = 0; j < data.length + 1; j++) {
//							if (j != data.length) {
//								out.write("<td align=left>" + data[j]);
//								if (j == 1 && message_ip.length() != 0) {
//									out.write("<br><FONT COLOR='#FF0000'>("
//											+ message_ip + ")</FONT>");
//								}
//								if (j == 0 && message_mac.length() != 0) {
//									out.write("<br><FONT COLOR='#FF0000'>("
//											+ message_mac + ")</FONT>");
//								}
//								out.write("</td>");
//							} else {
//								if (lock) {
//									out.write("<td>");
//									// out.write("<input type='checkbox' onclick='return false' name='choose_data' value='"
//									// + i + "' checked='checked'>");
//									out.write("<input type='checkbox' name='choose_data' value='"
//											+ i + "' checked='checked'>");
//									out.write("</td>");
//								} else {
//									out.write("<td>");
//									if (show) {
//										out.write("<input type='checkbox' name='choose_data' value='"
//												+ i + "'>");
//									}
//									out.write("</td>");
//								}
//							}
//						}
//						out.write("<td align=left>" + format.format(new Date())+"</td>");
//						out.write("</tr>");
//						String arrayString = Arrays.toString(data).replaceAll("\\[", "").replaceAll("\\]", "");
//						file_data[i]=arrayString;
//					}
//					//harry
//					//if (erro_ip_number == file_data.length) {
//					if (erro_ip_number >1) {
//						out.write("</table>"); 
//						out.write("<input class='btn btn-default' type='button' value='overwrite' onclick=\"return checkall();\">");
//						out.write("<input class='btn btn-default' type='submit' value='Back' onclick=\"form1.action='/FiveHundredNet/BlueCat/BackPage?choose=AddPage&log=multiCheckErro'\">");
//					} else { 
//						session.put("file_data", file_data);
//						
//						out.write("</table>");
//						out.write("<input class='btn btn-default' type='button' value='Add' onclick=\"return checkall();\">");
//						out.write("<input class='btn btn-default' type='submit' value='Back' onclick=\"form1.action='/FiveHundredNet/BlueCat/BackPage?choose=MultiMACAddress&log=multiUserCancel'\">");
//					}
//				} else {
//					out.write("<input class='btn btn-default' type='submit' value='Back' onclick=\"form1.action='/FiveHundredNet/BlueCat/BackPage?choose=MultiMACAddress&log=noData'\">");
//				}
//			} else {
//				out.write("<tr><td>" + read_error_message + "</td></tr>");
//				String userName = (String) session.get("userName");
//				String log_Content = userName + "," + "" + "," + "" + ","
//						+ "Read File" + "," + read_error_message + ","
//						+ Tools.getTime() + "," + "";
//				LogMian.writeLog(log_Content, userName);
//			}
		} catch (IOException e) {
			String userName = (String) session.get("userName");
			String log_Content = userName + "," + "" + "," + "" + ","
					+ "Bulk MAC Address" + "," + "Bulk read system error" + ","
					+ Tools.getTime() + "," + "";
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
				if (changUpper(mac_address).equals(container)) {
					ans = "repeat";
					break;
				}
			}
		}
		return ans;
	}
	
	private List<OneMacAddress> readImportCSVFile(String file_path){
		String csvFile = file_path;
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		List<OneMacAddress> result = new ArrayList<OneMacAddress>();
		
		try {
			 
			br = new BufferedReader(new FileReader(csvFile));
			line = br.readLine(); // read the header away
			while ((line = br.readLine()) != null) {
			        // use comma as separator
				System.out.println("Reading line from CSV file : " + line); 
				String[] data = line.split(cvsSplitBy, -1);
				System.out.println("After spliting line data is : " + data.toString() + " with length " + data.length);
				for (String s : data){
					System.out.println(" - " + s);
				}
				OneMacAddress oneMacAddress = new OneMacAddress();
				oneMacAddress.setMAC_Address(data[ImportAttr.MAC_ADDR.getIndex()]);
				oneMacAddress.setIP_Address(data[ImportAttr.IP_ADDR.getIndex()]);
				oneMacAddress.setMachine_Type(data[ImportAttr.MACHINE_TYPE.getIndex()]);
				oneMacAddress.setLocation(data[ImportAttr.LOCATION.getIndex()]);
				oneMacAddress.setOwner(data[ImportAttr.OWNER.getIndex()]);
				oneMacAddress.setDepartment(data[ImportAttr.DEPT.getIndex()]);
				oneMacAddress.setPhone_Number(data[ImportAttr.PHONE_NUM.getIndex()]);
				oneMacAddress.setReference(data[7]); // design flaw!!!!
				
				result.add(oneMacAddress);

			}
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return result;
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
