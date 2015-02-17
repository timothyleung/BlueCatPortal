package tw.com.fivehundred.history;

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

public class History extends ActionSupport {

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
			requst.setAttribute("history", "style='background-color:#E7CDFF;'");
			requst.setAttribute("seach", "");
			requst.setAttribute("whichPage",
					"/WEB-INF/Menu/LeftMenuHistory.jsp");
			String choose = requst.getParameter("choose");
			if (choose.equals("HistoryData")) {
				requst.setAttribute("ContentPage",
						"/WEB-INF/ContentPage/History/History.jsp");
				jump_login = false;
			}
			if (choose.equals("HistoryRemove")) {
				requst.setAttribute("ContentPage",
						"/WEB-INF/ContentPage/History/HistoryRemove.jsp");
				jump_login = false;
			}
		}
		if (jump_login) {
			requst.setAttribute("erroMessage", "Please login system");
			return "Login";
		} else {
			return "HistoryPage";
		}
	}
}