package com.revature.services;

import com.revature.exceptions.auth.NotAuthorizedException;
import com.revature.exceptions.crud.CreationUnsuccessfulException;
import com.revature.exceptions.crud.DeleteUnsuccessfulException;
import com.revature.exceptions.crud.ItemHasNonZeroIdException;
import com.revature.exceptions.crud.UpdateUnsuccessfulException;
import com.revature.exceptions.reimbursement.NoReimbursementExistsException;
import com.revature.exceptions.reimbursement.ReimbursementNoLongerPendingException;
import com.revature.exceptions.user.NoUserExistsException;
import com.revature.models.Reimbursement;
import com.revature.models.ReimbursementType;
import com.revature.models.Status;
import com.revature.models.User;
import com.revature.repositories.ReimbursementDAO;
import com.revature.repositories.UserDAO;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * The ReimbursementService should handle the submission, processing,
 * and retrieval of Reimbursements for the ERS application.
 *
 * {@code process} and {@code getReimbursementsByStatus} are the minimum methods required;
 * however, additional methods can be added.
 *
 * Examples:
 * <ul>
 *     <li>Create Reimbursement</li>
 *     <li>Update Reimbursement</li>
 *     <li>Get Reimbursements by ID</li>
 *     <li>Get Reimbursements by Author</li>
 *     <li>Get Reimbursements by Resolver</li>
 *     <li>Get All Reimbursements</li>
 * </ul>
 */
public class ReimbursementService {

    private static UserDAO uDao = UserDAO.getDao();
    private static ReimbursementDAO rDao = ReimbursementDAO.getDao();

    private ReimbursementService(){}

    /**
     * <ul>
     *     <li>Should ensure that the user is logged in as a Finance Manager</li>
     *     <li>Must throw exception if user is not logged in as a Finance Manager</li>
     *     <li>Should ensure that the reimbursement request exists</li>
     *     <li>Must throw exception if the reimbursement request is not found</li>
     *     <li>Should persist the updated reimbursement status with resolver information</li>
     *     <li>Must throw exception if persistence is unsuccessful</li>
     * </ul>
     *
     * Note: unprocessedReimbursement will have a status of PENDING, a non-zero ID and amount, and a non-null Author.
     * The Resolver should be null. Additional fields may be null.
     * After processing, the reimbursement will have its status changed to either APPROVED or DENIED.
     */
    public static Reimbursement process(Reimbursement unprocessedReimbursement, Status finalStatus, int userId) throws NoUserExistsException, NotAuthorizedException, NoReimbursementExistsException {

        User resolver = AuthService.getAdminUser(userId);
        Optional<Reimbursement> opReimbursement = rDao.getById(unprocessedReimbursement.getId());

        if(opReimbursement.isPresent()){
            Reimbursement reimbursement = opReimbursement.get();

            reimbursement.setStatus(finalStatus);
            reimbursement.setResolver(resolver);
            reimbursement.setResolutionDate(new Date());

            return reimbursement;
        }
        else
            throw new NoReimbursementExistsException();
    }

    /**
     * Should retrieve all reimbursements with the correct status.
     * Available only to Admins
     */
    public static List<Reimbursement> getReimbursementsByStatus(int id, Status status) throws NoUserExistsException, NotAuthorizedException {
        AuthService.getAdminUser(id);

        Optional<List<Reimbursement>> opReimbList = rDao.getByStatus(status);

        return opReimbList.orElse(Collections.emptyList());
    }

    /**
     * Should be available to Finance_Managers only
     */
    public static List<Reimbursement> getAllReimbursements(int userId) throws NoUserExistsException, NotAuthorizedException {

        AuthService.getAdminUser(userId);

       Optional<List<Reimbursement>> opReimbList = rDao.getAll();

       return opReimbList.orElse(Collections.emptyList());
    }

    public static Reimbursement submit(ReimbursementType type, String desc, double amount, int userId) throws ItemHasNonZeroIdException, CreationUnsuccessfulException, NoUserExistsException {
         Optional<User> opUser = uDao.getById(userId);

         if(opUser.isPresent()){
             User user = opUser.get();

             Reimbursement reimbursement = new Reimbursement(Status.PENDING, user, amount, desc, new Date(), type);

                 return rDao.createRequest(reimbursement);
         }
         else {
             throw new NoUserExistsException();
         }
    }

    /**
     * Admins can delete any request. Users can only delete their own PENDING requests.
     * @param userId Id of user performing the action
     * @param reimbursementId Id of the reimbursement that is trying to be canceled
     * @return {@code true} if the request was deleted or {@code false}
     * @throws DeleteUnsuccessfulException if for some reason the delete didn't persist
     */
    public static boolean deleteRequest(int userId, int reimbursementId) throws DeleteUnsuccessfulException, NoReimbursementExistsException, NoUserExistsException, NotAuthorizedException {
        Optional<Reimbursement> opReimb = rDao.getById(reimbursementId);
        Optional<User> opUser = uDao.getById(userId);

        if(opReimb.isPresent()){
            Reimbursement reimb = opReimb.get();
            if(!opUser.isPresent())
                throw new NoUserExistsException();

            User reimbAuthor = reimb.getAuthor();
            if(reimbAuthor.getId() == userId && reimb.getStatus() == Status.PENDING){
                rDao.deleteRequest(reimbursementId);
            }
            else{
                AuthService.getAdminUser(userId);

                rDao.deleteRequest(reimbursementId);
            }
            return true;
        }
        else{
            throw new NoReimbursementExistsException();
        }
    }

    /**
     * If the {@code requesteeUserId} does not belong to an admin they can only lookup their own records.
     * @param requesteeUserId The user Id that is requesting the lookup
     * @param requestingUserId The user Id that the requestee is looking to query
     * @return List of Reimbursements belonging to the {@code requestingUserId} User
     */
    public static List<Reimbursement> getReimbursementsByUser(int requesteeUserId, int requestingUserId) throws NoUserExistsException, NotAuthorizedException {

        if (requesteeUserId != requestingUserId) {
            AuthService.getAdminUser(requesteeUserId);
        }
        return rDao.getAllFromUser(requestingUserId).orElse(Collections.emptyList());
    }

    /**
     * Admins can edit any pending requests. Users can only edit their own pending requests.
     * @param userId The user Id of the person editing.
     * @param newReimbursement The edited reimbursement request.
     * @return The edited request.
     */
    public static Reimbursement editRequest(int userId, Reimbursement newReimbursement) throws ReimbursementNoLongerPendingException, NoUserExistsException, NotAuthorizedException, UpdateUnsuccessfulException {
        if(newReimbursement.getStatus() == Status.PENDING){
            if(newReimbursement.getAuthor().getId() != userId){
                AuthService.getAdminUser(userId);
            }
            return rDao.update(newReimbursement);
        }
        else {
            throw new ReimbursementNoLongerPendingException();
        }
    }
}
