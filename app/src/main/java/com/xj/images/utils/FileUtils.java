package com.xj.images.utils;

import android.os.Environment;
import android.text.TextUtils;

import com.xj.images.glide.ImageGlideModule;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ajun on 2017/1/24.
 */

public class FileUtils {
    // 复制文件
    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } finally {
            // 关闭流
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }

    public static File getExternalDir(){
        if(TextUtils.equals(Environment.getExternalStorageState(), Environment.MEDIA_MOUNTED)){
            File root = Environment.getExternalStorageDirectory();
            File savedFilesDir = new File(root, ImageGlideModule.DEFAULT_DISK_CACHE_DIR);
            if(null != savedFilesDir && !savedFilesDir.exists()){
                savedFilesDir.mkdirs();
            }
            return savedFilesDir;
        }
        return null;
    }
}
