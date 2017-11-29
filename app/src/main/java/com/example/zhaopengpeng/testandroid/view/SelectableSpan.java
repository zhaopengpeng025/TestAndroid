package com.example.zhaopengpeng.testandroid.view;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/*****************************************
 * 文  件：
 * 描  述：
 * 作  者： zhaopengpeng
 * 日  期： 2017/11/26
 *****************************************/
public class SelectableSpan extends ClickableSpan {

    private int color;

    public SelectableSpan(int currentTextColor) {
        this.color = currentTextColor;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
        ds.setColor(color);
    }

    @Override
    public void onClick(View widget) {

    }
}
