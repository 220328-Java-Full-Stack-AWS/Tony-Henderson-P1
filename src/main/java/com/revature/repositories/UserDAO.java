package com.revature.repositories;

import com.revature.exceptions.*;
import com.revature.models.Role;
import com.revature.models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
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
                return constructUser(resSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> getById(int id) {

        try{
            String sql = "SELECT * from users WHERE user_id = ?";

            PreparedStatement pstmt = ConnectionManager.getConnection().prepareStatement(sql);
            pstmt.setInt(1, id);

            ResultSet resSet = pstmt.executeQuery();

            if(resSet.next()){
                return constructUser(resSet);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> getByEmail(String email) {
        try{
            String sql = "SELECT * from users WHERE email = ?";

            PreparedStatement pstmt = ConnectionManager.getConnection().prepareStatement(sql);
            pstmt.setString(1, email);

            ResultSet resSet = pstmt.executeQuery();

            if(resSet.next()){
                return constructUser(resSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public List<User> getAllUsers() {

        List<User> allUsers = new LinkedList<>();
        try{
            String sql = "SELECT * FROM users";

            PreparedStatement pstmt = ConnectionManager.getConnection().prepareStatement(sql);
            ResultSet resSet = pstmt.executeQuery();

            while(resSet.next()){
                allUsers.add(constructUser(resSet).get());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allUsers;
    }

    /**
     * <ul>
     *     <li>Should Insert a new User record into the DB with the provided information.</li>
     *     <li>Should throw an exception if the creation is unsuccessful.</li>
     *     <li>Should return a User object with an updated ID.</li>
     * </ul>
     */
    public User create(User userToBeRegistered) throws RegistrationUnsuccessfulException, UserHasNonZeroIdException {

        if(userToBeRegistered.getId() != 0){
            throw new UserHasNonZeroIdException("User: '" + userToBeRegistered.getUsername() + "' already has an id. If" +
                    " this is a mistake set id to 0 and try again.");
        }
        try{
            String sql = "INSERT INTO users (username, password, role, first_name, last_name, email, phone_number, address) VALUES (?,?,?,?,?,?,?,?)";

            PreparedStatement pstmt = ConnectionManager.getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, userToBeRegistered.getUsername());
            pstmt.setString(2, userToBeRegistered.getPassword());
            pstmt.setString(3, userToBeRegistered.getRole().toString());
            pstmt.setString(4, userToBeRegistered.getFirstName());
            pstmt.setString(5, userToBeRegistered.getLastName());
            pstmt.setString(6, userToBeRegistered.getEmail());
            pstmt.setString(7, userToBeRegistered.getPhoneNumber());
            pstmt.setString(8, userToBeRegistered.getAddress());

            if(pstmt.executeUpdate() == 0){
                throw new RegistrationUnsuccessfulException("Failed to register user " + userToBeRegistered.getUsername());
            }

            if(pstmt.getGeneratedKeys().next()){
                userToBeRegistered.setId(pstmt.getGeneratedKeys().getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RegistrationUnsuccessfulException("Failed to register user " + userToBeRegistered.getUsername());
        }

        return userToBeRegistered;
    }

    @Override
    public Optional<User> updateUserInfo(int id, User u) throws UpdateUnsuccessfulException {
        if(u.getId() == 0) {
            throw new UpdateUnsuccessfulException("Failed to update user, the user has id = 0. " +
                    "Maybe you intend to create a user?");
        }

        try{
            String sql = "UPDATE users SET username = ?, password = ?, role = ?, first_name = ?, last_name = ?, email = ?, phone_number = ?, address = ? WHERE user_id = ?";

            PreparedStatement pstmt = ConnectionManager.getConnection().prepareStatement(sql);
            pstmt.setString(1, u.getUsername());
            pstmt.setString(2, u.getPassword());
            pstmt.setString(3, u.getRole() != null ? u.getRole().toString() : Role.EMPLOYEE.name());
            pstmt.setString(4, u.getFirstName());
            pstmt.setString(5, u.getLastName());
            pstmt.setString(6, u.getEmail());
            pstmt.setString(7, u.getPhoneNumber());
            pstmt.setString(8, u.getAddress());
            pstmt.setInt(9, id);

            if(pstmt.executeUpdate() == 0) {
                throw new UpdateUnsuccessfulException();
            }

            return Optional.of(u);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public boolean deleteUser(User u) throws DeleteUserUnsuccessfulException, CannotDeleteForeignKeyViolationException {

        try{
            String sql = "DELETE FROM users WHERE user_id = ?";

            PreparedStatement pstmt = ConnectionManager.getConnection().prepareStatement(sql);
            pstmt.setInt(1, u.getId());

            if(pstmt.executeUpdate() == 0){
                throw new DeleteUserUnsuccessfulException("Nothing was changed in the database");
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getSQLState().equals("23503")){
                throw new CannotDeleteForeignKeyViolationException();
            }
            throw new DeleteUserUnsuccessfulException("Failed to ");
        }
    }

    private Optional<User> constructUser(ResultSet resSet){

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

            User newUser = new User(username, password, role, firstName, lastName, email, phoneNumber, address);
            newUser.setId(id);
            return Optional.of(newUser);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
