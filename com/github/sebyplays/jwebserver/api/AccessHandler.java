package com.github.sebyplays.jwebserver.api;

import com.github.sebyplays.jevent.api.JEvent;
import com.github.sebyplays.jwebserver.api.Cookie;
import com.github.sebyplays.jwebserver.api.HttpSession;
import com.github.sebyplays.jwebserver.api.Response;
import com.github.sebyplays.jwebserver.api.ResponseHandler;
import com.github.sebyplays.jwebserver.api.events.AccessInterceptionEvent;
import com.github.sebyplays.jwebserver.api.events.ContextAccessedEvent;
import com.github.sebyplays.jwebserver.api.events.HttpSessionFetchEvent;
import com.github.sebyplays.jwebserver.api.events.ServerResponseInterceptionEvent;
import com.github.sebyplays.jwebserver.utils.HtmlModel;
import com.github.sebyplays.jwebserver.utils.Utilities;
import com.github.sebyplays.jwebserver.api.annotations.CookieParam;
import com.github.sebyplays.jwebserver.api.annotations.LinkedModel;
import com.github.sebyplays.jwebserver.api.annotations.RequestParam;
import com.github.sebyplays.jwebserver.api.annotations.methods.RequestMethod;
import com.github.sebyplays.jwebserver.utils.enums.ContentType;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.Getter;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.*;

/**
 * It handles the request, and then calls the handle function in the class that extends this class
 */
public abstract class AccessHandler implements HttpHandler, ResponseHandler {

    @Getter private HttpExchange httpExchange;
    @Getter private HashMap<String, String> queryMap;
    @Getter private String query = "";

    @Getter private byte[] requestBody;

    @Getter private HtmlModel htmlModel;

    @Getter private JSONObject jsonResponse = new JSONObject();

    @Getter private HttpSession userSession;
    @Getter private String usersIpAddress;


    public AccessHandler() {
    }

    /**
     * If the list of cookies is not null, then for each cookie in the list, if the cookie's key is equal to the key we're
     * looking for, then return the cookie.
     *
     * @param key The key of the cookie you want to get.
     * @return A cookie object
     */
    public Cookie getCookie(String key) {
        List<Cookie> cookies;
        if ((cookies = getCookies()) != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getKey().equalsIgnoreCase(key)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    /**
     * This function sets a cookie with the given key and value.
     *
     * @param key The name of the cookie
     * @param value The value of the cookie.
     */
    @SneakyThrows
    public void setCookie(String key, Object value) {
        Cookie cookie = new Cookie(key, value.toString());
        setCookie(cookie);
    }

    /**
     * Add a cookie to the response headers.
     *
     * @param cookie The cookie to set.
     */
    @SneakyThrows
    public void setCookie(Cookie cookie) {
        httpExchange.getResponseHeaders().add("Set-Cookie", cookie.toString());
    }

    /**
     * Remove a cookie from the response.
     *
     * @param key The key of the cookie to remove.
     */
    @SneakyThrows
    public void removeCookie(String key) {
        setCookie(key, null);
    }

    /**
     * It gets the cookie header from the request, splits it into an array of strings, and then creates a list of cookies
     * from the array
     *
     * @return A list of cookies
     */
    public List<Cookie> getCookies() {
        List<String> cook = null;
        if ((cook = httpExchange.getRequestHeaders().get("Cookie")) != null) {
            String[] cooks = cook.toString().replaceAll("\\[", "").replaceAll("]", "").split("; ");
            List<Cookie> cookies = new ArrayList();
            for (String cooki : cooks) {
                cookies.add(new Cookie(cooki.split("=")[0], cooki.split("=")[1]));
            }
            return cookies;
        }
        return null;
    }

    /**
     * If the response is null, respond with a 500 error. If the response is a file, respond with the file. If the response
     * is text, respond with the text. If the response is bytes, respond with the bytes. If the response is a redirect,
     * redirect. If the response is a download, download the file
     *
     * @param response The response object that you created in your controller.
     */
    public void respond(Response response) {
        if (response == null) {
            respond(500, "Internal Server Error");
            return;
        }

        switch (response.getResponseType()) {
            case FILE:
                fileResponse(response.getFile());
                return;

            case TEXT:
                respond(response.getStatusCode(), new String(response.getResponse()));
                return;

            case BYTES:
                if (response.getContentType() != null) {
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

    /**
     * Respond to the client with the given response code and response body.
     *
     * @param responseCode The HTTP response code.
     * @param response The response to send back to the client.
     */
    @SneakyThrows
    public void respond(int responseCode, String response) {
        respond(responseCode, ContentType.TEXT_HTML, response.getBytes());
    }

    /**
     * Sets the content type and then responds with the given response code and response.
     *
     * @param responseCode The HTTP response code.
     * @param contentType The type of content you're sending back.
     * @param response The response body.
     */
    public void respond(int responseCode, ContentType contentType, byte[] response) {
        setContentType(contentType);
        respond(responseCode, response);
    }

    /**
     * @param url The URL to redirect to.
     */
    public void redirect(String url) {
        respond(200, "<meta http-equiv=\"Refresh\" content=\"3; url=" + url + "\">");
    }

    /**
     * Sends a file to the client, with the Content-Disposition header set to attachment, so that the browser will download
     * the file instead of displaying it.
     *
     * @param file The file to be downloaded.
     */
    @SneakyThrows
    public void fileDownloadResponse(File file) {
        httpExchange.getResponseHeaders().add("Content-Disposition", "attachment; filename=" + file.getName());
        fileResponse(file);
    }

    /**
     * This function adds a header to the response with the key 'Content-Type' and the value of the contentType parameter.
     *
     * @param contentType The content type of the response.
     */
    public void setContentType(ContentType contentType) {
        httpExchange.getResponseHeaders().add("Content-Type", contentType.getContentType());
    }

    /**
     * Send a response to the client with the given response code and response bytes.
     *
     * @param responseCode The HTTP response code.
     * @param responseBytes The bytes to send back to the client.
     */
    @SneakyThrows
    public void respond(int responseCode, byte[] responseBytes) {
        httpExchange.sendResponseHeaders(responseCode, responseBytes.length);
        httpExchange.getResponseBody().write(responseBytes);
        httpExchange.getResponseBody().flush();
        httpExchange.close();
    }

    /**
     * Send a file to the client.
     *
     * @param file The file to be sent to the client.
     */
    @SneakyThrows
    public void fileResponse(File file) {
        httpExchange.sendResponseHeaders(200, file.length());
        Files.copy(file.toPath(), httpExchange.getResponseBody());
        httpExchange.getResponseBody().flush();
        httpExchange.close();
    }

    /**
     * It sets the httpExchange, requestBody, query, and jsonResponse variables, and then calls the ContextAccessedEvent
     *
     * @param httpExchange The HttpExchange object that is used to communicate with the client.
     * @return A boolean value.
     */
    public boolean setHttpExchange(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
        this.usersIpAddress = httpExchange.getRemoteAddress().getAddress().getHostAddress();
        this.requestBody = readAllBytes(httpExchange.getRequestBody());
        query = httpExchange.getRequestURI().getQuery();
        this.jsonResponse = new JSONObject();
        return (new JEvent(new ContextAccessedEvent(this)).callEvent().getEvent().isCancelled());
    }

    /**
     * It takes a string of the form "key1=value1&key2=value2&key3=value3" and returns a HashMap of the form {key1=value1,
     * key2=value2, key3=value3}
     *
     * @return A HashMap of the query parameters.
     */
    public HashMap<String, String> getQueryMap() {
        if (getQuery() != null && !getQuery().equals(""))
            return getMapFromString(getQuery());
        return null;
    }

    /**
     * It takes a string, decodes it, and then returns a map of the decoded string
     *
     * @return A HashMap of the POST parameters.
     */
    @SneakyThrows
    @Deprecated
    public HashMap<String, String> getPostMap() {
        if (httpExchange.getRequestMethod().equals("POST"))
            return getMapFromString(decode(new String(this.requestBody)));
        return null;
    }

    /**
     * It takes the request body, decodes it, and then returns a map of the decoded request body
     *
     * @return A HashMap of the request body.
     */
    public HashMap<String, String> getRequestBodyMap() {
        return getMapFromString(decode(new String(this.requestBody)));
    }

    /**
     * If the string is not null, decode it using UTF-8, otherwise return an empty string.
     *
     * @param s The string to decode
     * @return The decoded string.
     */
    public String decode(String s) {
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    /**
     * It takes a string of the form "key1=value1&key2=value2&key3=value3" and returns a HashMap with the keys and values
     *
     * @param string The string to be parsed.
     * @return A HashMap of the query string.
     */
    protected HashMap<String, String> getMapFromString(String string) {
        String[] quers = string.split("&");
        HashMap<String, String> hashMap = new HashMap<>();
        for (String str : quers)
            hashMap.put(str.split("=")[0], str.split("=").length == 2 ? str.split("=")[1] : null);
        this.queryMap = hashMap;
        return hashMap;
    }

    /**
     * Read all the bytes from the input stream and return them as a byte array.
     *
     * @param inputStream The input stream to read from.
     * @return A byte array of the bytes read from the input stream.
     */
    @SneakyThrows
    public byte[] readAllBytes(InputStream inputStream) {
        byte[] bytes = new byte[inputStream.available()];
        int read = 0;

        while (inputStream.available() > 0) {
            bytes[read] = (byte) inputStream.read();
            read++;
        }
        return bytes;
    }


    /**
     * It gets all the methods in the class, and if the method is named "handle" and has the annotation @LinkedModel, it
     * gets the value of the annotation and uses it to get the HTMLModel from the cache
     */
    public void handleAnnotations(){
        Method[] methods = this.getClass().getMethods();
        for (Method method : methods) {
            if(method.getName().equals("handle") && method.isAnnotationPresent(LinkedModel.class)){
                LinkedModel linkedModel = method.getAnnotation(LinkedModel.class);
                HtmlModel htmlModel;
                if((htmlModel = Utilities.getCachedHTMLModel(linkedModel.value())) != null){
                    this.htmlModel = htmlModel;
                    this.htmlModel.clearAttributes();
                }
            }
        }

    }


    public void handleHttpSession(){
        Cookie cookie = getCookie("JSESSIONID");
        String identifier = UUID.randomUUID().toString().replaceAll("-", "");

        this.userSession = HttpSession.getSession(cookie != null ? cookie.getValue() : identifier, this.usersIpAddress);
        this.userSession.setLastFetched(System.currentTimeMillis());
        setCookie(this.userSession.getSessionCookie());
        new JEvent(new HttpSessionFetchEvent(this)).callEvent();
    }

    /**
     * It handles the request, and then calls the handle function in the class that extends this class
     *
     * @param httpExchange The HttpExchange object that is passed to the handle method.
     */
    @SneakyThrows
    @Override
    public void handle(HttpExchange httpExchange) {
        if (setHttpExchange(httpExchange)) return;
        handleHttpSession();
        AccessInterceptionEvent acInterceptEvent = (AccessInterceptionEvent) new JEvent(new AccessInterceptionEvent(this)).callEvent().getEvent();
        if(acInterceptEvent.isCancelled()){
            respond(acInterceptEvent.getResponse());
            return;
        }

        handleAnnotations();
        HashMap<String, Object> returnValuesOfInvokedMethods = new HashMap<>();
        HashMap<String, String> map = new HashMap<>();
        for (Method method : getClass().getMethods()) {
            if (method.isAnnotationPresent(RequestMethod.class)) {

                RequestMethod requestMethod = method.getAnnotation(RequestMethod.class);

                if(getQueryMap() != null)
                    map.putAll(getQueryMap());
                map.putAll(getRequestBodyMap());

                List<String> methodParamsList = new ArrayList<>();

                for(Parameter parameter : method.getParameters()){
                    if(parameter.isAnnotationPresent(RequestParam.class)){
                        methodParamsList.add(map.get(parameter.getAnnotation(RequestParam.class).value()));
                    }
                    if(parameter.isAnnotationPresent(CookieParam.class)){
                        Cookie cookie = getCookie(parameter.getAnnotation(CookieParam.class).value());
                        if(cookie != null){
                            methodParamsList.add(cookie.getValue());
                        } else {
                            methodParamsList.add("null");
                        }
                    }
                }

                Object[] params = new Object[method.getParameterCount()];
                if(httpExchange.getRequestMethod().equalsIgnoreCase(
                        requestMethod.value().equalsIgnoreCase("ALL") ? httpExchange.getRequestMethod() : requestMethod.value())){
                    try {
                        if (methodParamsList.size() < 1 || methodParamsList.isEmpty()){}
                        else {
                            String[] paramsArray = methodParamsList.toArray(new String[methodParamsList.size()]);
                            for(int i = 0; i < paramsArray.length; i++)
                                params[i] = paramsArray[i];
                        }
                        returnValuesOfInvokedMethods.put(method.getName(), method.invoke(this, params));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        ServerResponseInterceptionEvent srInterceptEvent = (ServerResponseInterceptionEvent)
                new JEvent(new ServerResponseInterceptionEvent(handle(httpExchange, this,
                        returnValuesOfInvokedMethods),
                        this)).callEvent().getEvent();

        respond(srInterceptEvent.getResponse());
    }

}
