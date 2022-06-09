package com.github.sebyplays.jwebserver.api.caller;

import java.util.HashMap;

/**
 * If the time since the last request is greater than the time between requests, then allow the request
 */
public class RESTLimiter {

    public static final int MAX_REQUESTS_PER_SECOND = 10;
    public static final int MAX_REQUESTS_PER_MINUTE = 100;
    public static final int MAX_REQUESTS_PER_HOUR = 1000;

    private static final long SECOND = 1000;
    private static final long MINUTE = 60 * SECOND;
    private static final long HOUR = 60 * MINUTE;

    private static final int REQUESTS_PER_SECOND = 0;
    private static final int REQUESTS_PER_MINUTE = 0;
    private static final int REQUESTS_PER_HOUR = 0;

    private static final long REQUESTS_PER_SECOND_TIME = SECOND / MAX_REQUESTS_PER_SECOND;
    private static final long REQUESTS_PER_MINUTE_TIME = MINUTE / MAX_REQUESTS_PER_MINUTE;
    private static final long REQUESTS_PER_HOUR_TIME = HOUR / MAX_REQUESTS_PER_HOUR;

    private static final long REQUESTS_PER_SECOND_TIME_NEXT = REQUESTS_PER_SECOND_TIME + 1;
    private static final long REQUESTS_PER_MINUTE_TIME_NEXT = REQUESTS_PER_MINUTE_TIME + 1;
    private static final long REQUESTS_PER_HOUR_TIME_NEXT = REQUESTS_PER_HOUR_TIME + 1;

    private static long lastRequest = 0;

    public static HashMap<String, Integer> requests = new HashMap<>();

    /**
     * If the IP address is already in the map, increment the number of requests by one. Otherwise, add the IP address to
     * the map with a value of 1
     *
     * @param ip The IP address of the client making the request.
     */
    public static void addRequest(String ip) {
        if (requests.containsKey(ip)) {
            int requests = RESTLimiter.requests.get(ip);
            RESTLimiter.requests.put(ip, requests + 1);
        } else {
            RESTLimiter.requests.put(ip, 1);
        }
    }

    /**
     * If the IP address is in the map, decrement the number of requests by one
     *
     * @param ip The IP address of the client making the request.
     */
    public static void removeRequest(String ip) {
        if (requests.containsKey(ip)) {
            int requests = RESTLimiter.requests.get(ip);
            if (requests > 0) {
                RESTLimiter.requests.put(ip, requests - 1);
            }
        }
    }

    /**
     * Returns the number of milliseconds since the start of the current hour
     *
     * @return The current time in milliseconds.
     */
    public static long time() {
        long now = System.currentTimeMillis();
        long time = now % HOUR;
        return time;
    }

    /**
     * If the time since the last request is greater than the time between requests, then allow the request
     *
     * @param ip The IP address of the client making the request.
     * @return A boolean value.
     */
    public static boolean canRequest(String ip) {
        long now = System.currentTimeMillis();
        long time = now % HOUR;
        if (time < lastRequest) {
            lastRequest = 0;
        }
        if (time - lastRequest > REQUESTS_PER_HOUR_TIME_NEXT) {
            lastRequest = time;
            return true;
        }
        if (time - lastRequest > REQUESTS_PER_MINUTE_TIME_NEXT) {
            lastRequest = time;
            return true;
        }
        if (time - lastRequest > REQUESTS_PER_SECOND_TIME_NEXT) {
            lastRequest = time;
            return true;
        }
        if (requests.containsKey(ip)) {
            int requests = RESTLimiter.requests.get(ip);
            if (requests < MAX_REQUESTS_PER_MINUTE) {
                return true;
            }
        }
        return false;
    }


}
