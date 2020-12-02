package com.dbConsole.util;

/**
 * <p>Title: dbConsole</p>
 * <p>Description: A global Database interface</p>
 * <p>Copyright: Copyright (c) 2003-2004</p>
 * <p>Company: </p>
 * @author Simone Mosciatti
 * @version 2.0
 */

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.*;
import javax.naming.*;
import java.sql.*;
//import org.firebirdsql.jdbc.*;

public class RegistrationServlet
    extends HttpServlet {

  /* newUser: insert a new user
   */

  private void newUser(HttpServletRequest req, Connection conn,
                       PreparedStatement stmt, PreparedStatement stmt1) throws
      SQLException, Exception {
    // create a prepared statement to create a new user

    try {

      stmt = conn.prepareStatement("INSERT into users (user_name, user_pass,first_name,last_name,e_mail) values (?,?,?,?,?)");

      stmt.setString(1, req.getParameter("name"));

      stmt.setString(2, req.getParameter("password"));

      stmt.setString(3, req.getParameter("firstName"));

      stmt.setString(4, req.getParameter("lastName"));

      stmt.setString(5, req.getParameter("email"));

      stmt.executeUpdate();

      //now insert user's role
      stmt1 = conn.prepareStatement(
          "INSERT into user_roles (user_name, role_name) values (?,?)");

      stmt1.setString(1, req.getParameter("name"));

      stmt1.setString(2, req.getParameter("role"));

      stmt1.executeUpdate();
    }
    catch (SQLException e) {
      conn.rollback();
      throw e;
    }
    catch (Exception e) {
      conn.rollback();
      throw e;
    }
  }

  /* newClass: insert a new java driver class
   */

  private void newClass(HttpServletRequest req, Connection conn,
                        PreparedStatement stmt) throws
      SQLException, Exception {
    try {
      stmt = conn.prepareStatement(
          "INSERT into class_type (class, java_driver,port,prefix_url) values (?,?,?,?)");

      stmt.setString(1, req.getParameter("class_name"));

      stmt.setString(2, req.getParameter("java_driver"));

      stmt.setInt(3, Integer.parseInt(req.getParameter("std_port")));

      stmt.setString(4, req.getParameter("prefix_url"));

      stmt.executeUpdate();

    }
    catch (SQLException e) {
      conn.rollback();
      throw e;
    }
    catch (Exception e) {
      conn.rollback();
      throw e;
    }

  }

  /* newDb: insert a new db alias
   */
  private void newDb(HttpServletRequest req, Connection conn,
                     PreparedStatement stmt) throws
      SQLException, Exception {

    try {
      stmt = conn.prepareStatement(
          "INSERT into db_type (user_name,db_alias, class, url) values (?,?,?,?)");

      stmt.setString(1, req.getRemoteUser());

      stmt.setString(2, req.getParameter("db_alias"));

      stmt.setString(3, req.getParameter("class_name"));

      stmt.setString(4, req.getParameter("addr"));

      stmt.executeUpdate();

    }
    catch (SQLException e) {
      conn.rollback();
      throw e;
    }
    catch (Exception e) {
      conn.rollback();
      throw e;
    }
  }

  public void doPost(HttpServletRequest req, HttpServletResponse res) throws
      ServletException, IOException {
    // set up a connection
    Connection conn = null;
 //   ResultSet result = null;
    PreparedStatement stmt = null;
    PreparedStatement stmt1 = null;
    String arr = req.getParameter("hiddArrived");

    //   System.out.println("Inizio Registration Servlet");
    try {
      Context cntx = new InitialContext();
      DataSource ds = (DataSource) cntx.lookup("java:FirebirdDS");

      conn = ds.getConnection();
      conn.setAutoCommit(false);

      if (arr.equals("newUser")) {
        newUser(req, conn, stmt, stmt1);
      }
      if (arr.equals("newClass")) {
        newClass(req, conn, stmt);
      }
   //   System.out.println("prima di NEWDB");
      if (arr.equals("newDb")) {
        newDb(req, conn, stmt);
      }

      conn.commit();
      // close the connection
      conn.close();
      //    System.out.println("fine Registration Servlet");
    }
    catch (SQLException e) {
      System.out.println(" SQL Error occurred " + e);
      throw new ServletException(" SQL Error occurred " + e);
    }
    catch (NamingException e) {
      System.out.println(" Naming Error occurred " + e);
      throw new ServletException(" Naming error occured " + e);

    }
    catch (Exception e) {
      System.out.println("Error occurred " + e);
      throw new ServletException(" Generic error occured " + e);
    }
    req.getSession().setAttribute("arrived", "registerServlet");
    // now redirect to the welcome servlet, this will force a log in with their new username and password
    res.sendRedirect("welcome");
  }

}
