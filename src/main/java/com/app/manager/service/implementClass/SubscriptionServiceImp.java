package com.app.manager.service.implementClass;

import com.app.manager.entity.Subscription;
import com.app.manager.repository.SubscriptionRepository;
import com.app.manager.service.interfaceClass.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubscriptionServiceImp implements SubscriptionService {
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Override
    public void generateSubscription() {
        for(Subscription.SubscriptionList subscriptionName : Subscription.SubscriptionList.values()){
            var subscription = subscriptionRepository.findFirstByName(subscriptionName.getName());
            try {
                if (subscription == null) {
                    var newSubscription = new Subscription();
                    newSubscription.setName(subscriptionName.getName());
                    subscriptionRepository.save(newSubscription);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Can create role: " + subscriptionName.getName());
                System.out.println("Reason: " + e.getMessage());
                System.out.println("Cause by: " + e.getCause().toString());
            }
        }
    }

    @Override
    public Optional<Subscription> getBasicSubscription() {
        try {
            var subscription =
                    subscriptionRepository.findFirstByName(Subscription.SubscriptionList.FREE.getName());
            if (subscription == null) {
                var newSubscription = new Subscription();
                newSubscription.setName(Subscription.SubscriptionList.FREE.getName());
                subscriptionRepository.save(newSubscription);
                return Optional.of(newSubscription);
            }
            return Optional.of(subscription);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Reason: " + e.getMessage());
            System.out.println("Cause by: " + e.getCause().toString());
            return Optional.empty();
        }
    }
}
