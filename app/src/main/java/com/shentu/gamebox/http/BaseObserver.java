package com.shentu.gamebox.http;



import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observer;

import io.reactivex.disposables.Disposable;

public abstract class BaseObserver<T> implements Observer<T> {

    private Disposable mDisposable;

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        mDisposable = d;
    }


    public abstract void onNext(@NotNull T t);

    @Override
    public void onError(@NonNull Throwable e) {
        /*隐藏进度框 显示提示消息*/
        e.printStackTrace();
    }

    @Override
    public void onComplete() {
        /*隐藏进度框*/
        DisposObserver();
    }

    private void DisposObserver() {
        try {
            if (mDisposable != null) {
                mDisposable.dispose();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
