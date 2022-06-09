package com.github.sebyplays.jwebserver.api.events;

import com.github.sebyplays.jevent.Event;
import com.github.sebyplays.jwebserver.api.AccessHandler;
import lombok.Getter;

public class HttpSessionFetchEvent extends Event {

    @Getter private AccessHandler accessHandler;
    public HttpSessionFetchEvent() {}
    public HttpSessionFetchEvent(AccessHandler accessHandler) { this.accessHandler = accessHandler; }

}
