package com.github.sebyplays.jwebserver.api.events;

import com.github.sebyplays.jevent.Event;
import com.github.sebyplays.jwebserver.api.AccessHandler;
import com.github.sebyplays.jwebserver.api.Response;
import lombok.Getter;
import lombok.Setter;

/**
 * It's an event that is fired when a server response is intercepted
 */
public class ServerResponseInterceptionEvent extends Event {

    @Getter @Setter private Response response;
    @Getter private AccessHandler accessHandler;
    @Getter private String remoteAddress;

    public ServerResponseInterceptionEvent(){}
    public ServerResponseInterceptionEvent(Response response, AccessHandler accessHandler){
        this.accessHandler = accessHandler;
        this.response = response;
        this.remoteAddress = accessHandler.getHttpExchange().getRemoteAddress().getAddress().getHostAddress();
    }


}
