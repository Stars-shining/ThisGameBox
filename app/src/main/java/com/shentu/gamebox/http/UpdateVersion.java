package com.shentu.gamebox.http;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.alibaba.fastjson.JSON;
import com.shentu.gamebox.BuildConfig;
import com.shentu.gamebox.R;
import com.shentu.gamebox.bean.UpdateBean;
import com.shentu.gamebox.utils.LogUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.shentu.gamebox.utils.Constant.INSTALLED;

public class UpdateVersion {

    private Context mContext;
    private long downloadLength = 0;
    private long contentLength = 0;
    private Disposable downDispoable;
    private Disposable mDisPosable;
    private final boolean forceUpdate = true;
    private boolean cancleFlag ;
    private String updateDescription;
    private int serverVersion;
    private URL apkUrl;
    private int clientVersion;

    public UpdateVersion(Context context) {
        mContext = context;
    }
    /*检测*/
    public void test(String url) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        emitter.onError(e);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String result = "";
                        if (response.body() != null){
                            result = response.body().string();
                        }else{
                            /*返回错误数据*/
                            return;
                        }
                        emitter.onNext(result);
                    }
                });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mDisPosable = d;
                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        if (s.isEmpty()){
                            return;
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    /*下载文件*/
    public  void downFile(String url) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {

                downApk(url, emitter);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        downDispoable = d;
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        //设置progressdialog 进度条进度
                        showDownloadDialog(integer);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        LogUtils.e(e.toString());
//                        Toast.makeText(mContext, "网络异常，请重新下载！", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onComplete() {
//                        Toast.makeText(mContext, "服务器异常，请重新下载！", Toast.LENGTH_SHORT).show();

                    }
                });
    }



    /*显示更新对话框*/
    public void showNoticeDialog(String reponse){
        UpdateBean updateBean = JSON.toJavaObject(JSON.parseObject(reponse),UpdateBean.class);
        updateDescription = updateBean.getVersionName();
        serverVersion = updateBean.getVersionCode();
        apkUrl = updateBean.getDownloadUrl();
        clientVersion = getVersionCode();
        //如果版本最新 不更新
        if (serverVersion <= clientVersion)
            return;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("发现新版本 ：" + serverVersion);
        builder.setMessage(updateDescription);
        builder.setPositiveButton("现在更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        if (!forceUpdate){
            builder.setNegativeButton("待会更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }


    /*进度条对话框*/
    public void showDownloadDialog( int integer){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("正在更新");
        View view = LayoutInflater.from(mContext).inflate(R.layout.mustupdata_progress, null);
        ProgressBar update_progress = view.findViewById(R.id.update_progress);
        update_progress.setProgress(integer);
        update_progress.incrementProgressBy((int) (downloadLength * 1.0f / contentLength * 100));

        builder.setView(view);

        /*如果是强制更新不显示  取消按钮*/
        if(!forceUpdate){
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    cancleFlag = false;
                }
            });
        }
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        if (contentLength == downloadLength) {
            dialog.dismiss();
        }
    }

    /*下载apk*/
    public void downApk(String url, ObservableEmitter<Integer> emitter) {
        Toast.makeText(mContext, "开始下载", Toast.LENGTH_SHORT).show();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //下载失败 断点续传
//                downApk(url,emitter);
                breakPoint(url, emitter);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.body() == null) {
                    //下载失败
                    breakPoint(url, emitter);

//                    downApk(url,emitter);
                }
                InputStream is = null;
                FileOutputStream fos = null;
                int len;
                try {
                    is = Objects.requireNonNull(response.body()).byteStream();
                    File file = createFile();
                    fos = new FileOutputStream(file);
                    byte[] bytes = new byte[1024];
                    long total = Objects.requireNonNull(response.body()).contentLength();
                    contentLength = total;
                    long sum =0;
                    while ((len = is.read(bytes)) != -1){
                        fos.write(bytes,0,len);
                        sum += len;
                        int progress = (int) (len * 1.0f / total *100);
                        /*下载中 更新进度*/
                        emitter.onNext(progress);
                        downloadLength = sum;
                    }
                    fos.flush();
                    LogUtils.e("下载完成");
                    //下载完成 安装apk
                    installApk(mContext,file);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if (is != null)
                        is.close();
                    if (fos != null)
                        fos.close();
                }
            }
        });
    }

    public int getVersionCode(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P){
            try {

                PackageManager packageManager = mContext.getPackageManager();
                return packageManager.getPackageInfo(mContext.getPackageName(),
                        PackageManager.GET_CONFIGURATIONS).versionCode;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            try {
                PackageManager packageManager = mContext.getPackageManager();
                return (int) packageManager.getPackageInfo(mContext.getPackageName(),
                        PackageManager.GET_CONFIGURATIONS).getLongVersionCode();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /*新建文件*/
    private File createFile() {
        String path = mContext.getExternalFilesDir("dir").getPath();
        File file = new File(path, mContext.getResources().getString(R.string.app_name)+".apk");
        if (file.exists()){
            file.delete();
        }
        try {
            file.createNewFile();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*断点续传*/
    public void breakPoint(String url, ObservableEmitter<Integer> emitter) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("RANGE", "bytes" + downloadLength + "_" + contentLength)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //下载失败
                breakPoint(url, emitter);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.body() == null) {
                    //下载失败
                    breakPoint(url, emitter);
                }
                InputStream is = null;
                RandomAccessFile randomFile = null;
                byte[] bytes = new byte[2048];
                int len;
                try {
                    is = response.body().byteStream();
                    String path = mContext.getExternalFilesDir("dir").getPath();
                    File file = new File(path, mContext.getResources().getString(R.string.app_name)+".apk");
                    randomFile = new RandomAccessFile(file, "rwd");
                    randomFile.seek(downloadLength);
                    long total = contentLength;
                    long sum = downloadLength;
                    while ((len = is.read(bytes)) != -1) {
                        randomFile.write(bytes, 0, len);
                        sum += len;
                        int prograss = (int) (sum * 1.0f / total * 100);
                        //下载进度 更新下载进度
                        emitter.onNext(prograss);
                        downloadLength = sum;
                    }
                    installApk(mContext, file);
                } catch (Exception e) {
                    e.printStackTrace();
                    breakPoint(url, emitter);
                } finally {
                    try {
                        if (is != null)
                            is.close();
                        if (randomFile != null)
                            randomFile.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void installApk(Context mContext, File file) {
        if (mContext == null) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION|
                Intent.FLAG_GRANT_READ_URI_PERMISSION|
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri contentUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){

             contentUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".fileProvider", file);
        }else{
            contentUri = Uri.fromFile(file);
        }
        intent.setDataAndType(contentUri,"application/vnd.android.package-archive");
        List<ResolveInfo> resolveInfos = mContext.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo info:resolveInfos) {
            if (info != null && info.activityInfo != null){
                String packageName = info.activityInfo.packageName;
                mContext.grantUriPermission(packageName,contentUri,Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            }
        }
        mContext.startActivity(intent);
        INSTALLED = true;


        //弹出窗口把原程序关闭
        //避免安装完毕 点击打开没反应
//        Process.killProcess(Process.myPid());
    }
}
