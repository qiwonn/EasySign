package com.wq.esign;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import android.text.TextUtils;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.Spanned;
/** 文本工具 */
public class TextHelper
{
	public static int SHOW_LENGTH = 16;
	//提取纯文本
	public String clearFormat(String s){
		//<p>段落替换为换行
		s = s.replaceAll("<p .*?>","\n");
		//<br></br>替换为换行
		s = s.replaceAll("<br\\s*/?>","\n");
		//去掉其它的<>之间的东西
		s = s.replaceAll("\\<.*?>","");
		return s;
	}
	
	public Pattern setSearchString(String searchString) {
		if (!TextUtils.isEmpty(searchString)) {
			return Pattern.compile(searchString.toLowerCase());
		}
		return null;
	}
	
	/**
	 * 高亮显示指定字符串中的字符
	 * @param view 显示字符串的textview
	 * @param text 初始字符串
	 */
	public SpannableString hightLightMatcherText(Pattern p1,String text,int color){
		SpannableString spannableString = new SpannableString(text);
		Matcher matcher = p1.matcher(text.toLowerCase());
		if (matcher.find()) {
			spannableString.setSpan(new ForegroundColorSpan(color),
									matcher.start(), matcher.end(),
									Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return spannableString;
	}

	/**
	 * 截取指定字符串的一部分，并高亮显示其中的指定字符串
	 * @param view  显示字符串的textview
	 * @param text  初始字符串
	 */
	public SpannableString hightLightMatcherTextCut(Pattern p1,String text,int color){
		SpannableString spannableString = null;
		Matcher matcher = p1.matcher(text.toLowerCase());

		if (matcher.find()) {
			int start, end;
			int span_start = 0;
			if (matcher.start() - SHOW_LENGTH < 0) {
				start = 0;
				span_start = matcher.start();
			} else {
				start = matcher.start() - SHOW_LENGTH;
				span_start = SHOW_LENGTH;
			}

			if (matcher.end() + SHOW_LENGTH > text.length()) {
				end = text.length();
			} else {
				end = matcher.end() + SHOW_LENGTH;
			}

			text = text.substring(start, end);

			spannableString = new SpannableString(text);
			spannableString.setSpan(new ForegroundColorSpan(color),
									span_start, span_start + p1.pattern().length(),
									Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			return spannableString;
		}
		return null;
	}
}
