package com.app.manager.repository;

import com.app.manager.entity.Subscription;
import org.springframework.data.repository.CrudRepository;

public interface SubscriptionRepository extends CrudRepository<Subscription, String> {
    Subscription findFirstByName(String name);
}
