package org.florense.outbound.adapter.mercadolivre;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.florense.domain.model.*;
import org.florense.domain.model.enums.ListingTypeEnum;
import org.florense.outbound.adapter.mercadolivre.client.MercadoLivreOrderClient;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;
import org.florense.outbound.adapter.mercadolivre.exceptions.MLErrorTypesEnum;
import org.florense.outbound.adapter.mercadolivre.exceptions.MercadoLivreClientException;
import org.florense.outbound.adapter.mercadolivre.exceptions.MercadoLivreException;
import org.florense.outbound.adapter.mercadolivre.mlenum.MLStatusEnum;
import org.florense.outbound.adapter.mercadolivre.response.MLOrderResponse;
import org.florense.outbound.adapter.mercadolivre.response.MLOrderWrapperResponse;
import org.florense.outbound.port.mercadolivre.MercadoLivreVendaPort;
import org.florense.outbound.port.postgre.AnuncioEntityPort;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@ApplicationScoped
public class MercadoLivreVendaAdapter extends MercadoLivreAdapter implements MercadoLivreVendaPort {
    @ConfigProperty(name = "quarkus.rest-client.ml-api.app-id")
    String appId;
    @ConfigProperty(name = "quarkus.rest-client.ml-api.secret")
    String clientSecret;
    @ConfigProperty(name = "scheduler.job.order.search_order_limit_months")
    int searchLimit = 3;
    private static final int BATCH_SIZE = 50;
    @Inject
    AnuncioEntityPort anuncioEntityPort;

    @Inject
    Logger logger;

    @RestClient
    @Inject
    MercadoLivreOrderClient mercadoLivreOrderClient;

    @Override
    public Order getOrder(String mlOrderId, User user, boolean retry) throws MercadoLivreException, FailRequestRefreshTokenException {
        try {
            var o = mercadoLivreOrderClient.order(user.getAccessCode(), mlOrderId);
            return convertMlOrderResponseToOrder(o, user);
        } catch (MercadoLivreClientException e) {
            if (e.isRefreshToken()) {
                refreshAccessToken(appId, clientSecret, user);
                if (retry) {
                    return getOrder(mlOrderId, user, false);
                }
            }
            logger.error("Falha ao obter order", e);
            throw new MercadoLivreException(String.format("Falha ao obter order %s", mlOrderId), "mlOrderId",MLErrorTypesEnum.DEFAULT, e);
        }
    }

    @Override
    public List<Order> listAllordersByDate(User user, List<MLStatusEnum> status, LocalDateTime startDate, LocalDateTime endDate, boolean retry) throws FailRequestRefreshTokenException, MercadoLivreException {
        try {
            Map<Long, Order> listOrders = new LinkedHashMap<>();
            int offset = 0;
            int total = 1;

            StringBuilder filterStatus = new StringBuilder();
            status.forEach(mlStatusEnum -> filterStatus.append(mlStatusEnum.getIdentifier() + ","));
            filterStatus.setLength(filterStatus.length() - 1);

            Period period = Period.between(startDate.toLocalDate(), endDate.toLocalDate());
            int months = period.getYears() * 12 + period.getMonths();
            if(months > searchLimit) startDate = startDate.minusMonths(searchLimit);

            while (offset < total) {
                var start = ZonedDateTime.of(startDate.toLocalDate(),startDate.toLocalTime(),ZoneId.systemDefault());
                var end = ZonedDateTime.of(endDate.toLocalDate(),endDate.toLocalTime(),ZoneId.systemDefault());
                MLOrderWrapperResponse resp = mercadoLivreOrderClient.vendasOrderDesc(
                        user.getUserIdML(), DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(start), DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(end),
                        filterStatus.toString(), offset, "date_asc", BATCH_SIZE, user.getAccessCode());

                for (MLOrderResponse orderRespons : resp.getOrderResponses()) {
                    var newOrder = convertMlOrderResponseToOrder(orderRespons, user);
                    var existingOrder = listOrders.put(orderRespons.getShippingId(), newOrder);
                    if (Objects.nonNull(existingOrder)) existingOrder.getVendas().addAll(newOrder.getVendas());

                }
                total = resp.getTotal();
                offset += BATCH_SIZE;
            }
            return convertToReturn(listOrders);

        } catch (MercadoLivreClientException e) {
            if (e.isRefreshToken()) {
                refreshAccessToken(appId, clientSecret, user);
                if (retry) {
                    return listAllordersByDate(user,status,startDate,endDate,false);
                }
            }
            logger.error("Falha ao obter orders",e);
            throw new MercadoLivreException("Falha ao obter orders","listAllordersByDate", MLErrorTypesEnum.DEFAULT,e);
        }

    }

    @Override
    public List<Order> listOrdersUntilExistent(List<MLStatusEnum> status, Long existentOrderId, User user, boolean retry) throws FailRequestRefreshTokenException, MercadoLivreException {
        try {
            Map<Long, Order> listVendas = new LinkedHashMap<>();
            int offset = 0;
            int total = 1;
            StringBuilder filterStatus = new StringBuilder();
            status.forEach(mlStatusEnum -> filterStatus.append(mlStatusEnum.getIdentifier() + ","));
            filterStatus.setLength(filterStatus.length() - 1);

            while (offset < total) {
                var resp = mercadoLivreOrderClient.vendasOrderDescByStatus(user.getUserIdML(),
                        filterStatus.toString(), "date_desc", offset, BATCH_SIZE, user.getAccessCode());

                for (MLOrderResponse mlOrderResponse : resp.getOrderResponses()) {
                    if (mlOrderResponse.getOrderId().equals(existentOrderId)) {
                        return convertToReturn(listVendas);
                    }

                    var newOrder = convertMlOrderResponseToOrder(mlOrderResponse, user);
                    var existingOrder = listVendas.put(mlOrderResponse.getShippingId(), newOrder);
                    if (Objects.nonNull(existingOrder)) existingOrder.getVendas().addAll(newOrder.getVendas());
                }
                total = resp.getTotal();
                offset += BATCH_SIZE;
            }
            return convertToReturn(listVendas);

        } catch (MercadoLivreClientException e) {
            e.printStackTrace();
            if (e.isRefreshToken()) {
                refreshAccessToken(appId, clientSecret, user);
                if (retry) {
                    return listOrdersUntilExistent(status, existentOrderId, user, false);
                }
            }
            logger.error("Falha ao Buscar Orders",e);
            throw new MercadoLivreException("Falha ao Buscar Orders", "listOrdersUntilExistent", MLErrorTypesEnum.DEFAULT, e);

        }
    }

    private List<Order> convertToReturn(Map<Long, Order> orderMap) {
        List<Order> returnList = new LinkedList<>();
        for (Map.Entry<Long, Order> entry : orderMap.entrySet()) {
            returnList.add(entry.getValue());
        }
        Collections.reverse(returnList);
        return returnList;
    }

    //Esta aqui para evitar timeout em transaction anterior
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Order convertMlOrderResponseToOrder(MLOrderResponse mlOrderResponse, User user){
        boolean completo = mlOrderResponse.getCompleto() != null;

        Anuncio existingAnuncio = anuncioEntityPort.findAnyByMlId(mlOrderResponse.getMlId(), user);
        if (Objects.isNull(existingAnuncio)) {
            existingAnuncio = new Anuncio(null, mlOrderResponse.getMlId(), "", "", "", mlOrderResponse.getTitle(), "", 0.0, "", mlOrderResponse.getPrecoDesconto(), mlOrderResponse.getSaleFee(),
                    0.0, "active", null, 0.0, ListingTypeEnum.classico, user,0.0, false, new ArrayList<>());
        }

        Venda venda = new Venda(null, mlOrderResponse.getQuantity(), mlOrderResponse.getPrecoDesconto(), mlOrderResponse.getSaleFee(),
                0.0, existingAnuncio.getCusto(), 0.0, completo, mlOrderResponse.getStatus(),
                mlOrderResponse.getOrderId(), existingAnuncio, null);

        List<Venda> vendas = new ArrayList<>(List.of(venda));

        List<Reclamacao> reclamcoes = new ArrayList<>();
        mlOrderResponse.getMediations().forEach(mediation ->{
            Reclamacao reclamacao = new Reclamacao();
            reclamacao.setMediationId(mediation);
            reclamcoes.add(reclamacao);
        });

        var d = mlOrderResponse.getOrderCreationTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return new Order(null, mlOrderResponse.getOrderId(), mlOrderResponse.getShippingId(),
                vendas, reclamcoes, d, null);
    }

}