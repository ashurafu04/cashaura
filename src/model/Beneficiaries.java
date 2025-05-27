package model;

import java.time.LocalDateTime;

public class Beneficiaries {
    private int idAccount;        // Matches id_account in DB
    private int idBeneficiary;    // Matches id_beneficiary in DB
    private LocalDateTime addedAt; // Matches added_at in DB

    public Beneficiaries() {}

    public Beneficiaries(int idAccount, int idBeneficiary, LocalDateTime addedAt) {
        this.idAccount = idAccount;
        this.idBeneficiary = idBeneficiary;
        this.addedAt = addedAt;
    }

    // Getters and Setters
    public int getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(int idAccount) {
        this.idAccount = idAccount;
    }

    public int getIdBeneficiary() {
        return idBeneficiary;
    }

    public void setIdBeneficiary(int idBeneficiary) {
        this.idBeneficiary = idBeneficiary;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }

    @Override
    public String toString() {
        return "Beneficiary ID: " + idBeneficiary + " (for Account ID: " + idAccount + ")";
    }
}
