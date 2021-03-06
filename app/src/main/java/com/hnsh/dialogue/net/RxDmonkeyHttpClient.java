package com.hnsh.dialogue.net;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dosmono.logger.Logger;
import com.hnsh.dialogue.bean.cbs.PhraseResultBean;
import com.hnsh.dialogue.bean.cbs.QAResultBean;
import com.hnsh.dialogue.bean.cbs.QuickWordVersionBean;
import com.hnsh.dialogue.bean.cbs.ResponseBean;
import com.hnsh.dialogue.bean.db.QACategoryBean;
import com.hnsh.dialogue.bean.db.QAInfoBean;
import com.hnsh.dialogue.constants.BIZConstants;
import com.hnsh.dialogue.factory.DmonkeyParamFactory;
import com.hnsh.dialogue.sql.base.QuickwordDbHelper;
import com.hnsh.dialogue.utils.AppUtil;
import com.hnsh.dialogue.utils.CommonUtil;
import com.hnsh.dialogue.utils.EmptyUtils;
import com.hnsh.dialogue.utils.NetUtils;
import com.hnsh.dialogue.utils.PrefsUtils;
import com.hnsh.dialogue.utils.UIUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

import static com.hnsh.dialogue.constants.BIZConstants.Dmonkey.URL_SEND_DIALOGUE_STAT_DATA;

public class RxDmonkeyHttpClient {

    private final static Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Disposable disposableObj = (Disposable) msg.obj;
            if (EmptyUtils.isNotEmpty(disposableObj)) {
                disposableObj.dispose();
            }

        }
    };

    private static void showMessage(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(UIUtils.getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void sendInstallAppVersion(Map versions) {
        Disposable subscribe = RestCreator.getRxRestService().sendInstallAppVersion(DmonkeyParamFactory.createJson(versions))
//                .subscribeOn(Schedulers.io())
                .retryWhen(new Function<Observable<? extends Throwable>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<? extends Throwable> throwableObservable) throws Exception {
                        return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(Throwable throwable) throws Exception {
                                if (throwable instanceof IOException) {
                                    return Observable.just(throwable);
                                }
                                return Observable.error(throwable);
                            }
                        });
                    }
                })
                .map(new Function<String, ResponseBean<QuickWordVersionBean>>() {
                    @Override
                    public ResponseBean<QuickWordVersionBean> apply(String s) throws Exception {
                        return JSON.parseObject(s, new TypeReference<ResponseBean<QuickWordVersionBean>>() {
                        });
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }


    /**
     * ???????????????????????????
     *
     * @param observer
     * @return
     */
    public static Observer<QuickWordVersionBean> requestPhraseQAVersion(Observer observer) {
        return RestCreator.getRxRestService().requestPhraseQAVersionString(DmonkeyParamFactory.createJson(new JSONObject()))
//                .subscribeOn(Schedulers.io())
                .retryWhen(new Function<Observable<? extends Throwable>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<? extends Throwable> throwableObservable) throws Exception {
                        return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(Throwable throwable) throws Exception {
                                if (throwable instanceof IOException) {
                                    return Observable.just(throwable);
                                }
                                return Observable.error(throwable);
                            }
                        });
                    }
                })
                .map(new Function<String, ResponseBean<QuickWordVersionBean>>() {
                    @Override
                    public ResponseBean<QuickWordVersionBean> apply(String s) throws Exception {
                        return JSON.parseObject(s, new TypeReference<ResponseBean<QuickWordVersionBean>>() {
                        });
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer);

    }

    /**
     * ????????????????????????
     */
    public static void globalInitQuickWordData() {


        final Observer<ResponseBean<QuickWordVersionBean>> observer = new Observer<ResponseBean<QuickWordVersionBean>>() {
            Disposable disposable;

            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(ResponseBean<QuickWordVersionBean> responseBean) {
                QuickWordVersionBean quickWordVersionBean = responseBean.getData();
                if (quickWordVersionBean == null) {
                    Logger.e("[ RxDmonkeyHttpClient ] ??????????????????????????????????????????");
                    return;
                }

                long channelId = PrefsUtils.getPrefs(UIUtils.getContext(), BIZConstants.Dmonkey.KEY_CHANNELID, -1L);
                long newChannelId = quickWordVersionBean.getChannelId() != null ? quickWordVersionBean.getChannelId() : 0L;
                String phraseVersion = PrefsUtils.getPrefs(UIUtils.getContext(), BIZConstants.Dmonkey.KEY_QUICK_PHRASE_VERSION, "v1.0");
                String commonQAVersion = PrefsUtils.getPrefs(UIUtils.getContext(), BIZConstants.Dmonkey.KEY_COMMON_QA_VERSION, "v1.0");

                //????????????????????????????????????
                //??????????????????
                Logger.d("[ RxDmonkeyHttpClient ] ????????????Id???" + channelId + ",????????????Id???" + newChannelId);
                if (newChannelId != channelId) {
                    phraseVersion = "v1.0";
                    commonQAVersion = "v1.0";
                    PrefsUtils.setPrefs(UIUtils.getContext(), BIZConstants.Dmonkey.KEY_CHANNELID, newChannelId);
                }
                //?????? ???????????? ?????????
                Logger.d(quickWordVersionBean.toString());
                Logger.d("[ RxDmonkeyHttpClient ] ???????????????????????????" + commonQAVersion + ",???????????????????????????" + phraseVersion);

                if (!phraseVersion.equalsIgnoreCase(quickWordVersionBean.getUsedQVersion())) {
                    final Message msg = Message.obtain();
                    Disposable mDisposable = requestPhraseData().subscribe(responseBean1 -> mHandler.sendMessage(msg), throwable -> {
                        mHandler.sendMessage(msg);
                        Logger.e("[ RxDmonkeyHttpClient ] ???????????????????????????????????? " + throwable.toString());
                        Logger.e(throwable.toString());
                        Logger.e("[ RxDmonkeyHttpClient ] ???????????????" + BIZConstants.Dmonkey.URL_BASE);
                    });
                    msg.obj = mDisposable;
                }
                //?????? ?????????????????????
                if (!commonQAVersion.equalsIgnoreCase(quickWordVersionBean.getQuickQVersion())) {
                    final Message msg = Message.obtain();
                    Disposable mDisposable = requestCommonQAData().subscribe(new Consumer<ResponseBean<QAResultBean>>() {
                        @Override
                        public void accept(ResponseBean<QAResultBean> qaResultBeanResponseBean) throws Exception {
                            mHandler.sendMessage(msg);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            mHandler.sendMessage(msg);
                            //     showMessage("???????????????????????????????????? " + throwable.toString());
                            Logger.d("[ RxDmonkeyHttpClient ] ???????????????????????????????????? " + throwable.toString());
                            Logger.e(throwable.toString());
                        }
                    });
                    msg.obj = mDisposable;
                }
            }

            @Override
            public void onError(final Throwable e) {
                showMessage("?????????????????????????????????????????????????????????????????????" + e.toString());
                Logger.e("[ RxDmonkeyHttpClient ] ???????????????" + BIZConstants.Dmonkey.URL_BASE);
                Logger.e(e.toString());
                if (disposable != null)
                    disposable.dispose();
            }

            @Override
            public void onComplete() {
                if (disposable != null)
                    disposable.dispose();
            }
        };
        requestPhraseQAVersion(observer);
    }


    /**
     * ??????????????????
     *
     * @param lastedVersion
     * @param localVersion
     * @return
     */
    @Deprecated  //?????????????????? ???????????? - 20200326???????????????
    private static boolean compareVersions(String lastedVersion, String localVersion) {


        if (EmptyUtils.isEmpty(lastedVersion)) {
            return false;
        }


        if (EmptyUtils.isEmpty(localVersion)) {
            return true;
        }

        //20200326 ?????? ???????????? -?????????
        String lasted = lastedVersion;
        String current = localVersion;
        if (lastedVersion.toUpperCase().startsWith("V")) {
            lasted = lastedVersion.substring(1);
        }
        if (localVersion.toUpperCase().startsWith("V")) {
            current = localVersion.substring(1);
        }

        return CommonUtil.compareVersion(lasted, current) == 1;
    }

    /**
     * ????????????????????????
     *
     * @return
     */
    private static Observable<ResponseBean<PhraseResultBean>> requestPhraseData() {
        return RestCreator.getRxRestService()
                .requestPhraseDataString(DmonkeyParamFactory.createJson(new JSONObject()))

//                .subscribeOn(Schedulers.io())
                .retryWhen(new Function<Observable<? extends Throwable>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<? extends Throwable> throwableObservable) throws Exception {
                        return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(Throwable throwable) throws Exception {
                                if (throwable instanceof IOException) {
                                    return Observable.just(throwable);
                                }

                                return Observable.error(throwable);
                            }
                        });
                    }
                }).map(new Function<String, ResponseBean<PhraseResultBean>>() {
                    @Override
                    public ResponseBean<PhraseResultBean> apply(String s) throws Exception {
                        return JSON.parseObject(s, new TypeReference<ResponseBean<PhraseResultBean>>() {
                        });
                    }
                })
                .filter(new Predicate<ResponseBean<PhraseResultBean>>() {
                    @Override
                    public boolean test(ResponseBean<PhraseResultBean> responseBean) throws Exception {
                        return responseBean.getCode() == 200 && responseBean.getData() != null;
                    }
                })
                .map(new Function<ResponseBean<PhraseResultBean>, ResponseBean<PhraseResultBean>>() {
                    @Override
                    public ResponseBean<PhraseResultBean> apply(ResponseBean<PhraseResultBean> responseBean) throws Exception {
                        Logger.d("[ RxDmonkeyHttpClient ] thread name = " + Thread.currentThread().getName());
                        //TODO
                        PhraseResultBean phraseResultBean = null;
                        try {
                            phraseResultBean = responseBean.getData();
                        } catch (ClassCastException e) {
                            Logger.e("[ RxDmonkeyHttpClient ] insertPhraseData ?????????????????? -> " + e);
                        }
                        if (phraseResultBean == null) {
                            Logger.d("[ RxDmonkeyHttpClient ] ????????????phraseResultBean == null");
                            return responseBean;
                        }

                        //????????????????????????????????????????????????
                        QuickwordDbHelper.clearPhraseRelated();
                        if (EmptyUtils.isEmpty(phraseResultBean.getTypeInfo()) || EmptyUtils.isEmpty(phraseResultBean.getTypeInfo().getNext())) {
                            Logger.d("[ RxDmonkeyHttpClient ] ??????????????????????????????");
                            return responseBean;
                        }
                        //?????? ???????????????????????????
                        //??????????????????
                        QuickwordDbHelper.insertCategoryInfoList(phraseResultBean.getTypeInfo().getNext());

                        if (EmptyUtils.isNotEmpty(phraseResultBean.getQuestionInfo())) {
                            // ????????????????????????
                            QuickwordDbHelper.insertPhraseInfoList(phraseResultBean.getQuestionInfo());
                            Logger.d("[ RxDmonkeyHttpClient ] ???????????????????????????" + phraseResultBean.getQuestionInfo().size() + "????????????");
                            Logger.i("[ RxDmonkeyHttpClient ] ?????????????????????start " + phraseResultBean.getQuestionInfo().toString() + " end ");
                        }
                        if (EmptyUtils.isNotEmpty(phraseResultBean.getTranslationInfo())) {
                            // ??????????????????
                            QuickwordDbHelper.insertTranslationList(phraseResultBean.getTranslationInfo());
                            Logger.d("[ RxDmonkeyHttpClient ] ?????????????????????????????????" + phraseResultBean.getTranslationInfo().size() + "????????????");
                            Logger.i("[ RxDmonkeyHttpClient ] ???????????????????????????start " + phraseResultBean.getTranslationInfo().toString() + " end");
                        }
                        // ??????????????????
                        PrefsUtils.setPrefs(UIUtils.getContext(), BIZConstants.Dmonkey.KEY_QUICK_PHRASE_VERSION, phraseResultBean.getVersion());
                        return responseBean;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * ????????????????????????
     *
     * @param observer
     */
    public static void requestPhraseData(Observer observer) {

        requestPhraseData().subscribe(observer);
    }


    /**
     * ????????????????????????
     *
     * @return
     */
    private static Observable<ResponseBean<QAResultBean>> requestCommonQAData() {
        return RestCreator.getRxRestService()
                .requestCommonQADataString(DmonkeyParamFactory.createJson(new JSONObject()))
//                .subscribeOn(Schedulers.io())
                .retryWhen(new Function<Observable<? extends Throwable>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<? extends Throwable> throwableObservable) throws Exception {
                        return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(Throwable throwable) throws Exception {
                                if (throwable instanceof IOException) {
                                    return Observable.just(throwable);
                                }
                                return Observable.error(throwable);
                            }
                        });
                    }
                })
                .map(new Function<String, ResponseBean<QAResultBean>>() {
                    @Override
                    public ResponseBean<QAResultBean> apply(String s) throws Exception {
                        return JSON.parseObject(s, new TypeReference<ResponseBean<QAResultBean>>() {
                        });
                    }
                })
                .filter(new Predicate<ResponseBean<QAResultBean>>() {
                    @Override
                    public boolean test(ResponseBean<QAResultBean> responseBean) throws Exception {
                        return responseBean.getCode() == 200 && responseBean.getData() != null;
                    }
                })
                .map(new Function<ResponseBean<QAResultBean>, ResponseBean<QAResultBean>>() {
                    @Override
                    public ResponseBean<QAResultBean> apply(ResponseBean<QAResultBean> responseBean) throws Exception {

                        QAResultBean resultBean = responseBean.getData();
                        List<QACategoryBean> categoryBeanList = resultBean.getTypeList();
                        List<QAInfoBean> qaInfoBeans = new ArrayList<>();

                        QuickwordDbHelper.clearQACategoryList();
                        if (categoryBeanList == null || categoryBeanList.isEmpty()) {
                            //????????????????????????????????????????????????
                            QuickwordDbHelper.clearQAInfoList();
                            Logger.d("[ RxDmonkeyHttpClient ] ??????????????????????????????");
                            return responseBean;
                        }
                        //??????????????????
                        QuickwordDbHelper.insertQACategoryList(categoryBeanList);
                        for (int i = 0; i < categoryBeanList.size(); i++) {
                            QACategoryBean categoryBean = categoryBeanList.get(i);
                            List<QAInfoBean> dataInfos = categoryBean.getDataInfos();

                            if (dataInfos == null || dataInfos.isEmpty())
                                continue;

                            qaInfoBeans.addAll(dataInfos);

                        }
                        //????????????????????????
                        if (qaInfoBeans.size() > 0) {
                            QuickwordDbHelper.clearQAInfoList();
                            QuickwordDbHelper.insertQAInfoList(qaInfoBeans);
                            Logger.d("[ RxDmonkeyHttpClient ] ???????????????????????????:" + qaInfoBeans.size() + "?????????!");
                            Logger.i("[ RxDmonkeyHttpClient ] ?????????????????????start " + qaInfoBeans.toString() + " end");
                        }
                        //??????????????????
                        PrefsUtils.setPrefs(UIUtils.getContext(), BIZConstants.Dmonkey.KEY_COMMON_QA_VERSION, resultBean.getVersion());
                        return responseBean;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * ????????????????????????
     *
     * @param observer
     */
    public static void requestCommonQAData(Observer observer) {

        requestCommonQAData().subscribe(observer);
    }


    /**
     * ??????????????????
     */
    public static void sendDialogueStatData() {
        Logger.d("URL_SEND_DIALOGUE_STAT_DATA:" + URL_SEND_DIALOGUE_STAT_DATA);
        Map content = new JSONObject();
        String phraseStat = JSONObject.toJSONString(BIZConstants.Dmonkey.PHRASE_STAT);
        String QAStat = JSONObject.toJSONString(BIZConstants.Dmonkey.QA_STAT);
        String langStat = JSONObject.toJSONString(BIZConstants.Dmonkey.FELLOW_LANG_STAT);
        content.put("continuousQ", phraseStat);
        content.put("usedQ", QAStat);
        content.put("serveLanguage", langStat);
        JSONObject jo = DmonkeyParamFactory.createJson(content);
        Logger.d(jo.toJSONString());
        final Message msg = Message.obtain();
        Disposable disposable = RestCreator.getRxRestService()

                .sendDialogueStatData(jo)
//                .subscribeOn(Schedulers.io())
                .retryWhen(new Function<Observable<? extends Throwable>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<? extends Throwable> throwableObservable) throws Exception {
                        return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(Throwable throwable) throws Exception {
                                if (throwable instanceof IOException) {
                                    return Observable.just(throwable);
                                }
                                return Observable.error(throwable);
                            }
                        });
                    }
                })
                .map(new Function<String, ResponseBean>() {
                    @Override
                    public ResponseBean<QAResultBean> apply(String s) throws Exception {
                        return JSON.parseObject(s, new TypeReference<ResponseBean>() {
                        });
                    }
                })
                .filter(new Predicate<ResponseBean>() {
                    @Override
                    public boolean test(ResponseBean responseBean) throws Exception {
                        return responseBean.getCode() == 200;
                    }
                }).map(new Function<ResponseBean, ResponseBean>() {
                    @Override
                    public ResponseBean apply(ResponseBean responseBean) throws Exception {

                        BIZConstants.Dmonkey.PHRASE_STAT.clear();
                        BIZConstants.Dmonkey.QA_STAT.clear();
                        BIZConstants.Dmonkey.FELLOW_LANG_STAT.clear();
                        return responseBean;
                    }
                })
                .subscribe(new Consumer<ResponseBean>() {
                    @Override
                    public void accept(ResponseBean responseBean) throws Exception {
                        mHandler.sendMessage(msg);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mHandler.sendMessage(msg);
                        //    showMessage("????????????????????????,?????? =" + throwable.toString());
                        Logger.e("[ RxDmonkeyHttpClient ] ????????????????????????,?????? =" + throwable.toString());
                        Logger.e("[ RxDmonkeyHttpClient ] ???????????????" + BIZConstants.Dmonkey.URL_BASE);

                    }
                });
        msg.obj = disposable;
    }


    /**
     * ??????????????????
     */
    public static void sendHumanTranslationStatData() {
        Map<String, Object> tpMap = new HashMap<>();
        long salt = System.currentTimeMillis();
        tpMap.put("times", "1");
        RestCreator.getRxRestService().sendHumanTranslationStatData(DmonkeyParamFactory.parameters(tpMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();


    }

    public interface SaveUploadRecordVideoListener {
        void onSuccess(JSONObject responseBean);


        void onFailure();
    }

    public static void saveUploadRecordVideo(Map<String, String> params, final SaveUploadRecordVideoListener listener) {
        final Message msg = Message.obtain();
        RestCreator.getRxRestService().saveUploadRecordVideo(DmonkeyParamFactory.parameters(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new Function<Observable<? extends Throwable>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<? extends Throwable> throwableObservable) throws Exception {
                        return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(Throwable throwable) throws Exception {
                                if (throwable instanceof IOException) {
                                    return Observable.just(throwable);
                                }
                                return Observable.error(throwable);
                            }
                        });
                    }
                })
                .map(new Function<String, JSONObject>() {

                    @Override
                    public JSONObject apply(String s) throws Exception {
                        return JSONObject.parseObject(s);
                    }
                })
                .subscribe(new Consumer<JSONObject>() {
                    @Override
                    public void accept(JSONObject responseBean) throws Exception {
                        mHandler.sendMessage(msg);
                        if (listener != null)
                            listener.onSuccess(responseBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mHandler.sendMessage(msg);
                        //    showMessage("????????????????????????,?????? =" + throwable.toString());
                        Logger.e("[ RxDmonkeyHttpClient ] ????????????????????????,?????? =" + throwable.toString());
                        Logger.e("[ RxDmonkeyHttpClient ] ???????????????" + BIZConstants.Dmonkey.URL_BASE);
                        if (listener != null)
                            listener.onFailure();

                    }
                });
    }


    private static Disposable disposable;

    /**
     * ???????????????
     */
    public static void sendHeartbeatData() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
        }
        Logger.d("[ RxDmonkeyHttpClient ] sendHeartbeatData???");
        final JSONObject param = new JSONObject();
        param.put("sno", CommonUtil.getDeviceId());
        param.put("app_version", AppUtil.getAppInfo(UIUtils.getContext(), null).versionName);
        param.put("dosmonoId", "biz");
        param.put("com.dosmono.firmware", CommonUtil.getWireVersion(UIUtils.getContext()));
        final Message msg = Message.obtain();
        disposable = Observable.interval(2, TimeUnit.MINUTES)
                .filter(new Predicate<Long>() {
                    @Override
                    public boolean test(Long aLong) throws Exception {
                        return NetUtils.isConnected(UIUtils.getContext());
                    }
                })
                .flatMap(new Function<Long, ObservableSource<ResponseBody>>() {
                    @Override
                    public ObservableSource<ResponseBody> apply(Long aLong) throws Exception {
                        Logger.d(isMainThread() + "[ RxDmonkeyHttpClient ] send Heartbeat , param = " + param.toJSONString());

                        return RestCreator.getRxRestService()
                                .sendHeartbeat(param);
                    }
                })
                .subscribeOn(Schedulers.io())
//                .map(new Function<ResponseBody, String>() {
//                    @Override
//                    public String apply(ResponseBody responseBody) throws Exception {
//                        String result = responseBody.string();
//                      //  Logger.d("Heartbeat response from server ,result = " + result);
//                        return result;
//                    }
//                })
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody result) throws Exception {
                        Logger.d("[ RxDmonkeyHttpClient ] Heartbeat response from server ,result = " + result.string());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        // showMessage("?????????????????????,?????? =" + throwable.toString());
                        Logger.e("[ RxDmonkeyHttpClient ] ?????????????????????,??????=" + throwable.toString());
                    }
                });
    }

    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

}
