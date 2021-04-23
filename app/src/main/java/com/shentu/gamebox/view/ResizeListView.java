package com.shentu.gamebox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ResizeListView extends ListView {
    public ResizeListView(Context context) {
        super(context);
    }

    public ResizeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ResizeListView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
