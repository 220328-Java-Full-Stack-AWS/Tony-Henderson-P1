package com.revature.servlets;

import com.revature.exceptions.crud.ItemHasNonZeroIdException;
import com.revature.exceptions.sql.NotNullConstraintException;
import com.revature.exceptions.user.EmailNotUniqueException;
import com.revature.exceptions.user.RegistrationUnsuccessfulException;
import com.revature.exceptions.user.UsernameNotUniqueException;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.services.AuthService;
import com.revature.servlets.dto.UserDTO;
import com.revature.utils.JsonWebToken;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static com.revature.utils.ObjectMapperSingleton.*;
import static com.revature.servlets.responses.Responder.*;

@WebServlet(name = "AuthRegisterServlet", value = "/auth/register")
public class AuthRegisterServlet extends HttpServlet {

    /**
     * Creates user with the Employee role
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        User userToRegister = bodyToModel(req, User.class);
        userToRegister.setRole(Role.EMPLOYEE);

        try {
            User registeredUser = AuthService.register(userToRegister);
            String registeredJson = modelToJson(registeredUser);
            UserDTO registeredUserDTO = jsonToModel(registeredJson, UserDTO.class);
            String jwt = JsonWebToken.sign(modelToJson(registeredUserDTO));

            resp.setHeader("SetCookie", jwt);
            respondWithSuccess(resp, 201, "User was successfully registered", registeredUserDTO);
            return;
        } catch (RegistrationUnsuccessfulException e) {
            e.printStackTrace();
            respondWithError(resp, 400, "An error occurred processing this request");
        } catch (ItemHasNonZeroIdException e) {
            e.printStackTrace();
            respondWithError(resp, 400, "Item already has an Id, check the request again");
        } catch (UsernameNotUniqueException e) {
            e.printStackTrace();
            respondWithError(resp, 400, "Someone with that username already exists");
        } catch (EmailNotUniqueException e) {
            e.printStackTrace();
            respondWithError(resp, 400, "Someone with that email already exists");
        } catch (NotNullConstraintException e) {
            e.printStackTrace();
            respondWithError(resp, 400, "Minimum required fields not given");
        }

    }
}
