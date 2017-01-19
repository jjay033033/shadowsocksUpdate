/**
 * 
 */
package priv.lmoon.shadowsupdate.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import priv.lmoon.shadowsupdate.util.UrlContent;
import priv.lmoon.shadowsupdate.vo.ConfVo;

/**
 * @author guozy
 * @date 2017-1-6
 * 
 */
public class IshadowsocksConfigList implements ConfigList{
	
	private static final String FREE_URL = "http://www.ishadowsocks.com";
	private static final String beginStr = "<section id=\"free\">";
	private static final String endStr = "</section>";

	/* (non-Javadoc)
	 * @see priv.lmoon.shadowsupdate.config.ConfigList#getConfigList()
	 */
	@Override
	public List<ConfVo> getConfigList() {
		// TODO Auto-generated method stub
		return getConf(UrlContent.getURLContent(FREE_URL, beginStr, endStr)) ;
	}
	
	private List<ConfVo> getConf(String content) {
		List<ConfVo> list = new ArrayList<ConfVo>();
		if (StringUtils.isBlank(content)) {
			return list;
		}
		for (int i = 0; i < 3; i++) {
			ConfVo vo = new ConfVo();
			int serverIpIdx = content.indexOf("服务器地址:") + 6;
			int serverIpEnd = content.indexOf("<", serverIpIdx);

			if (serverIpIdx == -1) {
				break;
			}

			int serverPortIdx = content.indexOf("端口:") + 3;
			int serverPortEnd = content.indexOf("<", serverPortIdx);

			int passwordIdx = content.indexOf("密码:") + 3;
			int passwordEnd = content.indexOf("<", passwordIdx);

			int encryptionIdx = content.indexOf("加密方式:") + 5;
			int encryptionEnd = content.indexOf("<", encryptionIdx);

			vo.setServer(content.substring(serverIpIdx, serverIpEnd));
			vo.setServer_port(Integer.parseInt(content.substring(serverPortIdx,
					serverPortEnd)));
			vo.setPassword(content.substring(passwordIdx, passwordEnd));
			vo.setMethod(content.substring(encryptionIdx, encryptionEnd));

			list.add(vo);
			content = content.substring(encryptionEnd);
		}

		return list;
	}

}
