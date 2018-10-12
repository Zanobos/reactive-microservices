package it.zano.microservices.util;

import java.util.Map;

public class UrlUtils {

    public static String concatenateUrl(String baseUrl, String page) {
        if(baseUrl.endsWith("/") && page.startsWith("/"))
            page = page.substring(1);
        return baseUrl + page;
    }

    public static String addQueryStringToUrl(String url, Map<String, String> params) {
        if(params == null || params.isEmpty())
            return url;
        String combined = params.entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .reduce(url + "?", (prev, nextPair) -> prev + nextPair + "&");
        return combined.substring(0, combined.length()-1);
    }
}
