/**
 * 
 */
package priv.lmoon.shadowsupdate.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import priv.lmoon.shadowsupdate.qrcode.Base64Coder;
import priv.lmoon.shadowsupdate.qrcode.QRcoder;
import priv.lmoon.shadowsupdate.util.UrlContent;
import priv.lmoon.shadowsupdate.vo.ConfVO;
import priv.lmoon.shadowsupdate.vo.ServerConfigVO;

/**
 * @author guozy
 * @date 2017-1-6
 * 
 */
public class PicConfigList implements ConfigList{
	
	private static final Logger logger = Logger.getLogger(PicConfigList.class);
	
//	private static final String FREE_URL = "https://www.shadowsocks8.biz/";
//	private static final String beginStr = "<section id=\"free\"";
//	private static final String endStr = "<section id=\"sslist\"";

//	public String id = "shadowsocks8";
	
	private ServerConfigVO vo;

	public PicConfigList(ServerConfigVO vo) {
		this.vo = vo;
	}
	
	/* (non-Javadoc)
	 * @see priv.lmoon.shadowsupdate.config.ConfigList#getConfigList()
	 */
	@Override
	public List<ConfVO> getConfigList() {
		// TODO Auto-generated method stub
		return getConf(UrlContent.getURLContent(vo));
	}
	
	private List<ConfVO> getConf(String content) {
		List<ConfVO> list = new ArrayList<ConfVO>();
		if (StringUtils.isBlank(content)) {
			return list;
		}
		try{
			while(content.length()>0) {
				int findIdx = content.indexOf(vo.getPicUrlBegin());
				
				if (findIdx == -1) {
					break;
				}
				
				int serverIdx = findIdx + vo.getPicUrlBegin().length();
				int serverEnd = content.indexOf(vo.getPicUrlEnd(), serverIdx);
				
				String serverStr = content.substring(serverIdx, serverEnd);
				if(serverStr.contains(vo.getSeverPicFlag())){
					ConfVO vo = getConfFromStr(Base64Coder.decodeBase64ForSS(QRcoder.decode(getImgUrl(serverStr.trim()))));
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
	
	private String getImgUrl(String str){
		return str.startsWith(vo.getUrl())?str:vo.getUrl()+str;
	}
	
	//rc4-md5:71973556@138.68.61.42:23456
	private ConfVO getConfFromStr(String str){
		try{
			String[] strs = str.split(":");
			if(strs.length==3){
				String[] temp = strs[1].split("@");
				if(temp.length==2){
					ConfVO confVo = new ConfVO();
					confVo.setMethod(strs[0].trim());
					confVo.setPassword(temp[0].trim());
					confVo.setServer(temp[1].trim());
					confVo.setServer_port(Integer.parseInt(strs[2].trim()));
					confVo.setRemarks(vo.getId());
					return confVo;
				}
			}
		}catch(Exception e){
			logger.error("",e);
		}
		return null;
	}
	
//	public static void main(String[] args) {
//		System.out.println(new PicConfigList("shadowsocks8").getConfigList());
//	}

}
