package tw.com.fivehundred.tag.base;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts2.ServletActionContext;

import com.bluecatnetworks.proteus.api.client.java.constants.ObjectTypes;
import com.bluecatnetworks.proteus.api.client.java.proxy.APIEntity;
import com.bluecatnetworks.proteus.api.client.java.proxy.ProteusAPI_PortType;
import com.opensymphony.xwork2.ActionContext;

@SuppressWarnings("serial")
public class SelectList extends TagSupport {
	@Override
	public int doStartTag() throws JspException {

		ServletContext servletContext = ServletActionContext
				.getServletContext();
		HttpServletRequest requst = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		Map session = ActionContext.getContext().getSession();
		String check_pass = (String) session.get("check_pass");
		JspWriter out = this.pageContext.getOut();
		try {
			ProteusAPI_PortType service = (ProteusAPI_PortType) session
					.get("ready_server");
			if (service != null) {
				// 抓取Configuration裡所有的名稱
				APIEntity[] fields;
				fields = service.getEntities(0, ObjectTypes.Configuration, 0,
						999);
				out.write("<select name='select_config'>");
				for (int i = 0; i < fields.length; i++) {
					out.write("<option vaule='" + fields[i].getName() + "'>"
							+ fields[i].getName() + "</option>");
				}
				out.write("</select>");
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return super.doStartTag();
	}
}
