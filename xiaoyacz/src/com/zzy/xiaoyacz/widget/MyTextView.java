package com.zzy.xiaoyacz.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View.MeasureSpec;
import android.widget.TextView;

public class MyTextView extends TextView {
	private String pinyin;
	private String chinese;
	private int tvWidth=-1;
	private static final String SPACE="\u00A0";
	private static final int MARGIN=3;//拼音和汉字间隔
	private static final int MARGIN_LINE=5;//行间距

	public MyTextView(Context context) {
		super(context);
	}

	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public MyTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void setFullText(String pinyin, String chinese){
		this.pinyin=pinyin;
		this.chinese=chinese;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint=getPaint();
		Paint pinyinPaint=getPinyinPaint();
		chinese = chinese==null? "":chinese;
    	pinyin = pinyin==null?"":pinyin;
    	int x=getX(pinyin, chinese);
    	
    	float y1=getPaddingTop()-pinyinPaint.ascent();
    	float y2=y1+pinyinPaint.descent()+MARGIN-paint.ascent();
    	String[] pinyinList=pinyin.split(" ");
		for(int i=0;i<pinyinList.length;i++){
			float pw=pinyinPaint.measureText(pinyinList[i]);
			float cw=paint.measureText(chinese.substring(i, i+1));
			float w=pw>cw?pw:cw;
			WidthJudge judge=judgeX((int)(x+w), y1, y2);
			if(judge.isExceed){//超出宽度
				String[] pinyinTmp=new String[pinyinList.length-i];
				for(int j=i;j<pinyinList.length;j++){
					pinyinTmp[j-i]=pinyinList[j];
				}
				x=getX(pinyinTmp, chinese.substring(i));
				y1=judge.y1;
				y2=judge.y2;
			}
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
    	
	}
	
	private int getX(String pinyin, String chinese){
		String[] pinyinList=pinyin.split(" ");
		return getX(pinyinList, chinese);
	}
	private int getX(String[] pinyinList, String chinese){
		int x=getPaddingLeft();
    	if(getGravity()==Gravity.CENTER){
    		x=(int)(getWidth()-(getTextWidthByText(pinyinList, chinese)))/2;
    	}
    	if(x<=getPaddingLeft()){
    		x=getPaddingLeft();
    	}
    	return x;
	}
	/**
	 * 判断X是否已经超过宽度，是的话要换行
	 */
	private WidthJudge judgeX(int x, float y1, float y2){
		WidthJudge result=new WidthJudge();
		if(x>getWidth()-getPaddingRight()){
			result.isExceed=true;
			Paint paint=getPaint();
			Paint pinyinPaint=getPinyinPaint();
			result.y1=y2+paint.descent()+MARGIN_LINE-pinyinPaint.ascent();
	    	result.y2=result.y1+pinyinPaint.descent()+MARGIN-paint.ascent();
		}
		return result;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width=measureWidth(widthMeasureSpec);
    	int height=measureHeight(heightMeasureSpec, width);
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
        	chinese = chinese==null? "":chinese;
        	pinyin = pinyin==null?"":pinyin;
//            int result1 = getChineseTotalWidth();
//            int result2 = getPinyinTotalWidth();
            result=getTextWidth();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }

        return result;
    }
	private int getTextWidth(){
		if(tvWidth>-1){
			return tvWidth;
		}
		tvWidth=getTextWidthByText(pinyin, chinese);
		return tvWidth;
	}
	private int getTextWidthByText(String pinyin, String chinese){
		String[] pinyinList=pinyin.split(" ");
		return getTextWidthByText(pinyinList, chinese);
	}
	private int getTextWidthByText(String[] pinyinList, String chinese){
		Paint paint=getPaint();
		Paint pinyinPaint=getPinyinPaint();
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

    private int measureHeight(int measureSpec, int width) {
    	Paint paint=this.getPaint();
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
            result = -paint.ascent() + paint.descent() -pinyinPaint.ascent() + pinyinPaint.descent()  +MARGIN;
        	if(getTextWidth()>width){//宽度不够，需要换行
            	int lineCount=(int)Math.ceil(getTextWidth()*1.0/width);
            	result=result*lineCount+MARGIN_LINE*(lineCount-1);
            }
        	Log.v("Messure Height", "result Height:"+result);
            if (specMode == MeasureSpec.AT_MOST) {
            	Log.v("Messure Height", "At most Height:"+specSize);
                result = Math.min(result, specSize);
            }
        }
        return (int)result;
    }

    private Paint getPinyinPaint(){
    	Paint paint=new Paint();
    	paint.set(getPaint());
    	paint.setTextSize(paint.getTextSize()*0.55f);
    	return paint;
    }
    private class WidthJudge{
    	boolean isExceed;//是否超出宽度
    	float y1;
    	float y2;
    }
}
