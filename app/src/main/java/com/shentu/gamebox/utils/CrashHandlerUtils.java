package com.shentu.gamebox.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import android.os.Looper;
import android.util.Log;
import android.widget.Toast;


import com.google.gson.Gson;

import com.shentu.gamebox.http.RetrofitManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrashHandlerUtils implements Thread.UncaughtExceptionHandler {

    /**
     * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
     * Created by hk
     */

        String time;
        // 系统默认的UncaughtException处理类
        private Thread.UncaughtExceptionHandler mDefaultHandler;
        // CrashHandler实例
        private static CrashHandlerUtils INSTANCE = new CrashHandlerUtils();
        // 程序的Context对象
        private Context mContext;
        // 用来存储设备信息和异常信息
        private Map<String, String> infos = new LinkedHashMap<String, String>();
        // 用于格式化日期,作为日志文件名的一部分
        private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        /**
         * 程序崩溃报告根目录
         **/
//        private String path = Environment.getExternalStorageDirectory().getPath() + File.separator;
        /**
         * 程序崩溃报告上传服务器地址
         **/
        @SuppressWarnings("unused")
        private Gson gson = new Gson();
    private File logs;

    /**
         * 保证只有一个CrashHandler实例
         */
        private CrashHandlerUtils() {
        }

        /**
         * 获取CrashHandler实例 ,单例模式
         */
        public static CrashHandlerUtils getInstance() {
            return INSTANCE;
        }

        /**
         * 初始化
         *
         * @param context
         */
        public void init(Context context) {
            mContext = context;
            // 获取系统默认的UncaughtException处理器
            mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
            // 设置该CrashHandler为程序的默认处理器
            Thread.setDefaultUncaughtExceptionHandler(this);
        }

        /**
         * 当UncaughtException发生时会转入该函数来处理
         */
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            // if (!handleException(ex) && mDefaultHandler != null) {
            // // 如果用户没有处理则让系统默认的异常处理器来处理
            // mDefaultHandler.uncaughtException(thread, ex);
            // }
            if (!handleException(ex)) {
                if (mDefaultHandler != null) {
                    mDefaultHandler.uncaughtException(thread, ex);
                }
            } else {

//                /*退出jvm java虚拟机 释放内存 非零都是异常退出*/
//                System.exit(0);
//                // 退出程序
//                android.os.Process.killProcess(android.os.Process.myPid());

            }
        }

        /**
         * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
         *
         * @param ex
         * @return true:如果处理了该异常信息;否则返回false.
         */
        private boolean handleException(Throwable ex) {
            if (ex == null) {
                return true;
            }
//		// 使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, "很抱歉,程序出现异常", Toast.LENGTH_LONG)
						.show();
				Looper.loop();
			}
		}.start();
            // 收集设备参数信息
            collectDeviceInfo(mContext);
            // 保存日志文件到设备
            File file = saveCrashInfo2File(ex);
            // 保存日志文件到服务器
            setToServer(file);

            return true;
        }

        private void setToServer(File file) {

            if (file != null){
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("crashLogFile",file.getName(),requestBody);
            String descriptionString = "crashLog";
            RequestBody description = RequestBody.create(MediaType.parse("text/plain"), descriptionString);
            Call<ResponseBody> call = RetrofitManager.getApiService().uploadFile(description, body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
            }
        }

        /**
         * 收集设备参数信息
         *
         * @param ctx
         */
        public void collectDeviceInfo(Context ctx) {
            try {
                PackageManager pm = ctx.getPackageManager();
                PackageInfo pi = pm.getPackageInfo(
                        ctx.getPackageName(), 0);

                if (pi != null) {
                    infos = new LinkedHashMap<String, String>();
                    int labelRes = pi.applicationInfo.labelRes;
                    infos.put("appName", ctx.getResources().getString(labelRes));//软件名称
                    infos.put("appVersionName", pi.versionName);//软件版本名称
                    infos.put("appVersionCode", pi.versionCode + "");//软件版本号
//				String crashGps =  ApplicationData.crashGps;//当前设备所在经纬度
//                    infos.put("appGps",crashGps);
                }
                Field[] fields = Build.class.getDeclaredFields();
                for (Field field : fields) {
                    try {
                        field.setAccessible(true);
                        infos.put(field.getName(), field.get(null).toString());
                    } catch (Exception e) {
                    }
                }

            } catch (PackageManager.NameNotFoundException e) {
            }

        }

        /**
         * 保存错误信息到文件中
         *
         * @param ex
         * @return 返回文件名称, 便于将文件传送到服务器
         */
        private File saveCrashInfo2File(Throwable ex) {

            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, String> entry : infos.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                sb.append(key + "=" + value + "\n");
            }
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            printWriter.close();
            String result = writer.toString();
            sb.append(result);
            File file = null;
            try {
                logs = mContext.getExternalFilesDir("logs");
//			long timestamp = System.currentTimeMillis();
                time = formatter.format(new Date());
                String fileName = time + ".log";
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    file  = new File(logs,fileName);
                    if (!logs.exists()) {
                        logs.mkdirs();
                    }
                }
                //将崩溃日志写入文件中
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(sb.toString().getBytes());
                fos.close();

                return file;
            } catch (Exception e) {
                Log.e("LogCrashHandler", e.getMessage());
            }
            return null;
        }

    }
//    原文链接：https://blog.csdn.net/weixin_35959554/article/details/109688591

