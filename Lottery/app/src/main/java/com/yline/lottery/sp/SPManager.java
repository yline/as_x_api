package com.yline.lottery.sp;

import com.yline.application.SDKManager;
import com.yline.http.json.FastJson;
import com.yline.lottery.module.lotto.model.LottoTypeModel;
import com.yline.utils.SPUtil;

/**
 * SP全局统一管理，
 *
 * @author linjiang@kjtpay.com
 * @times 2018/7/26 -- 13:09
 */
public class SPManager {
	private static SPManager spManager;
	
	private SPManager() {
	}
	
	public static SPManager getInstance() {
		if (null == spManager) {
			synchronized (SPManager.class) {
				if (null == spManager) {
					spManager = new SPManager();
				}
			}
		}
		return spManager;
	}
	
	public String getLotteryId() {
		return (String) SPUtil.get(SDKManager.getApplication(), Key.LotteryId, "");
	}
	
	public void setLotteryId(String lotteryId) {
		SPUtil.put(SDKManager.getApplication(), Key.LotteryId, lotteryId);
	}
	
	public LottoTypeModel getLotteryType() {
		String typeString = (String) SPUtil.get(SDKManager.getApplication(), Key.LotteryType, "");
		return FastJson.toClass(typeString, LottoTypeModel.class);
	}
	
	public void setLotteryType(LottoTypeModel typeModel) {
		String typeString = FastJson.toString(typeModel);
		SPUtil.put(SDKManager.getApplication(), Key.LotteryType, typeString);
	}
	
	private static class Key {
		private static final String LotteryId = "lottery_id";
		private static final String LotteryType = "lottery_type";
	}
}
