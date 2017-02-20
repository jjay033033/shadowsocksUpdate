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
public class IshadowsocksConfigList implements ConfigList{
	
//	private static final String FREE_URL = "https://www.ishadowsocks.xyz";
//	private static final String beginStr = "<section id=\"free\">";
//	private static final String endStr = "</section>";
	
	private static final String id = "ishadowsocks";
	
	private static ServerConfigVo vo;

	public IshadowsocksConfigList() {
		vo = XmlConfig.getInstance().getServerConfigVo(id);
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
	
	public static void main(String[] args) {
		System.out.println(new IshadowsocksConfigList().getConfigList());
	}

}
