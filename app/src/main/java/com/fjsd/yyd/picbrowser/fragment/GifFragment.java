package com.fjsd.yyd.picbrowser.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fjsd.yyd.picbrowser.activity.SingleMediaActivity;
import com.fjsd.yyd.picbrowser.data.Media;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.koushikdutta.ion.Ion;

/**
 * Created by Administrator on 2017/6/10 0010.
 */

public class GifFragment extends Fragment {

    private Media gif;

    public static GifFragment newInstance(Media media) {
        GifFragment gifFragment = new GifFragment();

        Bundle args = new Bundle();
        args.putParcelable("gif", media);
        gifFragment.setArguments(args);

        return gifFragment;

    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gif =  getArguments().getParcelable("gif");
    }


    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PhotoView photoView = new PhotoView(container.getContext());

        Ion.with(getContext())
                .load(gif.getPath())
                .intoImageView(photoView);

        photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                ((SingleMediaActivity) getActivity()).toggleSystemUI();
            }
        });

        return photoView;
    }
}
