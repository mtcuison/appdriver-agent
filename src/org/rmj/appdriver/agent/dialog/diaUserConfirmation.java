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
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class diaUserConfirmation extends JDialog {
   private ImagePanelme loginBG;
   private JPasswordField txtPassword;
   private JTextField txtUserName;
   private JButton button;
   private JButton button_1;

   private boolean pbIsOkey = false;
	
   /**
    * Create the dialog.
    */
   public diaUserConfirmation(java.awt.Frame parent, boolean modal) {
      super(parent, modal);
      setResizable(false);
      addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent arg0) {
            txtUserName.setText("");
            txtPassword.setText("");
            pbIsOkey = false;
            setVisible(false);			
         }
      });
      setBounds(100, 100, 356, 227);
      getContentPane().setLayout(null);
	
      loginBG = new ImagePanelme(new ImageIcon("/images/user_confirmation_bg.jpg").getImage());
      loginBG.setBounds(0, 0, 350, 200);
      getContentPane().add(loginBG);

      JLabel label = new JLabel("User Name");
      label.setForeground(Color.WHITE);
      label.setBounds(127, 98, 67, 14);
      loginBG.add(label);

      JLabel label_1 = new JLabel("Password");
      label_1.setForeground(Color.WHITE);
      label_1.setBounds(127, 123, 46, 14);
      loginBG.add(label_1);

      txtPassword = new JPasswordField();
      txtPassword.setBounds(196, 120, 144, 20);
      loginBG.add(txtPassword);

      txtUserName = new JTextField();
      txtUserName.setColumns(10);
      txtUserName.setBounds(196, 95, 144, 20);
      loginBG.add(txtUserName);
        
      button = new JButton("OK");
      button.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            //make sure that username is filled-up
            if(txtUserName.getText().isEmpty()){
               JOptionPane.showMessageDialog(null, "Please fill-up the username!", "Login", JOptionPane.OK_OPTION + JOptionPane.INFORMATION_MESSAGE);
               txtUserName.requestFocusInWindow();
               return;
            }

            //make sure that password is filled-up
            if(txtPassword.getPassword().length == 0 ){
               JOptionPane.showMessageDialog(null, "Please fill-up the password!", "Login", JOptionPane.OK_OPTION + JOptionPane.INFORMATION_MESSAGE);
               txtPassword.requestFocusInWindow();
               return;
            }

            pbIsOkey = true;
            setVisible(false);
         }
      });
      button.setBounds(156, 155, 89, 23);
      loginBG.add(button);
        
      button_1 = new JButton("Cancel");
      button_1.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            txtUserName.setText("");
            txtPassword.setText("");
            pbIsOkey = false;
            setVisible(false);        		
         }
      });
      button_1.setBounds(251, 155, 89, 23);
      loginBG.add(button_1);
   }
	
   public boolean isOkey(){
      return pbIsOkey;
   }

   public String getUserName(){
      return txtUserName.getText();
   }

   public String getPassword(){
      return toString(txtPassword.getPassword());
   }	
     
   private String toString(char chars[]){
      StringBuilder str = new StringBuilder();
      for(char x: chars){
         str.append(x);
      }
      return str.toString();
   }     
}
