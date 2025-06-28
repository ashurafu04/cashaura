# Cashaura Banking Application

**Cashaura** is a modern desktop banking application built with **Java** and **Swing**. It allows users to manage accounts, perform transactions, and handle beneficiaries through a clean and intuitive interface.

## 🚀 Features

* 🔐 User authentication (login/register)
* 🏦 Account management (create, view, close)
* 💸 Transaction operations (deposit, withdraw, transfer)
* 👥 Beneficiary management
* 💰 Savings account support
* 🖥️ Minimalistic, responsive Swing-based UI

## 📦 Prerequisites

* **Java 11** or higher
* **MySQL 8.0+**
* A Java IDE (like IntelliJ IDEA or Eclipse) or `javac` CLI

## ⚙️ Setup Instructions

1. **Clone the repository**

   ```bash
   git clone https://github.com/yourusername/cashaura.git
   cd cashaura
   ```

2. **Create the MySQL database**

   ```sql
   CREATE DATABASE cashaura;
   ```

3. **Import the schema**

   ```bash
   mysql -u root -p cashaura < database/schema.sql
   ```

4. **Configure database connection**

   Edit the file:

   ```
   src/resources/database.properties
   ```

   Replace the default values with your MySQL `username` and `password`.

5. **Compile and Run the Application**

   From the root directory:

   ```bash
   javac -cp "lib/mysql-connector-java-8.0.xx.jar" -d bin src/**/*.java
   java -cp "bin:lib/mysql-connector-java-8.0.xx.jar" Main
   ```

   > ☝️ Replace `8.0.xx` with your version of the MySQL JDBC connector.

## 🧩 Project Structure

```
src/
├── controller/   # Handles user interactions (MVC Controllers)
├── dao/          # Data Access Layer (CRUD operations)
├── model/        # Data models (User, Account, Transaction, etc.)
├── service/      # Business logic
├── util/         # Utility classes (e.g., DB connection, validation)
└── view/         # UI components (Java Swing)
```

## 🔧 Development Notes

* Built using **native Java** (no external frameworks)
* Follows the **MVC architecture** for better code organization
* Uses **JDBC** for direct MySQL interaction
* UI entirely built using **Swing**

## 🤝 Contributing

Contributions are welcome!
To contribute:

1. Fork the repository
2. Create a new branch (`git checkout -b feature-name`)
3. Commit your changes (`git commit -m 'Add feature'`)
4. Push your branch (`git push origin feature-name`)
5. Open a Pull Request

## 📄 License

Developed by **Achraf MALKI**. All rights reserved.
