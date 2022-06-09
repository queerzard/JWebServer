package com.github.sebyplays.jwebserver.api.events;

import com.github.sebyplays.jevent.Event;
import com.github.sebyplays.jwebserver.api.AccessHandler;
import com.github.sebyplays.jwebserver.api.Cookie;
import com.github.sebyplays.jwebserver.api.Response;
import com.sun.net.httpserver.HttpExchange;
import lombok.Getter;

import java.util.List;

/**
 * It's an event that is fired when a context is accessed
 */
public class ContextAccessedEvent extends Event {

    @Getter private HttpExchange httpExchange;
    @Getter private String query;
    @Getter private Cookie[] cookies;
    @Getter private AccessHandler accessHandler;

    public ContextAccessedEvent(){}
    public ContextAccessedEvent(AccessHandler accessHandler){
        this.accessHandler = accessHandler;
        this.query = accessHandler.getQuery();
        List<Cookie> cookies2 = accessHandler.getCookies();
        if(cookies2 != null)
            this.cookies = cookies2.toArray(new Cookie[cookies2.size()]);
        this.httpExchange = accessHandler.getHttpExchange();
    }

    public void respond(Response response){
        accessHandler.respond(response);
    }

}
