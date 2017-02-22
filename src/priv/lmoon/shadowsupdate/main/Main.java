package priv.lmoon.shadowsupdate.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import priv.lmoon.shadowsupdate.QRCode.Base64Coder;
import priv.lmoon.shadowsupdate.config.ConfigList;
import priv.lmoon.shadowsupdate.config.ConfigListFactory;
import priv.lmoon.shadowsupdate.config.XmlConfig;
import priv.lmoon.shadowsupdate.vo.ConfVo;

public class Main {
	
	static{
		PropertyConfigurator.configure(System.getProperty("user.dir") + "/res/log4j.properties"); 
	}

	private static final Logger logger = Logger.getLogger(Main.class);

	private static final String HOME_PATH = "res/";
	
	private static final String PATH_NAME = HOME_PATH + "gui-config.json";
	private static final String EXE_NAME = "Shadowsocks.exe";
	private static final String EXE_PATH = HOME_PATH + EXE_NAME;
	private static final String QRCODE_PATH = HOME_PATH + "QRCode/";
	
	private static final String FIRST_SERVER = "firstServerId";
	
//	private static TextConfigList ishadowsocksConfigList = new TextConfigList();
//	private static PicConfigList shadowsocks8ConfigList = new PicConfigList();

	// private static final String PATH_NAME =
	// "C:\\Users\\LMoon\\Downloads\\Shadowsocks-win-2.3.1\\gui-config.json";
	// private static final String EXE_PATH =
	// "C:\\Users\\LMoon\\Downloads\\Shadowsocks-win-2.3.1\\Shadowsocks.exe";

	private static Process process = null;

	public static void main(String[] args) {
		
		
		// String uc = getURLContent("http://www.ishadowsocks.com/");
		// confVo vo = getConf(uc);
		// String json = buildContent(vo);
		// System.out.println(json);
		// System.out.println(readFile("C:\\Users\\LMoon\\Downloads\\Shadowsocks-win-2.3.1\\gui-config.json"));
		// writeFile(json, "d:/a.txt");
		// restartExe("C:\\Users\\LMoon\\Downloads\\Shadowsocks-win-2.3.1\\Shadowsocks.exe");
		// try {
		// Thread.sleep(5000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// kill("Shadowsocks.exe");
		if (checkExeHasDone(EXE_NAME)) {
			logger.info("已运行" + EXE_NAME);
			return;
		}
		restartExe(EXE_PATH);
		try {
			String firstServer = XmlConfig.getInstance().getValue(FIRST_SERVER);
			while (true) {
				List<ConfVo> list = getConfList(firstServer);
//				if(args.length>0 && "1".equals(args[0])){
//					list = shadowsocks8ConfigList.getConfigList();
//				}else{
//					list = ishadowsocksConfigList.getConfigList();
//				}
				if (list != null && !list.isEmpty()) {
					String newPw = list.get(0).getPassword();
					if (StringUtils.isNotBlank(newPw)) {
						String oldPw = getPasswordFromJson(readFile(PATH_NAME));
						if (!newPw.equals(oldPw)) {
							logger.debug("password changed!");
							String content = buildContent(list);
							writeFile(content, PATH_NAME);
							createQRCode(list);
							restartExe(EXE_PATH);
						} else {
							logger.debug("password ok!");
						}
					}
				}
				if (process != null) {
					try {
						process.exitValue();
						break;
					} catch (Exception e) {
					}

				}
				try {
					Thread.sleep(5 * 60 * 1000);
				} catch (InterruptedException e) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("",e);
		}

	}
	
	private static List<ConfVo> getConfList(String firstServer){
		List<ConfVo> list = null;	
		ConfigList c; 
		if(firstServer == null){
			c = ConfigListFactory.getInstance().getFirstConfigList();		
		}else{
			c = ConfigListFactory.getInstance().getConfigListObj(firstServer);
		}
		if(c!=null){
			list = c.getConfigList();
		}
		if(list==null||list.isEmpty()){
			Map<String, ConfigList> cMap = ConfigListFactory.getInstance().getConfigListMap();
			for(Iterator<Entry<String, ConfigList>> it=cMap.entrySet().iterator();it.hasNext();){
				c = it.next().getValue();
				if(c!=null){
					list = c.getConfigList();
					if(list!=null&&!list.isEmpty()){
						return list;
					}
				}
			}
		}
		return list;
	}

	public static boolean checkExeHasDone(String exeName) {
		try {
			Process pr = Runtime.getRuntime().exec("tasklist");
			Scanner in = new Scanner(pr.getInputStream());
			while (in.hasNextLine()) {
				String p = in.nextLine();
				System.out.println(p);
				if (p.contains(exeName)) {
					return true;
				}
			}
			return false;
		} catch (IOException e) {
			logger.error("restartExe:", e);
			e.printStackTrace();
			return true;
		}
	}

	public static void restartExe(String pathname) {
		try {
			if (process != null) {
				process.destroy();
			}
			process = Runtime.getRuntime().exec("\"" + pathname + "\"");
		} catch (IOException e) {
			logger.error("restartExe:", e);
			e.printStackTrace();
		}
	}

	public static void kill(String exeName) {
		try {
			Process pr = Runtime.getRuntime().exec("tasklist");
			Scanner in = new Scanner(pr.getInputStream());
			while (in.hasNextLine()) {
				String p = in.nextLine();
				System.out.println(p);
				if (p.contains(exeName)) {
					StringBuffer buf = new StringBuffer();
					for (int i = 0; i < p.length(); i++) {
						char ch = p.charAt(i);
						if (ch != ' ') {
							buf.append(ch);
						}
					}
					System.out.println(buf.toString().split("Console")[0]
							.substring(exeName.length()));
					Runtime.getRuntime().exec(
							"tskill "
									+ buf.toString().split("Console")[0]
											.substring(exeName.length()));
				}
			}
		} catch (IOException e) {
			logger.error("kill:", e);
			e.printStackTrace();
		}
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

	@SuppressWarnings("unchecked")
	public static String getPasswordFromJson(String jsonStr) {
		try {
			if (jsonStr == null || jsonStr.isEmpty()) {
				return null;
			}
			JSONObject jo = JSONObject.fromObject(jsonStr);
			JSONArray confJa = jo.getJSONArray("configs");
			if (confJa == null || confJa.isEmpty()) {
				return null;
			}
			List<ConfVo> list = (List<ConfVo>) JSONArray.toCollection(confJa,
					ConfVo.class);
			ConfVo vo = (ConfVo) list.get(0);
			return vo.getPassword();
		} catch (Exception e) {
			logger.error("getPasswordFromJson:", e);
			e.printStackTrace();
			return null;
		}

	}

	public static String readFile(String pathname) {
		File file = null;
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer();
		try {
			// ./gui-config.json
			file = new File(pathname);
			if (file == null || !file.exists()) {
				return null;
			}
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, "utf-8");
			br = new BufferedReader(isr);
			String buf = null;
			while ((buf = br.readLine()) != null) {
				sb.append(buf);
			}
		} catch (Exception e) {
			logger.error("readFile:", e);
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (isr != null) {
					isr.close();
				}
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static void writeFile(String content, String pathname) {
		File file = null;
		FileOutputStream fos = null;
		content.getBytes();
		try {
			// ./gui-config.json
			file = new File(pathname);
			if (file.exists()) {
				file.delete();
			}
			fos = new FileOutputStream(file);
			fos.write(content.getBytes("utf-8"));
		} catch (Exception e) {
			logger.error("writeFile:", e);
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void createQRCode(List<ConfVo> list) {
		List<String> strList = confStr4QRCode(list);
		for (int i = 0; i < strList.size(); i++) {
			try {
				File file = new File(QRCODE_PATH);
				if(!file.isDirectory()){
					file.mkdir();
				}
				Base64Coder.createQRCodePic4Base64(strList.get(i), QRCODE_PATH
						+ list.get(i).getServer() + ".jpg");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("create QRCode:", e);
			}
		}
	}

	public static List<String> confStr4QRCode(List<ConfVo> list) {
		List<String> rList = new ArrayList<String>();
		for (ConfVo vo : list) {
			StringBuffer sb = new StringBuffer();
			sb.append(vo.getMethod()).append(":").append(vo.getPassword())
					.append("@").append(vo.getServer()).append(":")
					.append(vo.getServer_port());
			rList.add(sb.toString());
		}
		return rList;
	}

}