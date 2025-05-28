-- Drop tables if they exist (in reverse order of dependencies)
DROP TABLE IF EXISTS SAVING_ACCOUNT;
DROP TABLE IF EXISTS BENEFICIARIES;
DROP TABLE IF EXISTS ACC_TRANSACTIONS;
DROP TABLE IF EXISTS ACCOUNT;
DROP TABLE IF EXISTS CLIENT;

-- Table CLIENT
CREATE TABLE CLIENT (
    id_client INT AUTO_INCREMENT,
    firstname VARCHAR(100) NOT NULL,
    lastname VARCHAR(100) NOT NULL,
    CIN VARCHAR(15) NOT NULL UNIQUE,	
    birthdate DATE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_client PRIMARY KEY (id_client)
);

-- Table ACCOUNT
CREATE TABLE ACCOUNT (
    id_account INT AUTO_INCREMENT,
    rib VARCHAR(50) UNIQUE NOT NULL,
    balance DECIMAL(12,2) DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    deleted_at DATETIME,
    type VARCHAR(20) NOT NULL CHECK (type IN ('CHECKING', 'SAVINGS')),
    id_client INT NOT NULL, 
    CONSTRAINT pk_account PRIMARY KEY (id_account),
    CONSTRAINT fk_account_client FOREIGN KEY (id_client) REFERENCES CLIENT(id_client)
);

-- Table TRANSACTION
CREATE TABLE ACC_TRANSACTIONS (
    id_transaction INT AUTO_INCREMENT,
    type VARCHAR(20) NOT NULL CHECK (type IN ('DEPOSIT', 'WITHDRAWAL', 'TRANSFER')),
    amount DECIMAL(12,2) NOT NULL CHECK (amount > 0),
    description VARCHAR(255),
    transaction_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'COMPLETED' CHECK (status IN ('PENDING', 'COMPLETED', 'FAILED')),
    id_account INT NOT NULL,
    id_recipient INT,
    CONSTRAINT pk_acc_transaction PRIMARY KEY (id_transaction), 
    CONSTRAINT fk_transaction_account FOREIGN KEY (id_account) REFERENCES ACCOUNT(id_account),
    CONSTRAINT fk_transaction_recipient FOREIGN KEY (id_recipient) REFERENCES ACCOUNT(id_account)
);

-- Table BENEFICIARY
CREATE TABLE BENEFICIARIES (
    id_account INT,
    id_beneficiary INT,
    added_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_beneficiaries PRIMARY KEY (id_account, id_beneficiary),
    CONSTRAINT fk_beneficiaries_account FOREIGN KEY (id_account) REFERENCES ACCOUNT(id_account),
    CONSTRAINT fk_beneficiaries_beneficiary FOREIGN KEY (id_beneficiary) REFERENCES ACCOUNT(id_account),
    CONSTRAINT check_different_accounts CHECK (id_account != id_beneficiary)
);

-- Table SAVING_ACCOUNT
CREATE TABLE SAVING_ACCOUNT (
    id_saving_account INT,
    id_account INT,
    interest_rate DECIMAL(5,2) NOT NULL CHECK (interest_rate >= 0),
    last_interest_calc_date DATE,
    CONSTRAINT pk_saving_account PRIMARY KEY (id_saving_account, id_account), 
    CONSTRAINT fk_saving_account FOREIGN KEY (id_saving_account) REFERENCES ACCOUNT(id_account),
    CONSTRAINT fk_base_account FOREIGN KEY (id_account) REFERENCES ACCOUNT(id_account)
);

-- Indexes for better performance
CREATE INDEX idx_client_email ON CLIENT(email);
CREATE INDEX idx_client_cin ON CLIENT(CIN);
CREATE INDEX idx_account_client ON ACCOUNT(id_client);
CREATE INDEX idx_account_type ON ACCOUNT(type);
CREATE INDEX idx_transaction_date ON ACC_TRANSACTIONS(transaction_date);
CREATE INDEX idx_transaction_account ON ACC_TRANSACTIONS(id_account);
CREATE INDEX idx_beneficiaries_account ON BENEFICIARIES(id_account);
CREATE INDEX idx_saving_account ON SAVING_ACCOUNT(id_saving_account); 