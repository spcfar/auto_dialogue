package com.hnsh.dialogue.factory.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hnsh.dialogue.R;
import com.hnsh.dialogue.adapter.WifiEncriptionAdapter;
import com.hnsh.dialogue.bean.WifiEncriptEntity;
import com.hnsh.dialogue.utils.EmptyUtils;
import com.hnsh.dialogue.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @项目名： BIZ_Project
 * @包名： com.dosmono.common.factory.dialog
 * @文件名: WIFIAddDialog
 * @创建者: zer
 * @创建时间: 2019/3/8 18:17
 * @描述： TODO
 */
public class WIFIAddDialog extends Dialog implements View.OnClickListener {

    private EditText mSsid;
    private ImageView mEditClean;
    private AppCompatSpinner mSpinner;
    private Button mBtnCancel;
    private Button mBtnSure;
    private List<WifiEncriptEntity> mDatas;
    private int                     encryptionTpye;
    private ICallback               mICallback;
    private RelativeLayout mPasswordView;
    private EditText mPassword;
    private ImageView mPasswordClean;

    public WIFIAddDialog(Context context) {
        super(context, R.style.dialog);
        setContentView(R.layout.dialog_wifi_add_layout);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);

        initView();
        initListener();
        initData();
    }

    private void initView() {
        mSsid = findViewById(R.id.et_wifi_add_net_name);
        mEditClean = findViewById(R.id.iv_wifi_add_edit_clean);
        mSpinner = findViewById(R.id.et_wifi_add_safety);
        mBtnCancel = findViewById(R.id.btn_wifi_add_cancel);
        mBtnSure = findViewById(R.id.btn_wifi_add_sure);

        mPasswordView = findViewById(R.id.rl_wifi_add_password_view);
        mPassword = findViewById(R.id.et_wifi_add_net_password);
        mPasswordClean = findViewById(R.id.iv_wifi_add_password_clean);
    }

    private void initListener() {
        mEditClean.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
        mBtnSure.setOnClickListener(this);
        mPasswordClean.setOnClickListener(this);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                WifiEncriptEntity entity = mDatas.get(position);
                encryptionTpye = entity.getEncription();
                if (encryptionTpye == 0){
                    mPasswordView.setVisibility(View.GONE);
                }else {
                    mPasswordView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSsid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0){
                    mEditClean.setVisibility(View.VISIBLE);
                }else {
                    mEditClean.setVisibility(View.GONE);
                }
            }
        });
        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0){
                    mPasswordClean.setVisibility(View.VISIBLE);
                }else {
                    mPasswordClean.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initData() {
        mDatas = new ArrayList<>();
        WifiEncriptEntity entity = new WifiEncriptEntity();
        entity.setEncription(0);
        entity.setName(getContext().getString(R.string.common_wifi_network_safety_none));
        mDatas.add(entity);

        WifiEncriptEntity entity1 = new WifiEncriptEntity();
        entity1.setEncription(1);
        entity1.setName("WEP");
        mDatas.add(entity1);

        WifiEncriptEntity entity2 = new WifiEncriptEntity();
        entity2.setEncription(2);
        entity2.setName("WPA/WPA2 PSK");
        mDatas.add(entity2);

        WifiEncriptEntity entity3 = new WifiEncriptEntity();
        entity3.setEncription(2);
        entity3.setName("802.1x EXP");
        mDatas.add(entity3);

        mSpinner.setDropDownVerticalOffset(5);
        WifiEncriptionAdapter adapter = new WifiEncriptionAdapter(getContext(), mDatas);
        mSpinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_wifi_add_edit_clean) {
            mSsid.setText("");
        } else if (id == R.id.btn_wifi_add_cancel) {
            dismiss();
        } else if (id == R.id.btn_wifi_add_sure) {
            String ssid = mSsid.getText().toString().trim();
            String password = mPassword.getText().toString().trim();
            if (EmptyUtils.isNotEmpty(ssid)) {
                if (encryptionTpye == 0){
                    dismiss();
                    password = "";
                    if (mICallback != null) {
                        mICallback.onSureClick(encryptionTpye, ssid,password);
                    }
                }else {
                    if (EmptyUtils.isNotEmpty(password)){
                        if (encryptionTpye != 1 && password.length() < 8){
                            UIUtils.showToast(getContext(),getContext().getString(R.string.pwd_length_toast));
                            return;
                        }
                        dismiss();
                        if (mICallback != null) {
                            mICallback.onSureClick(encryptionTpye, ssid,password);
                        }
                    }
                }
            }
        }else if (id == R.id.iv_wifi_add_password_clean){
            mPassword.setText("");
        }
    }

    public void setCallback(ICallback callback) {
        mICallback = callback;
    }


    public interface ICallback {
        void onSureClick(int encrypt, String ssid, String password);
    }

}
