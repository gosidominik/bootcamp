package repository;

import entity.StoreItem;

import java.sql.*;

public class InMemoryStoreRepository implements StoreItemRepository{

    private static String jdbcURL = "jdbc:h2:~/test";
    private static String jdbcUsername = "sa";
    private static String jdbcPassword = "";
    private final String createTableSQL = """
            CREATE TABLE IF NOT EXISTS INVENTORY(
                       id IDENTITY NOT NULL PRIMARY KEY,
                       product_name varchar(30) NOT NULL,
                       quantity int default 0
            );""";

    public InMemoryStoreRepository() {
        executeSql(createTableSQL);
    }

    @Override
    public StoreItem loadItem(String productName) {
        return selectProductRecordByName(productName);
    }

    @Override
    public void saveItem(StoreItem item) {
        if(isProductAvailable(item)) {
            insertProductRecord(item);
        } else {
            updateProductRecord(item);
        }
    }
private void executeSql(String sql) {
        try
            (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            printSQLException(e);
        } ;

}
    private static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private static void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
    private void insertProductRecord(StoreItem item) {
        String query = "INSERT INTO INVENTORY" +
                " (product_name, quantity) VALUES " +
                " (?, ?);";
        try(Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, item.getProductName());
            preparedStatement.setInt(2, item.getQuantity());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    private void updateProductRecord(StoreItem item) {
        String query = "update INVENTORY set quantity = ? where product_name = ?;";
        try(Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, item.getQuantity());
            preparedStatement.setString(2, item.getProductName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    private StoreItem selectProductRecordByName(String productName) {
        String query = "SELECT product_name, quantity FROM INVENTORY WHERE product_name = ?";
        try(Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, productName);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                int itemQuantity = rs.getInt("quantity");
                return new StoreItem(productName, itemQuantity);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return null;
    }

    private boolean isProductAvailable(StoreItem item) {
        return selectProductRecordByName(item.getProductName()) == null;
    }
}
