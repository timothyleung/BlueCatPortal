package tw.com.fivehundred.down;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.RespectBinding;
import javax.xml.ws.Response;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import tw.com.fivehundred.add.been.OneMacAddress;
import tw.com.fivehundred.tool.Tools;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import com.opensymphony.xwork2.ActionSupport;

public class DownDelip extends ActionSupport {

	public InputStream getDownloadFile() {
		return ServletActionContext.getServletContext().getResourceAsStream("/exampledelip.csv");
	}

	@Override
	public String execute() throws Exception {
		return "DownPage";
	}

}