package com.shentu.gamebox.http;

import com.shentu.gamebox.bean.HttpResult;

import java.io.Serializable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public class ResponseTransformer {
    public static <T>  ObservableTransformer<HttpResult<T>, T> handleResult(){
        return upstream -> upstream
                .onErrorResumeNext(new ErrorResumeFunction<T>())
                .flatMap(new ResponseFunction<T>());
    }


    private static class ErrorResumeFunction<T> implements Function<Throwable, ObservableSource<? extends HttpResult<T>>> {

        @Override
        public ObservableSource<? extends HttpResult<T>> apply(Throwable throwable) throws Exception {
            return Observable.error(CustomException.handleException(throwable));
        }
    }

    private static class ResponseFunction<T> implements Function<HttpResult<T>,ObservableSource<T>>{
        @Override
        public ObservableSource<T> apply(@NonNull HttpResult<T> tHttpResult) throws Exception {
            int code = tHttpResult.getRet();
            String message = tHttpResult.getMsg();
            if (code == 200){
                return Observable.just(tHttpResult.getData());
            }else{
                return Observable.error(new ApiException(code,message));
            }
        }
    }
}
