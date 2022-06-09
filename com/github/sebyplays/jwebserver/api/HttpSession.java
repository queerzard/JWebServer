package com.github.sebyplays.jwebserver.api;

import com.github.sebyplays.jevent.api.JEvent;
import com.github.sebyplays.jwebserver.api.events.HttpSessionPurgeEvent;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * It's a class that stores data for a client
 */
public class HttpSession {

    public static final String COOKIE_NAME = "JSESSIONID";

    @Getter private static ArrayList<HttpSession> sessions = new ArrayList<>();

    @Getter private String id;
    @Getter private String ip;
    @Getter private Cookie sessionCookie;

    @Getter @Setter private long lastAccessed = 0L;
    @Getter @Setter private long lastFetched = 0L;
    @Getter private long created = 0L;

    @Getter private HashMap<String, Object> sessionData;


    public HttpSession(String id, String ip) {
        this.id = id;
        this.ip = ip;
        this.sessionData = new HashMap<>();
        this.sessionCookie = new Cookie(COOKIE_NAME, id);
        this.created = System.currentTimeMillis();
        this.lastAccessed = this.created;
        sessions.add(this);
    }

    /**
     * If the session exists, update the last accessed time and return it. Otherwise, create a new session and return it
     *
     * @param id The session id
     * @param ip The IP address of the client.
     * @return A new session object
     */
    public static HttpSession getSession(String id, String ip) {

        for (HttpSession session : sessions)
            if (session.getId().equals(id)){
                session.setLastAccessed(System.currentTimeMillis());
                return session;
            }
        return new HttpSession(id, ip);
    }

    /**
     * It generates a random string of 20 characters
     *
     * @return A random string of 20 characters.
     */
    public static String generateSessionId() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            sb.append((char) (Math.random() * 26 + 97));

        }
        return sb.toString();
    }

    /**
     * If the cookie value is a valid session ID, return the session object associated with that ID.
     *
     * @param cookie The cookie that was sent by the client.
     * @param ip The IP address of the client.
     * @return A session object
     */
    public static HttpSession getSession(Cookie cookie, String ip) {
        return getSession(cookie.getValue(), ip);
    }

    /**
     * It removes all sessions that haven't been used in 30 minutes
     */
    public static void sessionPurger(){
        new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(10);
                    for(HttpSession httpSession : HttpSession.getSessions()) {
                        if (System.currentTimeMillis() - httpSession.getLastFetched() > TimeUnit.MINUTES.toMillis(30)) {
                            if(new JEvent(new HttpSessionPurgeEvent(httpSession)).callEvent().getEvent().isCancelled())
                                continue;
                            HttpSession.getSessions().remove(httpSession);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //toString
    @Override
    public String toString() {
        return "HttpSession{" +
                "id='" + id + '\'' +
                ", ip='" + ip + '\'' +
                ", sessionCookie=" + sessionCookie +
                ", lastAccessed=" + lastAccessed +
                ", created=" + created +
                ", sessionData=" + sessionData +
                '}';
    }

    public String toJSON() {
        return "{\"id\":\"" + id + "\",\"ip\":\"" + ip + "\",\"sessionCookie\":" + sessionCookie.toJSON() + ",\"lastAccessed\":" + lastAccessed + ",\"created\":" + created + ",\"sessionData\":" + sessionData + "}";
    }
}