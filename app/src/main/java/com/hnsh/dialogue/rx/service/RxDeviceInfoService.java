package com.hnsh.dialogue.rx.service;



import com.hnsh.dialogue.bean.cbs.PhraseResultBean;
import com.hnsh.dialogue.bean.cbs.QuickWordVersionBean;
import com.hnsh.dialogue.bean.cbs.ResponseBean;
import com.hnsh.dialogue.constants.BIZConstants;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RxDeviceInfoService {

    @FormUrlEncoded
    @POST(BIZConstants.Dmonkey.URL_GET_COMMON_PHRASE)
    Observable<ResponseBean<PhraseResultBean>> requestPhraseData(@FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST(BIZConstants.Dmonkey.URL_GET_QA_PHRASE_VER)
    Observable<ResponseBean<QuickWordVersionBean>> requestPhraseQAVersion(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(BIZConstants.Dmonkey.URL_GET_QA_PHRASE_VER)
    Observable<String> requestPhraseQAVersionString(@FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST(BIZConstants.Dmonkey.URL_GET_COMMON_PHRASE)
    Observable<String> requestPhraseDataString(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(BIZConstants.Dmonkey.URL_GET_COMMON_QA)
    Observable<String> requestCommonQADataString(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(BIZConstants.Dmonkey.URL_SEND_DEV_APP_VER)
    Observable<String> sendInstallAppVersion(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("api/StatisticsSwag/statDialogue")
    Observable<String> sendDialogueStatData(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(BIZConstants.Dmonkey.URL_SEND_HEART_BEAT)
    Observable<ResponseBody> sendHeartbeat(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(BIZConstants.Dmonkey.URL_SEND_HUMAN_TRANSLATION_STAT_DATA)
    Observable<ResponseBody> sendHumanTranslationStatData(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(BIZConstants.Dmonkey.URL_SEND_LOCATION_DATA)
    Observable<String> sendLocationData(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(BIZConstants.Dmonkey.URL_SAVE_UPLOAD_RECORD_VIDEO_URL)
    Observable<String> saveUploadRecordVideo(@FieldMap Map<String, Object> params);

}
