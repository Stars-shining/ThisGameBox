package com.shentu.gamebox.view;

import android.content.Context;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.Window;

public class DragableView extends androidx.appcompat.widget.AppCompatImageView {

    public DragableView(Context context) {
        super(context);
        mContext = context;

        // TODO Auto-generated constructor stub
    }

    private Context mContext;

    public DragableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

    }

    public DragableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;

    }

    private float x = 0;
    private float y = 0;
    private int defaultHeight = 70; //

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
    }

    /**
     * 获得状态栏和标题栏的高度
     *
     * @return
     */
    private int getStatusAndTitleBarHeight() {
        Activity ac = (Activity) mContext;

        Rect frame = new Rect();
        ac.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top; // 状态栏高度

        int contentTop = ac.getWindow().findViewById(Window.ID_ANDROID_CONTENT)
                .getTop();
        // statusBarHeight是上面所求的状态栏的高度
        int titleBarHeight = contentTop - statusBarHeight; // 标题栏高度
        return statusBarHeight + titleBarHeight;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // TODO Auto-generated method stub
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                defaultHeight = getStatusAndTitleBarHeight();
                // 触控笔按下
                x = event.getX();
                // 得到X坐标
                y = event.getY(); // 要减去标题栏和状态栏高度
                //
                break;
            case MotionEvent.ACTION_MOVE:
                int xx = (int) (event.getRawX() - x);
                int yy = (int) (event.getRawY() - y);

                this.layout(xx, yy, xx + getWidth(), yy + getHeight());
//                 ViewGroup.LayoutParams lp = new LayoutParams(
//                 LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
//                 (int) event.getRawX() - x, (int) event.getRawY() - y);
//                 this.setLayoutParams(lp);
                break;

            default:
                break;
        }
        return true;

    }
}