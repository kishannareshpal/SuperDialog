package com.kishannareshpal.superdialog;

import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;

public class AnimatedIcon extends View {

    Context ctx;
    private int fullWidth, fullHeight;
    private int default_padding = 8;
    private int circle_sweepAngle;
    private int strokeWidth = 8; // this width will expand +4 with animation. So in the end it's value'll be 12;
    private IconMode mode = IconMode.NO_ICON;


    private RectF oval;
    private Paint main_paint, stroke_paint, icon_paint, fullIcon_paint;
    private ValueAnimator valueAnimator;


    public void setMode(IconMode mode) {
        this.mode = mode;
        int mode_color;

        switch (mode){
            case CUSTOM_IMAGE:
            case NO_ICON:
                valueAnimator.end();
                return;

            case INDEFINITE_PROGRESS:
                @SuppressLint("CustomViewStyleable")
                TypedArray ta = ctx.obtainStyledAttributes(R.styleable.SuperDialogTheme);
                int ai_progressBackgroundColor = ta.getColor(R.styleable.SuperDialogTheme_sdt_ai_progressBackgroundColor, ContextCompat.getColor(ctx, R.color.md_grey_100));
//        int ai_progressStrokeColor = ta.getColor(R.styleable.SuperDialogTheme_sdt_ai_progressStrokeColor, ContextCompat.getColor(ctx, R.color.secondary_green));
                ta.recycle();
                main_paint.setColor(ai_progressBackgroundColor);
                stroke_paint.setColor(ContextCompat.getColor(ctx, R.color.secondary_green));
                valueAnimator.start();
                return;

            case DEFINITE_PROGRESS:
                // TODO: 12/14/18
                return;

            case SUCCESS:
                mode_color = R.color.secondary_green;
                break;

            case ERROR:
                mode_color = R.color.secondary_red;
                break;

            case WARNING:
                mode_color = R.color.secondary_yellow;
                break;

            default:
                return;
        }

        // will only run this if the mode is either SUCCESS, ERROR or WARNING.
        valueAnimator.end();
        main_paint.setColor(ContextCompat.getColor(ctx, mode_color));
        stroke_paint.setColor(ContextCompat.getColor(ctx, mode_color));
        PropertyValuesHolder propertyColorFade = PropertyValuesHolder.ofObject("color", new ArgbEvaluator(), icon_paint.getColor(), ContextCompat.getColor(ctx, R.color.md_white_1000));
        PropertyValuesHolder propertyStrokeWidth = PropertyValuesHolder.ofObject("strokeWidth", new FloatEvaluator(), icon_paint.getStrokeWidth(), strokeWidth+4);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(icon_paint, propertyColorFade, propertyStrokeWidth);
        animator.setDuration(500);
        animator.setInterpolator(new BounceInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });
        animator.start();
    }


    public AnimatedIcon(Context context) {
        super(context);
        init(context, null);
    }

    public AnimatedIcon(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void init(Context ctx, @Nullable AttributeSet attributeSet){
        this.ctx = ctx;
        oval = new RectF();



        // This paint is mainly use for the WARNING, ERROR, DANGER icons
        fullIcon_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fullIcon_paint.setColor(ContextCompat.getColor(ctx, R.color.md_white_1000));

        // This paint is for every icon background
        main_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        main_paint.setColor(ContextCompat.getColor(ctx, R.color.md_grey_100));

        // This paint is mainly used on the PROGRESS's rotating icon stroke
        stroke_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        stroke_paint.setStrokeCap(Paint.Cap.ROUND);
        stroke_paint.setStyle(Paint.Style.STROKE);
        stroke_paint.setColor(ContextCompat.getColor(ctx, R.color.secondary_green));

        // This paint is mainly use for the WARNING, ERROR, DANGER icon Background
        icon_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        icon_paint.setStrokeCap(Paint.Cap.ROUND);
        icon_paint.setStyle(Paint.Style.STROKE);
        icon_paint.setColor(ContextCompat.getColor(ctx, R.color.secondary_green));

        valueAnimator = ValueAnimator.ofInt(1, 270);
        valueAnimator.setDuration(1500);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                circle_sweepAngle = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        fullWidth = w;
        fullHeight = h;
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = fullWidth - getPaddingLeft() - getPaddingRight() - default_padding;
        int height = fullWidth - getPaddingTop() - getPaddingBottom() - default_padding;

        strokeWidth = width/8;

        icon_paint.setStrokeWidth(strokeWidth); // paint used for icon shapes and lines
        stroke_paint.setStrokeWidth(strokeWidth); // dynamic paint used for the rotating shapes (from PROGRESS_TYPE)

        // big background circle
        float cx            = fullWidth / 2;
        float cy            = fullHeight / 2;
        float circle_radius = width / 2;

        oval.top    = default_padding;
        oval.left   = default_padding;
        oval.right  = width;
        oval.bottom = height;
        canvas.drawCircle(cx, cy, circle_radius, main_paint);


        if (mode == IconMode.SUCCESS){
            // the first diagonal line (low left to center)
            float linestart_x = width/3;
            float linestart_y = (height/3) + (height/5);
            float lineend_x = width/2;
            float lineend_y = (width/2) + (width/6);
            canvas.drawLine(linestart_x-2, linestart_y, lineend_x, lineend_y, icon_paint);

            // the sencond diagonal line (center to top right)
            linestart_x = (width/2) + (width/4);
            linestart_y = height/3;
            canvas.drawLine(lineend_x, lineend_y, linestart_x-2, linestart_y, icon_paint);


        } else if (mode == IconMode.ERROR){
            // 1st diagonal line (left to right)
            float linestart_x = (fullWidth/2) - (fullWidth/6);
            float linestart_y = (fullHeight/2) - (fullHeight/6);
            float lineend_x = (fullWidth/2) + (fullWidth/6);
            float lineend_y = (fullHeight/2) + (fullHeight/6);
            canvas.drawLine(linestart_x, linestart_y, lineend_x, lineend_y, icon_paint);

            // 2nd diagonal line (right to left)
            linestart_x = (fullWidth/2) + (fullWidth/6);
            lineend_x = (fullWidth/2) - (fullWidth/6);
            canvas.drawLine(linestart_x, linestart_y, lineend_x, lineend_y, icon_paint);


        } else if (mode == IconMode.WARNING){
            // the vertical line (up to down)
            float linestart_x = fullWidth/2;
            float linestart_y = (fullHeight/2) - (fullHeight/6);
            float lineend_x = fullWidth/2;
            float lineend_y = fullHeight/2;
            canvas.drawLine(linestart_x, linestart_y, lineend_x, lineend_y, icon_paint);

            // the point (low)
            float point_x = fullWidth/2;
            float point_y = fullHeight/2 + 18;
            float point_radius = 8;
            canvas.drawCircle(point_x, point_y, point_radius, fullIcon_paint);
        }


        stroke_paint.setStrokeWidth(8);
        canvas.rotate(circle_sweepAngle * 4, cx, cy);
        canvas.drawArc(oval, 70, circle_sweepAngle, false, stroke_paint);
    }
}
