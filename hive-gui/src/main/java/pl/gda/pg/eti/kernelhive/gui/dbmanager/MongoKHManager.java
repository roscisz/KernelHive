/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.gui.dbmanager;

import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.gridfs.GridFS;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static pl.gda.pg.eti.kernelhive.gui.dbmanager.ManagerInterface.COL_DESC;
import static pl.gda.pg.eti.kernelhive.gui.dbmanager.ManagerInterface.DATABASE;
import static pl.gda.pg.eti.kernelhive.gui.dbmanager.ManagerInterface.MAIN_TABLE;

/**
 *
 * @author mike
 */
public class MongoKHManager implements ManagerInterface {

    @Override
    public void intiateKernelHiveManager() {
        Mongo mongo;
        try {
            mongo = new Mongo("localhost", 27017);
            DB db = mongo.getDB("hive-dataserver");           
        } catch (UnknownHostException ex) {
            Logger.getLogger(MongoDBManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MongoException ex) {
            Logger.getLogger(MongoDBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public long uploadPackage(DataPackage dataPack) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DataPackage downloadPackage(long packageID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DataPackage> listPackages() {
        List<DataPackage> result = new ArrayList<>();
        Mongo mongo;
        try {
            mongo = new Mongo("localhost", 27017);
            DB db = mongo.getDB("hive-dataserver");
            GridFS packageFile = new GridFS(db);
            
            DBCursor cursor = packageFile.getFileList();
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                DataPackage dataPack = new DataPackage();
                dataPack.setId(Long.parseLong((String) obj.get("filename")));
                dataPack.setDescription((String) obj.get(COL_DESC));
                result.add(dataPack);
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(MongoDBManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MongoException ex) {
            Logger.getLogger(MongoDBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
}
