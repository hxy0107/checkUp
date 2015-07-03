package com.alipay.security;

/**
 * Created by xianyu.hxy on 2015/6/10.
 */
public class Base64 {
    public static String toBase64(String normol)
    {
        byte[] bytes=android.util.Base64.encode(normol.getBytes(), android.util.Base64.DEFAULT);
        String s=new String(bytes);
        return s;
    }

}
