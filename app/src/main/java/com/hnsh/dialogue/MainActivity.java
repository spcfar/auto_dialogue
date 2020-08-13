package com.hnsh.dialogue;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dosmono.logger.Logger;
import com.dosmono.model.ai.recognizer.RecognizerReply;
import com.dosmono.model.ai.translate.TranslateReply;
import com.dosmono.universal.utils.ConfigUtils;
import com.hnsh.dialogue.adapter.ChatMessageAdapter;
import com.hnsh.dialogue.app.AppConstant;
import com.hnsh.dialogue.bean.ChatMessage;
import com.hnsh.dialogue.recognizer.RecognizerImpl;
import com.hnsh.dialogue.recognizer.SynthesisImpl;
import com.hnsh.dialogue.recognizer.TranslateImpl;
import com.hnsh.dialogue.ui.SelectorLanguageActivity;
import com.hnsh.dialogue.utils.SharedPreUtils;
import com.hnsh.dialogue.views.GuideView;
import com.hnsh.dialogue.views.WaveViewWithImg;

import java.util.ArrayList;
import java.util.List;

import static com.hnsh.dialogue.ui.SelectorLanguageActivity.COUNTRY_NAME;
import static com.hnsh.dialogue.ui.SelectorLanguageActivity.DIRECTION;
import static com.hnsh.dialogue.ui.SelectorLanguageActivity.LANGUAGE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mRvChat;
    private WaveViewWithImg mWvLeftLang;
    private WaveViewWithImg mWvRightLang;
    private ImageView mIvLeftLang;
    private ImageView mIvRightLang;
    private RelativeLayout mRlLeftLang;
    private RelativeLayout mRlRightLang;
    private TextView mTvLeftCountryName;
    private TextView mTvRightCountryName;
    private RelativeLayout mRlComeBack;  //导航栏返回按钮
    private ChatMessageAdapter messageAdapter;
    private SynthesisImpl mSynthesis;
    private RecognizerImpl mRecognizer;
    private TranslateImpl mTranslate;
    private TranslateImpl mTranslateBySayTips;
    private TranslateImpl mTranslateBySayTipsByRight;
    private TextView tv_usually_ask_answer;

    private int mFromLang;
    private int mToLang;

    private String mFromCountryName;
    private String mToCountryName;

    private static final int START_ACTIVITY_REQUEST_CODE = 0;
    private boolean leftWaveRecording = false;
    private boolean rightWaveRecording = false;

    private GuideView mGvLeftShadow;  //左边录音时的遮罩view
    private GuideView mGvRightShadow;  //右边录音时的遮罩view

    private TextView tvLeftRecordTips;
    private TextView tvRightRecordTips;
    private String leftRecordTips;
    private String rightRecordTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        setGuideView();
        initListener();
        initRecognizer();
    }


    private void initView() {
        mRvChat = findViewById(R.id.rv_chat);
        mIvLeftLang = findViewById(R.id.iv_left_lang);
        mIvRightLang = findViewById(R.id.iv_right_lang);
        mTvLeftCountryName = findViewById(R.id.tv_left_country_name);
        mTvRightCountryName = findViewById(R.id.tv_right_country_name);
        mWvLeftLang = findViewById(R.id.wv_left_lang);
        mWvRightLang = findViewById(R.id.wv_right_lang);
        mRlComeBack = findViewById(R.id.rl_come_back);
        mRlLeftLang = findViewById(R.id.rl_left_lang);
        mRlRightLang = findViewById(R.id.rl_right_lang);
        tv_usually_ask_answer = findViewById(R.id.tv_usually_ask_answer);
        mWvLeftLang.setInitialRadius(64f);
        mWvLeftLang.setMaxRadius(106f);
        mWvRightLang.setInitialRadius(64f);
        mWvRightLang.setMaxRadius(106f);
    }

    private List<ChatMessage> mList = new ArrayList<>();

    private void initData() {

        mFromLang = SharedPreUtils.getInt(MainActivity.this, AppConstant.FORM_LANG, 0);
        mToLang = SharedPreUtils.getInt(MainActivity.this, AppConstant.TO_LANG, 4);
        mFromCountryName = SharedPreUtils.getString(MainActivity.this, AppConstant.FORM_COUNTRY_NAME, getString(R.string.tv_left_normal));
        mToCountryName = SharedPreUtils.getString(MainActivity.this, AppConstant.TO_COUNTRY_NAME, getString(R.string.tv_right_normal));

        String leftFlag = ConfigUtils.INSTANCE.getFlag(MainActivity.this, mFromLang).getFlag();
        String rightFlag = ConfigUtils.INSTANCE.getFlag(MainActivity.this, mToLang).getFlag();
        setFlagAndWaveColor(leftFlag, rightFlag, mFromCountryName, mToCountryName);

        messageAdapter = new ChatMessageAdapter(this, mList);
        mRvChat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRvChat.setAdapter(messageAdapter);

        leftRecordTips = SharedPreUtils.getString(MainActivity.this, AppConstant.FORM_LISTENING, getString(R.string.i_have_been_listening));
        rightRecordTips = SharedPreUtils.getString(MainActivity.this, AppConstant.TO_LISTENING, getString(R.string.i_have_been_listening_right));
    }


    private void setFlagAndWaveColor(String leftFlag, String rightFlag, String leftCountryName, String rightCountryName) {
        try {
            if (!leftFlag.isEmpty()) {
                mTvLeftCountryName.setText(leftCountryName);
                Glide.with(this)
                        .asBitmap()
                        .load(leftFlag)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                mIvLeftLang.setImageBitmap(resource);
                            }
                        });
            }

            if (!rightFlag.isEmpty()) {
                mTvRightCountryName.setText(rightCountryName);
                Glide.with(this)
                        .asBitmap()
                        .load(rightFlag)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                mIvRightLang.setImageBitmap(resource);
                            }
                        });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void initListener() {
        messageAdapter.setOnPlayListener(position -> {
            stopPlayAll();
            mList.get(position).inPlay = true;
            messageAdapter.notifyDataSetChanged();
            int langId = mList.get(position).msgType == 0 ? mToLang : mFromLang;
            Logger.d("langId：" + langId);
            mSynthesis.start(mList.get(position).translateMsg, langId);
        });
        mRlLeftLang.setOnClickListener(this);
        mRlRightLang.setOnClickListener(this);
        mWvLeftLang.setOnClickListener(this);
        mWvRightLang.setOnClickListener(this);
        mRlComeBack.setOnClickListener(this);
        tv_usually_ask_answer.setOnClickListener(this);
    }

    private void stopPlayAll() {
        try {
            for (int i = 0; i < mList.size(); i++) {
                mList.get(i).inPlay = false;
            }
            mSynthesis.stop();
            messageAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.d("关闭语音出错啦：" + e.getMessage());
        }

    }


    private ChatMessage mChatMessage;

    private void initRecognizer() {
        mRecognizer = new RecognizerImpl(this) {
            @Override
            public void onRecognizerResult(RecognizerReply result) {
                if (result != null && !TextUtils.isEmpty(result.getText())) {
                    mChatMessage = new ChatMessage();
                    mChatMessage.msgType = mCurRecognizer;
                    mChatMessage.sourceMsg = result.getText();

                    mTranslate.start(result.getText(), result.getLangId(), mCurRecognizer == 0 ? mToLang : mFromLang);

                } else {
                    Toast.makeText(MainActivity.this, "未检测到您的声音", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onChangeVolume(int volumeCount) {

            }

            @Override
            public void onError(int state) {
                Toast.makeText(MainActivity.this, "识别失败", Toast.LENGTH_SHORT).show();
            }
        };

        mSynthesis = new SynthesisImpl(this) {
            @Override
            public void onSynFinished(int session) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopPlayAll();
                    }
                });

            }
        };

        mTranslate = new TranslateImpl(this) {
            @Override
            public void onTranslateResult(TranslateReply result) {
                if (result != null) {
                    if (mChatMessage == null) {
                        mChatMessage = new ChatMessage();
                    }
                    mChatMessage.translateMsg = result.getResultText();

                    mChatMessage.inPlay = true;
                    mSynthesis.stop();
                    mList.add(mChatMessage);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            messageAdapter.notifyDataSetChanged();
                            mRvChat.scrollToPosition(mList.size() - 1);
                        }
                    });
                    mIvLeftLang.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mSynthesis.start(result.getResultText(), result.getDstLangId());
                        }
                    }, 1000);
                }
            }
        };

        mTranslateBySayTips = new TranslateImpl(this) {
            @Override
            public void onTranslateResult(TranslateReply result) {
                if (result != null) {
                    leftRecordTips = result.getResultText();
                    tvLeftRecordTips.setText(leftRecordTips);
                    SharedPreUtils.saveString(MainActivity.this, AppConstant.FORM_LISTENING, leftRecordTips);
                }
            }
        };

        mTranslateBySayTipsByRight = new TranslateImpl(this) {
            @Override
            public void onTranslateResult(TranslateReply result) {
                if (result != null) {
                    rightRecordTips = result.getResultText();
                    tvRightRecordTips.setText(rightRecordTips);
                    SharedPreUtils.saveString(MainActivity.this, AppConstant.TO_LISTENING, rightRecordTips);
                }
            }
        };
    }


    private int mCurRecognizer = 0;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.iv_lang_up:
//                LanguageListActivity.toActivity(this, mFromLang, mToLang);
//                overridePendingTransition(R.anim.actionsheet_dialog_in, android.R.anim.fade_out);
//                break;
            case R.id.tv_usually_ask_answer:
                break;
            case R.id.rl_left_lang:
                Intent mIntent = new Intent();
                mIntent.putExtra(LANGUAGE, mFromLang);
                mIntent.putExtra(DIRECTION, 0);
                mIntent.setClass(this, SelectorLanguageActivity.class);
                startActivityForResult(mIntent, START_ACTIVITY_REQUEST_CODE);
                overridePendingTransition(R.anim.actionsheet_dialog_in, android.R.anim.fade_out);
                break;
            case R.id.wv_left_lang:
                stopPlayAll();
                mCurRecognizer = 0;
                leftWaveRecording = !leftWaveRecording;
                Logger.d("f:" + mFromLang + " t:" + mToLang);
                mGvLeftShadow.show();  //显示遮罩
                //开始录音
                mWvLeftLang.start();
                mRecognizer.start(mFromLang);
                break;
            case R.id.rl_right_lang:
                Intent mRightIntent = new Intent();
                mRightIntent.putExtra(LANGUAGE, mToLang);
                mRightIntent.putExtra(DIRECTION, 1);
                mRightIntent.setClass(this, SelectorLanguageActivity.class);
                startActivityForResult(mRightIntent, START_ACTIVITY_REQUEST_CODE);
                overridePendingTransition(R.anim.actionsheet_dialog_in, android.R.anim.fade_out);
                break;
            case R.id.wv_right_lang:
                stopPlayAll();
                mCurRecognizer = 1;
                rightWaveRecording = !rightWaveRecording;
                Logger.d("f:" + mFromLang + " t:" + mToLang);
                mGvRightShadow.show();  //显示遮罩
                //开始录音
                mWvRightLang.start();
                mRecognizer.start(mToLang);
                break;
            case R.id.rl_come_back:
                exeComeBack();
        }
    }

    private void exeComeBack() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == START_ACTIVITY_REQUEST_CODE && data != null) {
                int direction = data.getIntExtra(DIRECTION, 0);
                String countryName = data.getStringExtra(COUNTRY_NAME);
                String leftFlag = "";
                String rightFlag = "";
                if (direction == 0) {
                    // 左边
                    mFromLang = data.getIntExtra(LANGUAGE, 0);
                    mFromCountryName = countryName;
                    leftFlag = ConfigUtils.INSTANCE.getFlag(MainActivity.this, mFromLang).getFlag();
                    SharedPreUtils.saveInt(MainActivity.this, AppConstant.FORM_LANG, mFromLang);
                    SharedPreUtils.saveString(MainActivity.this, AppConstant.FORM_COUNTRY_NAME, countryName);
                    mTranslateBySayTips.start(getString(R.string.i_have_been_listening), 0, mFromLang);
                } else {
                    // 右边
                    mToLang = data.getIntExtra(LANGUAGE, 4);
                    mToCountryName = countryName;
                    rightFlag = ConfigUtils.INSTANCE.getFlag(MainActivity.this, mToLang).getFlag();
                    SharedPreUtils.saveInt(MainActivity.this, AppConstant.TO_LANG, mToLang);
                    SharedPreUtils.saveString(MainActivity.this, AppConstant.TO_COUNTRY_NAME, countryName);
                    mTranslateBySayTipsByRight.start(getString(R.string.i_have_been_listening), 0, mToLang);
                }
                setFlagAndWaveColor(leftFlag, rightFlag, mFromCountryName, mToCountryName);
            }
        }
    }

    @Override
    protected void onPause() {
        stopPlayAll();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setGuideView() {

        // 使用文字
        tvLeftRecordTips = new TextView(this);
        tvLeftRecordTips.setText(leftRecordTips);
        tvLeftRecordTips.setTextColor(getResources().getColor(R.color.white));
        tvLeftRecordTips.setTextSize(30);
        tvLeftRecordTips.setGravity(Gravity.CENTER);

        // 使用文字
        tvRightRecordTips = new TextView(this);
        tvRightRecordTips.setText(rightRecordTips);
        tvRightRecordTips.setTextColor(getResources().getColor(R.color.white));
        tvRightRecordTips.setTextSize(30);
        tvRightRecordTips.setGravity(Gravity.CENTER);


        mGvLeftShadow = GuideView.Builder
                .newInstance(this)
                .setTargetView(mWvLeftLang)//设置目标
                .setCustomGuideView(tvLeftRecordTips)
                .setDirction(GuideView.Direction.RIGHT_TOP)
                .setShape(GuideView.MyShape.CIRCULAR)   // 设置圆形显示区域，
                .setBgColor(getResources().getColor(R.color.shadow))
                .setOnclickListener(() -> {
                    mGvLeftShadow.hide();
                    //停止录音
                    mWvLeftLang.stopImmediately();
                    mRecognizer.stop();
                })
                .build();


        mGvRightShadow = GuideView.Builder
                .newInstance(this)
                .setTargetView(mWvRightLang)//设置目标
                .setCustomGuideView(tvRightRecordTips)
                .setDirction(GuideView.Direction.LEFT_TOP)
                .setShape(GuideView.MyShape.CIRCULAR)   // 设置圆形显示区域，
                .setBgColor(getResources().getColor(R.color.shadow))
                .setOnclickListener(() -> {
                    mGvRightShadow.hide();
                    //停止录音
                    mWvRightLang.stopImmediately();
                    mRecognizer.stop();
                })
                .build();
    }
}
