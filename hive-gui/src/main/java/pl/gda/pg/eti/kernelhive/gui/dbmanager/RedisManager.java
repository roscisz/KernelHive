/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.gui.dbmanager;

import java.util.List;
import redis.clients.jedis.Jedis;

/**
 *
 * @author mike
 */
public class RedisManager implements ManagerInterface {
    
    private static final String REDIS_KEY_VALUE_PATTERN = MAIN_TABLE + "_%s_" + "%s";
    
    @Override
    public void intiateKernelHiveManager() {
        Jedis jedis = new Jedis("localhost");
        System.out.println("Connection to server sucessfully");
      //check whether server is running or not
        System.out.println("Server is running: "+jedis.ping());
    }

    @Override
    public long uploadPackage(DataPackage dataPack) {
        long time = System.currentTimeMillis();
        
        Jedis jedis = new Jedis("localhost");
        jedis.set(String.format(REDIS_KEY_VALUE_PATTERN, String.valueOf(time), COL_ID), String.valueOf(time));
        jedis.set(String.format(REDIS_KEY_VALUE_PATTERN, String.valueOf(time), COL_DESC), dataPack.getDescription());
        jedis.set(String.format(REDIS_KEY_VALUE_PATTERN, String.valueOf(time), COL_DATA).getBytes(), dataPack.getData());

        return time;
    }

    @Override
    public DataPackage downloadPackage(long packageID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DataPackage> listPackages() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
