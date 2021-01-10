/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rmj.appdriver.agent;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author kalyptus
 */
public class MsgBox {
   public static int showYesNo(String theMessage){
      int result = JOptionPane.showConfirmDialog((Component)
              null, theMessage, "ALERT", JOptionPane.YES_NO_OPTION);
      return result;
   }

   public static int showYesNoCancel(String theMessage){
      int result = JOptionPane.showConfirmDialog((Component)
              null, theMessage, "ALERT", JOptionPane.YES_NO_CANCEL_OPTION);
      return result;
   }

   public static int showOkCancel(String theMessage){
      int result = JOptionPane.showConfirmDialog((Component)
              null, theMessage, "ALERT", JOptionPane.OK_CANCEL_OPTION);
      return result;
   }

   public static int showOk(String theMessage){
      int result = JOptionPane.showConfirmDialog((Component)
              null, theMessage, "ALERT", JOptionPane.DEFAULT_OPTION);
      return result;
   }

   public static final int RESP_WINDOW_CLOSED = -1;
   public static final int RESP_YES_OK = 0;
   public static final int RESP_NO = 1;
   public static final int RESP_CANCEL = 2;
}
