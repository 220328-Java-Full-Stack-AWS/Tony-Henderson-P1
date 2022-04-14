package com.revature.repositories;

import com.revature.models.Role;
import com.revature.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserDAO implements IUserDAO {

    static UserDAO dao = null;

    private UserDAO(){}

    public static UserDAO getDao(){
        if (dao == null){
            dao = new UserDAO();
        }
        return dao;
    }

    /**
     * Should retrieve a User from the DB with the corresponding username or an empty optional if there is no match.
     */
    public Optional<User> getByUsername(String username) {

        String sql = "SELECT * FROM users WHERE username = ?";

        try {
            PreparedStatement prepared = ConnectionManager.getConnection().prepareStatement(sql);

            prepared.setString(1, username);
            ResultSet resSet = prepared.executeQuery();

            if(resSet.next()){
                Optional<User> constructedUser = constructUser(resSet);
                if(constructedUser.isPresent()){
                    return constructedUser;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException illegalArgumentException){
            System.out.println(illegalArgumentException.getMessage());
            illegalArgumentException.printStackTrace();
        }

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

    private Optional<User> constructUser(ResultSet resSet) throws IllegalArgumentException{

        try {
            int id = resSet.getInt("user_id");
            String username = resSet.getString("username");
            String password = resSet.getString("password");
            Role role = Role.valueOf(resSet.getString("role"));
            String firstName = resSet.getString("first_name");
            String lastName = resSet.getString("last_name");
            String email = resSet.getString("email");
            String phoneNumber = resSet.getString("phone_number");
            String address = resSet.getString("address");

            User newUser = new User(id, username, password, role, firstName, lastName, email, phoneNumber, address);
            return Optional.of(newUser);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e){
            System.out.println("Cannot construct user from result set");
            e.printStackTrace();
            throw e;
        }
        return Optional.empty();
    }
}
