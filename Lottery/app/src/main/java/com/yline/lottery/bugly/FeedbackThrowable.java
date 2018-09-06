package com.yline.lottery.bugly;

/**
 * 错误类型 - 用户反馈（具体不算错误，但使用这种方式，实现用户反馈）
 * 没有服务器穷啊
 *
 * @author yline 2018/9/6 -- 16:40
 */
public class FeedbackThrowable extends Throwable {
	private static final long serialVersionUID = -6482237232102029401L;
	
	public FeedbackThrowable(String message) {
		super("feedback:" + message);
	}
}
