package com.revature.servlets;

import com.revature.exceptions.auth.NotAuthorizedException;
import com.revature.exceptions.crud.CreationUnsuccessfulException;
import com.revature.exceptions.crud.DeleteUnsuccessfulException;
import com.revature.exceptions.crud.ItemHasNonZeroIdException;
import com.revature.exceptions.crud.UpdateUnsuccessfulException;
import com.revature.exceptions.reimbursement.NoReimbursementExistsException;
import com.revature.exceptions.reimbursement.ReimbursementNoLongerPendingException;
import com.revature.exceptions.sql.NotNullConstraintException;
import com.revature.exceptions.user.CantParseUserException;
import com.revature.exceptions.user.NoUserExistsException;
import com.revature.models.Reimbursement;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.services.ReimbursementService;
import com.revature.services.UserService;
import com.revature.servlets.exceptions.MissingHeaderException;
import com.revature.servlets.responses.Headers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.revature.utils.ObjectMapperSingleton.*;
import static com.revature.servlets.responses.Responder.*;

@WebServlet(name = "UsersReimbursementsServlet", value = "/users/reimbursements")
public class UsersReimbursementsServlet extends HttpServlet {

    /**
     * Gets one/all of specified users current reimbursements. Admins can get any users reimbursements. Non-admins
     * can only get their own.
     * <ul>
     *     <li>Requires {@code Authorization} header</li>
     *     <li>Requires {@code User-Id} header</li>
     *     <li>(Optional) {@code Reimbursement-Id} if specific is desired</li>
     * </ul>
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Optional<User> opRequester = headerToRequester(req);
            if(!opRequester.isPresent())
                throw new NotAuthorizedException();

            User requester = opRequester.get();

            String reimbursementHeader = req.getHeader(Headers.REIMBURSEMENT_ID.toString());
            String idHeader = req.getHeader(Headers.USER_ID.toString());
            int reimbursementIdHeader = Integer.parseInt(reimbursementHeader != null ? reimbursementHeader : "0");
            int userIdHeader = Integer.parseInt(idHeader != null ? idHeader : "0");
            boolean isGetOne = reimbursementIdHeader != 0;
            if(userIdHeader == 0)
                throw new MissingHeaderException();

            // Admin
            if(requester.getRole() == Role.FINANCE_MANAGER){

                if(isGetOne){
                    Optional<User> opUser = UserService.getById(requester.getId(), userIdHeader);
                    if(!opUser.isPresent())
                        throw new NoUserExistsException();

                    List<Reimbursement> reimbList =  ReimbursementService.getReimbursementsByUser(requester.getId(), userIdHeader);

                    for(Reimbursement reimb : reimbList){
                        if(reimb.getId() == reimbursementIdHeader){
                            respondWithSuccess(resp, 200, "Successfully retrieved users reimbursement", reimb);
                            return;
                        }
                    }
                    throw new NoReimbursementExistsException();

                } else { // Get all
                    List<Reimbursement> reimbList = ReimbursementService.getReimbursementsByUser(requester.getId(), userIdHeader);

                    respondWithSuccess(resp, 200, "Successfully retrieved users reimbursements", reimbList);
                    return;
                }
            }
            // Non-admin
            else {
                if(requester.getId() != userIdHeader)
                    throw new NotAuthorizedException();

                if(isGetOne){
                    List<Reimbursement> reimbList = new ArrayList(ReimbursementService.getReimbursementsByUser(requester.getId(), userIdHeader));

                    for(Reimbursement reimb : reimbList){
                        if(reimb.getId() == reimbursementIdHeader){
                            respondWithSuccess(resp, 200, "Successfully retrieved users reimbursement", reimb);
                            return;
                        }
                    }

                    throw new NoReimbursementExistsException();
                }
                else { // Get all
                    List<Reimbursement> reimbList = ReimbursementService.getReimbursementsByUser(requester.getId(), userIdHeader);

                    respondWithSuccess(resp, 200, "Successfully retrieved you reimbursements", reimbList);
                    return;
                }
            }

        } catch (NotAuthorizedException | CantParseUserException e) {
            e.printStackTrace();
            respondWithError(resp, 403, "You are not authorized to make this request");
        } catch (NoUserExistsException e) {
            e.printStackTrace();
            respondWithError(resp, 400, "The user with that id doesn't exist");
        } catch (NoReimbursementExistsException e) {
            e.printStackTrace();
            respondWithError(resp, 400, "The reimbursement with that id doesn't exist for this user");
        } catch (MissingHeaderException e) {
            e.printStackTrace();
            respondWithError(resp, 400, "You are mising required User-Id header");
        }

    }

    /**
     * Creates a reimbursement on requester otherwise if User-Id header is given create a reimbursment for that user.
     * <ul>
     *     <li>Requires {@code Authorization} header</li>
     *     <li>(optional) {@code User-Id} header</li>
     * </ul>
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Optional<User> opRequester = headerToRequester(req);
            if(!opRequester.isPresent())
                throw new NotAuthorizedException();

            User requester = opRequester.get();
            boolean isAdmin = requester.getRole() == Role.FINANCE_MANAGER;
            boolean addOnSelf = false;
            Reimbursement reimbursement = bodyToModel(req, Reimbursement.class);
            String userIdHeader = req.getHeader(Headers.USER_ID.toString());
            int userId = 0;
            if(userIdHeader == null){
                addOnSelf = true;
                userId = requester.getId();
            }
            else
                userId = Integer.parseInt(userIdHeader);

            Optional<User> opAuthor = UserService.getById(requester.getId(), userId);
            if(!opAuthor.isPresent())
                throw new NoUserExistsException();

            if(isAdmin){
                if(addOnSelf){
                    reimbursement.setAuthor(requester);
                    Reimbursement submittedReimbursement = ReimbursementService.submit(reimbursement);

                    respondWithSuccess(resp, 201, "Thanks for submitting your request", submittedReimbursement);
                    return;
                }
                reimbursement.setAuthor(opAuthor.get());
                Reimbursement submittedReimbursement = ReimbursementService.submit(reimbursement);

                respondWithSuccess(resp, 201, "Successfully created a reimbursement for this user", submittedReimbursement);
                return;
            } else { // Non-admin
                // make sure requester is the author
                if(!addOnSelf)
                    throw new NotAuthorizedException();

                reimbursement.setAuthor(requester);
                Reimbursement submittedReimbursement = ReimbursementService.submit(reimbursement);

                respondWithSuccess(resp, 201, "Thanks for submitting your request", submittedReimbursement);
                return;
            }

        } catch (NotAuthorizedException | CantParseUserException e) {
            e.printStackTrace();
            respondWithError(resp, 403, "You do not have authorization to make this request");
        } catch (CreationUnsuccessfulException e) {
            e.printStackTrace();
            respondWithError(resp, 400, "Failed. There was an error processing the request");
        } catch (NoUserExistsException e) {
            e.printStackTrace();
            respondWithError(resp, 400, "No user exists with the specified id");
        } catch (ItemHasNonZeroIdException e) {
            e.printStackTrace();
            respondWithError(resp, 400, "The item must not have an Id");
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Optional<User> opUser = headerToRequester(req);
            if(!opUser.isPresent())
                throw new NotAuthorizedException();

            User requester = opUser.get();
            Reimbursement reimbToUpdate = bodyToModel(req, Reimbursement.class);

            Reimbursement submittedReimbursement = ReimbursementService.editRequest(requester, reimbToUpdate);

            respondWithSuccess(resp, 200, "Successfully updated reimbursement", submittedReimbursement);

        } catch (NotAuthorizedException | CantParseUserException e) {
            e.printStackTrace();
            respondWithError(resp, 403, "You are not authorized to make this request");
        } catch (ReimbursementNoLongerPendingException e) {
            e.printStackTrace();
            respondWithError(resp, 400, "This reimbursement is no longer pending");
        } catch (NoUserExistsException e) {
            e.printStackTrace();
            respondWithError(resp, 400, "No user exists with the specified id");
        } catch (NotNullConstraintException e) {
            e.printStackTrace();
            respondWithError(resp, 400, "Missing required fields");
        } catch (UpdateUnsuccessfulException e) {
            e.printStackTrace();
            respondWithError(resp, 400, "Failed. There was a problem processing the request");
        }


    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            Optional<User> opRequester = headerToRequester(req);
            if(!opRequester.isPresent())
                throw new NotAuthorizedException();

            User requester = opRequester.get();
            String userIdHeader = req.getHeader(Headers.USER_ID.toString());
            String reimbursementIdHeader = req.getHeader(Headers.REIMBURSEMENT_ID.toString());
            boolean isAdmin = opRequester.get().getRole() == Role.FINANCE_MANAGER;
            boolean targetSelf = true;
            boolean doDeleteAllForUser = true;
            int targetUserId = 0;
            int targetReimbId = 0;


            if(userIdHeader != null){
                targetSelf = false;
                targetUserId = Integer.parseInt(userIdHeader);
            }
            if(reimbursementIdHeader != null){
                doDeleteAllForUser = false;
                targetReimbId = Integer.parseInt(reimbursementIdHeader);
            }


            if(isAdmin){
                if(targetSelf){

                    if(doDeleteAllForUser)
                        ReimbursementService.deleteAllForUser(requester.getId());
                    else {
                        Optional<Reimbursement> opReimbursement = ReimbursementService.getReimbursementById(requester, targetReimbId);
                        if(!opReimbursement.isPresent() || opReimbursement.get().getAuthor().getId() != requester.getId())
                            throw new NoReimbursementExistsException();
                        ReimbursementService.deleteRequest(requester.getId(), targetReimbId);
                    }

                } else { // targetUserId

                    if(doDeleteAllForUser)
                        ReimbursementService.deleteAllForUser(targetUserId);
                    else
                        ReimbursementService.deleteRequest(requester.getId(), targetReimbId);
                }
            } else { // Non-admin

                if(!targetSelf && targetUserId != requester.getId())
                    throw new NotAuthorizedException();

                if(doDeleteAllForUser)
                    ReimbursementService.deleteAllForUser(requester.getId());
                else
                    ReimbursementService.deleteRequest(requester.getId(), targetReimbId);
            }
            respondWithSuccess(resp, 202, "Successfully deleted reimbursement(s)", null);

        } catch (NotAuthorizedException | CantParseUserException e) {
            e.printStackTrace();
            respondWithError(resp, 403, "You are not authorized to make this request");
        } catch (DeleteUnsuccessfulException e) {
            e.printStackTrace();
            respondWithError(resp, 400, "There was a problem deleting the reimbursement(s). It's possible there is none to delete.");
        } catch (NoReimbursementExistsException e) {
            e.printStackTrace();
            respondWithError(resp, 400, "No reimbursement exists with specified id for this user");
        } catch (NoUserExistsException e) {
            e.printStackTrace();
            respondWithError(resp, 400, "No user exists with specified id");
        }

    }


}
