package com.yline.lottery.module.main.model;

import android.widget.Space;

import com.yline.lottery.http.manager.TypeEnum;
import com.yline.lottery.module.lotto.model.LottoQueryModel;
import com.yline.lottery.sp.SPManager;

import java.io.Serializable;

/**
 * 最新展示的信息
 *
 * @author yline 2018/8/31 -- 16:53
 */
public class ActualModel implements Serializable {
	private static final long serialVersionUID = 5411295556852090892L;
	
	private TypeEnum typeEnum;
	private String result; // "03,08,11,14,18,23,16",
	private String number; // 开奖期号 18028
	
	public static ActualModel genActualModel(TypeEnum typeEnum, String result, String number) {
		ActualModel actualModel = new ActualModel();
		actualModel.typeEnum = typeEnum;
		actualModel.result = result;
		actualModel.number = number;
		return actualModel;
	}
	
	public TypeEnum getTypeEnum() {
		return typeEnum;
	}
	
	public void setTypeEnum(TypeEnum typeEnum) {
		this.typeEnum = typeEnum;
	}
	
	public String getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
	
	public String getNumber() {
		return number;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
}
