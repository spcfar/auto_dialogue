package com.hnsh.dialogue.mvp.models;

import android.content.Context;
import com.dosmono.universal.common.Error;
import com.hnsh.dialogue.bean.QuickwordContentBean;
import com.hnsh.dialogue.bean.cbs.MessageContent;
import com.hnsh.dialogue.bean.db.QuestionInfoBean;
import com.hnsh.dialogue.bean.db.TypeInfoBean;
import com.hnsh.dialogue.constants.TSRConstants;
import com.hnsh.dialogue.mvp.contracts.IUnfQuickWordContract;
import com.hnsh.dialogue.net.RxDmonkeyHttpClient;
import com.hnsh.dialogue.sql.base.QuickwordDbHelper;
import com.hnsh.dialogue.utils.CommonUtil;

import java.util.List;

import io.reactivex.Observer;

public class UnfQuickWordModel extends MessageModel implements IUnfQuickWordContract.IUnfQuickWordModel{

    private IUnfQuickWordContract.IUnfQuickWordModelCallback mCallback;

    private IUnfQuickWordContract.IUnfQuickWordPresenter mPresenter;
    private Context mContext;

    public UnfQuickWordModel(Context context, IUnfQuickWordContract.IUnfQuickWordPresenter presenter, IUnfQuickWordContract.IUnfQuickWordModelCallback callback) {
        super(context);
        this.mContext = context;
        this.mPresenter = presenter;
        this.mCallback = callback;
    }

    @Override
    public void loadCategoryData() {
        List<TypeInfoBean> categoryList = QuickwordDbHelper.queryCategoryInfoList();
        if (mCallback == null) {
            return;
        }
        mCallback.onLoadCategoryData(categoryList);
    }

    @Override
    public void loadPhraseData(long categoryId) {
        List<QuestionInfoBean> phraseList = QuickwordDbHelper.queryPhraseInfoList(categoryId);
        if (mCallback == null) {
            return;
        }
        mCallback.onLoadPhraseData(phraseList);
    }


    @Override
    public void insertPhraseData(Object dataBean) {


    }

    @Override
    public void insertQAData(Object dataBean) {

    }

    @Override
    public void requestPhraseQAVersion(Observer observer) {
        RxDmonkeyHttpClient.requestPhraseQAVersion(observer);
    }

    @Override
    public void requestPhraseData(Observer observer) {
        RxDmonkeyHttpClient.requestPhraseData(observer);
    }

    @Override
    public void reqeustQAData(Observer observer) {
        RxDmonkeyHttpClient.requestCommonQAData(observer);
    }

    @Override
    public void onDestroy() {
        mPresenter = null;
        mCallback = null;
    }

    private MessageContent makeMessage(int type, int langId, String text, int sessionId) {
        MessageContent content = new MessageContent();
        content.setContent(text);
        content.setLangId(langId);
        content.setMsgType(type);
        content.setSendTimeMs(System.currentTimeMillis());
        content.setSession(sessionId);
        return content;
    }

    @Override
    public void sendMessage(QuickwordContentBean contentBean, int sessionId) {
        String text = contentBean.getContent();
        int count = text == null ? 0 : text.length();
        if (count > 0 && !(count == 1 && CommonUtil.isSymbol(text))) {
            MessageContent content = makeMessage(TSRConstants.DIALOGUE_MSG_TYPE_ONESELF, 0, text,sessionId);
            content.setDeviceId(CommonUtil.getDeviceId());


            int state = sendMessage(content);
            if (state != Error.ERR_NONE) {
                if (mCallback != null) {
                    mCallback.onSendError(state);
                }
            }
        }
    }
}
