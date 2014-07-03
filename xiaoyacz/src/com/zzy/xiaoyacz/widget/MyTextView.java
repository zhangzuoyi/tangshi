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
		chinese = chinese==null? "":chinese;
    	pinyin = pinyin==null?"":pinyin;
    	int x1=getPaddingLeft();
    	int x2=getPaddingLeft();
    	if(getGravity()==Gravity.CENTER){
    		x1=(int)(getWidth()-(paint.measureText(pinyin)+getPaddingLeft()+getPaddingRight()))/2;
    		x2=(int)(getWidth()-(paint.measureText(chinese)+getPaddingLeft()+getPaddingRight()))/2;
    	}
    	canvas.drawText(pinyin, x1, getPaddingTop()-paint.ascent(), paint);
    	float y2=getPaddingTop()-paint.ascent()+paint.descent()+5;
    	canvas.drawText(chinese, x2, y2-paint.ascent(), paint);
        invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width=measureWidth(widthMeasureSpec);
    	int height=measureHeight(heightMeasureSpec);
        setMeasuredDimension(width ,height);
	}

	private int measureWidth(int measureSpec) {
		Paint paint=this.getPaint();
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
            int result1 = (int) paint.measureText(chinese) + getPaddingLeft() + getPaddingRight();
            int result2 = (int) paint.measureText(pinyin) + getPaddingLeft() + getPaddingRight();
            result=result1>result2?result1:result2;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }

        return result;
    }

    private int measureHeight(int measureSpec) {
    	Paint paint=this.getPaint();
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        int mAscent = (int) paint.ascent();
        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
        	Log.v("Messure Height", "spec Height:"+specSize);
            result = specSize;
        } else {
            // Measure the text (beware: ascent is a negative number)
            result = (int) (-mAscent + paint.descent())*2 + getPaddingTop() + getPaddingBottom() +5;
        	Log.v("Messure Height", "result Height:"+result);
            if (specMode == MeasureSpec.AT_MOST) {
            	Log.v("Messure Height", "At most Height:"+specSize);
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

//    private Paint getPinyinPaint(){
//    	Paint paint=new Paint();
//    	paint.set(getPaint());
//    	paint.setTextSize(paint.getTextSize()/4);
//    	return paint;
//    }
}
