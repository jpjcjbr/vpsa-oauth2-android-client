package br.com.vpsa.oauth2login.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

import android.os.Bundle;


public final class Utils {
	
    private static Bundle decodeUrl(String s) {
        Bundle params = new Bundle();
        if (s != null) {
            String array[] = s.split("&");
            for (String parameter : array) {
                String v[] = parameter.split("=");
                // YG: in case param has no value
                if (v.length==2){
                	params.putString(URLDecoder.decode(v[0]),
                                 URLDecoder.decode(v[1]));
                }
                else {
                	params.putString(URLDecoder.decode(v[0])," ");
                }
            }
        }
        return params;
    }

    public static Bundle parseUrl(String url) {
        // hack to prevent MalformedURLException
        url = url.replace("fbconnect", "http"); 
        try {
            URL u = new URL(url);
            Bundle b = decodeUrl(u.getQuery());
            b.putAll(decodeUrl(u.getRef()));
            return b;
        } catch (MalformedURLException e) {
            return new Bundle();
        }
    }
}
