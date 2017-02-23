/**
 * 
 */
package priv.lmoon.shadowsupdate.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import priv.lmoon.shadowsupdate.util.UrlContent;
import priv.lmoon.shadowsupdate.vo.ConfVo;
import priv.lmoon.shadowsupdate.vo.ServerConfigVo;

/**
 * @author guozy
 * @date 2017-1-6
 * 
 */
public class TextConfigList implements ConfigList{
	
//	private static final String FREE_URL = "https://www.ishadowsocks.xyz";
//	private static final String beginStr = "<section id=\"free\">";
//	private static final String endStr = "</section>";
	
//	private static final String id = "ishadowsocks";
	
	private ServerConfigVo vo;
	
	public TextConfigList(ServerConfigVo vo) {
		this.vo = vo;
	}

	/* (non-Javadoc)
	 * @see priv.lmoon.shadowsupdate.config.ConfigList#getConfigList()
	 */
	@Override
	public List<ConfVo> getConfigList() {
		// TODO Auto-generated method stub
		return getConf(UrlContent.getURLContent(vo)) ;
	}
	
	private List<ConfVo> getConf(String content) {
		List<ConfVo> list = new ArrayList<ConfVo>();
		if (StringUtils.isBlank(content)) {
			return list;
		}
		for (int i = 0; i < 3; i++) {
			ConfVo confVo = new ConfVo();
			int serverIpIdx = content.indexOf(vo.getServerIpBegin()) + vo.getServerIpBegin().length();
			int serverIpEndIds = content.indexOf(vo.getServerIpEnd(), serverIpIdx);

			if (serverIpIdx == -1) {
				break;
			}

			int serverPortIdx = content.indexOf(vo.getServerPortBegin()) + vo.getServerPortBegin().length();
			int serverPortEndIdx = content.indexOf(vo.getServerPortEnd(), serverPortIdx);

			int passwordIdx = content.indexOf(vo.getPasswordBegin()) + vo.getPasswordBegin().length();
			int passwordEndIdx = content.indexOf(vo.getPasswordEnd(), passwordIdx);

			int encryptionIdx = content.indexOf(vo.getEncryptionBegin()) + vo.getEncryptionBegin().length();
			int encryptionEndIdx = content.indexOf(vo.getEncryptionEnd(), encryptionIdx);

			confVo.setServer(content.substring(serverIpIdx, serverIpEndIds));
			confVo.setServer_port(Integer.parseInt(content.substring(serverPortIdx,
					serverPortEndIdx)));
			confVo.setPassword(content.substring(passwordIdx, passwordEndIdx));
			confVo.setMethod(content.substring(encryptionIdx, encryptionEndIdx));

			list.add(confVo);
			content = content.substring(encryptionEndIdx);
		}

		return list;
	}
	
//	public static void main(String[] args) {
//		System.out.println(new TextConfigList("ishadowsocks").getConfigList());
//	}

}
