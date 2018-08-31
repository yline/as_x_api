package com.yline.lottery.http.manager;

/**
 * 支持彩种列表
 * 由于数据简单，而且基本无变化，直接写死
 *
 * @author yline 2018/8/30 -- 16:33
 */
public enum TypeEnum {
	SSQ("ssq", "ssq.html", "双色球", 1, "每周二、四、日开奖"),
	DLT("dlt","dlt.html",  "超级大乐透", 2, "每周一、三、六开奖"),
	QLC("qlc", "qlc.html", "七乐彩", 1, "每周一、三、五开奖"),
	FCSD("fcsd", "fcsd.html", "福彩3D", 2, "每日开奖"),
	QXC("qxc","qxc.html",  "七星彩", 1, "每周二、五、日开奖"),
	PLS("pls", "pls.html", "排列3", 2, "每日开奖"),
	PLW("plw", "plw.html", "排列5", 2, "每日开奖");
	
	public static final int TYPE_WELFARE = 1;
	
	private final String id;  // 彩票ID
	private final String name;  // 	彩票名称
	private final int type; // 彩票类型，1:福利彩票 2:体育彩票
	private final String ruleFile;
	private final String remark;  // 描述信息 {每周二、四、日开奖}
	
	TypeEnum(String id, String ruleFile, String name, int type, String remark) {
		this.ruleFile = ruleFile;
		this.id = id;
		this.name = name;
		this.type = type;
		this.remark = remark;
	}
	
	public String getId() {
		return id;
	}
	
	public String getRuleFile() {
		return ruleFile;
	}
	
	public String getName() {
		return name;
	}
	
	public int getType() {
		return type;
	}
	
	public String getRemark() {
		return remark;
	}
}
