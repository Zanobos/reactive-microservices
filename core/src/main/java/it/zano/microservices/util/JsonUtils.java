package it.zano.microservices.util;

import it.zano.microservices.config.ArchConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class JsonUtils {

    private JsonUtils() {}

    private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    public static String toJson(Object object) {

        String s = null;
        try {
            s = ArchConfiguration.MAPPER.writeValueAsString(object);
        } catch (IOException e) {
            logger.error("Can not convert to json: {}", e);
        }
        return s;
    }

    public static <T> T toObject(String jsonInString, Class<T> aClass) {

        T object = null;
        try {
            if(jsonInString!=null)
                object = ArchConfiguration.MAPPER.readValue(jsonInString.getBytes(), aClass);
        } catch (IOException e) {
            logger.error("Can not construct object from json {}", e);
        }
        return object;
    }
}
