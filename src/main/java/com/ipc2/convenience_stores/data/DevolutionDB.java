package com.ipc2.convenience_stores.data;

import com.ipc2.convenience_stores.models.Product;
import com.ipc2.convenience_stores.models.devolutions.Devolution;
import com.ipc2.convenience_stores.models.devolutions.DevolutionReason;
import lombok.AllArgsConstructor;

import java.sql.*;
@AllArgsConstructor
public class DevolutionDB {

    private Connection connectionDB;

    public void createDevolution(Devolution devolution){
        String query = devolution.getId() == 0
                ? "INSERT INTO devolution(store_id, st_user_id, shipment_id, state, date_time, cost) VALUES (?, ?, ?, ?, ?, ?)"
                : "INSERT INTO devolution(store_id,st_user_id, shipment_id, state, date_time, cost, devolution_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (var preparedStatement = connectionDB.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, devolution.getStore().getId());
            preparedStatement.setInt(2, devolution.getStoreUser().getId());
            preparedStatement.setInt(3, devolution.getShipment().getId());
            preparedStatement.setString(4, devolution.getState().getNameDB());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(devolution.getDateTime()));
            preparedStatement.setDouble(6, devolution.getCost());
            if (devolution.getId() != 0 ){
                preparedStatement.setInt(7, devolution.getId());
            }
            preparedStatement.executeUpdate();
            var resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                int devolutionId = resultSet.getInt(1);
                for (Product product : devolution.getProducts()) {
                    createDevolutionItem(devolutionId, product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createDevolutionItem(int devolutionId, Product product){
        String query ="INSERT INTO devolution_item(devolution_id, product_id, amount, unit_cost, total_cost, reason) VALUES (?, ?, ?, ?, ?, ?)";
        try (var preparedStatement = connectionDB.prepareStatement(query)) {
            preparedStatement.setInt(1, devolutionId);
            preparedStatement.setInt(2, product.getId());
            preparedStatement.setInt(3, product.getStock());
            preparedStatement.setDouble(4, product.getCost());
            preparedStatement.setDouble(5, (product.getCost() * product.getStock()));
            preparedStatement.setString(6, ((DevolutionReason) product.getReason()).getNameDB());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
