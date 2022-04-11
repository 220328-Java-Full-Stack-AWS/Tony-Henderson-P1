package com.revature.repositories;

import com.revature.models.User;
import com.revature.interfaces.IUserDAO;

import java.util.List;
import java.util.Optional;

public class UserDAO implements IUserDAO {

    /**
     * Should retrieve a User from the DB with the corresponding username or an empty optional if there is no match.
     */
    public Optional<User> getByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public Optional<User> getById(int id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    /**
     * <ul>
     *     <li>Should Insert a new User record into the DB with the provided information.</li>
     *     <li>Should throw an exception if the creation is unsuccessful.</li>
     *     <li>Should return a User object with an updated ID.</li>
     * </ul>
     */
    public User create(User userToBeRegistered) {
        return userToBeRegistered;
    }

    @Override
    public Optional<User> updateUserInfo(int id, User u) {
        return Optional.empty();
    }

    @Override
    public boolean deleteUser(User u) {
        return false;
    }
}
