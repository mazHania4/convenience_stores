package com.ipc2.convenience_stores.data;

import com.ipc2.convenience_stores.models.*;
import com.ipc2.convenience_stores.models.stores.Store;
import lombok.AllArgsConstructor;
import java.sql.Connection;
import java.sql.SQLException;

@AllArgsConstructor
public class CatalogDB {

    private Connection connectionDB;

    public Catalog getCatalog(Store store){
        Catalog catalog = new Catalog(store);
        String query = "SELECT c.product_id, c.stock, c.cost, c.price, p.name FROM catalog_item c INNER JOIN product p" +
                " on c.product_id=p.product_id WHERE store_id = ?";
        try (var preparedStatement = connectionDB.prepareStatement(query)){
            preparedStatement.setInt(1, store.getId());
            try (var resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    var productId = resultSet.getInt("product_id");
                    var name = resultSet.getString("name");
                    var stock = resultSet.getInt("stock");
                    var cost = resultSet.getDouble("cost");
                    var price = resultSet.getDouble("price");
                    catalog.addProduct(new Product(productId, name, stock, cost, price));
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return catalog;
    }

    public void addProductToCatalog(Store store, Product product, int stock){
        String query = "INSERT INTO catalog_item (store_id, product_id, stock, cost, price) VALUES(?, ?, ?, ?, ?)";
        try (var preparedStatement = connectionDB.prepareStatement(query)) {
            preparedStatement.setInt(1, store.getId());
            preparedStatement.setInt(2, product.getId());
            preparedStatement.setInt(3, stock);
            preparedStatement.setDouble(4, product.getCost());
            preparedStatement.setDouble(5, product.getPrice());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProductFromCatalog(Store store, Product product){
        String query = "DELETE FROM catalog_item WHERE store_id = ? AND product_id = ?";
        try (var preparedStatement = connectionDB.prepareStatement(query)) {
            preparedStatement.setInt(1, store.getId());
            preparedStatement.setInt(2, product.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProductOfCatalog(Store store, Product product){
        String query = "UPDATE catalog_item SET stock = ?, cost = ?, price = ?  WHERE store_id = ? AND product_id = ?";
        try (var preparedStatement = connectionDB.prepareStatement(query)) {
            preparedStatement.setInt(1, product.getStock());
            preparedStatement.setDouble(2, product.getCost());
            preparedStatement.setDouble(3, product.getPrice());
            preparedStatement.setInt(4, store.getId());
            preparedStatement.setInt(5, product.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
