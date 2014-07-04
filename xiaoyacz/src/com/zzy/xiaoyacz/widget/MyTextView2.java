package com.zzy.xiaoyacz.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View.MeasureSpec;
import android.widget.TextView;

public class MyTextView2 extends TextView {
	private String[] pinyins;
	private String[] chineses;
	private String pinyinContent;
	private String content;
	private Paint pinyinPaint;
	private Paint chinesePaint;
	private static final String SPACE="\u00A0";
	private static final int MARGIN_IN=3;//拼音和汉字间隔
	private static final int MARGIN_LINE=5;//行间距

	public MyTextView2(Context context) {
		super(context);
	}

	public MyTextView2(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public MyTextView2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void setFullText(String pinyinContent, String content){
		this.pinyinContent=pinyinContent;
		this.content=content;
		pinyins=pinyinContent.split("<br />");
		chineses=content.split("<br />");
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if(pinyins==null){
			return;
		}
		float startY=getPaddingTop();
		for(int i=0; i<pinyins.length; i++){
			startY=drawLine(canvas, pinyins[i].trim(), chineses[i].trim(), startY);
		}
    	
//        invalidate();
	}
	private float drawLine(Canvas canvas, String pinyin, String chinese, float startY){
		Paint paint=getChinesePaint();
		Paint pinyinPaint=getPinyinPaint();
		int x=getPaddingLeft();
//    	int x2=getPaddingLeft();
    	if(getGravity()==Gravity.CENTER){
    		x=(int)(getWidth()-(getTextWidth(pinyin, chinese)))/2;
//    		x2=(int)(getWidth()-(getPinyinTotalWidth()))/2;
    	}
    	
    	float y1=startY-pinyinPaint.ascent();
    	float y2=y1+pinyinPaint.descent()+MARGIN_IN-paint.ascent();
    	String[] pinyinList=pinyin.split(" ");
		for(int i=0;i<pinyinList.length;i++){
			float pw=pinyinPaint.measureText(pinyinList[i]);
			float cw=paint.measureText(chinese.substring(i, i+1));
			float w=pw>cw?pw:cw;
			if(!"·".equals(pinyinList[i])){//拼音里有·，不用显示
				canvas.drawText(pinyinList[i], x+(w-pw)/2, y1, pinyinPaint);//居中
			}
	    	canvas.drawText(chinese.substring(i, i+1), x+(w-cw)/2, y2, paint);//居中
			x+=w;
			if(i<pinyinList.length-1){
				canvas.drawText(SPACE, x, y2, paint);;//汉字间要有空格
				x+=paint.measureText(SPACE);
			}
		}
		if(chinese.length()>pinyinList.length){//要加上汉字右边的标点符号等
			canvas.drawText(chinese.substring(pinyinList.length, chinese.length()), x, y2, paint);
		}
		return y2+paint.descent()+MARGIN_LINE;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width=measureWidth(widthMeasureSpec);
    	int height=measureHeight(heightMeasureSpec);
        setMeasuredDimension(width ,height);
	}

	private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text
        	result =  specSize;
        }

        return result;
    }
	private int getTextWidth(String pinyin,String chinese){
		Paint paint=getChinesePaint();
		Paint pinyinPaint=getPinyinPaint();
		String[] pinyinList=pinyin.split(" ");
		float result=0;
		for(int i=0;i<pinyinList.length;i++){
			float pw=pinyinPaint.measureText(pinyinList[i]);
			float cw=paint.measureText(chinese.substring(i, i+1));
			float w=pw>cw?pw:cw;
			if(i<pinyinList.length-1){
				w+=paint.measureText(SPACE);//汉字间要有空格
			}
			result+=w;
		}
		if(chinese.length()>pinyinList.length){//要加上汉字右边的标点符号等
			result+=paint.measureText(chinese.substring(pinyinList.length, chinese.length()));
		}
		return (int)result;
	}

    private int measureHeight(int measureSpec) {
    	Paint paint=getChinesePaint();
    	Paint pinyinPaint=getPinyinPaint();
        float result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
        	Log.v("Messure Height", "spec Height:"+specSize);
            result = specSize;
        } else {
            // Measure the text (beware: ascent is a negative number)
            result = -paint.ascent() + paint.descent() -pinyinPaint.ascent() + pinyinPaint.descent()  +MARGIN_IN;
            if(pinyins!=null){
            	result=result*pinyins.length+MARGIN_LINE*(pinyins.length-1);
            }
            result=result+ getPaddingTop() + getPaddingBottom();
        	Log.v("Messure Height", "result Height:"+result);
            if (specMode == MeasureSpec.AT_MOST) {
            	Log.v("Messure Height", "At most Height:"+specSize);
                result = Math.min(result, specSize);
            }
        }
        return (int)Math.ceil(result);
    }

    private Paint getPinyinPaint(){
    	if(pinyinPaint==null){
    		pinyinPaint=new Paint();
    		pinyinPaint.set(getChinesePaint());
    		pinyinPaint.setTextSize(pinyinPaint.getTextSize()*0.55f);
    	}
    	
    	return pinyinPaint;
    }
    private Paint getChinesePaint(){
    	if(chinesePaint==null){
    		chinesePaint=getPaint();
    	}
    	return chinesePaint;
    }
}
