package com.revature.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class JsonWebToken {

    private static String secret;
    private static String issuer;
    private static Algorithm algorithm;
    private static JWTVerifier verifier;

    private JsonWebToken(){}

    private static String createSecret(){
        Properties props = new Properties();

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream input = loader.getResourceAsStream("application.properties");
        try {
            props.load(input);

            secret = props.getProperty("secret");
            issuer = props.getProperty("issuer");
            return secret;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Couldn't get secret for JsonWebToken");
            System.exit(1);
        }
        return null;
    }

    /**
     * @param string Unencrypted string to sign
     * @return JsonWebToken
     */
    public static String sign(String string){

        if(secret == null)
            secret = createSecret();
        if(algorithm == null)
            algorithm =  Algorithm.HMAC512(secret);

        try {
            return JWT.create()
                    .withIssuer(issuer)
                    .withClaim("Json", string)
                    .sign(algorithm);
        } catch (JWTCreationException e){
            e.printStackTrace();
        }
        System.out.println("Wasn't able to sign: " + string);
        return null;
    }

    /**
     *
     * @param jwt JsonWebToken to decode
     * @return decoded String
     */
    public static String verify(String jwt) throws JWTVerificationException {

        if(verifier == null){
            verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build();
        }

        DecodedJWT decodedString = verifier.verify(jwt);

        return decodedString.getClaim("Json").asString();
    }



}
