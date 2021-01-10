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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class diaSystemApproval extends JDialog {
   private static final long serialVersionUID = -7355583364426504718L;
   private ImagePanelme loginBG;
   private JTextField txtUserName;
   private JPasswordField txtPassword;
   private JButton button;
   private JButton button_1;

   private boolean pbIsOkey = false;
	
	/**
	 * Create the dialog.
	 */
   public diaSystemApproval(java.awt.Frame parent, boolean modal) {
      super(parent, modal);
      setResizable(false);
      addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            txtUserName.setText("");
            txtPassword.setText("");
            pbIsOkey = false;
            setVisible(false);			
         }
      });
      
      setBounds(100, 100, 355, 227);
      getContentPane().setLayout(new BorderLayout());
      loginBG = new ImagePanelme(new ImageIcon("/images/system_approval_bg.jpg").getImage());
      loginBG.setBounds(0, 0, 350, 200);
      getContentPane().add(loginBG);
      loginBG.setLayout(null);
        
      JLabel label = new JLabel("User Name");
      label.setBounds(125, 96, 67, 14);
      label.setForeground(Color.WHITE);
      loginBG.add(label);
        
      txtUserName = new JTextField();
      txtUserName.setBounds(194, 93, 144, 20);
      txtUserName.setColumns(10);
      loginBG.add(txtUserName);
        
      JLabel label_1 = new JLabel("Password");
      label_1.setBounds(125, 121, 46, 14);
      label_1.setForeground(Color.WHITE);
      loginBG.add(label_1);
        
      txtPassword = new JPasswordField();
      txtPassword.setBounds(194, 118, 144, 20);
      loginBG.add(txtPassword);
        
      button = new JButton("OK");
      button.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
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
      button.setBounds(154, 153, 89, 23);
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
      button_1.setBounds(249, 153, 89, 23);
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
