package com.n4systems.fieldid.api.pub.auth;

/**
 * Created by kirillternovsky on 2014-09-23.
 */
public class OAuthEncoder {
    private static final String unreserved = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.~";

    private OAuthEncoder() {}

    public static String encode(String source) {
        StringBuilder sb = new StringBuilder(source.length() * 2);

        for(char c : source.toCharArray()) {
            if(unreserved.indexOf(c) != -1) {
                sb.append(c);
            }
            else {
                sb.append("%");
                sb.append(Integer.toHexString((int)c).toUpperCase());
            }
        }

        return sb.toString();
    }
}
