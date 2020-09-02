package com.app.manager.service.implementClass;

import com.app.manager.context.repository.RoleRepository;
import com.app.manager.entity.ERole;
import com.app.manager.entity.Role;
import com.app.manager.service.interfaceClass.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class RoleServiceImp implements RoleService {
    @Autowired RoleRepository roleRepository;
    @Override
    public List<Role> getAll() {
        return roleRepository.findAll().stream().filter(
                role -> role.getName() != ERole.ROLE_ADMIN &&
                role.getStatus() == Role.StatusEnum.SHOW)
                .collect(Collectors.toList());
    }
}
