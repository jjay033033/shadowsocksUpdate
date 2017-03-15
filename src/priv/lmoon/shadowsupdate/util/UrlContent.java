/**
 * 
 */
package priv.lmoon.shadowsupdate.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

import priv.lmoon.shadowsupdate.config.TextConfigList;
import priv.lmoon.shadowsupdate.vo.ServerConfigVO;

/**
 * @author guozy
 * @date 2017-1-6
 * 
 */
public class UrlContent {
	
	private static final Logger logger = Logger.getLogger(UrlContent.class);
	
	public static String getURLContent(ServerConfigVO vo){
		return getURLContent(vo.getUrl(), vo.getBegin(), vo.getEnd());
	}
	
	private static String getURLContent(String urlStr,String beginStr,String endStr) {
		InputStreamReader isr = null;
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer();
		try {
			URL url = new URL(urlStr);
			URLConnection connection = url.openConnection();
			connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			isr = new InputStreamReader(connection.getInputStream(), "utf-8");
			br = new BufferedReader(isr);
			String buf = null;
			boolean begin = false;
			if(beginStr==null||beginStr.isEmpty()){
				begin = true;
			}
			while ((buf = br.readLine()) != null) {
				if(begin){
					sb.append(buf.trim());
					if((endStr!=null&&!endStr.isEmpty()) && buf.contains(endStr)){
						break;
					}
				}else{
					if (buf.contains(beginStr)) {
						begin = true;
						sb.append(buf.trim());
					} 
				}
				
//				if (buf.contains(beginStr)) {
//					begin = true;
//					sb.append(buf.trim());
//				} else if (begin && buf.contains(endStr)) {
//					sb.append(buf.trim());
//					break;
//				} else if (begin) {
//					sb.append(buf.trim());
//				}
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getURLContent:"+urlStr, e);
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
