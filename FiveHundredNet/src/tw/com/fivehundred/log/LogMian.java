package tw.com.fivehundred.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.struts2.ServletActionContext;

public class LogMian {
	public static void checkLogFile(String user_name) {
		//String realpath = ServletActionContext.getServletContext().getRealPath( "/file/" + user_name + "/Log/");
		//harry
		String realpath = ServletActionContext.getServletContext().getRealPath( "/file/sys/Log/");
		File savefile = new File(new File(realpath), "log.txt");
		if (!savefile.getParentFile().exists()) {
			savefile.getParentFile().mkdirs();
		}
		if (!savefile.exists()) {
			try {
				savefile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void writeLog(String fileContent, String user_name) {
		//String realpath = ServletActionContext.getServletContext().getRealPath( "/file/" + user_name + "/Log/");
		//harry
		String realpath = ServletActionContext.getServletContext().getRealPath( "/file/sys/Log/");
		File savefile = new File(new File(realpath), "log.txt");

		FileWriter resultFile;
		try {
			resultFile = new FileWriter(savefile, true);
			BufferedWriter bw = new BufferedWriter(resultFile);
			bw.write(fileContent);
			bw.newLine();
			bw.close();
			resultFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
