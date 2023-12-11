package org.florense.outbound.adapter.mercadolivre.client;

import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class MercadoLivreServiceHeader implements ClientHeadersFactory {

    @ConfigProperty(name = "quarkus.rest-client.ml-api.access_code")
    String auth;

    @Override
    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> multivaluedMap, MultivaluedMap<String, String> multivaluedMap1) {
        var result = new MultivaluedHashMap<String, String>();
        result.add("Accept", "application/json");
        result.add("Cache-Control", "no-cache");
        result.add("Content-Type", "application/json");
        result.add("Authorization", "Bearer " + auth);
        return result;
    }
}
