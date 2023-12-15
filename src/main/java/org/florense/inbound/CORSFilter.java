package org.florense.inbound;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;

@Provider
public class CORSFilter implements ContainerResponseFilter {
    @ConfigProperty(name = "quarkus.http.cors.origins")
    String origins;

    @ConfigProperty(name = "quarkus.http.cors.headers")
    String headers;

    @ConfigProperty(name = "quarkus.http.cors.methods")
    String methods;

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext cres) throws IOException {
        cres.getHeaders().add("Access-Control-Allow-Origin", origins);
        cres.getHeaders().add("Access-Control-Allow-Headers", headers);
        cres.getHeaders().add("Access-Control-Allow-Methods", methods);

    }
}
