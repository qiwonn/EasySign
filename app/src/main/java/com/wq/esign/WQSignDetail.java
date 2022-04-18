package com.wq.esign;
/** 详情 */
public class WQSignDetail
{
	private boolean hasContent = false;
	private String content = null;
	private android.text.Editable editable = null;
	public WQSignDetail(String p1){
		content = p1;
		hasContent = true;
	}
	public WQSignDetail(android.text.Editable p1){
		editable = p1;
	}
	public boolean hasContent(){
		return hasContent;
	}
	public String content(){
		return content;
	}
	public android.text.Editable editable(){
		return editable;
	}
	
	public void release(){
		if(editable != null)editable.clear();
	}
}
