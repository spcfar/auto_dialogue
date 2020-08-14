package com.hnsh.dialogue.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.dosmono.logger.Logger;
import com.dosmono.universal.entity.shortcut.Item;
import com.google.gson.Gson;
import com.hnsh.dialogue.R;
import com.hnsh.dialogue.adapter.QuickwordContentAdapter;
import com.hnsh.dialogue.adapter.UnfCommonPhraseCategoryAdapter;
import com.hnsh.dialogue.bean.QuickwordCategoryBean;
import com.hnsh.dialogue.bean.QuickwordContentBean;
import com.hnsh.dialogue.bean.TemplateInfo;
import com.hnsh.dialogue.constants.BIZConstants;
import com.hnsh.dialogue.constants.TSRConstants;
import com.hnsh.dialogue.mvp.contracts.IUnfQuickWordContract;
import com.hnsh.dialogue.mvp.presenters.UnfCommonPhrasePresenter;
import com.hnsh.dialogue.utils.DrawableUtil;
import com.hnsh.dialogue.utils.EmptyUtils;
import com.hnsh.dialogue.utils.EventBusHelper;
import com.hnsh.dialogue.utils.PrefsUtils;
import com.hnsh.dialogue.utils.StatusBarCompat;
import com.hnsh.dialogue.utils.UIUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class UnfCommonPhraseActivity extends HeaderActivity<UnfCommonPhrasePresenter> implements View.OnClickListener, IUnfQuickWordContract.IUnfQuickWordView {
    private static final int REQUEST_CODE_SEARCH_PHRASE = 0x10;
    private RecyclerView mRecyclerView_category;
    private RecyclerView mRecyclerView_content;

    private View mllCategory;


    private UnfCommonPhraseCategoryAdapter mCategoryAdapter;
    private QuickwordContentAdapter mContentAdapter;
    private List<QuickwordCategoryBean> mCategoryDatas;
    private List<QuickwordContentBean> mContentList;
    private TextView mTvCancel;
    private View mLlCancel;
    private String mPhraseType;

    @Override
    public int initContextView(Bundle savedInstanceState) {
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.state_bar),0);
        return R.layout.activity_unf_common_phrase;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mllCategory = findViewById(R.id.ll_common_phrase_category);
        mRecyclerView_category = findViewById(R.id.rc_common_phrase_category);
        mRecyclerView_content = findViewById(R.id.rl_common_phrase_content);

        mTvCancel = findViewById(R.id.tv_shortcut_cancel);
        mLlCancel = findViewById(R.id.ll_shortcut_cancel);

        mLlCancel.setOnClickListener(this);
        mCategoryDatas = new ArrayList<>();
        mCategoryAdapter = new UnfCommonPhraseCategoryAdapter(this, mCategoryDatas);
        mContentList = new ArrayList();

        mContentAdapter = new QuickwordContentAdapter(this, mContentList);

        mRecyclerView_category.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView_category.setAdapter(mCategoryAdapter);
        mPresenter = new UnfCommonPhrasePresenter(this);

        mRecyclerView_content.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView_content.setAdapter(mContentAdapter);

        mPhraseType = getIntent().getStringExtra(TSRConstants.KEY_DATA);

        mCategoryAdapter.setOnItemClickListener(position -> {
            Logger.d("position = " + position);
            if (mCategoryAdapter.select(position)) {
                QuickwordCategoryBean typeInfoBean = mCategoryAdapter.getBean(position);
                if (typeInfoBean == null) {
                    Logger.d("点击获取的对象为空");
                }
                long categoryId = typeInfoBean.getCategoryId();
                //TODO
                loadContent(categoryId);
            }
        });
        mContentAdapter.setOnItemClickCallback(position -> {
            QuickwordContentBean contentBean = mContentList.get(position);
            Intent extraIntent = new Intent();
            extraIntent.putExtra(TSRConstants.KEY_DATA, contentBean);
            setResult(Activity.RESULT_OK, extraIntent);
            finish();
        });

        String json= PrefsUtils.getPrefs( UIUtils.getContext(), BIZConstants.SP.SP_TEMPLATE_THEME_INFO, "");
        StatusBarCompat.compat(this, getResources().getColor(R.color.broadside_bar_color));
        if(!EmptyUtils.isEmpty(json)) {
            TemplateInfo.ThemeInfo themeInfo = new Gson().fromJson(json, TemplateInfo.ThemeInfo.class);
            onEventTheme(themeInfo);
        }


        mllCategory.setBackgroundResource(R.mipmap.ic_main_common_phrase);

    }

    /**
     * 界面加载常用问答，快捷用语内容
     *
     * @param categoryId
     */

    public void loadContent(long categoryId) {

        switch (mPhraseType) {
            case TSRConstants.COMMON_SHORTCUT:

                mPresenter.loadPhraseData(categoryId);
                break;
            case TSRConstants.SPECIAL_SHORTCUT:

                mPresenter.loadQAData(categoryId, 0);
                break;
        }

    }

    /**
     * 加载快捷用语或常用问答的内容
     * 快捷用语和常用问答统一处理
     *
     * @param mDatas
     */
    @Override
    public void loadContentDatas(List<QuickwordContentBean> mDatas) {

        if (mContentList == null) {
            mContentList = new ArrayList<>(mDatas);
            mContentAdapter = new QuickwordContentAdapter(this, mContentList);
            mRecyclerView_content.setAdapter(mContentAdapter);
            return;
        } else {
            mContentList.clear();
            mContentList.addAll(mDatas);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mContentAdapter.notifyDataSetChanged();
            }
        });
    }


    /**
     * 加载分类信息(快捷用语和常用问答 统一处理)
     *
     * @param mDatas
     */
    @Override
    public void loadCategoryDatas(List<QuickwordCategoryBean> mDatas) {
        if (mCategoryDatas == null) {
            mCategoryDatas = new ArrayList<>(mDatas);
            mCategoryAdapter = new UnfCommonPhraseCategoryAdapter(this, mCategoryDatas);
            mRecyclerView_category.setAdapter(mCategoryAdapter);

            return;
        } else {
            mDatas.get(0).setSelected(true);
            mCategoryDatas.clear();
            mCategoryDatas.addAll(mDatas);
        }
        runOnUiThread(() -> mCategoryAdapter.notifyDataSetChanged());
    }

    /**
     * 判断 是快捷用语 还是常用问答
     *
     * @return 0 为快捷用语 1 为常用问答
     */
    @Override
    public boolean isPhraseOrQAMode() {
//        return mPhraseType.equals("0");
        return mPhraseType.equals("0");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if ( id == R.id.ll_shortcut_cancel) {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SEARCH_PHRASE) {
                if (null != data) {
                    Item item = data.getParcelableExtra(TSRConstants.KEY_DATA);
                    Intent extraIntent = new Intent();
                    extraIntent.putExtra(TSRConstants.KEY_DATA, item);
                    setResult(Activity.RESULT_OK, extraIntent);
                    finish();
                }
            }
        }
    }

    @Override
    public Context applicationContext() {
        return getApplicationContext();
    }


    @Override
    protected void onStart() {
        super.onStart();
        EventBusHelper.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusHelper.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventTheme(TemplateInfo.ThemeInfo themeInfo){
        if(themeInfo==null)return;
        mRecyclerView_category.setBackground(DrawableUtil.getGradualChangeDrawable(GradientDrawable.Orientation.LEFT_RIGHT,themeInfo.getTopColors()));
        mCategoryAdapter.setSelectColor( DrawableUtil.colorDeep(themeInfo.getTopStartColorInt()));
        mllCategory.setBackgroundColor(Color.TRANSPARENT);
    }


}
