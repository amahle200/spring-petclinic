import java.sql.*;

public class OnlineStoreDatabase {

    // Step 1: Define database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/OnlineStore";
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASS = "password"; // Replace with your MySQL password

    public static void main(String[] args) {
        try {
            // Step 2: Connect to the database
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();

            // Step 3: Create the tables
            createTables(stmt);

            // Step 4: Insert sample data
            insertSampleData(stmt);

            // Step 5: Query and display orders
            queryOrders(stmt);

            // Close the connection
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Step 3: Method to create the database tables
    private static void createTables(Statement stmt) throws SQLException {
        String createCustomersTable = "CREATE TABLE IF NOT EXISTS Customers (" +
                "CustomerID INT AUTO_INCREMENT PRIMARY KEY, " +
                "CustomerName VARCHAR(100) NOT NULL, " +
                "Email VARCHAR(100) NOT NULL UNIQUE, " +
                "Phone VARCHAR(15))";
        
        String createProductsTable = "CREATE TABLE IF NOT EXISTS Products (" +
                "ProductID INT AUTO_INCREMENT PRIMARY KEY, " +
                "ProductName VARCHAR(100) NOT NULL, " +
                "Price DECIMAL(10, 2) NOT NULL, " +
                "Stock INT NOT NULL)";
        
        String createOrdersTable = "CREATE TABLE IF NOT EXISTS Orders (" +
                "OrderID INT AUTO_INCREMENT PRIMARY KEY, " +
                "OrderDate DATE NOT NULL, " +
                "CustomerID INT, " +
                "FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID))";
        
        String createOrderDetailsTable = "CREATE TABLE IF NOT EXISTS OrderDetails (" +
                "OrderDetailsID INT AUTO_INCREMENT PRIMARY KEY, " +
                "OrderID INT, " +
                "ProductID INT, " +
                "Quantity INT NOT NULL, " +
                "FOREIGN KEY (OrderID) REFERENCES Orders(OrderID), " +
                "FOREIGN KEY (ProductID) REFERENCES Products(ProductID))";

        // Execute the SQL statements
        stmt.executeUpdate(createCustomersTable);
        stmt.executeUpdate(createProductsTable);
        stmt.executeUpdate(createOrdersTable);
        stmt.executeUpdate(createOrderDetailsTable);

        System.out.println("Tables created successfully.");
    }

    // Step 4: Method to insert sample data into the tables
    private static void insertSampleData(Statement stmt) throws SQLException {
        // Insert sample customers
        String insertCustomers = "INSERT INTO Customers (CustomerName, Email, Phone) VALUES " +
                "('John Doe', 'john.doe@example.com', '555-1234'), " +
                "('Jane Smith', 'jane.smith@example.com', '555-5678')";
        
        // Insert sample products
        String insertProducts = "INSERT INTO Products (ProductName, Price, Stock) VALUES " +
                "('Laptop', 12000.00, 10), " +
                "('Smartphone', 8000.00, 25), " +
                "('Headphones', 1500.00, 50)";
        
        // Insert a sample order for John Doe
        String insertOrder = "INSERT INTO Orders (OrderDate, CustomerID) VALUES (CURDATE(), 1)";

        // Insert order details for the sample order
        String insertOrderDetails = "INSERT INTO OrderDetails (OrderID, ProductID, Quantity) VALUES " +
                "(1, 1, 1), (1, 3, 2)";  // John Doe ordered 1 Laptop and 2 Headphones

        // Execute the SQL insert statements
        stmt.executeUpdate(insertCustomers);
        stmt.executeUpdate(insertProducts);
        stmt.executeUpdate(insertOrder);
        stmt.executeUpdate(insertOrderDetails);

        System.out.println("Sample data inserted successfully.");
    }

    // Step 5: Method to query and display order details
    private static void queryOrders(Statement stmt) throws SQLException {
        String query = "SELECT o.OrderID, c.CustomerName, p.ProductName, od.Quantity, o.OrderDate " +
                "FROM Orders o " +
                "JOIN Customers c ON o.CustomerID = c.CustomerID " +
                "JOIN OrderDetails od ON o.OrderID = od.OrderID " +
                "JOIN Products p ON od.ProductID = p.ProductID";

        ResultSet rs = stmt.executeQuery(query);

        // Display the results
        while (rs.next()) {
            int orderId = rs.getInt("OrderID");
            String customerName = rs.getString("CustomerName");
            String productName = rs.getString("ProductName");
            int quantity = rs.getInt("Quantity");
            Date orderDate = rs.getDate("OrderDate");

            System.out.printf("Order ID: %d, Customer: %s, Product: %s, Quantity: %d, Order Date: %s%n",
                    orderId, customerName, productName, quantity, orderDate);
        }

        rs.close();
    }
}
