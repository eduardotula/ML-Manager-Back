package org.florense.inbound.adapter;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.florense.domain.scheduler.jobs.checkorderstatuschange.CheckOrderStatusChangeJob;
import org.florense.domain.scheduler.jobs.listallneworders.ListAllNewOrdersJob;
import org.florense.domain.usecase.JobsUseCase;

@RequestScoped
@Path("/jobs")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class JobsAdapter {

    @Inject
    JobsUseCase useCase;

    @GET
    @Path("/searchNewOrders")
    public void searchNewOrders(@QueryParam("user-id") long userId){
        useCase.searchNewOrders(userId);
    }

    @GET
    @Path("/searchUpdateOrderStatus")
    public void searchUpdateOrderStatus(@QueryParam("user-id") long userId){
        useCase.searchUpdateOrderStatus(userId);
    }
}
