package tw.com.fivehundred.tag.history;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;

@SuppressWarnings("serial")
public class HistoryRemove extends TagSupport {
	private String title[] = { "NO", "User","Type", "Object", "Action","Result","Date","Change" };
	// private String data[] = {
	// "1","hans","2014/05/02","µn¤J","2","hans","2014/05/02","¼W¥[ip" };
	private String file_path;

	@Override
	public int doStartTag() throws JspException {
		//HttpServletRequest request = (HttpServletRequest) this.pageContext
		//		.getRequest();
		//Map session = ActionContext.getContext().getSession();
		//String user_name=(String)session.get("userName");
		//file_path = ServletActionContext.getServletContext().getRealPath( "/file/" + user_name + "/Log/");
		file_path = ServletActionContext.getServletContext().getRealPath( "/file/sys/Log/");
		//JspWriter out = this.pageContext.getOut();
		try {
			//Files.delete(file_path+"log.txt");
			File file = new File(new File( file_path), "log.txt");
			//file.deleteOnExit();
			file.delete(); 
			file.createNewFile();
			
			HttpServletResponse response = ServletActionContext.getResponse();
			response.sendRedirect("/FiveHundredNet/BlueCat/HistoryPage?choose=HistoryData");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.doStartTag();
	}
 
}
