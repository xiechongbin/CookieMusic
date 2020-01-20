package com.xcb.cookiemusic.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.xcb.commonlibrary.utils.DensityUtil;
import com.xcb.cookiemusic.R;
import com.xcb.cookiemusic.login.LoginAndRegisterActivity;
import com.xcb.cookiemusic.utils.Constant;
import com.xcb.cookiemusic.utils.SharedPreferenceUtil;

/**
 * @author xcb
 * date：2020-01-16 16:51
 * description:协议同意对话框
 */
public class ProtocolAgreeFragmentDialog extends DialogFragment implements View.OnClickListener {
    private static final String TAG = "ProtocolAgreeFragmentDi";
    private TextView tv_agree;
    private TextView tv_disagree;
    private TextView tv_privacy_content;

    public static ProtocolAgreeFragmentDialog newInstance() {
        Bundle args = new Bundle();
        ProtocolAgreeFragmentDialog fragment = new ProtocolAgreeFragmentDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Activity activity = getActivity();
                if (activity != null) {
                    int width = DensityUtil.getScreenWidth(activity) * 600 / 720;
                    int height = DensityUtil.getScreenHeight(activity) * 700 / 1520;
                    WindowManager.LayoutParams lp = window.getAttributes();
                    lp.width = width;
                    lp.height = height;
                    // window.setAttributes(lp);
                    // window.setLayout(width, height);
                }
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmeng_dialog_protocol_agree, container);
        tv_agree = view.findViewById(R.id.tv_agree);
        tv_disagree = view.findViewById(R.id.tv_disagree);
        tv_privacy_content = view.findViewById(R.id.tv_privacy_content);

        tv_agree.setOnClickListener(this);
        tv_disagree.setOnClickListener(this);

        initRichText();
        return view;
    }

    /**
     * 初始化富文本
     */
    private void initRichText() {
        CharSequence charSequence = getString(R.string.protocol_content);
        SpannableString spannableString = new SpannableString(charSequence);
        spannableString.setSpan(new URLSpan(Constant.URL_TERMS_OF_SERVICE), 33, 38, SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableString.setSpan(new URLSpan(Constant.URL_PRIVACY_POLICY), 40, 45, SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableString.setSpan(new URLSpan(Constant.URL_CHILD_PRIVACY_POLICY), 71, 79, SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#ff507daf")),
                33, 38, SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#ff507daf")),
                40, 45, SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#ff507daf")),
                71, 79, SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);
        tv_privacy_content.setText(spannableString);
        tv_privacy_content.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_disagree) {
            if (getActivity() != null) {
                getActivity().finish();
            }
        } else if (v.getId() == R.id.tv_agree) {
            SharedPreferenceUtil.getInstance().putBoolean(Constant.SP_KEY_HAS_AGREE, true).apply();
            boolean showLoginPage = SharedPreferenceUtil.getInstance().getBoolean(Constant.SP_KEY_FIRST_OPEN_SHOW_LOGIN_PAGE, false);
            if (!showLoginPage) {

            }
            LoginAndRegisterActivity.startActivity(this.getContext());
        }
    }
}
