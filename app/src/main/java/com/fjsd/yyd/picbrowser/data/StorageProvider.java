package com.fjsd.yyd.picbrowser.data;

import android.content.Context;

import com.fjsd.yyd.picbrowser.util.ContentHelper;
import com.fjsd.yyd.picbrowser.util.CustomAlbumsHelper;
import com.fjsd.yyd.picbrowser.util.PreferenceUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Administrator on 2017/6/9 0009.
 */

public class StorageProvider {
    private ArrayList<File> excludedFolders; //排除的文件夹
    private boolean includeVideo = true;    //包含视频
    private PreferenceUtil SP;

    public StorageProvider(Context context) {
        SP = PreferenceUtil.getInstance(context);
        excludedFolders = getExcludedFolders(context);
    }

    public ArrayList<Album> getAlbums(Context context, boolean hidden) {
        ArrayList<Album> list = new ArrayList<Album>();
        includeVideo = SP.getBoolean("set_include_video", false);
        if (hidden)  //隐藏
            for (File storage : ContentHelper.getStorageRoots(context))
                fetchRecursivelyHiddenFolder(context, storage, list);
        else
            for (File storage : ContentHelper.getStorageRoots(context))
                fetchRecursivelyFolder(context, storage, list);
        return list;
    }
    //获取排除的文件夹
    private ArrayList<File> getExcludedFolders(Context context) {
        ArrayList<File>  list = new ArrayList<File>();
        //forced excluded folder
        HashSet<File> storageRoots = ContentHelper.getStorageRoots(context);
        for(File file : storageRoots) {
            list.add(new File(file.getPath(), "Android"));  //存储卡根目录的Android文件夹都排除
        }

        CustomAlbumsHelper handler = CustomAlbumsHelper.getInstance(context);
        list.addAll(handler.getExcludedFolders());
        return list;
    }
    //扫描隐藏相册
    private void fetchRecursivelyHiddenFolder(Context context, File dir, ArrayList<Album> albumArrayList) {
        if (!excludedFolders.contains(dir)) {  //该目录不在排除文件夹的列表
            File[] folders = dir.listFiles(new FoldersFileFilter()); //该目录下所有的文件夹（不包含文件）
            if (folders != null) {
                for (File temp : folders) {
                    File nomedia = new File(temp, ".nomedia"); //.nomedia文件用来屏蔽媒体软件扫描的(不想被扫描的文件目录可以放该文件或文件夹)
                    if (!excludedFolders.contains(temp) && (nomedia.exists() || temp.isHidden()))
                        checkAndAddFolder(context, temp, albumArrayList);

                    fetchRecursivelyHiddenFolder(context, temp, albumArrayList); //递归扫描相册
                }
            }
        }
    }
    //扫描相册
    private void fetchRecursivelyFolder(Context context, File dir, ArrayList<Album> albumArrayList) {
        if (!excludedFolders.contains(dir)) {
            checkAndAddFolder(context, dir, albumArrayList);
            File[] children = dir.listFiles(new FoldersFileFilter());
            if (children != null) {
                for (File temp : children) {
                    File nomedia = new File(temp, ".nomedia");
                    if (!excludedFolders.contains(temp) && !temp.isHidden() && !nomedia.exists()) {
                        //非排除并且非隐藏得相册
                        fetchRecursivelyFolder(context, temp, albumArrayList);
                    }
                }
            }
        }
    }

    private void checkAndAddFolder(Context context, File dir, ArrayList<Album> albumArrayList) {
        File[] files = dir.listFiles(new ImageFileFilter(includeVideo)); //获取该目录下的所有图片(或视频)文件
        if (files != null && files.length > 0) {
            //如果该目录有图片或视频就说明是个相册，则创建相册
            Album asd = new Album(context, dir.getAbsolutePath(), -1, dir.getName(), files.length);

            long lastMod = Long.MIN_VALUE;
            File choice = null;
            for (File file : files) {
                if (file.lastModified() > lastMod) {
                    choice = file;
                    lastMod = file.lastModified();
                }
            }
            if (choice != null)
                asd.addMedia( new Media(choice.getAbsolutePath(), choice.lastModified())); //选择相册中最新的图片作为封面

            albumArrayList.add(asd);
        }
    }

    public static ArrayList<Media> getMedia(String path, boolean includeVideo) {
        ArrayList<Media> list = new ArrayList<Media>();
        File[] images = new File(path).listFiles(new ImageFileFilter(includeVideo));
        for (File image : images)
            list.add(new Media(image));
        return list;
    }
}
