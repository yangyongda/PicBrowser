package com.fjsd.yyd.picbrowser.util;

import com.bumptech.glide.signature.StringSignature;
import com.fjsd.yyd.picbrowser.data.Media;

/**
 * Created by Administrator on 2017/6/6 0006.
 */

public class MediaSignature extends StringSignature {
    private MediaSignature(String path, long lastModified, int orientation) {
        super(path + lastModified + orientation);
    }

    public MediaSignature(Media media) {
        this(media.getPath(), media.getDateModified(), media.getOrientation());
    }
}
