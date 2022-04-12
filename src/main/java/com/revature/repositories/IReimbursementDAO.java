package com.revature.repositories;

import com.revature.models.Reimbursement;
import com.revature.models.Status;

import java.util.List;
import java.util.Optional;

public interface IReimbursementDAO {
    Optional<Reimbursement> getById(int id);
    List<Reimbursement> getByStatus(Status status);
    Reimbursement update(Reimbursement unprocessedReimbursement);
    Reimbursement createRequest(Reimbursement newReimbursement);
    boolean deleteRequest(int id);
}
