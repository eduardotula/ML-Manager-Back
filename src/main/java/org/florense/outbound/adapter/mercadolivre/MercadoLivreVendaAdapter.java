package org.florense.outbound.adapter.mercadolivre;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.florense.domain.model.Anuncio;
import org.florense.domain.model.Order;
import org.florense.domain.model.Venda;
import org.florense.outbound.adapter.mercadolivre.client.MercadoLivreOrderService;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;
import org.florense.outbound.adapter.mercadolivre.exceptions.UnauthorizedAcessKeyException;
import org.florense.outbound.adapter.mercadolivre.response.MLOrderResponse;
import org.florense.outbound.port.mercadolivre.MercadoLivreVendaPort;

import java.util.*;

@ApplicationScoped
public class MercadoLivreVendaAdapter extends MercadoLivreAdapter implements MercadoLivreVendaPort {
    @ConfigProperty(name = "quarkus.rest-client.ml-api.app-id")
    String appId;
    @ConfigProperty(name = "quarkus.rest-client.ml-api.secret")
    String clientSecret;
    @ConfigProperty(name = "quarkus.rest-client.ml-api.user-id")
    String userId;
    private static final int BATCH_SIZE = 50;

    @RestClient
    @Inject
    MercadoLivreOrderService mercadoLivreOrderService;

    @Override
    public List<Order> listAllVendas(boolean retry) throws FailRequestRefreshTokenException {
        try {
            Map<Long, Order> listVendas = new HashMap<>();
            int offset = 0;
            int total = 1;

            while (offset < total) {
                var resp = mercadoLivreOrderService.vendasOrderDesc(userId, offset);

                resp.getOrderResponses().forEach(mlOrderResponse -> {
                    var newOrder = convertMlVendaToOrder(mlOrderResponse);
                    var existingOrder = listVendas.put(mlOrderResponse.getShippingId(), newOrder);
                    if (Objects.nonNull(existingOrder)) existingOrder.getVendas().addAll(newOrder.getVendas());

                });
                offset += BATCH_SIZE;
            }
            return new ArrayList<>(listVendas.values());

        } catch (RuntimeException e) {
            if (e.getCause() instanceof UnauthorizedAcessKeyException) {
                refreshAccessToken(appId, clientSecret);
                if (retry) listAllVendas(false);
            }
        }
        throw new IllegalStateException("Falha ao Buscar vendas");
    }

    @Override
    public List<Order> listVendasUntilExistent(List<MLStatusEnum> status, Long existentOrderId, boolean retry) throws FailRequestRefreshTokenException {
        try {
            Map<Long, Order> listVendas = new HashMap<>();
            int offset = 0;
            int total = 1;
            StringBuilder filterStatus = new StringBuilder();
            status.forEach(mlStatusEnum -> filterStatus.append(status + ","));
            filterStatus.setLength(filterStatus.length() - 1);

            while (offset < total) {

                var resp = mercadoLivreOrderService.vendasOrderDescByStatus(userId, filterStatus.toString(), offset);

                for (MLOrderResponse mlOrderResponse : resp.getOrderResponses()) {
                    if(mlOrderResponse.getOrderId().equals(existentOrderId)) return new ArrayList<>(listVendas.values());

                    var newOrder = convertMlVendaToOrder(mlOrderResponse);
                    var existingOrder = listVendas.put(mlOrderResponse.getShippingId(), newOrder);
                    if (Objects.nonNull(existingOrder)) existingOrder.getVendas().addAll(newOrder.getVendas());
                }

                offset += BATCH_SIZE;
            }
            return new ArrayList<>(listVendas.values());

        } catch (RuntimeException e) {
            if (e.getCause() instanceof UnauthorizedAcessKeyException) {
                refreshAccessToken(appId, clientSecret);
                if (retry) listAllVendas(false);
            }
        }
        throw new IllegalStateException("Falha ao Buscar vendas");
    }

    private Order convertMlVendaToOrder(MLOrderResponse mlOrderResponse) {
        boolean completo = mlOrderResponse.getCompleto() != null;
        Venda venda = new Venda(null, mlOrderResponse.getQuantity(), mlOrderResponse.getPrecoDesconto(), mlOrderResponse.getSaleFee(),
                0.0, 0.0, 0.0, completo, mlOrderResponse.getStatus(), null, null, null);
        var vendas = new ArrayList<Venda>();
        var anuncio = new Anuncio();
        anuncio.setMlId(mlOrderResponse.getMlId());
        venda.setAnuncio(anuncio);
        vendas.add(venda);

        return new Order(null, mlOrderResponse.getOrderId(), mlOrderResponse.getShippingId(), vendas, mlOrderResponse.getOrderCreationTime().toLocalDateTime(), null);
    }

}
