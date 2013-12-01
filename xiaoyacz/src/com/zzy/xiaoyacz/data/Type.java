package com.zzy.xiaoyacz.data;

import java.util.HashMap;
import java.util.Map;

public class Type {
	public static final String WJ="五言绝句";
	public static final String QJ="七言绝句";
	public static final String WL="五言律诗";
	public static final String QL="七言律诗";
	public static final String WG="五言古诗";
	public static final String QG="七言古诗";
	public static final String YF="乐府";
	private static Map<String,String> typeMap;
	static{
		typeMap=new HashMap<String,String>();
		typeMap.put(WJ, "五言绝句是绝句的一种，就是指五言四句而又合乎律诗规范的小诗，属于近体诗范畴。有仄起、平起二格。此体源于汉代乐府小诗，深受六朝民歌影响。到了唐代与近体律诗如孪生姐妹，并蒂双花，以其崭新的异彩出现在诗坛上。五言绝句仅二十字，便能展现出一幅幅清新的画图，传达一种种真切的意境。因小见大，以少总多，在短章中包含着丰富的内容，是其最大特色。代表作品有王维的《鸟鸣涧》、李白的《独坐敬亭山》、杜甫的《八阵图》、王之涣的《登鹳雀楼》、刘长卿的《送灵澈上人》等。");
		typeMap.put(QJ, "七言绝句是绝句的一种，属于近体诗范畴。绝句是由四句组成，有严格的格律要求。常见的绝句有五言绝句、七言绝句，还有很少见的六言绝句。每句七个字的绝句即是七言绝句。");
		typeMap.put(WL, "五言律诗，简称五律，是中国近体诗（格律诗）中的一种样式，年代为初唐。其格式是全诗共8句，每句5个字，有仄起、平起二格。三四句、五六句均为对仗句。代表诗作有唐代诗人李白的《送友人》、杜甫的《春望》、温庭筠的《商山早行》、李商隐《蝉》等。");
		typeMap.put(QL, "律诗是中国近体诗的一种。格律严密。发源于南朝齐永明时沈约等讲究声律、对偶的新体诗，至初唐沈佺期、宋之问时正式定型，成熟于盛唐时期。律诗要求诗句字数整齐划一，律诗由八句组成，七字句的称七言律诗。");
		typeMap.put(WG, "五言古诗是汉、魏时期形成的一种新诗体。它没有一定的格律，不限长短，不讲平仄，用韵也相当自由，但句式——每句五言却是固定不变的。因为它既不同于汉代乐府歌辞，也不同于唐代的近体律诗和绝句，故称五言古诗。");
		typeMap.put(QG, "七言古诗简称七古，在古代诗歌中，是形式最活泼、体裁最多样、句法和韵脚的处理最自由，而且抒情叙事最富有表现力的一种诗歌形式，诗体全篇每句七字或以七字句为主。简单地说就是篇幅较长，容量较大，用韵灵活。如杜甫的七言古诗代表作《观公孙大娘弟子舞剑器行并序》、《丹青引赠曹霸将军》等。");
		typeMap.put(YF, "乐府为中国传统诗歌诗体的一种，与古体诗、近体诗构成古典诗歌中的三大类，原指合音乐以唱的歌诗。由于乐府是合乐的声诗，以后凡是可传唱的诗，广义上都可称为乐府，因此乐府不仅是齐言的诗，连长短句的词、曲，也被士人俗称为乐府，如苏轼的词集《东坡乐府》、张可久的曲集《小山乐府》等便是。");
	}
	public static String getTypeExplain(String type){
		return typeMap.get(type);
	}
}
