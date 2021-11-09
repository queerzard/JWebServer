package com.github.sebyplays.jwebserver.testHandler;

import com.github.sebyplays.jwebserver.AccessHandler;
import com.github.sebyplays.jwebserver.api.Response;
import com.github.sebyplays.jwebserver.api.Status;
import com.github.sebyplays.jwebserver.api.URL;
import com.github.sebyplays.jwebserver.utils.Priority;
import com.github.sebyplays.jwebserver.utils.annotations.AccessController;
import com.github.sebyplays.jwebserver.utils.annotations.Register;
import com.sun.net.httpserver.HttpExchange;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.HashMap;


@Register(priority = Priority.DEFAULT)
@AccessController(index = "processlogin")
public class ProcessLogin extends AccessHandler {

    @SneakyThrows
    @Override
    public Response handle(HttpExchange httpExchange, AccessHandler accessHandler) {
        HashMap<String, String> props = getQueryMap();
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
