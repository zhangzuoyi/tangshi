package com.zzy.xiaoyacz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zzy.xiaoyacz.data.SelectItem;
import com.zzy.xiaoyacz.data.TestQuestion;

public class TestFragment extends Fragment{
	List<TestQuestion> questions;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		loadQuestions();
		TestQuestion question=questions.get(0);
		View view = inflater.inflate(R.layout.test, container, false);
		TextView titleView=(TextView) view.findViewById(R.id.title);
		titleView.setText(question.getQuestion());
		LinearLayout contentLayout=(LinearLayout) view.findViewById(R.id.content_layout);
		RadioGroup radioGroup=(RadioGroup) view.findViewById(R.id.radioGroup);
		for(SelectItem item:question.getOptions()){
//			View itemView=inflater.inflate(R.layout.test_option, container, false);
//			TextView tv=(TextView) itemView.findViewById(R.id.optionText);
//			tv.setText(item.getTag()+". "+item.getText());
//			RadioButton rb=(RadioButton) itemView.findViewById(R.id.selectRradio);
//			contentLayout.addView(itemView);
			RadioButton rb=new RadioButton(getActivity());
			rb.setText(item.getTag()+". "+item.getText());
			radioGroup.addView(rb);
		}
		Button opeButton=(Button) view.findViewById(R.id.operation_button);
		opeButton.setText("Next");
		return view;
	}
	private void loadQuestions(){
		questions=new ArrayList<TestQuestion>();
		XmlResourceParser xrp = getResources().getXml(R.xml.questions);
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
	
}
