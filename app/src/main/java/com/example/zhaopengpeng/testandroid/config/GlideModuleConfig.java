package com.example.zhaopengpeng.testandroid.config;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp3.OkHttpGlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;

/*****************************************
 * 文  件： 
 * 描  述： 
 * 作  者： zhaopengpeng 
 * 日  期： 2017/11/25
 *****************************************/

public class GlideModuleConfig extends OkHttpGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // 定义缓存大小和位置
        int DISK_SIZE = 1024 * 1024 * 250;
        int MEMORY_SIZE = (int) (Runtime.getRuntime().maxMemory()) / 4;  // 取1/4最大内存作为最大缓存
        String CACHE_PATH = "/images";

        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, CACHE_PATH, DISK_SIZE)); //sd卡磁盘

        // 自定义内存和图片池大小
        builder.setMemoryCache(new LruResourceCache(MEMORY_SIZE));
        builder.setBitmapPool(new LruBitmapPool(MEMORY_SIZE));

        // 定义图片格式
        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
