package com.revature.repositories;

import com.revature.exceptions.crud.*;
import com.revature.exceptions.sql.NotNullConstraintException;
import com.revature.models.Reimbursement;
import com.revature.models.Status;

import java.util.List;
import java.util.Optional;

public interface IReimbursementDAO {
    Optional<Reimbursement> getById(int id);
    Optional<List<Reimbursement>> getByStatus(Status status);
    Reimbursement update(Reimbursement unprocessedReimbursement) throws UpdateUnsuccessfulException, NotNullConstraintException;
    Reimbursement createRequest(Reimbursement newReimbursement) throws CreationUnsuccessfulException, ItemHasNonZeroIdException;
    boolean deleteRequest(int id) throws DeleteUnsuccessfulException;
}
