package com.ipc2.convenience_stores.data;

import com.ipc2.convenience_stores.models.Product;
import com.ipc2.convenience_stores.models.incidences.Incidence;
import com.ipc2.convenience_stores.models.incidences.IncidenceReason;
import lombok.AllArgsConstructor;

import java.sql.*;

@AllArgsConstructor
public class IncidenceDB {

    private Connection connectionDB;

    public void createIncidence(Incidence incidence){
        String query = incidence.getId() == 0
                ? "INSERT INTO incidence(store_id, st_user_id, shipment_id, state, date_time, solution) VALUES (?, ?, ?, ?, ?, ?)"
                : "INSERT INTO incidence(store_id,st_user_id, shipment_id, state, date_time, solution, incidence_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (var preparedStatement = connectionDB.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, incidence.getStore().getId());
            preparedStatement.setInt(2, incidence.getStoreUser().getId());
            preparedStatement.setInt(3, incidence.getShipment().getId());
            preparedStatement.setString(4, incidence.getState().getNameDB());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(incidence.getDateTime()));
            preparedStatement.setString(6, (incidence.getSolution() == null)
                    ? " " : incidence.getSolution());
            if (incidence.getId() != 0 ){
                preparedStatement.setInt(7, incidence.getId());
            }
            preparedStatement.executeUpdate();
            var resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                int incidenceId = resultSet.getInt(1);
                for (Product product : incidence.getProducts()) {
                    createIncidenceItem(incidenceId, product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createIncidenceItem(int incidenceId, Product product){
        String query ="INSERT INTO incidence_item(incidence_id, product_id, amount, reason) VALUES (?, ?, ?, ?)";
        try (var preparedStatement = connectionDB.prepareStatement(query)) {
            preparedStatement.setInt(1, incidenceId);
            preparedStatement.setInt(2, product.getId());
            preparedStatement.setInt(3, product.getStock());
            preparedStatement.setString(4, ((IncidenceReason) product.getReason()).getNameDB());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
