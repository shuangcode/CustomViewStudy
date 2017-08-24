package com.xsh.customviewstudy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.xsh.customviewstudy.activity.CustomImageTextViewActivity;
import com.xsh.customviewstudy.activity.CustomTextViewActivty;

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
}
