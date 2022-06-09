package com.github.sebyplays.jwebserver.api;

import com.github.sebyplays.jwebserver.utils.HtmlModel;
import com.github.sebyplays.jwebserver.utils.enums.ContentType;
import com.github.sebyplays.jwebserver.utils.enums.ResponseType;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

/**
 * It's a class that holds the response that will be sent to the client
 */
public class Response {

    @Getter @Setter private int statusCode;
    @Getter @Setter private byte[] response;
    @Getter @Setter private ContentType contentType = null;
    @Getter @Setter private URL redirect = null;
    @Getter @Setter private File file = null;
    @Getter @Setter private ResponseType responseType;

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

    public Response(HtmlModel htmlModel, boolean raw){
        this(Status.OK, htmlModel, raw);
    }

    public Response(Status status, HtmlModel htmlModel, boolean raw){
        this(status, raw ? htmlModel.getRawHtmlContent() : htmlModel.getHtmlContent());
    }

    public Response(Status status, byte[] bytes){
        this.response = bytes;
        this.statusCode = status.getStatusCode();
        this.responseType = ResponseType.BYTES;
    }

}

