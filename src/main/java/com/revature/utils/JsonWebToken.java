package com.revature.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.revature.exceptions.auth.CannotParseJWT;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class JsonWebToken {

    private static String issuer;
    private static Algorithm algorithm;
    private static JWTVerifier verifier;

    private JsonWebToken(){}


    /**
     * @param string Unencrypted string to sign
     * @return JsonWebToken
     */
    public static String sign(String string){
        try {
            return JWT.create()
                    .withIssuer(issuer)
                    .withClaim("Json", string)
                    .sign(algorithm);
        } catch (JWTCreationException e){
            e.printStackTrace();
        }
        System.out.println("JWT Wasn't able to sign: " + string);
        return null;
    }

    public static String verify(String jwt) throws CannotParseJWT {
        try {
            DecodedJWT decodedString = verifier.verify(jwt);
            return decodedString.getClaim("Json").asString();
        } catch (JWTVerificationException e) {
            throw new CannotParseJWT();
        }
    }

    public static void load(){
        Properties props = new Properties();

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream input = loader.getResourceAsStream("application.properties");
        try {
            props.load(input);

            String secret = props.getProperty("secret");
            issuer = props.getProperty("issuer");
            algorithm =  Algorithm.HMAC512(secret);
            verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Couldn't get secret for JsonWebToken");
            System.exit(1);
        }
    }
}
