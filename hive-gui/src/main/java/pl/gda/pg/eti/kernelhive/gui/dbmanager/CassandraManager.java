/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.gui.dbmanager;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mike
 */
public class CassandraManager implements ManagerInterface {

    @Override
    public void intiateKernelHiveManager() {
//        try {
        Session session;
        Cluster cluster;
        cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
        session = cluster.connect();
        
        session.execute("CREATE KEYSPACE " + DATABASE + " WITH replication "
            + "= {'class':'SimpleStrategy', 'replication_factor':3};");

        session.execute("CREATE TABLE " + DATABASE + "." + MAIN_TABLE + " (" + COL_ID + " bigint PRIMARY KEY,"
            + COL_DESC + " text," + COL_DATA + " blob" + ");");
    }

    @Override
    public long uploadPackage(DataPackage dataPack) {
        long time = System.currentTimeMillis();
        try {
            Session session;
            Cluster cluster;
            cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
            session = cluster.connect();
            ByteBuffer buffer = ByteBuffer.wrap(dataPack.getData());
            Statement statement = QueryBuilder.insertInto(DATABASE, MAIN_TABLE)
                    .value(COL_ID, time)
                    .value(COL_DATA, buffer)
                    .value(COL_DESC, dataPack.getDescription());
            session.execute(statement);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return time;
    }

    @Override
    public DataPackage downloadPackage(long packageID) {
        DataPackage dataPack = new DataPackage();
        try {
            Session session;
            Cluster cluster;
            cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
            session = cluster.connect();
            Statement statement = QueryBuilder.select()
                    .all()
                    .from(DATABASE, MAIN_TABLE)
                    .where(eq(COL_ID, packageID));
            ResultSet results = session.execute(statement);
            for(Row row : results) {
                dataPack.setId(row.getLong(COL_ID));
                dataPack.setDescription(row.getString(COL_DESC));
                dataPack.setData(row.getBytes(COL_DATA).array());
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return dataPack;
    }

    @Override
    public List<DataPackage> listPackages() {
        List<DataPackage> dataPacks = new ArrayList<>();
        try {
            Session session;
            Cluster cluster;
            cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
            session = cluster.connect();
            Statement statement = QueryBuilder.select()
                    .all()
                    .from(DATABASE, MAIN_TABLE);
            ResultSet results = session.execute(statement);
            for(Row row : results) {
                DataPackage dataPack = new DataPackage();
                dataPack.setId(row.getLong(COL_ID));
                dataPack.setDescription(row.getString(COL_DESC));
                dataPacks.add(dataPack);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return dataPacks;
    }
    
}
