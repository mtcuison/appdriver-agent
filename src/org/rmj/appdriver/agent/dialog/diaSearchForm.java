package org.rmj.appdriver.agent.dialog;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.rmj.appdriver.GRider;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ItemEvent;

public class diaSearchForm extends JDialog {
   private javax.swing.JComboBox<String> cmbField;
   private javax.swing.JButton cmdCancel;
   private javax.swing.JButton cmdLoad;
   private javax.swing.JScrollPane scrollPane;
   private javax.swing.JTable table;

   private final JPanel contentPanel = new JPanel();

   private String psColHead="";
   private String psColName="";
   private String psFldName="";

   private String[] paColHead;
   private String[] paColName;
   private String[] paFldName;

   private long pnSelectd = -1;
   private long pnRowFrom = 0;
   private long pnRowThru = 101;

   private String psSQLSrce = "";
   private ResultSet poRSData = null;

   private boolean pbActivated;
   private boolean pbCancel;
   private int pnSort;

   private DefaultTableModel model;
   private Map<TableColumn, Integer> columnSizes = new HashMap<TableColumn, Integer>();
   private TableRowSorter<TableModel> sorter;
   private JTextField txtSearch;

   public diaSearchForm(java.awt.Frame parent, boolean modal) {
      super(parent, modal);

      addWindowListener(new WindowAdapter() {
         @Override
         public void windowActivated(WindowEvent arg0) {
            if(!pbActivated){
               //set the column Header
               paColHead = psColHead.split("�");
               //set the name of fields to be used for each column
               paColName = psColName.split("�");

               //set field to be used on dynamic querying of database...
               if(psSQLSrce.isEmpty()){
                  paFldName = psColName.split("�");
               }
               else{
                  paFldName = psFldName.split("�");
               }

               //create the table model with 1 row
               model = new DefaultTableModel(1, paColHead.length){
                  private static final long serialVersionUID = -1242791453368915951L;
                  public boolean isCellEditable(int rowIndex, int columnIndex) {
                     return false;
                  }				
               };

               sorter = new TableRowSorter<TableModel>(model);
               //for(int lnctr=0;lnctr<paColHead.length;lnctr++){
               //	sorter.setSortable(lnctr, false);
               //}

               table.setModel(model);
               table.getSelectionModel().addListSelectionListener(new RowListener());
               table.setRowSorter(sorter);		
					
               //set the Column header
               cmbField.removeAllItems();
               for(int lnctr=0;lnctr<paColHead.length;lnctr++){
                  table.getColumnModel().getColumn(lnctr).setHeaderValue(paColHead[lnctr]);
                  cmbField.addItem(paColHead[lnctr]);
               }

               table.getTableHeader().setResizingAllowed(false);
               //table.getTableHeader().setReorderingAllowed(false);

               //Do not continue if column header length is different from column name length
               if(paColHead.length != paColName.length){
                  return;
               }
					
               //Add a click listener to get the clicked column header and assigned column as selected 
               //in our combo...
               table.getTableHeader().addMouseListener(new MouseAdapter() {
                  @Override
                  public void mouseClicked(MouseEvent e) {
                     int col = table.columnAtPoint(e.getPoint());
                     cmbField.setSelectedIndex(col);
                  }
               });					
					
               try {
                  poRSData.beforeFirst();
                  for(int lnRow = 0;poRSData.next();lnRow++){
                     model.setRowCount(lnRow+1);
                     for(int lnCol = 0;lnCol < paColName.length;lnCol++)
                        model.setValueAt(poRSData.getString(paColName[lnCol]), lnRow , lnCol);
                  }
               } catch (SQLException ex) {
                  Logger.getLogger(diaSearchForm.class.getName()).log(Level.SEVERE, null, ex);
                  ex.printStackTrace();
                  return;
               }

               //Adjust the column
               adjustColumns();
               cmbField.setSelectedIndex(pnSort);
               sortTable(cmbField.getSelectedIndex());
               pbActivated = true;
            }				
         }
      });
      
      setUndecorated(true);
      setResizable(false);
      getContentPane().setLayout(null);
      this.setSize(517,540);
		
      JPanel panel = new JPanel();
      panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
      panel.setBounds(10, 11, 492, 130);
      getContentPane().add(panel);
      panel.setLayout(null);
		
      JLabel lblKwiksearchV = new JLabel("KwikSearch v.0.1");
      lblKwiksearchV.setFont(new Font("Tahoma", Font.BOLD, 14));
      lblKwiksearchV.setBounds(10, 7, 274, 17);
      panel.add(lblKwiksearchV);
		
      JLabel lblCopyrightc = new JLabel("Copyright (c) 2018 and beyond");
      lblCopyrightc.setBounds(10, 27, 274, 14);
      panel.add(lblCopyrightc);

      JLabel lblGuanzonGroupSoftware = new JLabel("Guanzon Group Software Engineering Group");
      lblGuanzonGroupSoftware.setBounds(10, 44, 274, 14);
      panel.add(lblGuanzonGroupSoftware);

      txtSearch = new JTextField();
      txtSearch.setBounds(10, 99, 324, 20);
      panel.add(txtSearch);
      txtSearch.setColumns(10);
		
      cmbField = new JComboBox<String>();
      cmbField.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent arg0) {
            if (arg0.getStateChange() == ItemEvent.SELECTED) {
                sortTable(cmbField.getSelectedIndex());
                txtSearch.setText("");
                txtSearch.requestFocus();
            }				
         }
      });
      
      cmbField.setModel(new javax.swing.DefaultComboBoxModel<String>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
      cmbField.setBounds(344, 99, 138, 20);
      panel.add(cmbField);

      JLabel lblValue = new JLabel("Value");
      lblValue.setBounds(10, 82, 46, 14);
      panel.add(lblValue);

      JLabel lblField = new JLabel("Field");
      lblField.setBounds(344, 82, 46, 14);
      panel.add(lblField);
		
      cmdLoad = new JButton("Load");
      cmdLoad.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
         if(pnSelectd == -1){
             if(sorter.getViewRowCount() > 0){
                 pnSelectd = table.getRowSorter().convertRowIndexToModel(0);
             }
         }
         pbCancel = false;
         setVisible(false);				
         }
      });

      cmdLoad.setBounds(382, 9, 100, 23);
      panel.add(cmdLoad);
		
      cmdCancel = new JButton("Cancel");
      cmdCancel.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            pnSelectd = -1;
            pbCancel = true;
            setVisible(false);
         }
      });
      cmdCancel.setBounds(382, 33, 100, 23);
      panel.add(cmdCancel);
		
      scrollPane = new JScrollPane();
      scrollPane.setBounds(10, 152, 492, 374);
      getContentPane().add(scrollPane);

      table = new JTable();
      scrollPane.setViewportView(table);
      table.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
      table.setModel(new javax.swing.table.DefaultTableModel(
         new Object [][] {
            {null, null, null, null},
            {null, null, null, null},
            {null, null, null, null},
            {null, null, null, null}
         },
         new String [] {
            "Title 1", "Title 2", "Title 3", "Title 4"
         }
      ));
		
      table.setRowSelectionAllowed(true);
      table.setColumnSelectionAllowed(false);
      //set selection mode to single only
      table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      txtSearch.getDocument().addDocumentListener(
         new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
               newFilter();
            }
            public void insertUpdate(DocumentEvent e) {
               newFilter();
            }
            public void removeUpdate(DocumentEvent e) {
               newFilter();
            }
      });
   }
	
   public void adjustColumns(){
      TableColumnModel tcm = table.getColumnModel();

      for (int i = 0; i < tcm.getColumnCount(); i++){
              adjustColumn(i);
      }
   }   

   // Adjust the width of the specified column in the table
   public void adjustColumn(final int column){
      TableColumn tableColumn = table.getColumnModel().getColumn(column);

      if (! tableColumn.getResizable()) return;

      int columnHeaderWidth = getColumnHeaderWidth(column);
      int columnDataWidth   = getColumnDataWidth(column);
      int preferredWidth	= Math.max(columnHeaderWidth, columnDataWidth);

      //set spacing here...
      updateTableColumn(column, preferredWidth, 6);
   }    
    
   //Calculated the width based on the column name
   private int getColumnHeaderWidth(int column){
      TableColumn tableColumn = table.getColumnModel().getColumn(column);
      Object value = tableColumn.getHeaderValue();
      TableCellRenderer renderer = tableColumn.getHeaderRenderer();

      if (renderer == null){
         renderer = table.getTableHeader().getDefaultRenderer();
      }

      Component c = renderer.getTableCellRendererComponent(table, value, false, false, -1, column);
      return c.getPreferredSize().width;
   }

    //Calculate the width based on the widest cell renderer for the given column.
   private int getColumnDataWidth(int column)
   {
      int preferredWidth = 0;
      int maxWidth = table.getColumnModel().getColumn(column).getMaxWidth();
      for (int row = 0; row < table.getRowCount(); row++){
         preferredWidth = Math.max(preferredWidth, getCellDataWidth(row, column));
         //  We've exceeded the maximum width, no need to check other rows
         if (preferredWidth >= maxWidth)
            break;
      }
      return preferredWidth;
   }

   //Get the preferred width for the specified cell
   private int getCellDataWidth(int row, int column){
      //  Inovke the renderer for the cell to calculate the preferred width
      TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
      Component c = table.prepareRenderer(cellRenderer, row, column);
      int width = c.getPreferredSize().width + table.getIntercellSpacing().width;

      return width;
   }

   //Update the TableColumn with the newly calculated width
   private void updateTableColumn(int column, int width, int spacing){
      final TableColumn tableColumn = table.getColumnModel().getColumn(column);

      if (! tableColumn.getResizable()) return;

      width += spacing;

      columnSizes.put(tableColumn, new Integer(tableColumn.getWidth()));
      table.getTableHeader().setResizingColumn(tableColumn);
      tableColumn.setWidth(width);
   }    
 	
   private void newFilter() {
      RowFilter<TableModel , Object> rf = null;
      //If current expression doesn't parse, don't update.
      try {
         int index = cmbField.getSelectedIndex();
         //if no field was selected then set the first field as the selected index
         if(index == -1)
            index = 0;

         rf = RowFilter.regexFilter("^".concat(txtSearch.getText()), index);
      } catch (java.util.regex.PatternSyntaxException e) {
         return;
      }
      sorter.setRowFilter(rf);
   }	 

   //use this method to sort the table accordingly... 
   private void sortTable(int column){
      sorter = new TableRowSorter<TableModel>(model);

      table.setRowSorter(sorter);
      List<RowSorter.SortKey> sortKeys = new ArrayList<>();

      sortKeys.add(new RowSorter.SortKey(column, SortOrder.ASCENDING));

      sorter.setSortKeys(sortKeys);
      sorter.sort();		 
   }
   
   private class RowListener implements ListSelectionListener {
      public void valueChanged(ListSelectionEvent event) {
         if (event.getValueIsAdjusting()) {
            return;
         }
         if(table.getSelectedRow() == -1)
            pnSelectd = -1;
         else
            pnSelectd = table.getRowSorter().convertRowIndexToModel(table.getSelectedRow());
      }
   }	 

   public void setFieldHeader(String value){
      psColHead = value;
   }

   public void setFieldName(String value){
      psColName = value;
   }

   public void setFieldCriteria(String value){
      psFldName = value;
   }
    
   public void setSQLSource(String value){
      psSQLSrce = value;
   }

   public void setDataSource(ResultSet value){
      poRSData = value;
   }
    
   public void setSort(int sort){
      pnSort = sort;
   }
	
   public String getSelected(){
      System.out.println(pnSelectd);
      System.out.println(pbCancel);
      if(pbCancel)
         return null;
      else{
         if(pnSelectd == -1)
            return "";
         else{
            try {
               poRSData.absolute((int)pnSelectd + 1);
               StringBuilder lsSQL = new StringBuilder();
               ResultSetMetaData meta = poRSData.getMetaData();
               Object object;
               for(int x=1;x<=meta.getColumnCount();x++){
                  object = poRSData.getObject(x);
                  System.out.println(object == null ? null : object.toString());
                  if(x==1)
                     lsSQL.append((object == null ? null : object.toString()));
                  else
                     lsSQL.append("�").append((object == null ? "null" : object.toString()));
               }
               return lsSQL.toString();
            } catch (SQLException ex) {
                    ex.printStackTrace();
                    return null;
            }
         }
      }
   }
}
