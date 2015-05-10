/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.gui.dbmanager;

import java.util.List;

/**
 *
 * @author mike
 */
public interface ManagerInterface {
    static String COL_ID = "col_id";
    static String COL_DATA = "col_data";
    static String COL_DESC = "col_desc";
    
    static String DATABASE = "KernelHiveDB";
    static String MAIN_TABLE = "kernel_hive_data";
    /**
     * This method is responsible for creating MAIN_TABLE table
     * and proper column structure.
     */
    public void intiateKernelHiveManager();
    /**
     * This method uploads a byte array package to the database.
     * @param dataPack to be uploaded
     * @return Package ID
     */
    public long uploadPackage(DataPackage dataPack);
    /**
     * This method downloads a byte package from the database.
     * @param packageID to be downloaded
     * @return byte array with data
     */
    public DataPackage downloadPackage(long packageID);
    /**
     * List packages
     * @return list of packages
     */
    public List<DataPackage> listPackages();
    
}
