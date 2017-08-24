package com.xsh.customviewstudy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import com.xsh.customviewstudy.R;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by daniel.xiao on 2017/8/23.
 *
 */

public class CustomTextView extends TextView{

    /**
     * 文本
     */
    private String mTitle;
    /**
     * 文本的颜色
     */
    private int mTitleTextColor;
    /**
     * 文本的大小
     */
    private int mTitleTextSize;

    /**
     * 绘制时控制文本绘制的范围
     */
    private Rect mBounds;
    private Paint mPaint;

    public CustomTextView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public CustomTextView(Context context)
    {
        this(context, null);
    }

    /**
     * 获得我自定义的样式属性
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CustomTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        /**
         * 获得我们所定义的自定义样式属性
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTextView, defStyle, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++)
        {
            int attr = a.getIndex(i);
            switch (attr)
            {
                case R.styleable.CustomTextView_titleText:
                    mTitle = a.getString(attr);
                    break;
                case R.styleable.CustomTextView_titleTextColor:
                    // 默认颜色设置为黑色
                    mTitleTextColor = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomTextView_titleTextSize:
                    // 默认设置为16sp，TypeValue也可以把sp转化为px
                    mTitleTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;

            }

        }
        a.recycle();

        /**
         * 获得绘制文本的宽和高
         */
        mPaint = new Paint();
        mPaint.setTextSize(mTitleTextSize);
        // mPaint.setColor(mTitleTextColor);
        mBounds = new Rect();
        mPaint.getTextBounds(mTitle, 0, mTitle.length(), mBounds);

        this.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mTitle = randomText();
                postInvalidate();
            }

        });

    }

    private String randomText()
    {
        Random random = new Random();
        Set<Integer> set = new HashSet<Integer>();
        while (set.size() < 4)
        {
            int randomInt = random.nextInt(10);
            set.add(randomInt);
        }
        StringBuffer sb = new StringBuffer();
        for (Integer i : set)
        {
            sb.append("" + i);
        }

        return sb.toString();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height ;
        //说明设置明确的值或者是MATCH_PARENT
        if (widthMode == MeasureSpec.EXACTLY)
        {
            width = widthSize;
        } else
        {
            //wrap_content
            mPaint.setTextSize(mTitleTextSize);
            //文字的占据的宽和高
            mPaint.getTextBounds(mTitle, 0, mTitle.length(), mBounds);
            float textWidth = mBounds.width();
            //
            int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY)
        {
            height = heightSize;
        } else
        {
            mPaint.setTextSize(mTitleTextSize);
            mPaint.getTextBounds(mTitle, 0, mTitle.length(), mBounds);
            float textHeight = mBounds.height();
            int desired = (int) (getPaddingTop() + textHeight + getPaddingBottom());
            height = desired;
        }



        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        mPaint.setColor(Color.YELLOW);
        //先画一个矩形
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
        //再画一些文字
        mPaint.setColor(mTitleTextColor);
        canvas.drawText(
                mTitle, getWidth() / 2 - mBounds.width() / 2, getHeight() / 2 + mBounds.height() / 2, mPaint);
    }
}
