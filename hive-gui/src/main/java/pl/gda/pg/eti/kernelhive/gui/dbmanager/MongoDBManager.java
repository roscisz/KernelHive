/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.gui.dbmanager;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mike
 */
public class MongoDBManager implements ManagerInterface {

    @Override
    public void intiateKernelHiveManager() {
        Mongo mongo;
        try {
            mongo = new Mongo("localhost", 27017);
            DB db = mongo.getDB(DATABASE);           
        } catch (UnknownHostException ex) {
            Logger.getLogger(MongoDBManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MongoException ex) {
            Logger.getLogger(MongoDBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
			
    }

    @Override
    public long uploadPackage(DataPackage dataPack) {
        long time = System.currentTimeMillis();
        Mongo mongo;
        try {
            mongo = new Mongo("localhost", 27017);
            DB db = mongo.getDB(DATABASE);
            GridFS packageFile = new GridFS(db, MAIN_TABLE);
            GridFSInputFile gfsFile = packageFile.createFile(dataPack.getData());
            gfsFile.setFilename(String.valueOf(time));
            gfsFile.put(COL_ID, time);
            gfsFile.put(COL_DESC, dataPack.getDescription());
            gfsFile.save();
        } catch (UnknownHostException ex) {
            Logger.getLogger(MongoDBManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MongoException ex) {
            Logger.getLogger(MongoDBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return time;
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
            mongo = new Mongo("127.0.0.1", 27017);
            DB db = mongo.getDB(DATABASE);
            GridFS packageFile = new GridFS(db, MAIN_TABLE);
            
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
