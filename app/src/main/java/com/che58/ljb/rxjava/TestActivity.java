package com.che58.ljb.rxjava;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.che58.ljb.rxjava.model.Course;
import com.che58.ljb.rxjava.model.Student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class TestActivity extends AppCompatActivity {
    private static final String tag = "Test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_activity);
        /*RxJava-Asynctask*/
        testFrom();
        testPic();
        testJustAndSchedual();
        test_changed_map();
        test_changed_flatMap();
        test_changed_lift();
        test_doOnSubscribe();
        big_test_search();
        big_test_rx_android();
    }

    private void big_test_rx_android() {

    }

    private void big_test_search() {
        query("www")
                .flatMap(new Func1<List<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(List<String> list) {
                        return Observable.from(list);
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        return getTitle(s);
                    }
                })
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return s != "404";
                    }
                })
                .take(3)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e(tag, s);
                    }
                });

    }

    private Observable<String> getTitle(String s) {
        if (s.startsWith("www.baidu")) {
            return Observable.just("百度");
        } else if (s.startsWith("www.sina")) {
            return Observable.just("新浪");
        } else {
            return Observable.just("404");
        }
    }

    public Observable<List<String>> query(String str) {
        List<String> list = new ArrayList<>();
        if (str.contains("www")) {
            list = Arrays.asList("www.baidu.com", "www.sina.com", "www.360.cn");
        }
        return Observable.just(list);
    }

    private void test_doOnSubscribe() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("123");
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showUI();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.e(tag, s);
                    }
                });
    }

    private void showUI() {

    }

    /**
     * 变幻原理：lift
     */
    private void test_changed_lift() {
        Observable observable = Observable.from(new String[]{"1", "23"});
        observable.lift(new Observable.Operator<String, Integer>() {

            @Override
            public Subscriber<? super Integer> call(final Subscriber<? super String> subscriber) {
                return new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        subscriber.onError(throwable);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        subscriber.onNext("" + integer);
                    }
                };
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.e(tag, integer.intValue() + "=========");
            }
        });
    }

    /**
     * 遍历学生课程：一对多
     */
    private void test_changed_flatMap() {
        List<Course> mList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int random = (int) (Math.random() * 10 + 1);
            if (random < 3) {
                mList.add(new Course("语文" + random));
            } else if (random < 6) {
                mList.add(new Course("英语" + random));
            } else {
                mList.add(new Course("数学" + random));
            }
        }
        final Student[] students = {new Student("tom", mList), new Student
                ("jerry", mList)};
        Observable.from(students).flatMap(new Func1<Student, Observable<Course>>() {
            @Override
            public Observable<Course> call(Student student) {
                return Observable.from(student.course);
            }
        }).subscribe(new Subscriber<Course>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onNext(Course course) {
                Log.e(tag, course.type);
            }
        });
    }

    /**
     * map()
     */
    private void test_changed_map() {
        Observable.just("images/logo.png")
                .map(new Func1<String, Bitmap>() {
                    @Override
                    public Bitmap call(String s) {
                        return getBitmapFromPath(s);
                    }
                })
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        showBitmap(bitmap);
                    }
                });
    }

    private void showBitmap(Bitmap bitmap) {

    }

    private Bitmap getBitmapFromPath(String s) {
        return null;
    }

    /**
     * 『后台线程取数据，主线程显示』
     */
    private void testJustAndSchedual() {
        Observable.just(1, 2, 3, 4)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.e(tag, "number:" + integer);
                    }
                });
    }

    /**
     * iv显示pic,读入图片参考异步
     */
    private void testPic() {
        final int drawableRes = R.mipmap.ic_launcher;
        final ImageView imageView = (ImageView) findViewById(R.id.iv);
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = getResources().getDrawable(drawableRes);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        }).subscribe(new Observer<Drawable>() {
            @Override
            public void onNext(Drawable drawable) {
                imageView.setImageDrawable(drawable);
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(TestActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 自定义注册事件
     */
    private void testFrom() {
        String[] names = {"tom", "jerry", "john"};
        Observable.from(names).subscribe(new Action1<String>() {
            @Override
            public void call(String name) {
                Log.e(tag, name);
            }
        });
    }

}
