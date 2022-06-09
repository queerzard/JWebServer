package com.github.sebyplays.jwebserver.api.events;

import com.github.sebyplays.jevent.Event;
import com.github.sebyplays.jwebserver.api.AccessHandler;
import com.github.sebyplays.jwebserver.api.Response;
import com.github.sebyplays.jwebserver.api.Status;
import lombok.Getter;
import lombok.Setter;

/**
 * It's an event that is fired when a request is intercepted by the server
 */
public class AccessInterceptionEvent extends Event {

    @Getter private AccessHandler accessHandler;
    @Getter @Setter private Response response = new Response(Status.NO_CONTENT, "The server has refused to respond to your request.");

    public AccessInterceptionEvent(AccessHandler accessHandler) {
        this.accessHandler = accessHandler;
    }

    public AccessInterceptionEvent(){
    }

}
