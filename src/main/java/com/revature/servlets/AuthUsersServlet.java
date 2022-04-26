package com.revature.servlets;

import com.revature.exceptions.auth.LoginFailedException;
import com.revature.models.User;
import com.revature.services.AuthService;
import com.revature.servlets.responses.ErrorResponse;
import com.revature.servlets.responses.SuccessResponse;
import com.revature.utils.JsonWebToken;

import static com.revature.utils.ObjectMapperSingleton.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "AuthUsersServlet", value = "/auth/user")
public class AuthUsersServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User userToAuthenticate = bodyToModel(req, User.class);

        try {
            // Authenticate user
            User user = AuthService.login(userToAuthenticate.getUsername(), userToAuthenticate.getPassword());
            String jwt = JsonWebToken.sign(modelToJson(user));

            Cookie userCookie = new Cookie("Authorization", jwt);
            userCookie.setPath("/");
            resp.addCookie(userCookie);

            resp.setStatus(202);
            resp.setContentType("application/json");
            resp.getWriter().print(modelToJson(new SuccessResponse(202, "Successful login", user)));
        } catch (LoginFailedException e) {
            resp.setStatus(401);
            resp.setContentType("application/json");
            resp.getWriter().print(modelToJson(new ErrorResponse(401, "The username or password is incorrect")));
        }
    }
}