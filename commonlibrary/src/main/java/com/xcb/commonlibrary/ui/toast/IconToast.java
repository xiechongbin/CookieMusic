package com.xcb.commonlibrary.ui.toast;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.xcb.commonlibrary.R;


/**
 * @author Sanchez
 * @date 2019/11/14 13:03
 * description:
 */
public class IconToast {

    public static void makeIcon(Context context, @DrawableRes int icon, @StringRes int text) {
        View view = createView(context, icon, text);
        createToast(context, view).show();
    }

    public static void makeIcon(Context context, @DrawableRes int icon, String text) {
        View view = createView(context, icon, text);
        createToast(context, view).show();
    }

    public static void makeSuccessful(Context context, String text) {
        makeIcon(context, R.drawable.ic_successful, text);
    }

    public static void makeSuccessful(Context context, @StringRes int text) {
        makeIcon(context, R.drawable.ic_successful, text);
    }

    public static void makeWarming(Context context, String text) {
        makeIcon(context, R.drawable.ic_warming, text);
    }

    public static void makeWarming(Context context, @StringRes int text) {
        makeIcon(context, R.drawable.ic_warming, text);
    }

    public static void makeText(Context context, String text) {
        View view = createView(context, text);
        view.findViewById(R.id.toastIcon).setVisibility(View.GONE);
        createToast(context, view).show();
    }

    public static void makeText(Context context, @StringRes int text) {
        View view = createView(context, text);
        view.findViewById(R.id.toastIcon).setVisibility(View.GONE);
        createToast(context, view).show();
    }


    private static View createView(Context context, @DrawableRes int icon, @StringRes int text) {
        View view = createView(context, text);
        ImageView imageView = view.findViewById(R.id.toastIcon);
        imageView.setImageResource(icon);
        return view;
    }

    private static View createView(Context context, @DrawableRes int icon, String text) {
        View view = createView(context, text);
        ImageView imageView = view.findViewById(R.id.toastIcon);
        imageView.setImageResource(icon);
        return view;
    }

    private static View createView(Context context, @StringRes int text) {
        View view = LayoutInflater.from(context).inflate(R.layout.toast_with_icon, null);
        TextView textView = view.findViewById(R.id.toastText);
        textView.setText(text);
        return view;
    }

    private static View createView(Context context, String text) {
        View view = LayoutInflater.from(context).inflate(R.layout.toast_with_icon, null);
        TextView textView = view.findViewById(R.id.toastText);
        textView.setText(text);
        return view;
    }

    private static Toast createToast(Context context, View view) {
        Toast toast = new Toast(context);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        return toast;
    }
}
