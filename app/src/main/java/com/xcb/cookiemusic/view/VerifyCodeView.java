package com.xcb.cookiemusic.view;

import android.content.Context;
import android.os.ResultReceiver;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.xcb.cookiemusic.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xcb
 * date：2020-01-20 18:19
 * description:验证码输入view
 */
public class VerifyCodeView extends LinearLayout implements TextWatcher, View.OnKeyListener {
    private TextView tv_code1;
    private TextView tv_code2;
    private TextView tv_code3;
    private TextView tv_code4;
    private EditText et_code;
    private InputMethodManager im;
    private List<String> codes = new ArrayList<>();

    public VerifyCodeView(Context context) {
        super(context);
        init(context);
    }

    public VerifyCodeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VerifyCodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        im = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_verify_code_view, this);
        tv_code1 = view.findViewById(R.id.tv_code1);
        tv_code2 = view.findViewById(R.id.tv_code2);
        tv_code3 = view.findViewById(R.id.tv_code3);
        tv_code4 = view.findViewById(R.id.tv_code4);
        et_code = view.findViewById(R.id.et_code);

        et_code.addTextChangedListener(this);
        et_code.setOnKeyListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable != null && editable.length() > 0) {
            Log.d("advg", editable.toString());
            et_code.setText("");
            if (codes.size() < 4) {
                codes.add(editable.toString());
                showCode();
            }
        }
    }

    // 监听验证码删除按键
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN && codes.size() > 0) {
            codes.remove(codes.size() - 1);
            showCode();
            return true;
        }
        return false;
    }

    /**
     * 显示输入的验证码
     */
    private void showCode() {
        String code1 = "";
        String code2 = "";
        String code3 = "";
        String code4 = "";
        if (codes.size() >= 1) {
            code1 = codes.get(0);
        }
        if (codes.size() >= 2) {
            code2 = codes.get(1);
        }
        if (codes.size() >= 3) {
            code3 = codes.get(2);
        }
        if (codes.size() >= 4) {
            code4 = codes.get(3);
        }
        tv_code1.setText(code1);
        tv_code2.setText(code2);
        tv_code3.setText(code3);
        tv_code4.setText(code4);

        setColor();//设置高亮颜色
        callBack();//回调
    }

    /**
     * 设置高亮颜色
     */
    private void setColor() {
        tv_code1.setBackgroundResource(R.drawable.shape_bottom_line_normal);
        tv_code2.setBackgroundResource(R.drawable.shape_bottom_line_normal);
        tv_code3.setBackgroundResource(R.drawable.shape_bottom_line_normal);
        tv_code4.setBackgroundResource(R.drawable.shape_bottom_line_normal);

        if (codes.size() == 0) {
            tv_code1.setBackgroundResource(R.drawable.shape_bottom_line_normal);
            tv_code2.setBackgroundResource(R.drawable.shape_bottom_line_normal);
            tv_code3.setBackgroundResource(R.drawable.shape_bottom_line_normal);
            tv_code4.setBackgroundResource(R.drawable.shape_bottom_line_normal);
        }
        if (codes.size() == 1) {
            tv_code1.setBackgroundResource(R.drawable.shape_bottom_line_input);
            tv_code2.setBackgroundResource(R.drawable.shape_bottom_line_normal);
            tv_code3.setBackgroundResource(R.drawable.shape_bottom_line_normal);
            tv_code4.setBackgroundResource(R.drawable.shape_bottom_line_normal);
        }
        if (codes.size() == 2) {
            tv_code1.setBackgroundResource(R.drawable.shape_bottom_line_input);
            tv_code2.setBackgroundResource(R.drawable.shape_bottom_line_input);
            tv_code3.setBackgroundResource(R.drawable.shape_bottom_line_normal);
            tv_code4.setBackgroundResource(R.drawable.shape_bottom_line_normal);
        }
        if (codes.size() == 3) {
            tv_code1.setBackgroundResource(R.drawable.shape_bottom_line_input);
            tv_code2.setBackgroundResource(R.drawable.shape_bottom_line_input);
            tv_code3.setBackgroundResource(R.drawable.shape_bottom_line_input);
            tv_code4.setBackgroundResource(R.drawable.shape_bottom_line_normal);
        }
        if (codes.size() == 4) {
            tv_code1.setBackgroundResource(R.drawable.shape_bottom_line_input);
            tv_code2.setBackgroundResource(R.drawable.shape_bottom_line_input);
            tv_code3.setBackgroundResource(R.drawable.shape_bottom_line_input);
            tv_code4.setBackgroundResource(R.drawable.shape_bottom_line_input);
        }
    }

    /**
     * 回调
     */
    private void callBack() {
        if (onInputListener == null) {
            return;
        }
        if (codes.size() == 4) {
            onInputListener.onSuccess(getPhoneCode());
        } else {
            onInputListener.onInput();
        }
    }

    //定义回调
    public interface OnInputListener {
        void onSuccess(String code);

        void onInput();
    }

    private OnInputListener onInputListener;

    public void setOnInputListener(OnInputListener onInputListener) {
        this.onInputListener = onInputListener;
    }

    /**
     * 显示键盘
     */
    public void showSoftInput() {
        //显示软键盘
        if (im != null && et_code != null) {
            et_code.postDelayed(new Runnable() {
                @Override
                public void run() {
                    im.showSoftInput(getRootView(), 0);
                }
            }, 200);
        }
    }

    /**
     * 隐藏键盘
     */
    public void hideSoftInput() {
        //显示软键盘
        if (im != null && et_code != null) {
            et_code.postDelayed(new Runnable() {
                @Override
                public void run() {
                    im.hideSoftInputFromWindow(getWindowToken(), 0);
                }
            }, 200);
        }
    }

    /**
     * 获得手机号验证码
     *
     * @return 验证码
     */
    public String getPhoneCode() {
        StringBuilder sb = new StringBuilder();
        for (String code : codes) {
            sb.append(code);
        }
        return sb.toString();
    }

}
