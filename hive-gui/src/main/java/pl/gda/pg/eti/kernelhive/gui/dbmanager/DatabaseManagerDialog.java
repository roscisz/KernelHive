/**
 * Copyright (c) 2015 Gdansk University of Technology Copyright (c) 2015 Michael
 * Suchacz
 *
 * This file is part of KernelHive. KernelHive is free software; you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * KernelHive is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * KernelHive. If not, see <http://www.gnu.org/licenses/>.
 */
package pl.gda.pg.eti.kernelhive.gui.dbmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.jdesktop.beansbinding.Binding;

/**
 *
 * @author mike
 */
public class DatabaseManagerDialog extends javax.swing.JDialog {

    /**
     * Creates new form DatabaseManager
     */
    public DatabaseManagerDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        packageList = new ArrayList<>();
        initComponents();
        dbManager = hBaseManager;
    }
    
    private ManagerInterface dbManager;
    
    private class NotEditableTable extends JTable {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false; //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        hBaseManager = new pl.gda.pg.eti.kernelhive.gui.dbmanager.HBaseManager();
        mySQLManager = new pl.gda.pg.eti.kernelhive.gui.dbmanager.MySQLManager();
        cassandraManager = new pl.gda.pg.eti.kernelhive.gui.dbmanager.CassandraManager();
        mongoDBManager = new pl.gda.pg.eti.kernelhive.gui.dbmanager.MongoDBManager();
        selectedDataPackage = new pl.gda.pg.eti.kernelhive.gui.dbmanager.DataPackage();
        packageList = new java.util.ArrayList<DataPackage>();
        fileChooser = new javax.swing.JFileChooser();
        redisManager = new pl.gda.pg.eti.kernelhive.gui.dbmanager.RedisManager();
        mongoKHManager = new pl.gda.pg.eti.kernelhive.gui.dbmanager.MongoKHManager();
        lblTitle = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPackages = new NotEditableTable();
        jPanel1 = new javax.swing.JPanel();
        lblDesc = new javax.swing.JLabel();
        tfDescription = new javax.swing.JTextField();
        btUpload = new javax.swing.JButton();
        btDownload = new javax.swing.JButton();
        btInitiate = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cbDatabaseEngine = new javax.swing.JComboBox();
        btGetList = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        lblTitle.setText("Database Manager");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        getContentPane().add(lblTitle, gridBagConstraints);

        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, packageList, tblPackages, "tablePackages");
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${id}"));
        columnBinding.setColumnName("Id");
        columnBinding.setColumnClass(Long.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${description}"));
        columnBinding.setColumnName("Description");
        columnBinding.setColumnClass(String.class);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, selectedDataPackage, org.jdesktop.beansbinding.ObjectProperty.create(), tblPackages, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        tblPackages.getColumnModel().getColumn(0).setPreferredWidth(100);
        jScrollPane1.setViewportView(tblPackages);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jScrollPane1, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        lblDesc.setText("Description:");
        jPanel1.add(lblDesc);

        tfDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfDescriptionActionPerformed(evt);
            }
        });
        jPanel1.add(tfDescription);

        btUpload.setText("Upload");
        btUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btUploadActionPerformed(evt);
            }
        });
        jPanel1.add(btUpload);

        btDownload.setText("Download");
        btDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDownloadActionPerformed(evt);
            }
        });
        jPanel1.add(btDownload);

        btInitiate.setText("Initiate");
        btInitiate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btInitiateActionPerformed(evt);
            }
        });
        jPanel1.add(btInitiate);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        getContentPane().add(jPanel1, gridBagConstraints);

        jLabel1.setText("Select database engine:");
        jPanel2.add(jLabel1);

        cbDatabaseEngine.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "HBase", "MySQL", "Cassandra", "Redis", "MongoDB", "MongoKH" }));
        cbDatabaseEngine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbDatabaseEngineActionPerformed(evt);
            }
        });
        jPanel2.add(cbDatabaseEngine);

        btGetList.setText("Get list");
        btGetList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btGetListActionPerformed(evt);
            }
        });
        jPanel2.add(btGetList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        getContentPane().add(jPanel2, gridBagConstraints);

        bindingGroup.bind();

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbDatabaseEngineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbDatabaseEngineActionPerformed
        switch(cbDatabaseEngine.getSelectedItem().toString()) {
            case "HBase":
                dbManager = hBaseManager;
                break;
            case "MySQL":
                dbManager = mySQLManager;
                break;
            case "Cassandra":
                dbManager = cassandraManager;
                break;
            case "MongoDB":
                dbManager = mongoDBManager;
                break;      
            case "MongoKH":
                dbManager = mongoKHManager;
                break;
            case "Redis":
                dbManager = redisManager;
                break;
        }        // TODO add your handling code here:
    }//GEN-LAST:event_cbDatabaseEngineActionPerformed

    private void btGetListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btGetListActionPerformed
        Binding b = bindingGroup.getBinding("tablePackages");
        b.unbind();
        packageList.clear();
        for(DataPackage pack : dbManager.listPackages()) {
            packageList.add(pack);
        }
        b.bind();
        tblPackages.repaint();
    }//GEN-LAST:event_btGetListActionPerformed

    private void btUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUploadActionPerformed
        DataPackage dataPack = new DataPackage();
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (file.length() < 1000000) {
                FileInputStream fileInputStream;
                try {
                    fileInputStream = new FileInputStream(file);
                    byte[] data = new byte[(int)file.length()];
                    fileInputStream.read(data);
                    dataPack.setData(data);
                    dataPack.setDescription(tfDescription.getText());
                    dbManager.uploadPackage(dataPack);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(DatabaseManagerDialog.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(DatabaseManagerDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }

        } else {
            System.out.println("File access cancelled by user.");
        }
    }//GEN-LAST:event_btUploadActionPerformed

    private void tfDescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfDescriptionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfDescriptionActionPerformed

    private void btDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDownloadActionPerformed
        dbManager.downloadPackage(selectedDataPackage.getId());
    }//GEN-LAST:event_btDownloadActionPerformed

    private void btInitiateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btInitiateActionPerformed
        dbManager.intiateKernelHiveManager();        // TODO add your handling code here:
    }//GEN-LAST:event_btInitiateActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btDownload;
    private javax.swing.JButton btGetList;
    private javax.swing.JButton btInitiate;
    private javax.swing.JButton btUpload;
    private pl.gda.pg.eti.kernelhive.gui.dbmanager.CassandraManager cassandraManager;
    private javax.swing.JComboBox cbDatabaseEngine;
    private javax.swing.JFileChooser fileChooser;
    private pl.gda.pg.eti.kernelhive.gui.dbmanager.HBaseManager hBaseManager;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDesc;
    private javax.swing.JLabel lblTitle;
    private pl.gda.pg.eti.kernelhive.gui.dbmanager.MongoDBManager mongoDBManager;
    private pl.gda.pg.eti.kernelhive.gui.dbmanager.MongoKHManager mongoKHManager;
    private pl.gda.pg.eti.kernelhive.gui.dbmanager.MySQLManager mySQLManager;
    private java.util.ArrayList<DataPackage> packageList;
    private pl.gda.pg.eti.kernelhive.gui.dbmanager.RedisManager redisManager;
    private pl.gda.pg.eti.kernelhive.gui.dbmanager.DataPackage selectedDataPackage;
    private javax.swing.JTable tblPackages;
    private javax.swing.JTextField tfDescription;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
