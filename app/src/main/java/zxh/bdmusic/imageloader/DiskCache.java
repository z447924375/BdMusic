package zxh.bdmusic.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.android.volley.toolbox.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by dllo on 16/9/22.
 */
public class DiskCache implements ImageLoader.ImageCache {

    private final File cacheDir;

    public DiskCache(Context context) {
        //获得缓存路径
        cacheDir = context.getCacheDir();
        if (!cacheDir.exists()) {
            //如果该路径还没有,即创建该路径
            cacheDir.mkdir();


        }
    }

    @Override
    public Bitmap getBitmap(String url) {

        String name = MD5Util.getMD5String(url);
        File image = new File(cacheDir, name);

        if (!image.exists()) {
            return null;
        }

        Bitmap bitmap = BitmapFactory.decodeFile(image.getPath());
        return bitmap;
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        //把url用MD5处理一下 去掉/

        String name = MD5Util.getMD5String(url);
        //获得图片的文件
        File image = new File(cacheDir, name);

        try {
            if (!image.exists()) {
                //如果这个图片的文件不存在,就创建一个图片文件
                image.createNewFile();
            }

            //文件的 输出流
            FileOutputStream fileOutputStream = new FileOutputStream(image);

            //把图片编程二进制数组
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            //拿到图片的二进制数组
            byte[] bytes = byteArrayOutputStream.toByteArray();
            fileOutputStream.write(bytes);
            byteArrayOutputStream.close();
            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }
}
