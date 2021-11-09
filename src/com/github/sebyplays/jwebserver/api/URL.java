package com.github.sebyplays.jwebserver.api;

import lombok.Getter;

import java.net.MalformedURLException;

public class URL{

    @Getter private String url;

    public URL(String url){
        this.url = url;
    }

}
