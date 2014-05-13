package com.zzy.xiaoyacz;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

public class ResultDialogFragment extends DialogFragment{
	public static ResultDialogFragment newInstance(int totalNum,int correctNum){
		ResultDialogFragment dialog=new ResultDialogFragment();
		Bundle bd=new Bundle();
		bd.putInt("totalNum", totalNum);
		bd.putInt("correctNum", correctNum);
		dialog.setArguments(bd);
		return dialog;
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int totalNum=getArguments().getInt("totalNum");
		int correctNum=getArguments().getInt("correctNum");
		AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
		dialog.setTitle(R.string.quiz_result);
		View view=getActivity().getLayoutInflater().inflate(R.layout.quiz_result, null);
		RatingBar rb=(RatingBar) view.findViewById(R.id.ratingBar);
		rb.setRating(correctNum*5.0f/totalNum);
		TextView tv1=(TextView) view.findViewById(R.id.resultText1);
		TextView tv2=(TextView) view.findViewById(R.id.resultText2);
		tv1.setText(getResultStr(totalNum,correctNum));
		tv2.setText(getActivity().getResources().getString(R.string.quiz_result_message2, totalNum,correctNum));
		dialog.setView(view);
		dialog.setPositiveButton(R.string.action_return, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				ResultDialogFragment.this.dismiss();
			}
			
		});
		dialog.setNegativeButton(R.string.restart, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				QuizDataAdapter qda=(QuizDataAdapter)getActivity();
				qda.reloadQuiz();
			}
			
		});
		return dialog.create();
	}
	private String getResultStr(int totalNum,int correctNum){
		int resultInt=correctNum*10/totalNum;
		if(resultInt>=10){
			return getActivity().getResources().getString(R.string.result_100);
		}else if(resultInt>=9){
			return getActivity().getResources().getString(R.string.result_90);
		}else if(resultInt>=8){
			return getActivity().getResources().getString(R.string.result_80);
		}else if(resultInt>=7){
			return getActivity().getResources().getString(R.string.result_70);
		}else if(resultInt>=6){
			return getActivity().getResources().getString(R.string.result_60);
		}else if(resultInt>=4){
			return getActivity().getResources().getString(R.string.result_50);
		}else{
			return getActivity().getResources().getString(R.string.result_30);
		}
	}
	public interface QuizDataAdapter{
		void reloadQuiz();
	}
}