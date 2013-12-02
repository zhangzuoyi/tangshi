package com.zzy.xiaoyacz.data;

import android.os.Parcel;
import android.os.Parcelable;

public class TangShi implements Parcelable{
	private long id;
	private String title;
	private String author;
	private String content;
	private String explain;//赏析
	private int img;
	private String audio;
	private int degree;//难度
	private String type;
	private int collectStatus;//收藏状态，0未收藏，1已收藏
	private String comments;//注释
	private String translate;//译文
	public TangShi(){
		
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getExplain() {
		return explain;
	}
	public void setExplain(String explain) {
		this.explain = explain;
	}
	public int getDegree() {
		return degree;
	}
	public void setDegree(int degree) {
		this.degree = degree;
	}
	public int getImg() {
		return img;
	}
	public void setImg(int img) {
		this.img = img;
	}
	public String getAudio() {
		return audio;
	}
	public void setAudio(String audio) {
		this.audio = audio;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getCollectStatus() {
		return collectStatus;
	}
	public void setCollectStatus(int collectStatus) {
		this.collectStatus = collectStatus;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getTranslate() {
		return translate;
	}
	public void setTranslate(String translate) {
		this.translate = translate;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeString(title);
		dest.writeString(author);
		dest.writeString(content);
		dest.writeString(explain);
		dest.writeInt(img);
		dest.writeString(audio);
		dest.writeInt(degree);
		dest.writeString(type);
		dest.writeInt(collectStatus);
		dest.writeString(comments);
		dest.writeString(translate);
	}
	
	public static final Parcelable.Creator<TangShi> CREATOR
	    = new Parcelable.Creator<TangShi>() {
	public TangShi createFromParcel(Parcel in) {
	    return new TangShi(in);
	}
	
	public TangShi[] newArray(int size) {
	    return new TangShi[size];
	}
	};
	
	private TangShi(Parcel in) {
		id=in.readLong();
		title=in.readString();
		author=in.readString();
		content=in.readString();
		explain=in.readString();
		img=in.readInt();
		audio=in.readString();
		degree=in.readInt();
		type=in.readString();
		collectStatus=in.readInt();
		comments=in.readString();
		translate=in.readString();
	}
}
