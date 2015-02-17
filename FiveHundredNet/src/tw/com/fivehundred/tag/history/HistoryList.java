package tw.com.fivehundred.tag.history;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;

@SuppressWarnings("serial")
public class HistoryList extends TagSupport {
	private String title[] = { "NO", "User","Type", "Object", "Action","Result","Date","Change" };
	// private String data[] = {
	// "1","hans","2014/05/02","µn¤J","2","hans","2014/05/02","¼W¥[ip" };
	private String file_path;

	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) this.pageContext
				.getRequest();
		Map session = ActionContext.getContext().getSession();
		String user_name=(String)session.get("userName");
		//file_path = ServletActionContext.getServletContext().getRealPath( "/file/" + user_name + "/Log/");
		file_path = ServletActionContext.getServletContext().getRealPath( "/file/sys/Log/");
		JspWriter out = this.pageContext.getOut();
		try {
			String[] data_original=readFile();
			ArrayList<ArrayList<String>> list_data=new ArrayList<ArrayList<String>>();
			String[] data_cut=null;
			for(int i=0;i<data_original.length;i++){
				data_cut=data_original[i].split(",");
				ArrayList<String> data_cut_list=new ArrayList<String>();
				data_cut_list.add(String.valueOf(i));
				for(int j=0;j<data_cut.length;j++){					
					data_cut_list.add(data_cut[j]);
				}
				if(data_cut_list.size()<8){
					data_cut_list.add("");
				}
				list_data.add(data_cut_list);
			}
			out.write("<tr>");
			for (int i = 0; i < title.length; i++) {
				out.write("<td align=left>" + title[i] + "&nbsp;</td>");
			}
			out.write("<tr>");
			for(int i=list_data.size()-1;i>=0;i--){	
				out.write("<tr>");
				for (int j =0; j<list_data.get(i).size() ; j++) {
					out.write("<td align=left>" + list_data.get(i).get(j) + "&nbsp;</td>");
				}
				out.write("<tr>");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.doStartTag();
	}

	private String[] readFile() {
		BufferedReader dllRead = null;
		ArrayList<String> fileData = new ArrayList<String>();
		String aa = "";
		try {
			dllRead = new BufferedReader(new FileReader(new File(new File(
					file_path), "log.txt")));
			try {
				while ((aa = dllRead.readLine()) != null) {
					fileData.add(aa);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				dllRead.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String[] data = new String[fileData.size()];
		data = fileData.toArray(data);
		return data;
	}
}
