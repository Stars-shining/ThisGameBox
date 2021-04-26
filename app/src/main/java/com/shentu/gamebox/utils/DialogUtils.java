package com.shentu.gamebox.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import com.shentu.gamebox.R;

public class DialogUtils {
    public DialogUtils() {
    }

    public static void getDialog(Context context, View v) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

//        v.setPadding(30,30,30,0);

        dialog.setView(v);
        AlertDialog alertDialog = dialog.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        float scale = context.getResources().getDisplayMetrics().density;
        lp.height = (int) (90 * scale / 0.5f);
        lp.width = (int) (150 * scale / 0.5f);
        alertDialog.getWindow().setAttributes(lp);
        ImageView closeImg = v.findViewById(R.id.close);
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    public static void bigImageDialog(Context context, View view, String imgUrl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ImageView bigImage = view.findViewById(R.id.show_bigimage);

        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
        Glide.with(context).load(imgUrl).into(bigImage);
        Window window = alertDialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialog.getWindow().setAttributes(lp);

        bigImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    public static void showHtmlDialog(Context context, View view, String html) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        WebView webView = view.findViewById(R.id.web_view);
//        TextView rebackText = view.findViewById(R.id.reback_text);
        ImageView close_dialog = view.findViewById(R.id.close_dialog);
//        if (index == 0) {
//            rebackText.setVisibility(View.GONE);
//            builder.setView(view);
            webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
//
//        } else {
//            webView.setVisibility(View.GONE);
//            rebackText.setText(html);
            builder.setView(view);
//        }

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
        Window window = alertDialog.getWindow();
//        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.getDecorView().setPadding(30, 30, 30, 30);
        window.setGravity(Gravity.CENTER);
        float scale = context.getResources().getDisplayMetrics().density;
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.height = (int) (250 * scale / 0.5f);
        lp.width = (int) (180 * scale / 0.5f);

        alertDialog.getWindow().setAttributes(lp);

//        webView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//            }
//        });
//        rebackText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//            }
//        });
        close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }
}
