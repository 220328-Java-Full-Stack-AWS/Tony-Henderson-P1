package com.revature.servlets.responses;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.revature.utils.ObjectMapperSingleton.modelToJson;

public class Responder {

    private Responder(){}

    /**
     * Takes in {@code HttpServletResponse} and wires up the request body.
     * @param res The @{HttpServletResponse} object
     * @param status The http status code
     * @param message Any message to send to the client
     * @throws IOException
     */
    public static void respondWithError(HttpServletResponse res, int status, String message) throws IOException {
        res.setStatus(status);
        res.setContentType("application/json");
        res.getWriter().print(modelToJson(new ErrorResponse(status, message)));
    }

    /**
     * Takes in {@code HttpServletResponse} and wires up the request body.
     * @param resp The {@code HttpServletResponse} object
     * @param status The http status code
     * @param message Any message to send to the client
     * @param data Model to parse into body
     * @throws IOException
     */
    public static <T> void respondWithSuccess(HttpServletResponse resp, int status, String message, T data) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json");
        resp.getWriter().print(modelToJson(new SuccessResponse(status, message, data)));
    }
}
