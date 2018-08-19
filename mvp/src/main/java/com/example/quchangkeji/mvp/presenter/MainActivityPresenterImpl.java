package com.example.quchangkeji.mvp.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.quchangkeji.mvp.view.MainActivityView;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by zhuwujing on 2018/8/19.
 */

public class MainActivityPresenterImpl implements MainActivityPresenter {
    private MainActivityView mMainActivityView;

    public MainActivityPresenterImpl(MainActivityView mainActivityView) {
        mMainActivityView = mainActivityView;
    }

    @Override
    public void loadBitmap(final String url) {
        new Thread(new Runnable() {

            private HttpURLConnection mConn;

            @Override
            public void run() {
                try {
                    URL path = new URL(url);
                    mConn = (HttpURLConnection) path.openConnection();
                    // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
                    // http正文内，因此需要设为true, 默认情况下是false;
                    mConn.setDoOutput(true);
                    // 设置是否从httpUrlConnection读入，默认情况下是true;
                    mConn.setDoInput(true);
                    // Post 请求不能使用缓存
                    mConn.setUseCaches(false);
                    mConn.setConnectTimeout(5000);
                    // 设定传送的内容类型是可序列化的java对象
                    // (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
                    mConn.setRequestProperty("Content-type", "application/x-java-serialized-object");
                    // 设定请求的方法为"POST"，默认是GET
//                    conn.setRequestMethod("POST");
                    mConn.setRequestMethod("GET");
                    // 连接，从上述第2条中url.openConnection()至此的配置必须要在connect之前完成，
                    mConn.connect();

                    int responseCode = mConn.getResponseCode();
                    if (responseCode == 200) {
                        InputStream inputStream = mConn.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        if (mMainActivityView != null){
                            mMainActivityView.afterLoadBitmap(bitmap);
                        }
                    } else {
                        if (mMainActivityView != null){
                            mMainActivityView.afterLoadBitmap(null);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (mMainActivityView != null){
                        mMainActivityView.afterLoadBitmap(null);
                    }
                }finally {
                    if (mConn != null){
                        mConn.disconnect();
                    }
                }
            }
        }).start();
    }
}
