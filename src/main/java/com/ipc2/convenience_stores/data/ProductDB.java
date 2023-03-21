package com.ipc2.convenience_stores.data;

import com.ipc2.convenience_stores.models.Product;
import lombok.AllArgsConstructor;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class ProductDB {

    private Connection connectionDB;

    public Optional<Product> getProductGeneralCatalog(String name){
        Product product = null;
        String query = "SELECT * FROM product WHERE name = ?";
        try (var preparedStatement = connectionDB.prepareStatement(query)){
            preparedStatement.setString(1, name);
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    var id = resultSet.getInt("product_id");
                    var stock = resultSet.getInt("stock");
                    var cost = resultSet.getDouble("cost");
                    var price = resultSet.getDouble("price");
                    product = new Product(id, name, stock, cost, price);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.ofNullable(product);
    }


    public Optional<Product> getProductGeneralCatalog(int id){
        Product product = null;
        String query = "SELECT * FROM product WHERE product_id = ?";
        try (var preparedStatement = connectionDB.prepareStatement(query)){
            preparedStatement.setInt(1, id);
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    var name = resultSet.getString("name");
                    var stock = resultSet.getInt("stock");
                    var cost = resultSet.getDouble("cost");
                    var price = resultSet.getDouble("price");
                    product = new Product(id, name, stock, cost, price);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.ofNullable(product);
    }

    public void createProductGeneralCatalog(Product product){
        String query = product.getId() == 0 ? "INSERT INTO product(name, stock, cost, price) VALUES(?, ?, ?, ?)"
                : "INSERT INTO product(name, stock, cost, price, product_id) VALUES(?, ?, ?, ?, ?)";
        try (var preparedStatement = connectionDB.prepareStatement(query)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setInt(2, product.getStock());
            preparedStatement.setDouble(3, product.getCost());
            preparedStatement.setDouble(4, product.getPrice());
            if (product.getId() != 0){preparedStatement.setInt(5, product.getId());}
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProductGeneralCatalog(Product product){
        String query = "UPDATE product SET name = ?, stock = ?, cost = ?, price = ?  WHERE product_id = ?";
        try (var preparedStatement = connectionDB.prepareStatement(query)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setInt(2, product.getStock());
            preparedStatement.setDouble(3, product.getCost());
            preparedStatement.setDouble(4, product.getPrice());
            preparedStatement.setInt(5, product.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProductGeneralCatalog(int id){
        String query = "DELETE FROM product WHERE product_id = ?";
        try (var preparedStatement = connectionDB.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
