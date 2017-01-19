/**
 * 
 */
package priv.lmoon.shadowsupdate.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.log4j.Logger;

import priv.lmoon.shadowsupdate.config.IshadowsocksConfigList;

/**
 * @author guozy
 * @date 2017-1-6
 * 
 */
public class UrlContent {
	
	private static final Logger logger = Logger.getLogger(UrlContent.class);
	
	public static String getURLContent(String urlStr,String beginStr,String endStr) {
		InputStreamReader isr = null;
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer();
		try {
			URL url = new URL(urlStr);
			isr = new InputStreamReader(url.openStream(), "utf-8");
			br = new BufferedReader(isr);
			String buf = null;
			boolean begin = false;
			while ((buf = br.readLine()) != null) {
				if (buf.contains(beginStr)) {
					begin = true;
					sb.append(buf.trim());
				} else if (begin && buf.contains(endStr)) {
					sb.append(buf.trim());
					break;
				} else if (begin) {
					sb.append(buf.trim());
				}
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getURLContent:", e);
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (isr != null) {
					isr.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
