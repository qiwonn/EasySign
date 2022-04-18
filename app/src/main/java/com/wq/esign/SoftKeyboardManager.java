package com.wq.esign;
import android.view.inputmethod.InputMethodManager;
import android.app.Activity;
import android.content.Context;
/** 软键盘管理器 */
public final class SoftKeyboardManager {

    /**
     * 关闭软键盘
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm!=null && imm.isActive() && null != activity.getCurrentFocus())
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

	/**
     * 打开软键盘
     * @param activity
     */
    public static void showSoftKeyboard(Activity activity){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
       	if(null != imm)
			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
