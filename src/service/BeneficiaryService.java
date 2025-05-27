package service;

import dao.BeneficiaryDAO;
import dao.AccountDAO;
import model.Beneficiaries;
import model.Account;
import java.sql.SQLException;
import java.util.List;

public class BeneficiaryService {
    private final BeneficiaryDAO beneficiaryDAO;
    private final AccountDAO accountDAO;

    public BeneficiaryService() {
        this.beneficiaryDAO = new BeneficiaryDAO();
        this.accountDAO = new AccountDAO();
    }

    public void addBeneficiary(int idAccount, int idBeneficiary) throws SQLException {
        // Validate that both accounts exist
        if (!accountDAO.findById(idAccount).isPresent()) {
            throw new IllegalArgumentException("Source account not found");
        }
        if (!accountDAO.findById(idBeneficiary).isPresent()) {
            throw new IllegalArgumentException("Beneficiary account not found");
        }

        // Check if this beneficiary is already added
        if (beneficiaryDAO.exists(idAccount, idBeneficiary)) {
            throw new IllegalArgumentException("This beneficiary is already added to your list");
        }

        // Add the beneficiary
        beneficiaryDAO.create(idAccount, idBeneficiary);
    }

    public List<Beneficiaries> getAccountBeneficiaries(int idAccount) throws SQLException {
        return beneficiaryDAO.findByAccountId(idAccount);
    }

    public void deleteBeneficiary(int idAccount, int idBeneficiary) throws SQLException {
        if (!beneficiaryDAO.exists(idAccount, idBeneficiary)) {
            throw new IllegalArgumentException("Beneficiary not found");
        }
        beneficiaryDAO.delete(idAccount, idBeneficiary);
    }

    public boolean validateBeneficiary(int idAccount, int idBeneficiary) throws SQLException {
        return beneficiaryDAO.exists(idAccount, idBeneficiary);
    }
} 