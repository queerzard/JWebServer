package com.github.sebyplays.jwebserver.api.caller;

import com.github.sebyplays.jwebserver.api.Cookie;
import com.github.sebyplays.jwebserver.utils.enums.ContentType;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RESTCaller {


    public RESTCaller() {
    }


    public static RESTRequest GET(RESTUrlBuilder urlBuilder, boolean urlParams) {
        return makeCall(urlBuilder, "GET", urlParams);
    }

    public static RESTRequest GET(String url, Map<String, String> params, boolean urlParams) {
        return makeCall(url, "GET", params, urlParams);
    }

    public static RESTRequest POST(RESTUrlBuilder urlBuilder, boolean urlParams){
        return makeCall(urlBuilder, "POST", urlParams);
    }

    public static RESTRequest POST(String url, Map<String, String> params, boolean urlParams) {
        return makeCall(url, "POST", params, urlParams);
    }

    public static RESTRequest PUT(RESTUrlBuilder urlBuilder, boolean urlParams) {
        return makeCall(urlBuilder, "PUT", urlParams);
    }

    public static RESTRequest PUT(String url, Map<String, String> params, boolean urlParams) {
        return makeCall(url, "PUT", params, urlParams);
    }

    /**
     * Make a DELETE request to the given URL.
     *
     * @return A RESTRequest object.
     */
    public static RESTRequest DELETE(RESTUrlBuilder urlBuilder, boolean urlParams) {
        return makeCall(urlBuilder, "DELETE", urlParams);
    }

    /**
     * It makes a DELETE request to the given url with the given parameters.
     *
     * @param url The URL to make the request to.
     * @param params A map of key-value pairs that will be sent as parameters to the server.
     * @param urlParams If true, the params will be appended to the url. If false, they will be sent in
     * the body of the request.
     * @return A RESTRequest object
     */
    public static RESTRequest DELETE(String url, Map<String, String> params, boolean urlParams) {
        return makeCall(url, "DELETE", params, urlParams);
    }


    /**
     * > This function takes a RESTUrlBuilder object, a method, and a boolean, and returns a RESTRequest object
     *
     * @param urlBuilder The RESTUrlBuilder object that contains the base url and the parameters to be sent.
     * @param method The HTTP method to use (GET, POST, PUT, DELETE)
     * @param urlParams If true, the parameters will be appended to the URL. If false, they will be sent in the body of the request.
     * @return A RESTRequest object.
     */
    public static RESTRequest makeCall(RESTUrlBuilder urlBuilder, String method, boolean urlParams) {
        return makeCall(urlBuilder.getBaseUrl(), method, urlBuilder.getParameters(), urlParams);
    }

    /**
     * "Make a REST call to the given URL with the given method, parameters, and headers, and return the response."
     *
     * The first thing we do is create a new RESTRequest object. This is the object that will hold the response from the
     * REST call
     *
     * @param url The URL to make the call to.
     * @param method The HTTP method to use (GET, POST, PUT, DELETE, etc.)
     * @param params A map of key-value pairs that will be sent as parameters to the REST API.
     * @param urlParams If true, the params will be appended to the url as query parameters. If false, they will be sent in the body of the request.
     * @return A RESTRequest object
     */
    @SneakyThrows
    public static RESTRequest makeCall(String url, String method, Map<String, String> params, boolean urlParams) {
        return makeCall(url, method, params, null, urlParams);
    }

    /**
     * "Make a REST call to the given URL with the given method, parameters, and headers, and return the response."
     *
     * The first thing we do is create a new RESTRequest object. This is the object that will hold the response from the
     * REST call
     *
     * @param url The URL to make the call to.
     * @param method The HTTP method to use (GET, POST, PUT, DELETE, etc.)
     * @param params The parameters to be sent to the server.
     * @param headers A map of headers to be sent with the request.
     * @param urlParams If true, the params will be appended to the url as query parameters. If false, they will be sent in the body of the request.
     * @return A RESTRequest object
     */
    @SneakyThrows
    public static RESTRequest makeCall(String url, String method, Map<String, String> params, Map<String, String> headers, boolean urlParams) {
        return makeCall(url, method, params, headers, null, urlParams);
    }

    /**
     * "Make a call to the given URL with the given method, parameters, headers, and cookies, and return the response."
     *
     * The first thing we do is create a new RESTRequest object. This is the object that will hold the response from the
     * server
     *
     * @param url The URL to call
     * @param method The HTTP method to use (GET, POST, PUT, DELETE, etc.)
     * @param params The parameters to be sent in the request.
     * @param headers A map of headers to be sent with the request.
     * @param cookies A map of cookies to be sent with the request.
     * @param urlParams If true, the params will be appended to the url as query parameters. If false, they will be
     *                  contained in the body of the request.
     * @return A RESTRequest object
     */
    @SneakyThrows
    public static RESTRequest makeCall(String url, String method, Map<String, String> params, Map<String, String> headers, Map<String, String> cookies, boolean urlParams) {
        return makeCall(url, method, params, cookies, headers, null, urlParams);
    }


    /**
     *
     * @param restRequest The object that contains all the information about the request.
     * @return A RESTRequest object
     */
    public static RESTRequest makeCall(RESTRequest restRequest) {
        return makeCall(restRequest.getUrl(), restRequest.getMethod(), restRequest.getParams(), restRequest.getCookies(), restRequest.getHeaders(), restRequest.getContentType(), restRequest.isQueryParams());
    }


    /**
     * It makes a call to a REST API.
     *
     * @param url The url to make the request to
     * @param method GET, POST, PUT, DELETE, etc.
     * @param params The parameters to be sent in the request body.
     * @param cookies a map of cookies to be sent with the request
     * @param headers A map of headers to be sent with the request.
     * @param contentType The content type of the request.
     * @param queryParameters If true, the parameters will be appended to the url as a query string. If false, the
     * parameters will be sent in the request body.
     * @return A RESTRequest object
     */
    public static RESTRequest makeCall(String url, String method, Map<String, String> params, Map<String, String> cookies,Map<String, String> headers, ContentType contentType, boolean queryParameters) {
        try {
            URL obj = new URL(!queryParameters ? url : url + getQuery(params));
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            //disable response code exceptions
            con.setRequestMethod(method);
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            //set content type
            if (contentType != null)
                con.setRequestProperty("Content-Type", contentType.getContentType());
                //set cookies
            if (cookies != null) {
                for (Map.Entry<String, String> entry : cookies.entrySet()) {
                    con.setRequestProperty("Cookie", entry.getKey() + "=" + entry.getValue());
                }
            }

            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    con.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            if(!queryParameters && params != null) {
                //put params in request body
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("charset", "utf-8");
                con.setRequestProperty("Content-Length", "" + (getQuery(params).substring(1).getBytes().length));
                con.setUseCaches(false);
                con.setConnectTimeout(5000);
                con.setReadTimeout(5000);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(getQuery(params).substring(1));
                wr.flush();
                wr.close();
            }

            con.disconnect();
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            RESTResponse rsp = new RESTResponse(getResponse(con), getCookies(getCookies(con)), con.getResponseCode(), con.getHeaderFields());
            RESTRequest restRequest = new RESTRequest(rsp, url, method, params, cookies, headers, contentType, queryParameters);
            return restRequest;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * It takes an HttpURLConnection object and returns a map of the cookies that were sent in the response
     *
     * @param con The HttpURLConnection object that you want to get the cookies from.
     * @return A map of cookies.
     */
    public static Map<String, String> getCookies(HttpURLConnection con) {
        Map<String, String> cookies = new HashMap<>();
        if (con.getHeaderFields().get("Set-Cookie") != null) {
            for (String cookie : con.getHeaderFields().get("Set-Cookie")) {
                String[] cookieParts = cookie.split(";");
                cookies.put(cookieParts[0].split("=")[0], cookieParts[0].split("=")[1]);
            }
        }
        return cookies;
    }
    //cookie map to cookie object

    /**
     * It takes a map of cookie names and values and returns an array of Cookie objects
     *
     * @param cookies A map of cookie names and values.
     * @return A Cookie[] array
     */
    public static Cookie[] getCookies(Map<String, String> cookies) {
        Cookie[] cookieArray = new Cookie[cookies.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            cookieArray[i] = new Cookie(entry.getKey(), entry.getValue());
            i++;
        }
        return cookieArray;
    }

    /**
     * It takes a string of cookies, splits them into an array, then splits each cookie into a key and value, and returns
     * an array of cookies
     *
     * @param cookieString The string of cookies you want to parse.
     * @return A list of cookies
     */
    public static Cookie[] getCookies(String cookieString) {
        String[] cookies = cookieString.split(";");
        Cookie[] cookieList = new Cookie[cookies.length];
        for (int i = 0; i < cookies.length; i++) {
            String[] cookie = cookies[i].split("=");
            cookieList[i] = new Cookie(cookie[0], cookie[1]);
        }
        return cookieList;
    }

    /**
     * It takes a map of key-value pairs and returns a string of the form "?key1=value1&key2=value2&key3=value3"
     *
     * @param params The parameters to be sent to the server.
     * @return A string of the query parameters.
     */
    private static String getQuery(Map<String, String> params) {
        String query = "";
        if (params != null)
            for (Map.Entry<String, String> entry : params.entrySet())
                query += entry.getKey() + "=" + entry.getValue() + "&";
        query = query.substring(0, query.length() - 1);
        query = "?" + query;
        return query;
    }

    @SneakyThrows
    /**
     * It reads the response from the server and returns it as a string.
     *
     * @param con The connection to the server.
     * @return The response from the server.
     */
    public static String getResponse(HttpURLConnection con) {
        return new String(readBytes(con.getInputStream()));
    }

    /**
     * Read all bytes from an InputStream and return them as a byte array.
     *
     * @param is The input stream to read from.
     * @return A byte array
     */
    public static byte[] readBytes(InputStream is) throws IOException {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[0xFFFF];
            for (int len; (len = is.read(buffer)) != -1;)
                os.write(buffer, 0, len);
            return os.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            is.close();
        }
        return null;
    }

    public static void main(String[] args) {
        //make call
        RESTResponse response = makeCall("http://google.com:80", "GET", null, false).getLastResponse();
        System.out.println(response.getResponse());

        Map<String, String> params = new HashMap<>();
        params.put("token", "TOK");
        System.out.println(makeCall("http://mcsilent.de:41414/api/server", "GET", params, true));
    }

}
