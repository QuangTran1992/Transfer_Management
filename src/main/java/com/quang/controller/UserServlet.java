package com.quang.controller;

import com.quang.DAO.UserDAO;
import com.quang.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
@WebServlet(name = "UserServlet", urlPatterns = "/users")
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDAO userDAO;

    public void init() {
        userDAO = new UserDAO();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        try {
            switch (action) {
                case "create":
                    insertUser(request, response);
                    break;
                case "edit":
                    updateUser(request, response);
                    break;
                case "transfer":
                    tranfer(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }



    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }

        try {
            switch (action) {
                case "create":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteUser(request, response);
                    break;
                case "transfer":
                    showFormTransfer(request, response);
                    break;
                default:
                    listUser(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void showFormTransfer(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        User existingUser = userDAO.selectUser(id);

        request.setAttribute("user", existingUser);

        List<User> users = userDAO.selectAllUsersNonExistCurrentUser(id);
        request.setAttribute("users",users);

        RequestDispatcher dispatcher = request.getRequestDispatcher("user/transfer.jsp");
        dispatcher.forward(request, response);
    }
    private void tranfer(HttpServletRequest request, HttpServletResponse response) throws IOException,ServletException,SQLException{
        int id = Integer.parseInt(request.getParameter("id"));
        int receiverId = Integer.parseInt(request.getParameter("receiverId"));
        Float amount = Float.parseFloat(request.getParameter("amount"));

        User userexting = userDAO.selectUser(id);
        User receiver = userDAO.selectUser(receiverId);

        if(userexting.getSalary() > amount ){
            userexting.setSalary(userexting.getSalary() - amount);
            receiver.setSalary(receiver.getSalary() + amount);
            List<User> listUser = userDAO.selectAllUsers();
            String message = "transfer successfully";
            request.setAttribute("message",message);
            request.setAttribute("users",listUser);

            userDAO.transferBalance(userexting);
            userDAO.transferBalance(receiver);

//            RequestDispatcher dispatcher = request.getRequestDispatcher("user/list.jsp");
//            dispatcher.forward(request , response);
            listUser(request, response);
        }else
            System.out.println("balance not enough");




        userDAO.transferBalance(userexting);
        userDAO.transferBalance(receiver);
        listUser(request, response);
    }

    private void listUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<User> listUser = userDAO.selectAllUsers();
        request.setAttribute("listUser", listUser);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/user/list.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/create.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        User existingUser = userDAO.selectUser(id);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/edit.jsp");
        request.setAttribute("user", existingUser);
        dispatcher.forward(request, response);

    }

    private void insertUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String country = request.getParameter("country");
        Float salary = Float.parseFloat(request.getParameter("salary")) ;
        User newUser = new User(name, email, country,salary);
        userDAO.insertUser(newUser);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/create.jsp");
        dispatcher.forward(request, response);
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String country = request.getParameter("country");
        Float salary = Float.parseFloat(request.getParameter("salary")) ;

        User user = new User(id, name, email, country,salary);
        userDAO.updateUser(user);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/edit.jsp");
        dispatcher.forward(request, response);
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        userDAO.deleteUser(id);
        List<User> listUser = userDAO.selectAllUsers();
        request.setAttribute("listUser", listUser);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/list.jsp");
        dispatcher.forward(request, response);
    }

}
