package com.github.sebyplays.jwebserver.api;

import com.sun.net.httpserver.HttpExchange;

import java.util.HashMap;

public interface ResponseHandler {

    Response handle(HttpExchange httpExchange, AccessHandler accessHandler, HashMap<String, Object> returnValues);

}
