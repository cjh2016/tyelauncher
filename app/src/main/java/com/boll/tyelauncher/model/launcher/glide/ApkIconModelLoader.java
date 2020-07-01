package com.boll.tyelauncher.model.launcher.glide;

package com.toycloud.launcher.model.launcher.glide;

import android.content.Context;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import java.io.InputStream;
import org.apache.http.HttpStatus;

public class ApkIconModelLoader implements ModelLoader<GlideAppModel, InputStream> {
    private Context context;
    private final ModelCache<GlideAppModel, GlideAppModel> modelCache;

    public ApkIconModelLoader(Context context2, ModelCache<GlideAppModel, GlideAppModel> modelCache2) {
        this.context = context2;
        this.modelCache = modelCache2;
    }

    public DataFetcher<InputStream> getResourceFetcher(GlideAppModel model, int i, int i1) {
        GlideAppModel url = model;
        if (this.modelCache != null && (url = this.modelCache.get(model, 0, 0)) == null) {
            this.modelCache.put(model, 0, 0, model);
            url = model;
        }
        return new ApkIconFetcher(this.context, url);
    }

    static class Factory implements ModelLoaderFactory<GlideAppModel, InputStream> {
        private final ModelCache<GlideAppModel, GlideAppModel> modelCache = new ModelCache<>(HttpStatus.SC_INTERNAL_SERVER_ERROR);

        Factory() {
        }

        public ModelLoader<GlideAppModel, InputStream> build(Context context, GenericLoaderFactory genericLoaderFactory) {
            return new ApkIconModelLoader(context, this.modelCache);
        }

        public void teardown() {
        }
    }
}