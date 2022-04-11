package com.revature.models;

import java.util.Date;
import java.util.Objects;

/**
 * This concrete Reimbursement class can include additional fields that can be used for
 * extended functionality of the ERS application.
 *
 * Example fields:
 * <ul>
 *     <li>Description</li>
 *     <li>Creation Date</li>
 *     <li>Resolution Date</li>
 *     <li>Receipt Image</li>
 * </ul>
 *
 */
public class Reimbursement extends AbstractReimbursement {

    private String description;
    private Date creationDate;
    private Date resolutionDate;
//    private Date receiptImage;

    public Reimbursement() {
        super();
    }

    /**
     * This includes the minimum parameters needed for the {@link com.revature.models.AbstractReimbursement} class.
     * If other fields are needed, please create additional constructors.
     */
    public Reimbursement(int id, Status status, User author, User resolver, double amount) {
        super(id, status, author, resolver, amount);
    }

    public Reimbursement(int id, Status status, User author, User resolver, double amount, String description, Date creationDate, Date resolutionDate) {
        super(id, status, author, resolver, amount);
        this.description = description;
        this.creationDate = creationDate;
        this.resolutionDate = resolutionDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getResolutionDate() {
        return resolutionDate;
    }

    public void setResolutionDate(Date resolutionDate) {
        this.resolutionDate = resolutionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Reimbursement that = (Reimbursement) o;
        return Objects.equals(description, that.description) && Objects.equals(creationDate, that.creationDate) && Objects.equals(resolutionDate, that.resolutionDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), description, creationDate, resolutionDate);
    }

    @Override
    public String toString() {
        return "Reimbursement{" +
                "description='" + description + '\'' +
                ", creationDate=" + creationDate +
                ", resolutionDate=" + resolutionDate +
                '}';
    }
}
