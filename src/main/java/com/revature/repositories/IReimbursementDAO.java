package com.revature.repositories;

import com.revature.exceptions.CreationUnsuccessfulException;
import com.revature.exceptions.DeleteUnsuccessfulException;
import com.revature.exceptions.ItemHasNonZeroIdException;
import com.revature.exceptions.UpdateUnsuccessfulException;
import com.revature.models.Reimbursement;
import com.revature.models.Status;

import java.util.List;
import java.util.Optional;

public interface IReimbursementDAO {
    Optional<Reimbursement> getById(int id);
    Optional<List<Reimbursement>> getByStatus(Status status);
    Reimbursement update(Reimbursement unprocessedReimbursement) throws UpdateUnsuccessfulException;
    Reimbursement createRequest(Reimbursement newReimbursement) throws CreationUnsuccessfulException, ItemHasNonZeroIdException;
    boolean deleteRequest(int id) throws DeleteUnsuccessfulException;
}
