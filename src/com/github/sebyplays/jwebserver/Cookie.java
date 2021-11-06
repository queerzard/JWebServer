package com.github.sebyplays.jwebserver;

import com.github.sebyplays.jwebserver.utils.SameSite;
import lombok.Getter;
import lombok.Setter;

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

    public Cookie(String key, String value){
        this.key = key;
        this.value = value;
    }

    public Cookie(String cookieSet){

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

}
