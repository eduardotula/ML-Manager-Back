package org.florense.domain.usecase;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.florense.domain.model.User;
import org.florense.domain.scheduler.jobs.checkorderstatuschange.CheckOrderStatusChange;
import org.florense.domain.scheduler.jobs.checkorderstatuschange.CheckOrderStatusChangeJob;
import org.florense.domain.scheduler.jobs.listallneworders.ListAllNewOrders;
import org.florense.domain.scheduler.jobs.listallneworders.ListAllNewOrdersJob;
import org.florense.outbound.port.postgre.UserEntityPort;

import java.util.Objects;

@RequestScoped
public class JobsUseCase {
    @Inject
    CheckOrderStatusChange checkOrderStatusChange;
    @Inject
    ListAllNewOrders listAllNewOrders;
    @Inject
    UserEntityPort userEntityPort;

    public void searchNewOrders(Long userId){
        User user = getUserOrThrowException(userId);
        listAllNewOrders.execute(user);
    }

    public void searchUpdateOrderStatus(Long userId){
        User user = getUserOrThrowException(userId);
        checkOrderStatusChange.execute(user);
    }

    private User getUserOrThrowException(Long userId) throws IllegalArgumentException {
        User user = userEntityPort.findById(userId);
        if (Objects.isNull(user))
            throw new IllegalArgumentException(String.format("User com id: %s não encontrado", userId));
        return user;
    }
}
