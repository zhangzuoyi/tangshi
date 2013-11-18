package com.zzy.xiaoyacz.data;

public class Author {
	private long id;
	private String name;
	private String initial;//拼音首字母
	private String intro;//介绍
	private String stage;//朝代 初唐 盛唐 中唐 晚唐
	public static final String ID="_id";
	public static final String NAME="name";
	public static final String INITIAL="initial";
	public static final String INTRO="intro";
	public static final String STAGE="stage";
	public static final String TABLE="author";
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInitial() {
		return initial;
	}
	public void setInitial(String initial) {
		this.initial = initial;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getStage() {
		return stage;
	}
	public void setStage(String stage) {
		this.stage = stage;
	}
	@Override
	public String toString() {
		return name;
	}
	
}
