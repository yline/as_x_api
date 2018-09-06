package com.yline.lottery.http;

import com.yline.http.OkHttpUtils;
import com.yline.http.callback.OnJsonCallback;
import com.yline.lottery.http.adapter.LottoHttpAdapter;
import com.yline.lottery.http.model.LottoBonusModel;
import com.yline.lottery.http.model.LottoHistoryModel;
import com.yline.lottery.http.model.LottoQueryModel;
import com.yline.lottery.http.model.LottoTypeModel;

import java.util.List;
import java.util.Map;

public class OkHttpManager {
	/**
	 * 中奖计算器
	 *
	 * @param lotteryId  彩票ID
	 * @param lotteryNum 开奖期数，默认最新一期
	 * @param lotteryRes 购买的号码，号码之间用英文逗号隔开，红色球和蓝色求之间用@连接，例如：01,11,02,09,14,22,25@05,03
	 */
	public static void lottoBonus(String lotteryId, String lotteryNum, String lotteryRes, OnJsonCallback<LottoBonusModel> callback) {
		String httpUrl = "http://apis.juhe.cn/lottery/bonus";
		
		Map<String, String> reqMap = new android.support.v4.util.ArrayMap<>();
		reqMap.put("lottery_id", lotteryId);
		reqMap.put("lottery_no", lotteryNum);
		reqMap.put("lottery_res", lotteryRes);
		
		postMulti(httpUrl, reqMap, httpUrl, callback);
	}
	
	/**
	 * 历史开奖结果查询
	 * 居然是1开始，每次回来都需要加1；接口的奇葩啊
	 *
	 * @param lotteryId 彩票种类 id
	 * @param pageSize  每次返回条数，默认10，最大50
	 * @param pageNum   当前页数，默认1
	 */
	public static void lottoQueryHistory(String lotteryId, int pageSize, int pageNum, OnJsonCallback<LottoHistoryModel> callback) {
		String httpUrl = "http://apis.juhe.cn/lottery/history";
		
		Map<String, String> reqMap = new android.support.v4.util.ArrayMap<>();
		reqMap.put("lottery_id", lotteryId);
		reqMap.put("page_size", String.valueOf(pageSize));
		reqMap.put("page", String.valueOf(pageNum));
		
		postMulti(httpUrl, reqMap, httpUrl, callback);
	}
	
	/**
	 * 开奖结果查询
	 *
	 * @param lotteryId 彩票种类 id
	 * @param lotteryNo 彩票期号，默认最新一期
	 */
	public static void lottoQuery(String lotteryId, String lotteryNo, OnJsonCallback<LottoQueryModel> callback) {
		String httpUrl = "http://apis.juhe.cn/lottery/query";
		
		Map<String, String> reqMap = new android.support.v4.util.ArrayMap<>();
		reqMap.put("lottery_id", lotteryId);
		reqMap.put("lottery_no", lotteryNo);
		
		postMulti(httpUrl, reqMap, httpUrl, callback);
	}
	
	/**
	 * 支持彩种列表
	 * 由于数据简单，而且基本无变化，直接写死
	 */
	public static void lottoType(OnJsonCallback<List<LottoTypeModel>> callback) {
		String httpUrl = "http://apis.juhe.cn/lottery/types";
		
		postMulti(httpUrl, null, "type", callback);
	}
	
	/**
	 * LottoHttpAdapter 进行适配
	 */
	private static <T> void postMulti(String httpUrl, Map<String, String> paramMap, Object tag, OnJsonCallback<T> callback) {
		OkHttpUtils.request(new LottoHttpAdapter(httpUrl, paramMap, tag), callback);
	}
}
