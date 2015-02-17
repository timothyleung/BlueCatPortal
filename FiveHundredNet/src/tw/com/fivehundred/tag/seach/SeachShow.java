package tw.com.fivehundred.tag.seach;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts2.ServletActionContext;

import tw.com.fivehundred.log.LogMian;
import tw.com.fivehundred.tool.Tools;

import com.bluecatnetworks.proteus.api.client.java.constants.ObjectProperties;
import com.bluecatnetworks.proteus.api.client.java.constants.ObjectTypes;
import com.bluecatnetworks.proteus.api.client.java.proxy.APIEntity;
import com.bluecatnetworks.proteus.api.client.java.proxy.ProteusAPI_PortType;
import com.opensymphony.xwork2.ActionContext;

public class SeachShow extends TagSupport {
	private String title[] = { "選項 : ", "搜索文字 : " };
	private String data[] = { "MAC Address", "IP Address", "Machine type",
			"Location", "Owner", "Department" };
	private String vauleName[] = { "address=", "ip_machine_type=",
			"ip_location=", "ip_device_ower=", "ip_department=",
			"ip_phone_number=", "ip_input_date=", "ip_reference=" };
	private String vauleName_ip[] = { "address=", "state=", "macAddress=",
			"ip_machine_type=", "ip_location=", "ip_device_ower=",
			"ip_department=", "ip_phone_number=", "ip_input_date=",
			"ip_reference=" };

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) this.pageContext
				.getRequest();
		Map session = ActionContext.getContext().getSession();
		String select_ans = (String) session.get("select_ans");
		String seach_text = (String) session.get("seach_text");
		String select_config = (String) session.get("select_config");
		session.remove("select_config");
		session.remove("select_ans");
		session.remove("seach_text");
		ProteusAPI_PortType service = (ProteusAPI_PortType) session
				.get("ready_server");
		JspWriter out = this.pageContext.getOut();
		String userName = (String) session.get("userName");
		APIEntity config;
		long id = 0;
		boolean cant_find = true;
		try {
			config = service.getEntityByName(0, select_config,
					ObjectTypes.Configuration);
			id = config.getId();
			if (select_ans.equals("MAC Address")) {
				seach_text = seach_text.trim().replace(":", "-");
				if (seach_text.split("-").length == 1) {
					if (seach_text.indexOf("-") != (seach_text.length() - 1)) {
						String mac_new = "";
						int a = 0;
						for (int k = 0; k < seach_text.trim().length(); k++) {
							if (k != 0 && k % 2 == 0) {
								a++;
								if (k != seach_text.trim().length()) {
									mac_new = mac_new + "-";
								}
							}
							mac_new = mac_new + seach_text.trim().charAt(k);
						}
						seach_text = mac_new;
					} else {
						seach_text = seach_text.trim().replace("-", "");
					}
				}
				if (seach_text == null || seach_text.trim().length() == 0) {
					// 抓取Configuration裡所有的mac address
					APIEntity[] fields_mac = Tools
							.getAllMacAddress(service, id);
					String container_mac = "", container_ip_mac = "", container_other;
					boolean cant_find_mac = true;
					out.write("<tr style='background-color:#55A8FF;'>"
							+ "<td>MAC Address</td><td>IP Address</td><td>Machine Type</td>"
							+ "<td>Location</td><td>Owner</td><td>Department</td>"
							+ "<td>Phone Number</td><td>Input Date</td>"
							+ "<td>Reference</td></tr>");
					for (int i = 0; i < fields_mac.length; i++) {
						container_mac = fields_mac[i].getProperties()
								.substring(
										fields_mac[i].getProperties().indexOf(
												"address=") + 8,
										fields_mac[i].getProperties().indexOf(
												"|",
												fields_mac[i].getProperties()
														.indexOf("address=")));
						APIEntity[] fields_ip = service.getLinkedEntities(
								fields_mac[i].getId(), ObjectTypes.IP4Address,
								0, 10);
						out.write("<td>" + container_mac.trim() + "&nbsp;</td>");
						if (fields_ip.length > 0) {
							for (int k = 0; k < vauleName.length; k++) {
								if (fields_ip[0].getProperties().indexOf(
										vauleName[k]) != -1) {
									container_other = fields_ip[0]
											.getProperties()
											.substring(
													fields_ip[0]
															.getProperties()
															.indexOf(
																	vauleName[k])
															+ vauleName[k]
																	.length(),
													fields_ip[0]
															.getProperties()
															.indexOf(
																	"|",
																	fields_ip[0]
																			.getProperties()
																			.indexOf(
																					vauleName[k])));
									out.write("<td>" + container_other.trim()
											+ "&nbsp;</td>");
								} else {
									out.write("<td></td>");
								}
							}
						}
						out.write("</tr>");
					}
					String log_Content = userName + "," + "Search MAC Address"
							+ "," + "All" + "," + "Search" + "," + "Success"
							+ "," + Tools.getTime() + ","
							+ "Search MAC Address";
					LogMian.writeLog(log_Content, userName);
				} else {
					// 抓取Configuration裡所有的mac address
					APIEntity[] fields_mac = Tools.getMacAddress(service, id,
							seach_text);
					if (fields_mac.length != 0) {
						String container_mac = "", container_ip_mac = "", container_other;
						boolean cant_find_mac = true;
						out.write("<tr style='background-color:#55A8FF;'>"
								+ "<td>MAC Address</td><td>IP Address</td><td>Machine Type</td>"
								+ "<td>Location</td><td>Owner</td><td>Department</td>"
								+ "<td>Phone Number</td><td>Input Date</td>"
								+ "<td>Reference</td></tr>");
						for (int i = 0; i < fields_mac.length; i++) {
							container_mac = fields_mac[i]
									.getProperties()
									.substring(
											fields_mac[i].getProperties()
													.indexOf("address=") + 8,
											fields_mac[i]
													.getProperties()
													.indexOf(
															"|",
															fields_mac[i]
																	.getProperties()
																	.indexOf(
																			"address=")));

							out.write("<tr><td>" + container_mac + "&nbsp;</td>");
							APIEntity[] fields_ip = service.getLinkedEntities(
									fields_mac[i].getId(),
									ObjectTypes.IP4Address, 0, 10);
							if (fields_ip.length > 0) {
								for (int k = 0; k < vauleName.length; k++) {
									if (fields_ip[0].getProperties().indexOf(
											vauleName[k]) != -1) {
										container_other = fields_ip[0]
												.getProperties()
												.substring(
														fields_ip[0]
																.getProperties()
																.indexOf(
																		vauleName[k])
																+ vauleName[k]
																		.length(),
														fields_ip[0]
																.getProperties()
																.indexOf(
																		"|",
																		fields_ip[0]
																				.getProperties()
																				.indexOf(
																						vauleName[k])));
										out.write("<td>"
												+ container_other.trim()
												+ "&nbsp;</td>");
									} else {
										out.write("<td></td>");
									}
								}
							}
							out.write("</tr>");
						}
					} else {
						out.write("No Record Found");
					}
					String log_Content = userName + "," + "Search MAC Address"
							+ "," + seach_text + "," + "Search" + ","
							+ "Success" + "," + Tools.getTime() + ","
							+ "Search MAC Address";
					LogMian.writeLog(log_Content, userName);
				}

		 		
				// search others
			} else if (select_ans.equals("Machine type")
					|| select_ans.equals("Location")
					|| select_ans.equals("Owner")
					|| select_ans.equals("Department")) {
				// DeviceType

				if (seach_text != null || seach_text.trim().length() == 0) {
					
					

					// 所有有用到的ip
					APIEntity[] fields = null;
					int count_number = 0;
					do {
						fields = service.searchByObjectTypes(seach_text,
								ObjectTypes.IP4Address, 1000 * count_number,
								999);
						String container_ip = "";
						String container_Status = "";
						String container_mac = "";
						if (count_number == 0) {
							out.write("<tr style='background-color:#55A8FF;'><td>IP Address</td>"
									+ "<td>Status</td><td>MAC Address</td><td>Machine Type</td>"
									+ "<td>Location</td><td>Owner</td><td>Department</td>"
									+ "<td>Phone Number</td><td>Input Date</td>"
									+ "<td>Reference</td></tr>");
						}
						count_number++;
						String container_other = "";
						for (int i = 0; i < fields.length; i++) {
							container_other = "";
							out.write("<tr>");
							for (int k = 0; k < vauleName_ip.length; k++) {
								if (fields[i].getProperties().indexOf(
										vauleName_ip[k]) != -1) {
									container_other = fields[i]
											.getProperties()
											.substring(
													fields[i]
															.getProperties()
															.indexOf(
																	vauleName_ip[k])
															+ vauleName_ip[k]
																	.length(),
													fields[i]
															.getProperties()
															.indexOf(
																	"|",
																	fields[i]
																			.getProperties()
																			.indexOf(
																					vauleName_ip[k])));
									//if (k == 0) {
									//	container_ip = container_other.trim();
									//}
									//if (container_ip.indexOf(seach_text) != -1) {
										out.write("<td>"
												+ container_other.trim()
												+ "&nbsp;</td>");
									//}
								} else {
									out.write("<td></td>");
								}
								cant_find = false;
							}
							out.write("</tr>");
						}
					} while (fields.length == 999);
					if (cant_find) {
						out.write("No Record Found");
					}
					String log_Content = userName + "," + "Search IP Address"
							+ "," + seach_text + "," + "Search" + ","
							+ "Success" + "," + Tools.getTime() + ","
							+ "Search IP Address";
					LogMian.writeLog(log_Content, userName);
				
					
					
				}
 
				
				/*
				 * } else if (select_ans.equals("Location")){ //DeviceType
				 * 
				 * if (seach_text == null || seach_text.trim().length() == 0) {
				 * 
				 * }
				 * 
				 * 
				 * } else if (select_ans.equals("Owner")){ //DeviceType
				 * 
				 * if (seach_text == null || seach_text.trim().length() == 0) {
				 * 
				 * }
				 * 
				 * 
				 * } else if (select_ans.equals("Department")){ //DeviceType
				 * 
				 * if (seach_text == null || seach_text.trim().length() == 0) {
				 * 
				 * }
				 */

				// search ip
			} else {
				
				if (seach_text == null || seach_text.trim().length() == 0) {
					// 該有的ip
					// 所有有用到的BLOCK
					APIEntity[] fields = service.getEntities(id,
							ObjectTypes.IP4Block, 0, 9999);

					APIEntity[] fields1 = null;
					String container_cidr = "";

					for (int j = 0; j < fields.length; j++) {
						if (fields[j].getProperties().indexOf("CIDR=") != -1) {
							container_cidr = fields[j].getProperties()
									.substring(
											fields[j].getProperties().indexOf(
													"CIDR=") + 5,
											fields[j].getProperties().indexOf(
													"|",
													fields[j].getProperties()
															.indexOf("CIDR=")));
						}

						if (j != 0) {
							out.write("</table>");
						}

						out.write("<div>CIDR=" + container_cidr + "</div>");
						if (j != 0) {
							out.write("<table cellspacing='0' width='200%' border='1' borderColor='#DEE6EE'>");
						}
						// 所有的mac
						int count_number = 0;
						do {
							fields1 = service.searchByObjectTypes(
									container_cidr.split("\\.")[0],
									ObjectTypes.IP4Address,
									1000 * count_number, 999);
							String container_ip = "";
							String container_Status = "";
							String container_mac = "";
							if (count_number == 0) {
								out.write("<tr style='background-color:#55A8FF;'><td>IP Address</td>"
										+ "<td>Status</td><td>MAC Address</td><td>Machine Type</td>"
										+ "<td>Location</td><td>Owner</td><td>Department</td>"
										+ "<td>Phone Number</td><td>Input Date</td>"
										+ "<td>Reference</td></tr>");
							}
							count_number++;
							String container_other = "";
							for (int i = 0; i < fields1.length; i++) {
								container_other = "";
								out.write("<tr>");
								for (int k = 0; k < vauleName_ip.length; k++) {
									if (fields1[i].getProperties().indexOf(
											vauleName_ip[k]) != -1) {
										container_other = fields1[i]
												.getProperties()
												.substring(
														fields1[i]
																.getProperties()
																.indexOf(
																		vauleName_ip[k])
																+ vauleName_ip[k]
																		.length(),
														fields1[i]
																.getProperties()
																.indexOf(
																		"|",
																		fields1[i]
																				.getProperties()
																				.indexOf(
																						vauleName_ip[k])));
										if (k == 0) {
											container_ip = container_other
													.trim();
										}
										if (container_ip.indexOf(container_cidr
												.split("\\.")[0]) != -1) {
											out.write("<td>"
													+ container_other.trim()
													+ "</td>");
										}
									} else {
										out.write("<td></td>");
									}
									cant_find = false;
								}
								out.write("</tr>");
							}
						} while (fields1.length == 999);
					}
					if (cant_find) {
						out.write("No Record Found");
					}
					String log_Content = userName + "," + "Search IP Address"
							+ "," + "All" + "," + "Search" + "," + "Success"
							+ "," + Tools.getTime() + "," + "Search IP Address";
					LogMian.writeLog(log_Content, userName);

				} else {
					// 所有有用到的ip
					APIEntity[] fields = null;
					int count_number = 0;
					do {
						fields = service.searchByObjectTypes(seach_text,
								ObjectTypes.IP4Address, 1000 * count_number,
								999);
						String container_ip = "";
						String container_Status = "";
						String container_mac = "";
						if (count_number == 0) {
							out.write("<tr style='background-color:#55A8FF;'><td>IP Address</td>"
									+ "<td>Status</td><td>MAC Address</td><td>Machine Type</td>"
									+ "<td>Location</td><td>Owner</td><td>Department</td>"
									+ "<td>Phone Number</td><td>Input Date</td>"
									+ "<td>Reference</td></tr>");
						}
						count_number++;
						String container_other = "";
						for (int i = 0; i < fields.length; i++) {
							container_other = "";
							out.write("<tr>");
							for (int k = 0; k < vauleName_ip.length; k++) {
								if (fields[i].getProperties().indexOf(
										vauleName_ip[k]) != -1) {
									container_other = fields[i]
											.getProperties()
											.substring(
													fields[i]
															.getProperties()
															.indexOf(
																	vauleName_ip[k])
															+ vauleName_ip[k]
																	.length(),
													fields[i]
															.getProperties()
															.indexOf(
																	"|",
																	fields[i]
																			.getProperties()
																			.indexOf(
																					vauleName_ip[k])));
									if (k == 0) {
										container_ip = container_other.trim();
									}
									if (container_ip.indexOf(seach_text) != -1) {
										out.write("<td>"
												+ container_other.trim()
												+ "</td>");
									}
								} else {
									out.write("<td></td>");
								}
								cant_find = false;
							}
							out.write("</tr>");
						}
					} while (fields.length == 999);
					if (cant_find) {
						out.write("No Record Found");
					}
					String log_Content = userName + "," + "Search IP Address"
							+ "," + seach_text + "," + "Search" + ","
							+ "Success" + "," + Tools.getTime() + ","
							+ "Search IP Address";
					LogMian.writeLog(log_Content, userName);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.doStartTag();
	}
}
