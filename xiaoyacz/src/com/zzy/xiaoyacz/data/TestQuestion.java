package com.zzy.xiaoyacz.data;

import java.io.Serializable;

public class TestQuestion implements Serializable {
	private long id;
	private String question;//问题
	private String type;//类型（单选，多选，填空）
	private String options;//选择项(json)
	private String answer;//答案
}
