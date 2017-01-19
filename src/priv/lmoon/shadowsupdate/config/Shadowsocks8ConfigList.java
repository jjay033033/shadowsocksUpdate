/**
 * 
 */
package priv.lmoon.shadowsupdate.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import priv.lmoon.shadows.QRCode.Base64Coder;
import priv.lmoon.shadows.QRCode.QRcoder;
import priv.lmoon.shadowsupdate.util.UrlContent;
import priv.lmoon.shadowsupdate.vo.ConfVo;

/**
 * @author guozy
 * @date 2017-1-6
 * 
 */
public class Shadowsocks8ConfigList implements ConfigList{
	
	private static final Logger logger = Logger.getLogger(Shadowsocks8ConfigList.class);
	
	private static final String FREE_URL = "http://www.shadowsocks8.com/";
	private static final String beginStr = "<section id=\"free\"";
	private static final String endStr = "<section id=\"sslist\"";

	/* (non-Javadoc)
	 * @see priv.lmoon.shadowsupdate.config.ConfigList#getConfigList()
	 */
	@Override
	public List<ConfVo> getConfigList() {
		// TODO Auto-generated method stub
		return getConf(UrlContent.getURLContent(FREE_URL, beginStr, endStr));
	}
	
	private List<ConfVo> getConf(String content) {
		List<ConfVo> list = new ArrayList<ConfVo>();
		if (StringUtils.isBlank(content)) {
			return list;
		}
		try{
			while(content.length()>0) {
				int findIdx = content.indexOf("<img src=\"");
				
				if (findIdx == -1) {
					break;
				}
				
				int serverIdx = findIdx + 10;
				int serverEnd = content.indexOf("\"", serverIdx);

				
				
				String serverStr = content.substring(serverIdx, serverEnd);
				if(serverStr.contains("server")){
					ConfVo vo = getConfFromStr(Base64Coder.decodeBase64ForSS(QRcoder.decode(getImgUrl(serverStr.trim()))));
					if(vo!=null){
						list.add(vo);
					}
				}
				content = content.substring(serverEnd);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("",e);
		}
		return list;
	}
	
	private static String getImgUrl(String str){
		return str.startsWith(FREE_URL)?str:FREE_URL+str;
	}
	
	//rc4-md5:71973556@138.68.61.42:23456
	private static ConfVo getConfFromStr(String str){
		try{
			String[] strs = str.split(":");
			if(strs.length==3){
				String[] temp = strs[1].split("@");
				if(temp.length==2){
					ConfVo vo = new ConfVo();
					vo.setMethod(strs[0].trim());
					vo.setPassword(temp[0].trim());
					vo.setServer(temp[1].trim());
					vo.setServer_port(Integer.parseInt(strs[2].trim()));
					return vo;
				}
			}
		}catch(Exception e){
			logger.error("",e);
		}
		return null;
	}
	
	public static void main(String[] args) {
		new Shadowsocks8ConfigList().getConfigList();
	}

}
