package com.revature.servlets;


import com.revature.exceptions.auth.NotAuthorizedException;
import com.revature.exceptions.crud.CreationUnsuccessfulException;
import com.revature.exceptions.crud.DeleteUnsuccessfulException;
import com.revature.exceptions.crud.ItemHasNonZeroIdException;
import com.revature.exceptions.sql.NotNullConstraintException;
import com.revature.exceptions.crud.UpdateUnsuccessfulException;
import com.revature.exceptions.reimbursement.NoReimbursementExistsException;
import com.revature.exceptions.reimbursement.ReimbursementNoLongerPendingException;
import com.revature.exceptions.user.CantParseUserException;
import com.revature.servlets.dto.ReimbursementDto;
import com.revature.servlets.responses.Headers;
import com.revature.models.Reimbursement;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.exceptions.user.NoUserExistsException;
import com.revature.services.ReimbursementService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.revature.servlets.responses.Responder.*;
import static com.revature.utils.ObjectMapperSingleton.*;

@WebServlet(name = "ReimbursementsServlet", value = "/reimbursements")
public class ReimbursementsServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
    }

    /**
     * Responds to getting all reimbursements OR a specific reimbursement.
     *  If a reimbursement-id is passed in the header get the specific resource.
     *  <ul>
     *      <li>Admins only</li>
     *      <li>Requires {@code 'Authorization'} header.</li>
     *      <li>(Optional) {@code Reimbursement-Id} header. If desired outcome is a specific item.</li>
     *  </ul>
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reimbId = null;
        try {
            reimbId = req.getHeader(Headers.REIMBURSEMENT_ID.toString());
            Optional<User> opRequester = headerToRequester(req);

            if (opRequester.isPresent()) { // Have proper header
                User requester = opRequester.get();

                if (requester.getRole() == Role.FINANCE_MANAGER) { // is Admin

                    if (reimbId != null) { // Get specific reimbursement
                        Optional<Reimbursement> opReimb = ReimbursementService.getReimbursementById(requester, Integer.parseInt(reimbId));

                        if (opReimb.isPresent()) {
                            Reimbursement reimb = opReimb.get();

                            respondWithSuccess(resp, 200, "Successfully retrieved reimbursement", reimb);
                            return;
                        } else
                            throw new NoReimbursementExistsException();
                    } else { // Get all reimbursements
                        List<Reimbursement> reimbList = ReimbursementService.getAllReimbursements(requester);

                        respondWithSuccess(resp, 200, "Successfully retrieved reimbursements", reimbList);
                        return;
                    }
                }
            }
        } catch (CantParseUserException | NotAuthorizedException e) {
            e.printStackTrace();
            respondWithError(resp, 403, "You are not authorized to make this request");
            return;
        } catch (NoReimbursementExistsException e) {
            e.printStackTrace();
            respondWithError(resp, 404, "There was no reimbursement found with the Id: " + Integer.parseInt(reimbId));
            return;
        }
        respondWithError(resp, 403, "You are not authorized to make this request");
    }

    /**
     * Updates a specific reimbursement.
     *  <ul>
     *      <li>Admins only</li>
     *      <li>Requires {@code 'Authorization'} header.</li>
     *      <li>Requires {@code Reimbursement} item in body</li>
     *  </ul>
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Optional<User> opRequester = headerToRequester(req);

            if(!opRequester.isPresent())
                throw new NoUserExistsException();

            User requester = opRequester.get();
            if(requester.getRole() == Role.FINANCE_MANAGER){
                Reimbursement reimbursement = bodyToModel(req, Reimbursement.class);
                reimbursement = ReimbursementService.editRequest(requester, reimbursement);
                respondWithSuccess(resp, 200, "Successfully updated reimbursement", reimbursement);
                return;
            }

        } catch (NotAuthorizedException | NoUserExistsException | CantParseUserException e) { // Not Authorized
            e.printStackTrace();
            respondWithError(resp, 403, "You are not authorized to make this request");
            return;
        } catch (ReimbursementNoLongerPendingException e) {
            respondWithError(resp, 400, "This reimbursement is no longer pending");
            return;
        } catch (UpdateUnsuccessfulException e) {
            respondWithError(resp, 400, "There was a problem with completing the process");
            return;
        } catch (NotNullConstraintException e) {
            respondWithError(resp, 400, "Not all required fields were given");
            return;
        }
        respondWithError(resp, 403, "You are not authorized to make this request");
    }

    /**
     * Responds to getting all reimbursements OR a specific reimbursement.
     *  <ul>
     *      <li>Admins only</li>
     *      <li>Requires {@code 'Authorization'} header.</li>
     *  </ul>
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            try {
                Optional<User> opRequester = headerToRequester(req);

                if(!opRequester.isPresent())
                    throw new NotAuthorizedException();

                User requester = opRequester.get();
                if(requester.getRole() != Role.FINANCE_MANAGER)
                    throw new NotAuthorizedException();

                Reimbursement reimbursementToDelete = bodyToModel(req, Reimbursement.class);
                ReimbursementService.deleteRequest(requester.getId() ,reimbursementToDelete.getId());

                respondWithSuccess(resp, 202, "Successfully deleted item", null);
            } catch (NotAuthorizedException | CantParseUserException | NoUserExistsException e) {
                e.printStackTrace();
                respondWithError(resp, 403, "You are not authorized to make this request");
            } catch (NoReimbursementExistsException e) {
                e.printStackTrace();
                respondWithError(resp, 400, "That reimbursement doesn't exist anymore");
            } catch (DeleteUnsuccessfulException e) {
                e.printStackTrace();
                respondWithError(resp, 500, "Failed to delete Reimbursement");
            }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Optional<User> opRequester = headerToRequester(req);
            
            if(!opRequester.isPresent())
                throw new NoUserExistsException();
            
            User requester = opRequester.get();
            if(requester.getRole() != Role.FINANCE_MANAGER)
                throw new NotAuthorizedException();
            
            ReimbursementDto reimbToSubmit = bodyToModel(req, ReimbursementDto.class);
            System.out.println(reimbToSubmit);
            Reimbursement newReimbursement = ReimbursementService.submit(reimbToSubmit);

            respondWithSuccess(resp, 201, "Successfully created reimbursement", newReimbursement);
        } catch (CantParseUserException |NotAuthorizedException | NoUserExistsException e) {
            e.printStackTrace();
            respondWithError(resp, 403, "You are not authorized to make this request");
        } catch (CreationUnsuccessfulException e) {
            e.printStackTrace();
            respondWithError(resp, 400, "There was a problem submitting the reimbursement");
        } catch (ItemHasNonZeroIdException e) {
            e.printStackTrace();
            respondWithError(resp, 400, "Reimbursement already has an Id. Failed to create.");
        }

    }
}
