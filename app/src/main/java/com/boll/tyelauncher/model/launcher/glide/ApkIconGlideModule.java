package com.boll.tyelauncher.model.launcher.glide;

package com.toycloud.launcher.model.launcher.glide;

import android.content.Context;
import android.support.annotation.Keep;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.module.GlideModule;
import com.toycloud.launcher.model.launcher.glide.ApkIconModelLoader;
import java.io.InputStream;

@Keep
public class ApkIconGlideModule implements GlideModule {
    public void applyOptions(Context context, GlideBuilder builder) {
    }

    public void registerComponents(Context context, Glide glide) {
        glide.register(GlideAppModel.class, InputStream.class, new ApkIconModelLoader.Factory());
    }
}
