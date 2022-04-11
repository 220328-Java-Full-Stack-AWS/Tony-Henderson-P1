package com.revature.interfaces;

import com.revature.models.User;

import java.util.List;
import java.util.Optional;

public interface IUserDAO {

    Optional<User> getByUsername(String username);
    Optional<User> getById(int id);
    Optional<User> getByEmail(String email);
    List<User> getAllUsers();
    User create(User u);
    Optional<User> updateUserInfo(int id, User u);
    boolean deleteUser(User u);

}
