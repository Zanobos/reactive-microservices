package it.zano.microservices.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
public class JsonUtils {

    private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    @Autowired
    private ObjectMapper objectMapper;

    private static ObjectMapper mapper;

    @PostConstruct
    private void init () {
        mapper = this.objectMapper;
    }

    public static String toJson(Object object) {

        String s = null;
        try {
            s = mapper.writeValueAsString(object);
        } catch (IOException e) {
            logger.error("Can not convert to json: {}", e);
        }
        return s;
    }

    public static <T> T toObject(String jsonInString, Class<T> aClass) {

        T object = null;
        try {
            if(jsonInString!=null)
                object = mapper.readValue(jsonInString.getBytes(), aClass);
        } catch (IOException e) {
            logger.error("Can not construct object from json {}", e);
        }
        return object;
    }
}
