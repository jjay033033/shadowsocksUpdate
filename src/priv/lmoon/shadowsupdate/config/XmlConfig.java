package priv.lmoon.shadowsupdate.config;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import priv.lmoon.shadowsupdate.util.XmlMap;
import priv.lmoon.shadowsupdate.vo.ServerConfigVo;

public class XmlConfig {
	
	private static final Logger logger = Logger.getLogger(XmlConfig.class);
	
	private static final String path = "res/config.xml";
	
	private static XmlConfig xmlConfig;
	
	private Map<String,ServerConfigVo> map = new HashMap<String, ServerConfigVo>();
	
	private XmlConfig(){
		init();
	}
	
	private void init(){
		try {
			XmlMap xm = new XmlMap(path);
			Map map = xm.getConfigMap();
			if (map == null || map.isEmpty()) {
				throw new FileNotFoundException();
			}
			List items = (List) map.get("server");
			if (items == null || items.isEmpty()) {
				throw new Exception("没有server项！");
			}
			Iterator iter = items.iterator();
			while (iter.hasNext()) {
				Map item = (Map) iter.next();
				if (item != null && item.get("id") != null) {
					ServerConfigVo vo = new ServerConfigVo();
					vo.setBegin((String) item.get("begin"));
					vo.setEnd((String) item.get("end"));
					vo.setId((String) item.get("id"));
					vo.setUrl((String) item.get("url"));
					this.map.put((String)item.get("id"), vo);
				}
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("config.xml初始化失败:", e);
		}
		
	}
	
	public static XmlConfig getInstance(){
		if(xmlConfig == null){
			xmlConfig = new XmlConfig();
		}
		return xmlConfig;
	}
	
	public ServerConfigVo getServerConfigVo(String id){
		ServerConfigVo vo = map.get(id);
		if(vo == null){
			vo = new ServerConfigVo();
		}
		return vo;
	}

}
