package com.kishannareshpal.superdialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;

public class
SuperDialog extends DialogFragment {

    public static final int POSITIVE = 9;
    public static final int NEGATIVE = 10;
    public static final int CANCEL = 11;


    private static final int DEFAULT = -1;

    private boolean isShown = false;
    private Context ctx;
    SuperDialog superDialog;

    // Components
    private AnimatedIcon ai_animatedIcon;
    private MaterialButton btn_positive, btn_negative, btn_cancel;
    private TextView tv_title, tv_message;
    private ImageView iv_icon;
    private ScrollView sv_scrollMessage;
    private FadingEdgeLayout fel_fadingEdge;
    private View v_scrollIndicator_top, v_scrollIndicator_bottom;

    // Setters
    private IconMode iconMode = IconMode.NO_ICON; // No Icon.
    private int gravity = DEFAULT; // Centered.
    @DrawableRes private int customIconRes = DEFAULT; // None.
    @ColorRes private int positiveTextColorRes = DEFAULT; // White.
    @ColorRes private int negativeTextColorRes = DEFAULT; // White.
    @ColorRes private int cancelTextColorRes = DEFAULT; // White.
    @ColorRes private int positiveColorRes = DEFAULT; // Light Green.
    @ColorRes private int negativeColorRes = DEFAULT; // Light Green.
    @ColorRes private int cancelColorRes = DEFAULT; // Light Grey.
    private boolean isScrollable;
    private boolean isAllCaps; // false.
    private boolean cancelable = true;
    private String title; // null.
    private String message; // null.
    private String positiveText; // null.
    private String negativeText; // null.
    private String cancelText; // null.
    private Space space; // View.VISIBLE.
    private OnButtonClickListener onPositive, onNegative, onCancel;

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        isShown = false;
    }

    public interface OnButtonClickListener {
        void OnButtonClick(SuperDialog dialog, int whichButton);
    }

    public SuperDialog iconMode(IconMode iconMode) {
        this.iconMode = iconMode;
        if (isShown) changeIconMode(iconMode);
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
    public SuperDialog customIconRes(@DrawableRes int iconRes) {
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
        this.gravity = Gravity.CENTER;
        if (isShown) {
            changeMessage(message);
            changeMessageGravity(Gravity.CENTER);
        }
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
    public SuperDialog positiveTextColorRes(@ColorRes int positiveTextColorRes) {
        this.positiveTextColorRes = positiveTextColorRes;
        if (isShown && ctx != null) changePositiveTextColor(positiveTextColorRes);
        return this;
    }
    public SuperDialog negativeTextColorRes(@ColorRes int negativeTextColorRes) {
        this.negativeTextColorRes = negativeTextColorRes;
        if (isShown && ctx != null) changeNegativeTextColor(negativeTextColorRes);
        return this;
    }
    public SuperDialog cancelTextColorRes(@ColorRes int cancelTextColorRes) {
        this.cancelTextColorRes = cancelTextColorRes;
        if (isShown && ctx != null) changeCancelTextColor(cancelTextColorRes);
        return this;
    }
    public SuperDialog positiveColorRes(@ColorRes int positiveColorRes) {
        this.positiveColorRes = positiveColorRes;
        if (isShown && ctx != null) changePositiveColor(positiveColorRes);
        return this;
    }
    public SuperDialog negativeColorRes(@ColorRes int negativeColorRes) {
        this.negativeColorRes = negativeColorRes;
        if (isShown && ctx != null) changeNegativeColor(negativeColorRes);
        return this;
    }
    public SuperDialog cancelColorRes(@ColorRes int cancelColorRes) {
        this.cancelColorRes = cancelColorRes;
        if (isShown && ctx != null) changeNegativeColor(cancelColorRes);
        return this;
    }

    public boolean isScrollable() {
        return this.isScrollable;
    }


    /*
     * @source: https://stackoverflow.com/questions/14657490/how-to-properly-retain-a-dialogfragment-through-rotation
     */
    @Override
    public void onDestroyView() {
        Dialog dialog = getDialog();
        // handles https://code.google.com/p/android/issues/detail?id=17423
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
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
            Window window = getDialog().getWindow();
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.requestFeature(Window.FEATURE_NO_TITLE);
        }
        this.setRetainInstance(true);

        // INIT COMPONENTS
        fel_fadingEdge           = view.findViewById(R.id.fel_fadingEdge);
        sv_scrollMessage         = view.findViewById(R.id.sv_scrollMessage);
        v_scrollIndicator_top    = view.findViewById(R.id.v_scrollIndicator_top);
        v_scrollIndicator_bottom = view.findViewById(R.id.v_scrollIndicator_bottom);
        tv_title                 = view.findViewById(R.id.tv_title);
        tv_message               = view.findViewById(R.id.tv_message);
        iv_icon                  = view.findViewById(R.id.iv_icon);
        btn_positive             = view.findViewById(R.id.btn_positive);
        ai_animatedIcon          = view.findViewById(R.id.ai_animatedIcon);
        btn_negative             = view.findViewById(R.id.btn_negative);
        btn_cancel               = view.findViewById(R.id.btn_cancel);
        space                    = view.findViewById(R.id.space);

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
         * Button color setup
         */
        if (ctx != null) {
            // Setup Button Text Colors
            changePositiveTextColor(positiveTextColorRes);
            changeNegativeTextColor(negativeTextColorRes);
            changeCancelTextColor(cancelTextColorRes);

            // Setup Button Background Colors
            changePositiveColor(positiveColorRes);
            changeNegativeColor(negativeColorRes);
            changeCancelColor(cancelColorRes);
        }

        return view;
    }

    // Is the scrollview set to be scrollable at the momment??
    private void manageMessageScroll() {
        sv_scrollMessage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Here you can get the size :)
                View child = sv_scrollMessage.getChildAt(0);
                if (child != null) {
                    int childHeight = child.getHeight();
                    isScrollable = sv_scrollMessage.getHeight() < childHeight + sv_scrollMessage.getPaddingTop() + sv_scrollMessage.getPaddingBottom();
                    v_scrollIndicator_top.animate().alpha(0).start();
                    if (isScrollable) {
                        v_scrollIndicator_bottom.animate().alpha(1).start();
                    } else {
                        v_scrollIndicator_bottom.animate().alpha(0).start();
                        fel_fadingEdge.setFadeEdges(false, false, false, false);
                    }
                }
            }
        });

        sv_scrollMessage.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = sv_scrollMessage.getScrollY();

                if (scrollY > 0 && scrollY < sv_scrollMessage.getMaxScrollAmount()){
                    v_scrollIndicator_top.animate().alpha(1).setDuration(250).start();
                    v_scrollIndicator_bottom.animate().alpha(1).setDuration(250).start();

                    fel_fadingEdge.setFadeEdges(true, false, true, false);

                } else {
                    if (!sv_scrollMessage.canScrollVertically(1)) {
                        // bottom of scroll view
                        v_scrollIndicator_top.animate().alpha(1).setDuration(250).start();
                        v_scrollIndicator_bottom.animate().alpha(0).setDuration(250).start();

                        fel_fadingEdge.setFadeEdges(true, false, false, false);
                    }

                    if (!sv_scrollMessage.canScrollVertically(-1)) {
                        // top of scroll view
                        v_scrollIndicator_bottom.animate().alpha(1).setDuration(250).start();
                        v_scrollIndicator_top.animate().alpha(0).setDuration(250).start();

//                        v_scrollIndicator_bottom.setVisibility(View.VISIBLE);
                        fel_fadingEdge.setFadeEdges(false, false, true, false);
                    }
                }

            }
        });


    }


    private boolean isColorDark(@ColorRes int colorRes){
        return ColorUtils.calculateLuminance(ContextCompat.getColor(ctx, colorRes)) < 0.6F;
    }


    /**
     *  Setup Space between buttons
     */
    private void addSpaceBetweenButtons(){
        if (negativeText != null && positiveText != null){
            space.setVisibility(View.VISIBLE);
        }
    }


    /**
     * Setup Cancelable
     * @param cancelable
     */
    private void changeCancelable(boolean cancelable){
        setCancelable(cancelable);
    }


    /**
     * Setup Positive Button
     * @param positiveText
     */
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
    private void changePositiveTextColor(@ColorRes int positiveTextColorRes){
        if (positiveTextColorRes != DEFAULT) {
            btn_positive.setTextColor(ContextCompat.getColor(ctx, positiveTextColorRes));
        }
    }
    private void changePositiveColor(@ColorRes int positiveColorRes){
        if (positiveColorRes != DEFAULT) {
            btn_positive.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(ctx, positiveColorRes)));
            if (positiveTextColorRes == DEFAULT){
                int light_or_dark = isColorDark(positiveColorRes) ? Color.WHITE : Color.BLACK;
                btn_positive.setTextColor(light_or_dark);
            }
        }
    }


    /**
     * Setup Negative Button
     * @param negativeText
     */
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
    private void changeNegativeTextColor(@ColorRes int negativeTextColorRes){
        if (negativeTextColorRes != DEFAULT) {
            btn_negative.setTextColor(ContextCompat.getColor(ctx, negativeTextColorRes));
        }
    }
    private void changeNegativeColor(@ColorRes int negativeColorRes){
        if (negativeColorRes != DEFAULT) {
            btn_negative.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(ctx, negativeColorRes)));
            if (negativeTextColorRes == DEFAULT){
                int light_or_dark = isColorDark(negativeColorRes) ? Color.WHITE : Color.BLACK;
                btn_negative.setTextColor(light_or_dark);
            }
        }
    }


    /**
     * Setup Cancel Button
     * @param cancelText
     */
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
    private void changeCancelTextColor(@ColorRes int cancelTextColorRes){
        if (cancelTextColorRes != DEFAULT) {
            btn_cancel.setTextColor(ContextCompat.getColor(ctx, cancelTextColorRes));
        }
    }
    private void changeCancelColor(@ColorRes int cancelColorRes){
        if (cancelColorRes != DEFAULT) {
            btn_cancel.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(ctx, cancelColorRes)));
            if (cancelTextColorRes == DEFAULT){
                int light_or_dark = isColorDark(cancelColorRes) ? Color.WHITE : Color.BLACK;
                btn_cancel.setTextColor(light_or_dark);
            }
        }
    }


    /**
     * Setup Title
     * @param title
     */
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


    /**
     * Setup Message or Content
     * @param message
     */
    private void changeMessage(String message){
        if (message != null) {
            if (tv_message.getVisibility() != View.VISIBLE){
                tv_message.setVisibility(View.VISIBLE);
            }
            tv_message.setIncludeFontPadding(true);
            tv_message.setText(message);

            manageMessageScroll();

        } else {
            tv_message.setVisibility(View.GONE);
        }
    }
    public void changeMessageGravity(int gravity){
        if (message != null) {
            tv_message.setGravity(gravity);
        }
    }


    /**
     * Setup Icon
     * @param iconMode
     */
    private void changeIconMode(IconMode iconMode){
        if (iconMode == IconMode.CUSTOM_IMAGE || iconMode == IconMode.NO_ICON){
            ai_animatedIcon.setVisibility(View.GONE);

        } else {
            ai_animatedIcon.setVisibility(View.VISIBLE);
        }

        ai_animatedIcon.setMode(iconMode);
    }
    private void changeCustomIconRes(@DrawableRes int customIconRes){
        if (iconMode == IconMode.CUSTOM_IMAGE) {
            if (customIconRes != DEFAULT) {
                iv_icon.setVisibility(View.VISIBLE);
                iv_icon.setImageResource(customIconRes);
            }
        }
    }


    // And Finally, to show the dialog
    public void show(FragmentManager fm){
        isShown = true;
        show(fm, "");
    }
}
