package com.fjsd.yyd.picbrowser.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fjsd.yyd.picbrowser.R;
import com.fjsd.yyd.picbrowser.data.Album;
import com.fjsd.yyd.picbrowser.util.ColorPalette;
import com.fjsd.yyd.picbrowser.util.PermissionUtils;
import com.fjsd.yyd.picbrowser.util.PreferenceUtil;
import com.fjsd.yyd.picbrowser.util.StringUtils;

import java.io.File;

public class SplashScreen extends SharedMediaActivity {

    private final int READ_EXTERNAL_STORAGE_ID = 12;
    private static final int PICK_MEDIA_REQUEST = 44;

    final static String CONTENT = "content";
    final static String PICK_MODE = "pick_mode";

    final static int ALBUMS_PREFETCHED = 23;
    final static int PHOTOS_PREFETCHED = 2;
    final static int ALBUMS_BACKUP = 60;
    private boolean PICK_INTENT = false;
    public final static String ACTION_OPEN_ALBUM = "com.horaapps.leafpic.OPEN_ALBUM";

    //private HandlingAlbums albums;
    private Album album;

    private PreferenceUtil SP;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        SP = PreferenceUtil.getInstance(getApplicationContext()); //获取配置信息
        //根据主题设置对应的颜色
        ((ProgressBar) findViewById(R.id.progress_splash)).getIndeterminateDrawable().setColorFilter(getPrimaryColor(), PorterDuff.Mode.SRC_ATOP);

        RelativeLayout RelLay = (RelativeLayout) findViewById(R.id.Splah_Bg);
        RelLay.setBackgroundColor(getBackgroundColor());
        //取消全屏模式
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setNavBarColor();
        setStatusBarColor();
        //检查本应用是否有读取存储卡的权限
        if (PermissionUtils.isDeviceInfoGranted(this)) {
            //这里的getIntent()返回的Intent是其他应用跳转到该应用的Intent
            if (getIntent().getAction().equals(ACTION_OPEN_ALBUM)) {
                Bundle data = getIntent().getExtras();
                if (data != null) {
                    String ab = data.getString("albumPath");

                    if (ab != null) {
                        File dir = new File(ab);
                        // TODO: 19/08/16 look for id
                        album = new Album(getApplicationContext(), dir.getAbsolutePath(), data.getInt("albumId", -1), dir.getName(), -1);
                        new PrefetchPhotosData().execute();
                    }
                } else StringUtils.showToast(getApplicationContext(), "Album not found");
            } else
                new PrefetchAlbumsData().execute();
            //这里的getIntent()返回的Intent是其他应用跳转到该应用的Intent
            //ACTION_GET_CONTENT:让用户选择数据，并返回所选数据    ACTION_PICK:从列表中选择某项并返回所选的数据
            //这里是将该应用作为选择器使用(其他应用跳转到该应用来选择数据并返回)
            if (getIntent().getAction().equals(Intent.ACTION_GET_CONTENT) || getIntent().getAction().equals(Intent.ACTION_PICK))
                PICK_INTENT = true;

        } else {
            //没有存储卡读取权限时，则请求权限
            String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
            PermissionUtils.requestPermissions(this, READ_EXTERNAL_STORAGE_ID, permissions);
        }
    }
    //跳转后返回结果的回调函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_MEDIA_REQUEST) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, data);
                finish();
            }
        }
    }

    @Override
    public void setNavBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ColorPalette.getTransparentColor(
                    ContextCompat.getColor(getApplicationContext(), R.color.md_black_1000), 70));
        }
    }

    @Override
    protected void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ColorPalette.getTransparentColor(
                    ContextCompat.getColor(getApplicationContext(), R.color.md_black_1000), 70));
        }
    }

    //返回请求权限后的结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE_ID:
                boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (granted)
                    new PrefetchAlbumsData().execute(SP.getBoolean(getString(R.string.preference_auto_update_media), false));
                else
                    Toast.makeText(SplashScreen.this, getString(R.string.storage_permission_denied), Toast.LENGTH_LONG).show();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    //如果备份文件为空，则加载相册（直接打开应用）
    private class PrefetchAlbumsData extends AsyncTask<Boolean, Boolean, Boolean> {

        @Override
        protected Boolean doInBackground(Boolean... arg0) {
            getAlbums().restoreBackup(getApplicationContext());
            if(getAlbums().dispAlbums.size() == 0) {
                getAlbums().loadAlbums(getApplicationContext(), false);
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            Intent i = new Intent(SplashScreen.this, MainActivity.class);
            Bundle b = new Bundle();
            b.putInt(CONTENT, result ? ALBUMS_PREFETCHED : ALBUMS_BACKUP);
            b.putBoolean(PICK_MODE, PICK_INTENT);
            i.putExtras(b);
            if (PICK_INTENT)
                startActivityForResult(i, PICK_MEDIA_REQUEST);
            else {
                startActivity(i);
                finish();
            }
            //没有备份文件则保存备份
            if(result)
                getAlbums().saveBackup(getApplicationContext());
        }
    }
    //显示图片数据（其他应用使用该应用显示图片）
    private class PrefetchPhotosData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            album.updatePhotos(getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Intent i = new Intent(SplashScreen.this, MainActivity.class);
            Bundle b = new Bundle();
            getAlbums().addAlbum(0, album);
            b.putInt(CONTENT, PHOTOS_PREFETCHED);
            i.putExtras(b);
            startActivity(i);
            finish();
        }
    }
}
