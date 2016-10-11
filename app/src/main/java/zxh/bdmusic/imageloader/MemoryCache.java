package zxh.bdmusic.imageloader;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by dllo on 16/9/22.
 * ImageLoader所需要的L1缓存
 */
public class MemoryCache implements ImageLoader.ImageCache {
    private LruCache<String, Bitmap> lruCache;

    public MemoryCache() {
        long maxMemory = Runtime.getRuntime().maxMemory() / 1024; //获取程序运行时所能使用的最大内存   除以1024是为了将单位转换成kb

        lruCache = new LruCache<String, Bitmap>((int) (maxMemory / 10)) {

            //用来确定每一个元素所占用的空间
            @Override
            protected int sizeOf(String key, Bitmap value) {
//                return value.getRowBytes() * value.getHeight() / 1024;

                //返回值是bitmap所占用的空间,除以1024是为了将单位转换成kb
                //来与最大容量单位保持一致
                return value.getByteCount() / 1024;
            }
        };
    }


    @Override
    public Bitmap getBitmap(String url) {


        return lruCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        lruCache.put(url, bitmap);
    }
}
