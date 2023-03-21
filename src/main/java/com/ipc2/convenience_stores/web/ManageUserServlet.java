package com.ipc2.convenience_stores.web;

import com.ipc2.convenience_stores.data.StoreDB;
import com.ipc2.convenience_stores.data.UserDB;
import com.ipc2.convenience_stores.models.users.User;
import com.ipc2.convenience_stores.models.users.UserType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;

@WebServlet("/manage-user-servlet")
public class ManageUserServlet extends HttpServlet {

    private UserDB userDB;
    private StoreDB storeDB;
    private HttpSession session;
    private UserType userTypeManaged;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        session = req.getSession();
        Connection connection = (Connection) session.getAttribute("connection");
        userDB = new UserDB(connection);
        storeDB = new StoreDB(connection);
        if (req.getParameter("create") != null && req.getParameter("create").equals("true")) {
            createUser(req);
        } else if (req.getParameter("update") != null && req.getParameter("update").equals("true")){
            updateUser(req);
        } else if (req.getParameter("updatewhs") != null && req.getParameter("updatewhs").equals("true")){
            updateDesignatedStoresWhUser(req);
        }
        req.getRequestDispatcher(getDirection()).forward(req, resp);
    }

    private void updateDesignatedStoresWhUser(HttpServletRequest req) {
        int userId = Integer.parseInt(req.getParameter("whUserId"));
        int storeId = Integer.parseInt(req.getParameter("whStoreId"));
        userTypeManaged = getUserType(req.getParameter("whUserType"));
        var storeOptional = storeDB.getStore(storeId);
        if (storeOptional.isPresent()){
            var userOptional = userDB.getUser(userId, userTypeManaged);
            if (userOptional.isPresent()){
                var store = storeOptional.get();
                var user = userOptional.get();
                store.setWarehouseUser(user);
                storeDB.updateStore(store);
                successUpdate(req);
            }else {
                req.setAttribute("error", "Parece haber un problema con el id del usuario");
                req.removeAttribute("success");
            }
        } else {
            req.setAttribute("error", "Parece haber un problema con el id de la tienda");
            req.removeAttribute("success");
        }
    }

    private void createUser(HttpServletRequest req) {
        String name = req.getParameter("newName");
        String username = req.getParameter("newUsername");
        String email = req.getParameter("newEmail");
        String password = req.getParameter("newPassword");
        userTypeManaged = getUserType(req.getParameter("newUserType"));
        if (userTypeManaged.equals(UserType.SUPERVISOR_USER)) {
                userDB.createUser(new User(userTypeManaged, name, username, email, password, null));
        } else {
            String storeId = req.getParameter("newStoreId");
            var store = storeDB.getStore(Integer.parseInt(storeId));
            if (store.isPresent()) {
                    System.out.println("entró");
                if (userTypeManaged.equals(UserType.STORE_USER)){
                    userDB.createUser(new User(userTypeManaged, name, username, email, password, store.get()));
                } else if (userTypeManaged.equals(UserType.WAREHOUSE_USER)) {
                    userDB.createUser(new User(userTypeManaged, name, username, email, password, null));
                    var usertmp = userDB.getUser(userDB.getLastGeneratedId(), userTypeManaged);
                    if (usertmp.isPresent()){
                        var updatedStore = store.get();
                        updatedStore.setWarehouseUser(usertmp.get());
                        storeDB.updateStore(updatedStore);
                    }
                }
            } else {
                req.setAttribute("error", "Parece haber un problema con el id de la tienda");
                req.removeAttribute("success");
                return;
            }
        }
        var newUser = userDB.getUser(userDB.getLastGeneratedId(), userTypeManaged);
        if (newUser.isPresent()) {
            successUpdate(req);
        } else {
            req.setAttribute("error", "Revisa los dato ingresados, puede ser que el nombre de usuario no esté disponible");
            req.removeAttribute("success");
        }
    }
    private void successUpdate(HttpServletRequest req){
        req.setAttribute("success", "Se guardó la información de forma exitosa");
        req.removeAttribute("error");
        session.setAttribute("storeUsers", userDB.getUsers(UserType.STORE_USER));
        session.setAttribute("warehouseUsers", userDB.getUsers(UserType.WAREHOUSE_USER));
        session.setAttribute("supervisorUsers", userDB.getUsers(UserType.SUPERVISOR_USER));
        session.setAttribute("whUsersStores", userDB.getWhUsersStores());
    }

    private String getDirection(){
        switch (userTypeManaged) {
            case STORE_USER:
                return "adminModule/editStoreUser.jsp";
            case WAREHOUSE_USER:
                return "adminModule/editWarehouseUser.jsp";
            case SUPERVISOR_USER:
                return "adminModule/editSupervisorUser.jsp";
        }
        return null;
    }

    private void updateUser(HttpServletRequest req){
        int userId = Integer.parseInt(req.getParameter("userId"));
        String name = req.getParameter("name");
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        userTypeManaged = getUserType(req.getParameter("userType"));
        var userTmp = userDB.getUser(userId, userTypeManaged);
        if (userTmp.isPresent()) {
            User user = userTmp.get();
            user.setName(name);
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            if (userTypeManaged.equals(UserType.STORE_USER)) {
                int storeId = Integer.parseInt(req.getParameter("storeId"));
                var store = storeDB.getStore(storeId);
                if (store.isPresent()) {
                    user.setStore(store.get());
                } else {
                    req.setAttribute("error", "Parece haber un problema con el id de la tienda");
                    req.removeAttribute("success");
                }
            }
            userDB.updateUser(user);
            var updatedUser = userDB.getUser(userDB.getLastGeneratedId(), userTypeManaged);
            if (updatedUser.isPresent()) {
                successUpdate(req);
            } else {
                req.setAttribute("error", "Revisa los dato ingresados, puede ser que el nombre de usuario no esté disponible");
                req.removeAttribute("success");
            }
        } else {
                req.setAttribute("error", "Parece haber un problema con el id del usuario");
                req.removeAttribute("success");
        }
    }

    private UserType getUserType(String typeString){
        for (UserType type : UserType.values()) {
            if (type.getName().equals(typeString))
                return type;
        }
        System.out.println("tipo nulo" + typeString);
        return null;
    }


}
