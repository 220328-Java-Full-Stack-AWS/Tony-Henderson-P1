package com.revature.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.exceptions.auth.CannotParseJWT;
import com.revature.exceptions.user.CantParseUserException;
import com.revature.servlets.responses.Headers;
import com.revature.models.User;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

public class ObjectMapperSingleton {

    private static ObjectMapper mapper;

    private ObjectMapperSingleton() {
    }

    public static ObjectMapper getMapper() {
        if (mapper == null) {
            mapper = new ObjectMapper();
        }
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    public static <T> T bodyToModel(HttpServletRequest req, Class<T> modelClass) throws IOException {
        return getMapper().readValue(req.getInputStream(), modelClass);
    }

    public static String modelToJson(Object model) throws JsonProcessingException {
        return getMapper().writeValueAsString(model);
    }


    public static <T> T jsonToModel(String json, Class<T> modelClass) throws JsonProcessingException {
        return getMapper().readerFor(modelClass).readValue(json);
    }

    public static String bodyToJson(HttpServletRequest req) throws IOException {
        return req.getReader().lines().collect(Collectors.joining());
    }

    /**
     * Takes an {@code HttpServletRequest} req object and outputs a {@code Optional<User>}
     * @param req Must contain an 'Authorization' with proper jwt to parse.
     */
    public static Optional<User> headerToRequester(HttpServletRequest req) throws CantParseUserException {
        String bearer = req.getHeader(Headers.AUTHORIZATION.toString());

        if(bearer == null)
            return Optional.empty();

        try{
            String json = JsonWebToken.verify(bearer);
            User user = jsonToModel(json, User.class);
            return Optional.ofNullable(user);

        } catch(CannotParseJWT | JsonProcessingException e){
            e.printStackTrace();
            throw new CantParseUserException();
        }
    }
}
