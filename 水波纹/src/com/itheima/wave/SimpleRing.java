package com.itheima.wave;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SimpleRing extends View {

	private Paint mPaint;
	
	//颜色的数组
	int[] mColors = new int[]{Color.RED,Color.GREEN,Color.BLUE,Color.YELLOW};
	
	private List<WaveInfo> list = new ArrayList<WaveInfo>();
	private List<WaveInfo> deleteList = new ArrayList<WaveInfo>();
	
	private Handler handler =  new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			fushData();
			
			//刷新界面
			invalidate();
			
			//集合中有数据才继续发送
			if (!list.isEmpty()) {
				handler.sendEmptyMessageDelayed(0, 50);
			}
			
		};
	};

	public SimpleRing(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
//		init();
	}

	public SimpleRing(Context context, AttributeSet attrs) {
		super(context, attrs);
//		init();
	}

	public SimpleRing(Context context) {
		super(context);
//		init();
	}
	
	/**
	 * 给自定义控件设置触摸监听   点击和 滑动的时候画圆
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			//在按下 或者 滑动的时候添加一个圆心
			addPointe((int)event.getX(),(int)event.getY());
			
			break;

		default:
			break;
		}
		
		return true;
	}
	
	/**
	 * 在按下 或者 滑动的时候添加一个圆心
	 * @param x
	 * @param y
	 */
	private void addPointe(int x, int y) {
		//如果集合中没有元素  则表明是第一次画圆
		if (list.isEmpty()) {
			addWave(x,y);
			
			handler.sendEmptyMessage(0);
		}else{
			addWave(x,y);
		}
		
	}
	
	/**
	 * 在集合中添加圆环对象
	 * @param x
	 * @param y
	 */
	private void addWave(int x, int y) {
		WaveInfo wave = new WaveInfo();
		wave.x = x;
		wave.y = y;
		wave.radius = 0;//半径
		Paint paint = new Paint();
		int index = (int)(Math.random() * 4);
		paint.setColor(mColors[index]);
		paint.setStyle(Style.STROKE);//空心圆
		paint.setAlpha(255);//透明度
		paint.setAntiAlias(true);//去掉锯齿
		paint.setStrokeWidth(wave.radius / 3);//圆环的宽度
		wave.paint = paint;
		list.add(wave);
	}
	
	/**
	 * 刷新圆 
	 */
	protected void fushData() {
		for (WaveInfo wave : list) {
			//半径每次增加10
			wave.radius += 5;
			
			wave.paint.setStrokeWidth(wave.radius / 3);
			//透明度每次减5
			int alpha = wave.paint.getAlpha();
			alpha -= 5;
			if (alpha < 0) {
				alpha = 0;
			}
			wave.paint.setAlpha(alpha);
			
			//将已经消失的圆环添加到删除集合中  避免内存溢出
			if (alpha == 0) {
				
				deleteList.add(wave);
			}
		}
		//删除删除集合
		list.removeAll(deleteList);
	}

	/*private void init(){
		mPaint = new Paint();
		mPaint.setColor(Color.RED);//设置画笔的颜色
		mPaint.setStyle(Style.STROKE); //画一个空心圆
		mPaint.setAntiAlias(true); //去掉锯齿
		mPaint.setStrokeWidth(5); //圆环的宽度
	}*/
	
	@Override
	protected void onDraw(Canvas canvas) {
		for (WaveInfo wave : list) {
			canvas.drawCircle(wave.x, wave.y, wave.radius, wave.paint);
		}
	}
	
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		setMeasuredDimension(200, 200);
//	}
//	
//	@Override
//	protected void onDraw(Canvas canvas) {
//		canvas.drawCircle(getWidth() /2 , getHeight() / 2, 50, mPaint);
//	}
	
	/**
	 * 圆环信息
	 * @author MACHAO
	 *
	 */
	class WaveInfo{
		int x;
		int y;
		Paint paint;
		int radius;
	}
	
}
