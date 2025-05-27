package controller;

import view.BeneficiaryView;
import service.BeneficiaryService;
import model.Beneficiaries;
import dao.AccountDAO;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BeneficiaryController {
    private BeneficiaryView view;
    private BeneficiaryService service;
    private AccountDAO accountDAO;
    private int currentAccountId;

    public BeneficiaryController(BeneficiaryView view, int accountId) {
        this.view = view;
        this.service = new BeneficiaryService();
        this.accountDAO = new AccountDAO();
        this.currentAccountId = accountId;

        // Initialize view
        initializeView();
        
        // Add listeners
        this.view.addAddListener(new AddBeneficiaryListener());
        this.view.addDeleteListener(new DeleteBeneficiaryListener());
        this.view.addBackListener(e -> view.dispose());
    }

    private void initializeView() {
        try {
            // Load and display existing beneficiaries
            view.setBeneficiaries(service.getAccountBeneficiaries(currentAccountId));
        } catch (SQLException e) {
            view.showError("Error loading beneficiaries: " + e.getMessage());
        }
    }

    private class AddBeneficiaryListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String beneficiaryRib = view.getAccountRib();
                if (beneficiaryRib.isEmpty()) {
                    view.showError("Please enter a beneficiary RIB");
                    return;
                }

                // Find account by RIB
                var beneficiaryAccount = accountDAO.findByRib(beneficiaryRib);
                if (beneficiaryAccount.isEmpty()) {
                    view.showError("Invalid beneficiary RIB");
                    return;
                }

                // Add beneficiary
                service.addBeneficiary(currentAccountId, beneficiaryAccount.get().getId());
                
                // Refresh the list
                view.setBeneficiaries(service.getAccountBeneficiaries(currentAccountId));
                view.clearFields();
                view.showSuccess("Beneficiary added successfully");
            } catch (IllegalArgumentException ex) {
                view.showError(ex.getMessage());
            } catch (SQLException ex) {
                view.showError("Error adding beneficiary: " + ex.getMessage());
            }
        }
    }

    private class DeleteBeneficiaryListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Beneficiaries selected = view.getSelectedBeneficiary();
                if (selected == null) {
                    view.showError("Please select a beneficiary to delete");
                    return;
                }

                // Delete beneficiary
                service.deleteBeneficiary(currentAccountId, selected.getIdBeneficiary());
                
                // Refresh the list
                view.setBeneficiaries(service.getAccountBeneficiaries(currentAccountId));
                view.showSuccess("Beneficiary deleted successfully");
            } catch (SQLException ex) {
                view.showError("Error deleting beneficiary: " + ex.getMessage());
            }
        }
    }
} 