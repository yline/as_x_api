package com.yline.lottery.sp;

import android.text.TextUtils;

import com.yline.application.SDKManager;
import com.yline.http.json.FastJson;
import com.yline.lottery.http.model.LottoTypeModel;
import com.yline.lottery.module.main.model.ActualModel;
import com.yline.utils.LogUtil;
import com.yline.utils.SPUtil;

import java.util.ArrayList;
import java.util.List;

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
	
	/* -------------------------支持的彩种列表（每个月同步一次）--------------------------- */
	public void setLottoTypeList(List<LottoTypeModel> typeModelList) {
		// 数据错误则不保存，接下来都使用默认数据
		if (null != typeModelList && !typeModelList.isEmpty()) {
			long saveTime = System.currentTimeMillis();
			LogUtil.v("saveTime = " + saveTime);
			SPUtil.put(SDKManager.getApplication(), Key.TypeTime, saveTime);
			SPUtil.put(SDKManager.getApplication(), Key.TypeCount, typeModelList.size());
			for (int i = 0; i < typeModelList.size(); i++) {
				LottoTypeModel typeModel = typeModelList.get(i);
				LogUtil.v("typeModel = " + typeModel.toString());
				
				String lottoId = typeModel.getLottery_id();
				SPUtil.put(SDKManager.getApplication(), Key.TypeId + i, lottoId);
				
				SPUtil.put(SDKManager.getApplication(), Key.TypeName + lottoId, typeModel.getLottery_name());
				SPUtil.put(SDKManager.getApplication(), Key.TypeType + lottoId, typeModel.getLottery_type_id());
				SPUtil.put(SDKManager.getApplication(), Key.TypeRemark + lottoId, typeModel.getRemarks());
			}
		}
	}
	
	public List<LottoTypeModel> getLottoTypeList() {
		int count = (int) SPUtil.get(SDKManager.getApplication(), Key.TypeCount, 0);
		List<LottoTypeModel> typeModelList = new ArrayList<>();
		if (count == 0) { // 异常处理
			for (TypeEnum typeEnum : TypeEnum.values()) {
				typeModelList.add(new LottoTypeModel(typeEnum.getId(), typeEnum.getName(), typeEnum.getType(), typeEnum.getRemark()));
			}
		} else {
			for (int i = 0; i < count; i++) {
				String lottoId = (String) SPUtil.get(SDKManager.getApplication(), Key.TypeId + i, "");
				
				String name = (String) SPUtil.get(SDKManager.getApplication(), Key.TypeId + lottoId, "");
				int type = (int) SPUtil.get(SDKManager.getApplication(), Key.TypeId + lottoId, LottoTypeModel.TYPE_WELFARE);
				String remark = (String) SPUtil.get(SDKManager.getApplication(), Key.TypeId + lottoId, "");
				typeModelList.add(new LottoTypeModel(lottoId, name, type, remark));
			}
		}
		return typeModelList;
	}
	
	public List<String> getLottoTypeIdList() {
		int count = (int) SPUtil.get(SDKManager.getApplication(), Key.TypeCount, 0);
		List<String> idList = new ArrayList<>();
		if (count == 0) { // 异常处理
			for (TypeEnum typeEnum : TypeEnum.values()) {
				idList.add(typeEnum.getId());
			}
		} else {
			for (int i = 0; i < count; i++) {
				String id = (String) SPUtil.get(SDKManager.getApplication(), Key.TypeId + i, "");
				idList.add(id);
			}
		}
		return idList;
	}
	
	public String getLottoTypeFirstId() {
		int count = (int) SPUtil.get(SDKManager.getApplication(), Key.TypeCount, 0);
		if (count == 0){ // 异常处理
			return TypeEnum.SSQ.getId();
		}else {
			return (String) SPUtil.get(SDKManager.getApplication(), Key.TypeId + 0, "");
		}
	}
	
	public String getLottoTypeNameByLottoId(String lottoId) {
		String typeName = (String) SPUtil.get(SDKManager.getApplication(), Key.TypeName + lottoId, "");
		if (TextUtils.isEmpty(typeName)) { // 异常处理
			return TypeEnum.getNameById(lottoId);
		} else {
			return typeName;
		}
	}
	
	public int getLottoTypeCount() {
		int count = (int) SPUtil.get(SDKManager.getApplication(), Key.TypeCount, 0);
		if (count == 0) { // 异常处理
			return TypeEnum.values().length;
		} else {
			return (int) SPUtil.get(SDKManager.getApplication(), Key.TypeCount, 0);
		}
	}
	
	public long getLottoTypeSaveTime() {
		return (long) SPUtil.get(SDKManager.getApplication(), Key.TypeTime, 0L);
	}
	
	/* -------------------------最新的，彩票中奖列表（每天同步一次）--------------------------- */
	public void setActualModel(String lottoId, String result, String number, String date) {
		SPUtil.put(SDKManager.getApplication(), Key.ActualTime + lottoId, System.currentTimeMillis());
		SPUtil.put(SDKManager.getApplication(), Key.ActualResult + lottoId, result);
		SPUtil.put(SDKManager.getApplication(), Key.ActualNumber + lottoId, number);
		SPUtil.put(SDKManager.getApplication(), Key.ActualDate + lottoId, date);
	}
	
	public long getActualModelTime(String lottoId) {
		return (long) SPUtil.get(SDKManager.getApplication(), Key.ActualTime + lottoId, 0L);
	}
	
	public ActualModel getActualModel(String lottoId) {
		String result = (String) SPUtil.get(SDKManager.getApplication(), Key.ActualResult + lottoId, "");
		String number = (String) SPUtil.get(SDKManager.getApplication(), Key.ActualNumber + lottoId, "");
		String date = (String) SPUtil.get(SDKManager.getApplication(), Key.ActualDate + lottoId, "");
		return ActualModel.genActualModel(lottoId, result, number, date);
	}
	
	/* -------------------------历史查询，切换默认的彩种展示--------------------------- */
	public String getHistoryLotteryId() {
		return (String) SPUtil.get(SDKManager.getApplication(), Key.HistoryLotteryId, "");
	}
	
	public void setHistoryLotteryId(String lotteryId) {
		SPUtil.put(SDKManager.getApplication(), Key.HistoryLotteryId, lotteryId);
	}
	
	public String getHistoryLotteryName(){
		return (String) SPUtil.get(SDKManager.getApplication(), Key.HistoryLotteryName, "");
	}
	
	public void setHistoryLotteryName(String lotteryName){
		SPUtil.put(SDKManager.getApplication(), Key.HistoryLotteryName, lotteryName);
	}
	
	public LottoTypeModel getHistoryLotteryType() {
		String typeString = (String) SPUtil.get(SDKManager.getApplication(), Key.HistoryLotteryType, "");
		return FastJson.toClass(typeString, LottoTypeModel.class);
	}
	
	public void setHistoryLotteryType(LottoTypeModel typeModel) {
		String typeString = FastJson.toString(typeModel);
		SPUtil.put(SDKManager.getApplication(), Key.HistoryLotteryType, typeString);
	}
	
	/* -------------------------规则查询，切换默认的彩种展示--------------------------- */
	public String getRuleLotteryId() {
		return (String) SPUtil.get(SDKManager.getApplication(), Key.RuleLotteryId, "");
	}
	
	public void setRuleLotteryId(String lotteryId) {
		SPUtil.put(SDKManager.getApplication(), Key.RuleLotteryId, lotteryId);
	}
	
	public String getRuleLotteryName(){
		return (String) SPUtil.get(SDKManager.getApplication(), Key.RuleLotteryName, "");
	}
	
	public void setRuleLotteryName(String lotteryName){
		SPUtil.put(SDKManager.getApplication(), Key.RuleLotteryName, lotteryName);
	}
	
	public LottoTypeModel getRuleLotteryType() {
		String typeString = (String) SPUtil.get(SDKManager.getApplication(), Key.RuleLotteryType, "");
		return FastJson.toClass(typeString, LottoTypeModel.class);
	}
	
	public void setRuleLotteryType(LottoTypeModel typeModel) {
		String typeString = FastJson.toString(typeModel);
		SPUtil.put(SDKManager.getApplication(), Key.RuleLotteryType, typeString);
	}
	
	private static class Key {
		// 支持的彩种列表（每个月同步一次）
		private static final String TypeTime = "type_time"; // 上一次保存，彩种列表，时间戳
		private static final String TypeCount = "type_count"; // 上一次保存，彩种列表，总个数
		private static final String TypeId = "type_id_"; // id
		private static final String TypeName = "type_name_"; // name
		private static final String TypeType = "type_type_"; // 福彩还是体彩
		private static final String TypeRemark = "type_remark_"; // 开奖提示
		
		// 最新的，彩票中奖列表（每天同步一次）
		private static final String ActualTime = "actual_time_"; // 上一次，获取彩票中奖列表，时间
		private static final String ActualResult = "actual_result_"; // 彩票，开奖号码
		private static final String ActualNumber = "actual_number_"; // 彩票，开奖期号
		private static final String ActualDate = "actual_date_"; // 开奖日期
		
		// 历史查询，切换默认的彩种展示
		private static final String HistoryLotteryId = "history_lottery_id";
		private static final String HistoryLotteryName = "history_lottery_name";
		private static final String HistoryLotteryType = "history_lottery_type";
		
		// 规则查询，切换默认的彩种展示
		private static final String RuleLotteryId = "rule_lottery_id";
		private static final String RuleLotteryName = "rule_lottery_name";
		private static final String RuleLotteryType = "rule_lottery_type";
	}
}
