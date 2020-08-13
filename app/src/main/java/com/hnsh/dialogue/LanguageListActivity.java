package com.hnsh.dialogue;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dosmono.logger.Logger;
import com.dosmono.universal.entity.flag.Flag;
import com.dosmono.universal.entity.language.Language;
import com.dosmono.universal.utils.ConfigUtils;
import com.hnsh.dialogue.adapter.LanguageAdapter;
import com.hnsh.dialogue.bean.LanguageInfo;
import com.hnsh.dialogue.utils.AppScreenMgr;
import com.hnsh.dialogue.utils.GlideUtil;
import com.hnsh.dialogue.views.MyItemClickListener;

import java.util.ArrayList;
import java.util.List;

import static android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED;
import static android.support.design.widget.BottomSheetBehavior.STATE_HIDDEN;

public class LanguageListActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String FROM_LANG = "fromLang";
    public static final String TO_LANG = "toLang";


    public static void toActivity(Activity activity,int fromLang,int toLang){
        Intent intent = new Intent(activity,LanguageListActivity.class);
        intent.putExtra(FROM_LANG,fromLang);
        intent.putExtra(TO_LANG,toLang);
        activity.startActivityForResult(intent,110);
    }


    private TextView mTextConfirm;
    private RecyclerView mRecyclerLanguage;
    private LinearLayout ll_bottom_sheet;
    private View mViewBg;
    private LinearLayout mLLLeftLang;
    private ImageView mIvLeftFlag;
    private TextView mTextLeftLangName;

    private LinearLayout mLLRightLang;
    private ImageView mIvRightFlag;
    private TextView mTextRightLangName;

    private List<LanguageInfo> mList = new ArrayList<>();

    private LanguageAdapter mAdapter;
    private int mLangType =0;

    private int mFromLang = 0;
    private int mToLang =3;
    private BottomSheetBehavior<View> behavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_langage_list);



        initView();
        initData();
        initListener();
    }



    private void initView() {
        mTextConfirm =findViewById(R.id.tv_confirm);
        mRecyclerLanguage = findViewById(R.id.rv_language);
        ll_bottom_sheet = findViewById(R.id.ll_bottom_sheet);
        behavior = BottomSheetBehavior.from(ll_bottom_sheet);
        mViewBg = findViewById(R.id.view_main_bg);

        mLLLeftLang = findViewById(R.id.ll_left_lang);
        mIvLeftFlag = findViewById(R.id.iv_left_lang_flag);
        mTextLeftLangName = findViewById(R.id.tv_left_lang_name);

        mLLRightLang = findViewById(R.id.ll_right_lang);
        mIvRightFlag = findViewById(R.id.iv_right_lang_flag);
        mTextRightLangName = findViewById(R.id.tv_right_lang_name);



        behavior.setPeekHeight(AppScreenMgr.getScreenHeight(this));
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, @BottomSheetBehavior.State int newState) {
//                        state = "STATE_DRAGGING";//过渡状态此时用户正在向上或者向下拖动bottom sheet
//                        state = "STATE_SETTLING"; // 视图从脱离手指自由滑动到最终停下的这一小段时间
//                        state = "STATE_EXPANDED"; //处于完全展开的状态
//                        state = "STATE_COLLAPSED"; //默认的折叠状态
//                        state = "STATE_HIDDEN"; //下滑动完全隐藏 bottom sheet
                Logger.d("newState:" + newState);
                if(newState == STATE_HIDDEN){
                    backClick();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                try {
                    Logger.d(Float.isNaN(slideOffset)+"BottomSheetDemoslideOffset:" + slideOffset);
                    if(slideOffset<0){
                        int alpha = (int) (slideOffset * 255 *-1);
                        mViewBg.getBackground().setAlpha(255-alpha);
                    }else {
                        mViewBg.getBackground().setAlpha(255);
                    }

                }catch (Exception e){
                    Logger.e(e,"e");
                }


            }
        });
        mViewBg.setBackgroundColor(ContextCompat.getColor(this,R.color.selector_ripple_trans));
        mViewBg.getBackground().setAlpha(255);
        behavior.setState(STATE_COLLAPSED);

    }

    private void initData() {
        mFromLang = getIntent().getIntExtra(FROM_LANG,0);
        mToLang = getIntent().getIntExtra(TO_LANG,3);

        List<Language> list = ConfigUtils.INSTANCE.getLanguageList(this);

        if(list!=null){
            LanguageInfo languageInfo= null;
            Log.e("huanghuang"," 语种数量："+list.size());
            for (int i = 0; i < list.size(); i++) {

//                if(isDels(list.get(i).getId())){
//                    continue;
//                }
                String name= list.get(i).getSubname();
                int index = name.indexOf("(");
                if(index>0){
                    name = name.substring(0,index);
                }
                languageInfo = new LanguageInfo();
                languageInfo.id = list.get(i).getId();
                languageInfo.name = list.get(i).getName();
                languageInfo.subname_zh = name ;
                Flag flag = ConfigUtils.INSTANCE.getFlag(LanguageListActivity.this,list.get(i).getId());
                if(flag!=null){
                    languageInfo.flag =flag.getFlag();
                }else {
                    Logger.e("无国旗："+list.get(i));
                }

                mList.add(languageInfo);

                Log.d("huanghuang","languageInfo："+languageInfo);
            }
        }
        Log.d("huanghuang",mList+"");

        mAdapter = new LanguageAdapter(this,mList);
        mRecyclerLanguage.setLayoutManager(new GridLayoutManager(this,4));
        mRecyclerLanguage.setNestedScrollingEnabled(false);//禁止滑动
        mRecyclerLanguage.setAdapter(mAdapter);




        Flag fromFlag = ConfigUtils.INSTANCE.getFlag(LanguageListActivity.this,mFromLang);
        Flag toFlag = ConfigUtils.INSTANCE.getFlag(LanguageListActivity.this,mToLang);

        Logger.d("huang fromFlag:"+fromFlag);



        mTextLeftLangName.setText(fromFlag.getSubname_zh());
        mTextRightLangName.setText(toFlag.getSubname_zh());

        GlideUtil.loadImg(this,fromFlag.getFlag(),mIvLeftFlag);
        GlideUtil.loadImg(this,toFlag.getFlag(),mIvRightFlag);




    }

    private boolean isDels(int langid){

        if(langid>=1 && langid<8){
            return true;
        }
        if(langid==86 || langid==28|| langid==28){
            return true;
        }
        return false;

    }
    private void initListener() {
        mTextConfirm.setOnClickListener(this);
        mLLLeftLang.setOnClickListener(this);
        mLLRightLang.setOnClickListener(this);

        mAdapter.setOnItemClickListener(new MyItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {


                if(mLangType==0){
                    mFromLang = mList.get(position).id;
                    GlideUtil.loadImg(LanguageListActivity.this,mList.get(position).flag,mIvLeftFlag);
                    mTextLeftLangName.setText( mList.get(position).subname_zh);
                }else {
                    mToLang = mList.get(position).id;
                    GlideUtil.loadImg(LanguageListActivity.this,mList.get(position).flag,mIvRightFlag);
                    mTextRightLangName.setText( mList.get(position).subname_zh);
                }

            }
        });


    }


    private void selectedLang(){
        Intent intent = new Intent();
        intent.putExtra(FROM_LANG,mFromLang);
        intent.putExtra(TO_LANG,mToLang);
        setResult(Activity.RESULT_OK,intent);
        behavior.setState(STATE_HIDDEN);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.tv_confirm:
                selectedLang();
                break;

            case R.id.ll_left_lang:
                mLangType = 0;
                mLLRightLang.setBackground(new ColorDrawable(Color.TRANSPARENT));
                mLLLeftLang.setBackgroundResource(R.drawable.language_title_left_bg);

                textBold();

                break;


            case R.id.ll_right_lang:
                mLangType = 1;
                mLLLeftLang.setBackground(new ColorDrawable(Color.TRANSPARENT));
                mLLRightLang.setBackgroundResource(R.drawable.language_title_right_bg);

                textBold();
                break;

        }
    }

    private void textBold(){
        if(mLangType==0){
            mTextLeftLangName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            mTextRightLangName.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        }else {
            mTextLeftLangName.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            mTextRightLangName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }

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
