package com.fjsd.yyd.picbrowser.activity;


import android.os.Bundle;

import com.fjsd.yyd.picbrowser.MyApplication;
import com.fjsd.yyd.picbrowser.data.Album;
import com.fjsd.yyd.picbrowser.util.HandlingAlbums;

public class SharedMediaActivity extends ThemedActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public HandlingAlbums getAlbums() {
        return ((MyApplication) getApplicationContext()).getAlbums();
    }

    public Album getAlbum() {
        return ((MyApplication) getApplicationContext()).getAlbum();
    }
}
