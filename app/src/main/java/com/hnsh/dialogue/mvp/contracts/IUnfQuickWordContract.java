package com.hnsh.dialogue.mvp.contracts;

import android.content.Context;

import com.hnsh.dialogue.bean.QuickwordCategoryBean;
import com.hnsh.dialogue.bean.QuickwordContentBean;
import com.hnsh.dialogue.bean.db.QuestionInfoBean;
import com.hnsh.dialogue.bean.db.TypeInfoBean;
import com.hnsh.dialogue.mvp.models.base.IModel;
import com.hnsh.dialogue.mvp.presenters.base.IPresenter;
import com.hnsh.dialogue.mvp.view.base.IView;

import java.util.List;

import io.reactivex.Observer;

public interface IUnfQuickWordContract {

    interface IUnfQuickWordPresenter extends IPresenter {

        void loadPhraseData(long categoryId);
        void loadQAData(long categoryId, int lang);
        void sendMessage(QuickwordContentBean contentBean, int sessionId);
    }

    interface IUnfQuickWordView extends IView {
        void loadCategoryDatas(List<QuickwordCategoryBean> mDatas);
        Context applicationContext();
        void loadContentDatas(List<QuickwordContentBean> mContents);
        boolean isPhraseOrQAMode();

    }



    interface IUnfQuickWordModel extends IModel {

        void loadCategoryData();

        void loadPhraseData(long categoryId);

        void sendMessage(QuickwordContentBean contentBean, int sessionId);

        //        void loadPhraseData();
//        void loadQAData();
        void insertPhraseData(Object dataBean);

        void insertQAData(Object dataBean);

        void requestPhraseQAVersion(Observer observer);

        void requestPhraseData(Observer observer);

        void reqeustQAData(Observer observer);

    }

    interface IUnfQuickWordModelCallback {
        //加载分类信息数据 完成
        void onLoadCategoryData(List<TypeInfoBean> categoryList);

        //加载 快捷用语信息数据 完成
        void onLoadPhraseData(List<QuestionInfoBean> phraseList);

        void onSendError(int state);
    }
}
