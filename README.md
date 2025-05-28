# Cashaura Banking Application

**Cashaura** is a modern desktop banking application built with Java and Swing. It allows users to manage accounts, perform transactions, and handle beneficiaries through a clean and intuitive interface.

## 🚀 Features

* 🔐 User authentication (login/register)
* 🏦 Account management (create, view, close)
* 💸 Transaction operations (deposit, withdraw, transfer)
* 👥 Beneficiary management
* 💰 Savings account support
* 🖥️ Modern, minimalistic Swing-based UI

## 📦 Prerequisites

* **Java 11** or higher
* **MySQL 8.0+**

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

3. **Import the database schema**

   ```bash
   mysql -u root -p cashaura < database/schema.sql
   ```

4. **Configure database connection**
   Edit `src/resources/database.properties` with your MySQL username and password.

5. **Build the project**

   ```bash
   mvn clean package
   ```

6. **Run the application**

   ```bash
   java -jar target/cashaura-1.0-SNAPSHOT-jar-with-dependencies.jar
   ```

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

* Built with **Maven**
* Uses **HikariCP** for efficient connection pooling
* Follows **MVC architecture**
* Includes unit testing with **JUnit** and **Mockito**

## ✅ Running Tests

To run all unit tests:

```bash
mvn test
```

## 🤝 Contributing

Contributions are welcome!
To contribute:

1. Fork the repository
2. Create a new feature branch (`git checkout -b feature-name`)
3. Commit your changes (`git commit -m 'Add feature'`)
4. Push to the branch (`git push origin feature-name`)
5. Create a Pull Request

## 📄 License

Developed by **Achraf MALKI**. All rights reserved.
