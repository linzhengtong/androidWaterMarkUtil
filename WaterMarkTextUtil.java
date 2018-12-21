package com.zzmcc.channelsupport.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.ViewGroup;

/**
 * 
 * 文件名:WaterMarkTextUtil.java 功能:在指定的Activity加入水印 创建日期:2018年11月13日下午5:37:42
 * 创建人:linzt 修改日期: 修改人:
 * 修改内容:我封装成一个类了，调用的时候直接调用setWaterMarkTextBg这个方法就可以，传入当前的activty和要显示的文字就可以
 */
public class WaterMarkTextUtil {

	// 字大小
	private static final int TEXT_SIZE = 18;
	// 默认后缀文字--该值可以不用设置如需要设置将suffixTextWidth设置为0
	private static final String SUFFIX_TEXT = "中国移动";
	// SUFFIX_TEXT距离前个文字的偏移量
	private static final int OFFSET = 100;
	// 文字旋转角度
	private static final float RORATE = 20f;
	// 图片距离右边的距离
	private static final int MARIGIN_RGIHT = 160;
	// 图片距离上面的距离
	private static final int MARIGIN_TOP = 200;

	// 一个字所占的像素
	private static int oneTextPx;
	// 当前字符串所占长度
	private static int textLength;
	// 后缀文字宽度
	private static int suffixTextWidth = 20;
	// 后缀文字高度
	private static int suffixTextHight = 20;

	/**
	 * 设置水印背景
	 * 
	 * @param bgText
	 */
	@SuppressLint("NewApi")
	public static void setWaterMarkTextBg(Activity activity, String bgText) {
		getRootView(activity).setBackground(drawTextToBitmap(activity, bgText));
	}

	/**
	 * 将文字写到bitmap中
	 * 
	 * @param context
	 * @param text
	 * @return
	 */
	private static BitmapDrawable drawTextToBitmap(Context context, String text) {
		int widthPx = 0;
		int hightPx = 0;
		TextPaint oneTextPxTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		// 设置比重、密度
		oneTextPxTextPaint.density = context.getResources().getDisplayMetrics().density;
		oneTextPx = sp2px(context, TEXT_SIZE);
		oneTextPxTextPaint.setTextSize(TEXT_SIZE);
		// 计算第一个文字的长度
		textLength = (int) oneTextPxTextPaint.measureText(text);
		// 计算第二个文字的长度
		int suffixTextLength = (int) oneTextPxTextPaint
				.measureText(SUFFIX_TEXT);
		// 第二列文字宽度
		if (suffixTextWidth == 0) {
			suffixTextWidth = measurementWidth(suffixTextLength);
			suffixTextHight = measurementHight(suffixTextLength);
		}
		// 传入文字的长和高
		widthPx = measurementWidth(suffixTextLength);
		hightPx = measurementHight(suffixTextLength);
		// 计算显示该内容需要的宽度
		int bitmapWidth = widthPx + suffixTextWidth + 2 * oneTextPx + OFFSET;
		int bitmapHight = (hightPx + oneTextPx) * 2;
		// 创建计算宽度和高度的画板
		Bitmap bitmap = Bitmap.createBitmap(bitmapWidth + MARIGIN_RGIHT,
				bitmapHight + (2 * MARIGIN_TOP), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawColor(Color.WHITE);

		// 初始化画笔
		TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		textPaint.density = context.getResources().getDisplayMetrics().density;
		textPaint.setColor(Color.GRAY);
		textPaint.setAlpha(90);
		textPaint.setStyle(Paint.Style.FILL);
		textPaint.setAntiAlias(true);
		textPaint.setTextAlign(Paint.Align.LEFT);
		textPaint.setFakeBoldText(false);
		textPaint.setTextSkewX(0);
		textPaint.setTextSize(oneTextPx);

		// 画板移动，绘制第一个位置
		canvas.translate(oneTextPx, hightPx + oneTextPx + MARIGIN_TOP);
		canvas.rotate(-RORATE);
		canvas.drawText(text, 0, 0, textPaint);

		// 恢复原点后移动位置
		canvas.rotate(RORATE);
		canvas.translate(widthPx + OFFSET + oneTextPx, 0);
		// 旋转一定度数、绘制文字
		canvas.rotate(-RORATE);
		// canvas.drawText(suffixText,0 ,0, textPaint);

		// 恢复原来度数
		canvas.rotate(RORATE);
		canvas.translate(-(widthPx + OFFSET + oneTextPx), oneTextPx + hightPx
				+ MARIGIN_TOP);
		// 旋转一定度数、绘制文字
		canvas.rotate(-RORATE);
		canvas.drawText(text, 0, 0, textPaint);

		// 恢复原来度数
		canvas.rotate(RORATE);
		canvas.translate(widthPx + OFFSET + oneTextPx, 0);
		// 旋转一定度数、绘制文字
		canvas.rotate(-RORATE);
		// canvas.drawText(suffixText, 0, 0,textPaint);

		// 保存画布
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();

		// 使用 BitmapDrawable进行反复平铺在视图中
		BitmapDrawable bitmapDrawable = new BitmapDrawable(
				context.getResources(), bitmap);
		bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT,
				Shader.TileMode.REPEAT);
		bitmapDrawable.setDither(true);

		return bitmapDrawable;
	}

	/**
	 * 计算对应的宽度
	 * 
	 * @param textLength
	 * @return
	 */
	private static int measurementWidth(int textLength) {
		int square = textLength * textLength;
		return ceilInt(Math.sqrt(9 * (square / 10)));
	}

	/**
	 * 计算对应的高度
	 * 
	 * @param textLength
	 * @return
	 */
	private static int measurementHight(int textLength) {
		int square = textLength * textLength;
		return ceilInt(Math.sqrt(square / 10));
	}

	/**
	 * 计算对应的ceil值
	 * 
	 * @param number
	 * @return
	 */
	private static int ceilInt(double number) {
		return (int) Math.ceil(number);
	}

	/**
	 * 获取最底层的view
	 * 
	 * @param activity
	 * @return
	 */
	private static ViewGroup getRootView(Activity activity) {
		ViewGroup view = (ViewGroup) activity
				.findViewById(android.R.id.content);
		return view;
	}

	/**
	 * 像素转化sp转px
	 * 
	 * @param context
	 * @param spVal
	 * @return
	 */
	private static int sp2px(Context context, float spVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				spVal, context.getResources().getDisplayMetrics());
	}

}
