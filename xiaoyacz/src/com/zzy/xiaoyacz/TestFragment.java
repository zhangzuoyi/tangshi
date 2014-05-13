package com.zzy.xiaoyacz;

import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zzy.xiaoyacz.data.QuestionUtil;
import com.zzy.xiaoyacz.data.SelectItem;
import com.zzy.xiaoyacz.data.TestQuestion;

public class TestFragment extends Fragment{
	private List<TestQuestion> questions;
	private int currentQuestionIndex;
	private TextView titleView;
	private RadioGroup radioGroup;
	private TextView testIndicator;
	private LinearLayout resultView;
	private ImageView resultImage;
	private TextView resultText;
	private Button resultButton;
	private Button opeButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		loadQuestions();
		currentQuestionIndex=0;
		View view = inflater.inflate(R.layout.test, container, false);
		titleView=(TextView) view.findViewById(R.id.title);
		testIndicator=(TextView) view.findViewById(R.id.testIndicator);
		resultView=(LinearLayout) view.findViewById(R.id.resultView);
		resultImage=(ImageView) view.findViewById(R.id.resultImage);
		resultText=(TextView) view.findViewById(R.id.resultText);
		resultButton=(Button) view.findViewById(R.id.result_button);
		radioGroup=(RadioGroup) view.findViewById(R.id.radioGroup);
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				TestQuestion question=questions.get(currentQuestionIndex);
				RadioButton checkedRb=(RadioButton) group.findViewById(checkedId);
				boolean isCorrect=true;
				int num=group.getChildCount();
				for(int i=0;i<num;i++){
					RadioButton rb=(RadioButton) group.getChildAt(i);
					rb.setClickable(false);
				}
				if(!checkedRb.getTag().equals(question.getAnswer())){
					checkedRb.setButtonDrawable(R.drawable.ic_wrong);
					isCorrect=false;
				}else{
					checkedRb.setButtonDrawable(R.drawable.ic_correct);
					isCorrect=true;
				}
				showResultView(isCorrect,question.getAnswer());
				question.setCorrect(isCorrect);
				if(currentQuestionIndex==questions.size()-1){
					resultButton.setVisibility(View.VISIBLE);
				}else{
					opeButton.setVisibility(View.VISIBLE);
				}
			}
		});
		opeButton=(Button) view.findViewById(R.id.operation_button);
		opeButton.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				currentQuestionIndex++;
//				if(currentQuestionIndex>=questions.size()){
//					currentQuestionIndex=0;
//					Collections.shuffle(questions);//打乱题目顺序
//				}
//				if(currentQuestionIndex==questions.size()-1){
//					opeButton.setVisibility(View.GONE);
//					resultButton.setVisibility(View.VISIBLE);
//				}
				updateUI();
			}
			
		});
		resultButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showResult();
			}
		});
		updateUI();
		return view;
	}
	private void showResultView(boolean isCorrect, String answer){
		resultView.setVisibility(View.VISIBLE);
		if(isCorrect){
			resultImage.setImageResource(R.drawable.ic_correct);
			resultText.setText(R.string.answer_right);
		}else{
			resultImage.setImageResource(R.drawable.ic_wrong);
			resultText.setText(getResources().getString(R.string.answer_wrong, answer));
		}
	}
	private void updateUI(){
		TestQuestion question=questions.get(currentQuestionIndex);
		titleView.setText(question.getQuestion());
		testIndicator.setText((currentQuestionIndex+1)+" / "+questions.size());
		radioGroup.removeAllViews();
		for(SelectItem item:question.getOptions()){
			RadioButton rb=new RadioButton(getActivity());
			rb.setText(item.getTag()+". "+item.getText());
			rb.setTextSize( 16);
			rb.setTag(item.getTag());
			rb.setButtonDrawable(R.drawable.btn_radio_off_holo_light);
			radioGroup.addView(rb);
		}
		resultView.setVisibility(View.GONE);
		opeButton.setVisibility(View.GONE);
	}
	/**
	 * 重新开始做题
	 */
	public void refreshQuestion(){
		opeButton.setVisibility(View.VISIBLE);
		resultButton.setVisibility(View.GONE);
		currentQuestionIndex=0;
		Collections.shuffle(questions);//打乱题目顺序
		updateUI();
	}
	private void loadQuestions(){
		questions=QuestionUtil.getQuestions(getActivity());
	}
	private void showResult(){
		int totalNum=questions.size();
		int correctNum=0;
		for(TestQuestion item:questions){
			if(item.isCorrect()){
				correctNum++;
			}
		}
		ResultDialogFragment dialog=ResultDialogFragment.newInstance(totalNum, correctNum);
		dialog.setCancelable(false);
		dialog.show(this.getFragmentManager(), null);
	}
	
}
