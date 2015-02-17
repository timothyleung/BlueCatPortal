package tw.com.fivehundred.tag.delete;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class OneIPAddress extends TagSupport {

	private String title[] ={ "IP Address"};
	private String vauleName[] = {"IP_Address"};

	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) this.pageContext
				.getRequest();
		JspWriter out = this.pageContext.getOut();
		try {
			for (int i = 0; i < title.length; i++) {

				out.write("<tr><th align=left>" + title[i] + "</th><td>");

				out.write("<input type='text' size='20' name='"
						+ vauleName[i] + "'"
						+" onfocus=\"if (this.value=='xxx.xxx.xxx.xxx') this.value='';\" onblur=\"if (this.value=='') this.value='xxx.xxx.xxx.xxx';\" value=\"xxx.xxx.xxx.xxx\""
						+">");
				out.write("</td></tr>");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.doStartTag();
	}
}
