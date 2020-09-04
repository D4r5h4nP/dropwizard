package com.dropwizard.demo.authentication;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

public class AppAuthenticator implements Authenticator<BasicCredentials, User> {
   
    private static final Map<String,ImmutableSet<String>> VALID_USERS = ImmutableMap.of(
        "user", ImmutableSet.of("USER"),
        "admin", ImmutableSet.of("ADMIN")
    );

	@Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
        if (VALID_USERS.containsKey(credentials.getUsername()) && "password".equals(credentials.getPassword())) {
            return Optional.of(new User(credentials.getUsername(), (Set<String>) VALID_USERS.get(credentials.getUsername())));
        }
        return Optional.empty();
    }
}