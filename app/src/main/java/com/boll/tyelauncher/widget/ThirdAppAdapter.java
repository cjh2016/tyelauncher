package com.boll.tyelauncher.widget;

package com.toycloud.launcher.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.database.Observable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.anarchy.classify.adapter.BaseMainAdapter;
import com.anarchy.classify.adapter.BaseSubAdapter;
import com.anarchy.classify.simple.AppInfo;
import com.anarchy.classify.simple.PrimitiveSimpleAdapter;
import com.iflytek.cbg.common.utils.ListUtils;
import com.iflytek.stats.StatsHelper;
import com.orhanobut.logger.Logger;
import com.toycloud.launcher.R;
import com.toycloud.launcher.common.GlobalVariable;
import com.toycloud.launcher.databinding.ItemIReaderFolderBinding;
import com.toycloud.launcher.model.AppContants;
import com.toycloud.launcher.model.EtsConstant;
import com.toycloud.launcher.ui.MainActivity;
import com.toycloud.launcher.util.EtsUtils;
import com.toycloud.launcher.util.NoDoubleClickUtils;
import com.toycloud.launcher.util.PadInfoUtil;
import com.toycloud.launcher.util.SharepreferenceUtil;
import com.toycloud.launcher.util.UpdateUtil;
import com.toycloud.launcher.view.DialogUtil;
import com.toycloud.launcher.view.ShowAppForbiddenNoticeWin;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThirdAppAdapter extends PrimitiveSimpleAdapter<IReaderMockDataGroup, ViewHolder> {
    private static final String[] NEED_LOGIN_PACKAGES = {"com.toycloud.app.greenbrowser", GlobalVariable.SYNCHRONOUSEXERCISE_PKG, GlobalVariable.RECOMMEND_PREPAREFORSCHOOL_PKG, "com.iflytek.sceneenglish", AppContants.RECITE_BOOK, EtsConstant.ETS_PHONETIC_VIRTUAL_PACKAGE};
    private static final List<String> NEED_LOGIN_PACKAGES_LIST = Arrays.asList(NEED_LOGIN_PACKAGES);
    private static final String TAG = "ThirdAppAdapter";
    private static final Map<String, Integer> sPresentIcons = new HashMap();
    private DataChangeInterFace dataChangeInterFace;
    public boolean isCancelEdite = true;
    public boolean isDragEnd = false;
    private List<IReaderMockData> mCheckedData = new ArrayList();
    /* access modifiers changed from: private */
    public Context mContext;
    private DialogInterface.OnDismissListener mDismissListener = new DialogInterface.OnDismissListener() {
        public void onDismiss(DialogInterface dialog) {
            if (ThirdAppAdapter.this.mObservable.isRegister(ThirdAppAdapter.this.mSubObserver)) {
                ThirdAppAdapter.this.mObservable.unregisterObserver(ThirdAppAdapter.this.mSubObserver);
            }
            boolean unused = ThirdAppAdapter.this.mSubEditMode = false;
        }
    };
    private int[] mDragPosition = new int[2];
    private boolean mEditMode;
    private List<IReaderMockDataGroup> mLastMockGroup;
    private List<IReaderMockData> mMockSource;
    private boolean mMockSourceChanged;
    /* access modifiers changed from: private */
    public IReaderObservable mObservable = new IReaderObservable();
    /* access modifiers changed from: private */
    public boolean mSubEditMode;
    /* access modifiers changed from: private */
    public SubObserver mSubObserver = new SubObserver(this.mObservable);

    public interface DataChangeInterFace {
        void setDataChange(List<IReaderMockData> list);
    }

    static {
        sPresentIcons.put("com.android.settings", Integer.valueOf(R.drawable.appicon_setting));
    }

    public ThirdAppAdapter(Context context) {
        this.mContext = context;
    }

    public void setDataChangeInterFace(DataChangeInterFace dataChangeInterFace2) {
        this.dataChangeInterFace = dataChangeInterFace2;
    }

    public void registerObserver(IReaderObserver observer) {
        this.mObservable.registerObserver(observer);
    }

    public List<IReaderMockData> getMockSource() {
        return this.mMockSource;
    }

    public void setMockSource(List<IReaderMockData> mockSource) {
        this.mMockSource = mockSource;
        notifyDataSetChanged();
    }

    /* access modifiers changed from: protected */
    public void onDragStart(ViewHolder viewHolder, int parentIndex, int index) {
        Logger.d(TAG, "onDragStart");
        if (parentIndex >= 0) {
            this.isDragEnd = true;
            IReaderMockData mockData = index == -1 ? this.mMockSource.get(parentIndex) : ((IReaderMockDataGroup) this.mMockSource.get(parentIndex)).getChild(index);
            if (!this.mEditMode && mockData != null) {
                mockData.setChecked(true);
                this.mCheckedData.add(mockData);
                this.mObservable.notifyItemCheckChanged(true);
                if (TextUtils.isEmpty(mockData.getAppInfo().getAppName()) || mockData.getAppInfo().isSystemApp()) {
                    viewHolder.getBinding().iReaderFolderCheckBox.setVisibility(4);
                } else {
                    viewHolder.getBinding().iReaderFolderCheckBox.setVisibility(0);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDragAnimationEnd(ViewHolder viewHolder, int parentIndex, int index) {
        if (!this.mEditMode) {
            this.mObservable.onLongClick();
        } else {
            notifyDataSetChanged();
        }
    }

    /* access modifiers changed from: protected */
    public void onSubDialogShow(Dialog dialog, int parentPosition) {
        dialog.setOnDismissListener(this.mDismissListener);
        ViewGroup contentView = (ViewGroup) dialog.getWindow().findViewById(16908290);
        TextView selectAll = (TextView) contentView.findViewById(R.id.text_select_all);
        TextView title = (TextView) contentView.findViewById(R.id.text_title);
        EditText editText = (EditText) contentView.findViewById(R.id.edit_title);
        FrameLayout frameLayout = (FrameLayout) contentView.findViewById(R.id.sub_container);
        IReaderMockDataGroup mockDataGroup = (IReaderMockDataGroup) this.mMockSource.get(parentPosition);
        this.mSubObserver.setBindResource(mockDataGroup, selectAll, getMainAdapter(), getSubAdapter(), parentPosition);
        if (!this.mObservable.isRegister(this.mSubObserver)) {
            this.mObservable.registerObserver(this.mSubObserver);
        }
        selectAll.setVisibility(this.mEditMode ? this.mSubEditMode ? 8 : 0 : 8);
        title.setText(String.valueOf(mockDataGroup.getCategory()));
    }

    /* access modifiers changed from: protected */
    public void onSubDialogCancel(Dialog dialog, int parentPosition) {
        Logger.i(TAG, "onSubDialogCancel");
        super.onSubDialogCancel(dialog, parentPosition);
    }

    static class SubObserver extends IReaderObserver {
        View.OnClickListener allSelectListener = new View.OnClickListener() {
            public void onClick(View v) {
                int childCount = SubObserver.this.mGroup.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    IReaderMockData child = SubObserver.this.mGroup.getChild(i);
                    if (!child.isChecked()) {
                        child.setChecked(true);
                        SubObserver.this.mObservable.notifyItemCheckChanged(true);
                    }
                }
                SubObserver.this.mSubAdapter.notifyDataSetChanged();
                SubObserver.this.mMainAdapter.notifyItemChanged(SubObserver.this.parentPosition);
            }
        };
        View.OnClickListener cancelSelectListener = new View.OnClickListener() {
            public void onClick(View v) {
                int childCount = SubObserver.this.mGroup.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    IReaderMockData child = SubObserver.this.mGroup.getChild(i);
                    if (child.isChecked()) {
                        child.setChecked(false);
                        SubObserver.this.mObservable.notifyItemCheckChanged(false);
                    }
                }
                SubObserver.this.mSubAdapter.notifyDataSetChanged();
                SubObserver.this.mMainAdapter.notifyItemChanged(SubObserver.this.parentPosition);
            }
        };
        IReaderMockDataGroup mGroup;
        boolean mLastIsAllSelect;
        BaseMainAdapter mMainAdapter;
        final IReaderObservable mObservable;
        BaseSubAdapter mSubAdapter;
        int parentPosition;
        TextView selectAll;

        SubObserver(@NonNull IReaderObservable observable) {
            this.mObservable = observable;
        }

        /* access modifiers changed from: package-private */
        public void setBindResource(IReaderMockDataGroup source, TextView bindView, BaseMainAdapter mainAdapter, BaseSubAdapter subAdapter, int parentPosition2) {
            this.mGroup = source;
            this.selectAll = bindView;
            this.mSubAdapter = subAdapter;
            this.mMainAdapter = mainAdapter;
            this.parentPosition = parentPosition2;
            updateBind(true);
        }

        public void onChecked(boolean isChecked) {
            updateBind(false);
        }

        private void updateBind(boolean force) {
            boolean isAllSelect = this.mGroup.getChildCount() == this.mGroup.getCheckedCount();
            if (force) {
                updateBindInternal(isAllSelect);
            } else if (this.mLastIsAllSelect != isAllSelect) {
                updateBindInternal(isAllSelect);
            }
        }

        private void updateBindInternal(boolean isAllSelect) {
            this.mLastIsAllSelect = isAllSelect;
            this.selectAll.setText(isAllSelect ? "取消" : "全选");
            this.selectAll.setOnClickListener(isAllSelect ? this.cancelSelectListener : this.allSelectListener);
        }
    }

    public void appendItem(IReaderMockData mockData) {
        if (this.mMockSource == null) {
            this.mMockSource = new ArrayList();
        }
        int pos = this.mMockSource.size();
        this.mMockSource.add(mockData);
        notifyItemInsert(pos);
    }

    public void removeApp(AppInfo appInfo) {
        if (this.mMockSource != null) {
            int size = this.mMockSource.size();
            int i = 0;
            while (i < size) {
                AppInfo cur = this.mMockSource.get(i).getAppInfo();
                if (cur == null || !TextUtils.equals(appInfo.getPakageName(), cur.getPakageName())) {
                    i++;
                } else {
                    this.mMockSource.remove(i);
                    notifyDataSetChanged();
                    return;
                }
            }
        }
    }

    public void clearData() {
        if (this.mMockSource == null) {
            this.mMockSource = new ArrayList();
        } else {
            this.mMockSource.clear();
        }
    }

    public void setEditMode(boolean editMode) {
        this.mEditMode = editMode;
        if (!editMode) {
            if (this.mCheckedData.size() > 0) {
                for (IReaderMockData data : this.mCheckedData) {
                    data.setChecked(true);
                }
                this.mCheckedData.clear();
            }
            this.mObservable.notifyItemRestore();
        }
        notifyDataSetChanged();
        this.mObservable.notifyItemEditModeChanged(editMode);
        SharepreferenceUtil.getSharepferenceInstance(this.mContext).setAppEditAble(editMode);
    }

    public boolean isEditMode() {
        return this.mEditMode;
    }

    public List<IReaderMockDataGroup> getMockGroup() {
        if (this.mMockSource == null) {
            return null;
        }
        if (this.mLastMockGroup != null && !this.mMockSourceChanged) {
            return this.mLastMockGroup;
        }
        List<IReaderMockDataGroup> result = new ArrayList<>();
        for (IReaderMockData mockData : this.mMockSource) {
            if (mockData instanceof IReaderMockDataGroup) {
                result.add((IReaderMockDataGroup) mockData);
            }
        }
        this.mMockSourceChanged = false;
        this.mLastMockGroup = result;
        return result;
    }

    /* access modifiers changed from: protected */
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_i_reader_folder, parent, false));
    }

    public View getView(ViewGroup parent, View convertView, int mainPosition, int subPosition) {
        View result;
        if (convertView != null) {
            result = convertView;
        } else {
            result = new View(parent.getContext());
        }
        try {
            result.setBackground(((IReaderMockDataGroup) this.mMockSource.get(mainPosition)).getChild(subPosition).getAppInfo().getIcon());
        } catch (Exception e) {
            Log.e("icon-->", ":" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /* access modifiers changed from: protected */
    public int getItemCount() {
        if (this.mMockSource == null) {
            return 0;
        }
        return this.mMockSource.size();
    }

    /* access modifiers changed from: protected */
    public int getSubItemCount(int parentPosition) {
        if (parentPosition < this.mMockSource.size()) {
            IReaderMockData mockData = this.mMockSource.get(parentPosition);
            if (mockData instanceof IReaderMockDataGroup) {
                return ((IReaderMockDataGroup) mockData).getChildCount();
            }
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public IReaderMockDataGroup getSubSource(int parentPosition) {
        IReaderMockData mockData = this.mMockSource.get(parentPosition);
        if (mockData instanceof IReaderMockDataGroup) {
            return (IReaderMockDataGroup) mockData;
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public boolean canExplodeItem(int position, View pressedView) {
        if (position == -1) {
            return false;
        }
        return this.mMockSource.get(position) instanceof IReaderMockDataGroup;
    }

    /* access modifiers changed from: protected */
    public void onMove(int selectedPosition, int targetPosition) {
        Logger.d(TAG, "onMove: selectedPosition=" + selectedPosition + ", targetPosition=" + targetPosition);
        if (selectedPosition != targetPosition && selectedPosition >= 0 && targetPosition >= 0) {
            this.mMockSource.add(targetPosition, this.mMockSource.remove(selectedPosition));
            this.mMockSourceChanged = true;
            Log.e("sdjasdaslkjj", this.mMockSource.toString());
            this.dataChangeInterFace.setDataChange(this.mMockSource);
            this.mObservable.onPositionChanged(selectedPosition, targetPosition);
        }
    }

    /* access modifiers changed from: protected */
    public void onDragEnd_Main() {
        this.isDragEnd = false;
        notifyDataSetChanged();
    }

    public ArrayList<AppInfo> getAppInfoList() {
        ArrayList<AppInfo> appInfos = new ArrayList<>();
        for (int i = 0; i < this.mMockSource.size(); i++) {
            appInfos.add(getMockSource().get(i).getAppInfo());
        }
        return appInfos;
    }

    /* access modifiers changed from: protected */
    public void onSubMove(IReaderMockDataGroup iReaderMockDataGroup, int selectedPosition, int targetPosition) {
        iReaderMockDataGroup.addChild(targetPosition, iReaderMockDataGroup.removeChild(selectedPosition));
    }

    /* access modifiers changed from: protected */
    public boolean canMergeItem(int selectPosition, int targetPosition) {
        int size = ListUtils.size(this.mMockSource);
        if (selectPosition < 0 || selectPosition >= size || (this.mMockSource.get(selectPosition) instanceof IReaderMockDataGroup)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onMerged(int selectedPosition, int targetPosition) {
        IReaderMockData target = this.mMockSource.get(targetPosition);
        if (!TextUtils.isEmpty(target.getAppInfo().getAppName())) {
            IReaderMockData select = this.mMockSource.remove(selectedPosition);
            if (target instanceof IReaderMockDataGroup) {
                ((IReaderMockDataGroup) target).addChild(0, select);
            } else {
                IReaderMockDataGroup group = new IReaderMockDataGroup();
                group.addChild(select);
                group.addChild(target);
                group.setCategory(generateNewCategoryTag());
                int targetPosition2 = this.mMockSource.indexOf(target);
                this.mMockSource.remove(targetPosition2);
                this.mMockSource.add(targetPosition2, group);
            }
            this.mMockSourceChanged = true;
        }
    }

    private String generateNewCategoryTag() {
        List<IReaderMockDataGroup> mockDataGroups = getMockGroup();
        if (mockDataGroups.size() <= 0) {
            return "分类1";
        }
        int serialNumber = 1;
        int[] mHoldNumber = null;
        for (IReaderMockDataGroup temp : mockDataGroups) {
            if (temp.getCategory().startsWith("分类")) {
                String pendingStr = temp.getCategory().substring(2);
                if (!TextUtils.isEmpty(pendingStr) && TextUtils.isDigitsOnly(pendingStr)) {
                    try {
                        int serialCategory = Integer.parseInt(pendingStr);
                        if (mHoldNumber == null) {
                            mHoldNumber = new int[]{serialCategory};
                        } else {
                            mHoldNumber = Arrays.copyOf(mHoldNumber, mHoldNumber.length + 1);
                            mHoldNumber[mHoldNumber.length - 1] = serialCategory;
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }
        if (mHoldNumber != null) {
            Arrays.sort(mHoldNumber);
            for (int serial : mHoldNumber) {
                if (serial >= serialNumber) {
                    if (serial != serialNumber) {
                        break;
                    }
                    serialNumber++;
                }
            }
        }
        return "分类" + serialNumber;
    }

    /* access modifiers changed from: protected */
    public int onLeaveSubRegion(int parentPosition, IReaderMockDataGroup iReaderMockDataGroup, int selectedPosition) {
        if (this.mObservable.isRegister(this.mSubObserver)) {
            this.mObservable.unregisterObserver(this.mSubObserver);
        }
        this.mMockSource.add(0, iReaderMockDataGroup.removeChild(selectedPosition));
        if (iReaderMockDataGroup.getChildCount() == 0) {
            this.mMockSource.remove(this.mMockSource.indexOf(iReaderMockDataGroup));
        }
        this.mMockSourceChanged = true;
        return 0;
    }

    /* access modifiers changed from: protected */
    public void onBindMainViewHolder(ViewHolder holder, int position) {
        holder.bind(this.mMockSource.get(position), this.mEditMode);
    }

    public void setAppIcon(AppInfo appInfo, ImageView imageView, Drawable drawable) {
        Integer id = sPresentIcons.get(appInfo.getPakageName());
        if (id != null) {
            imageView.setImageResource(id.intValue());
        } else if (drawable != null) {
            imageView.setImageDrawable(drawable);
        } else {
            imageView.setImageDrawable((Drawable) null);
        }
    }

    /* access modifiers changed from: protected */
    public void onBindSubViewHolder(ViewHolder holder, int mainPosition, int subPosition) {
        holder.bind(((IReaderMockDataGroup) this.mMockSource.get(mainPosition)).getChild(subPosition), this.mEditMode);
    }

    /* access modifiers changed from: protected */
    public void onItemClick(ViewHolder viewHolder, int parentIndex, int index) {
        IReaderMockData mockData;
        AppInfo info;
        if (index == -1) {
            mockData = this.mMockSource.get(parentIndex);
        } else {
            mockData = ((IReaderMockDataGroup) this.mMockSource.get(parentIndex)).getChild(index);
        }
        if (!(mockData == null || (info = mockData.getAppInfo()) == null)) {
            String pkg = info.getPakageName();
            String name = info.getAppName();
            if (!TextUtils.isEmpty(pkg) && !TextUtils.isEmpty(name)) {
                StatsHelper.desktopClickedApp(pkg, name);
            }
        }
        if (!this.isCancelEdite) {
            this.isCancelEdite = true;
        } else if (this.mEditMode) {
            setEditMode(false);
        } else {
            AppInfo appInfo = mockData.getAppInfo();
            String packageName = appInfo.getPakageName();
            if (TextUtils.isEmpty(packageName)) {
                if (this.mEditMode) {
                    setEditMode(false);
                }
            } else if (appInfo.isForbbiden()) {
                new ShowAppForbiddenNoticeWin.Builder(this.mContext, new PadInfoUtil(this.mContext).getSnCode(), appInfo.getAppStatus()).build().showPopWin(this.mContext);
            } else {
                Log.e("itemclick-->", "点击了APP1：" + packageName);
                Log.e("packageName-->", packageName);
                if (NEED_LOGIN_PACKAGES_LIST.contains(packageName) && !GlobalVariable.isLogin()) {
                    DialogUtil.showDialogToLogin(this.mContext);
                } else if (TextUtils.equals(packageName, "com.iflytek.wrongnotebook")) {
                    OpenWrongNoteBookHandler.openWrongNoteBook(this.mContext);
                } else if (EtsConstant.ETS_PHONETIC_VIRTUAL_PACKAGE.equals(packageName)) {
                    EtsUtils.launchEts(this.mContext, "com.ets100.study.ACTION_SOUND_MARK_STUDY", SharepreferenceUtil.getSharepferenceInstance(this.mContext).getUserInfo());
                } else if (!AppContants.RECITE_BOOK.equals(packageName)) {
                    try {
                        Intent intent = this.mContext.getPackageManager().getLaunchIntentForPackage(packageName);
                        if (intent != null) {
                            int versionCode = appInfo.mVersionCode;
                            intent.addFlags(268435456);
                            intent.putExtra("token", SharepreferenceUtil.getToken());
                            UpdateUtil.checkUpdateAndStart(this.mContext, packageName, versionCode, intent);
                        }
                    } catch (Throwable exp) {
                        Logger.e(TAG, "getLaunchIntentForPackage error", exp);
                    }
                } else if (!NoDoubleClickUtils.isDoubleClick()) {
                    if (isAvilible(this.mContext, AppContants.RECITE_BOOK)) {
                        int versionCode2 = appInfo.mVersionCode;
                        Intent intent2 = new Intent();
                        intent2.addFlags(268435456);
                        intent2.putExtra("token", "Bearer " + SharepreferenceUtil.getToken());
                        intent2.setComponent(new ComponentName(AppContants.RECITE_BOOK, AppContants.RECITE_BOOK_ACTIVITY));
                        UpdateUtil.checkUpdateAndStart(this.mContext, packageName, versionCode2, intent2);
                        return;
                    }
                    Toast.makeText(this.mContext, "请检查是否安装了背课文应用", 0).show();
                }
            }
        }
    }

    class ViewHolder extends PrimitiveSimpleAdapter.ViewHolder {
        int count = 0;
        private ItemIReaderFolderBinding mBinding;

        ViewHolder(View itemView) {
            super(itemView);
            this.mBinding = ItemIReaderFolderBinding.bind(itemView);
        }

        /* access modifiers changed from: package-private */
        public ItemIReaderFolderBinding getBinding() {
            return this.mBinding;
        }

        /* access modifiers changed from: package-private */
        public void bind(final IReaderMockData iReaderMockData, boolean inEditMode) {
            if (!inEditMode) {
                this.mBinding.iReaderFolder.clearAnimation();
                this.mBinding.iReaderFolderBg.setVisibility(8);
                this.mBinding.iReaderFolderCheckBox.setVisibility(4);
                this.mBinding.iReaderFolderGrid.setVisibility(4);
            } else if (iReaderMockData instanceof IReaderMockDataGroup) {
                Log.i("CLASSIFY_VIEW_INIT", "IReaderMockDataGroup");
            } else {
                this.mBinding.iReaderFolderBg.setVisibility(8);
                String appName = iReaderMockData.getAppInfo().getAppName();
                if (!TextUtils.isEmpty(appName) && !iReaderMockData.getAppInfo().isSystemApp()) {
                    this.mBinding.ivAppicon.setVisibility(0);
                    this.mBinding.ivAppicon.setImageDrawable(iReaderMockData.getAppInfo().getIcon());
                    this.mBinding.iReaderFolderCheckBox.setVisibility(0);
                    this.mBinding.iReaderFolderCheckBox.setBackground(this.itemView.getContext().getResources().getDrawable(R.drawable.icon_delete_x));
                } else if (TextUtils.isEmpty(appName) || !iReaderMockData.getAppInfo().isSystemApp()) {
                    this.mBinding.ivAppicon.setVisibility(8);
                    this.mBinding.iReaderFolderCheckBox.setVisibility(4);
                } else {
                    this.mBinding.ivAppicon.setVisibility(0);
                    this.mBinding.iReaderFolderCheckBox.setVisibility(4);
                }
                this.mBinding.iReaderFolderCheckBox.setText("");
            }
            if (iReaderMockData instanceof IReaderMockDataGroup) {
                this.mBinding.ivAppicon.setVisibility(8);
                this.mBinding.iReaderFolderGrid.setVisibility(0);
                this.mBinding.iReaderFolderTag.setVisibility(0);
                this.mBinding.iReaderFolderTag.setText(((IReaderMockDataGroup) iReaderMockData).getCategory());
                this.mBinding.iReaderFolderContent.setVisibility(8);
                this.mBinding.iReaderFolderBg.setVisibility(0);
            } else {
                if (TextUtils.isEmpty(iReaderMockData.getAppInfo().getAppName())) {
                    this.mBinding.ivAppicon.setVisibility(8);
                } else {
                    this.mBinding.ivAppicon.setVisibility(0);
                    if (iReaderMockData.getAppInfo().isForbbiden()) {
                        this.mBinding.ivAppiconForbidden.setVisibility(0);
                    } else {
                        this.mBinding.ivAppiconForbidden.setVisibility(8);
                    }
                    ThirdAppAdapter.this.setAppIcon(iReaderMockData.getAppInfo(), this.mBinding.ivAppicon, iReaderMockData.getAppInfo().getIcon());
                }
                this.mBinding.iReaderFolderGrid.setVisibility(4);
                this.mBinding.iReaderFolderTag.setVisibility(0);
                this.mBinding.iReaderFolderTag.setText(iReaderMockData.getAppInfo().getAppName());
                this.mBinding.iReaderFolderContent.setBackgroundColor(iReaderMockData.getColor());
                this.mBinding.iReaderFolderContent.setVisibility(0);
                this.mBinding.iReaderFolderBg.setVisibility(8);
            }
            this.mBinding.iReaderFolderCheckBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    final String packageName = iReaderMockData.getAppInfo().getPakageName();
                    if (TextUtils.isEmpty(packageName)) {
                        return;
                    }
                    if (AppContants.RECITE_BOOK.equals(packageName)) {
                        ThirdAppAdapter.this.isCancelEdite = false;
                        if (ThirdAppAdapter.this.mContext instanceof MainActivity) {
                            new AlertDialog.Builder(ThirdAppAdapter.this.mContext).setTitle("要卸载此应用吗？").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    SharepreferenceUtil.getSharepferenceInstance(ThirdAppAdapter.this.mContext).setReciteBookUninstalled(true);
                                    ((MainActivity) ThirdAppAdapter.this.mContext).reMoveAppFromList(packageName);
                                }
                            }).create().show();
                            return;
                        }
                        return;
                    }
                    ThirdAppAdapter.this.isCancelEdite = false;
                    ThirdAppAdapter.this.mContext.startActivity(new Intent("android.intent.action.DELETE", Uri.fromParts("package", packageName, (String) null)));
                }
            });
            this.mBinding.ivAppicon.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                }
            });
        }
    }

    static class IReaderObservable extends Observable<IReaderObserver> {
        IReaderObservable() {
        }

        public boolean isRegister(IReaderObserver observer) {
            return this.mObservers.contains(observer);
        }

        public void notifyItemCheckChanged(boolean isChecked) {
            for (int i = this.mObservers.size() - 1; i >= 0; i--) {
                ((IReaderObserver) this.mObservers.get(i)).onChecked(isChecked);
            }
        }

        public void notifyItemEditModeChanged(boolean editMode) {
            for (int i = this.mObservers.size() - 1; i >= 0; i--) {
                ((IReaderObserver) this.mObservers.get(i)).onEditChanged(editMode);
            }
        }

        public void notifyItemRestore() {
            for (int i = this.mObservers.size() - 1; i >= 0; i--) {
                ((IReaderObserver) this.mObservers.get(i)).onRestore();
            }
        }

        public void notifyItemHideSubDialog() {
            for (int i = this.mObservers.size() - 1; i >= 0; i--) {
                ((IReaderObserver) this.mObservers.get(i)).onHideSubDialog();
            }
        }

        public void onLongClick() {
            for (int i = this.mObservers.size() - 1; i >= 0; i--) {
                ((IReaderObserver) this.mObservers.get(i)).onLongClick();
            }
        }

        public void onPositionChanged(int srcPos, int targetPos) {
            for (int i = this.mObservers.size() - 1; i >= 0; i--) {
                ((IReaderObserver) this.mObservers.get(i)).onPositionChanged(srcPos, targetPos);
            }
        }
    }

    public static abstract class IReaderObserver {
        public void onChecked(boolean isChecked) {
        }

        public void onEditChanged(boolean inEdit) {
        }

        public void onRestore() {
        }

        public void onHideSubDialog() {
        }

        public void onLongClick() {
        }

        public void onPositionChanged(int srcPos, int targetPos) {
        }
    }

    /* access modifiers changed from: protected */
    public boolean isAvilible(Context context, String packageName) {
        List<PackageInfo> pinfo = context.getPackageManager().getInstalledPackages(0);
        List<String> pName = new ArrayList<>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                pName.add(pinfo.get(i).packageName);
            }
        }
        return pName.contains(packageName);
    }
}
