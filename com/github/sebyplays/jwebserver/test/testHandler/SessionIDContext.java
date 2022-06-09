package com.github.sebyplays.jwebserver.test.testHandler;

import com.github.sebyplays.jwebserver.api.AccessHandler;
import com.github.sebyplays.jwebserver.api.Response;
import com.github.sebyplays.jwebserver.api.Status;
import com.github.sebyplays.jwebserver.api.annotations.AccessController;
import com.github.sebyplays.jwebserver.api.annotations.Register;
import com.sun.net.httpserver.HttpExchange;

import java.util.HashMap;

@AccessController(index = "sessionID")
@Register
public class SessionIDContext extends AccessHandler {

    @Override
    public Response handle(HttpExchange httpExchange, AccessHandler accessHandler, HashMap<String, Object> returnValues) {

        return new Response(Status.OK, "sessionID_" + accessHandler.getUserSession());
    }
}
