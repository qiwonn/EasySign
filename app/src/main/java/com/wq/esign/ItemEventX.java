package com.wq.esign;

import org.greenrobot.eventbus.EventBus;

public class ItemEventX {
    /*private int action;
	private SignBeanX sb;

    private ItemEventX(int action) {
        this(action, null);
    }

	private ItemEventX(int action, SignBeanX sb) {
		this.action = action;
		this.sb = sb;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getAction() {
        return action;
    }

    public SignBeanX getSb() {
        return sb;
    }

    public static void postAddAction(SignBeanX sb) {
        postAction(new ItemEventX(GlobalConst.ACTION_ADD, sb));
    }

    public static void postRemoveAction(SignBeanX sb) {
        postAction(new ItemEventX(GlobalConst.ACTION_REMOVE,sb));
    }

    public static void postClearAction() {
        postAction(new ItemEventX(GlobalConst.ACTION_CLEAR));
    }

    public static void postUpdateAction(SignBeanX sb) {
        postAction(new ItemEventX(GlobalConst.ACTION_UPDATE,sb));
    }

    private static void postAction(ItemEventX event) {
        EventBus.getDefault().post(event);
    }

	public static void postAddStickyAction(SignBeanX sb) {
        EventBus.getDefault().postSticky(new ItemEventX(GlobalConst.ACTION_ADD, sb));
    }

    @Override
    public String toString() {
        return "name=" + action + " time=" + "";
    }*/
}
