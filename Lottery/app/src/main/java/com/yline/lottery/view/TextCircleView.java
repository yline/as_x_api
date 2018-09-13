package com.yline.lottery.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.yline.utils.UIScreenUtil;

public class TextCircleView extends TextView {
	private Paint mPaint = new Paint();
	
	private RectF mRectF = new RectF();
	private Rect outRect = new Rect();
	
	private static final int WIDTH = 1; // 边界宽度
	private int innerRadius; // 边界绘制，需要移入半个边界宽度
	
	public TextCircleView(Context context) {
		this(context, null);
	}
	
	public TextCircleView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, -1);
	}
	
	public TextCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		int width = UIScreenUtil.dp2px(context, WIDTH);
		mPaint.setStyle(Paint.Style.STROKE); // 空心
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(width); // 宽度
		
		innerRadius = (width + 1) >> 1;
		
		setGravity(Gravity.CENTER);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// 计算圆圈大小
		getDrawingRect(outRect);
		mRectF.set(outRect);
		mRectF.inset(innerRadius, innerRadius);
		
		mPaint.setColor(getCurrentTextColor()); // 颜色
		
		// 绘制圆圈
		canvas.drawArc(mRectF, 0, 360, false, mPaint);
	}
}
