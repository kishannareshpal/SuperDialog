package com.kishannareshpal.superdialog;

import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;

import static com.kishannareshpal.superdialog.AnimatedIcon.Mode.ERROR;
import static com.kishannareshpal.superdialog.AnimatedIcon.Mode.INDEFINITE;
import static com.kishannareshpal.superdialog.AnimatedIcon.Mode.STOPED;
import static com.kishannareshpal.superdialog.AnimatedIcon.Mode.SUCCESS;

public class AnimatedIcon extends View {

    Context ctx;

    class Mode {
        public static final int STOPED = -1;
        public static final int SUCCESS = 0;
        public static final int ERROR = 1;
        public static final int INDEFINITE = 2;
    }

    private int fullWidth, fullHeight;
    private int default_padding = 8;
    private int circle_sweepAngle;
    private int strokeWidth = 8;
    private int mode = INDEFINITE;


    private RectF oval;
    private Paint main_paint, stroke_paint, icon_paint;
    private ValueAnimator valueAnimator;


    public void setMode(int mode) {
        this.mode = mode;

        if (mode == INDEFINITE) {
            valueAnimator.start();

        } else if (mode == STOPED){
            valueAnimator.end();

        } else if (mode == SUCCESS){
            valueAnimator.end();
            main_paint.setColor(ContextCompat.getColor(ctx, R.color.secondary_green));
            stroke_paint.setColor(ContextCompat.getColor(ctx, R.color.secondary_green));
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

        } else if (mode == ERROR) {
            valueAnimator.end();
            main_paint.setColor(ContextCompat.getColor(ctx, R.color.secondary_red));
            stroke_paint.setColor(ContextCompat.getColor(ctx, R.color.secondary_red));
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
    }



    public AnimatedIcon(Context context) {
        super(context);
        init(context);
    }

    public AnimatedIcon(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context ctx){
        this.ctx = ctx;

        oval = new RectF();

        main_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        main_paint.setColor(ContextCompat.getColor(ctx, R.color.md_grey_100));

        stroke_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        stroke_paint.setStrokeCap(Paint.Cap.ROUND);
        stroke_paint.setStyle(Paint.Style.STROKE);
        stroke_paint.setColor(ContextCompat.getColor(ctx, R.color.secondary_green));

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

        icon_paint.setStrokeWidth(strokeWidth);
        stroke_paint.setStrokeWidth(strokeWidth);

        float cx            = fullWidth / 2;
        float cy            = fullHeight / 2;
        float circle_radius = width / 2;

        oval.top    = default_padding;
        oval.left   = default_padding;
        oval.right  = width;
        oval.bottom = height;
        canvas.drawCircle(cx, cy, circle_radius, main_paint);


        if (mode == SUCCESS){
            float linestart_x = width/3;
            float linestart_y = (height/3) + (height/5);
            float lineend_x = width/2;
            float lineend_y = (width/2) + (width/6);
            canvas.drawLine(linestart_x-2, linestart_y, lineend_x, lineend_y, icon_paint);
            linestart_x = (width/2) + (width/4);
            linestart_y = height/3;
            canvas.drawLine(lineend_x, lineend_y, linestart_x-2, linestart_y, icon_paint);

        } else if (mode == ERROR) {
            float linestart_x = (fullWidth/2) - (fullWidth/6);
            float linestart_y = (fullHeight/2) - (fullHeight/6);
            float lineend_x = (fullWidth/2) + (fullWidth/6);
            float lineend_y = (fullHeight/2) + (fullHeight/6);

            canvas.drawLine(linestart_x, linestart_y, lineend_x, lineend_y, icon_paint);
            linestart_x = (fullWidth/2) + (fullWidth/6);
            lineend_x = (fullWidth/2) - (fullWidth/6);
            canvas.drawLine(linestart_x, linestart_y, lineend_x, lineend_y, icon_paint);
        }


        stroke_paint.setStrokeWidth(8);
        canvas.rotate(circle_sweepAngle * 4, cx, cy);
        canvas.drawArc(oval, 70, circle_sweepAngle, false, stroke_paint);
    }
}
