package cn.itcast.ppx.utils;

import android.content.Context;

public class CacheUtils {

    //写缓存
    //以url为key(标识),以json为值，保存在本地
    public static void setCache(Context ctx,String key, String json){
        //有时也会保存在本地文件中，以url为文件名，以json为文件内容保存在sdcard中
        PrefUtils.setString(ctx,key,json);
    }

    //读缓存
    public static String getCache(Context ctx,String url){
        //读MD5(url)文件名
        return PrefUtils.getString(ctx,url,null);
    }

}
