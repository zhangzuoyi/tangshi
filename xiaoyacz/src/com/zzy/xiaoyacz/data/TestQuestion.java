package com.zzy.xiaoyacz.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TestQuestion implements Serializable {
	private long id;
	private String question;//问题
	private String type;//类型（ss:单选，ms:多选，fill:填空）
	private List<SelectItem> options;//选择项(json)
	private String answer;//答案
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<SelectItem> getOptions() {
		if(options==null){
			options=new ArrayList<SelectItem>();
		}
		return options;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
}
