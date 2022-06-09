package com.github.sebyplays.jwebserver.test.testHandler;

import com.github.sebyplays.jwebserver.api.AccessHandler;
import com.github.sebyplays.jwebserver.api.Response;
import com.github.sebyplays.jwebserver.api.Status;
import com.github.sebyplays.jwebserver.api.URL;
import com.github.sebyplays.jwebserver.api.annotations.AccessController;
import com.sun.net.httpserver.HttpExchange;
import lombok.SneakyThrows;

import java.util.HashMap;


@AccessController(index = "processlogin")
public class ProcessLogin extends AccessHandler {

    @SneakyThrows
    @Override
    public Response handle(HttpExchange httpExchange, AccessHandler accessHandler, HashMap<String, Object> args) {
        HashMap<String, String> props = getQueryMap();
        if (httpExchange.getRequestMethod().equals("POST")) {
            System.out.println(getPostMap());
        }
        if(props != null && props.containsKey("username") && props.containsKey("password")) {
            if(props.get("username").equals("admin") && props.get("password").equals("admin")) {
                setCookie("session", "admin");
                setCookie("loginSuccess", "true");
                return new Response(Status.OK, new URL("http://youtube.com"));
            }
        }
        setCookie("loginSuccess", "false");
        return new Response(Status.UNAUTHORIZED, new URL("login.html"));
    }


}
