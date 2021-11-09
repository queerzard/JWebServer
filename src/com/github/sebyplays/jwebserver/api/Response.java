package com.github.sebyplays.jwebserver.api;

import com.github.sebyplays.jwebserver.utils.ContentType;
import com.github.sebyplays.jwebserver.utils.ResponseType;
import lombok.Getter;

import java.io.File;

public class Response {

    @Getter private int statusCode;
    @Getter private byte[] response;
    @Getter private ContentType contentType = null;
    @Getter private URL redirect = null;
    @Getter private File file = null;
    @Getter private ResponseType responseType;

    public Response(Status status, String response) {
        this.statusCode = status.getStatusCode();
        this.response = response.getBytes();
        this.contentType = ContentType.TEXT_HTML;
        this.responseType = ResponseType.TEXT;
    }

    public Response(Status status, ContentType contentType, byte[] bytes) {
        this.statusCode = status.getStatusCode();
        this.response = bytes;
        this.contentType = contentType;
        this.responseType = ResponseType.BYTES;
    }

    public Response(Status status, URL redirectionUrl) {
        this.redirect = redirectionUrl;
        this.statusCode = status.getStatusCode();
        this.responseType = ResponseType.REDIRECT;
    }

    public Response(Status status, boolean download, File file){
        this.file = file;
        this.statusCode = status.getStatusCode();
        this.responseType = ResponseType.FILE;
        if(download)
            this.responseType = ResponseType.DOWNLOAD;
    }

    public Response(Status status, byte[] bytes){
        this.response = bytes;
        this.statusCode = status.getStatusCode();
        this.responseType = ResponseType.BYTES;
    }

}

