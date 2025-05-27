-- Create beneficiary table
CREATE TABLE IF NOT EXISTS beneficiary (
    id INT PRIMARY KEY AUTO_INCREMENT,
    client_id INT NOT NULL,
    account_rib VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (client_id) REFERENCES client(id_client),
    FOREIGN KEY (account_rib) REFERENCES account(rib),
    UNIQUE KEY unique_client_rib (client_id, account_rib)
); 