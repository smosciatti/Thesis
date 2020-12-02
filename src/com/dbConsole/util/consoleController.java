package com.dbConsole.util;

/**
 * <p>Title: dbConsole</p>
 * <p>Description: A global Database interface</p>
 * <p>Copyright: Copyright (c) 2003-2004</p>
 * <p>Company: </p>
 * @author Simone Mosciatti
 * @version 1.0
 */
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.*;
import javax.naming.*;
import java.sql.*;

public class consoleController
    extends HttpServlet {

  public void doGet(HttpServletRequest req, HttpServletResponse res) throws
      ServletException, IOException {
    doPost(req, res);
  }

  /*
   Make a url jdbc compliant aka //server:port/dbUrl
   @param strDb   String     //server/dbUrl
   @param strPort String
   @return strUrl String
   */
  private String parseUrl(String strDb, String strPort) throws Exception {
    String strUrl = "";
    int i = 3; // not necessary parse inital slash
    while (i < strDb.length()) {
      if (strDb.charAt(i - 1) == '/') {
        break;
      }
      i++;
    }
    if (i != strDb.length()) {
      strUrl = strDb.substring(0, i - 1) + ":" + strPort + "/" +
          strDb.substring(i);
    }
    return strUrl;
  }

  /* logIn
   */
  private connectionDetail logIn(HttpServletRequest req, Connection conn) throws
      SQLException, Exception {

    String dbAlias = "";
    String url = "";
    String cls = "";
    String prefixUrl = "";
    String postfixUrl = "";
    String javaDriver = "";
    String userName = "";
    String userPsw = "";
    String port = "";

    ResultSet result = null;
    PreparedStatement stmt = null;
    try {
      userName = req.getParameter("txtUser").toString();
      userPsw = req.getParameter("txtPsw").toString();
      dbAlias = req.getParameter("mnuAlias").toString();
      stmt = conn.prepareStatement(
          "SELECT class, url FROM db_type WHERE db_alias = ? ");
      stmt.setString(1, dbAlias);
      result = stmt.executeQuery();
      result.next();
      cls = result.getString("class"); //   firebird
      postfixUrl = result.getString("url"); // //paperone/acme
      stmt = null;
      result = null;
      stmt = conn.prepareStatement(
          "SELECT java_driver, port, prefix_URL  FROM class_type WHERE class = ?");
      stmt.setString(1, cls);
      result = stmt.executeQuery();
      result.next();
      javaDriver = result.getString("java_driver"); // org.firebirdsql.jdbc.FBDriver
      port = result.getString("port"); // 3050
      prefixUrl = result.getString("prefix_URL"); // jdbc:firebirdsql:
//making URL
      url = prefixUrl + parseUrl(postfixUrl, port);

      return new connectionDetail(javaDriver, userName, userPsw, url);

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

    Connection conn = null;
    //  ResultSet result = null;
    //  PreparedStatement stmt = null;
    //  PreparedStatement stmt1 = null;
    String url = "";
    connectionDetail urlConn;
    String name = "";
    // String pass = "";
    RequestDispatcher rd;
    Profile profile;
    //System.out.println("*****************Inizio Controller");
    HttpSession session = req.getSession();
    ServletContext application = getServletContext();
    profile = (Profile) session.getAttribute("currentUser");
    String arr = req.getParameter("hiddParam");
   // System.out.println("controller arrivo: " + arr);
   // System.out.println("user " + req.getRemoteUser());
    if (session.getAttribute("currentUser") == null) {
      // create a new profile object, passing in the username
      try {
        //      System.out.println("creo user ");
        profile = new Profile(req.getRemoteUser());
      }
      catch (Exception e) {
        throw new ServletException(" An error occured ");
      }

      session.setAttribute("currentUser", profile);
      //   session.setAttribute("arrived", "");

    }

    try {

      name = (String) session.getAttribute("arrived");

      Context cntx = new InitialContext();

      DataSource ds = (DataSource) cntx.lookup("java:FirebirdDS");

      conn = ds.getConnection();

      conn.setAutoCommit(false);
      //  System.out.println("controller6: " + arr);
      //    if (session.getAttribute("arrived") == null) {
      if (arr != null) {
        if (arr.equals("logIn")) {
          urlConn = logIn(req, conn);
          conn.commit();
          // close the connection
          conn.close();
          session.setAttribute("className", urlConn.getClassName());
          session.setAttribute("userName", urlConn.getUserName());
          session.setAttribute("userPassword", urlConn.getUserPassword());
          session.setAttribute("url", urlConn.getUrl());
          url = "/../../dbConsoleClient/jsp/clientController";
          application.getContext("/dbConsoleClient").setAttribute("className",
              urlConn.getClassName());
          application.getContext("/dbConsoleClient").setAttribute("userName",
              urlConn.getUserName());
          application.getContext("/dbConsoleClient").setAttribute(
              "userPassword", urlConn.getUserPassword());
          application.getContext("/dbConsoleClient").setAttribute("url",
              urlConn.getUrl());
        }
        //      else {
        //    url = "/jsp/welcome.jsp";
        //    }
      }
      else {

        //if arrived from deleteServlet, it must show only welcome
        //     if (name.equals("deleteServlet")) {
        System.out.println(" NAME: " + name);
      }
      if (name != null) {
        url = "/jsp/welcome.jsp";
        session.setAttribute("arrived", null);
      }

      System.out.println(" URL: " + url);
      if (url == "") {
        //  System.out.println("controllerprofile: " + profile.getRole());
        if (profile.getRole().equalsIgnoreCase("admin")) {
          url = "/jsp/admin.html";
        }
        else {
          url = "/jsp/user.html";
        }

     //   System.out.println("urlFinale: " + url);
      }
      // everything set up, so forward to the home page
      rd = application.getRequestDispatcher(url);
      if (rd == null) {
        rd = application.getContext("/dbConsoleClient").getRequestDispatcher(
            "/jsp/clientController");

      }
      //System.out.println("Fine Controller url " + url);
      rd.forward(req, res);
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
  }
}
