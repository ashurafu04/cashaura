package dao;

import model.SavingAccount;
import java.sql.SQLException;
import java.util.List;

public interface SavingAccountDAO {
    SavingAccount create(SavingAccount savingAccount) throws SQLException;
    SavingAccount findById(int idSavingAccount) throws SQLException;
    List<SavingAccount> findByClientId(int idClient) throws SQLException;
    void updateInterestCalculationDate(int idSavingAccount) throws SQLException;
    void updateInterestRate(int idSavingAccount, double newRate) throws SQLException;
    boolean delete(int idSavingAccount) throws SQLException;
}