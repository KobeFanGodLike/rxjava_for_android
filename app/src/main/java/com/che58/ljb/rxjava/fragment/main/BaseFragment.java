package com.che58.ljb.rxjava.fragment.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.che58.ljb.rxjava.protocol2.TestProtocol;
import com.trello.rxlifecycle.components.support.RxFragment;

import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by wangmaobo on 2017/4/25 0025.
 */
public class BaseFragment extends RxFragment {
    public TestProtocol mTestProtocol;
    public Activity mActivity;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTestProtocol = new TestProtocol();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        mActivity = getActivity();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 建议放到baseFragment里面
     *
     * @param url    请求路径url
     * @param params 请求参数map集合
     * @param clazz  请求实体类
     */
    public <T> void send_post_request(String url, Map<String, Object> params, final Class<T>
            clazz) {
        mTestProtocol.test_post(url, params, clazz)
                .compose(this.<T>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<T>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        deal_post_error(throwable);
                    }

                    @Override
                    public void onNext(T t) {
                        late_init_UI(t);
                    }
                });
    }

    /**
     * 网络请求成功，子类重写此方法
     *
     * @param t
     * @param <T>
     */
    public <T> void late_init_UI(T t) {

    }

    /**
     * 网络请求失败，子类重写此方法
     *
     * @param throwable
     */
    public void deal_post_error(Throwable throwable) {

    }

}
