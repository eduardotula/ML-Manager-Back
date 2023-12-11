package org.florense.domain.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ObjectMapperUtil {

    public ObjectMapper mapper = new ObjectMapper();

}
