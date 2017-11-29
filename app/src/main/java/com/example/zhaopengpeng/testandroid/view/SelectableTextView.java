package com.example.zhaopengpeng.testandroid.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*****************************************
 * 文  件： 
 * 描  述： 
 * 作  者： zhaopengpeng 
 * 日  期： 2017/11/26
 *****************************************/

public class SelectableTextView extends AppCompatTextView{

    private int oldStart;
    private int oldEnd;

    private SelectableTextListener mListener;

    public SelectableTextView(Context context) {
        super(context);
    }

    public SelectableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        int x = (int) event.getX();
        int y = (int) event.getY();

        x -= getTotalPaddingLeft();
        y -= getTotalPaddingTop();

        x += getScrollX();
        y += getScrollY();

        Layout layout = getLayout();
        int line = layout.getLineForVertical(y); //获取点击TextView的行数
        int off = layout.getOffsetForHorizontal(line, x);//获取点击TextView当前行内容的索引
        CharSequence text = getText();
        if (TextUtils.isEmpty(text) || !(text instanceof Spannable)) {
            return false;
        }
        Spannable buffer = (Spannable) text;
        ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);//获取点击位置的ClickableSpan
        if (link.length != 0) {
            if (action == MotionEvent.ACTION_UP ) {
                int start = buffer.getSpanStart(link[0]);
                int end = buffer.getSpanEnd(link[0]);
                if (start >= 0 && end >= start) {
                    if(oldStart == start && oldEnd == end){
                        return true;
                    }
                    if(oldStart >=0 && oldEnd >= 0 && oldEnd >= oldStart){
                        buffer.setSpan(new BackgroundColorSpan(Color.TRANSPARENT), 0, text.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    buffer.setSpan(new BackgroundColorSpan(Color.parseColor("#88FF4081")), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    oldStart = start;
                    oldEnd = end;
                    Log.e("zpp",text.toString().substring(start,end));
                    Log.e("zpp","start:"+start+" end:"+end);
                    //获取到点击单词
                    if(mListener != null){
                        mListener.onObtainWord(text.toString().substring(start,end));
                    }
                }
            }
        }
        return true;
    }

    public void setContent(String content) {
        setText("");
        int length = content.length();
        int start = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = content.charAt(i);
            if (matches(c)) {
                if (sb.length() > 0) {
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(sb.toString());
                    SelectableSpan selectableSpan = new SelectableSpan(getCurrentTextColor());
                    spannableStringBuilder.setSpan(selectableSpan, 0, sb.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    append(spannableStringBuilder);
                }
                sb = new StringBuilder();
                SpannableString mSpannableString = new SpannableString(String.valueOf(c));
                append(mSpannableString);
            } else {
                sb.append(c);
            }
        }
    }

    private boolean matches(char s) {
        String regEx = "[\n\r`~!-@#$%^&*() +=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(String.valueOf(s));
        return m.matches();
    }

    public void setOnSelectableTextListener(SelectableTextListener listener){
        mListener = listener;
    }

    public interface SelectableTextListener{
        void onObtainWord(String word);
    }
}
