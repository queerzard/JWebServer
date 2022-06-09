package com.github.sebyplays.jwebserver.test.testHandler;

import com.github.sebyplays.jwebserver.api.AccessHandler;
import com.github.sebyplays.jwebserver.api.Cookie;
import com.github.sebyplays.jwebserver.api.Response;
import com.github.sebyplays.jwebserver.api.Status;
import com.github.sebyplays.jwebserver.utils.enums.SameSite;
import com.github.sebyplays.jwebserver.api.annotations.AccessController;
import com.github.sebyplays.jwebserver.api.annotations.Register;
import com.sun.net.httpserver.HttpExchange;

import java.util.Date;
import java.util.HashMap;

@AccessController(index = "cookieTest")
@Register
public class CookieContext extends AccessHandler {
        //// STOPSHIP: 05.11.2021
        // TODO: Hey you idiot. Remember, the issue of why this site isn't loading in this case,
        //  is occurring due to the invalid parsing attempt of the cookies, that is executed in the AccessHandler class.
        //  The Handler gets stuck in the process and thus isn't sending any data back to the client.
        //  To be more specific on why it's exactly not working: The getCookies() Method is buggy if the cookies are empty on the users side.
        //  The Code is trying to access values that do not exist, making the code dysfunctional.
    // STOPSHIP: 03.05.2022 - I think i fixed it lol


    @Override
    public Response handle(HttpExchange httpExchange, AccessHandler accessHandler, HashMap<String, Object> args) {

        HashMap<String, String> props = getQueryMap();
        if(Boolean.parseBoolean(props.get("mode"))){
            setCookie(new Cookie("testCookie", "thicc", new Date(System.currentTimeMillis() + 100000), 10, null, null, SameSite.LAX, true, false));
            setCookie("YourPenis", "isSmall");
            return new Response(Status.OK, "");
        }
        removeCookie("YourPenis");
        removeCookie("testCookie");

        return new Response(Status.OK, "");
    }
}
