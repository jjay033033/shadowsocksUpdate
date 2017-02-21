package priv.lmoon.shadowsupdate.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import priv.lmoon.shadowsupdate.SysConstants;
import priv.lmoon.shadowsupdate.vo.ServerConfigVo;

public class ConfigListFactory {
	
	private static Map<String,ConfigList> configMap = new HashMap<String, ConfigList>();
	
	private static ConfigListFactory configListFactory;
	
	private static ConfigList firstConfigList;
	
	private ConfigListFactory(){
		init();
	}
	
	private void init(){
		Map<String,ServerConfigVo> map = XmlConfig.getInstance().getServerConfigMap();
		int i = 0;
		for(Iterator<String> it = map.keySet().iterator();it.hasNext();){
			String key = it.next();
			ServerConfigVo vo = map.get(key);
			switch(vo.getType()){
			case SysConstants.ServerType.TEXT:
				configMap.put(vo.getId(), new TextConfigList(vo));
				break;
			case SysConstants.ServerType.PIC:
				configMap.put(vo.getId(), new PicConfigList(vo));
				break;
			default:
				break;
			}
			if(i==0){
				firstConfigList = configMap.get(vo.getId());
			}
			i++;
		}
	}
	
	public static ConfigListFactory getInstance(){
		if(configListFactory == null){
			configListFactory = new ConfigListFactory();
		}
		return configListFactory;
	}
	
	public ConfigList getConfigListObj(String id){
		return configMap.get(id);
	}
	
	public ConfigList getFirstConfigList(){
		return firstConfigList;
	}

}
