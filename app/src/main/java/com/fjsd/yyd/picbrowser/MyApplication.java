package com.fjsd.yyd.picbrowser;

import android.app.Application;

import com.fjsd.yyd.picbrowser.data.Album;
import com.fjsd.yyd.picbrowser.util.HandlingAlbums;

/**
 * Created by Administrator on 2017/6/6 0006.
 * MyApplication类：定义一些全局变量
 */

public class MyApplication extends Application {
    private HandlingAlbums albums = null;

    public Album getAlbum() {
        return albums.dispAlbums.size() > 0 ? albums.getCurrentAlbum() : Album.getEmptyAlbum();
    }

    @Override
    public void onCreate() {
        albums = new HandlingAlbums(getApplicationContext());
        super.onCreate();
    }

    public HandlingAlbums getAlbums() {
        return albums;
    }

    public void setAlbums(HandlingAlbums albums) {
        this.albums = albums;
    }

    public void updateAlbums() {
        albums.loadAlbums(getApplicationContext());
    }
}
