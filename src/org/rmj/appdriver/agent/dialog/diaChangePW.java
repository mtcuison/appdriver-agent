package org.rmj.appdriver.agent.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.rmj.appdriver.agent.ImagePanelme;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class diaChangePW extends JDialog {
   private static final long serialVersionUID = -2250905726534506297L;
   private ImagePanelme loginBG;
   private JPasswordField newPassword;
   private JPasswordField oldPassword;
   private JPasswordField conPassword;

   private boolean pbIsOkey = false;

   /**
    * Create the dialog.
    */
   public diaChangePW(java.awt.Frame parent, boolean modal) {
      super(parent, modal);
      addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
                   oldPassword.setText("");
                   newPassword.setText("");
                   conPassword.setText("");
                   pbIsOkey = false;
         }
      });

      setResizable(false);
      setBounds(100, 100, 354, 228);
      getContentPane().setLayout(new BorderLayout());

      loginBG = new ImagePanelme(new ImageIcon("/images/system_approval_bg.jpg").getImage());
      loginBG.setBounds(0, 0, 350, 200);
      getContentPane().add(loginBG);
      loginBG.setLayout(null);
      
      JLabel lblOldPassword = new JLabel("Old Password");
      lblOldPassword.setForeground(Color.WHITE);
      lblOldPassword.setBounds(127, 86, 89, 14);
      loginBG.add(lblOldPassword);
      
      JLabel lblNewPassword = new JLabel("New Password");
      lblNewPassword.setForeground(Color.WHITE);
      lblNewPassword.setBounds(127, 108, 89, 14);
      loginBG.add(lblNewPassword);
      
      newPassword = new JPasswordField();
      newPassword.setBounds(220, 105, 118, 20);
      loginBG.add(newPassword);

      oldPassword = new JPasswordField();
      oldPassword.setBounds(220, 83, 118, 20);
      loginBG.add(oldPassword);
      
      conPassword = new JPasswordField();
      conPassword.setBounds(220, 127, 118, 20);
      loginBG.add(conPassword);
      
      JLabel lblConfirmPassword = new JLabel("Confirm Password");
      lblConfirmPassword.setForeground(Color.WHITE);
      lblConfirmPassword.setBounds(127, 130, 89, 14);
      loginBG.add(lblConfirmPassword);      
      
      JButton button = new JButton("OK");
      button.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            //make sure that username is filled-up
            if(oldPassword.getPassword().length == 0){
               JOptionPane.showMessageDialog(null, "Please fill-up the old password!", "Change Password", JOptionPane.OK_OPTION + JOptionPane.INFORMATION_MESSAGE);
               oldPassword.requestFocusInWindow();
               return;
            }

            //make sure that password is filled-up
            if(newPassword.getPassword().length == 0){
               JOptionPane.showMessageDialog(null, "Please fill-up the password!", "Change Password", JOptionPane.OK_OPTION + JOptionPane.INFORMATION_MESSAGE);
               newPassword.requestFocusInWindow();
               return;
            }

            //make sure that password is filled-up
            if(conPassword.getPassword().length == 0){
               JOptionPane.showMessageDialog(null, "Please confirm password!", "Change Password", JOptionPane.OK_OPTION + JOptionPane.INFORMATION_MESSAGE);
               conPassword.requestFocusInWindow();
               return;
            }

            String oldx = this.toString(oldPassword.getPassword());
            String newx = this.toString(newPassword.getPassword());
            String conx = this.toString(conPassword.getPassword());

            if(oldx.compareTo(newx) == 0) {
               JOptionPane.showMessageDialog(null, "Old password and new password are the same!", "Change Password", JOptionPane.OK_OPTION + JOptionPane.INFORMATION_MESSAGE);
               newPassword.requestFocusInWindow();
               return;
            }

            if(newx.compareTo(conx) != 0) {
               JOptionPane.showMessageDialog(null, "New password is different from confirmation!", "Change Password", JOptionPane.OK_OPTION + JOptionPane.INFORMATION_MESSAGE);
               newPassword.requestFocusInWindow();
               return;
            }

            pbIsOkey = true;
            setVisible(false);      		
         }

         private String toString(char chars[]){
            StringBuilder str = new StringBuilder();
            for(char x: chars){
               str.append(x);
            }
            return str.toString();
         }     
      });

      button.setBounds(154, 158, 89, 23);
      loginBG.add(button);
      
      JButton button_1 = new JButton("Cancel");
      button_1.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            oldPassword.setText("");
            newPassword.setText("");
            conPassword.setText("");
            pbIsOkey = false;
            setVisible(false);
         }
      });
      
      button_1.setBounds(249, 158, 89, 23);
      loginBG.add(button_1);
   }
	
   public boolean isOkey(){
      return pbIsOkey;
   }

   public String getOldPassword(){
      return toString(oldPassword.getPassword());
   }

   public String getNewPassword(){
      return toString(newPassword.getPassword());
   }	
   
   public String getConfirmation(){
      return toString(conPassword.getPassword());
   }	
   
   private String toString(char chars[]){
      StringBuilder str = new StringBuilder();
      for(char x: chars){
         str.append(x);
      }
      return str.toString();
   }     
}
