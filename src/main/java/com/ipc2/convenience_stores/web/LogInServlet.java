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

@WebServlet("/login-servlet")
public class LogInServlet extends HttpServlet {

    private UserDB userDB;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Connection connection = (Connection) session.getAttribute("connection");
        User user = (User) session.getAttribute("user");
        userDB = new UserDB(connection);
        if (user == null) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String userType = request.getParameter("userType");
            var newUser = userDB.getUser(username, getUserType(userType));
            if (newUser.isPresent() && newUser.get().validatePassword(password)) {
                session.setAttribute("user", newUser.get());
                response.sendRedirect("modules-servlet");
            } else {
                request.setAttribute("error", "Credenciales incorrectas");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } else {
                response.sendRedirect(getDirection(user.getType()));
        }
    }

    private String getDirection(UserType type){
        switch (type) {
            case STORE_USER:
                return "storeModule.jsp";
            case WAREHOUSE_USER:
                return"warehouseModule.jsp";
            case ADMIN_USER:
                return"adminModule/adminModule.jsp";
            case SUPERVISOR_USER:
                return"supervisorModule.jsp";
        }
        return null;
    }
    private UserType getUserType(String typeString){
        for (UserType type : UserType.values()) {
            if (type.getName().equals(typeString))
                return type;
        }
        return null;
    }

}
