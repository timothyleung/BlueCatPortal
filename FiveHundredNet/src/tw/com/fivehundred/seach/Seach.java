package tw.com.fivehundred.seach;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.RespectBinding;
import javax.xml.ws.Response;

import org.apache.struts2.ServletActionContext;

import com.bluecatnetworks.proteus.api.client.java.proxy.ProteusAPI_PortType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class Seach extends ActionSupport {
	private String select_ans;
	private String seach_text;
	private String select_config;
	public String execute() {
		boolean jump_login = true;
		HttpServletRequest requst = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ServletContext servletContext = ServletActionContext
				.getServletContext();
		Map session = ActionContext.getContext().getSession();
		ProteusAPI_PortType service = (ProteusAPI_PortType) session
				.get("ready_server");
		if (service != null) {
			requst.setAttribute("add", "");
			requst.setAttribute("delete", "");
			requst.setAttribute("history", "");
			requst.setAttribute("seach", "style='background-color:#E7CDFF;'");
			requst.setAttribute("whichPage",
					"/WEB-INF/Menu/LeftMenuForSeach.jsp");
			String choose = requst.getParameter("choose");
			if (choose.equals("SeachData")) {
				requst.setAttribute("ContentPage",
						"/WEB-INF/ContentPage/Seach/Seach.jsp");
				jump_login = false;
			}
			if (choose.equals("check")) {
				String jump = requst.getParameter("jump");
				session.put("select_ans", select_ans);
				session.put("seach_text", seach_text);
				session.put("select_config", select_config);
				if (jump.equals("seach")) {
					requst.setAttribute("ContentPage",
							"/WEB-INF/ContentPage/Seach/SeachShow.jsp");
					jump_login = false;
				}
			}
		}
		if (jump_login) {
			requst.setAttribute("erroMessage", "Please login system");
			return "Login";
		} else {
			return "SeachPage";
		}
	}

	public String getSelect_ans() {
		return select_ans;
	}

	public void setSelect_ans(String select_ans) {
		this.select_ans = select_ans;
	}

	public String getSeach_text() {
		return seach_text;
	}

	public void setSeach_text(String seach_text) {
		this.seach_text = seach_text;
	}

	public String getSelect_config() {
		return select_config;
	}

	public void setSelect_config(String select_config) {
		this.select_config = select_config;
	}

}