package com.ipc2.convenience_stores.data;

import com.ipc2.convenience_stores.models.stores.Store;
import com.ipc2.convenience_stores.models.users.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class UserDB {

    private Connection connectionDB;
    private int lastGeneratedId;

    public Optional<User> getUser(String username, UserType type){
        User user = null;
        String query = "SELECT * FROM " + type.getName() + " WHERE username = ?";
        try (var preparedStatement = connectionDB.prepareStatement(query)){
            preparedStatement.setString(1, username);
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    var id = resultSet.getInt("user_id");
                    var name = resultSet.getString("name");
                    var email = resultSet.getString("email");
                    var password = resultSet.getString("password");
                    if (type.equals(UserType.STORE_USER)) {
                        var storeID = resultSet.getInt("store_id");
                        Optional<Store> store = new StoreDB(this.connectionDB).getStore(storeID);
                        if (store.isPresent()){user = new User(id, type, name, username, email, password, store.get());}
                    } else {user = new User(id, type, name, username, email, password, null);}
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.ofNullable(user);
    }

    public Optional<User> getUser(int id, UserType type){
        User user = null;
        String query = "SELECT * FROM " + type.getName() + " WHERE user_id = ?";
        try (var preparedStatement = connectionDB.prepareStatement(query)){
            preparedStatement.setInt(1, id);
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    var username = resultSet.getString("username");
                    var name = resultSet.getString("name");
                    var email = resultSet.getString("email");
                    var password = resultSet.getString("password");
                    if (type.equals(UserType.STORE_USER)) {
                        var storeID = resultSet.getInt("store_id");
                        Optional<Store> store = new StoreDB(this.connectionDB).getStore(storeID);
                        if (store.isPresent()) {
                            user = new User(id, type, name, username, email, password, store.get());
                        }
                    } else {
                        user = new User(id, type, name, username, email, password, null);
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.ofNullable(user);
    }

    public void createUser(User user){
        boolean hasId = user.getId() != 0;
        boolean hasStore = user.getType().equals(UserType.STORE_USER);
        String idSection1 = hasId ? ", user_id" : "";
        String idSection2 = hasId ? ", ?" : "";
        String storeSection1 = hasStore ? ", store_id" : "";
        String storeSection2 = hasStore ? ", ?" : "";
        String query = "INSERT INTO " + user.getType().getName()
                + "(name, username, email, password" + idSection1 + storeSection1
                + ") VALUES(?, ?, ?, ?" + idSection2 + storeSection2 + ")";

        try (var preparedStatement = connectionDB.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, (user.getEmail().equals(""))
                                                            ? null : user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            if (hasId){
                preparedStatement.setInt(5, user.getId());
                if (hasStore){
                    preparedStatement.setInt(6, user.getStore().getId());
                }
            }else {
                if (hasStore){
                preparedStatement.setInt(5, user.getStore().getId());
                }
            }
            preparedStatement.executeUpdate();
            var resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) lastGeneratedId = resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getUsers(UserType type){
        String query = "SELECT * FROM " + type.getName();
        List<User> users = new ArrayList<>();
        try (var preparedStatement = connectionDB.prepareStatement(query)) {
            try (var resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    var id = resultSet.getInt("user_id");
                    var username = resultSet.getString("username");
                    var name = resultSet.getString("name");
                    var email = resultSet.getString("email");
                    var password = resultSet.getString("password");
                    User user = null;
                    if (type.equals(UserType.STORE_USER)) {
                        var storeID = resultSet.getInt("store_id");
                        Optional<Store> store = new StoreDB(this.connectionDB).getStore(storeID);
                        if (store.isPresent()) {
                            user = new User(id, type, name, username, email, password, store.get());
                        }
                    } else {
                        user = new User(id, type, name, username, email, password, null);
                    }
                    users.add(user);
                }
            }
        }catch (SQLException e) {
            System.out.println("Error al consultar: " + e);
        }
        return users;
    }

     public String[][] getWhUsersStores(){
         String query = "SELECT s.address, s.store_id, u.user_id, u.username, u.name, u.email " +
                 "FROM store s RIGHT JOIN warehouse_user u on s.w_user_id=u.user_id ORDER BY u.user_id";
         List<String[]> usersStores = new ArrayList<>();
         try (var preparedStatement = connectionDB.prepareStatement(query);
              var resultSet = preparedStatement.executeQuery()) {
                 while (resultSet.next()) {
                     var address = resultSet.getString("address");
                     var storeId = String.valueOf(resultSet.getInt("store_id"));
                     var userId = String.valueOf(resultSet.getInt("user_id"));
                     var username = resultSet.getString("username");
                     var name = resultSet.getString("name");
                     var email = resultSet.getString("email");
                     String[] userStore = {address, storeId, userId, username, name, email};
                     usersStores.add(userStore);
                 }
         }catch (SQLException e) {
             System.out.println("Error al consultar: " + e);
         }
         String[][] stringUsersStores = new String[usersStores.size()][];
         int i = 0;
         for (String [] userStore : usersStores) {
             stringUsersStores[i++] = userStore;
         }
         return stringUsersStores;
     }

    public void updateUser(User user){
        String extra = user.getType().equals(UserType.STORE_USER) ? ", store_id = ? " : " ";
        String query = "UPDATE " + user.getType().getName() + " SET name = ?, username = ?, email = ?, password = ?"
                + extra + "WHERE user_id = ?";
        try (var preparedStatement = connectionDB.prepareStatement(query)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            if (user.getType().equals(UserType.STORE_USER)){
                preparedStatement.setInt(5, user.getStore().getId());
                preparedStatement.setInt(6, user.getId());
            }else{
                preparedStatement.setInt(5, user.getId());
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int id, UserType type){
        String query = "DELETE FROM " + type.getName() + " WHERE user_id = ?";
        try (var preparedStatement = connectionDB.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public UserDB(Connection connectionDB) {
        this.connectionDB = connectionDB;
    }

    public int getLastGeneratedId() {
        return lastGeneratedId;
    }
}
