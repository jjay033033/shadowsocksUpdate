package priv.lmoon.shadowsupdate.QRCode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import priv.lmoon.shadowsupdate.main.Main;
import priv.lmoon.shadowsupdate.vo.ConfVO;

public class QRcodeUtil {
	
	private static final Logger logger = Logger.getLogger(QRcodeUtil.class);
	
	public static void createQRCode(List<ConfVO> list,String path) {
		List<String> strList = confStr4QRCode(list);
		for (int i = 0; i < strList.size(); i++) {
			try {
				File file = new File(path);
				if(!file.isDirectory()){
					file.mkdir();
				}
				Base64Coder.createQRCodePic4Base64(strList.get(i), path
						+ list.get(i).getServer() + ".jpg");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("create QRCode:", e);
			}
		}
	}

	public static List<String> confStr4QRCode(List<ConfVO> list) {
		List<String> rList = new ArrayList<String>();
		for (ConfVO vo : list) {
			StringBuffer sb = new StringBuffer();
			sb.append(vo.getMethod()).append(":").append(vo.getPassword())
					.append("@").append(vo.getServer()).append(":")
					.append(vo.getServer_port());
			rList.add(sb.toString());
		}
		return rList;
	}

}
