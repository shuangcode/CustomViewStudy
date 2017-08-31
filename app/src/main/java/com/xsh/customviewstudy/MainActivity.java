package com.xsh.customviewstudy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.xsh.customviewstudy.activity.CircleImageDrawableActivity;
import com.xsh.customviewstudy.activity.CustomImageTextViewActivity;
import com.xsh.customviewstudy.activity.CustomProgressBarActivity;
import com.xsh.customviewstudy.activity.CustomTextViewActivty;
import com.xsh.customviewstudy.activity.CustomVolumeContrlBarActivity;
import com.xsh.customviewstudy.activity.RoundImageDrawableActivity;
import com.xsh.customviewstudy.activity.SwipeDeleteActivity;
import com.xsh.customviewstudy.activity.VDHActivity1;
import com.xsh.customviewstudy.activity.VDHActivity2;
import com.xsh.customviewstudy.view.CustomImgContainerActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void customTextView(View view){
        Intent intent = new Intent(this, CustomTextViewActivty.class);
        startActivity(intent);
    }

    public void customImageTextView(View view){
        Intent intent = new Intent(this, CustomImageTextViewActivity.class);
        startActivity(intent);
    }

    public void customProgressBar(View view){
        Intent intent = new Intent(this, CustomProgressBarActivity.class);
        startActivity(intent);
    }
    public void customVolumeControl(View view){
        Intent intent = new Intent(this, CustomVolumeContrlBarActivity.class);
        startActivity(intent);
    }

    public void customImgContainer(View view){
        Intent intent = new Intent(this, CustomImgContainerActivity.class);
        startActivity(intent);
    }

    public void vdhActivity1(View view){
        Intent intent = new Intent(this, VDHActivity1.class);
        startActivity(intent);
    }

    public void vdhActivity2(View view){
        Intent intent = new Intent(this, VDHActivity2.class);
        startActivity(intent);
    }

    /**
     * 圆形的Bitmap
     * @param view
     */
    public void roundBitmap(View view){
        Intent intent = new Intent(this, RoundImageDrawableActivity.class);
        startActivity(intent);
    }

    /**
     * 圆角的Bitmap
     * @param view
     */
    public void circleBitmap(View view){
        Intent intent = new Intent(this, CircleImageDrawableActivity.class);
        startActivity(intent);
    }


    /**
     * 圆角的Bitmap
     * @param view
     */
    public void swipeDelete(View view){
        Intent intent = new Intent(this, SwipeDeleteActivity.class);
        startActivity(intent);
    }

}
