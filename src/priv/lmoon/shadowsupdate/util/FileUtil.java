package priv.lmoon.shadowsupdate.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

public class FileUtil {
	
	private static final Logger logger = Logger.getLogger(FileUtil.class);
	
	public static String readFile(String pathname) {
		File file = null;
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer();
		try {
			// ./gui-config.json
			file = new File(pathname);
			if (file == null || !file.exists()) {
				return null;
			}
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, "utf-8");
			br = new BufferedReader(isr);
			String buf = null;
			while ((buf = br.readLine()) != null) {
				sb.append(buf);
			}
		} catch (Exception e) {
			logger.error("readFile:", e);
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (isr != null) {
					isr.close();
				}
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static void writeFile(String content, String pathname) {
		File file = null;
		FileOutputStream fos = null;
		content.getBytes();
		try {
			// ./gui-config.json
			file = new File(pathname);
			if (file.exists()) {
				file.delete();
			}
			fos = new FileOutputStream(file);
			fos.write(content.getBytes("utf-8"));
		} catch (Exception e) {
			logger.error("writeFile:", e);
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
