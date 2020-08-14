package com.hnsh.dialogue.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.dosmono.logger.Logger;
import com.dosmono.universal.entity.flag.Flag;
import com.dosmono.universal.entity.language.Language;
import com.dosmono.universal.utils.ConfigUtils;
import com.hnsh.dialogue.LanguageListActivity;
import com.hnsh.dialogue.R;
import com.hnsh.dialogue.adapter.LanguageAdapter;
import com.hnsh.dialogue.bean.LanguageInfo;
import com.hnsh.dialogue.utils.AppScreenMgr;
import com.hnsh.dialogue.utils.GlideUtil;
import com.hnsh.dialogue.views.MyItemClickListener;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import static android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED;
import static android.support.design.widget.BottomSheetBehavior.STATE_HIDDEN;

public class SelectorLanguageActivity extends AppCompatActivity {
    public static final String LANGUAGE = "language";
    public static final String DIRECTION = "direction";
    public static final String COUNTRY_NAME = "country_name";
    private RecyclerView mRecyclerLanguage;
    private RelativeLayout rlComeBack;
    private List<LanguageInfo> mList = new ArrayList<>();
    private LanguageAdapter mAdapter;
    private int direction = 0;    //0:左边；1：右边
    private int language = 0;
    private String countryName = "";
    private static final int COLUMN_SIZE = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucentForImageView(this, 0, null);
        setContentView(R.layout.activity_selector_language);
        initView();
        initData();
        initListener();
    }


    private void initView() {
        mRecyclerLanguage = findViewById(R.id.rv_language);
        rlComeBack = findViewById(R.id.rl_come_back);
        rlComeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initData() {
        language = getIntent().getIntExtra(LANGUAGE, 0);
        direction = getIntent().getIntExtra(DIRECTION, 0);
        List<Language> list = ConfigUtils.INSTANCE.getLanguageList(this);
        if (list != null) {
            LanguageInfo languageInfo = null;
            Log.e("huanghuang", " 语种数量：" + list.size());
            for (int i = 0; i < list.size(); i++) {
                String name = list.get(i).getSubname();
                int index = name.indexOf("(");
                if (index > 0) {
                    name = name.substring(0, index);
                }
                languageInfo = new LanguageInfo();
                languageInfo.id = list.get(i).getId();
                languageInfo.name = list.get(i).getName();
                languageInfo.subname_zh = name;
                Flag flag = ConfigUtils.INSTANCE.getFlag(this, list.get(i).getId());
                if (flag != null) {
                    languageInfo.flag = flag.getFlag();
                } else {
                    Logger.e("无国旗：" + list.get(i));
                }
                mList.add(languageInfo);
                Log.d("huanghuang", "languageInfo：" + languageInfo);
                Log.d("huanghuang", "i:" + i + "languageInfo.name：" + languageInfo.name);
                Log.d("huanghuang", "i:" + i + "languageInfo.subname_zh：" + languageInfo.subname_zh);
            }
        }
        Log.d("huanghuang", mList + "");
        mAdapter = new LanguageAdapter(this, mList);
        mRecyclerLanguage.setLayoutManager(new GridLayoutManager(this, COLUMN_SIZE));
        mRecyclerLanguage.setNestedScrollingEnabled(false);//禁止滑动
        mRecyclerLanguage.setAdapter(mAdapter);
    }

    private void initListener() {
        mAdapter.setOnItemClickListener(new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                language = mList.get(position).id;
                countryName = mList.get(position).name;
                selectedLang();
            }
        });
    }


    private void selectedLang() {
        Intent intent = new Intent();
        intent.putExtra(LANGUAGE, language);
        intent.putExtra(DIRECTION, direction);
        intent.putExtra(COUNTRY_NAME, countryName);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }


    /**
     * 点击系统返回和页面返回键时的处理
     */
    private void backClick() {
        finish();
        // 定义出入场动画
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        backClick();
        super.onBackPressed();
    }
}
