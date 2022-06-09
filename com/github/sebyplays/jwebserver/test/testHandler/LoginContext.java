package com.github.sebyplays.jwebserver.test.testHandler;

import com.github.sebyplays.jwebserver.api.AccessHandler;
import com.github.sebyplays.jwebserver.api.Response;
import com.github.sebyplays.jwebserver.api.Status;
import com.github.sebyplays.jwebserver.api.annotations.LinkedModel;
import com.github.sebyplays.jwebserver.utils.enums.ContentType;
import com.github.sebyplays.jwebserver.api.annotations.AccessController;
import com.github.sebyplays.jwebserver.api.annotations.Register;
import com.sun.net.httpserver.HttpExchange;
import lombok.SneakyThrows;

import java.util.HashMap;

@Register
@AccessController
public class LoginContext extends AccessHandler {

    @SneakyThrows
    @Override
    @LinkedModel("login.html")
    public Response handle(HttpExchange httpExchange, AccessHandler accessHandler, HashMap<String, Object> args) {


        if (httpExchange.getRequestMethod().equals("POST")) {
            System.out.println(getPostMap());
        }

        if(getCookies() == null || getCookie("loginSuccess") == null){
            setContentType(ContentType.TEXT_HTML);
            return new Response(Status.OK, getHtmlModel().getHtmlContent());
        } else {
            if(getCookies() != null && getCookie("loginSuccess") != null && !getCookie("loginSuccess").getValue().equalsIgnoreCase("true")) {
                getHtmlModel().getDocument().getElementById("invalidLabel").attr("style", "display: inline-block; color: red;");
                setContentType(ContentType.TEXT_HTML);
                return new Response(Status.BAD_REQUEST, getHtmlModel().getHtmlContent());
            }
        }

        return getHtmlModel().getProcessedResponse();
    }
}
