package com.github.sebyplays.jwebserver;

import lombok.Getter;

public class Cookie {

    @Getter private String key;
    @Getter private String value;

    public Cookie(String keyValueSet){
        this.key = keyValueSet.split("=")[0];
        this.value = keyValueSet.split("=")[1];
    }

}
