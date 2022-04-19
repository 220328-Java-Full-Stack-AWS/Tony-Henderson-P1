package com.revature.services;

import com.revature.exceptions.auth.LoginFailedException;
import com.revature.exceptions.auth.NotAuthorizedException;
import com.revature.exceptions.crud.ItemHasNonZeroIdException;
import com.revature.exceptions.user.EmailNotUniqueException;
import com.revature.exceptions.user.NoUserExistsException;
import com.revature.exceptions.user.RegistrationUnsuccessfulException;
import com.revature.exceptions.user.UsernameNotUniqueException;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.repositories.UserDAO;

import java.util.Optional;

/**
 * The AuthService should handle login and registration for the ERS application.
 *
 * {@code login} and {@code register} are the minimum methods required; however, additional methods can be added.
 *
 * Examples:
 * <ul>
 *     <li>Retrieve Currently Logged-in User</li>
 *     <li>Change Password</li>
 *     <li>Logout</li>
 * </ul>
 */
public class AuthService {

    private static UserDAO uDao = UserDAO.getDao();

    private AuthService(){}

    /**
     * <ul>
     *     <li>Needs to check for existing users with username/email provided.</li>
     *     <li>Must throw exception if user does not exist.</li>
     *     <li>Must compare password provided and stored password for that user.</li>
     *     <li>Should throw exception if the passwords do not match.</li>
     *     <li>Must return user object if the user logs in successfully.</li>
     * </ul>
     */
    public static User login(String username, String password) throws LoginFailedException, NoUserExistsException {

        Optional<User> u = uDao.getByUsername(username);

        if(u.isPresent()){
            User user = u.get();

            if(user.getPassword().equals(password))
                return user;
            else
                throw new LoginFailedException();
        }
        else
            throw new NoUserExistsException();
    }

    /**
     * <ul>
     *     <li>Should ensure that the username/email provided is unique.</li>
     *     <li>Must throw exception if the username/email is not unique.</li>
     *     <li>Should persist the user object upon successful registration.</li>
     *     <li>Must throw exception if registration is unsuccessful.</li>
     *     <li>Must return user object if the user registers successfully.</li>
     *     <li>Must throw exception if provided user has a non-zero ID</li>
     * </ul>
     *
     * Note: userToBeRegistered will have an id=0, additional fields may be null.
     * After registration, the id will be a positive integer.
     */
    public static User register(User userToBeRegistered) throws RegistrationUnsuccessfulException, ItemHasNonZeroIdException, UsernameNotUniqueException, EmailNotUniqueException {
        return uDao.create(userToBeRegistered);
    }

//    /**
//     * This is an example method signature for retrieving the currently logged-in user.
//     * It leverages the Optional type which is a useful interface to handle the
//     * possibility of a user being unavailable.
//     */
//    public static  Optional<User> exampleRetrieveCurrentUser(int id) {
//        return uDao.getById(id);
//    }
    
    public static User getAdminUser(int id) throws NoUserExistsException, NotAuthorizedException {
        Optional<User> opUser = uDao.getById(id);

        if(opUser.isPresent()){
            User u = opUser.get();

            if(u.getRole() == Role.EMPLOYEE)
                throw new NotAuthorizedException();

            return u;
        }
        else {
            throw new NoUserExistsException();
        }
    }
}
