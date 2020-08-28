package com.app.manager.service.implementClass;

import com.app.manager.context.repository.CourseCategoryRepository;
import com.app.manager.context.repository.RoleRepository;
import com.app.manager.context.repository.SubscriptionRepository;
import com.app.manager.entity.*;
import com.app.manager.model.SeederData;
import com.app.manager.service.interfaceClass.SeederService;
import com.app.manager.service.interfaceClass.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@SuppressWarnings("ALL")
@Service
public class SeederServiceImp implements SeederService {
    @Autowired SeederData seederData;
    @Autowired RoleRepository roleRepository;
    @Autowired SubscriptionRepository subscriptionRepository;
    @Autowired CourseCategoryRepository coursecategoryRepository;
    @Autowired UserService userService;

    @Override
    public void generateRoles() {
        for(ERole eRole : ERole.values()){
            try {
                if(eRole == ERole.ALL) continue;
                var role = roleRepository.findByName(eRole);
                if (role.isEmpty()) {
                    var newRole = new Role();
                    newRole.setName(eRole);
                    roleRepository.save(newRole);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Can create role: " + eRole.getName());
                System.out.println("Reason: " + e.getMessage());
                System.out.println("Cause by: " + e.getCause().toString());
            }
        }
    }

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
    public void generateCategory() {
        for (String name: seederData.getCourseCategoryNames()
        ) {
            try {
                var category = coursecategoryRepository.findFirstByName(name);
                if(category == null){
                    var newCategory = new CourseCategory();
                    newCategory.setName(name);
                    newCategory.setDescription(name);
                    coursecategoryRepository.save(newCategory);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Can create role: " + name);
                System.out.println("Reason: " + e.getMessage());
                System.out.println("Cause by: " + e.getCause().toString());
            }
        }
    }

    @Override
    public void generateUser() {
        seederData.getUserSeeds().forEach(signupRequest -> {
            var checkUsername = userService.checkExistUsername(signupRequest.getUsername());
            if (checkUsername.isEmpty() || checkUsername.get()) return;
            var checkEmail = userService.checkExistEmail(signupRequest.getEmail());
            if (checkEmail.isEmpty() || checkEmail.get()) return;
            var user = new User(signupRequest.getUsername(),
                    signupRequest.getEmail(),
                    signupRequest.getPassword());
            var rand = new Random();
            var chance = rand.nextInt(1000);
            var subscribtionName = signupRequest.getRole()
                    .contains(ERole.ROLE_ADMIN) ? Subscription.SubscriptionList.PREMIUM :
                    chance % 2 == 0 ? Subscription.SubscriptionList.PREMIUM :
                            Subscription.SubscriptionList.FREE;
            var subscribtion = getSubscribtionInstant(subscribtionName);
            System.out.println(userService.saveUser(user, signupRequest.getRole(),
                    subscribtion.get().getId()).getDescription());
        });
    }

    private Optional<Role> getRoleInstant(ERole roleName){
        try {
            var role = roleRepository.findByName(roleName);
            if(role.isPresent()) return role;
            var newRole = new Role(roleName);
            roleRepository.save(newRole);
            return Optional.of(newRole);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    private Optional<Subscription> getSubscribtionInstant(
            Subscription.SubscriptionList subscriptionName){
        try {
            var subscription = subscriptionRepository
                    .findFirstByName(subscriptionName.getName());
            if(subscription.isPresent()) return subscription;
            var newSubscription = new Subscription();
            newSubscription.setName(subscriptionName.getName());
            subscriptionRepository.save(newSubscription);
            return Optional.of(newSubscription);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }
}
