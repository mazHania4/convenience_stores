package com.ipc2.convenience_stores.data;

import com.ipc2.convenience_stores.models.stores.*;
import com.ipc2.convenience_stores.models.users.*;
import lombok.AllArgsConstructor;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class StoreDB {

    private Connection connectionDB;

    public Optional<Store> getStore(int id){
        Store store = null;
        String query = "SELECT * FROM store WHERE store_id = ?";
        try (var preparedStatement = connectionDB.prepareStatement(query)){
            preparedStatement.setInt(1, id);
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    var address = resultSet.getString("address");
                    var warehouseUserID = resultSet.getInt("w_user_id");
                    StoreType type = resultSet.getString("type").equals("Supervised") ?
                            StoreType.SUPERVISED : StoreType.NOT_SUPERVISED;
                    Optional<User> user = new UserDB(this.connectionDB).getUser(warehouseUserID, UserType.WAREHOUSE_USER);
                    if (user.isPresent()) {
                        store = new Store(id, user.get(), address, type);
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.ofNullable(store);
    }


    public void createStore(Store store){
        String query = "INSERT INTO store(w_user_id, address, type) VALUES (?, ?, ?)";
        try (var preparedStatement = connectionDB.prepareStatement(query)) {
            preparedStatement.setInt(1, store.getWarehouseUser().getId());
            preparedStatement.setString(2, store.getAddress());
            preparedStatement.setString(3, store.getType().getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStore(Store store){
        String query = "UPDATE store SET w_user_id = ?, address = ?, type = ? WHERE store_id = ?";
        try (var preparedStatement = connectionDB.prepareStatement(query)) {
            preparedStatement.setInt(1, store.getWarehouseUser().getId());
            preparedStatement.setString(2, store.getAddress());
            preparedStatement.setString(3, store.getType().getName());
            preparedStatement.setInt(4, store.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStore(int id){
        String query = "DELETE FROM store WHERE store_id = ?";
        try (var preparedStatement = connectionDB.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
