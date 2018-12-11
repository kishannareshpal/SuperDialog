package com.kishannareshpal.superdialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

public class SuperDialog extends DialogFragment {

    public static final int POSITIVE = 9;
    public static final int NEGATIVE = 10;
    public static final int CANCEL = 11;

    public static final int CUSTOM_ICON   = 0;
    public static final int PROGRESS_ICON = 1;
    public static final int SUCCESS_ICON  = 2;
    public static final int ERROR_ICON    = 3;

    private static final int DEFAULT = -1;

    private boolean isShown = false;
    private Context ctx;
    SuperDialog superDialog;

    // Components
    private AnimatedIcon ai_animatedIcon;
    private MaterialButton btn_positive, btn_negative, btn_cancel;
    private TextView tv_title, tv_message;
    private ImageView iv_icon;

    // Setters
    private int iconMode = CUSTOM_ICON;
    private int gravity = DEFAULT;
    private int customIconRes = DEFAULT;
    private int positiveTextColorRes = DEFAULT;
    private int negativeTextColorRes = DEFAULT;
    private int cancelTextColorRes = DEFAULT;
    private int positiveColorRes = DEFAULT;
    private int negativeColorRes = DEFAULT;
    private int cancelColorRes = DEFAULT;
    private boolean isAllCaps;
    private boolean cancelable = true;
    private String title;
    private String message;
    private String positiveText;
    private String negativeText;
    private String cancelText;
    private Space space;
    private OnButtonClickListener onPositive, onNegative, onCancel;

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        isShown = false;
    }

    public interface OnButtonClickListener {
        void OnButtonClick(SuperDialog dialog, int whichButton);
    }

    public SuperDialog iconMode(int mode) {
        this.iconMode = mode;
        if (isShown) changeIconMode(mode);
        return this;
    }
    public SuperDialog cancelable(boolean cancelable){
        this.cancelable = cancelable;
        if (isShown) changeCancelable(cancelable);
        return this;
    }
    public SuperDialog positiveText(String positiveText) {
        this.positiveText = positiveText;
        if (isShown) changePositiveText(positiveText);
        return this;
    }
    public SuperDialog negativeText(String negativeText) {
        this.negativeText = negativeText;
        if (isShown) changeNegativeText(negativeText);
        return this;
    }
    public SuperDialog cancelText(String cancelText) {
        this.cancelText = cancelText;
        if (isShown) changeCancelText(cancelText);
        return this;
    }
    public SuperDialog onPositive(OnButtonClickListener onPositive) {
        this.onPositive = onPositive;
        if (isShown) changeOnPositive(onPositive);
        return this;
    }
    public SuperDialog onNegative(OnButtonClickListener onNegative) {
        this.onNegative = onNegative;
        if (isShown) changeOnNegative(onNegative);
        return this;
    }
    public SuperDialog onCancel(OnButtonClickListener onCancel) {
        this.onCancel = onCancel;
        if (isShown) changeOnCancel(onCancel);
        return this;
    }
    public SuperDialog customIconRes(int iconRes) {
        this.customIconRes = iconRes;
        if (isShown) changeCustomIconRes(iconRes);
        return this;
    }
    public SuperDialog title(String title) {
        this.title = title;
        if (isShown) changeTitle(title);
        return this;
    }
    public SuperDialog title(String title, boolean isAllCaps) {
        this.title = title;
        this.isAllCaps = isAllCaps;
        if (isShown) {
            changeTitle(title);
            changeTitleCaps(isAllCaps);
        }
        return this;
    }
    public SuperDialog message(String message) {
        this.message = message;
        if (isShown) changeMessage(message);
        return this;
    }
    public SuperDialog message(String message, int gravity) {
        this.message = message;
        this.gravity = gravity;

        if (isShown) {
            changeMessage(message);
            changeMessageGravity(gravity);
        }
        return this;
    }
    public SuperDialog positiveTextColorRes(int positiveTextColorRes) {
        this.positiveTextColorRes = positiveTextColorRes;
        if (isShown && ctx != null) changePositiveTextColor(positiveTextColorRes);
        return this;
    }
    public SuperDialog negativeTextColorRes(int negativeTextColorRes) {
        this.negativeTextColorRes = negativeTextColorRes;
        if (isShown && ctx != null) changeNegativeTextColor(negativeTextColorRes);
        return this;
    }
    public SuperDialog cancelTextColorRes(int cancelTextColorRes) {
        this.cancelTextColorRes = cancelTextColorRes;
        if (isShown && ctx != null) changeCancelTextColor(cancelTextColorRes);
        return this;
    }
    public SuperDialog positiveColorRes(int positiveColorRes) {
        this.positiveColorRes = positiveColorRes;
        if (isShown && ctx != null) changePositiveColor(positiveColorRes);
        return this;
    }
    public SuperDialog negativeColorRes(int negativeColorRes) {
        this.negativeColorRes = negativeColorRes;
        if (isShown && ctx != null) changeNegativeColor(negativeColorRes);
        return this;
    }
    public SuperDialog cancelColorRes(int cancelColorRes) {
        this.cancelColorRes = cancelColorRes;
        if (isShown && ctx != null) changeNegativeColor(cancelColorRes);
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.superdialog_main, container, false);

        // Init Utils
        this.ctx = getActivity();
        this.superDialog = this;

        // Set transparent background and remove stock title decoration views.. so the round corners bg shows.
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        // INIT COMPONENTS
        tv_title        = view.findViewById(R.id.tv_title);
        tv_message      = view.findViewById(R.id.tv_message);
        iv_icon         = view.findViewById(R.id.iv_icon);
        btn_positive    = view.findViewById(R.id.btn_positive);
        ai_animatedIcon = view.findViewById(R.id.ai_animatedIcon);
        btn_negative    = view.findViewById(R.id.btn_negative);
        btn_cancel      = view.findViewById(R.id.btn_cancel);
        space           = view.findViewById(R.id.space);

        // Setup Icon
        changeIconMode(iconMode);
        changeCustomIconRes(customIconRes);

        // Setup Title
        changeTitle(title);
        changeTitleCaps(isAllCaps);

        // Setup Message (a.k.a. Content)
        changeMessage(message);
        changeMessageGravity(gravity);

        // Add space between positive and negative button when both are visible.
        addSpaceBetweenButtons();

        // Setup Positive Button
        changePositiveText(positiveText);
        changeOnPositive(onPositive);

        // Setup Negative Button
        changeNegativeText(negativeText);
        changeOnNegative(onNegative);

        // Setup Cancel Button
        changeCancelText(cancelText);
        changeOnCancel(onCancel);


        /**
         * Colors
         */
        if (ctx != null) {
            // Setup Button Text Colors
            changePositiveTextColor(positiveTextColorRes);
            changeNegativeTextColor(negativeTextColorRes);
            changeCancelTextColor(negativeTextColorRes);

            // Setup Button Background Colors
            changePositiveColor(positiveColorRes);
            changeNegativeColor(negativeColorRes);
            changeCancelColor(cancelColorRes);
        }

        return view;
    }



    // Setup Space between buttons
    private void addSpaceBetweenButtons(){
        if (negativeText != null && positiveText != null){
            space.setVisibility(View.VISIBLE);
        }
    }

    // Setup Cancelable
    private void changeCancelable(boolean cancelable){
        setCancelable(cancelable);
    }

    // Setup Positive Button
    private void changePositiveText(String positiveText){
        if (positiveText != null){
            btn_positive.setVisibility(View.VISIBLE);
            btn_positive.setText(positiveText);

            if (negativeText != null){
                space.setVisibility(View.VISIBLE);
            } else {
                space.setVisibility(View.GONE);
            }

        } else {
            btn_positive.setVisibility(View.GONE);
        }
    }
    private void changeOnPositive(final OnButtonClickListener onPositive){
        if (positiveText != null) {
            if (onPositive != null) {
                btn_positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onPositive.OnButtonClick(superDialog, POSITIVE);
                        if (cancelable){
                            getDialog().dismiss();
                        }
                    }
                });

            } else {
                btn_positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getDialog().dismiss();
                    }
                });
            }
        }
    }
    private void changePositiveTextColor(int positiveTextColorRes){
        if (positiveTextColorRes != DEFAULT) {
            btn_positive.setTextColor(ContextCompat.getColor(ctx, positiveTextColorRes));
        }
    }
    private void changePositiveColor(int positiveColorRes){
        if (positiveColorRes != DEFAULT) {
            btn_positive.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(ctx, positiveColorRes)));
        }
    }


    // Setup Negative Button
    private void changeNegativeText(String negativeText){
        if (negativeText != null){
            btn_negative.setVisibility(View.VISIBLE);
            btn_negative.setText(negativeText);

            if (positiveText != null){
                space.setVisibility(View.VISIBLE);
            } else {
                space.setVisibility(View.GONE);
            }

        } else {
            btn_negative.setVisibility(View.GONE);
        }
    }
    private void changeOnNegative(final OnButtonClickListener onNegative){
        if (negativeText != null) {
            if (onNegative != null) {
                btn_negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onNegative.OnButtonClick(superDialog, NEGATIVE);
                        if (cancelable) {
                            getDialog().dismiss();
                        }
                    }
                });

            } else {
                btn_negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getDialog().dismiss();
                    }
                });
            }
        }
    }
    private void changeNegativeTextColor(int negativeTextColorRes){
        if (negativeTextColorRes != DEFAULT) {
            btn_negative.setTextColor(ContextCompat.getColor(ctx, negativeTextColorRes));
        }
    }
    private void changeNegativeColor(int negativeColorRes){
        if (negativeColorRes != DEFAULT) {
            btn_negative.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(ctx, negativeColorRes)));
        }
    }


    // Setup Cancel Button
    private void changeCancelText(String cancelText){
        if (cancelText != null){
            btn_cancel.setVisibility(View.VISIBLE);
            btn_cancel.setText(cancelText);
        } else {
            btn_cancel.setVisibility(View.GONE);
        }
    }
    private void changeOnCancel(final OnButtonClickListener onCancel){
        if (cancelText != null) {
            if (onCancel != null) {
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCancel.OnButtonClick(superDialog, CANCEL);
                        if (cancelable) {
                            getDialog().dismiss();
                        }
                    }
                });

            } else {
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getDialog().dismiss();
                    }
                });
            }
        }
    }
    private void changeCancelTextColor(int cancelTextColorRes){
        if (cancelTextColorRes != DEFAULT) {
            btn_cancel.setTextColor(ContextCompat.getColor(ctx, cancelTextColorRes));
        }
    }
    private void changeCancelColor(int cancelColorRes){
        if (cancelColorRes != DEFAULT) {
            btn_cancel.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(ctx, cancelColorRes)));
        }
    }

    // Setup Title
    private void changeTitle(String title){
        if (title != null){
            tv_title.setVisibility(View.VISIBLE);
            tv_title.setText(title);

        } else {
            tv_title.setVisibility(View.GONE);
        }
    }
    public void changeTitleCaps(boolean isAllCaps){
        if (title != null) {
            tv_title.setAllCaps(isAllCaps);

        } else {
            tv_title.setVisibility(View.GONE);
        }
    }

    // Setup Message or Content
    private void changeMessage(String message){
        if (message != null) {
            tv_message.setVisibility(View.VISIBLE);
            tv_message.setText(message);

        } else {
            tv_message.setVisibility(View.GONE);
        }
    }
    public void changeMessageGravity(int gravity){
        if (message != null) {
            tv_message.setGravity(gravity);
        }
    }

    // Setup Icon
    private void changeIconMode(int iconMode){
        if (iconMode == CUSTOM_ICON){
            ai_animatedIcon.setVisibility(View.GONE);
            ai_animatedIcon.setMode(CUSTOM_ICON);

        } else {
            ai_animatedIcon.setVisibility(View.VISIBLE);
            ai_animatedIcon.setMode(iconMode);
        }
    }
    private void changeCustomIconRes(int customIconRes){
        if (iconMode == CUSTOM_ICON) {
            if (customIconRes != DEFAULT) {
                iv_icon.setVisibility(View.VISIBLE);
                iv_icon.setImageResource(customIconRes);
            }
        }
    }


    // Finally
    public void show(FragmentManager fm){
        isShown = true;
        show(fm, "");
    }
}
