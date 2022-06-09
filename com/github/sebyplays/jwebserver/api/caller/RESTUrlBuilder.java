package com.github.sebyplays.jwebserver.api.caller;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class RESTUrlBuilder {

    @Getter private String baseUrl;
    @Getter private Map<String, String> parameters = new HashMap<>();

    public RESTUrlBuilder(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * This function adds a key-value pair to the parameters map and returns the RESTUrlBuilder object.
     *
     * @param key The key of the parameter
     * @param value The value of the parameter.
     * @return The RESTUrlBuilder object.
     */
    public RESTUrlBuilder param(String key, String value) {
        parameters.put(key, value);
        return this;
    }

    /**
     * > It takes a base URL and a map of parameters and returns a URL with the parameters appended to the base URL
     *
     * @return A string that is the url with the parameters added to it.
     */
    public String build() {
        StringBuilder url = new StringBuilder(baseUrl);
        if (parameters.size() > 0) {
            url.append("?");
            parameters.forEach((key, value) -> url.append(key).append("=").append(value).append("&"));
            url.deleteCharAt(url.length() - 1);
        }
        return url.toString();
    }



}
