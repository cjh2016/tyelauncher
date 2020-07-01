package com.boll.tyelauncher.util;


import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.SystemClock;

public abstract class MyCountDownTimer {
    private static final int MSG = 1;
    /* access modifiers changed from: private */
    public boolean mCancelled = false;
    /* access modifiers changed from: private */
    public final long mCountdownInterval;
    /* access modifiers changed from: private */
    public long mElapsedRealtime;
    @SuppressLint({"HandlerLeak"})
    private Handler mHandler = new Handler() {
        /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void handleMessage(android.os.Message r15) {
            /*
                r14 = this;
                com.toycloud.launcher.util.MyCountDownTimer r7 = com.toycloud.launcher.util.MyCountDownTimer.this
                monitor-enter(r7)
                com.toycloud.launcher.util.MyCountDownTimer r6 = com.toycloud.launcher.util.MyCountDownTimer.this     // Catch:{ all -> 0x004d }
                boolean r6 = r6.mCancelled     // Catch:{ all -> 0x004d }
                if (r6 == 0) goto L_0x000d
                monitor-exit(r7)     // Catch:{ all -> 0x004d }
            L_0x000c:
                return
            L_0x000d:
                com.toycloud.launcher.util.MyCountDownTimer r6 = com.toycloud.launcher.util.MyCountDownTimer.this     // Catch:{ all -> 0x004d }
                long r2 = r6.mElapsedRealtime     // Catch:{ all -> 0x004d }
                com.toycloud.launcher.util.MyCountDownTimer r6 = com.toycloud.launcher.util.MyCountDownTimer.this     // Catch:{ all -> 0x004d }
                long r8 = android.os.SystemClock.elapsedRealtime()     // Catch:{ all -> 0x004d }
                long unused = r6.mElapsedRealtime = r8     // Catch:{ all -> 0x004d }
                com.toycloud.launcher.util.MyCountDownTimer r6 = com.toycloud.launcher.util.MyCountDownTimer.this     // Catch:{ all -> 0x004d }
                long r8 = r6.mElapsedRealtime     // Catch:{ all -> 0x004d }
                long r4 = r8 - r2
                com.toycloud.launcher.util.MyCountDownTimer r6 = com.toycloud.launcher.util.MyCountDownTimer.this     // Catch:{ all -> 0x004d }
                com.toycloud.launcher.util.MyCountDownTimer r8 = com.toycloud.launcher.util.MyCountDownTimer.this     // Catch:{ all -> 0x004d }
                long r8 = r8.mMillisFinished     // Catch:{ all -> 0x004d }
                long r8 = r8 + r4
                long unused = r6.mMillisFinished = r8     // Catch:{ all -> 0x004d }
                com.toycloud.launcher.util.MyCountDownTimer r6 = com.toycloud.launcher.util.MyCountDownTimer.this     // Catch:{ all -> 0x004d }
                long r8 = r6.mMillisInFuture     // Catch:{ all -> 0x004d }
                com.toycloud.launcher.util.MyCountDownTimer r6 = com.toycloud.launcher.util.MyCountDownTimer.this     // Catch:{ all -> 0x004d }
                long r10 = r6.mMillisFinished     // Catch:{ all -> 0x004d }
                int r6 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
                if (r6 > 0) goto L_0x0050
                com.toycloud.launcher.util.MyCountDownTimer r6 = com.toycloud.launcher.util.MyCountDownTimer.this     // Catch:{ all -> 0x004d }
                com.toycloud.launcher.util.MyCountDownTimer r8 = com.toycloud.launcher.util.MyCountDownTimer.this     // Catch:{ all -> 0x004d }
                long r8 = r8.mMillisFinished     // Catch:{ all -> 0x004d }
                r6.onFinish(r8)     // Catch:{ all -> 0x004d }
            L_0x004b:
                monitor-exit(r7)     // Catch:{ all -> 0x004d }
                goto L_0x000c
            L_0x004d:
                r6 = move-exception
                monitor-exit(r7)     // Catch:{ all -> 0x004d }
                throw r6
            L_0x0050:
                com.toycloud.launcher.util.MyCountDownTimer r6 = com.toycloud.launcher.util.MyCountDownTimer.this     // Catch:{ all -> 0x004d }
                com.toycloud.launcher.util.MyCountDownTimer r8 = com.toycloud.launcher.util.MyCountDownTimer.this     // Catch:{ all -> 0x004d }
                long r8 = r8.mMillisFinished     // Catch:{ all -> 0x004d }
                r6.onTick(r8)     // Catch:{ all -> 0x004d }
                com.toycloud.launcher.util.MyCountDownTimer r6 = com.toycloud.launcher.util.MyCountDownTimer.this     // Catch:{ all -> 0x004d }
                long r8 = r6.mMillisInFuture     // Catch:{ all -> 0x004d }
                com.toycloud.launcher.util.MyCountDownTimer r6 = com.toycloud.launcher.util.MyCountDownTimer.this     // Catch:{ all -> 0x004d }
                long r10 = r6.mMillisFinished     // Catch:{ all -> 0x004d }
                long r0 = r8 - r10
                com.toycloud.launcher.util.MyCountDownTimer r6 = com.toycloud.launcher.util.MyCountDownTimer.this     // Catch:{ all -> 0x004d }
                long r8 = r6.mCountdownInterval     // Catch:{ all -> 0x004d }
                int r6 = (r0 > r8 ? 1 : (r0 == r8 ? 0 : -1))
                if (r6 <= 0) goto L_0x0094
                com.toycloud.launcher.util.MyCountDownTimer r6 = com.toycloud.launcher.util.MyCountDownTimer.this     // Catch:{ all -> 0x004d }
                long r8 = r6.mElapsedRealtime     // Catch:{ all -> 0x004d }
                com.toycloud.launcher.util.MyCountDownTimer r6 = com.toycloud.launcher.util.MyCountDownTimer.this     // Catch:{ all -> 0x004d }
                long r10 = r6.mCountdownInterval     // Catch:{ all -> 0x004d }
                long r8 = r8 + r10
                long r10 = android.os.SystemClock.elapsedRealtime()     // Catch:{ all -> 0x004d }
                long r8 = r8 - r10
                com.toycloud.launcher.util.MyCountDownTimer r6 = com.toycloud.launcher.util.MyCountDownTimer.this     // Catch:{ all -> 0x004d }
                long r10 = r6.mMillisFinished     // Catch:{ all -> 0x004d }
                com.toycloud.launcher.util.MyCountDownTimer r6 = com.toycloud.launcher.util.MyCountDownTimer.this     // Catch:{ all -> 0x004d }
                long r12 = r6.mCountdownInterval     // Catch:{ all -> 0x004d }
                long r10 = r10 % r12
                long r0 = r8 - r10
            L_0x0094:
                r8 = 0
                int r6 = (r0 > r8 ? 1 : (r0 == r8 ? 0 : -1))
                if (r6 >= 0) goto L_0x00a2
                com.toycloud.launcher.util.MyCountDownTimer r6 = com.toycloud.launcher.util.MyCountDownTimer.this     // Catch:{ all -> 0x004d }
                long r8 = r6.mCountdownInterval     // Catch:{ all -> 0x004d }
                long r0 = r0 + r8
                goto L_0x0094
            L_0x00a2:
                r6 = 1
                android.os.Message r6 = r14.obtainMessage(r6)     // Catch:{ all -> 0x004d }
                r14.sendMessageDelayed(r6, r0)     // Catch:{ all -> 0x004d }
                goto L_0x004b
            */
            throw new UnsupportedOperationException("Method not decompiled: com.toycloud.launcher.util.MyCountDownTimer.AnonymousClass1.handleMessage(android.os.Message):void");
        }
    };
    /* access modifiers changed from: private */
    public long mMillisFinished = 0;
    /* access modifiers changed from: private */
    public final long mMillisInFuture;

    public abstract void onFinish(long j);

    public abstract void onTick(long j);

    public MyCountDownTimer(long millisInFuture, long countDownInterval) {
        this.mMillisInFuture = millisInFuture;
        this.mCountdownInterval = countDownInterval;
    }

    public final synchronized void cancel() {
        this.mCancelled = true;
        this.mHandler.removeMessages(1);
    }

    public final synchronized MyCountDownTimer stop() {
        this.mHandler.removeMessages(1);
        long elapsedRealtime = this.mElapsedRealtime;
        this.mElapsedRealtime = SystemClock.elapsedRealtime();
        this.mMillisFinished += this.mElapsedRealtime - elapsedRealtime;
        onTick(this.mMillisFinished);
        return this;
    }

    /* Debug info: failed to restart local var, previous not found, register: 6 */
    public final synchronized MyCountDownTimer start() {
        MyCountDownTimer myCountDownTimer;
        this.mCancelled = false;
        if (this.mMillisInFuture <= this.mMillisFinished) {
            onFinish(this.mMillisFinished);
            myCountDownTimer = this;
        } else {
            this.mElapsedRealtime = SystemClock.elapsedRealtime();
            this.mHandler.sendMessage(this.mHandler.obtainMessage(1));
            myCountDownTimer = this;
        }
        return myCountDownTimer;
    }

    public final synchronized long getNowTime() {
        return (this.mMillisFinished + SystemClock.elapsedRealtime()) - this.mElapsedRealtime;
    }
}
