package com.ipc2.convenience_stores.data;

import com.ipc2.convenience_stores.models.Product;
import com.ipc2.convenience_stores.models.shipments.Shipment;
import com.ipc2.convenience_stores.models.shipments.ShipmentState;
import com.ipc2.convenience_stores.models.users.UserType;
import lombok.AllArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ShipmentDB {

    private Connection connectionDB;

    public void createShipment(Shipment shipment){
        String query = shipment.getId() == 0
                ? "INSERT INTO shipment(store_id, w_user_id, requisition_id, state, dispatched_date_time, received_date_time, cost) VALUES (?, ?, ?, ?, ?, ?, ?)"
                : "INSERT INTO shipment(store_id, w_user_id, requisition_id, state, dispatched_date_time, received_date_time, cost, shipment_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (var preparedStatement = connectionDB.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, shipment.getStore().getId());
            preparedStatement.setInt(2, shipment.getWarehouseUser().getId());
            preparedStatement.setInt(3, shipment.getRequisition().getId());
            preparedStatement.setString(4, shipment.getState().getNameDB());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(shipment.getDispatchedDateTime()));
            if(shipment.getReceivedDateTime() == null) {
                preparedStatement.setNull(6, Types.NULL);
            } else { preparedStatement.setTimestamp(6, Timestamp.valueOf(shipment.getReceivedDateTime()));}
            preparedStatement.setDouble(7, shipment.getCost());
            if (shipment.getId() != 0 ){
                preparedStatement.setInt(8, shipment.getId());
            }
            preparedStatement.executeUpdate();
            var resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                int shipmentId = resultSet.getInt(1);
                for (Product product : shipment.getProducts()) {
                    createShipmentItem(shipmentId, product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createShipmentItem(int shipmentId, Product product){
        String query ="INSERT INTO shipment_item(shipment_id, product_id, amount, unit_cost, total_cost) VALUES (?, ?, ?, ?, ?)";
        try (var preparedStatement = connectionDB.prepareStatement(query)) {
            preparedStatement.setInt(1, shipmentId);
            preparedStatement.setInt(2, product.getId());
            preparedStatement.setInt(3, product.getStock());
            preparedStatement.setDouble(4, product.getCost());
            preparedStatement.setDouble(5, (product.getCost() * product.getStock()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Product> getShipmentItems(int shipmentId){
        List<Product> items = new ArrayList<>();
        String query = "SELECT s.product_id, s.amount, s.unit_cost, p.name " +
                "FROM shipment_item s INNER JOIN product p on s.product_id=p.product_id " +
                "WHERE s.shipment_id = ?";
        try (var preparedStatement = connectionDB.prepareStatement(query)){
            preparedStatement.setInt(1, shipmentId);
            try (var resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    var productId = resultSet.getInt("product_id");
                    var name = resultSet.getString("name");
                    var stock = resultSet.getInt("amount");
                    var cost = resultSet.getDouble("unit_cost");
                    items.add(new Product(productId, name, stock, cost, 0.0));
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return items;
    }

    public Shipment getShipment(int shipmentId){
        Shipment shipment = null;
        String query = "SELECT store_id, w_user_id, requisition_id, state, dispatched_date_time, received_date_time, cost " +
                "FROM shipment WHERE shipment_id = ?";
        try (var preparedStatement = connectionDB.prepareStatement(query)){
            preparedStatement.setInt(1, shipmentId);
            try (var resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    var store = new StoreDB(connectionDB).getStore(resultSet.getInt("store_id"));
                    var whUser = new UserDB(connectionDB).getUser(resultSet.getInt("w_user_id"), UserType.WAREHOUSE_USER);
                    var requisition = new RequisitionDB(connectionDB).getRequisition(resultSet.getInt("requisition_id"));
                    var state = getState(resultSet.getString("state"));
                    var dispatchedDateTime = resultSet.getTimestamp("dispatched_date_time") != null
                            ? resultSet.getTimestamp("dispatched_date_time").toLocalDateTime(): null;
                    var receivedDateTime = resultSet.getTimestamp("received_date_time") != null
                            ? resultSet.getTimestamp("received_date_time").toLocalDateTime() : null;
                    var cost = resultSet.getDouble("cost");
                    List<Product> products = getShipmentItems(shipmentId);
                    if (store.isPresent() && whUser.isPresent()){
                        shipment = new Shipment(shipmentId, store.get(), whUser.get(), requisition, state, dispatchedDateTime, receivedDateTime, cost, products);
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return shipment;
    }

    private ShipmentState getState(String value){
        for (ShipmentState state : ShipmentState.values()) {
            if (state.getNameDB().equals(value)){
                return state;
            }
        }
        return null;
    }

}
