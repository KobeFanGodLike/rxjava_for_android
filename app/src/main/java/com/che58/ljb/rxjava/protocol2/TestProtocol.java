package com.che58.ljb.rxjava.protocol2;

import com.che58.ljb.rxjava.model.DeleteModel;
import com.che58.ljb.rxjava.model.GetModel;
import com.che58.ljb.rxjava.model.PutModel;
import com.che58.ljb.rxjava.net.XgoHttpClient;

import java.util.Map;

import rx.Observable;

/**
 * 测试接口
 * Created by ljb on 2016/3/23.
 */
public class TestProtocol extends BaseProtocol {

    private static final String BASE_URL = "http://service.test.xgo.com.cn:8080/app/v1/demo/";

    /**
     * Get请求
     */
    public Observable<GetModel> text_Get() {
        String path = "1";
        return createObservable(BASE_URL + path, XgoHttpClient.METHOD_GET, null, GetModel.class);
    }


    /**
     * Post请求
     *
     * @param url    请求路径url
     * @param params 请求参数map集合
     * @param clazz  请求实体类
     * @return
     */
    public <T> Observable<T> test_post(String url, Map<String, Object> params, final Class<T>
            clazz) {
        return createObservable(url, XgoHttpClient.METHOD_POST, params, clazz);
    }

    /**
     * Put请求
     */
    public Observable<PutModel> text_Put(Map<String, Object> params) {
        return createObservable(BASE_URL, XgoHttpClient.METHOD_PUT, params, PutModel.class);
    }

    /**
     * Delete请求
     */
    public Observable<DeleteModel> text_Delete() {
        String path = "1";
        return createObservable(BASE_URL + path, XgoHttpClient.METHOD_DELETE, null, DeleteModel
                .class);
    }

}
