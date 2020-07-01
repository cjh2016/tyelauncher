package com.boll.tyelauncher.view.wheelview;

package com.toycloud.launcher.view.wheelview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class WheelScroller {
    public static final int MIN_DELTA_FOR_SCROLLING = 1;
    private static final int SCROLLING_DURATION = 400;
    private final int MESSAGE_JUSTIFY = 1;
    private final int MESSAGE_SCROLL = 0;
    /* access modifiers changed from: private */
    public Handler animationHandler = new Handler() {
        public void handleMessage(Message msg) {
            WheelScroller.this.scroller.computeScrollOffset();
            int currY = WheelScroller.this.scroller.getCurrY();
            int delta = WheelScroller.this.lastScrollY - currY;
            int unused = WheelScroller.this.lastScrollY = currY;
            if (delta != 0) {
                WheelScroller.this.listener.onScroll(delta);
            }
            if (Math.abs(currY - WheelScroller.this.scroller.getFinalY()) < 1) {
                int currY2 = WheelScroller.this.scroller.getFinalY();
                WheelScroller.this.scroller.forceFinished(true);
            }
            if (!WheelScroller.this.scroller.isFinished()) {
                WheelScroller.this.animationHandler.sendEmptyMessage(msg.what);
            } else if (msg.what == 0) {
                WheelScroller.this.justify();
            } else {
                WheelScroller.this.finishScrolling();
            }
        }
    };
    private Context context;
    private GestureDetector gestureDetector;
    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return true;
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            int unused = WheelScroller.this.lastScrollY = 0;
            WheelScroller.this.scroller.fling(0, WheelScroller.this.lastScrollY, 0, (int) (-velocityY), 0, 0, -2147483647, Integer.MAX_VALUE);
            WheelScroller.this.setNextMessage(0);
            return true;
        }
    };
    private boolean isScrollingPerformed;
    /* access modifiers changed from: private */
    public int lastScrollY;
    private float lastTouchedY;
    /* access modifiers changed from: private */
    public ScrollingListener listener;
    /* access modifiers changed from: private */
    public Scroller scroller;

    public interface ScrollingListener {
        void onFinished();

        void onJustify();

        void onScroll(int i);

        void onStarted();
    }

    public WheelScroller(Context context2, ScrollingListener listener2) {
        this.gestureDetector = new GestureDetector(context2, this.gestureListener);
        this.gestureDetector.setIsLongpressEnabled(false);
        this.scroller = new Scroller(context2);
        this.listener = listener2;
        this.context = context2;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.scroller.forceFinished(true);
        this.scroller = new Scroller(this.context, interpolator);
    }

    public void scroll(int distance, int time) {
        this.scroller.forceFinished(true);
        this.lastScrollY = 0;
        this.scroller.startScroll(0, 0, 0, distance, time != 0 ? time : 400);
        setNextMessage(0);
        startScrolling();
    }

    public void stopScrolling() {
        this.scroller.forceFinished(true);
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                this.lastTouchedY = event.getY();
                this.scroller.forceFinished(true);
                clearMessages();
                break;
            case 2:
                int distanceY = (int) (event.getY() - this.lastTouchedY);
                if (distanceY != 0) {
                    startScrolling();
                    this.listener.onScroll(distanceY);
                    this.lastTouchedY = event.getY();
                    break;
                }
                break;
        }
        if (!this.gestureDetector.onTouchEvent(event) && event.getAction() == 1) {
            justify();
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void setNextMessage(int message) {
        clearMessages();
        this.animationHandler.sendEmptyMessage(message);
    }

    private void clearMessages() {
        this.animationHandler.removeMessages(0);
        this.animationHandler.removeMessages(1);
    }

    /* access modifiers changed from: private */
    public void justify() {
        this.listener.onJustify();
        setNextMessage(1);
    }

    private void startScrolling() {
        if (!this.isScrollingPerformed) {
            this.isScrollingPerformed = true;
            this.listener.onStarted();
        }
    }

    /* access modifiers changed from: package-private */
    public void finishScrolling() {
        if (this.isScrollingPerformed) {
            this.listener.onFinished();
            this.isScrollingPerformed = false;
        }
    }
}
