/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rmj.appdriver.agent;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

import org.rmj.appdriver.constants.UserLockState;
import org.rmj.appdriver.constants.UserRight;
import org.rmj.appdriver.constants.UserState;
import org.rmj.appdriver.constants.UserType;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.MiscUtil;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agent.dialog.diaSearchForm;
import org.rmj.appdriver.agent.dialog.diaSystemApproval;
import org.rmj.appdriver.agent.dialog.diaUserConfirmation;
import org.rmj.appdriver.agent.dialog.diaChangePW;
import org.rmj.appdriver.agent.dialog.diaLogin;

/**
 *
 * @author kalyptus
 */
public class GRiderX extends GRider{
   public GRiderX(){
      super();
   }

   public GRiderX(String fsProductID){
      super(fsProductID);
   }
   
   /*
    * ===================================
    * method: logUser
    *           => logs possible user through the GUI
    * params: none
    *           => application to be use during the login process will be selected
    *              in the login screen
    * returns: boolean
    * ===================================
    */
   public boolean logUser(){
      int lnCtr = 1;
      diaLogin dialog = new diaLogin(null, true);
      Statement loStmt = null;
      ResultSet loRs = null;
      String lsProdctID = null;
      String lsUserIDxx = null;

      Connection loCon = null;
      String lsUserName;
      String lsPassWord;

      try {

         if (poCon == null) {
            loCon = doConnect();
         } else {
            loCon = poCon;
         }
         
         if (loCon == null) {
            JOptionPane.showMessageDialog(null, "No connection to the database detected!", "Login", JOptionPane.OK_OPTION + JOptionPane.ERROR_MESSAGE);
            return false;
         }

         //Load the items here
         String lsSQLApp = "SELECT" +
                                 "  a.sProdctID" +
                                 ", a.sProdctNm" +
                                 ", a.sApplName" +
                                 ", b.sApplPath" +
                          " FROM xxxSysObject a" +
                                  ", xxxSysApplication b" +
                          " WHERE a.sProdctID = b.sProdctID" +
                            " AND b.sClientID = " + SQLUtil.toSQL(this.getClientID()) +
                            " AND a.sProdctID <> " + SQLUtil.toSQL(xeDriverID);

         System.out.println(lsSQLApp);
         Statement loStmtApp = loCon.createStatement();
         ResultSet loRSApp = loStmtApp.executeQuery(lsSQLApp);
         LinkedList laIDx = new LinkedList();
         LinkedList laApp = new LinkedList();

         while(loRSApp.next()){
                 laIDx.add(loRSApp.getString("sProdctID"));
                 laApp.add(loRSApp.getString("sProdctNm"));
         }

         String[] lsApplName = new String[laIDx.size()];
         String[] lsApplCode = new String[laIDx.size()];

         for(int ln=0; ln<laIDx.size();ln++){
            lsApplName[ln] = (String) laApp.get(ln);
            lsApplCode[ln] = (String) laIDx.get(ln);
         }

         dialog.setApps(lsApplName, lsApplCode);

         //Show login form
         do {
            //show the modal dialog form
            dialog.setModal(true);
            dialog.setVisible(true);
            //Does user pressed cancel
            if (!dialog.isOkey()) {
               JOptionPane.showMessageDialog(null, "User cancelled the login process!", "Login", JOptionPane.OK_OPTION + JOptionPane.INFORMATION_MESSAGE);

               MiscUtil.close(loRs);
               MiscUtil.close(loStmt);
               if (poCon == null)
                  MiscUtil.close(loCon);

                  dialog = null;
                  return false;
            }

            lsUserName = this.Encrypt(dialog.getUserName());
            lsPassWord = this.Encrypt(dialog.getPassword());

            String lsSQL = "SELECT *" +
                           " FROM xxxSysUser" +
                           " WHERE sLogNamex = " + SQLUtil.toSQL(lsUserName) +
                             " AND sPassword = " + SQLUtil.toSQL(lsPassWord);

            loStmt = loCon.createStatement();
            loRs = loStmt.executeQuery(lsSQL);

            //System.out.println(lsSQL);

            if (!loRs.next()) {
               JOptionPane.showMessageDialog(null, "Invalid username and/or Password!", "Login", JOptionPane.OK_OPTION + JOptionPane.INFORMATION_MESSAGE);
               lnCtr++;
            } else {
               break;
            }
         } while (lnCtr <= 3);


         lsProdctID = dialog.getSelectedApp();
         lsUserIDxx = loRs.getString("sUserIDxx");

      } catch (SQLException ex) {
         Logger.getLogger(GRiderX.class.getName()).log(Level.SEVERE, null, ex);
         ex.printStackTrace();

         MiscUtil.close(loRs);
         MiscUtil.close(loStmt);
         if (poCon == null)
            MiscUtil.close(loCon);

         return false;
      }
      finally{
         MiscUtil.close(loRs);
         MiscUtil.close(loStmt);
         if (poCon == null)
            MiscUtil.close(loCon);

         dialog = null;
      }

      return this.logUser(lsProdctID, lsUserIDxx);
   }

   /*
    * ===================================
    * method: logUser
    *           => logs possible user through the GUI
    * params: sProductID
    *           => application to be use during the login process
    * returns: boolean
    * ===================================
   */
   public boolean logUser(String sProdctID){
      int lnCtr = 1;
      diaLogin dialog = new diaLogin(null, true);
      Statement loStmt = null;
      ResultSet loRs = null;
      String lsProdctID = null;
      String lsUserIDxx = null;

      Connection loCon = null;
      String lsUserName;
      String lsPassWord;

      if(sProdctID.trim().isEmpty())
         return logUser();

      lsProdctID = sProdctID;

      try {
         if (poCon == null) {
            loCon = doConnect();
         } else {
            loCon = poCon;
         }

         if (loCon == null) {
            JOptionPane.showMessageDialog(null, "No connection to the database detected!", "Login", JOptionPane.OK_OPTION + JOptionPane.ERROR_MESSAGE);
            return false;
         }

         //Load the items here
         String lsSQLApp = "SELECT" +
                                 "  a.sProdctID" +
                                 ", a.sProdctNm" +
                                 ", a.sApplName" +
                                 ", b.sApplPath" +
                          " FROM xxxSysObject a" +
                                  ", xxxSysApplication b" +
                          " WHERE a.sProdctID = b.sProdctID" +
                            " AND b.sClientID = " + SQLUtil.toSQL(this.getClientID()) +
                            " AND a.sProdctID = " + SQLUtil.toSQL(lsProdctID);
         Statement loStmtApp = loCon.createStatement();
         ResultSet loRSApp = loStmtApp.executeQuery(lsSQLApp);
         LinkedList laIDx = new LinkedList();
         LinkedList laApp = new LinkedList();
         while(loRSApp.next()){
            laIDx.add(loRSApp.getString("sProdctID"));
            laApp.add(loRSApp.getString("sProdctNm"));
         }

         String[] lsApplName = new String[laIDx.size()];
         String[] lsApplCode = new String[laIDx.size()];

         for(int ln=0; ln<laIDx.size();ln++){
            lsApplName[ln] = (String) laApp.get(ln);
            lsApplCode[ln] = (String) laIDx.get(ln);
         }

         dialog.setApps(lsApplName, lsApplCode);

         //Show login form
         do {
            //show the modal dialog form
            dialog.setModal(true);
            dialog.setVisible(true);
            //Does user pressed cancel
            if (!dialog.isOkey()) {
               JOptionPane.showMessageDialog(null, "User cancelled the login process!", "Login", JOptionPane.OK_OPTION + JOptionPane.INFORMATION_MESSAGE);

               MiscUtil.close(loRs);
               MiscUtil.close(loStmt);
               if (poCon == null)
                  MiscUtil.close(loCon);

               dialog = null;
               return false;
            }

            lsUserName = this.Encrypt(dialog.getUserName());
            lsPassWord = this.Encrypt(dialog.getPassword());

            String lsSQL = "SELECT *" +
                        " FROM xxxSysUser" +
                        " WHERE sLogNamex = " + SQLUtil.toSQL(lsUserName) +
                          " AND sPassword = " + SQLUtil.toSQL(lsPassWord);

            loStmt = loCon.createStatement();
            loRs = loStmt.executeQuery(lsSQL);
            System.out.println(lsSQL);

            if (!loRs.next()) {
               JOptionPane.showMessageDialog(null, "Invalid username and/or Password!", "Login", JOptionPane.OK_OPTION + JOptionPane.INFORMATION_MESSAGE);
               lnCtr++;
            } else {
               break;
            }
         } while (lnCtr <= 3);

         lsProdctID = dialog.getSelectedApp();
         lsUserIDxx = loRs.getString("sUserIDxx");
      } catch (SQLException ex) {
         Logger.getLogger(GRiderX.class.getName()).log(Level.SEVERE, null, ex);
         ex.printStackTrace();

         MiscUtil.close(loRs);
         MiscUtil.close(loStmt);
         if (poCon == null)
            MiscUtil.close(loCon);

         return false;
      }
      finally{
         MiscUtil.close(loRs);
         MiscUtil.close(loStmt);
         if (poCon == null)
                 MiscUtil.close(loCon);

         dialog = null;
      }

      return this.logUser(lsProdctID, lsUserIDxx);
   }
   /*
    * ===================================
    * method: unlockUser
    *           => unlocks locked user - possibly caused by runtime error
    *              or improper shutdown.
    * params: sUserID
    *           => the ID of the user to be unlock
    * returns: boolean
    * ===================================
    */
   @Override
   public boolean unlockUser(String sUserID){
      int lnCtr = 1;

      do{
         //show form
         //if cancelled
         //    break;
         //else
         //  extract username and password

         //load user

         //if record is null
         //  show message
         //  increment counter
         //else
         //  break;
      }while(lnCtr <= 3);

      return true;
   }

   /*
    * ===================================
    * method: getSystemApproval
    *          => get the approval of a user with a right for the process
    * params: Rights
    *          => the level of rights that can approve the process
    * returns: Empty
    *          => Not approve.
    *         sUserIDxx�sUserName�nUserLevl
    *          => Information of user that makes the approval
    * ===================================
    */
   public String getSystemApproval(int Rights){
      //Make sure that System Engineering Group has the right to approve any transaction.
      if((Rights & UserRight.ENGINEER) == 0)
         Rights += UserRight.ENGINEER;

      //Make sure the SEG Head has the right to approve any transaction.
      if((Rights & UserRight.SYSMASTER) == 0)
         Rights += UserRight.SYSMASTER;

      int lnCtr = 1;
      
      diaSystemApproval frm = new diaSystemApproval(null, true);
      String user;
      String pass;
      ResultSet rs=null;
        
      try {
         do{	
            //show form
            frm.setVisible(true);

            //check user response
            if(frm.isOkey()) {
               user = frm.getUserName();
               pass = frm.getPassword();
            }
            else {
               break;
            }
        	  
            String lsSQL = "SELECT *" +
                          " FROM xxxSysUser" +
                          " WHERE sLogNamex = " + SQLUtil.toSQL(user) +
                            " AND sPassword = " + SQLUtil.toSQL(pass);    		

            rs = this.executeQuery(lsSQL);
            //check for result
            lnCtr++;
            if(rs == null || !rs.next()) {
               MsgBox.showOk("Please verify your username/password...");  
               continue;
            }
            else {
               //user gives a correct user/password info
               break;
            }
         }while(lnCtr <= 3);

         //check if user has right for the procedure...  
         if((rs.getInt("nUserLevl") & Rights) == 0) {
            MsgBox.showOk("User has no right for this procedure...");  
            return "";
         }
         else if(rs.getString("cUserStat").equalsIgnoreCase(UserState.SUSPENDED)) {
            MsgBox.showOk("User is currently Suspended...");  
            return "";
         }
         else if(rs.getString("cUserStat").equalsIgnoreCase(UserLockState.LOCKED)) {
            MsgBox.showOk("User is currently LOCK...");  
            return "";
         }
         else if(rs.getString("cUserType").equalsIgnoreCase(UserType.LOCAL)){
            if(!rs.getString("sProdctID").equalsIgnoreCase(this.getProductID())) {
              MsgBox.showOk("User is not a Member of this Application...");  
              return "";
            }
         }
            
         return rs.getString("sUserIDxx");
			
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         setErrMsg(e.getMessage());
         return null;
      }
   }

   /*
    * ===================================
    * method: getSystemConfirmation
    *          => get the confirmation of the current users identity
    * params: Rights
    *          => the level of rights that can approve the process
    * returns: Empty
    *          => Not approve.
    *         sUserIDxx�sUserName�nUserLevl
    *           => Information of user that makes the approval
    * ===================================  
    */
   public boolean getSystemConfirmation(){
      int lnCtr = 1;
      
      diaUserConfirmation frm = new diaUserConfirmation(null, true);
      String user;
      String pass;
      ResultSet rs=null;

      try {
         do{	
            //show form
            frm.setVisible(true);
   
            //check user response
            if(frm.isOkey()) {
               user = frm.getUserName();
               pass = frm.getPassword();
            }
            else {
               break;
            }
     	  
            String lsSQL = "SELECT *" +
                        " FROM xxxSysUser" +
                            " WHERE sLogNamex = " + SQLUtil.toSQL(user) +
                              " AND sPassword = " + SQLUtil.toSQL(pass);    		

            rs = this.executeQuery(lsSQL);
            //check for result
            lnCtr++;
            if(rs == null || !rs.next()) {
               MsgBox.showOk("Please verify your username/password...");  
               continue;
            }
            else {
               //user gives a correct user/password info
               break;
            }
         }while(lnCtr <= 3);
        
         if(rs.getString("sUserIDxx").equalsIgnoreCase(this.getUserID()))
            return true;
         else 
            return false;
        
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         setErrMsg(e.getMessage());
         return false;
      }
   }

   /*
    * ===================================
    * method: setSystemDate
    *         => sets the new system date
    * params: None
    * returns: true
    *         => sucess.
    * ===================================
    */
   public boolean setSystemDate(){
      String lsApproval = "";
      Date ldOldSysDate = getSysDate();
      Date ldNewSysDate;

      //display the form

      //If user presses cancel
      // Inform that setting of system date was cancelled then exit the process

      //get the new System Date
      ldNewSysDate = SQLUtil.toDate("", "yyyy-MM-dd");
      if(ldNewSysDate == null){
         //Inform that the new system date is invalid.
         return false;
      }
      //if new sysdate = current system date get the approval of user with
      else if(ldNewSysDate.equals(ldOldSysDate))
      {
         //Inform that the new system date is the current system date
         return false;
      }
		//if new sysdate > current system date get the approval of user with
      else if(ldNewSysDate.after(ldNewSysDate)){
         if((getUserLevel() & UserRight.ENCODER) > 0)
            lsApproval = getSystemApproval(UserRight.SUPERVISOR + 
                                          UserRight.MANAGER +
                                          UserRight.SYSADMIN);
         if(lsApproval.isEmpty()){
            //Inform that system approval was not initiated
            return false;
         }
         else{
            //if current user has a supervisory level or above just ask for  
            //confirmation of his identity
            if(!getSystemConfirmation()){
               //Inform that system confirmation was not initiated
               return false;
            }
         }
      }

      //if new sysdate > current system date get the approval of user with
      //administrator right
      else{
         //Inform that the user is setting the system date to a date less than
         //the current system date and needs an approval from the system administrator
         //Normally the system administor needs the reason why the user wants
         //to backdate.

         //If the user is a system administrator/engineer/sysmaster 
         //confirm his identity
         if((getUserLevel() & UserRight.SYSADMIN) > 0 ||
            (getUserLevel() & UserRight.ENGINEER) > 0 ||
            (getUserLevel() & UserRight.SYSMASTER) > 0 ){
            if(!getSystemConfirmation()){
               //Inform that system confirmation was not initiated
               return false;
            }
         }
         //If the user is a not a system administrator/engineer/sysmaster
         //get an approval from the systems administrator
         else{
            lsApproval = getSystemApproval(UserRight.SYSADMIN);
            if(lsApproval.isEmpty()){
               //Inform that system approval was not initiated
               return false;
            }
         }

         String laUserInfo[] = lsApproval.split("�");
         boolean lbIsSet = setSystemDate(laUserInfo[0], ldNewSysDate);
         if(!lbIsSet){
            //Show information extracted from the getErrMessage();
            return false;
         }
      }

      //Show information that setting of system date was successfully executed.
      return true;
   }

   public boolean changePassword(){
      int lnCtr = 1;
      
      diaChangePW frm = new diaChangePW(null, true);
      String oldx;
      String newx="";
      ResultSet rs=null;
      boolean correct = false;
      try {
         do{	
            //show form
            frm.setVisible(true);

            //check user response
            if(frm.isOkey()) {
               oldx = this.Encrypt(frm.getOldPassword());
               newx = this.Encrypt(frm.getNewPassword());
            }
            else {
               break;
            }

            String lsSQL = "SELECT *" +
                          " FROM xxxSysUser" +
                          " WHERE sUserIDxx = " + SQLUtil.toSQL(this.getUserID());    		

            rs = this.executeQuery(lsSQL);

            //check for result
            lnCtr++;
            if(rs == null || !rs.next()) {
               MsgBox.showOk("Error in accessing the database detected...");  
               return false;
            }
            else {
               if(rs.getString("sPassword").compareToIgnoreCase(oldx) != 0) {
                  MsgBox.showOk("Please make sure to enter the correct current password...");  
                  continue;
               }

               correct = true;
               break;
            }
         }while(lnCtr <= 3);
        
         if(correct) {
            String sql = "UPDATE xxxSysUser" + 
                        " SET sPassword = " + SQLUtil.toSQL(newx) + 
                        " WHERE sUserIDxx = " + SQLUtil.toSQL(this.getUserID());
            if(this.executeQuery(sql, "xxxSysUser", this.getBranchCode(), "") == 0) {
               MsgBox.showOk("Error saving the new password...");  
               return false;
            }
            return true;
         }
         else 
            return false;
        
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         setErrMsg(e.getMessage());
         return false;
      }
   }
	
   public String KwikBrowse( 
         ResultSet source, 
         String field, 
         String description,
         int sort
      ){
      System.out.println("KwikBrowse before");		
      diaSearchForm frm = new diaSearchForm(null, true);
      frm.setDataSource(source);
      frm.setFieldHeader(description);
      frm.setFieldName(field);
      frm.setLocationRelativeTo(null);
      frm.setSort(sort);
      frm.setModal(true);
      frm.setVisible(true);
      System.out.println("KwikBrowse after");		

      String lsReturn = frm.getSelected();
      frm = null;
      return lsReturn;
   }
	
   private static final String xeDriverID = "GRider";
}

//Usual information needed
//Product ID
//Product Name
//Client ID
//Branch Code
//User ID
//User Name
//User Right

//++++++++++++++++++++++++++++++++++++++++++
//kalyptus - 2017.08.15 11:59am
//SELECT a.sModuleID, IFNULL(a.nUserRght, 127) nUserRght, b.sUserIDxx, c.dDateFrom, c.dDateThru, c.sModuleID xModuleID 
//FROM xxxSysModule a
//   LEFT JOIN xxxSysUserWL b ON a.sModuleID = b.sModuleID AND b.sUserIDxx = 'M001100016'
//   LEFT JOIN xxxSysUserWL c ON a.sModuleID = c.sModuleID
//WHERE a.sModuleDs = 'XMGCOffPoints.closeTransaction' 
//ORDER BY xModuleID DESC, sUserIDxx DESC 
//LIMIT 1;
//
//AllowUser(fsModuleDs, fsUserID) AS BOOLEAN
//++++++++++++++++++++++++++++++++++++++++++
