package service;

import dao.ClientDAO;

public class ClientService {
    private final ClientDAO clientDAO;

    public ClientService() {
        this.clientDAO = new ClientDAO();
    }

    public boolean authenticate(String email, String password) {
        // This is a placeholder. You should implement password hashing and secure comparison in production.
        return clientDAO.checkCredentials(email, password);
    }

    public boolean register(model.Client client) {
        try {
            clientDAO.addClient(client);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public model.Client getClientByEmail(String email) {
        try {
            return clientDAO.getClientByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
} 