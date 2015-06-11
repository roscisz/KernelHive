/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.gui.dbmanager;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author mike
 */
public class HBaseManager implements ManagerInterface {
    
    private Configuration configuration;
    
    public HBaseManager() {
        configuration = HBaseConfiguration.create();
    }
    
    @Override
    public void intiateKernelHiveManager() {
        configuration = HBaseConfiguration.create();
        HBaseAdmin admin = null;
        try {
            admin = new HBaseAdmin(configuration);
        } catch (ZooKeeperConnectionException ex) {
            Logger.getLogger(DatabaseManagerDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DatabaseManagerDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(MAIN_TABLE));
        tableDescriptor.addFamily(new HColumnDescriptor(COL_DATA));
        tableDescriptor.addFamily(new HColumnDescriptor(COL_ID));
        tableDescriptor.addFamily(new HColumnDescriptor(COL_DESC));
        try {
            admin.createTable(tableDescriptor);
        } catch (IOException ex) {
            Logger.getLogger(DatabaseManagerDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public List<DataPackage> listPackages() {
        HTable table = null;
        try {
            table = new HTable(configuration, MAIN_TABLE);
        } catch (IOException ex) {
            Logger.getLogger(HBaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        Scan scan = new Scan();
        scan.addColumn(Bytes.toBytes(COL_ID), Bytes.toBytes(COL_ID));
        scan.addColumn(Bytes.toBytes(COL_DESC), Bytes.toBytes(COL_DESC));
        List<DataPackage> packageList = new ArrayList<>();
        try {
            ResultScanner scanner = table.getScanner(scan);
            for (Result result = scanner.next(); result != null; result = scanner.next()) {
                DataPackage packageInfo = new DataPackage();
                packageInfo.setId(Bytes.toLong(result.getValue(Bytes.toBytes(COL_ID), Bytes.toBytes(COL_ID))));
                packageInfo.setDescription(Bytes.toString(result.getValue(Bytes.toBytes(COL_DESC), Bytes.toBytes(COL_DESC))));
                packageList.add(packageInfo);
            }
        } catch (IOException ex) {
            Logger.getLogger(HBaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return packageList;
    }

    @Override
    public long uploadPackage(DataPackage dataPack) {
        long time = System.currentTimeMillis();
        String rowName = String.valueOf(time);
        HTable table = null;
        try {
            table = new HTable(configuration, MAIN_TABLE);
        } catch (IOException ex) {
            Logger.getLogger(HBaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        Put p = new Put(Bytes.toBytes(rowName));
        p.add(Bytes.toBytes(COL_ID), Bytes.toBytes(COL_ID), Bytes.toBytes(time));
        p.add(Bytes.toBytes(COL_DATA), Bytes.toBytes(COL_DATA), dataPack.getData());
        p.add(Bytes.toBytes(COL_DESC), Bytes.toBytes(COL_DESC), Bytes.toBytes(dataPack.getDescription()));
        try {
            table.put(p);
        } catch (IOException ex) {
            Logger.getLogger(HBaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            table.close();
        } catch (IOException ex) {
            Logger.getLogger(HBaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return time;
    }

    @Override
    public DataPackage downloadPackage(long packageID) {
        HTable table = null;
        try {
            table = new HTable(configuration, MAIN_TABLE);
        } catch (IOException ex) {
            Logger.getLogger(HBaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        Get g = new Get(Bytes.toBytes(packageID));
        Result result = null;
        try {
            result = table.get(g);
        } catch (IOException ex) {
            Logger.getLogger(HBaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        DataPackage dataPack = new DataPackage();
        if(result != null) {
            dataPack.setId(Bytes.toLong(result.getValue(Bytes.toBytes(COL_ID), Bytes.toBytes(COL_ID))));
            dataPack.setData(result.getValue(Bytes.toBytes(COL_DATA), Bytes.toBytes(COL_DATA)));
            dataPack.setDescription(Bytes.toString(result.getValue(Bytes.toBytes(COL_DESC), Bytes.toBytes(COL_DESC))));
        }
        return dataPack;
    }
    
}
