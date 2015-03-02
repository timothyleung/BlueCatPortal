package tw.com.fivehundred.tag.add;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
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

public class MultiMACAddressAdd extends TagSupport {
	private String serverid1="BDDS1";
	private String serverid2="BDDS2";
	private String file_path;

	private SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private String title[] = { "MAC Address", "IP Address", "Machine Type",
			"Location", "Owner", "Department", "Phone Number", "Reference",
			"Input Date","Overwrite" };
	private String vauleName[] = { "MAC_Address", "IP_Address", "Machine_Type",
			"Location", "Owner", "Department", "Phone_Number", "Reference",
			"Input_Date", "choose_data" };
	private String read_error_message = "";

	@Override
	public int doStartTag() throws JspException {
		ServletContext servletContext = ServletActionContext
				.getServletContext();
		HttpServletRequest request = (HttpServletRequest) this.pageContext
				.getRequest();
		Map session = ActionContext.getContext().getSession();
		HttpServletResponse response = ServletActionContext.getResponse();
		String choose_data = (String) session.get("choose_data"); // return the checked index 1, 2, 3..
		String select_config = "hkbu";//(String) session.get("select_config");
		String select_servers = (String) session.get("select_servers");
		String[] ip_rows_data = (String[]) session.get("ip_rows_data");
		String ip_choice = (String) session.get("ip_choice");
		file_path = (String) session.get("file_path");
		session.remove("select_servers");
		session.remove("file_path");
		session.remove("choose_data");
		session.remove("select_config");
		session.remove("ip_choice");

		// String[] file_data =  (String[]) session.get("file_data");// readFile();
		String[] file_data = readFile();

		System.out.println("Printing out file data"  );
		for(String s : file_data){
			System.out.println("[*] " + s) ;
		}
		String[] data = null;
		JspWriter out = this.pageContext.getOut();
		ProteusAPI_PortType service = (ProteusAPI_PortType) session
				.get("ready_server");
		request.setAttribute("select_config", select_config);

		try {
			APIEntity config = service.getEntityByName(0, select_config,
					ObjectTypes.Configuration);
			long id = config.getId();
			if (choose_data != null) {
				String[] choose_string = choose_data.split(",");
				Integer[] choose = new Integer[choose_string.length];
				for (int i = 0; i < choose_string.length; i++) {
					choose[i] = Integer.valueOf(choose_string[i].trim());
				}
				String userName = (String) session.get("userName");
				String log_Content = "";
				for (int i = 0; i < choose.length; i++) {
					log_Content = "";
					data = file_data[choose[i]].split(",", -1);
					
					// �R��IP 
					APIEntity config7;
					if(!ip_choice.equals("2")){
						config7 = service.getIP4Address(id, data[1].trim());
						if (config7!=null){
						service.delete(config7.getId());
						} 
					}					
					//�R��mac
					config7 = service.getMACAddress(id, data[0].trim());
					if (config7!=null){
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
					}
					
					EntityProperties props0 = new EntityProperties();
					props0.addProperty(ObjectProperties.name, "hans");
					String user_defined = "";

					user_defined = user_defined + "ip_machine_type=" + data[2].trim()
							+ "|";
					user_defined = user_defined + "ip_location=" + data[3].trim()
							+ "|";
					user_defined = user_defined + "ip_device_ower=" + data[4].trim()
							+ "|";
					user_defined = user_defined + "ip_department=" + data[5].trim()
							+ "|";
					user_defined = user_defined + "ip_phone_number=" + data[6].trim()
							+ "|";
					user_defined = user_defined + "ip_input_date=" + format.format(new Date())
							+ "|";
					user_defined = user_defined + "ip_reference=" + data[7].trim()
							+ "|";
					try {
						long mac_long = service.addMACAddress(id, data[0].trim(),
								props0.getPropertiesString());
						// �[�Jip
						long ip_ans;
						if(ip_choice.equals("2")){
							 ip_ans = service.assignIP4Address(id, ip_rows_data[choose[i]-1].trim(),
									data[0].trim(), "|", "MAKE_DHCP_RESERVED",
									user_defined);
						} else {
							 ip_ans = service.assignIP4Address(id, data[1].trim(),
									data[0].trim(), "|", "MAKE_DHCP_RESERVED",
									user_defined);
						}
						log_Content = userName + "," + "Multi Address" + ","
								+ data[0].trim() + "," + "Add" + "," + "Success" + ","
								+ Tools.getTime() + "," + "MAC Address Created";
						LogMian.writeLog(log_Content, userName);
					} catch (Exception e) {
						if (e.toString().equals("Duplicate of another item")) {
							// �[�Jip
							long ip_ans = service.assignIP4Address(id, data[1].trim(),
									data[0].trim(), "|", "MAKE_DHCP_RESERVED",
									user_defined);
							log_Content = userName + "," + "Multi Address"
									+ "," + data[1].trim() + "," + "Add" + ","
									+ "Success" + "," + Tools.getTime() + ","
									+ "Add MAC Address";
							LogMian.writeLog(log_Content, userName);
						}
					}
				}
			} else {
				out.write("<tr><td>" + read_error_message + "</td></tr>");
				String userName = (String) session.get("userName");
				String log_Content = userName + "," + "" + "," + "" + ","
						+ "Read File" + "," + read_error_message + ","
						+ Tools.getTime() + "," + "";
				LogMian.writeLog(log_Content, userName);
			}
			session.put("erroMessage", "Multi Mac Add Completed");
			
			//harry
			//APIEntity service_data = service.getEntityByName(id,
			//		select_servers, ObjectTypes.Server);
			APIEntity service_data = service.getEntityByName(id,
					serverid1, ObjectTypes.Server);
			APIEntity service_data2 = service.getEntityByName(id,
					serverid2, ObjectTypes.Server);
//			service.deployServer(service_data.getId());
			//harry
			service.deployServerServices(service_data.getId(),"services=DHCP");
			service.deployServerServices(service_data2.getId(),"services=DHCP");
			
			
			response.sendRedirect("/FiveHundredNet/BlueCat/AddPage?choose=MultiMACAddress");
		} catch (Exception e1) {
			e1.printStackTrace();
			try {
				String userName = (String) session.get("userName");
				String log_Content = userName + "," + "" + "," + "" + "," + ""
						+ "," + "Bulk MAC address import system error" + ","
						+ Tools.getTime() + "," + "";
				LogMian.writeLog(log_Content, userName);
				out.write("<FONT COLOR='#FF0000'>"
						+ "Bulk MAC address import system error" + "</FONT>");
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
