package com.zzy.xiaoyacz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.zzy.xiaoyacz.data.SelectItem;
import com.zzy.xiaoyacz.data.TestQuestion;
//TODO 增加上一题按钮
public class TestFragment extends SherlockFragment{
	List<TestQuestion> questions;
	int currentQuestionIndex;
	TextView titleView;
	RadioGroup radioGroup;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		loadQuestions();
		currentQuestionIndex=0;
		View view = inflater.inflate(R.layout.test, container, false);
		titleView=(TextView) view.findViewById(R.id.title);
		radioGroup=(RadioGroup) view.findViewById(R.id.radioGroup);
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				TestQuestion question=questions.get(currentQuestionIndex);
				RadioButton checkedRb=(RadioButton) group.findViewById(checkedId);
				int num=group.getChildCount();
				for(int i=0;i<num;i++){
					RadioButton rb=(RadioButton) group.getChildAt(i);
					if(rb.getTag().equals(question.getAnswer())){
						rb.setButtonDrawable(R.drawable.btn_check_buttonless_on);
					}
				}
				if(!checkedRb.getTag().equals(question.getAnswer())){
					checkedRb.setButtonDrawable(R.drawable.ic_delete);
				}
			}
		});
		final Button opeButton=(Button) view.findViewById(R.id.operation_button);
		opeButton.setText(R.string.next_question);
		opeButton.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				currentQuestionIndex++;
				if(currentQuestionIndex>=questions.size()){
					currentQuestionIndex=0;
					Collections.shuffle(questions);//打乱题目顺序
				}
				if(currentQuestionIndex==questions.size()-1){
					opeButton.setText(R.string.restart);
				}else{
					opeButton.setText(R.string.next_question);
				}
				updateUI();
			}
			
		});
		updateUI();
		return view;
	}
	private void updateUI(){
		TestQuestion question=questions.get(currentQuestionIndex);
		titleView.setText((currentQuestionIndex+1)+"."+question.getQuestion());
		radioGroup.removeAllViews();
		for(SelectItem item:question.getOptions()){
			RadioButton rb=new RadioButton(getActivity());
			rb.setText(item.getTag()+". "+item.getText());
			rb.setTextSize( 16);
			rb.setTag(item.getTag());
			rb.setButtonDrawable(R.drawable.btn_radio_off_holo_light);
			radioGroup.addView(rb);
		}
		
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
		Collections.shuffle(questions);//打乱题目顺序
	}
	
}
