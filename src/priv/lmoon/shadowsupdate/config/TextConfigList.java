/**
 * 
 */
package priv.lmoon.shadowsupdate.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import priv.lmoon.shadowsupdate.util.UrlContent;
import priv.lmoon.shadowsupdate.vo.ConfVo;
import priv.lmoon.shadowsupdate.vo.ServerConfigVo;

/**
 * @author guozy
 * @date 2017-1-6
 * 
 */
public class TextConfigList implements ConfigList {
	
	private static final Logger logger = Logger.getLogger(TextConfigList.class);

	// private static final String FREE_URL = "https://www.ishadowsocks.xyz";
	// private static final String beginStr = "<section id=\"free\">";
	// private static final String endStr = "</section>";

	// private static final String id = "ishadowsocks";

	private ServerConfigVo vo;

	public TextConfigList(ServerConfigVo vo) {
		this.vo = vo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see priv.lmoon.shadowsupdate.config.ConfigList#getConfigList()
	 */
	@Override
	public List<ConfVo> getConfigList() {
		// TODO Auto-generated method stub
		return getConf(UrlContent.getURLContent(vo));
	}

	private List<ConfVo> getConf(String content) {
		List<ConfVo> list = new ArrayList<ConfVo>();
		if (StringUtils.isBlank(content)) {
			return list;
		}
		try {
			while (content.length() > 0) {
				ConfVo confVo = new ConfVo();

				int findIdx = content.indexOf(vo.getServerIpBegin());

				if (findIdx == -1) {
					break;
				}
				
				int serverIpIdx = findIdx + vo.getServerIpBegin().length();
				int serverIpEndIds = content.indexOf(vo.getServerIpEnd(), serverIpIdx);

				int serverPortIdx = content.indexOf(vo.getServerPortBegin()) + vo.getServerPortBegin().length();
				int serverPortEndIdx = content.indexOf(vo.getServerPortEnd(), serverPortIdx);

				int passwordIdx = content.indexOf(vo.getPasswordBegin()) + vo.getPasswordBegin().length();
				int passwordEndIdx = content.indexOf(vo.getPasswordEnd(), passwordIdx);

				int encryptionIdx = content.indexOf(vo.getEncryptionBegin()) + vo.getEncryptionBegin().length();
				int encryptionEndIdx = content.indexOf(vo.getEncryptionEnd(), encryptionIdx);

				confVo.setServer(content.substring(serverIpIdx, serverIpEndIds));
				confVo.setServer_port(Integer.parseInt(content.substring(serverPortIdx, serverPortEndIdx)));
				confVo.setPassword(content.substring(passwordIdx, passwordEndIdx));
				confVo.setMethod(content.substring(encryptionIdx, encryptionEndIdx));
				confVo.setRemarks(vo.getId());

				list.add(confVo);
				content = content.substring(encryptionEndIdx);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(content);
			logger.error("",e);
			logger.error(content);
		}

		return list;
	}

	// public static void main(String[] args) {
	// System.out.println(new TextConfigList("ishadowsocks").getConfigList());
	// }

}
