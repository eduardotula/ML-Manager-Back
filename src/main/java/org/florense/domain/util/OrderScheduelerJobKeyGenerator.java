package org.florense.domain.util;

import jakarta.enterprise.context.ApplicationScoped;
import org.florense.domain.model.User;
import org.quartz.JobKey;

@ApplicationScoped
public class OrderScheduelerJobKeyGenerator {

    public JobKey createJobKey(User user){
        return new JobKey(String.format("%s_searchOrders", user.getName()), "searchOrders");
    }
}
