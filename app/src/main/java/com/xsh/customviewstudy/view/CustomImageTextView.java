package com.xsh.customviewstudy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import com.xsh.customviewstudy.R;

public class CustomImageTextView extends View
{
	/**
	 * �ؼ��Ŀ�
	 */
	private int mWidth;
	/**
	 * �ؼ��ĸ�
	 */
	private int mHeight;
	/**
	 * �ؼ��е�ͼƬ
	 */
	private Bitmap mImage;
	/**
	 * ͼƬ������ģʽ
	 */
	private int mImageScale;
	private static final int IMAGE_SCALE_FITXY = 0;
	private static final int IMAGE_SCALE_CENTER = 1;
	/**
	 * ͼƬ�Ľ���
	 */
	private String mTitle;
	/**
	 * �������ɫ
	 */
	private int mTextColor;
	/**
	 * ����Ĵ�С
	 */
	private int mTextSize;

	private Paint mPaint;
	/**
	 * ���ı���Լ��
	 */
	private Rect mTextBound;
	/**
	 * �������岼��
	 */
	private Rect rect;

	public CustomImageTextView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public CustomImageTextView(Context context)
	{
		this(context, null);
	}

	/**
	 * ��ʼ���������Զ�������
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public CustomImageTextView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomImageTextView, defStyle, 0);

		int n = a.getIndexCount();

		for (int i = 0; i < n; i++)
		{
			int attr = a.getIndex(i);

			switch (attr)
			{
			case R.styleable.CustomImageTextView_image:
				//��ȡͼƬ��Bitmap
				mImage = BitmapFactory.decodeResource(getResources(), a.getResourceId(attr, 0));
				break;
			case R.styleable.CustomImageTextView_imageScaleType:
				//��ȡscaleType
				mImageScale = a.getInt(attr, 0);
				break;
			case R.styleable.CustomImageTextView_titleText:
				//��ȡ�ı�
				mTitle = a.getString(attr);
				break;
			case R.styleable.CustomImageTextView_titleTextColor:
				//��ȡ�ı�����ɫ
				mTextColor = a.getColor(attr, Color.BLACK);
				break;
			case R.styleable.CustomImageTextView_titleTextSize:
				//��ȡ�����С
				mTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
						16, getResources().getDisplayMetrics()));
				break;

			}
		}
		a.recycle();
		rect = new Rect();
		mPaint = new Paint();
		mTextBound = new Rect();
		mPaint.setTextSize(mTextSize);
		// ���������������Ҫ�ķ�Χ,������TextBound��
		mPaint.getTextBounds(mTitle, 0, mTitle.length(), mTextBound);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		/**
		 * ���ÿ��
		 */
		int specMode = MeasureSpec.getMode(widthMeasureSpec);
		int specSize = MeasureSpec.getSize(widthMeasureSpec);

		if (specMode == MeasureSpec.EXACTLY)// match_parent , accurate
		{
			Log.e("xxx", "EXACTLY");
			mWidth = specSize;
		} else
		{
			// ��ͼƬ�����Ŀ�
			int desireByImg = getPaddingLeft() + getPaddingRight() + mImage.getWidth();
			// ����������Ŀ�
			int desireByTitle = getPaddingLeft() + getPaddingRight() + mTextBound.width();

			if (specMode == MeasureSpec.AT_MOST)// wrap_content
			{
				int desire = Math.max(desireByImg, desireByTitle);
				mWidth = Math.min(desire, specSize);
				Log.e("xxx", "AT_MOST");
			}
		}

		/***
		 * ���ø߶�
		 */

		specMode = MeasureSpec.getMode(heightMeasureSpec);
		specSize = MeasureSpec.getSize(heightMeasureSpec);
		if (specMode == MeasureSpec.EXACTLY)// match_parent , accurate
		{
			mHeight = specSize;
		} else
		{
			int desire = getPaddingTop() + getPaddingBottom() + mImage.getHeight() + mTextBound.height();
			if (specMode == MeasureSpec.AT_MOST)// wrap_content
			{
				mHeight = Math.min(desire, specSize);
			}
		}
		setMeasuredDimension(mWidth, mHeight);

	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		// super.onDraw(canvas);
		/**
		 * �߿�
		 */
		mPaint.setStrokeWidth(4);//���û��ʵĴֶ�
		mPaint.setStyle(Style.STROKE);//���û���Ϊʵ��
		mPaint.setColor(Color.CYAN);//���û��ʵ���ɫ
		canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);//������

		rect.left = getPaddingLeft();
		rect.right = mWidth - getPaddingRight();
		rect.top = getPaddingTop();
		rect.bottom = mHeight - getPaddingBottom();

		mPaint.setColor(mTextColor);
		mPaint.setStyle(Style.FILL);
		/**
		 * ��ǰ���õĿ��С��������Ҫ�Ŀ�ȣ��������Ϊxxx...
		 */
		if (mTextBound.width() > mWidth)
		{
			TextPaint paint = new TextPaint(mPaint);
			String msg = TextUtils.ellipsize(mTitle, paint, (float) mWidth - getPaddingLeft() - getPaddingRight(),
					TextUtils.TruncateAt.END).toString();
			canvas.drawText(msg, getPaddingLeft(), mHeight - getPaddingBottom(), mPaint);

		} else
		{
			//������������������
			canvas.drawText(mTitle, mWidth / 2 - mTextBound.width() * 1.0f / 2, mHeight - getPaddingBottom(), mPaint);
		}

		//ȡ��ʹ�õ��Ŀ�
		rect.bottom -= mTextBound.height();

		if (mImageScale == IMAGE_SCALE_FITXY)
		{
			canvas.drawBitmap(mImage, null, rect, mPaint);
		} else
		{
			//������еľ��η�Χ
			rect.left = mWidth / 2 - mImage.getWidth() / 2;
			rect.right = mWidth / 2 + mImage.getWidth() / 2;
			rect.top = (mHeight - mTextBound.height()) / 2 - mImage.getHeight() / 2;
			rect.bottom = (mHeight - mTextBound.height()) / 2 + mImage.getHeight() / 2;

			canvas.drawBitmap(mImage, null, rect, mPaint);
		}

	}

}
