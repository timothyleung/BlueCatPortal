package tw.com.fivehundred.tag.delete;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts2.ServletActionContext;

import tw.com.fivehundred.log.LogMian;
import tw.com.fivehundred.tool.AddressType;
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
	private String page_title = "Checking IP/MAC Addresses";
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
		ProteusAPI_PortType service = (ProteusAPI_PortType) session
				.get("ready_server");
		request.setAttribute("select_config", select_config);

		APIEntity config;
		long id = 0;
		try {
			config = service.getEntityByName(0, select_config,
					ObjectTypes.Configuration);
			id = config.getId();
			// fields = Tools.getAllMacAddress(service, id);
			String check_MACAddress = "";
			boolean show_ip = true;
			boolean show_mac = true;
			out.write("<h1>" + page_title +" </h1><br>");
			out.write("<table><tr><td>Bluecat servers : BDDS1 & BDDS2");
			out.write("</td></tr></table>");
			
			// ip or mac
			String ip_choice = (String)session.get("ip_choice");

			// reading file
			List<String> data_file = readCSVFile(file_path);
			System.out.println("Printing data_file : " + data_file.toString());
			if(data_file.size() != 0){
				out.write("<table class=\"table table-striped table-hover table-bordered\">");
	
				if (ip_choice.equals("ip")){
					print_table_header(out, "IP Address", "MAC Address", "Select To Delete");
					Tools.multiple_deletion_check(out, service, config, AddressType.IP, data_file);
				}else{
					print_table_header(out, "MAC Address", "IP Address", "Select To Delete");
					Tools.multiple_deletion_check(out, service, config, AddressType.MAC, data_file);
				}
				out.write("</table>");
				
				out.write("</table><input class=\"btn btn-default\" type='button' value='Delete' onclick=\"return checkalldel();\">");
				out.write("</table><input class=\"btn btn-default\"  type='button' value='Back' onclick=\"top.location='/FiveHundredNet/BlueCat/DeletePage?choose=MultiMACAddress'\">");
			} else {
				out.write("<h3>File is empty</h3>");
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
			e.printStackTrace();
			try {
				out.write("Bulk read system error");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return super.doStartTag();
	}
	
	private static void print_table_header(JspWriter out, String h1, String h2, String h3) throws IOException{
		out.write("<tr><th>" + h1 + "</th><th>" + h2 + "</th><th>" + h3 + "</th></tr>");
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
	
	private List<String> readCSVFile(String file_path){
		String csvFile = file_path;
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		List<String> data = new ArrayList<String>();
	 
		try {
	 
			br = new BufferedReader(new FileReader(csvFile));
			line = br.readLine(); // read the header away
			while ((line = br.readLine()) != null) {
	 
			        // use comma as separator
				System.out.println("Reading line from CSV file : " + line); 
				String[] country = line.split(cvsSplitBy);
				System.out.println("ip/mac = " + country[0] );
				data.add(country[0]);
	 
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
		return data;
	}
}
