import java.sql.*;
import java.util.Scanner;

public class Product {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Connect to database");
        System.out.print("Enter product name: ");
        String proName = scanner.nextLine();
        System.out.print("Enter product price: ");
        Double price = scanner.nextDouble();
        System.out.print("Active (Enter 1 for true, 0 for false): ");
        int activeInput = scanner.nextInt();
        boolean active = (activeInput == 1);

        try {
            insertData(proName, price, active);
            showAllProduct();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            scanner.close();  // Close scanner at the end
        }
    }

    static Connection connectionToDb() throws ClassNotFoundException, SQLException {
        String dbUrl = "jdbc:postgresql://localhost:5432/product_dt";
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(dbUrl, "postgres", "052003");
        return connection;
    }

    static void insertData(String productName, Double price, Boolean active) throws SQLException, ClassNotFoundException {
        Connection con = connectionToDb();
        PreparedStatement preparedStatement =
                con.prepareStatement("INSERT INTO products(product_name, price, active) VALUES (?,?,?)");
        preparedStatement.setString(1, productName);
        preparedStatement.setDouble(2, price);
        preparedStatement.setBoolean(3, active);
        preparedStatement.executeUpdate();  // Use executeUpdate for INSERT statements
        con.close();  // Close connection after use
    }

    static public void showAllProduct() {
        try {
            Connection connection = connectionToDb();
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM products";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("product_id");
                String name = resultSet.getString("product_name");
                double price = resultSet.getDouble("price");
                boolean active = resultSet.getBoolean("active");
                System.out.println("Product ID: " + id);
                System.out.println("Product Name: " + name);
                System.out.println("Product Price_per_unit: $" + price);
                System.out.println("Active_for_sell: " + active);
                System.out.println("==========================================");
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

