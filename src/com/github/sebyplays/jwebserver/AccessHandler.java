package com.github.sebyplays.jwebserver;

import com.github.sebyplays.jevent.api.JEvent;
import com.github.sebyplays.jwebserver.events.ContextAccessedEvent;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public abstract class AccessHandler implements HttpHandler {

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
                cookies.add(new Cookie(cooki));
            }
            return cookies;
        }
        return null;
    }

    @SneakyThrows
    public void respond(int responseCode, String response){
        httpExchange.sendResponseHeaders(responseCode, response.getBytes().length);
        httpExchange.getResponseBody().write(response.getBytes());
        httpExchange.getResponseBody().flush();
        httpExchange.close();
    }

    @SneakyThrows
    public void fileResponse(File file){
        httpExchange.getResponseHeaders().add("Content-Disposition", "attachment; filename=" + file.getName());
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

}
