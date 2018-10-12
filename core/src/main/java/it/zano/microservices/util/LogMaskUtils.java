package it.zano.microservices.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogMaskUtils  {

    private static final Logger logger = LoggerFactory.getLogger(LogMaskUtils.class);

    private static final String MASK_LONG_XML_FIELD = "<([^>]+)>([^<]{150}[^<]+)</\\1>";

    private static final Pattern PATTERN_LONG_XML_FIELD = Pattern.compile(MASK_LONG_XML_FIELD);

    private static final String LONG_XML_PREFIX = "LONGXML_";

    public static String maskFields(Object[] args) {

        Object[] objects = Arrays.stream(args).map(o -> {
            if (o != null) {
                return o.toString();
            }
            return o;
        }).filter(Objects::nonNull).toArray();

        String s = Arrays.toString(objects);
        return maskFields(s);

    }

    public static String maskFields(String s) {

        if (s == null)
            return "";

        //Mask Long xml field
        Matcher matcher = PATTERN_LONG_XML_FIELD.matcher(s);
        while(matcher.find()){
            String id = LONG_XML_PREFIX+UUID.randomUUID().toString();
            String longXmlField = matcher.group(2);
            s = s.replace(longXmlField, id);
            logger.info(id +": "+ longXmlField);
        }

        //Mask the log
        return s
                .replaceAll("(?<=\"password\":)\"[^\"]+\"", "\"********\"")
                .replaceAll("\"([^\"]+)\":\"([^\"]{144})[^\"]{3}[^\"]+([^\"]{3})\"", "\"$1\":\"$2...$3\"") //Json
                .replaceAll("(?<=authorization=)[^]]+]", "[*****]")
                .replaceAll("(?<=[aA]uthorization-[tT]oken=)[^]]+]", "[*****]")
                .replaceAll("(?<=user-session=)[^]]+]", "[*****]")
        ;
    }

}
