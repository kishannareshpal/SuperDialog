package com.kishannareshpal.superdialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.appcompat.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;

public class SuperDialog extends DialogFragment {

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
    private AppCompatCheckBox accb_checkbox;
    private TextInputEditText et_text;
    private TextInputLayout til;

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
    private boolean isCheckable; // if the dialog have a checkbox.
    private boolean isChecked; // if the dialog's checkbox is checked.
    private boolean isScrollable;
    private boolean isAllCaps; // false.
    private boolean isPrompt; // false
    private boolean cancelable = true;
    private String checkboxText; // null
    private String title; // null.
    private String message; // null.
    private String positiveText; // null.
    private String negativeText; // null.
    private String cancelText; // null.
    private Space space; // View.VISIBLE.
    private OnButtonClickListener onPositive, onNegative, onCancel;
    private OnTextInputListener onTextInputListener;

    private String promptHint;
    private String promptText;
    private String promptHelperText;
    private String promptErrorText;
    private int promptTextInputType = InputType.TYPE_CLASS_TEXT;
    @ColorRes int promptHelperTextColor = DEFAULT;
    @ColorRes int promptErrorTextColor = DEFAULT;
    private int promptLines = 1; // number of lines
    private int promptMaxTextLength = 90;
    private boolean isPromptTextCountEnabled; // false;
    private boolean isPromptHelperEnabled; // false
    private boolean isPromptErrorEnabled; // false

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        isShown = false;
    }

    public interface OnButtonClickListener {
        void OnButtonClick(SuperDialog superDialog, SuperDialogButtonType whichButton);
    }

    public interface OnTextInputListener {
        void OnTextInput(SuperDialog superDialog, String text);
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
    public SuperDialog checkable(boolean isCheckable, boolean isCheckedByDefault){
        this.isCheckable = isCheckable;
        this.isChecked = isCheckedByDefault;
        if (isShown) changeCheckable(isCheckable);
        return this;
    }
    public SuperDialog checkboxState(boolean setChecked){
        this.isChecked = setChecked;
        if (isShown) {
            changeCheckboxState(setChecked);
        }
        return this;
    }
    public SuperDialog checkboxText(String checkboxText){
        this.checkboxText = checkboxText;
        if (isShown) {
            changeCheckboxText(checkboxText);
        }
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
            changeMessageGravity(this.gravity);
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


    public SuperDialog prompt() {
        this.isPrompt = true;

        if (isShown) {
            changePrompt(true, promptHint, promptText, promptLines);
        }
        return this;
    }

    public SuperDialog prompt(boolean isPrompt) {
        this.isPrompt = isPrompt;

        if (isShown) {
            changePrompt(isPrompt, promptHint, promptText, promptLines);
        }
        return this;
    }

    /**
     * Set the text input type of the prompt
     *
     * @param inputType must be a constant from InputType;
     * @return
     */
    public SuperDialog promptTextInputType(int inputType) {
        this.promptTextInputType = inputType;
        if (isShown) {
            changePromptTextInputType(inputType);
        }

        return this;
    }

    public SuperDialog promptMaxTextLength(int maxTextLength) {
        this.promptMaxTextLength = maxTextLength;
        if (isShown) {
            changePromptMaxTextLength(maxTextLength);
        }
        return this;
    }

    public SuperDialog prompt(boolean isPrompt, String hint, String defaultText, int lines) {
        this.isPrompt = isPrompt;
        this.promptHint = hint;
        this.promptText = defaultText;
        if (lines > 0) this.promptLines = lines;

        if (isShown) {
            changePrompt(isPrompt, hint, defaultText, lines);
        }
        return this;
    }

    public SuperDialog setPromptToFail(String errorText) {
        this.promptErrorText = errorText;

        if (isShown) {
            changePromptToFail(errorText);
        }
        return this;
    }

    public SuperDialog setPromptToHelp(String helperText) {
        this.promptHelperText = helperText;

        if (isShown) {
            changePromptHelper(helperText);
        }
        return this;
    }

    public SuperDialog resetPrompt() {
        if (isShown) {
            changePromptBackToNormal(false);
        }
        return this;
    }

    public SuperDialog resetPrompt(boolean errorsToo) {
        if (isShown) {
            changePromptBackToNormal(errorsToo);
        }
        return this;
    }

    public SuperDialog promptHelperTextColor(@ColorRes int promptHelperTextColor) {
        this.promptHelperTextColor = promptErrorTextColor;
        if (isShown && ctx != null) changePromptHelperTextColor(promptHelperTextColor);
        return this;
    }

    public void clearPromptText() {
        if (isShown) {
            et_text.setText(null);
        }
    }

    public String getPromptText() {
        Editable et = et_text.getText();
        return (et != null) ? et.toString() : null;
    }

    public SuperDialog onPromptTextChanged(OnTextInputListener onTextInputListener) {
        this.onTextInputListener = onTextInputListener;
        if (isShown) {
            changeOnPromptTextChanged(onTextInputListener);
        }
        return this;
    }


    public boolean isScrollable() {
        return this.isScrollable;
    }

    public boolean isChecked() {
        this.isChecked = accb_checkbox.isChecked();
        return this.isChecked;
    }

    /**
     *  @source: https://stackoverflow.com/questions/14657490/how-to-properly-retain-a-dialogfragment-through-rotatio */
    @Override
    public void onDestroyView() {
        Dialog dialog = getDialog();
        // handles https://code.google.com/p/android/issues/detail?id=17423 issue
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }

        if (dialog != null && dialog.getWindow() != null){
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            super.onDestroyView();
        }
    }

    // getContext() will always be not null between onAttach() and onDetach()
    // @src: https://stackoverflow.com/a/48688603/7493547
    @Override
    public void onAttach(@NonNull Context ctx) {
        super.onAttach(ctx);
        this.ctx = ctx;
        this.superDialog = this;
        // set the theme
        ctx.setTheme(SuperDialogConfiguration.getTheme());
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.superdialog_main, container, false);
        // Set transparent background and remove stock title decoration views.. so the round corners bg shows.
        if (getDialog() != null && getDialog().getWindow() != null) {
            Window window = getDialog().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); // This flag is required to set otherwise the setDimAmount method will not show any effect
            window.setDimAmount(0.7F); //0 for no dim to 1 for full dim
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.requestFeature(Window.FEATURE_NO_TITLE);
        }
        this.setRetainInstance(true);

        // INIT COMPONENTS
        til                      = view.findViewById(R.id.til);
        et_text                  = view.findViewById(R.id.et_text);
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
        accb_checkbox            = view.findViewById(R.id.accb_checkbox);
        space                    = view.findViewById(R.id.space);

        // Setup Cancelable
        changeCancelable(cancelable);

        // Setup Icon
        changeIconMode(iconMode);
        changeCustomIconRes(customIconRes);

        // Setup Title
        changeTitle(title);
        changeTitleCaps(isAllCaps);

        // Setup Message (a.k.a. Content)
        changeMessage(message);
        changeMessageGravity(gravity);

        // Setup Prompt
        changePrompt(isPrompt, promptHint, promptText, promptLines);
        changeOnPromptTextChanged(onTextInputListener);
        changePromptTextInputType(promptTextInputType);
        changePromptMaxTextLength(promptMaxTextLength);
        changePromptHelperTextColor(promptHelperTextColor);

        // Add space between positive and negative button when both are visible.
        addSpaceBetweenButtons();

        // Setup Checkbox
        changeCheckable(isCheckable, isChecked);
        changeCheckboxText(checkboxText);

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
        if (space != null) return; // prevent null pointer exceptions
        if (negativeText != null && positiveText != null){
            space.setVisibility(View.VISIBLE);
        }
    }


    /**
     * Setup Prompt
     */
    private void changePrompt(boolean isPrompt, String hint, String defaultText, int lines) {
        if (et_text == null || til == null) return; // prevent null pointer exceptions
        if (isPrompt) {
            // show the prompt area.
            til.setVisibility(View.VISIBLE);

            // set the hint if available.
            if (hint != null) {
                til.setHint(hint);
            } else {
                et_text.setHint(null);
            }

            // set default text if available.
            if (defaultText != null) {
                et_text.setText(defaultText);
            } else {
                et_text.setText(null);
            }

            // set default max lines
            if (lines <= 1) {
                lines = 1;
                et_text.setSingleLine(true);
            }
            et_text.setLines(lines);

        } else {
            // hide the prompt area.
            til.setVisibility(View.GONE);
        }
    }

    private void changePromptToFail(String errorText) {
        if (til == null) return; // prevent null pointer exceptions
        if (errorText != null) {
            this.isPromptErrorEnabled = true;
            til.setHelperTextEnabled(false); // hide the helper text, otherwise the layout would get messy. See Material Guidelines on EditText
            til.setErrorEnabled(true);
            til.setError(errorText);

        } else {
            this.isPromptErrorEnabled = false;
            til.setErrorEnabled(false);

            if (isPromptHelperEnabled) {
                til.setHelperTextEnabled(true);
            }
        }
    }

    private void changePromptHelper(String helperText) {
        if (til == null) return; // prevent null pointer exceptions
        if (helperText != null) {
            this.isPromptHelperEnabled = true;
            til.setErrorEnabled(false); // hide the error text, otherwise the layout would get messy. See Material Guidelines on EditText
            til.setHelperTextEnabled(true);
            til.setHelperText(helperText);

        } else {
            this.promptHelperText = null;
            this.isPromptHelperEnabled = false;
            til.setHelperTextEnabled(false);
        }
    }

    private void changePromptBackToNormal(boolean errorsToo) {
        if (til == null) return; // prevent null pointer exceptions
        if (isPromptErrorEnabled) {
            this.isPromptErrorEnabled = false;
            til.setErrorEnabled(false);

            if (isPromptHelperEnabled) {
                til.setHelperText(promptHelperText);
                til.setHelperTextEnabled(true);
            }

            if (errorsToo) {
                this.promptHelperText = null;
                this.promptErrorText  = null;
                til.setHelperText(null);
                til.setError(null);
            }

        } else if (errorsToo) {
            this.promptHelperText = null;
            this.promptErrorText  = null;
            til.setHelperText(null);
            til.setError(null);
        }
    }

    private void changePromptHelperTextColor(@ColorRes int promptHelperTextColor) {
        if (til == null) return; // prevent null pointer exceptions
        if (promptHelperTextColor != DEFAULT) {
            til.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(ctx, promptHelperTextColor)));
        }
    }

    private void changeOnPromptTextChanged(final OnTextInputListener onTextInputListener) {
        if (et_text == null) return; // prevent null pointer exceptions
        if (onTextInputListener != null) {
            et_text.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    // do nothing..
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    // do nothing..
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    onTextInputListener.OnTextInput(superDialog, editable.toString());
                }
            });
        }
    }

    private void changePromptTextInputType(int inputType) {
        et_text.setInputType(inputType);
    }

    private void changePromptMaxTextLength(int maxTextLength) {
        if (et_text == null) return; // prevent null pointer exceptions
        et_text.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(maxTextLength),
        });
    }


    /**
     * Setup Cancelable
     * @param cancelable
     */
    private void changeCancelable(boolean cancelable){
        setCancelable(cancelable);
    }


    /**
     * Setup Checkbox
     * @param isCheckable
     */
    private void changeCheckable(boolean isCheckable){
        if (accb_checkbox == null) return; // prevent null pointer exceptions
        if (isCheckable) {
            accb_checkbox.setVisibility(View.VISIBLE);

        } else {
            accb_checkbox.setVisibility(View.GONE);
        }
    }
    private void changeCheckable(boolean isCheckable, boolean isCheckedByDefault){
        if (accb_checkbox == null) return; // prevent null pointer exceptions
        if (isCheckable) {
            accb_checkbox.setVisibility(View.VISIBLE);
            changeCheckboxState(isCheckedByDefault);

        } else {
            accb_checkbox.setVisibility(View.GONE);
        }
    }
    private void changeCheckboxText(String checkboxText){
        if (accb_checkbox == null) return; // prevent null pointer exceptions
        accb_checkbox.setText(checkboxText);
    }
    private void changeCheckboxState(boolean setChecked) {
        if (accb_checkbox == null) return; // prevent null pointer exceptions
        accb_checkbox.setChecked(setChecked);
    }


    /**
     * Setup Positive Button
     * @param positiveText
     */
    private void changePositiveText(String positiveText){
        if (btn_positive == null || space == null) return; // prevent null pointer exceptions
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
        if (btn_positive == null) return; // prevent null pointer exceptions
        if (positiveText != null) {
            if (onPositive != null) {
                btn_positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onPositive.OnButtonClick(superDialog, SuperDialogButtonType.POSITIVE);
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
        if (btn_positive == null) return; // prevent null pointer exceptions
        if (positiveTextColorRes != DEFAULT) {
            btn_positive.setTextColor(ContextCompat.getColor(ctx, positiveTextColorRes));
        }
    }
    private void changePositiveColor(@ColorRes int positiveColorRes){
        if (btn_positive == null) return; // prevent null pointer exceptions
        if (positiveColorRes == DEFAULT) {
            TypedArray ta = ctx.obtainStyledAttributes(R.styleable.SuperDialogTheme);
            positiveColorRes = ta.getResourceId(R.styleable.SuperDialogTheme_sdt_positiveButtonColor, ContextCompat.getColor(ctx, R.color.secondary_green));
            ta.recycle();
        }

        btn_positive.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(ctx, positiveColorRes)));
        if (positiveTextColorRes == DEFAULT){
            int light_or_dark = isColorDark(positiveColorRes) ? Color.WHITE : Color.BLACK;
            btn_positive.setTextColor(light_or_dark);
        }
    }


    /**
     * Setup Negative Button
     * @param negativeText
     */
    private void changeNegativeText(String negativeText){
        if (btn_negative == null || space == null) return; // prevent null pointer exceptions
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
        if (btn_negative == null) return; // prevent null pointer exceptions
        if (negativeText != null) {
            if (onNegative != null) {
                btn_negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onNegative.OnButtonClick(superDialog, SuperDialogButtonType.NEGATIVE);

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
        if (btn_negative == null) return; // prevent null pointer exceptions
        if (negativeTextColorRes != DEFAULT) {
            btn_negative.setTextColor(ContextCompat.getColor(ctx, negativeTextColorRes));
        }
    }
    private void changeNegativeColor(@ColorRes int negativeColorRes){
        if (btn_negative == null) return; // prevent null pointer exceptions
        if (negativeColorRes == DEFAULT) {
            TypedArray ta = ctx.obtainStyledAttributes(R.styleable.SuperDialogTheme);
            negativeColorRes = ta.getResourceId(R.styleable.SuperDialogTheme_sdt_negativeButtonColor, ContextCompat.getColor(ctx, R.color.secondary_green));
            ta.recycle();
        }

        btn_negative.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(ctx, negativeColorRes)));
        if (negativeTextColorRes == DEFAULT){
            int light_or_dark = isColorDark(negativeColorRes) ? Color.WHITE : Color.BLACK;
            btn_negative.setTextColor(light_or_dark);
        }
    }


    /**
     * Setup Cancel Button
     * @param cancelText
     */
    private void changeCancelText(String cancelText){
        if (btn_cancel == null) return; // prevent null pointer exceptions
        if (cancelText != null){
            btn_cancel.setVisibility(View.VISIBLE);
            btn_cancel.setText(cancelText);
        } else {
            btn_cancel.setVisibility(View.GONE);
        }
    }
    private void changeOnCancel(final OnButtonClickListener onCancel){
        if (btn_cancel == null) return; // prevent null pointer exceptions
        if (cancelText != null) {
            if (onCancel != null) {
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCancel.OnButtonClick(superDialog, SuperDialogButtonType.CANCEL);
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
        if (btn_cancel == null) return; // prevent null pointer exceptions
        if (cancelTextColorRes != DEFAULT) {
            btn_cancel.setTextColor(ContextCompat.getColor(ctx, cancelTextColorRes));
        }
    }
    private void changeCancelColor(@ColorRes int cancelColorRes){
        if (btn_cancel == null) return; // prevent null pointer exceptions
        if (cancelColorRes == DEFAULT) {
            TypedArray ta = ctx.obtainStyledAttributes(R.styleable.SuperDialogTheme);
            cancelColorRes = ta.getResourceId(R.styleable.SuperDialogTheme_sdt_cancelButtonColor, ContextCompat.getColor(ctx, R.color.secondary_green));
            ta.recycle();
        }


        btn_cancel.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(ctx, cancelColorRes)));
        if (cancelTextColorRes == DEFAULT){
            int light_or_dark = isColorDark(cancelColorRes) ? Color.WHITE : Color.BLACK;
            btn_cancel.setTextColor(light_or_dark);
        }

    }


    /**
     * Setup Title
     * @param title
     */
    private void changeTitle(String title){
        if (tv_title == null) return; // prevent null pointer exceptions
        if (title != null){
            tv_title.setVisibility(View.VISIBLE);
            tv_title.setText(title);

        } else {
            tv_title.setVisibility(View.GONE);
        }
    }
    public void changeTitleCaps(boolean isAllCaps){
        if (tv_title == null) return; // prevent null pointer exceptions
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
        if (tv_message == null) return; // prevent null pointer exceptions
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
    private void changeMessageGravity(int gravity){
        if (tv_message == null) return; // prevent null pointer exceptions
        if (message != null) {
            tv_message.setGravity(gravity);
        }
    }


    /**
     * Setup Icon
     * @param iconMode
     */
    private void changeIconMode(IconMode iconMode){
        if (ai_animatedIcon == null) return; // prevent null pointer exceptions
        if (iconMode == IconMode.CUSTOM_IMAGE || iconMode == IconMode.NO_ICON){
            ai_animatedIcon.setVisibility(View.GONE);

        } else {
            ai_animatedIcon.setVisibility(View.VISIBLE);
        }

        ai_animatedIcon.setMode(iconMode);
    }
    private void changeCustomIconRes(@DrawableRes int customIconRes){
        if (iv_icon == null) return; // prevent null pointer exceptions
        if (iconMode == IconMode.CUSTOM_IMAGE) {
            if (customIconRes != DEFAULT) {
                iv_icon.setVisibility(View.VISIBLE);
                iv_icon.setImageResource(customIconRes);
            }
        }
    }


    // And Finally, to show the dialog
    public void show(FragmentManager fm){
        show(fm, "");
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        super.show(manager, tag);
        isShown = true;
    }
}
