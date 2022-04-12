package com.revature.repositories;

import com.revature.models.Reimbursement;
import com.revature.models.Status;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ReimbursementDAO implements IReimbursementDAO{

    /**
     * Should retrieve a Reimbursement from the DB with the corresponding id or an empty optional if there is no match.
     */
    @Override
    public Optional<Reimbursement> getById(int id) {
        return Optional.empty();
    }

    /**
     * Should retrieve a List of Reimbursements from the DB with the corresponding Status or an empty List if there are no matches.
     */
    @Override
    public List<Reimbursement> getByStatus(Status status) {
        return Collections.emptyList();
    }

    /**
     * <ul>
     *     <li>Should Update an existing Reimbursement record in the DB with the provided information.</li>
     *     <li>Should throw an exception if the update is unsuccessful.</li>
     *     <li>Should return a Reimbursement object with updated information.</li>
     * </ul>
     */
    @Override
    public Reimbursement update(Reimbursement unprocessedReimbursement) {
    	return null;
    }

    @Override
    public Reimbursement createRequest(Reimbursement newReimbursement) {
        return null;
    }

    @Override
    public boolean deleteRequest(int id) {
        return false;
    }
}
