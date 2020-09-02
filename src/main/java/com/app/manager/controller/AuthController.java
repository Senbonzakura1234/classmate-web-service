package com.app.manager.controller;

import com.app.manager.entity.ESubscription;
import com.app.manager.entity.User;
import com.app.manager.model.payload.request.LoginRequest;
import com.app.manager.model.payload.request.SignupRequest;
import com.app.manager.model.payload.response.JwtResponse;
import com.app.manager.model.payload.response.MessageResponse;
import com.app.manager.model.seeder.Seeder;
import com.app.manager.security.authService.UserDetailsImpl;
import com.app.manager.security.jwt.JwtUtils;
import com.app.manager.service.interfaceClass.RoleService;
import com.app.manager.service.interfaceClass.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/auth", consumes = "application/json", produces = "application/json")
public class AuthController {
    @Autowired AuthenticationManager authenticationManager;
    @Autowired UserService userService;
    @Autowired RoleService roleService;
    @Autowired PasswordEncoder encoder;
    @Autowired JwtUtils jwtUtils;
    @Autowired Seeder seeder;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
                                              BindingResult bindingResult) {
//        seeder.Seed(); // uncomment this to seed data, comment it again when done seeding
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .forEach(System.out::println);
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Validate Error"));
        }

        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        var jwt = jwtUtils.generateJwtToken(authentication);

        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest,
                                          BindingResult bindingResult) {
//        seeder.Seed(); // uncomment this to seed data, comment it again when done seeding

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .forEach(System.out::println);
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Validate Error"));
        }

        var checkUsername = userService.checkExistUsername(signUpRequest.getUsername());
        if (checkUsername.isEmpty() || checkUsername.get()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(
                            checkUsername.isEmpty()? "Error: Sever error!" :
                                    "Error: Username is already in use!"
                    ));
        }
        var checkEmail = userService.checkExistEmail(signUpRequest.getEmail());
        if (checkEmail.isEmpty() || checkEmail.get()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(
                            checkEmail.isEmpty()? "Error: Sever error!" :
                                    "Error: Email is already in use!"
                    ));
        }

        // Create new user's account
        var user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        var strRoles = signUpRequest.getRole();
        var result = userService.saveUser(user, strRoles,
                ESubscription.FREE);

        return result.isSuccess() ?
                ResponseEntity
                    .ok(new MessageResponse("User registered successfully!")) :
                ResponseEntity
                    .badRequest()
                    .body(result);
    }

    @GetMapping("/getRoleList")
    public ResponseEntity<?> getRoleList(){
        return ResponseEntity.ok(roleService.getAll());
    }
}
