package com.dbConsole.util;

/**
 * <p>Title: dbConsole</p>
 * <p>Description: A global Database interface</p>
 * <p>Copyright: Copyright (c) 2003-2004</p>
 * <p>Company: </p>
 * @author Simone Mosciatti
 * @version 1.0
 */

import javax.sql.*;
import javax.naming.*;
import java.sql.*;

public class Profile {

  private String name;
  private String role;
  private String password;
  private String firstName;
  private String lastName;
  private String mail;
  private int id;

  /*******************************************
   * Profile
   *@param name String
   *@throws Exception
   * ******************************************/
  public Profile(String name) throws Exception {
    // System.out.println("Inizio profile");
    this.name = name;

    DataSource ds = null;
    Connection conn = null;
    ResultSet result = null;
    ResultSet result1 = null;
//    ResultSetMetaData rsmd = null;
    Statement stmt = null;
    try {
      Context cntx = new InitialContext();
      //    System.out.println("contesto ");
      if (cntx == null) {
        throw new RuntimeException("Init: Cannot get Initial Context");
      }

      ds = (DataSource) cntx.lookup("java:/FirebirdDS");
      if (ds != null) {

        conn = ds.getConnection();
        stmt = conn.createStatement();
        result = stmt.executeQuery(
            "SELECT * FROM user_roles where user_name = '" + name + "'");
        // move the cursor to the first row
        result.next();
        role = result.getString("ROLE_NAME");

        result1 = stmt.executeQuery(
            "SELECT * FROM users where user_name = '" + name + "'");
        result1.next();
        password = result1.getString("USER_PASS");
        firstName = result1.getString("FIRST_NAME");
        lastName = result1.getString("LAST_NAME");
        mail = result1.getString("E_MAIL");
        id = result1.getInt("ID");

        // close the connection
        result.close();
        result1.close();
        stmt.close();
        conn.close();
      }

      //     System.out.println("Fine profile");
    }
    catch (SQLException e) {
      System.out.println(" SQL Error occurred " + e);
      throw e;
    }
    catch (NamingException e) {
      System.out.println("Error occurred " + e);
      throw e;
    }
    catch (Exception e) {
      System.out.println("Error occurred " + e);
      throw e;
    }

  }

  /*******************************************
   * Profile:
   * @param id int
   * @throws Exception
   ******************************************/
  public Profile(int id) throws Exception {
    this.id = id;

    DataSource ds = null;
    Connection conn = null;
    ResultSet result = null;
    ResultSet result1 = null;
    ResultSetMetaData rsmd = null;
    Statement stmt = null;
    try {
      Context cntx = new InitialContext();
      //    System.out.println("contesto ");
      if (cntx == null) {
        throw new RuntimeException("Init: Cannot get Initial Context");
      }

      ds = (DataSource) cntx.lookup("java:/FirebirdDS");
      if (ds != null) {

        conn = ds.getConnection();
        stmt = conn.createStatement();

        result1 = stmt.executeQuery(
            "SELECT * FROM users where id = '" + id + "'");
        result1.next();
        password = result1.getString("USER_PASS");
        firstName = result1.getString("FIRST_NAME");
        lastName = result1.getString("LAST_NAME");
        mail = result1.getString("E_MAIL");
        name = result1.getString("USER_NAME");

        result = stmt.executeQuery(
            "SELECT * FROM user_roles where user_name = '" + name + "'");
        // move the cursor to the first row
        result.next();
        role = result.getString("ROLE_NAME");

        // close the connection
        result.close();
        result1.close();
        stmt.close();
        conn.close();
      }

      //     System.out.println("Fine profile");
    }
    catch (SQLException e) {
      System.out.println("Error occurred " + e);
      throw e;
    }
    catch (NamingException e) {
      System.out.println("Error occurred " + e);
      throw e;
    }
    catch (Exception e) {
      System.out.println("Error occurred " + e);
      throw e;

    }

  }

  /*******************************************
   * getName:
   * @return name String
   ******************************************/
  public String getName() {
    return name;
  }

  /*******************************************
   * setName:
   *@param s String
   ******************************************/
  public void setName(String s) {
    name = s;
  }

  /*******************************************
   * getRole:
   *@return role String
   ******************************************/
  public String getRole() {
    return role;
  }

  /*******************************************
   * setRole:
   *@param s String
   ******************************************/
  public void setRole(String s) {
    role = s;
  }

  /*******************************************
   * getPassword:
   * @return password String
   ******************************************/
  public String getPassword() {
    return password;
  }

  /*******************************************
   * setPassword:
   *@param s String
   ******************************************/
  public void setPassword(String s) {
    password = s;
  }

  /*******************************************
   * getFirstName:
   * @return firstName String
   ******************************************/
  public String getFirstName() {
    return firstName;
  }

  /*******************************************
   * setFirstName:
   *@param s String
   ******************************************/
  public void setFirstName(String s) {
    firstName = s;
  }

  /*******************************************
   * getLastName:
   * @return lastName String
   ******************************************/
  public String getLastName() {
    return lastName;
  }

  /*******************************************
   * setLastName:
   *@param s String
   ******************************************/
  public void setLastName(String s) {
    lastName = s;
  }

  /*******************************************
   * getMail:
   * @return mail
   ******************************************/
  public String getMail() {
    return mail;
  }

  /*******************************************
   * setMail:
   *@param s String
   ******************************************/
  public void setMail(String s) {
    mail = s;
  }

  /*******************************************
   * getId:
   * @return id
   ******************************************/
  public int getId() {
    return id;
  }

  /*******************************************
   * setId:
   *@param i int
   ******************************************/
  public void setId(int i) {
    id = i;
  }

}
