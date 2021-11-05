package com.github.sebyplays.jwebserver.testHandler;

import com.github.sebyplays.jwebserver.AccessHandler;
import com.github.sebyplays.jwebserver.Cookie;
import com.github.sebyplays.jwebserver.utils.SameSite;
import com.github.sebyplays.jwebserver.utils.annotations.AccessController;
import com.github.sebyplays.jwebserver.utils.annotations.Register;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

@AccessController(index = "cookieTest")
@Register
public class CookieContext extends AccessHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        //// STOPSHIP: 05.11.2021
        // TODO: Hey you idiot. Remember, the issue of why this site isn't loading in this case,
        //  is occurring due to the invalid parsing attempt of the cookies, that is executed in the AccessHandler class.
        //  The Handler gets stuck in the process and thus isn't sending any data back to the client.

        setHttpExchange(httpExchange);
        HashMap<String, String> props = getQueryMap();
        if(Boolean.parseBoolean(props.get("mode"))){
            setCookie(new Cookie("testCookie", "thicc", new Date(System.currentTimeMillis() + 100000), -1, null, null, SameSite.NO_RESTRICTION, true, false));
            respond(200, "" + getCookies().toArray(new Cookie[getCookies().size()]));
            return;
        }
        removeCookie("testCookie");
        respond(200, "" + getCookies().toArray(new Cookie[getCookies().size()]));
    }
}
