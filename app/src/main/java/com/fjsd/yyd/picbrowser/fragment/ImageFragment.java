package com.fjsd.yyd.picbrowser.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.fjsd.yyd.picbrowser.R;
import com.fjsd.yyd.picbrowser.activity.SingleMediaActivity;
import com.fjsd.yyd.picbrowser.data.Media;
import com.fjsd.yyd.picbrowser.util.PreferenceUtil;
import com.fjsd.yyd.picbrowser.view.RotateTransformation;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.Date;

/**
 * Created by Administrator on 2017/6/10 0010.
 */

@SuppressWarnings("ResourceType")
public class ImageFragment extends Fragment {

    private Media img;

    public static ImageFragment newInstance(Media media) {
        ImageFragment imageFragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putParcelable("image", media);
        imageFragment.setArguments(args);

        return imageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        img = getArguments().getParcelable("image");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (PreferenceUtil.getInstance(getContext()).getBoolean(getString(R.string.preference_sub_scaling) , false)) {
            SubsamplingScaleImageView imageView = new SubsamplingScaleImageView(getContext());
            imageView.setImage(ImageSource.uri(img.getUri()));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((SingleMediaActivity) getActivity()).toggleSystemUI();
                }
            });
            return imageView;
        } else {
            PhotoView photoView = new PhotoView(getContext());
            displayMedia(photoView, true);
            photoView.setOnPhotoTapListener(new OnPhotoTapListener() { //点击事件
                @Override
                public void onPhotoTap(ImageView view, float x, float y) {
                    ((SingleMediaActivity) getActivity()).toggleSystemUI(); //显示or隐藏系统状态栏、ToolBar
                }
            });
            photoView.setMaximumScale(5.0F);
            photoView.setMediumScale(3.0F);

            return photoView;
        }
    }


    private void displayMedia(PhotoView photoView, boolean useCache) {
        Glide.clear(photoView);
        Glide.with(getContext())
                .load(img.getUri())
                .asBitmap()
                .signature(useCache ? img.getSignature(): new StringSignature(new Date().getTime()+""))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .thumbnail(0.2f)
                //.transform(new RotateTransformation(getContext(), img.getOrientation(), false))
                // .animate(R.anim.fade_in)
                .into(photoView);

    }

    public boolean rotatePicture(int rotation) {

        PhotoView photoView = (PhotoView) getView();

        Glide.clear(photoView);
        Glide.with(getContext())
                .load(img.getUri())
                .asBitmap()
                //.signature(img.getSignature())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .thumbnail(0.2f)
                .transform(new RotateTransformation(getContext(), rotation , true))
                .into(photoView);
        return true;
    }
}
