package tw.com.fivehundred.tag.seach;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class Seach extends TagSupport {
	private String title[] = { "Option : ","Key word search : "};
	private String data[] = { "MAC Address","IP Address","Machine type","Location","Owner" ,"Department" };
	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) this.pageContext
				.getRequest();
		JspWriter out = this.pageContext.getOut();
		try {
			out.write("<tr>");
			out.write("<td align=left>" + title[0] + "&nbsp;</td>");
			out.write("<td><select name='select_ans'>");
			for(int i=0;i<data.length;i++){
				out.write("<option vaule='"+data[i]+"'>" + data[i] + "</option>");
			}
			out.write("</select></td>");
			out.write("</tr>");
			out.write("<tr>");
			out.write("<td align=left>" + title[1] + "&nbsp;</td>");
			out.write("<td><input type='text' size='20' name='seach_text'></td>");
			out.write("</tr>");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.doStartTag();
	}
}
