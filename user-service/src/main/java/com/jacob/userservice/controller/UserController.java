package com.jacob.userservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jacob.userservice.config.UsersServiceConfig;
import com.jacob.userservice.dto.Properties;
import com.jacob.userservice.dto.UserDto;
import com.jacob.userservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService service;

    @Autowired
    private UsersServiceConfig userConfig;

    @GetMapping("properties")
    public String getPropertyDetails() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Properties properties = new Properties(userConfig.getMsg(), userConfig.getBuildVersion(),
                userConfig.getMailDetails(), userConfig.getActiveBranches());
        return ow.writeValueAsString(properties);
    }
    @GetMapping("all")
    public Flux<UserDto> all() {
        return this.service.all();
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<UserDto>> getUserById(@RequestHeader("market-correlation-id") String correlationId, @PathVariable int id) {
        logger.info("getUserById() is called");
        return this.service.getUserById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .doOnNext(c -> logger.info("getUserById() has ended"));
    }

    @PostMapping
    public Mono<UserDto> createUser(@RequestHeader("market-correlation-id") String correlationId, @RequestBody Mono<UserDto> userDtoMono) {
        return this.service.createUser(userDtoMono);
    }

    @PutMapping("{id}")
    public Mono<ResponseEntity<UserDto>> updateUser(@PathVariable int id, @RequestBody Mono<UserDto> userDtoMono) {
        return this.service.updateUser(id, userDtoMono)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public Mono<Void> deleteUser(@PathVariable int id) {
        return this.service.deleteUser(id);
    }
}
