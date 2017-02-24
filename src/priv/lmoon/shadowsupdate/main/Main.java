package priv.lmoon.shadowsupdate.main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import priv.lmoon.shadowsupdate.QRCode.QRcodeUtil;
import priv.lmoon.shadowsupdate.config.ConfigList;
import priv.lmoon.shadowsupdate.config.ConfigListFactory;
import priv.lmoon.shadowsupdate.config.XmlConfig;
import priv.lmoon.shadowsupdate.util.ConfListUtil;
import priv.lmoon.shadowsupdate.util.FileUtil;
import priv.lmoon.shadowsupdate.util.WinCmdUtil;
import priv.lmoon.shadowsupdate.vo.ConfVo;

public class Main {

	static {
		PropertyConfigurator.configure(System.getProperty("user.dir") + "/res/log4j.properties");
	}

	private static final Logger logger = Logger.getLogger(Main.class);

	private static final String HOME_PATH = "res/";

	private static final String PATH_NAME = HOME_PATH + "gui-config.json";
	private static final String EXE_NAME = "Shadowsocks.exe";
	private static final String EXE_PATH = HOME_PATH + EXE_NAME;
	private static final String QRCODE_PATH = HOME_PATH + "QRCode/";

	private static final String SLEEP_TIME = "sleepTime";

//	private static final String FIRST_SERVER = "firstServerId";

	public static void main(String[] args) {

		// kill("Shadowsocks.exe");
		if (WinCmdUtil.checkExeHasDone(EXE_NAME)) {
			System.out.println("已运行" + EXE_NAME);
			logger.info("已运行" + EXE_NAME);
			return;
		}
		try {
//			String firstServer = XmlConfig.getInstance().getValue(FIRST_SERVER);
			long sleepTime = Long.parseLong(XmlConfig.getInstance().getValue(SLEEP_TIME));
			WinCmdUtil.restartExe(EXE_PATH);
			while (true) {
				List<ConfVo> newList = getConfListFromServer();
				List<ConfVo> oldList = getConfListFromJson(FileUtil.readFile(PATH_NAME));
				Map<String, Object> compareMap = ConfListUtil.CompareList(oldList, newList);
				boolean isChange = (Boolean) compareMap.get("isChange");
				if (isChange) {
					logger.debug("password changed!");
					List<ConfVo> list = (List<ConfVo>) compareMap.get("confList");
					String content = buildContent(list);
					FileUtil.writeFile(content, PATH_NAME);
					QRcodeUtil.createQRCode(list, QRCODE_PATH);
					WinCmdUtil.restartExe(EXE_PATH);
				} else {
					logger.debug("password ok!");
				}

				if (!WinCmdUtil.isExistProcess()) {
					break;
				}
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}

	}

	// private static List<ConfVo> getConfList(String firstServer){
	// List<ConfVo> list = null;
	// ConfigList c;
	// if(firstServer == null){
	// c = ConfigListFactory.getInstance().getFirstConfigList();
	// }else{
	// c = ConfigListFactory.getInstance().getConfigListObj(firstServer);
	// }
	// if(c!=null){
	// list = c.getConfigList();
	// }
	// if(list==null||list.isEmpty()){
	// Map<String, ConfigList> cMap =
	// ConfigListFactory.getInstance().getConfigListMap();
	// for(Iterator<Entry<String, ConfigList>>
	// it=cMap.entrySet().iterator();it.hasNext();){
	// c = it.next().getValue();
	// if(c!=null){
	// list = c.getConfigList();
	// if(list!=null&&!list.isEmpty()){
	// return list;
	// }
	// }
	// }
	// }
	// return list;
	// }

	private static List<ConfVo> getConfListFromServer() {
		List<ConfVo> list = new ArrayList<ConfVo>();
		ConfigList c;

		Map<String, ConfigList> cMap = ConfigListFactory.getInstance().getConfigListMap();
		for (Iterator<Entry<String, ConfigList>> it = cMap.entrySet().iterator(); it.hasNext();) {
			c = it.next().getValue();
			if (c != null) {
				List<ConfVo> cList = c.getConfigList();
				if (cList != null && !cList.isEmpty()) {
					list.addAll(cList);
				}
			}
		}

		return list;
	}

	public static String buildContent(List<ConfVo> list) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("configs", list);
		map.put("strategy", "com.shadowsocks.strategy.ha");
		map.put("index", -1);
		map.put("global", false);
		map.put("enabled", true);
		map.put("shareOverLan", false);
		map.put("isDefault", false);
		map.put("localPort", 1080);
		map.put("pacUrl", null);
		map.put("useOnlinePac", false);
		JSONObject jo = JSONObject.fromObject(map);
		return jo.toString();
	}

	// @SuppressWarnings("unchecked")
	// public static String getPasswordFromJson(String jsonStr) {
	// try {
	// if (jsonStr == null || jsonStr.isEmpty()) {
	// return null;
	// }
	// JSONObject jo = JSONObject.fromObject(jsonStr);
	// JSONArray confJa = jo.getJSONArray("configs");
	// if (confJa == null || confJa.isEmpty()) {
	// return null;
	// }
	// List<ConfVo> list = (List<ConfVo>) JSONArray.toCollection(confJa,
	// ConfVo.class);
	// ConfVo vo = (ConfVo) list.get(0);
	// return vo.getPassword();
	// } catch (Exception e) {
	// logger.error("getPasswordFromJson:", e);
	// e.printStackTrace();
	// return null;
	// }
	//
	// }

	public static List<ConfVo> getConfListFromJson(String jsonStr) {
		try {
			if (jsonStr == null || jsonStr.isEmpty()) {
				return null;
			}
			JSONObject jo = JSONObject.fromObject(jsonStr);
			JSONArray confJa = jo.getJSONArray("configs");
			if (confJa == null || confJa.isEmpty()) {
				return null;
			}
			List<ConfVo> list = (List<ConfVo>) JSONArray.toCollection(confJa, ConfVo.class);
			return list;
		} catch (Exception e) {
			logger.error("getConfListFromJson:", e);
			e.printStackTrace();
			return null;
		}

	}

}