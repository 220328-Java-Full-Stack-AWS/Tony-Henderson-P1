package com.revature.servlets;

import com.revature.exceptions.auth.LoginFailedException;
import com.revature.exceptions.user.CantParseUserException;
import com.revature.exceptions.user.NoUserExistsException;
import com.revature.models.User;
import com.revature.services.AuthService;
import com.revature.servlets.dto.UserDTO;
import com.revature.servlets.responses.ErrorResponse;
import com.revature.servlets.responses.SuccessResponse;
import com.revature.utils.JsonWebToken;

import static com.revature.utils.ObjectMapperSingleton.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import static com.revature.servlets.responses.Responder.*;

@WebServlet(name = "AuthUsersServlet", value = "/auth/user")
public class AuthUsersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Optional<User> opRequester = headerToRequester(req);

            if(!opRequester.isPresent())
                throw new NoUserExistsException();

            User requester = opRequester.get();
            String userJson = modelToJson(requester);
            UserDTO userDto = jsonToModel(userJson, UserDTO.class);
            respondWithSuccess(resp, 200, "Hello " + userDto.getFirstName(), userDto);
        } catch (NoUserExistsException | CantParseUserException e) {
            e.printStackTrace();
            respondWithError(resp, 404, "You are not logged in");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User userToAuthenticate = bodyToModel(req, User.class);

        try {
            // Authenticate user
            User user = AuthService.login(userToAuthenticate.getUsername(), userToAuthenticate.getPassword());
            String userJson = modelToJson(user);
            UserDTO requesterDTO = jsonToModel(userJson, UserDTO.class);
            String jwt = JsonWebToken.sign(modelToJson(requesterDTO));

            resp.setHeader("SetCookie", jwt);

            resp.setStatus(202);
            resp.setContentType("application/json");
            resp.getWriter().print(modelToJson(new SuccessResponse(202, "Successful login", requesterDTO)));
        } catch (LoginFailedException e) {
            resp.setStatus(401);
            resp.setContentType("application/json");
            resp.getWriter().print(modelToJson(new ErrorResponse(401, "The username or password is incorrect")));
        }
    }
}