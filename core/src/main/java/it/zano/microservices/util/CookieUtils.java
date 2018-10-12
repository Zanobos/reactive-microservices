package it.zano.microservices.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Stream;

public class CookieUtils {

    private static Logger logger = LoggerFactory.getLogger(CookieUtils.class);

    public static final String DEFAULT_PATH = "/";
    public static final String MAX_AGE_ONE_DAY  = "" + 60*60*24;

    public static String getBase64DecodedCookieValue(String name, HttpHeaders httpHeaders) {
        String base64Cookie = getCookieValue(name, httpHeaders);
        if(base64Cookie == null)
            return null;

        byte[] decodedCookie = Base64.getDecoder().decode(base64Cookie); //TODO: should handle the IllegalArgumentException if not base64?
        return new String(decodedCookie);
    }

    public static String getCookieValue(String name, HttpHeaders httpHeaders) {
        List<String> cookies = httpHeaders.get(HttpHeaders.COOKIE);
        if(cookies == null || cookies.size() == 0)
            return null;
        return getCookieValue(name, cookies);
    }

    public static String getCookieValue(String name, List<String> cookieHeaders) {
        return getCookieValue(name, cookieHeaders.stream().flatMap(CookieUtils::splitCookieFields)); //Split the various cookies
    }

    public static String getCookieValue(String name, String cookieHeader) {
        return getCookieValue(name, splitCookieFields(cookieHeader)); //Split the various cookies
    }

    //--- DELETE COOKIE

    /**
     * Useful to delete the cookie. Remember to add the uri endpoint to onboarding.security.allowedCookies.cookie-name.comesFrom
     * in gateway configuration in order to be able to send the Set-Cookie header. Path will be considered as '/'.
     * @param name
     * @param httpHeaders can be null
     * @return
     */
    public static HttpHeaders deleteCookie(String name, HttpHeaders httpHeaders) {
        return deleteCookie(name, DEFAULT_PATH, httpHeaders);
    }

    /**
     * Useful to delete the cookie. Remember to add the uri endpoint to onboarding.security.allowedCookies.cookie-name.comesFrom
     * in gateway configuration in order to be able to send the Set-Cookie header.
     * @param name
     * @param path fundamental to delete the right cookie.
     * @param httpHeaders can be null
     * @return
     */
    public static HttpHeaders deleteCookie(String name, String path, HttpHeaders httpHeaders) {
        if(httpHeaders == null)
            httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.SET_COOKIE, createSetCookieDeleteContent(name, path));
        return httpHeaders;
    }

    public static void deleteCookie(String name, HttpServletResponse response) {
        deleteCookie(name, DEFAULT_PATH, response);
    }
    
    public static void deleteCookie(String name, String path, HttpServletResponse response) {
        response.setHeader(HttpHeaders.SET_COOKIE, createSetCookieDeleteContent(name, path));
    }

    private static String createSetCookieDeleteContent(String name, String path) {
        return name+"={}; Path="+path+"; Expires=Thu, 01 Jan 1970 00:00:00 GMT; Max-Age=0";
    }

    //--- SET COOKIE

    private static String createSetCookieContent(String name, String value, String path, String maxAge) {
        return name+ "="+value+"; Path="+path+"; Max-Age="+maxAge;
    }

    private static String createSetCookieContent(String name, String value, String path, String maxAge, boolean secure, boolean httpOnly) {
        String cookie = name+ "="+value+"; Path="+path+"; Max-Age="+maxAge;
        cookie = secure ? cookie + ";Secure" : cookie;
        cookie = httpOnly ? cookie + ";HttpOnly" : cookie;
        return cookie;
    }

    private static String createSetCookieContent(String name, String value, String path, String domain, String maxAge, boolean secure, boolean httpOnly) {
        String cookie = name+ "="+value+"; Path="+path+"; Domain="+domain+"; Max-Age="+maxAge;
        cookie = secure ? cookie + ";Secure" : cookie;
        cookie = httpOnly ? cookie + ";HttpOnly" : cookie;
        return cookie;
    }

    public static void setCookie(String name, String value, String path, String maxAge, HttpHeaders httpHeaders) {
        httpHeaders.add(HttpHeaders.SET_COOKIE, createSetCookieContent(name, value, path,maxAge));
    }

    public static void setCookie(String name, String value, String path, String maxAge, boolean secure, boolean httpOnly, HttpHeaders httpHeaders) {
        httpHeaders.add(HttpHeaders.SET_COOKIE, createSetCookieContent(name, value, path, maxAge, secure, httpOnly));
    }

    public static void setCookie(String name, String value, String path, String domain, String maxAge,  boolean secure, boolean httpOnly, HttpHeaders httpHeaders) {
        httpHeaders.add(HttpHeaders.SET_COOKIE, createSetCookieContent(name, value, path, domain, maxAge, secure, httpOnly));
    }

    public static void addCookieToRequest(String name, String value, HttpHeaders httpHeaders) {
        httpHeaders.add(HttpHeaders.COOKIE, name + "=" + value);
    }

    private static String getCookieValue(String name, Stream<String> cookieFields) {
        String encodedCookie = cookieFields.filter(cookie -> cookie.contains("="))  //Check if it is well formed
                .map(cookie -> cookie.split("=", 2))  //Transform it into an array 0 -> name, 1 -> content
                .filter(cookieMap -> cookieMap.length == 2)
                .filter(cookieMap -> name.equalsIgnoreCase(cookieMap[0]))  //Take the desired cookie
                .map(cookieMap -> cookieMap[1]) //Take the content
                .findAny().orElse(null);

        if(encodedCookie == null)
            return null;

        //Decode the cookie (it has %22 instead of '"' and %2C instead of ',')
        // (For Base64 cookies, this is not necessary)
        String decodedCookie = null;
        try {
            decodedCookie = URLDecoder.decode(encodedCookie, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("Error with the encoding"); //Should never happen
        }

        return decodedCookie;
    }


    private static Stream<String> splitCookieFields(String cookieHeader) {
        return Arrays.stream(cookieHeader.split(";[ ]?"));
    }
}
