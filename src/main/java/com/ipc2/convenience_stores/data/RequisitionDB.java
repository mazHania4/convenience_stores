package com.ipc2.convenience_stores.data;

import com.ipc2.convenience_stores.models.Product;
import com.ipc2.convenience_stores.models.requisitions.*;
import com.ipc2.convenience_stores.models.users.UserType;
import lombok.AllArgsConstructor;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class RequisitionDB {

    private Connection connectionDB;

    public void createRequisition(Requisition requisition){
        String query = requisition.getId() == 0
                ? "INSERT INTO requisition(store_id, st_user_id, state, date_time, cost) VALUES (?, ?, ?, ?, ?)"
                : "INSERT INTO requisition(store_id, st_user_id, state, date_time, cost, requisition_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (var preparedStatement = connectionDB.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, requisition.getStore().getId());
            preparedStatement.setInt(2, requisition.getStoreUser().getId());
            preparedStatement.setString(3, requisition.getState().getNameDB());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(requisition.getDateTime()));
            preparedStatement.setDouble(5, requisition.getCost());
            if (requisition.getId() != 0 ){
                preparedStatement.setInt(6, requisition.getId());
            }
            preparedStatement.executeUpdate();
            var resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                int requisitionId = resultSet.getInt(1);
                for (Product product : requisition.getProducts()) {
                    createRequisitionItem(requisitionId, product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createRequisitionItem(int requisitionId, Product product){
        String query ="INSERT INTO requisition_item(requisition_id, product_id, amount, unit_cost, total_cost) VALUES (?, ?, ?, ?, ?)";
        try (var preparedStatement = connectionDB.prepareStatement(query)) {
            preparedStatement.setInt(1, requisitionId);
            preparedStatement.setInt(2, product.getId());
            preparedStatement.setInt(3, product.getStock());
            preparedStatement.setDouble(4, product.getCost());
            preparedStatement.setDouble(5, (product.getCost() * product.getStock()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Product> getRequisitionItems(int requisitionId){
        List<Product> items = new ArrayList<>();
        String query = "SELECT r.product_id, r.amount, r.unit_cost, p.name " +
                "FROM requisition_item r INNER JOIN product p on r.product_id=p.product_id " +
                "WHERE r.requisition_id = ?";
        try (var preparedStatement = connectionDB.prepareStatement(query)){
            preparedStatement.setInt(1, requisitionId);
            try (var resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    var productId = resultSet.getInt("product_id");
                    var name = resultSet.getString("name");
                    var stock = resultSet.getInt("amount");
                    var cost = resultSet.getDouble("unit_cost");
                    items.add(new Product(productId, name, stock, cost, 0));
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return items;
    }

    public Requisition getRequisition(int requisitionId){
        Requisition requisition = null;
        String query = "SELECT store_id, st_user_id, state, date_time, cost " +
                "FROM requisition WHERE requisition_id = ?";
        try (var preparedStatement = connectionDB.prepareStatement(query)){
            preparedStatement.setInt(1, requisitionId);
            try (var resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    var store = new StoreDB(connectionDB).getStore(resultSet.getInt("store_id"));
                    var stUser = new UserDB(connectionDB).getUser(resultSet.getInt("st_user_id"), UserType.STORE_USER);
                    var state = getState(resultSet.getString("state"));
                    var dateTime = resultSet.getTimestamp("date_time").toLocalDateTime();
                    var cost = resultSet.getDouble("cost");
                    List<Product> products = getRequisitionItems(requisitionId);
                    if (store.isPresent() && stUser.isPresent()){
                        requisition = new Requisition(requisitionId, store.get(), stUser.get(), state, cost, dateTime, products);
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return requisition;
    }

    private RequisitionState getState(String value){
        for (RequisitionState state : RequisitionState.values()) {
            if (state.getNameDB().equals(value)){
                return state;
            }
        }
        return null;
    }


}
