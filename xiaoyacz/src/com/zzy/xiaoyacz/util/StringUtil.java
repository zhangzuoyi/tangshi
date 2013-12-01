package com.zzy.xiaoyacz.util;

public class StringUtil {

	public static boolean isBlank(String str){
		if(str==null){
			return true;
		}else if("".equals(str.trim())){
			return true;
		}
		return false;
	}

}
