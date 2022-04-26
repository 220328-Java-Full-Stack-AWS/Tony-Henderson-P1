package com.revature.repositories;

import com.revature.exceptions.sql.CannotDeleteForeignKeyViolationException;
import com.revature.exceptions.crud.DeleteUnsuccessfulException;
import com.revature.exceptions.crud.ItemHasNonZeroIdException;
import com.revature.exceptions.crud.UpdateUnsuccessfulException;
import com.revature.exceptions.sql.NotNullConstraintException;
import com.revature.exceptions.user.EmailNotUniqueException;
import com.revature.exceptions.user.RegistrationUnsuccessfulException;
import com.revature.exceptions.user.UsernameNotUniqueException;
import com.revature.models.User;

import java.util.List;
import java.util.Optional;

public interface IUserDAO {

    Optional<User> getByUsername(String username);
    Optional<User> getById(int id);
    Optional<User> getByEmail(String email);
    List<User> getAllUsers();
    User create(User u) throws RegistrationUnsuccessfulException, ItemHasNonZeroIdException, EmailNotUniqueException, UsernameNotUniqueException, NotNullConstraintException;
    Optional<User> updateUserInfo(int id, User u) throws UpdateUnsuccessfulException;
    boolean deleteUser(User u) throws DeleteUnsuccessfulException, CannotDeleteForeignKeyViolationException;
}
