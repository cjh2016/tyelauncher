package com.boll.tyelauncher.view;

package com.toycloud.launcher.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyClickButton extends View {
    public static final int TO_LEFT = 11;
    public static final int TO_RIGHT = 22;
    /* access modifiers changed from: private */
    public int center;
    private float endx;
    Handler handler;
    /* access modifiers changed from: private */
    public boolean isAnimate;
    private boolean isRight;
    private int measuredHeight;
    private int measuredWidth;
    private int mid_x;
    private OnMClickListener onClickListener;
    /* access modifiers changed from: private */
    public Paint paint;
    /* access modifiers changed from: private */
    public int rec_x;
    private int smallCenter;
    /* access modifiers changed from: private */
    public float smallCenter_x;
    private Paint smallPaint;
    private float startx;

    public interface OnMClickListener {
        void onClick(boolean z);
    }

    public MyClickButton(Context context) {
        this(context, (AttributeSet) null);
    }

    public MyClickButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyClickButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.isRight = true;
        this.isAnimate = false;
        this.handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 11:
                        MyClickButton.this.paint.setColor(-7829368);
                        if (MyClickButton.this.smallCenter_x <= ((float) MyClickButton.this.center)) {
                            float unused = MyClickButton.this.smallCenter_x = (float) MyClickButton.this.center;
                            MyClickButton.this.setEnabled(true);
                            boolean unused2 = MyClickButton.this.isAnimate = false;
                            break;
                        } else {
                            float unused3 = MyClickButton.this.smallCenter_x = MyClickButton.this.smallCenter_x - 5.0f;
                            MyClickButton.this.handler.sendEmptyMessageDelayed(11, 1);
                            boolean unused4 = MyClickButton.this.isAnimate = true;
                            break;
                        }
                    case 22:
                        MyClickButton.this.paint.setColor(Color.parseColor("#2A8AFB"));
                        if (MyClickButton.this.smallCenter_x >= ((float) MyClickButton.this.rec_x)) {
                            float unused5 = MyClickButton.this.smallCenter_x = (float) MyClickButton.this.rec_x;
                            MyClickButton.this.setEnabled(true);
                            boolean unused6 = MyClickButton.this.isAnimate = false;
                            break;
                        } else {
                            float unused7 = MyClickButton.this.smallCenter_x = MyClickButton.this.smallCenter_x + 5.0f;
                            MyClickButton.this.handler.sendEmptyMessageDelayed(22, 1);
                            boolean unused8 = MyClickButton.this.isAnimate = true;
                            break;
                        }
                }
                MyClickButton.this.invalidate();
            }
        };
        init();
    }

    public void setOnMbClickListener(OnMClickListener onClickListener2) {
        this.onClickListener = onClickListener2;
    }

    private void init() {
        this.paint = new Paint(1);
        this.smallPaint = new Paint(1);
        this.paint.setColor(Color.parseColor("#2A8AFB"));
        this.smallPaint.setColor(-1);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.measuredHeight = getMeasuredHeight();
        this.measuredWidth = getMeasuredWidth();
        this.center = this.measuredHeight / 2;
        this.rec_x = this.measuredWidth - this.center;
        this.smallCenter = this.center - 5;
        this.smallCenter_x = (float) this.rec_x;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        canvas.drawCircle((float) this.center, (float) this.center, (float) this.center, this.paint);
        canvas.drawRect((float) this.center, 0.0f, (float) this.rec_x, (float) this.measuredHeight, this.paint);
        canvas.drawCircle((float) this.rec_x, (float) this.center, (float) this.center, this.paint);
        canvas.drawCircle(this.smallCenter_x, (float) this.center, (float) this.smallCenter, this.smallPaint);
        super.onDraw(canvas);
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                this.startx = event.getX();
                this.endx = event.getX();
                break;
            case 1:
                if (Math.abs(event.getX() - Math.abs(this.startx)) >= 2.0f) {
                    this.mid_x = this.center + ((this.rec_x - this.center) / 2);
                    if (this.smallCenter_x < ((float) this.mid_x)) {
                        this.isRight = false;
                        this.smallCenter_x = (float) this.center;
                        this.paint.setColor(-7829368);
                        setEnabled(true);
                        invalidate();
                    } else {
                        this.isRight = true;
                        this.smallCenter_x = (float) this.rec_x;
                        this.paint.setColor(Color.parseColor("#2A8AFB"));
                        setEnabled(true);
                        invalidate();
                    }
                } else if (!this.isAnimate) {
                    startGO();
                }
                if ((this.smallCenter_x == ((float) this.rec_x) || this.smallCenter_x == ((float) this.center)) && this.onClickListener != null) {
                    this.onClickListener.onClick(this.isRight);
                    break;
                }
            case 2:
                this.smallCenter_x += event.getX() - this.endx;
                if (this.smallCenter_x > ((float) this.rec_x)) {
                    this.isRight = true;
                    this.smallCenter_x = (float) this.rec_x;
                    this.paint.setColor(Color.parseColor("#2A8AFB"));
                } else if (this.smallCenter_x < ((float) this.center)) {
                    this.smallCenter_x = (float) this.center;
                    this.isRight = false;
                    this.paint.setColor(-7829368);
                }
                invalidate();
                break;
        }
        return true;
    }

    public void goLeft() {
        this.isRight = false;
        this.handler.sendEmptyMessageDelayed(11, 40);
    }

    public void goRight() {
        this.isRight = true;
        this.handler.sendEmptyMessageDelayed(22, 40);
    }

    public void startGO() {
        if (this.isRight) {
            goLeft();
        } else {
            goRight();
        }
    }
}
