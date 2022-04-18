package com.wq.esign;

public enum CursorManager
{
	INSTANCE {

		@Override
		public void setSelection(int v0, int v1)
		{
			selStart = v0;
			selEnd = v1;
		}

		@Override
		public int getSelStart()
		{
			return selStart;
		}

		@Override
		public int getSelEnd()
		{
			return selEnd;
		}
	};
	
	private static int selStart = -1;
	private static int selEnd = -1;
	public abstract void setSelection(int v0,int v1);
	public abstract int getSelStart();
	public abstract int getSelEnd();
}
