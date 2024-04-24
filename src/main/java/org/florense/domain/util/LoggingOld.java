package org.florense.domain.util;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.jboss.logging.Logger;

@Dependent
@Getter
public class LoggingOld {

    private String originClass = LoggingOld.class.getName();
    @Inject
    Logger logger;

    public void info(String message) {
        this.info(message, null);
    }

    public void info(String message, Object[] params) {
        this.logger.info(originClass,message,params, null);
    }

    public void error(String message) {
        this.error(message, null);
    }

    public void error(Object message, Throwable t) {
        this.error(message, null,t);
    }

    public void error(Object message, Object[] params,Throwable t) {
        this.logger.error(originClass,message,params,t );
    }

    public void setOriginClass(Class originClass){
        this.originClass = originClass.getName();
    }

}
