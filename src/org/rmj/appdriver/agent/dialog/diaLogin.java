package org.rmj.appdriver.agent.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.rmj.appdriver.agent.ImagePanelme;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class diaLogin extends JDialog {
   private final JPanel contentPanel = new JPanel();
   private ImagePanelme loginBG;
   private JTextField txtUserName;
   private JPasswordField txtPassword;
   private JComboBox cmbApplication = new JComboBox();
   private JButton btnOkey = new JButton();
   private JButton btnCancel = new JButton("Cancel");

   private boolean pbIsOkey = false;
   private String psAppCode[];
   private String selected = "";
	   
   public diaLogin(java.awt.Frame parent, boolean modal) {
      super(parent, modal);
        
      addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent arg0) {
            txtUserName.setText("");
            txtPassword.setText("");
            pbIsOkey = false;
            setVisible(false);        		
         }
      });
      
      System.out.println("Displaying diaLogin");
      
      this.setSize(514,440);
      this.setBackground(Color.black);
      this.setLocationRelativeTo(null);

      loginBG = new ImagePanelme(new ImageIcon("/images/login_bg2.jpg").getImage());
      loginBG.setBounds(0, 0, 500, 400);
      getContentPane().add(loginBG, BorderLayout.SOUTH);

      JLabel lblUserAuthentication = new JLabel("USER AUTHENTICATION");
      lblUserAuthentication.setForeground(Color.WHITE);
      lblUserAuthentication.setFont(new Font("Arial", Font.BOLD, 18));
      lblUserAuthentication.setBounds(259, 86, 229, 29);
      loginBG.add(lblUserAuthentication);

      JLabel lblUserName = new JLabel("User Name");
      lblUserName.setForeground(Color.WHITE);
      lblUserName.setBounds(259, 126, 67, 14);
      loginBG.add(lblUserName);

      txtUserName = new JTextField();
      txtUserName.setBounds(328, 123, 144, 20);
      loginBG.add(txtUserName);
      txtUserName.setColumns(10);
        
      JLabel lblPassword = new JLabel("Password");
      lblPassword.setForeground(Color.WHITE);
      lblPassword.setBounds(259, 151, 46, 14);
      loginBG.add(lblPassword);

      txtPassword = new JPasswordField();
      txtPassword.setBounds(328, 148, 144, 20);
      loginBG.add(txtPassword);
        
      JLabel lblApplication = new JLabel("Application");
      lblApplication.setFont(new Font("Tahoma", Font.BOLD, 12));
      lblApplication.setForeground(Color.WHITE);
      lblApplication.setBounds(259, 176, 101, 20);
      loginBG.add(lblApplication);

      cmbApplication = new JComboBox();
      cmbApplication.setForeground(Color.BLACK);
      cmbApplication.setModel(new DefaultComboBoxModel(new String[] {"GCard Systems", "GGC Integrated Systems"}));
      cmbApplication.setBounds(258, 196, 214, 20);
      loginBG.add(cmbApplication);
        
      btnOkey = new JButton("OK");
      btnOkey.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            if(txtUserName.getText().trim().isEmpty()){
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
            
            if(cmbApplication.getSelectedIndex() > -1)
                    selected = psAppCode[cmbApplication.getSelectedIndex()];

            pbIsOkey = true;
            setVisible(false);
         }
      });

      btnOkey.setBounds(288, 238, 89, 23);
      loginBG.add(btnOkey);
        
      btnCancel = new JButton("Cancel");
      btnCancel.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            txtUserName.setText("");
            txtPassword.setText("");
            pbIsOkey = false;
            setVisible(false);        		
         }
      });
        
      btnCancel.setBounds(383, 238, 89, 23);
      loginBG.add(btnCancel);
    }	
    
   public void setApps(String name[], String code[]){
      psAppCode = code;
      cmbApplication.removeAllItems();
      for(String x:name)
         cmbApplication.addItem(x);

      selected = code[0];
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

   public String getSelectedApp(){
      return selected;
   }
     
   private String toString(char chars[]){
      StringBuilder str = new StringBuilder();
      for(char x: chars){
         str.append(x);
      }
      return str.toString();
   }
}
