package com.ipc2.convenience_stores.web;

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

@WebServlet("/modules-servlet")
public class ModulesServlet extends HttpServlet {

    private UserDB userDB;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Connection connection = (Connection) session.getAttribute("connection");
        User user = (User) session.getAttribute("user");
        userDB = new UserDB(connection);
        session.setAttribute("storeUsers", userDB.getUsers(UserType.STORE_USER));
        session.setAttribute("warehouseUsers", userDB.getUsers(UserType.WAREHOUSE_USER));
        session.setAttribute("supervisorUsers", userDB.getUsers(UserType.SUPERVISOR_USER));
        session.setAttribute("whUsersStores", userDB.getWhUsersStores());
        resp.sendRedirect(getDirection(user.getType()));
    }


    private String getDirection(UserType type){
        switch (type) {
            case STORE_USER:
                return "storeModule/storeModule.jsp";
            case WAREHOUSE_USER:
                return "warehouseModule/warehouseModule.jsp";
            case ADMIN_USER:
                return "adminModule/adminModule.jsp";
            case SUPERVISOR_USER:
                return "supervisorModule/supervisorModule.jsp";
        }
        return null;
    }
}
