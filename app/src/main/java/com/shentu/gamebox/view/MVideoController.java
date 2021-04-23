package com.shentu.gamebox.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.shentu.gamebox.utils.ScreenUtils;


public class MVideoController extends RelativeLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private Context mContext;
    private int dip_10;
    private int dip_40;
    private ImageView mImagePlay;
    private int mBeginViewId = 0x7F24FFF0;
    private TextView mCurrentTime;
    private TextView mTotalTime;
    private SeekBar mSeekBar;
    private VideoView mVideoView;
    private int mDuration;
    private int mCurrent;
    private int mBuffer;
    private boolean bPause;

    public MVideoController(Context context) {
        super(context,null);
    }

    public MVideoController(Context context, AttributeSet attrs) {
        super(context, attrs,0);
    }

    public MVideoController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        dip_10 = ScreenUtils.dp2px(mContext, 10);
        dip_40 = ScreenUtils.dp2px(mContext, 40);
        initView();
    }



    private void initView() {
        /*播放按钮布局*/
        mImagePlay = new ImageView(mContext);
        LayoutParams imageParams = new LayoutParams(dip_40, dip_10);
        imageParams.addRule(CENTER_VERTICAL);
        mImagePlay.setLayoutParams(imageParams);
        mImagePlay.setId(mBeginViewId);
        mImagePlay.setOnClickListener(this);
        /*播放时长*/
        mCurrentTime = newTextView(mContext,mBeginViewId+1);
        RelativeLayout.LayoutParams currentParams = (LayoutParams) mCurrentTime.getLayoutParams();
        currentParams.setMargins(dip_10,0,0,0);
        currentParams.addRule(RelativeLayout.RIGHT_OF,mImagePlay.getId());
        mCurrentTime.setLayoutParams(currentParams);
        /*总时长*/
        mTotalTime = newTextView(mContext, mBeginViewId + 2);
        RelativeLayout.LayoutParams totalParams = (LayoutParams) mTotalTime.getLayoutParams();
        totalParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        mTotalTime.setLayoutParams(totalParams);
        /*播放进度条*/
        mSeekBar = new SeekBar(mContext);
        LayoutParams seekBarParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        seekBarParams.setMargins(dip_10,0,0,0);
        seekBarParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        seekBarParams.addRule(RelativeLayout.RIGHT_OF,mCurrentTime.getId());
        seekBarParams.addRule(RelativeLayout.LEFT_OF,mTotalTime.getId());
        mSeekBar.setLayoutParams(seekBarParams);
        mSeekBar.setMax(100);
        mSeekBar.setMinimumHeight(100);
        mSeekBar.setThumbOffset(0);
        mSeekBar.setId(mBeginViewId + 3);
        mSeekBar.setOnSeekBarChangeListener(this);


    }

    private TextView newTextView(Context mContext, int id) {
        TextView view = new TextView(mContext);
        view.setId(id);
        view.setGravity(Gravity.CENTER);
        view.setTextColor(Color.WHITE);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);

        return view;
    }

    private void reset(){

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mImagePlay.getId()){
            if (mVideoView.isPlaying()){
                mVideoView.pause();
                bPause = true;
            }else{
                if (mCurrent ==0){
//                    mVideoView.begin(null);
                }
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void setVideoView(VideoView videoView){
        mVideoView = videoView;
        mDuration = mVideoView.getDuration();
    }

    public void setCurrenTime(int current_time,int buffer_time){
        mCurrent = current_time;
        mBuffer = buffer_time;

    }
}
