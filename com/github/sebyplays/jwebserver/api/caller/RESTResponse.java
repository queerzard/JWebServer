package com.github.sebyplays.jwebserver.api.caller;

import com.github.sebyplays.jwebserver.api.Cookie;
import lombok.Getter;

import java.util.List;
import java.util.Map;

public class RESTResponse {
    @Getter private String response;
    @Getter private Cookie[] cookies;
    @Getter private int responseCode;

    @Getter private Map<String, List<String>> responseHeaders;

    @Getter private RESTCaller restCaller;

    public RESTResponse(String response, Cookie[] cookies, int responseCode, Map<String, List<String>> headers) {
        this.response = response;
        this.cookies = cookies;
        this.responseCode = responseCode;
        this.responseHeaders = headers;
    }

    /**
     * This function returns a string representation of the object
     *
     * @return The response, cookies, response code, and response headers.
     */
    @Override
    public String toString() {
        return "RESTResponse{" +
                "response='" + response + '\'' +
                ", cookies=" + cookies +
                ", responseCode=" + responseCode +
                ", responseHeaders=" + responseHeaders +
                '}';
    }


}
