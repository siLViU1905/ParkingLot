package org.example.parkinglot.servlets;

import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.parkinglot.common.UserDto;
import org.example.parkinglot.ejb.CarsBean;
import org.example.parkinglot.ejb.UsersBean;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AddCar", value = "/AddCar")
public class AddCar extends HttpServlet {

    @Inject
    UsersBean usersBean;

    @Inject
    CarsBean carsBean;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        List<UserDto> users = usersBean.findAllUsers();
        request.setAttribute("users", users);
        request.getRequestDispatcher("/WEB-INF/pages/addCar.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        String licensePlate = request.getParameter("license_plate");
        String parkingSpot = request.getParameter("parking_spot");
        Long userID = Long.parseLong(request.getParameter("owner_id"));
        Long carID = Long.parseLong(request.getParameter("car_id"));

        carsBean.createCar(licensePlate, parkingSpot, userID);

        response.sendRedirect(request.getContextPath() + "/Cars");
    }
}