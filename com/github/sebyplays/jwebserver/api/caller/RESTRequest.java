package com.github.sebyplays.jwebserver.api.caller;

import com.github.sebyplays.jwebserver.utils.enums.ContentType;
import lombok.Getter;

import java.util.Map;

public class RESTRequest {
    @Getter private String url;
    @Getter private String method;
    @Getter private Map<String, String> params;
    @Getter private Map<String, String> cookies;
    @Getter private Map<String, String> headers;
    @Getter private ContentType contentType;
    @Getter private boolean queryParams;
    @Getter private RESTResponse lastResponse;

    public RESTRequest(RESTResponse resp, String url, String method, Map<String, String> params,
                       Map<String, String> cookies, Map<String, String> headers, ContentType contentType, boolean queryParameters){
        this.url = url;
        this.method = method;
        this.params = params;
        this.cookies = cookies;
        this.headers = headers;
        this.contentType = contentType;
        this.queryParams = queryParameters;
        this.lastResponse = resp;
    }

    /**
     * > This function makes a call to the REST API and returns the response
     *
     * @return The last response from the REST call.
     */
    public RESTResponse makeCall(){
        RESTResponse resp = RESTCaller.makeCall(this).lastResponse;

        this.lastResponse = resp;
        return resp;
    }
}
