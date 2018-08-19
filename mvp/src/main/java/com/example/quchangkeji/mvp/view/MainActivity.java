package com.example.quchangkeji.mvp.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.quchangkeji.mvp.R;
import com.example.quchangkeji.mvp.presenter.MainActivityPresenterImpl;

public class MainActivity extends Activity implements MainActivityView {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.image);
    }

    /**
     * 点击事件
     */
    public void loadImage(View view) {
        String url = "https://www.baidu.com/img/bd_logo1.png?qua=high&where=super";
        new MainActivityPresenterImpl(this).loadBitmap(url);
    }

    /**
     * 这个方法是在子线程中
     *
     * @param bitmap
     */
    @Override
    public void afterLoadBitmap(final Bitmap bitmap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (bitmap != null) {
                    mImageView.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(MainActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
