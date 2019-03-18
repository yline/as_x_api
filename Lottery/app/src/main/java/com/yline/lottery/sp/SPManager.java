package com.yline.lottery.sp;

import com.yline.application.SDKManager;
import com.yline.http.json.FastJson;
import com.yline.lottery.http.model.LottoTypeModel;
import com.yline.lottery.module.main.model.ActualModel;
import com.yline.utils.SPUtil;

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
    // 暂时先不支持，保存一下，idList
    public void setLottoTypeList(List<LottoTypeModel> typeModelList) {
        if (null != typeModelList && !typeModelList.isEmpty()) {
            String typeJson = FastJson.toString(typeModelList);
            SPUtil.put(SDKManager.getApplication(), Key.TypeValue, typeJson);
        }
    }

    public String getLottoTypeList() {
        return (String) SPUtil.get(SDKManager.getApplication(), Key.TypeValue, "");
    }

    public void setLottoTypeSaveTime(long time) {
        SPUtil.put(SDKManager.getApplication(), Key.TypeTime, time);
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

    /* ------------------------- 切换默认的彩种展示--------------------------- */
    public String getLastLottoId() {
        return (String) SPUtil.get(SDKManager.getApplication(), Key.LastLottoId, TypeEnum.DLT.getId());
    }

    public void setLastLottoId(String lottoId) {
        SPUtil.put(SDKManager.getApplication(), Key.LastLottoId, lottoId);
    }

    private static class Key {
        // 支持的彩种列表（每个月同步一次）
        private static final String TypeTime = "type_time"; // 上一次保存，彩种列表，时间戳
        private static final String TypeValue = "type_value"; // 上一次保存，所有内容

        // 最新的，彩票中奖列表（每天同步一次）
        private static final String ActualTime = "actual_time_"; // 上一次，获取彩票中奖列表，时间
        private static final String ActualResult = "actual_result_"; // 彩票，开奖号码
        private static final String ActualNumber = "actual_number_"; // 彩票，开奖期号
        private static final String ActualDate = "actual_date_"; // 开奖日期

        // 上一次，选择的彩种
        private static final String LastLottoId = "last_lotto_id";
    }
}
