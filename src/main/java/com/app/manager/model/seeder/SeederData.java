package com.app.manager.model.seeder;

import com.app.manager.entity.ERole;
import com.app.manager.model.payload.request.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Component
public class SeederData {
    private static final String username = "senbonzakura1997";
    private static final String email = "anhdungpham090";
    private static final String password = "8mr5QyEABmiBp12D";
    @Autowired PasswordEncoder encoder;

    public SeederData() {
    }

    public List<String> getCourseCategoryNames (){
        return IntStream.range(0, 11).mapToObj(i -> i == 0 ?
                "Undefine" : "Category " + (i - 1)).collect(Collectors.toCollection(ArrayList::new));
    }

    public List<SignupRequest> getUserSeeds() {
        var list = new ArrayList<SignupRequest>();

        for (ERole role : ERole.values()) {
            if (role != ERole.ALL && role != ERole.ROLE_USER) {
                if (role != ERole.ROLE_ADMIN) {
                    for (int i = 0; i < (role == ERole.ROLE_TEACHER ? 10 : 10); i++)
                        list.add(new SignupRequest(getUsername(role.getName() + i),
                                getEmail(role.getName() + i), getPassword(),
                                new HashSet<>(Arrays.asList(role.getName(),
                                        ERole.ROLE_USER.getName()))));
                } else {
                    list.add(new SignupRequest(getUsername(""), getEmail(""),
                            getPassword(), new HashSet<>(Collections.singletonList(role.getName()))));
                }
            }
        }
        return list;
    }

    private String getUsername (String append){
        return username + append;
    }

    private String getEmail (String append){
        return email + append + "@gmail.com";
    }

    private String getPassword (){
        return encoder.encode(password);
    }
}
