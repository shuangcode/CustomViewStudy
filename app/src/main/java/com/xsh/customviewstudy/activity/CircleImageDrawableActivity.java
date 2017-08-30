package com.xsh.customviewstudy.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import com.xsh.customviewstudy.R;
import com.xsh.customviewstudy.view.CircleImageDrawable;

public class CircleImageDrawableActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_round_bitmap);

		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.mv);
		ImageView iv = (ImageView) findViewById(R.id.id_one);
		iv.setImageDrawable(new CircleImageDrawable(bitmap));
		iv = (ImageView) findViewById(R.id.id_two);
		iv.setImageDrawable(new CircleImageDrawable(bitmap));

	}

}
