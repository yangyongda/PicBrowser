package com.fjsd.yyd.picbrowser.data;

import android.content.Context;
import android.support.annotation.Nullable;

import com.fjsd.yyd.picbrowser.util.CustomAlbumsHelper;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/6 0006.
 */

public class AlbumSettings implements Serializable {
    private String path;
    private String coverPath;
    private int sortingMode;
    private int sortingOrder;
    private boolean pinned;

    private FilterMode filterMode = FilterMode.ALL;

    public static AlbumSettings getSettings(Context context, Album album) {
        CustomAlbumsHelper h = CustomAlbumsHelper.getInstance(context);
        return h.getSettings(album.getPath());
    }

    public static AlbumSettings getDefaults() {
        return new AlbumSettings(null, null, SortingMode.DATE.getValue(), SortingOrder.DESCENDING.getValue(), 0);
    }


    public AlbumSettings(String path, String cover, int sortingMode, int sortingOrder, int pinned) {
        this.path = path;
        this.coverPath = cover; //封面图片路径
        this.sortingMode = sortingMode;  //排序模式
        this.sortingOrder = sortingOrder; //升序or降续
        this.pinned = pinned == 1;   //置顶
    }

    public FilterMode getFilterMode() {
        return filterMode;
    }

    public void setFilterMode(FilterMode filterMode) {
        this.filterMode = filterMode;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public SortingMode getSortingMode() {
        return SortingMode.fromValue(sortingMode);
    }

    public SortingOrder getSortingOrder() {
        return SortingOrder.fromValue(sortingOrder);
    }

    public void changeSortingMode(Context context, SortingMode sortingMode) {
        this.sortingMode = sortingMode.getValue();
        CustomAlbumsHelper h = CustomAlbumsHelper.getInstance(context);
        h.setAlbumSortingMode(path, sortingMode.getValue());
    }

    public void changeSortingOrder(Context context, SortingOrder sortingOrder) {
        this.sortingOrder = sortingOrder.getValue();
        CustomAlbumsHelper h = CustomAlbumsHelper.getInstance(context);
        h.setAlbumSortingOrder(path, sortingOrder.getValue());
    }

    public void changeCoverPath(Context context, @Nullable String coverPath) {
        this.coverPath = coverPath;
        CustomAlbumsHelper h = CustomAlbumsHelper.getInstance(context);
        h.setAlbumPhotoPreview(path, coverPath);
    }

    public boolean isPinned() {
        return pinned;
    }

    public void togglePin(Context context) {
        this.pinned = !pinned;
        CustomAlbumsHelper h = CustomAlbumsHelper.getInstance(context);
        h.pinAlbum(path, pinned);
    }
}
