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

public class MultiAddressChoice extends TagSupport {
	private String file_path;
	private String title[] = { "MAC Address", "IP Address",
			"Select To Delete"};
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
		String select_config = "hkbu";//(String) session.get("select_config");
		session.remove("select_config");
		//String[] file_data = readFile();
		JspWriter out = this.pageContext.getOut();
		String[] data = null;
		APIEntity[] fields = null;
		ProteusAPI_PortType service = (ProteusAPI_PortType) session
				.get("ready_server");
		request.setAttribute("select_config", select_config);

		APIEntity config;
		long id = 0;
		try { 
			out.write("<table cellspacing='0' width='100%' border='1' borderColor='#DEE6EE'><tr>"); 
			out.write("<td align=left><input type='radio' name='ip_choice' value='mac' checked >MAC Address List <input type='radio' name='ip_choice' value='ip' >IP Address List</td>");
			out.write("</tr></table>");
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
				if (changUpper(mac_address).equals(container)) {
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
