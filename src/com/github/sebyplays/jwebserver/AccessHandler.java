package com.github.sebyplays.jwebserver;

import com.github.sebyplays.jevent.api.JEvent;
import com.github.sebyplays.jwebserver.api.Response;
import com.github.sebyplays.jwebserver.api.ResponseHandler;
import com.github.sebyplays.jwebserver.events.ContextAccessedEvent;
import com.github.sebyplays.jwebserver.utils.ContentType;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.Getter;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public abstract class AccessHandler implements HttpHandler, ResponseHandler {

    @Getter private HttpExchange httpExchange;
    @Getter private HashMap<String, String> queryMap;
    @Getter private String query = "";

    @Getter private JSONObject jsonResponse = new JSONObject();
    public AccessHandler() {
    }

    public Cookie getCookie(String key){
        List<Cookie> cookies;
        if((cookies = getCookies()) != null){
            for(Cookie cookie : cookies){
                if(cookie.getKey().equalsIgnoreCase(key)){
                    return cookie;
                }
            }
        }
        return null;
    }

    @SneakyThrows
    public void setCookie(String key, Object value){
        httpExchange.getResponseHeaders().add("Set-Cookie", key + "=" + value);
    }

    @SneakyThrows
    public void setCookie(Cookie cookie){
        httpExchange.getResponseHeaders().add("Set-Cookie", cookie.toString());
    }

    @SneakyThrows
    public void removeCookie(String key){
        setCookie(key, null);
    }

    //TODO The problem is occurring in this piece of code whilst trying to parse the cookies.
    public List<Cookie> getCookies(){
        List<String> cook = null;
        if((cook = httpExchange.getRequestHeaders().get("Cookie")) != null){
            String[] cooks = cook.toString().replaceAll("\\[", "").replaceAll("]", "").split("; ");
            List<Cookie> cookies = new ArrayList();
            for(String cooki : cooks){
                cookies.add(new Cookie(cooki.split("=")[0], cooki.split("=")[1]));
            }
            return cookies;
        }
        return null;
    }

    public void respond(Response response){
        if(response == null){
            respond(500, "Internal Server Error");
            return;
        }

        switch (response.getResponseType()){
            case FILE:
                fileResponse(response.getFile());
                return;

            case TEXT:
                respond(response.getStatusCode(), new String(response.getResponse()));
                return;

            case BYTES:
                if(response.getContentType() != null){
                    respond(response.getStatusCode(), response.getContentType(), response.getResponse());
                    return;
                }
                respond(response.getStatusCode(), response.getResponse());
                return;

            case REDIRECT:
                redirect(response.getRedirect().getUrl());
                return;

            case DOWNLOAD:
                fileDownloadResponse(response.getFile());
        }
    }

    @SneakyThrows
    public void respond(int responseCode, String response){
        respond(responseCode, ContentType.TEXT_HTML, response.getBytes());
    }

    public void respond(int responseCode, ContentType contentType, byte[] response){
        setContentType(contentType);
        respond(responseCode, response);
    }

    public void redirect(String url) {
        respond(200, "<meta http-equiv=\"Refresh\" content=\"3; url=" + url + "\">");
    }

    @SneakyThrows
    public void fileDownloadResponse(File file){
        httpExchange.getResponseHeaders().add("Content-Disposition", "attachment; filename=" + file.getName());
        fileResponse(file);
    }

    public void setContentType(ContentType contentType){
        httpExchange.getResponseHeaders().add("Content-Type", contentType.getContentType());
    }

    @SneakyThrows
    public void respond(int responseCode, byte[] responseBytes){
        httpExchange.sendResponseHeaders(responseCode, responseBytes.length);
        httpExchange.getResponseBody().write(responseBytes);
        httpExchange.getResponseBody().flush();
        httpExchange.close();
    }

    @SneakyThrows
    public void fileResponse(File file){
        httpExchange.sendResponseHeaders(200, file.length());
        Files.copy(file.toPath(), httpExchange.getResponseBody());
        httpExchange.getResponseBody().flush();
        httpExchange.close();
    }

    public boolean setHttpExchange(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
        query = httpExchange.getRequestURI().getQuery();
        this.jsonResponse = new JSONObject();
        return (new JEvent(new ContextAccessedEvent(this)).callEvent().getEvent().isCancelled());
    }

    public HashMap<String, String> getQueryMap(){
        if(getQuery() != null && !getQuery().equals("")){
            String[] quers = getQuery().split("&");
            HashMap<String, String> hashMap = new HashMap<>();
            for(String str : quers)
                hashMap.put(str.split("=")[0], str.split("=").length == 2 ? str.split("=")[1] : null);
            this.queryMap = hashMap;
            return hashMap;
        }
        return null;
    }

    @Override
    public void handle(HttpExchange httpExchange) {
        if(setHttpExchange(httpExchange)) return;
        respond(handle(httpExchange, this));
    }

}
