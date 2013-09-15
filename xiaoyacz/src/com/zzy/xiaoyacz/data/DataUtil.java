package com.zzy.xiaoyacz.data;

import java.util.ArrayList;
import java.util.List;

import com.zzy.xiaoyacz.R;

public class DataUtil {
	private static List<TangShi> data;
	static{
		data=new ArrayList<TangShi>();
		TangShi ts=new TangShi();
		ts.setTitle("春晓");
		ts.setAuthor("孟浩然");
		ts.setContent("春眠不觉晓，处处闻啼鸟。<br />夜来风雨声，花落知多少。");
		ts.setDegree(10);
//		ts.setAudio(R.raw.cx);
		ts.setExplain("这是一首惜春诗，诗人抓住春晨生活的一刹那，镌刻了自然的神髓，生活的真"+
								"趣，抒发了对烂漫醉人春光的喜悦，对生机勃勃春意的酷爱。言浅意浓，景真情真，"+
								"悠远深沉，韵味无穷。可以说是五言绝句中的一粒蓝宝石，传之千古，光彩照人。");
		data.add(ts);
		ts=new TangShi();
		ts.setTitle("静夜思");
		ts.setAuthor("李白");
		ts.setContent("床前明月光，疑是地上霜。<br />举头望明月，低头思故乡。");
		ts.setDegree(20);
//		ts.setAudio(R.raw.jys);
		ts.setExplain("这是写远客思乡之情的诗，诗以明白如话的语言雕琢出明静醉人的秋夜的意境。"+
								"它不追求想象的新颖奇特，也摒弃了辞藻的精工华美；它以清新朴素的笔触，抒写了"+
								"丰富深曲的内容。境是境，情是情，那么逼真，那么动人，百读不厌，耐人寻绎。无"+
								"怪乎有人赞它是“妙绝古今”。"+
								 "李白的这首思乡之作，被称为“千古思乡第一诗”，感动了古今无数他乡流落之人。");
		data.add(ts);
	}
	public static List<TangShi> getData() {
		return data;
	}
	
}
