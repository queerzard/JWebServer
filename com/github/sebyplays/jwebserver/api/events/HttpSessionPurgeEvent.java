package com.github.sebyplays.jwebserver.api.events;

import com.github.sebyplays.jevent.Event;
import com.github.sebyplays.jwebserver.api.HttpSession;
import lombok.Getter;

public class HttpSessionPurgeEvent extends Event {

    @Getter private HttpSession httpSession;

    public HttpSessionPurgeEvent(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    public HttpSessionPurgeEvent(){}

}
