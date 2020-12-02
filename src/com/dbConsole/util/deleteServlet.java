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


public class deleteServlet
    extends HttpServlet {

  /* delUser: delete selected users
   */

  private void delUser(HttpServletRequest req, Connection conn,
                       PreparedStatement stmt, PreparedStatement stmt1,
                       PreparedStatement stmt2, String ids[]) throws
      SQLException, Exception {

    try {
// create a prepared statement to delete users
      stmt = conn.prepareStatement("DELETE from USER_ROLES  where user_name = (SELECT user_name from  USERS where id  =(?))");
      stmt1 = conn.prepareStatement("DELETE from DB_TYPE where user_name = (SELECT user_name from  USERS where id  =(?))");
      stmt2 = conn.prepareStatement("DELETE from USERS where id  =(?)");

      for (int i = 0; i < ids.length; i++) {
//for every user
        stmt.clearParameters();
        stmt1.clearParameters();
        stmt2.clearParameters();
        stmt.setInt(1, Integer.parseInt(ids[i]));
        stmt1.setInt(1, Integer.parseInt(ids[i]));
        stmt2.setInt(1, Integer.parseInt(ids[i]));

        stmt.executeUpdate();
        stmt1.executeUpdate();
        stmt2.executeUpdate();
      }

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

  /* delDB
   */

  private void delDb(HttpServletRequest req, Connection conn,
                     PreparedStatement stmt, String ids[]) throws
      SQLException, Exception {

    try {


      System.out.println("del db " +req.getRemoteUser());

// create a prepared statement to delete
      stmt = conn.prepareStatement(
          "DELETE from DB_TYPE  where user_name = (?)and DB_ALIAS = (?)");

      for (int i = 0; i < ids.length; i++) {
//for every alias
        stmt.clearParameters();
        stmt.setString(1, req.getRemoteUser());

//System.out.println(" 1 " +req.getRemoteUser());
        System.out.println(" 2 " +ids[i]);

        stmt.setString(2, ids[i]);

        stmt.executeUpdate();
  //      System.out.println("del db eseguito");
      }
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

  /* delClass:
   */

  private void delClass(HttpServletRequest req, Connection conn,
                        PreparedStatement stmt, PreparedStatement stmt1,
                        String ids[]) throws
      SQLException, Exception {

    try {
// create a prepared statement to delete
      stmt = conn.prepareStatement("DELETE from DB_TYPE where CLASS=(?)");
      stmt1 = conn.prepareStatement("DELETE from CLASS_TYPE  where CLASS =(?)");
      for (int i = 0; i < ids.length; i++) {
        stmt.clearParameters();
        stmt1.clearParameters();
        stmt.setString(1, ids[i]);
        stmt1.setString(1, ids[i]);
        stmt.setString(1, ids[i]);
        stmt1.setString(1, ids[i]);
        stmt.executeUpdate();
        stmt1.executeUpdate();
      }

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
  //  ResultSet result = null;
    PreparedStatement stmt = null;
    PreparedStatement stmt1 = null;
    PreparedStatement stmt2 = null;
    HttpSession session = req.getSession();
    String arr = req.getParameter("hiddDel");
   // System.out.println("INIZIO DELETE SERVLET");
   // System.out.println("DELETE arr  " + arr);
   try {
      Context cntx = new InitialContext();
      DataSource ds = (DataSource) cntx.lookup("java:FirebirdDS");
      conn = ds.getConnection();
      conn.setAutoCommit(false);
      String ids[] = (String[]) session.getAttribute("id");
      if (arr.equals("usr")) {
        delUser(req, conn, stmt, stmt1, stmt2, ids);
      }
      if (arr.equals("cls")) {
        delClass(req, conn, stmt, stmt1, ids);
      }
      if (arr.equals("db")) {
        delDb(req, conn, stmt, ids);
      }
      // close the connection
      conn.commit();
      conn.close();
    //  System.out.println("FINE DELETE SERVLET");
    }
    catch (SQLException e) {
      System.out.println(" SQL Error occurred " + e);

      throw new ServletException(" SQL Error occurred " + e);
    }
    catch (NamingException e) {
      System.out.println(" Naming Error occurred " + e);
      //   throw new UnavailableException("Naming error occured " + e);
      throw new ServletException("Naming error occured " + e);
    }
    catch (Exception e) {
      System.out.println("Error occurred " + e);
      throw new ServletException("Generic error occured " + e);
    }
    session.setAttribute("arrived", "deleteServlet");
    // now redirect to the welcome servlet, this will force a log in with their new username and password
    res.sendRedirect("../welcome");
  }
}
