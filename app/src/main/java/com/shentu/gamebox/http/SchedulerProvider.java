package com.shentu.gamebox.http;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class SchedulerProvider{

    private static  SchedulerProvider INSTSANCE ;

    public SchedulerProvider() {
    }

    public static synchronized SchedulerProvider getInstance(){
        if (INSTSANCE == null){
            INSTSANCE = new SchedulerProvider();
        }
        return INSTSANCE;
    }

    public Scheduler computation(){
        return Schedulers.computation();
    }


    public Scheduler io(){
        return Schedulers.io();
    }

    public Scheduler ui(){
        return AndroidSchedulers.mainThread();
    }



    public <T> ObservableTransformer<T,T> applySchedulers(){
        return new ObservableTransformer<T, T>() {
            @NonNull
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream.subscribeOn(io())
                        .observeOn(ui());
            }
        };
    }

}
