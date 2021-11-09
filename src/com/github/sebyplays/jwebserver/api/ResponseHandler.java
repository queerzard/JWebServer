package com.github.sebyplays.jwebserver.api;

import com.github.sebyplays.jwebserver.AccessHandler;
import com.sun.net.httpserver.HttpExchange;

public interface ResponseHandler {

    Response handle(HttpExchange httpExchange, AccessHandler accessHandler);

}
