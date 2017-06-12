package com.fjsd.yyd.picbrowser.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.fjsd.yyd.picbrowser.data.Media;
import com.fjsd.yyd.picbrowser.fragment.GifFragment;
import com.fjsd.yyd.picbrowser.fragment.ImageFragment;
import com.fjsd.yyd.picbrowser.fragment.VideoFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/10 0010.
 */

public class MediaPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Media> media;
    private View.OnClickListener videoOnClickListener;
    private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    public MediaPagerAdapter(FragmentManager fm, ArrayList<Media> media) {
        super(fm);
        this.media = media;
    }

    public void setVideoOnClickListener(View.OnClickListener videoOnClickListener) {
        this.videoOnClickListener = videoOnClickListener;
    }

    @Override public Fragment getItem(int pos) {
        Media media = this.media.get(pos);
        //根据媒体类型返回不同的Fragment
        if (media.isVideo()) {
            VideoFragment fragment = VideoFragment.newInstance(media);
            fragment.setOnClickListener(videoOnClickListener);
            return fragment;
        }
        if (media.isGif())
            return GifFragment.newInstance(media);
        else
            return ImageFragment.newInstance(media);
    }

    @Override public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    public void swapDataSet(ArrayList<Media> media) {
        this.media = media;
        notifyDataSetChanged();
    }

    @Override public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override public int getCount() {
        return media.size();
    }
}
