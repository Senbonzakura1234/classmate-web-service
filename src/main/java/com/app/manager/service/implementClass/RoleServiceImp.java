package com.app.manager.service.implementClass;

import com.app.manager.entity.ERole;
import com.app.manager.entity.Role;
import com.app.manager.context.repository.RoleRepository;
import com.app.manager.service.interfaceClass.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImp implements RoleService {
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    RoleRepository roleRepository;

    @Override
    public void generateRoles() {
        for(ERole eRole : ERole.values()){
            try {
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
}
