package com.github.sebyplays.jwebserver.events;

import com.github.sebyplays.jevent.Event;
import com.github.sebyplays.jwebserver.AccessHandler;
import com.github.sebyplays.jwebserver.Cookie;
import com.sun.net.httpserver.HttpExchange;
import lombok.Getter;

import java.util.List;

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

}
