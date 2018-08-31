package com.yline.lottery.sp;

import com.yline.application.SDKManager;
import com.yline.http.json.FastJson;
import com.yline.lottery.module.lotto.model.LottoTypeModel;
import com.yline.lottery.http.manager.TypeEnum;
import com.yline.lottery.module.main.model.ActualModel;
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
	
	public void setActualModel(TypeEnum typeEnum, String result, String number) {
		SPUtil.put(SDKManager.getApplication(), Key.ActualTime + typeEnum.getId(), System.currentTimeMillis());
		SPUtil.put(SDKManager.getApplication(), Key.ActualResult + typeEnum.getId(), result);
		SPUtil.put(SDKManager.getApplication(), Key.ActualNumber + typeEnum.getId(), number);
	}
	
	public long getActualModelTime(TypeEnum typeEnum) {
		return (long) SPUtil.get(SDKManager.getApplication(), Key.ActualTime + typeEnum.getId(), 0L);
	}
	
	public ActualModel getActualModel(TypeEnum typeEnum) {
		String result = (String) SPUtil.get(SDKManager.getApplication(), Key.ActualResult + typeEnum.getId(), "");
		String number = (String) SPUtil.get(SDKManager.getApplication(), Key.ActualNumber + typeEnum.getId(), "");
		return ActualModel.genActualModel(typeEnum, result, number);
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
		
		private static final String ActualResult = "actual_result_";
		private static final String ActualNumber = "actual_number_";
		private static final String ActualTime = "actual_time_";
	}
}
