package com.zzy.xiaoyacz.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;

import com.zzy.xiaoyacz.R;

public class QuestionUtil {
	private static List<TestQuestion> questions;

	public static List<TestQuestion> getQuestions(Context context){
		if(questions==null){
			questions=new ArrayList<TestQuestion>();
			XmlResourceParser xrp = context.getResources().getXml(R.xml.questions);
			boolean isInOptions=false;//是否在options节点范围内
			TestQuestion tq=null;
			try {
				while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT){
					if (xrp.getEventType() == XmlResourceParser.START_TAG) {
		                      String tagName = xrp.getName();// 获取标签的名字
		                      if(isInOptions){
		                    	  SelectItem si=new SelectItem();
		                    	  si.setTag(tagName);
		                    	  String msg=tagName;
		                    	  xrp.next();
		                    	  msg=msg+":"+xrp.getText();
		                    	  Log.i("option", msg);
		                    	  si.setText(xrp.getText());
		                    	  tq.getOptions().add(si);
		                      }
		                      
		                      if (tagName.equals("textQuestion")) {
		                    	  Log.i("xml", "start question");
		                    	  tq=new TestQuestion();
		                    	  questions.add(tq);
		                      }else if(tagName.equals("question")){
		                    	  xrp.next();
		                    	  Log.i("question", xrp.getText());
		                    	  tq.setQuestion(xrp.getText());
		                      }else if(tagName.equals("type")){
		                    	  xrp.next();
		                    	  Log.i("type", xrp.getText());
		                    	  tq.setType(xrp.getText());
		                      }else if(tagName.equals("options")){
		                    	  Log.i("options", "options start");
		                    	  isInOptions=true;
		                      }else if(tagName.equals("answer")){
		                    	  xrp.next();
		                    	  Log.i("answer", xrp.getText());
		                    	  tq.setAnswer(xrp.getText());
		                      }
					}else if (xrp.getEventType() == XmlResourceParser.END_TAG) {
						String tagName = xrp.getName();
						if(tagName.equals("options")){
	                  	  Log.i("options", "options end");
	                  	  isInOptions=false;
	                    }
					}
					xrp.next();
				}
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return questions;
	}
}
