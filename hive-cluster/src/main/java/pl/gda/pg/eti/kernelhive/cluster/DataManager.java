/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Pawel Rosciszewski
 * Copyright (c) 2016 Adrian Boguszewski
 *
 * This file is part of KernelHive.
 * KernelHive is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * KernelHive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with KernelHive. If not, see <http://www.gnu.org/licenses/>.
 */
package pl.gda.pg.eti.kernelhive.cluster;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import pl.gda.pg.eti.kernelhive.common.clusterService.DataAddress;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job;
import pl.gda.pg.eti.kernelhive.common.clusterService.JobInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class DataManager implements Runnable {
	private static final Logger logger = Logger.getLogger(DataManager.class.getName());
	private JobInfo job;
	private UnitServer unitServer;
	public ServerAddress localServer;
	
	public DataManager(JobInfo job, String destHostname, int destPort, UnitServer unitServer) throws UnknownHostException {
		this.job = job;
		this.unitServer = unitServer;
		this.localServer = new ServerAddress(destHostname, destPort);
	}

	@Override
	public void run() {
		logger.info("Job data string: " + this.job.dataString);
		List<DataAddress> givenAddresses = Job.getAddressesForDataString(this.job.dataString);
		List<DataAddress> prefetchedAddresses = new ArrayList<DataAddress>();
					        
        for(DataAddress givenAddress : givenAddresses) {
        	logger.info("Prefetching data from address " + givenAddress.hostname + " " + givenAddress.port);
			try {
				prefetchedAddresses.add(prefetchData(givenAddress, localServer));
			} catch (IOException e) {
				e.printStackTrace();
			}
        }

        logger.info("Finished prefetching, got " + prefetchedAddresses.size() + " addresses.");
        unitServer.onJobDone(job.ID, Job.serializeAddresses(prefetchedAddresses));
        logger.info("Reported over, over.");
	}
	
	public GridFS connectToDatabase(ServerAddress server) {
		MongoCredential credential = MongoCredential.createMongoCRCredential("hive-dataserver", "admin", "hive-dataserver".toCharArray());
		MongoClient mongoClient = new MongoClient(server, Arrays.asList(credential));
		logger.info("got client");
		DB db = mongoClient.getDB("hive-dataserver");
		logger.info("Got DB");
		return new GridFS(db);
	}

	public DataAddress prefetchData(DataAddress givenAddress, ServerAddress destAddress) throws IOException {
		logger.info("yo2");
		ServerAddress givenServer = new ServerAddress(givenAddress.hostname, givenAddress.port);
		GridFS givenDatabase = connectToDatabase(givenServer);
		
		logger.info("yo");
             		
        GridFSDBFile givenPackage = givenDatabase.findOne(new BasicDBObject("_id", givenAddress.ID));
        ByteArrayOutputStream baos = new ByteArrayOutputStream((int)givenPackage.getLength());
        givenPackage.writeTo(baos);
               
        logger.info("Prefetched");
                                      
        GridFS destDatabase = connectToDatabase(destAddress);
        GridFSInputFile destPackage = destDatabase.createFile(baos.toByteArray());
        int newID = getNextId(destDatabase);
        logger.info("Got new id for prefetched package: " + newID);
        destPackage.put("_id", newID);
        destPackage.save();
        
        logger.info("after save");
        
        DataAddress ret = new DataAddress();
        ret.hostname = destAddress.getHost();
        ret.port = destAddress.getPort();
        ret.ID = newID;            
        return ret;        
	}

	public int getNextId(GridFS destDatabase) {
		DBCollection countersCollection = destDatabase.getDB().getCollection("counters");
	
		DBObject record = countersCollection.findOne(new BasicDBObject("_id", "package"));
		if (record == null) {
			BasicDBObject dbObject = new BasicDBObject("_id", "package");
			dbObject.append("seq", 0);
			countersCollection.insert(dbObject);
			record = dbObject;
		}
		int oldID = (int) record.get("seq");
		int newID = oldID + 1;
		record.put("seq", newID);
		countersCollection.update(new BasicDBObject("_id", "package"), record);
		
		return newID;
	}

	public DataAddress uploadData(String data, DataAddress dataAddress) throws UnknownHostException {
        ServerAddress server = new ServerAddress(dataAddress.hostname, dataAddress.port);
        GridFS database = connectToDatabase(server);

        logger.info("Database connected");

        GridFSInputFile file = database.createFile(data.getBytes());
        int newID = getNextId(database);
        logger.info("Got new id for uploaded file: " + newID);
		file.setFilename(String.valueOf(newID));
        file.put("_id", newID);
        file.save();

        logger.info("after save");

        return new DataAddress(dataAddress.hostname, dataAddress.port, newID);
    }

}
