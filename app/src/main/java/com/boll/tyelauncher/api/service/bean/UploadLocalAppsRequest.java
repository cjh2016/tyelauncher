package com.boll.tyelauncher.api.service.bean;

package com.toycloud.launcher.api.service.bean;

import android.support.annotation.Keep;
import java.util.ArrayList;
import java.util.List;

@Keep
public class UploadLocalAppsRequest {
    public List<UploadAppInfo> appList = new ArrayList();
    public String sn;
}
