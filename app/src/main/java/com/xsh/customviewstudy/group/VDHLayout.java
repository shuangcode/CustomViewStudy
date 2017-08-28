package com.xsh.customviewstudy.group;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by DanielXiao on 17/8/28.
 */
public class VDHLayout extends LinearLayout {
    private ViewDragHelper mDragger;

    public VDHLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDragger = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                //只在ViewGroup的内部移动
                final int leftBound = getPaddingLeft();
                final int rightBound = getWidth() - child.getWidth() - getPaddingRight();
                //这个写法不懂
                //我希望只在ViewGroup的内部移动，
                // 即：最小>=paddingleft，最大<=ViewGroup.getWidth()-paddingright-child.getWidth。
                final int newLeft = Math.min(Math.max(left, leftBound), rightBound);

                return newLeft;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return top;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mDragger.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragger.processTouchEvent(event);
        return true;
    }
}