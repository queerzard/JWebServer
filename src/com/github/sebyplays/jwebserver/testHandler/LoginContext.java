package com.github.sebyplays.jwebserver.testHandler;

import com.github.sebyplays.jwebserver.AccessHandler;
import com.github.sebyplays.jwebserver.JWebServer;
import com.github.sebyplays.jwebserver.api.Response;
import com.github.sebyplays.jwebserver.api.Status;
import com.github.sebyplays.jwebserver.utils.ContentType;
import com.github.sebyplays.jwebserver.utils.Priority;
import com.github.sebyplays.jwebserver.utils.annotations.AccessController;
import com.github.sebyplays.jwebserver.utils.annotations.Register;
import com.sun.net.httpserver.HttpExchange;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

@Register(priority = Priority.DEFAULT)
@AccessController(index = "login")
public class LoginContext extends AccessHandler {

    @SneakyThrows
    @Override
    public Response handle(HttpExchange httpExchange, AccessHandler accessHandler) {
        String html = new String(Files.readAllBytes(new File(JWebServer.rootHtmlDir.getAbsolutePath() + "/login.html").toPath()));
        Document document = Jsoup.parse(html);
        document.getElementById("username").attr("value", "Invalid!");

        if(getCookies() == null || getCookie("loginSuccess") == null){
            setContentType(ContentType.TEXT_HTML);
            return new Response(Status.OK, html);
        } else {
            if(getCookies() != null && getCookie("loginSuccess") != null && !getCookie("loginSuccess").getValue().equalsIgnoreCase("true")) {
                setContentType(ContentType.TEXT_HTML);
                return new Response(Status.BAD_REQUEST, document.html());
            }
        }

        return new Response(Status.OK, "Login successful");
    }
}
