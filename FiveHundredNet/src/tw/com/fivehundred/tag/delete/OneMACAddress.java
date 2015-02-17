package tw.com.fivehundred.tag.delete;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import tw.com.fivehundred.add.been.OneMacAddress;

import com.opensymphony.xwork2.ActionContext;

public class OneMACAddress extends TagSupport {

	private String title[] = { "MAC Address" };
	private String vauleName[] = { "MAC_Address" };

	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) this.pageContext
				.getRequest();
		JspWriter out = this.pageContext.getOut();


		try {
			for (int i = 0; i < title.length; i++) {
				out.write("<tr><th align=left>" + title[i] + "</th><td>");
				out.write("<input type='text' size='20' name='"
						+ vauleName[i]
						+ "'"
						+ " onfocus=\"if (this.value=='xx-xx-xx-xx-xx-xx') this.value='';\" onblur=\"if (this.value=='') this.value='xx-xx-xx-xx-xx-xx';\" value=\"xx-xx-xx-xx-xx-xx\""
						+ ">");
				out.write("</td></tr>");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.doStartTag();
	}
}
