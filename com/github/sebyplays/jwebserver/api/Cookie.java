package com.github.sebyplays.jwebserver.api;

import com.github.sebyplays.jwebserver.utils.enums.SameSite;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Cookie {

    @Getter @Setter private String key;
    @Getter @Setter private String value;

    @Getter @Setter private Date expires;
    @Getter @Setter private Integer maxAge;

    @Getter @Setter private String domain;
    @Getter @Setter private String path;
    @Getter @Setter private String sameSite;

    @Getter @Setter private boolean secure;
    @Getter @Setter private boolean httpOnly;

    public Cookie(){}

    public Cookie(String key, String value){
        this.key = key;
        this.value = value;
    }

    public Cookie(String key, String value, long expires, int maxAge, String domain, String path, SameSite sameSite, boolean secure, boolean httpOnly){
        this(key, value, new Date(expires), maxAge, domain, path, sameSite, secure, httpOnly);
    }

    public Cookie(String key, String value, Date expires, int maxAge, String domain, String path, SameSite sameSite, boolean secure, boolean httpOnly){
        this.domain = domain;
        this.value = value;
        this.key = key;
        this.secure = secure;
        this.path = path;
        this.expires = expires;
        this.httpOnly = httpOnly;
        this.maxAge = maxAge;
        this.sameSite = sameSite.getProperty();
    }

    /**
     * If the cookie has an expiration date, add it to the string. If the cookie has a max age, add it to the string. If
     * the cookie has a domain, add it to the string. If the cookie has a path, add it to the string. If the cookie has a
     * same site, add it to the string. If the cookie is secure, add it to the string. If the cookie is http only, add it
     * to the string.
     *
     * @return A string representation of the cookie.
     */
    public String toString(){
        StringBuilder c = new StringBuilder();
        c.append(this.key + "=" + this.value);
        if(this.expires != null) c.append("; Expires=" + new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss").format(this.expires) + " GMT");
        if(this.maxAge != null) c.append("; Max-Age=" + this.maxAge);
        if(this.domain != null) c.append("; Domain=" + this.domain);
        if(this.path != null) c.append("; Path=" + this.path);
        if(this.sameSite != null) c.append("; SameSite=" + this.sameSite);
        if(this.secure) c.append("; Secure");
        if(this.httpOnly) c.append("; HttpOnly");
        return c.toString();
    }

    /**
     * The function takes a string as input, splits it into an array of strings, and then splits each string into a key and
     * a value.
     *
     * The first string in the array is the key and value of the cookie. The rest of the strings are the attributes of the
     * cookie.
     *
     * The function then sets the key and value of the cookie, and then sets the attributes of the cookie.
     *
     * The function returns the cookie.
     *
     * @param cookieString The string that contains the cookie.
     * @return A cookie object
     */
    public static Cookie parse(String cookieString){
        Cookie cookie1 = new Cookie();
        String[] cookieArr = cookieString.split(";");
        String[] keyValue = cookieArr[0].split("=");
        cookie1.key = keyValue[0];
        cookie1.value = keyValue[1];
        for(int i = 1; i < cookieArr.length; i++){
            String[] kv = cookieArr[i].split("=");
            switch(kv[0].toLowerCase()){
                case "expires":
                    cookie1.expires = new Date(Long.parseLong(kv[1]));
                    break;
                case "max-age":
                    cookie1.maxAge = Integer.parseInt(kv[1]);
                    break;
                case "domain":
                    cookie1.domain = kv[1];
                    break;
                case "path":
                    cookie1.path = kv[1];
                    break;
                case "samesite":
                    cookie1.sameSite = kv[1];
                    break;
                case "secure":
                    cookie1.secure = true;
                    break;
                case "httponly":
                    cookie1.httpOnly = true;
                    break;
            }
        }
        return cookie1;
    }

    //toJSON
    public String toJSON(){
        return new JSONObject()
        .put("key", this.key)
        .put("value", this.value)
        .put("expires", this.expires)
        .put("max-age", this.maxAge)
        .put("domain", this.domain)
        .put("path", this.path)
        .put("samesite", this.sameSite)
        .put("secure", this.secure)
        .put("httponly", this.httpOnly).toString();
    }


}
