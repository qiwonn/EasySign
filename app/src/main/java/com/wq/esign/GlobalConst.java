package com.wq.esign;
/** 全局静态常量 */
public class GlobalConst
{
	public static final char[] IMG = {'[','^','‿','^',']'};
	//public static final String IMG = "[^_^]";
	public static final int PHOTO_SUCCESS = 1;
    public static final int CAMERA_SUCCESS = 2;
	public static final int SIGN_COMPLETE = 4;
	public static final int RK_MAIN = 7;
	public static final int RK_CREATE = 8;
	public static final int RK_DETAIL = 9;
	public static final int SELECT_CHECK = 10;
	public static final int DP_SID_CONT = 14;
	public static final int DP_IMG_WIDT = 32;
	public static final int DP_IMG_MARG = 16;
	public static final int DP_TIT_BART = 54;
	// 事件巴士 ACTION事件
	// 增、删、改、查、更新
    public static final int ACTION_ADD = 21;
    public static final int ACTION_REMOVE = 22;
	public static final int ACTION_MODIFY = 24;
	public static final int ACTION_CHECK = 25;
	public static final int ACTION_UPDATE = 26;
	// 置顶
	public static final int ACTION_TOP = 27;
	public static final int ACTION_TOP_CANCEL = 28;
	// 编辑、取消、全选、全不选
	public static final int ACTION_EDIT = 29;
	public static final int ACTION_EDIT_CANCEL = 30;
	public static final int ACTION_SELECT_ALL = 31;
	public static final int ACTION_SELECT_NONE = 32;
	// 颜色值
	public static final int COLOR_HINT = 0xffc0c0c0;
}
