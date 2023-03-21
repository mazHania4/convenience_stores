package com.ipc2.convenience_stores.web;

import java.io.*;
import com.ipc2.convenience_stores.data.ConnectionDB;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/init-servlet")
public class InitServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ConnectionDB connectionDB = new ConnectionDB();
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(3600);
        session.setAttribute("connection",  connectionDB.getConnection() );

        if(connectionDB.isDBstarted()){
            response.sendRedirect("login.jsp");
        }else{
            response.sendRedirect("loadEntry.jsp");
        }


    }
}