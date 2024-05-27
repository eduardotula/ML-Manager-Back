package org.florense.domain.usecase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.resource.spi.IllegalStateException;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.florense.domain.model.Anuncio;
import org.florense.domain.model.Order;
import org.florense.domain.model.User;
import org.florense.inbound.adapter.dto.WebhookNotification;
import org.florense.inbound.adapter.dto.consultas.AnuncioSimulation;
import org.florense.inbound.adapter.dto.consultas.AnuncioSimulationResponse;
import org.florense.inbound.port.AnuncioAdapterPort;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;
import org.florense.outbound.adapter.mercadolivre.exceptions.MercadoLivreException;
import org.florense.outbound.port.mercadolivre.MercadoLivreAnuncioPort;
import org.florense.outbound.port.postgre.AnuncioEntityPort;
import org.florense.outbound.port.postgre.OrderEntityPort;
import org.florense.outbound.port.postgre.UserEntityPort;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class AnuncioUseCase implements AnuncioAdapterPort {

    @Inject
    AnuncioEntityPort anuncioEntityPort;
    @Inject
    UserEntityPort userEntityPort;
    @Inject
    MercadoLivreAnuncioPort mercadoLivreAnuncioPort;
    @Inject
    OrderEntityPort orderEntityPort;
    @Inject
    Logger logger;
    @ConfigProperty(name = "mercado-livre.maxpriceforfrete")
    double maxPriceFrete;

    //Cria um anuncio e busca informações no mercado livre
    @Override
    @Transactional
    public Anuncio createMlSearch(Anuncio anuncio, Long userId) throws FailRequestRefreshTokenException, MercadoLivreException {
        logger.infof("Inicio createMlSearch: anuncio.mlId %s userId %d", anuncio.getMlId(), userId);

        User user = getUserOrThrowException(userId);

        var existProd = anuncioEntityPort.findByMlId(anuncio.getMlId(), user);
        if (existProd != null)
            throw new IllegalArgumentException(String.format("Anuncio com id: %s já cadastrado", anuncio.getMlId()));

        Anuncio completeAnuncio = mercadoLivreAnuncioPort.getAnuncio(anuncio.getMlId(), user, true);
        completeAnuncio.setTaxaML(mercadoLivreAnuncioPort.getTarifas(completeAnuncio.getPrecoDesconto(),
                completeAnuncio.getCategoria(), completeAnuncio.getListingType(), user, true));
        if (completeAnuncio.getPrecoDesconto() >= maxPriceFrete){
            try {
                completeAnuncio.setCustoFrete(mercadoLivreAnuncioPort.getFrete(completeAnuncio.getMlId(), user, true));
            } catch (MercadoLivreException e) {
                completeAnuncio.setCustoFrete(0.0);
            }
        } else completeAnuncio.setCustoFrete(0.0);

        completeAnuncio = setAnuncioDataForAnuncioUpdate(completeAnuncio, anuncio, user);
        completeAnuncio.setComplete(true);
        completeAnuncio.setUser(user);

        logger.infof("Final createMlSearch: anuncio.mlId %s userId %d", anuncio.getMlId(), userId);
        return anuncioEntityPort.createUpdate(completeAnuncio);
    }

    //Somente atualiza um anuncio sem buscar com mercado livre
    @Override
    @Transactional
    public Anuncio updateSimple(Anuncio anuncio, Long userId) throws FailRequestRefreshTokenException, MercadoLivreException {
        logger.infof("Inicio updateSimple: anuncio.mlId %s userId %d", anuncio.getMlId(), userId);

        User user = getUserOrThrowException(userId);

        var existProd = anuncioEntityPort.findAnyByMlId(anuncio.getMlId(), user);
        if (Objects.isNull(existProd))
            throw new IllegalArgumentException(String.format("Anuncio com id %s não encontrado", anuncio.getMlId()));

        Anuncio completeAnuncio = mercadoLivreAnuncioPort.getAnuncio(anuncio.getMlId(), user, true);
        existProd = setAnuncioDataForAnuncioUpdate(completeAnuncio, existProd, user);
        existProd.updateLocalData(anuncio);

        verifyIfAnuncioMatchesUserOrThrowException(existProd, user);

        if(!existProd.isComplete()){
            List<Order> orders = orderEntityPort.listAllOrdersByAnuncio(existProd);
            Anuncio finalExistProd = existProd;
            orders.forEach(order -> order.getVendas().forEach(venda -> {
                if(venda.getCusto() == 0) venda.setCusto(finalExistProd.getCusto());
                venda.setLucro(Anuncio.calculateLucro(finalExistProd));
            }));
            orderEntityPort.createUpdateAll(orders);
        }
        existProd.setComplete(true);

        logger.infof("Final updateSimple: anuncio.mlId %s userId %d", anuncio.getMlId(), userId);
        return anuncioEntityPort.createUpdate(existProd);
    }

    //Busca novas atualizações do anuncio no mercado livre
    @Override
    @Transactional
    public Anuncio updateSearch(String mlId, Long userId) throws FailRequestRefreshTokenException, MercadoLivreException {
        logger.infof("Inicio updateSearch: mlId %s userId %d", mlId, userId);

        User user = getUserOrThrowException(userId);
        var existProd = anuncioEntityPort.findByMlId(mlId, user);
        if (Objects.isNull(existProd)) {
            throw new IllegalArgumentException(String.format("Anuncio com mlId: %s não encontrado", mlId));
        }
        Anuncio completeAnuncio = mercadoLivreAnuncioPort.getAnuncio(mlId, user, true);
        verifyIfAnuncioMatchesUserOrThrowException(existProd, user);
        completeAnuncio = setAnuncioDataForAnuncioUpdate(completeAnuncio, existProd, user);

        completeAnuncio.setComplete(true);
        logger.infof("Final updateSearch: mlId %s userId %d", mlId, userId);
        return anuncioEntityPort.createUpdate(completeAnuncio);
    }

    @Override
    @Transactional
    public List<String> listAllAnunciosMercadoLivre(Long userId, boolean includePaused) throws FailRequestRefreshTokenException {
        User user = getUserOrThrowException(userId);
        return mercadoLivreAnuncioPort.listAllAnunciosMercadoLivre(user, includePaused,true);
    }

    @Override
    @Transactional
    public Anuncio findAnuncioByMlId(String mlId, Long userId) {
        User user = getUserOrThrowException(userId);
        return anuncioEntityPort.findByMlId(mlId, user);
    }
    @Override
    @Transactional
    public Anuncio findAnyAnuncioByMlId(String mlId, Long userId) {
        User user = getUserOrThrowException(userId);
        return anuncioEntityPort.findAnyByMlId(mlId, user);
    }

    @Override
    @Transactional
    public Anuncio findAnuncioByMlIdSearch(String mlId, Long userId) throws FailRequestRefreshTokenException, MercadoLivreException {
        User user = getUserOrThrowException(userId);
        return mercadoLivreAnuncioPort.getAnuncio(mlId, user, true);
    }

    @Override
    @Transactional
    public List<Anuncio> listAll(Long userId) {
        User user = getUserOrThrowException(userId);
        return anuncioEntityPort.listAll(user);
    }

    @Override
    @Transactional
    public List<Anuncio> listAllRegistered(Long userId) {
        User user = getUserOrThrowException(userId);
        return anuncioEntityPort.listAllRegistered(user);
    }

    private User getUserOrThrowException(Long userId) throws IllegalArgumentException {
        User user = userEntityPort.findById(userId);
        if (Objects.isNull(user))
            throw new IllegalArgumentException(String.format("User com id: %d não encontrado", userId));
        return user;
    }

    private void verifyIfAnuncioMatchesUserOrThrowException(Anuncio anuncio, User user) {
        if (anuncio.getUser() == null || !Objects.equals(anuncio.getUser().getId(), user.getId())) {
            throw new IllegalArgumentException(String.format("Anuncio com id:%d não pertence a usuário %s", anuncio.getId(), user.getName()));
        }
    }

    @Override
    @Transactional
    public void deleteBy(Long id) throws IllegalStateException {
        logger.infof("Inicio deleteBy: id %d", id);

        Anuncio anuncio = anuncioEntityPort.findAnyById(id);
        if (Objects.isNull(anuncio))
            throw new IllegalArgumentException(String.format("Anuncio com id %s não encontrado", id));

        try {
            List<Order> orders = orderEntityPort.listAllOrdersByAnuncio(anuncio);
            if(orders.isEmpty()) anuncioEntityPort.deleteById(id);
            else anuncioEntityPort.disableById(id);
        } catch (Exception e) {
            this.logger.error("Falha ao apagar anuncio com id: %s",e);
            throw new IllegalStateException(String.format("Falha ao apagar anuncio com id: %s", id));
        }
        logger.infof("Final deleteBy: id %d", id);

    }

    public Anuncio setAnuncioDataForAnuncioUpdate(Anuncio anuncioMercadoLivre, Anuncio existProd, User user) throws FailRequestRefreshTokenException, MercadoLivreException {
        anuncioMercadoLivre.setTaxaML(mercadoLivreAnuncioPort.getTarifas(anuncioMercadoLivre.getPrecoDesconto(),
                anuncioMercadoLivre.getCategoria(), anuncioMercadoLivre.getListingType(), user, true));
        if (anuncioMercadoLivre.getPrecoDesconto() >= maxPriceFrete){
            try {
                anuncioMercadoLivre.setCustoFrete(mercadoLivreAnuncioPort.getFrete(anuncioMercadoLivre.getMlId(), user, true));
            } catch (MercadoLivreException ignored) {}
        } else anuncioMercadoLivre.setCustoFrete(0);
        anuncioMercadoLivre.update(existProd);
        anuncioMercadoLivre.updateLocalData(existProd);

        return anuncioMercadoLivre;
    }

    @Override
    @Transactional
    public AnuncioSimulationResponse simulateAnuncio(AnuncioSimulation anuncioSimulation) throws MercadoLivreException, FailRequestRefreshTokenException {
        User user = getUserOrThrowException(anuncioSimulation.getUserId());
        double taxaML = mercadoLivreAnuncioPort.getTarifas(anuncioSimulation.getValorVenda(),anuncioSimulation.getCategoria(),
                anuncioSimulation.getTipoAnuncio(),user,true);
        double imposto = Anuncio.calculateImposto(anuncioSimulation.getCsosn(),anuncioSimulation.getValorVenda());

        double frete;
        if(anuncioSimulation.getValorVenda() >= maxPriceFrete){
            if(!anuncioSimulation.getEquivalentMlId().isEmpty())
                frete = mercadoLivreAnuncioPort.getFrete(anuncioSimulation.getEquivalentMlId(),user,true);
            else frete = mercadoLivreAnuncioPort.getFrete(anuncioSimulation.getMlId(), user, true);
        }else frete = 0.0;

        double lucro = Anuncio.calculateLucro(anuncioSimulation.getCusto(),taxaML, frete, imposto, anuncioSimulation.getValorVenda());
        return new AnuncioSimulationResponse(lucro, anuncioSimulation.getCusto(), anuncioSimulation.getCsosn(), taxaML, frete, imposto, anuncioSimulation.getCategoria());
    }

    @Transactional
    public synchronized void processNotification(WebhookNotification webhookNotification) throws FailRequestRefreshTokenException, MercadoLivreException {
        logger.infof("Inicio processNotification AnuncioUseCase: userIdML %s", webhookNotification.getUserIdML());

        String mlId = webhookNotification.getResource().split("/")[2];
        User user = userEntityPort.findByMlIdUser(webhookNotification.getUserIdML());
        if(Objects.isNull(user)) throw new IllegalArgumentException(String.format("User com MLId %s não encontrado", webhookNotification.getUserIdML()));

        Anuncio anuncio = mercadoLivreAnuncioPort.getAnuncio(mlId,user,true);
        Anuncio existingAnuncio = anuncioEntityPort.findAnyByMlId(mlId,user);

        double taxaMl = mercadoLivreAnuncioPort.getTarifas(anuncio.getPrecoDesconto(),anuncio.getCategoria(),anuncio.getListingType(),user,true);
        double custoFrete = mercadoLivreAnuncioPort.getFrete(anuncio.getMlId(), user,true);
        anuncio.setTaxaML(taxaMl);
        anuncio.setCustoFrete(custoFrete);

        anuncio.setUser(user);
        if(Objects.nonNull(existingAnuncio)){
            anuncio.update(existingAnuncio);
            anuncio.setComplete(existingAnuncio.isComplete());
            anuncio.updateLocalData(existingAnuncio);
        }

        anuncioEntityPort.createUpdate(anuncio);
        logger.infof("Notificação processada com sucesso mlId: %s" , mlId);
    }
}
