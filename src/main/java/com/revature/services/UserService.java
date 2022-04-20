package com.revature.services;

import com.revature.exceptions.reimbursement.auth.NotAuthorizedException;
import com.revature.exceptions.crud.UpdateUnsuccessfulException;
import com.revature.exceptions.user.NoUserExistsException;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.repositories.UserDAO;

import java.util.List;
import java.util.Optional;

/**
 * The UserService should handle the processing and retrieval of Users for the ERS application.
 *
 * {@code getByUsername} is the only method required;
 * however, additional methods can be added.
 *
 * Examples:
 * <ul>
 *     <li>Create User</li>
 *     <li>Update User Information</li>
 *     <li>Get Users by ID</li>
 *     <li>Get Users by Email</li>
 *     <li>Get All Users</li>
 * </ul>
 */
public class UserService {

	private static UserDAO uDao = UserDAO.getDao();

	/**
	 * Admins can request any user. Users can only get themselves.
	 * @param requesteeUserId User Id of person taking the action
	 * @param username Username to query
	 */
	public static Optional<User> getByUsername(int requesteeUserId, String username) throws NoUserExistsException, NotAuthorizedException {
		Optional<User> opUser = uDao.getByUsername(username);
		if(opUser.isPresent()){
			User u = opUser.get();

			if(u.getId() != requesteeUserId)
				AuthService.getAdminUser(requesteeUserId);

			return Optional.of(u);

		}
		else
			return Optional.empty();
	}

	/**
	 * Admins can request any user. Users can only get themselves.
	 * @param requesteeUserId User Id of person taking the action
	 * @param requestingUserId User Id to query
	 */
	public static Optional<User> getById(int requesteeUserId, int requestingUserId) throws NoUserExistsException, NotAuthorizedException {

		Optional<User> opUser = uDao.getById(requestingUserId);
		if(opUser.isPresent()){
			User u = opUser.get();

			if(u.getId() != requesteeUserId)
				AuthService.getAdminUser(requesteeUserId);

			return Optional.of(u);

		}
		else
			return Optional.empty();
	}

	/**
	 * Admins can request any user. Users can only get themselves.
	 * @param requesteeUserId User Id of person taking the action
	 * @param email User email to query
	 */
	public static Optional<User> getByEmail(int requesteeUserId, String email) throws NoUserExistsException, NotAuthorizedException {

		Optional<User> opUser = uDao.getByEmail(email);
		if(opUser.isPresent()){
			User u = opUser.get();

			if(u.getId() != requesteeUserId)
				AuthService.getAdminUser(requesteeUserId);

			return Optional.of(u);

		}
		else
			return Optional.empty();
	}

	/**
	 * Only Available to Admins
	 */
	public static List<User> getAllUsers(int requesteeUserId) throws NotAuthorizedException {

		try {
			AuthService.getAdminUser(requesteeUserId);
		} catch (NoUserExistsException e) {
			e.printStackTrace();
			throw new NotAuthorizedException();
		}

		return uDao.getAllUsers();
	}


	/**
	 * Only Admins can promote users
	 * @param requesteeUserId User Id of person taking the action
	 * @param userIdToChange User Id of person to promote
	 */
	public static User changeToAdmin(int requesteeUserId, int userIdToChange) throws NoUserExistsException, NotAuthorizedException, UpdateUnsuccessfulException {
		AuthService.getAdminUser(requesteeUserId);

		Optional<User> opUser = uDao.getById(userIdToChange);

		if(opUser.isPresent()){
			User u = opUser.get();

			u.setRole(Role.FINANCE_MANAGER);
			uDao.updateUserInfo(userIdToChange, u);

			return u;
		}
		else
			throw new NoUserExistsException();
	}
}
