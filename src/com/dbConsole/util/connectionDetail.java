package com.dbConsole.util;

/**
 * <p>Title: dbConsole</p>
 * <p>Description: A global Database interface</p>
 * <p>Copyright: Copyright (c) 2003-2004</p>
 * <p>Company: </p>
 * @author Simone Mosciatti
 * @version 1.0
 */

public class connectionDetail {

  private String userName;
  private String userPassword;
  private String className;
  private String url;

  /*******************************************
   * connectionDetail
   * ******************************************/
  public connectionDetail() {
  }

  /*******************************************
   * connectionDetail
   *@param cls String     java class
   *@param userN String   user name
   *@param userP String   user password
   *@param url String     connection url
   * ******************************************/
  public connectionDetail(String cls, String userN, String userP, String url) {
    this.userName = userN;
    this.userPassword = userP;
    this.className = cls;
    this.url = url;
  }

  /*******************************************
   * getUserName:
   * @return name String
   ******************************************/
  public String getUserName() {
    return userName;
  }

  /*******************************************
   * setUserName:
   *@param us String
   ******************************************/
  public void setUserName(String us) {
    userName = us;
  }

  /*******************************************
   * getUserPassword:
   * @return name String
   ******************************************/
  public String getUserPassword() {
    return userPassword;
  }

  /*******************************************
   * setUserPassword:
   *@param up String
   ******************************************/
  public void setUserPassword(String up) {
    userPassword = up;
  }

  /*******************************************
   * getClassName:
   * @return name String
   ******************************************/
  public String getClassName() {
    return className;
  }

  /*******************************************
   * setClassName:
   *@param cn String
   ******************************************/
  public void setClassName(String cn) {
    className = cn;
  }

  /*******************************************
   * getUrl:
   * @return name String
   ******************************************/
  public String getUrl() {
    return url;
  }

  /*******************************************
   * setUrl:
   *@param url String
   ******************************************/
  public void setUrl(String url) {
    url = url;
  }
}
