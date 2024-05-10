package org.florense.domain.usecase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.resource.spi.IllegalStateException;
import jakarta.transaction.Transactional;
import org.florense.domain.model.Anuncio;
import org.florense.domain.model.Order;
import org.florense.domain.model.User;
import org.florense.domain.util.Log;
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

    //Cria e atualiza com mercado livre
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
        if (completeAnuncio.getPrecoDesconto() >= 80){
            try {
                completeAnuncio.setCustoFrete(mercadoLivreAnuncioPort.getFrete(completeAnuncio.getMlId(), user, true));
            } catch (MercadoLivreException e) {
                completeAnuncio.setCustoFrete(0.0);
            }
        } else completeAnuncio.setCustoFrete(0.0);

        completeAnuncio.setCusto(anuncio.getCusto());
        completeAnuncio.setCsosn(anuncio.getCsosn());
        completeAnuncio.setLucro(Anuncio.calculateLucro(completeAnuncio));
        completeAnuncio.setUser(user);
        completeAnuncio.setComplete(true);

        logger.infof("Final createMlSearch: anuncio.mlId %s userId %d", anuncio.getMlId(), userId);
        return anuncioEntityPort.createUpdate(completeAnuncio);
    }

    //Somente atualiza um anuncio
    @Override
    @Transactional
    public Anuncio updateSimple(Anuncio anuncio, Long userId) {
        logger.infof("Inicio updateSimple: anuncio.mlId %s userId %d", anuncio.getMlId(), userId);

        User user = getUserOrThrowException(userId);

        var existProd = anuncioEntityPort.findAnyByMlId(anuncio.getMlId(), user);
        if (Objects.isNull(existProd))
            throw new IllegalArgumentException(String.format("Anuncio com id %s não encontrado", anuncio.getMlId()));

        existProd.setCsosn(anuncio.getCsosn());
        existProd.setCusto(anuncio.getCusto());
        existProd.setLucro(Anuncio.calculateLucro(existProd));
        existProd.setUser(user);
        verifyIfAnuncioMatchesUserOrThrowException(existProd, user);

        if(!existProd.isComplete()){
            List<Order> orders = orderEntityPort.listAllOrdersByAnuncio(existProd);
            orders.forEach(order -> order.getVendas().forEach(venda -> {
                if(venda.getCusto() == 0) venda.setCusto(existProd.getCusto());
                venda.setLucro(Anuncio.calculateLucro(existProd));

            }));
            orderEntityPort.createUpdateAll(orders);
        }
        existProd.setComplete(true);

        logger.infof("Final updateSimple: anuncio.mlId %s userId %d", anuncio.getMlId(), userId);
        return anuncioEntityPort.createUpdate(existProd);
    }

    //Atualiza dados somente do mercado livre
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
        completeAnuncio.setTaxaML(mercadoLivreAnuncioPort.getTarifas(completeAnuncio.getPrecoDesconto(),
                completeAnuncio.getCategoria(), completeAnuncio.getListingType(), user, true));
        if (completeAnuncio.getPrecoDesconto() >= 80){
            try {
                completeAnuncio.setCustoFrete(mercadoLivreAnuncioPort.getFrete(completeAnuncio.getMlId(), user, true));
            } catch (MercadoLivreException ignored) {}
        } else completeAnuncio.setCustoFrete(0.0);
        completeAnuncio.update(existProd);
        completeAnuncio.setCsosn(existProd.getCsosn());
        completeAnuncio.setCusto(existProd.getCusto());
        completeAnuncio.setLucro(Anuncio.calculateLucro(completeAnuncio));
        completeAnuncio.setUser(user);
        completeAnuncio.setComplete(true);
        verifyIfAnuncioMatchesUserOrThrowException(completeAnuncio, user);

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

    @Override
    @Transactional
    public AnuncioSimulationResponse simulateAnuncio(AnuncioSimulation anuncioSimulation) throws MercadoLivreException, FailRequestRefreshTokenException {
        User user = getUserOrThrowException(anuncioSimulation.getUserId());
        double taxaML = mercadoLivreAnuncioPort.getTarifas(anuncioSimulation.getValorVenda(),anuncioSimulation.getCategoria(),
                anuncioSimulation.getTipoAnuncio(),user,true);
        double imposto = Anuncio.calculateImposto(anuncioSimulation.getCsosn(),anuncioSimulation.getValorVenda());

        double frete;
        if(anuncioSimulation.getValorVenda() >= 80){
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

        if(Objects.isNull(existingAnuncio)){
            anuncio.setComplete(false);
            anuncio.setUser(user);
        }else anuncio.update(existingAnuncio);

        anuncioEntityPort.createUpdate(anuncio);
        logger.infof("Notificação processada com sucesso mlId: %s" , mlId);
    }
}
