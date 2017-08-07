package com.liangjing.circlebutton.views;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.liangjing.circlebutton.R;
import com.liangjing.circlebutton.utils.DimentionUtil;

/**
 * Created by liangjing on 2017/8/7.
 * function:自定义view-圆形按钮(继承自ImageView,所以不会有没有文字效果)
 */

public class CircleButtonView extends ImageView {

    private static final int PRESSED_COLOR_LIGHTUP = 255 / 25;
    private static final int DEFAULT_PRESSED_RING_WIDTH_DIP = 4;
    private static final int PRESSED_RING_ALPHA = 75;
    private static final int ANIMATION_TIME_ID = android.R.integer.config_shortAnimTime;

    //实心圆画笔
    private Paint circlePaint;
    //获取焦点时用到的画笔(画空心圆)
    private Paint focusPaint;
    private ObjectAnimator pressedAnimator;

    private float animationProgress;
    private int pressedRingWidth;
    private int defaultColor;
    private int pressedColor;

    private int centerY;
    private int centerX;
    private int outerRadius;
    private int pressedRingRadius;


    public CircleButtonView(Context context) {
        super(context);
        init(context, null);
    }

    public CircleButtonView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleButtonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * function:初始化操作
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {

        //控制如何调整图像大小或移动以匹配此ImageView的大小。
        this.setScaleType(ScaleType.CENTER_INSIDE);
        //设置此视图是否可以接收焦点。将此设置为false将确保此视图在触摸模式下不可对焦.如果为true，则此视图可以接收焦点。
        this.setFocusable(true);
        setClickable(true);

        //实心圆画笔
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);

        //focusPaint是获取焦点时候调用的，它的Style是STROKE类型的，所以画出一个圆环效果，这也就是点击button之后出现的
        focusPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        focusPaint.setStyle(Paint.Style.STROKE);//仅描边不填充

        //默认值
        pressedRingWidth = DimentionUtil.dip2px(context, DEFAULT_PRESSED_RING_WIDTH_DIP);

        int color = Color.BLUE;

        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleButtonView);
            color = a.getColor(R.styleable.CircleButtonView_cbv_color, color);
            pressedRingWidth = (int) a.getDimension(R.styleable.CircleButtonView_cbv_pressedRingWidth, pressedRingWidth);
            a.recycle();
        }

        setColor(color);

        //设置focusPaint的描边区域大小（即除了空心圆之外还要画多大的区域）
        focusPaint.setStrokeWidth(pressedRingWidth);
        //Return an integer associated with a particular resource ID.
        final int pressedAnimationTime = getResources().getInteger(ANIMATION_TIME_ID);

        //这里设置为-->0f到0f 。在下面的操作中通过监听用户有没有按下按钮再来设置相应的值
        //animationProgress-->该值用来控制空心圆的半径大小
        pressedAnimator = ObjectAnimator.ofFloat(this, "animationProgress", 0f, 0f);
        pressedAnimator.setDuration(pressedAnimationTime);
    }


    /**
     * function: 不断地去改变animationProgress的值，若不创建该方法，则该属性就会不生效。
     *
     * @param animationProgress
     */
    public void setAnimationProgress(float animationProgress) {
        this.animationProgress = animationProgress;
        this.invalidate();
    }

    public float getAnimationProgress() {
        return animationProgress;
    }

    /**
     * function: 给画笔设置颜色
     *
     * @param color
     */
    private void setColor(int color) {
        this.defaultColor = color;
        //pressedColor --> 按下去后的颜色（带透明度）
        this.pressedColor = getHighlightColor(color, PRESSED_COLOR_LIGHTUP);

        //给画笔添加颜色
        circlePaint.setColor(defaultColor);
        focusPaint.setColor(pressedColor);
        //为画空心圆---添加透明度属性
        focusPaint.setAlpha(PRESSED_RING_ALPHA);

        this.invalidate();
    }

    /**
     * function:实现有透明度的color
     *
     * @param color
     * @param amount
     * @return
     */
    private int getHighlightColor(int color, int amount) {
        return Color.argb(Math.min(255, Color.alpha(color)), Math.min(255, Color.red(color) + amount),
                Math.min(255, Color.green(color) + amount), Math.min(255, Color.blue(color) + amount));
    }


    /**
     * function:该方法在当前View尺寸变化时被调用.
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //圆心
        centerX = w / 2;
        centerY = h / 2;
        //半径
        outerRadius = Math.min(w, h) / 2;
        pressedRingRadius = outerRadius - pressedRingWidth - pressedRingWidth / 2;
    }


    /**
     * function:onSizeChanged()调用完后调用onDraw()
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //pressedRingRadius + animationProgress -->空心圆的半径
        canvas.drawCircle(centerX, centerY, pressedRingRadius + animationProgress, focusPaint);
        //outerRadius - pressedRingWidth -->实心圆的半径
        canvas.drawCircle(centerX, centerY, outerRadius - pressedRingWidth, circlePaint);
        super.onDraw(canvas);
        invalidate();
    }


    /**
     * function:监听按钮是否处于按压状态，然后执行相对应的操作（一般是当按下按钮的时候才会调用该方法（此时视图状态已经发生改变），然后当松开手后又会再次调用该方法）
     *
     * @param pressed
     */
    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);

        if (circlePaint != null) {
            circlePaint.setColor(pressed ? pressedColor : defaultColor);
        }

        if (pressed) {
            showPressedRing();
        } else {
            hidePressedRing();
        }
    }

    /**
     * function:处于平常（松开按钮）状态时--animationProgress的值在该区间进行过渡，最终为0
     */
    private void hidePressedRing() {

        pressedAnimator.setFloatValues(pressedRingWidth, 0f);
        pressedAnimator.start();
    }


    /**
     * function:按下按钮时--animationProgress的值在该区间进行过渡
     */
    private void showPressedRing() {
        pressedAnimator.setFloatValues(animationProgress, pressedRingWidth);
        pressedAnimator.start();
    }
}


