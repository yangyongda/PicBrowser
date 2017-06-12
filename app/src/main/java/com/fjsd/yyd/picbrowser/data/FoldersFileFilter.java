package com.fjsd.yyd.picbrowser.data;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by Administrator on 2017/6/9 0009.
 */

public class FoldersFileFilter implements FileFilter {
    @Override
    public boolean accept(File file) {
        return file.isDirectory();
    }
}
