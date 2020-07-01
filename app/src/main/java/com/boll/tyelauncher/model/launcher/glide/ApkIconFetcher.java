package com.boll.tyelauncher.model.launcher.glide;

package com.toycloud.launcher.model.launcher.glide;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.text.TextUtils;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.orhanobut.logger.Logger;
import com.toycloud.launcher.R;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ApkIconFetcher implements DataFetcher<InputStream> {
    private final GlideAppModel mAppInfo;
    private final int mBitmapSize = this.mContext.getResources().getDimensionPixelSize(R.dimen.px110);
    private final Context mContext;
    private final PackageManager mPackageManager;

    public ApkIconFetcher(Context context, GlideAppModel appInfo) {
        this.mContext = context;
        this.mAppInfo = appInfo;
        this.mPackageManager = context.getPackageManager();
    }

    public InputStream loadData(Priority priority) throws Exception {
        if (TextUtils.equals("com.peopledailychina.activity", this.mAppInfo.mPackageName)) {
            Logger.d("", "");
        }
        Bitmap iconBitmap = createIconBitmap(this.mPackageManager.getApplicationInfo(this.mAppInfo.mPackageName, 0).loadIcon(this.mPackageManager), this.mBitmapSize, this.mContext);
        if (iconBitmap != null) {
            return bitmap2InputStream(iconBitmap);
        }
        throw new IOException("createIconBitmap failed");
    }

    public static final Bitmap createIconBitmap(Drawable drawable, int bitmapSize, Context context) {
        if (drawable == null) {
            return null;
        }
        try {
            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            }
            if (drawable instanceof AdaptiveIconDrawable) {
                AdaptiveIconDrawable iconDrawable = (AdaptiveIconDrawable) drawable;
                int width = bitmapSize;
                int height = bitmapSize;
                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                iconDrawable.setBounds(0, 0, width, height);
                iconDrawable.draw(canvas);
                return bitmap;
            }
            if (drawable instanceof PaintDrawable) {
                PaintDrawable painter = (PaintDrawable) drawable;
                painter.setIntrinsicWidth(bitmapSize);
                painter.setIntrinsicHeight(bitmapSize);
                Bitmap bitmap2 = Bitmap.createBitmap(bitmapSize, bitmapSize, Bitmap.Config.ARGB_8888);
                Canvas canvas2 = new Canvas(bitmap2);
                painter.setBounds(0, 0, canvas2.getWidth(), canvas2.getHeight());
                painter.draw(canvas2);
                return bitmap2;
            }
            return null;
        } catch (Throwable th) {
        }
    }

    private InputStream bitmap2InputStream(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return new ByteArrayInputStream(baos.toByteArray());
    }

    public void cleanup() {
    }

    public String getId() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.mAppInfo.mPackageName).append("/").append(this.mAppInfo.mVersionCode);
        return sb.toString();
    }

    public void cancel() {
    }
}