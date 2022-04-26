package com.revature.servlets;

import com.revature.exceptions.auth.NotAuthorizedException;
import com.revature.exceptions.crud.UpdateUnsuccessfulException;
import com.revature.exceptions.sql.NotNullConstraintException;
import com.revature.exceptions.user.CantParseUserException;
import com.revature.exceptions.user.NoUserExistsException;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.services.UserService;
import com.revature.servlets.responses.Headers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.revature.utils.ObjectMapperSingleton.*;
import static com.revature.servlets.responses.Responder.*;

@WebServlet(name = "UsersServlet", value = "/users")
public class UsersServlet extends HttpServlet {

    /**
     * Gets one or all users. Non-admin users can only get themselves. Getting all users is Admin only.
     * <ul>
     *     <li>Requires {@code Authorization} header</li>
     *     <li>(Optional) {@code User-Id} Header if desired outcome is one user</li>
     * </ul>
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Optional<User> opRequester = headerToRequester(req);

            // Not a user/Not logged in
            if(!opRequester.isPresent())
                throw new NotAuthorizedException();

            User requester = opRequester.get();

            // Admin User
            if(requester.getRole() == Role.FINANCE_MANAGER) {
                String userIdHeader = req.getHeader(Headers.USER_ID.toString());
                // Get all users
                if(userIdHeader == null){
                    List<User> allUsers = UserService.getAllUsers(requester.getId());

                    respondWithSuccess(resp, 200, "Successfully retrieved all users", allUsers);
                    return;
                } else {
                    int userIdToGet = Integer.parseInt(userIdHeader);
                    Optional<User> opRetrievedUser = UserService.getById(requester.getId(), userIdToGet);
                    if(!opRetrievedUser.isPresent())
                        throw new NoUserExistsException();

                    respondWithSuccess(resp, 200, "Successfully retrieved user", opRetrievedUser.get());
                    return;
                }
            }
            // Non-admin user (gets themselves)
            respondWithSuccess(resp, 200, "Here is yourself", requester);

        } catch (NotAuthorizedException | CantParseUserException e) {
            e.printStackTrace();
            respondWithError(resp, 403, "You are not authorized to make this request");
        } catch (NoUserExistsException e) {
            e.printStackTrace();
            respondWithError(resp, 400, "No user exists with that id");
        }
    }

    /**
     * Updates a user. Non-admin users can only update themselves.
     * <ul>
     *     <li>Requires {@code Authorization} header</li>
     * </ul>
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            Optional<User> opRequester = headerToRequester(req);

            if(!opRequester.isPresent())
                throw new NotAuthorizedException();

            User requester =  opRequester.get();
            User userUpdatedInfo = bodyToModel(req, User.class);

            if(requester.getRole() == Role.FINANCE_MANAGER)
                UserService.update(userUpdatedInfo);
            else if(userUpdatedInfo.getId() == requester.getId())
                UserService.update(userUpdatedInfo);
            else
                throw new NotAuthorizedException();

            respondWithSuccess(resp, 200, "Successfully updated user", userUpdatedInfo);
        } catch (CantParseUserException | NotAuthorizedException e) {
            e.printStackTrace();
            respondWithError(resp, 403, "You are not authorized to make this request");
        } catch (UpdateUnsuccessfulException e) {
            e.printStackTrace();
            respondWithError(resp, 400, "Failed to update user");
        } catch (NotNullConstraintException e) {
            e.printStackTrace();
            respondWithError(resp, 400, "Failed. Missing required user information");
        }

    }

}
