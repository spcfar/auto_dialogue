package com.hnsh.dialogue.mvp.presenters;

import android.content.Intent;
import android.widget.Toast;
import com.dosmono.logger.Logger;
import com.hnsh.dialogue.bean.QuickwordCategoryBean;
import com.hnsh.dialogue.bean.QuickwordContentBean;
import com.hnsh.dialogue.bean.cbs.PhraseResultBean;
import com.hnsh.dialogue.bean.cbs.QAResultBean;
import com.hnsh.dialogue.bean.cbs.QuickWordVersionBean;
import com.hnsh.dialogue.bean.cbs.ResponseBean;
import com.hnsh.dialogue.bean.db.QACategoryBean;
import com.hnsh.dialogue.bean.db.QAInfoBean;
import com.hnsh.dialogue.bean.db.QuestionInfoBean;
import com.hnsh.dialogue.bean.db.TypeInfoBean;
import com.hnsh.dialogue.constants.BIZConstants;
import com.hnsh.dialogue.mvp.contracts.IUnfQuickWordContract;
import com.hnsh.dialogue.mvp.models.UnfQuickWordModel;
import com.hnsh.dialogue.mvp.presenters.base.BasePresenter;
import com.hnsh.dialogue.net.RxDmonkeyHttpClient;
import com.hnsh.dialogue.sql.base.QuickwordDbHelper;
import com.hnsh.dialogue.utils.EmptyUtils;
import com.hnsh.dialogue.utils.PrefsUtils;
import com.hnsh.dialogue.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class UnfCommonPhrasePresenter extends BasePresenter<IUnfQuickWordContract.IUnfQuickWordModel, IUnfQuickWordContract.IUnfQuickWordView> implements IUnfQuickWordContract.IUnfQuickWordPresenter {


    private QuickwordObserver mVersionObserver;
    private QuickwordObserver mPhraseObserver;
    private QuickwordObserver mQAObserver;
    private QuickWordModelCallback mModelCallback;
    private UnfQuickWordModel mModel;

    public UnfCommonPhrasePresenter(IUnfQuickWordContract.IUnfQuickWordView view) {
        mRootView = view;
    }

    @Override
    public void initData(Intent var1) {
        mModelCallback = new QuickWordModelCallback();
        mModel = new UnfQuickWordModel(mRootView.applicationContext(),this, mModelCallback);
        startLoadData();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    private void startLoadData() {
        if (mRootView.isPhraseOrQAMode()) {
            loadCategoryDatas();
        } else {
            loadQACategoryDatas();
        }
    }


    private void checkQuickWordVersion() {
        mVersionObserver = new QuickwordObserver() {
            @Override
            void onSuccess(Object o) {
                if (mRootView != null) {
                    mRootView.hideLoading();
                }

                ResponseBean<QuickWordVersionBean> responseBean = (ResponseBean<QuickWordVersionBean>) o;
                String phraseVersion = PrefsUtils.getPrefs(UIUtils.getContext(), BIZConstants.Dmonkey.KEY_QUICK_PHRASE_VERSION, "");
                String commonQAVersion = PrefsUtils.getPrefs(UIUtils.getContext(), BIZConstants.Dmonkey.KEY_COMMON_QA_VERSION, "");

                if (responseBean.getCode() == 200) {

                    mRootView.hideLoading();

                    QuickWordVersionBean dataBean = responseBean.getData();
                    // ???????????????
                    if (compareVersions(dataBean.getUsedQVersion(), phraseVersion)) {
                        //????????????????????????
                        requestPhraseData();
                    }
                    // ???????????????
                    if (compareVersions(dataBean.getQuickQVersion(), commonQAVersion)) {
                        //???????????????????????????
                        requestQAData();
                    }
                }
                // requestPhraseData();
            }

            @Override
            void onFailure(Throwable e) {
                if (mRootView != null)
                    mRootView.hideLoading();
            }
        };
        mRootView.showLoading();

        RxDmonkeyHttpClient.requestPhraseQAVersion(mVersionObserver);
    }

    /***
     * ?????????????????????????????????
     */
    public void loadQACategoryDatas() {
        if (mRootView == null) {
            Logger.e("mRootView??????");
            return;
        }

        List<QACategoryBean> categoryInfoList = QuickwordDbHelper.queryQACategoryList();

        List<QuickwordCategoryBean> categoryBeans = convertCategoryType(categoryInfoList);

        if (EmptyUtils.isNotEmpty(categoryBeans)) {

            mRootView.loadCategoryDatas(categoryBeans);
            //????????????
            loadQAData(categoryBeans.get(0).getCategoryId(), 0);
            //????????????
       //     checkQuickWordVersion();
           // requestQAData();
        } else {
            //???????????????????????????????????????
            requestQAData();
        }
    }

    /**
     * ?????? ???????????? ???????????????
     */
    public void loadCategoryDatas() {

        if (mRootView == null) {
            Logger.e("mRootView??????");
            return;
        }

        List<TypeInfoBean> categoryInfoList = QuickwordDbHelper.queryCategoryInfoList();

        List<QuickwordCategoryBean> categoryBeans = convertCategoryType(categoryInfoList);

        if (categoryBeans != null && !categoryBeans.isEmpty()) {
            //??????????????????
            mRootView.loadCategoryDatas(categoryBeans);
            loadPhraseData(categoryInfoList.get(0).getTypeId());
            //??????????????????
      //      checkQuickWordVersion();
        } else {
            //?????? ???????????? ??????
            requestPhraseData();
        }
    }

    /**
     * ?????? ???????????? ??????
     *
     * @param categoryId
     */
    @Override
    public void loadPhraseData(long categoryId) {
        List<QuestionInfoBean> phraseList = QuickwordDbHelper.queryPhraseInfoList(categoryId);
        List<QuickwordContentBean> contentBeanList = convertContentType(phraseList);
        mRootView.loadContentDatas(contentBeanList);
    }


    /**
     * ?????? ???????????? ??????
     * @param categoryId
     */

    @Override
    public void loadQAData(long categoryId, int lang) {
        List<QAInfoBean> phraseList = QuickwordDbHelper.queryQAInfoList(categoryId, lang);
        List<QuickwordContentBean> contentBeanList = convertContentType(phraseList);
        mRootView.loadContentDatas(contentBeanList);
    }

    private <T> List<QuickwordContentBean> convertContentType(List<T> list) {
        if (list == null) {
            Logger.d("??????????????????,list????????????");
        }
        if (list.size() == 0) {
            return new ArrayList();
        }
        List<QuickwordContentBean> contentBeanList = new ArrayList<>();
        //????????????
        if (list.get(0) instanceof QuestionInfoBean) {

            for (T t : list) {
                QuestionInfoBean bean = (QuestionInfoBean) t;
                contentBeanList.add(new QuickwordContentBean(bean.getQId(), bean.getContent(), false));
            }

            return contentBeanList;

        }
        //????????????
        else if (list.get(0) instanceof QAInfoBean) {
            for (T t : list) {
                QAInfoBean bean = (QAInfoBean) t;
                contentBeanList.add(new QuickwordContentBean(bean.getId(), bean.getContent(), true));
            }

            return contentBeanList;
        }
        return null;
    }

    @Override
    public void sendMessage(QuickwordContentBean contentBean,int sessionId) {
        mModel.sendMessage(contentBean,sessionId);
    }

    private <T> List<QuickwordCategoryBean> convertCategoryType(List<T> list) {
        if (list == null) {
            Logger.d("??????????????????,list????????????");
        }
        if (list.size() == 0) {
            return new ArrayList();
        }
        List<QuickwordCategoryBean> contentBeanList = new ArrayList<>();
        if (list.get(0) instanceof TypeInfoBean) {

            for (T t : list) {
                TypeInfoBean bean = (TypeInfoBean) t;
                contentBeanList.add(new QuickwordCategoryBean(bean.getTypeId(), bean.getTypeName()));
            }

            return contentBeanList;
        } else if (list.get(0) instanceof QACategoryBean) {
            for (T t : list) {
                QACategoryBean bean = (QACategoryBean) t;
                contentBeanList.add(new QuickwordCategoryBean(bean.getTypeId(), bean.getTypeName()));
            }
            return contentBeanList;
        }
        return null;
    }

    public boolean compareVersions(String lastedVersion, String currentVersion) {
        if(lastedVersion == null){
            return false;
        }
        return lastedVersion.compareToIgnoreCase(currentVersion) > 0;
    }

    private void requestPhraseData() {

        mPhraseObserver = new QuickwordObserver() {
            @Override
            void onSuccess(Object o) {

                if(mRootView == null){
                    return;
                }
                mRootView.hideLoading();
                ResponseBean<PhraseResultBean> responseBean = (ResponseBean<PhraseResultBean>) o;
                PhraseResultBean dataBean = responseBean.getData();
          //      List<TypeInfoBean> list = QuickwordDbHelper.queryCategoryInfoList();
                List<QuickwordCategoryBean> categoryBeanList = convertCategoryType(dataBean.getTypeInfo().getNext());
                //
                if(categoryBeanList != null && !categoryBeanList.isEmpty() && mRootView.isPhraseOrQAMode()){

                    mRootView.loadCategoryDatas(categoryBeanList);
                    loadPhraseData(categoryBeanList.get(0).getCategoryId());
                }

            }

            @Override
            void onFailure(Throwable e) {
                if (mRootView != null) {
                    mRootView.hideLoading();
                }
                Logger.d("?????????????????????????????????????????????????????????");
            }
        };
        mRootView.showLoading();
        mModel.requestPhraseData(mPhraseObserver);
    }

    private void requestQAData() {
        mQAObserver = new QuickwordObserver() {
            @Override
            void onSuccess(Object o) {
                Logger.d("requestQAData=====????????????");
                if (mRootView == null) {
                    return;
                }
                mRootView.hideLoading();
                ResponseBean<QAResultBean> responseBean = (ResponseBean<QAResultBean>) o;
                QAResultBean dataBean = responseBean.getData();
                List<QACategoryBean> categoryBeanList = dataBean.getTypeList();
                List<QuickwordCategoryBean> categoryBeans = convertCategoryType(categoryBeanList);

                //?????????????????????????????????????????????
                if(categoryBeans != null && !categoryBeans.isEmpty() && !mRootView.isPhraseOrQAMode()){
                    mRootView.loadCategoryDatas(categoryBeans);
                    loadQAData(categoryBeans.get(0).getCategoryId(),0);
                }
            }

            @Override
            void onFailure(Throwable e) {

                if (mRootView != null) {
                    mRootView.hideLoading();
                }
                new IllegalStateException("?????????????????????????????????????????????????????????");
                Logger.d("requestQAData=====????????????");
                Logger.d("?????????????????????????????????????????????????????????");
            }
        };
        mRootView.showLoading();
        mModel.reqeustQAData(mQAObserver);
    }

    abstract class QuickwordObserver implements Observer {

        private Disposable mDisposable = null;

        @Override
        public void onSubscribe(Disposable d) {
            mDisposable = d;
        }

        @Override
        public void onNext(Object o) {
            onSuccess(o);
        }

        @Override
        public void onError(Throwable e) {
            onFailure(e);
            if (mDisposable != null) {
                mDisposable.dispose();
            }
        }

        @Override
        public void onComplete() {
            if (mDisposable != null) {
                mDisposable.dispose();
            }
        }

        abstract void onSuccess(Object o);

        abstract void onFailure(Throwable e);
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {
        mVersionObserver = null;
        mPhraseObserver = null;
        mQAObserver = null;
        if (mRootView != null) {
            mRootView.hideLoading();
        }
        if (mModel != null) {
            mModel.onDestroy();
            mModel = null;
        }
    }

    public class QuickWordModelCallback implements IUnfQuickWordContract.IUnfQuickWordModelCallback {

        @Override
        public void onLoadCategoryData(List<TypeInfoBean> categoryList) {
            if (mRootView == null) {
                Logger.e("onLoadCategoryData -> mRooView ??????????????????????????????");
                return;
            }
            if (categoryList == null || categoryList.size() == 0) {
                Logger.e("onLoadCategoryData -> categoryList ??????????????????????????????????????????");
                return;
            }
            List<QuickwordCategoryBean> categoryBeans = convertCategoryType(categoryList);
            mRootView.loadCategoryDatas(categoryBeans);
            mModel.loadPhraseData(categoryList.get(0).getTypeId());
        }

        @Override
        public void onLoadPhraseData(List<QuestionInfoBean> phraseList) {
            if (mRootView == null) {
                Logger.e("onLoadPhraseData -> mRooView ??????????????????????????????");
                return;
            }
            if (phraseList == null || phraseList.size() == 0) {
                Logger.e("onLoadPhraseData -> phraseList ??????????????????????????????????????????");
                return;
            }

        }

        @Override
        public void onSendError(int state) {
            Toast.makeText(UIUtils.getContext(), "??????????????????", Toast.LENGTH_SHORT).show();
        }
    }
}
