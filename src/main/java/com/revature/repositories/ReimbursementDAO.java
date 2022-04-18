package com.revature.repositories;

import com.revature.exceptions.CreationUnsuccessfulException;
import com.revature.exceptions.DeleteUnsuccessfulException;
import com.revature.exceptions.ItemHasNonZeroIdException;
import com.revature.exceptions.UpdateUnsuccessfulException;
import com.revature.models.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ReimbursementDAO implements IReimbursementDAO{

    private static ReimbursementDAO dao = null;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    private ReimbursementDAO(){}

    public static ReimbursementDAO getDao(){
        if(dao != null){
            return dao;
        }
        return new ReimbursementDAO();
    }

    @Override
    public Optional<Reimbursement> getById(int id) {

        String sql = "SELECT * FROM reimbursements_users_view WHERE reimbursement_id = ?";

        try {
            PreparedStatement pstmt = ConnectionManager.getConnection().prepareStatement(sql);

            pstmt.setInt(1, id);

            ResultSet resSet = pstmt.executeQuery();

            if(resSet.next()){
                Reimbursement reimb = constructReimbursement(resSet);
                return Optional.of(reimb);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return Optional.empty();
    }

    @Override
    public Optional<List<Reimbursement>> getByStatus(Status status) {

        String sql = "SELECT * FROM reimbursements_users_view WHERE status = ?";
        List<Reimbursement> reimbursementList = new LinkedList<>();

        try{
            PreparedStatement pstmt = ConnectionManager.getConnection().prepareStatement(sql);

            pstmt.setString(1, status.toString());

            ResultSet resSet = pstmt.executeQuery();

            while(resSet.next()){
                reimbursementList.add(constructReimbursement(resSet));
            }
            return Optional.of(reimbursementList);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Reimbursement update(Reimbursement unprocessedReimbursement) throws UpdateUnsuccessfulException {

        if(unprocessedReimbursement.getId() == 0){
            throw new UpdateUnsuccessfulException("Cannot update item as it has an id of 0. Maybe you would like to create it first?");
        }

        String sql = "UPDATE reimbursements SET status = ?, reimbursement_type = ?, resolver = ?, amount = ?::numeric::money, description = ?, resolution_date = ? WHERE reimbursement_id = ?";

        try {
            PreparedStatement pstmt = ConnectionManager.getConnection().prepareStatement(sql);

            String status = unprocessedReimbursement.getStatus().toString();

            pstmt.setString(1, status);
            pstmt.setString(2, unprocessedReimbursement.getType().toString());
            pstmt.setDouble(4, unprocessedReimbursement.getAmount());
            pstmt.setString(5, unprocessedReimbursement.getDescription());
            pstmt.setInt(7, unprocessedReimbursement.getId());

            if(status.equals("APPROVED") || status.equals("DENIED")){
                pstmt.setInt(3, unprocessedReimbursement.getResolver().getId());
                pstmt.setString(6, formatter.format(unprocessedReimbursement.getResolutionDate()));
            }
            else {
                pstmt.setString(3, null);
                pstmt.setString(6, null);
            }

            if(pstmt.executeUpdate() == 0){
                throw new UpdateUnsuccessfulException("No rows were updated");
            }

            return unprocessedReimbursement;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Reimbursement createRequest(Reimbursement newReimbursement) throws CreationUnsuccessfulException, ItemHasNonZeroIdException {

        int reimbursementId = newReimbursement.getId();
        String status = newReimbursement.toString();

        if(reimbursementId != 0){
            throw new ItemHasNonZeroIdException("Cannot create a reimbursement as it has an id that is not zero, therefore it might already exist. Maybe " +
                    "you would like to update this item.");
        }

        String sql = "INSERT INTO reimbursements (status, author, reimbursement_type, resolver, amount, description, creation_date, resolution_date) VALUES (?, ?, ?, ?, ?::numeric::money, ?, CREATION_DATE, ?);";

        try {
            PreparedStatement pstmt = ConnectionManager.getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, status); // status
            pstmt.setInt(2, newReimbursement.getAuthor().getId()); // author
            pstmt.setString(3, newReimbursement.getType().toString()); // reimbursement_type
            pstmt.setDouble(5, newReimbursement.getAmount()); // amount
            pstmt.setString(6, newReimbursement.getDescription()); // description

            if(status.equals("PENDING")){
                pstmt.setString(7, null); // resolution_date
                pstmt.setString(4, null); // resolver
            } else {
                pstmt.setString(7, newReimbursement.getResolutionDate().toString()); // resolution_date
                pstmt.setInt(4, newReimbursement.getResolver().getId()); // resolver
            }

            if(pstmt.executeUpdate() == 0){
                throw new CreationUnsuccessfulException("Reimbursement was not created");
            }
            else {
                ResultSet keys = pstmt.getGeneratedKeys();

                if(keys.next()){

                    newReimbursement.setId(keys.getInt(1));

                    return newReimbursement;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public boolean deleteRequest(int id) throws DeleteUnsuccessfulException {

        String sql = "DELETE FROM reimbursements WHERE reimbursement_id = ?";

        try {
            PreparedStatement pstmt = ConnectionManager.getConnection().prepareStatement(sql);
            pstmt.setInt(1, id);

            if(pstmt.executeUpdate() == 0){
                throw new DeleteUnsuccessfulException("Nothing in the database was deleted. The id: '" +
                        id + "' may not exist.");
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private Reimbursement constructReimbursement(ResultSet resultSet) throws SQLException {

        try {
            LinkedList<User> userList = constructUsers(resultSet);

            int id = resultSet.getInt("reimbursement_id");
            Status status = Status.valueOf(resultSet.getString("status"));
            ReimbursementType type = ReimbursementType.valueOf(resultSet.getString("reimbursement_type"));
            double amount = resultSet.getDouble("amount");
            String description = resultSet.getString("description");
            Date creationDate = formatter.parse(resultSet.getString("creation_date"));
            User author = userList.getFirst();
            User resolver = null;
            Date resolutionDate = null;

            if(userList.size() == 2){
                resolver = userList.getLast();
                resolutionDate = formatter.parse(resultSet.getString("resolution_date"));
            }

            Reimbursement reimb = new Reimbursement(status, author, resolver, amount, description, creationDate, resolutionDate, type);
            reimb.setId(id);

            return reimb;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Martials a result set retrieved from a reimbursement query and returns
     * a size 2 list where index 0 is the author User object and index 1 holds the resolver
     * User object. Every list contains an author so if list size is 1 the reimbursement has no resolvers
     * therefore it is still pending.
     * @param resSet ResultSet object returned from a join between reimbursements, users, and users
     * @return List<User>
     */
    private LinkedList<User> constructUsers(ResultSet resSet) throws SQLException {
        LinkedList<User> userList = new LinkedList<>();

        int id = resSet.getInt("author_user_id");
        String username = resSet.getString("author_username");
        String password = resSet.getString("author_password");
        Role role = Role.valueOf(resSet.getString("author_role"));
        String firstName = resSet.getString("author_first_name");
        String lastName = resSet.getString("author_last_name");
        String email = resSet.getString("author_email");
        String phoneNumber = resSet.getString("author_phone_number");
        String address = resSet.getString("author_address");

        User author = new User(username, password, role, firstName, lastName, email, phoneNumber, address);
        author.setId(id);
        userList.add(author);

        id = resSet.getInt("resolver_user_id");
        if(id != 0) {
            username = resSet.getString("resolver_username");
            password = resSet.getString("resolver_password");
            role = Role.valueOf(resSet.getString("resolver_role"));
            firstName = resSet.getString("resolver_first_name");
            lastName = resSet.getString("resolver_last_name");
            email = resSet.getString("resolver_email");
            phoneNumber = resSet.getString("resolver_phone_number");
            address = resSet.getString("resolver_address");

            User resolver = new User(username, password, role, firstName, lastName, email, phoneNumber, address);
            resolver.setId(id);
            userList.add(resolver);
        }

        return userList;
    }

}
