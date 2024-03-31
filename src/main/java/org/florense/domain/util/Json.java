package org.florense.domain.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.HashMap;

@ApplicationScoped
public class Json {

    private ObjectMapper mapper;
    @Inject
    Logger logger;

    public Json(){
        this.mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    public String toJson(Object object)  {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error("Falha ao converter objeto em json",e);
            return "{}";
        }
    }

    public HashMap<String, Object> toMap(Object object){
        return mapper.convertValue(object, new TypeReference<HashMap<String, Object>>() {});
    }

}
