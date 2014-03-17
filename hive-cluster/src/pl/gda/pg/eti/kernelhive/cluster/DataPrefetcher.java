package pl.gda.pg.eti.kernelhive.cluster;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

import pl.gda.pg.eti.kernelhive.common.clusterService.ClusterBean;
import pl.gda.pg.eti.kernelhive.common.clusterService.DataAddress;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job;
import pl.gda.pg.eti.kernelhive.common.clusterService.JobInfo;

public class DataPrefetcher implements Runnable {
	private static final Logger logger = Logger.getLogger(DataPrefetcher.class.getName());
	private JobInfo job;
	private UnitServer unitServer;
	public ServerAddress localServer;
	
	public DataPrefetcher(JobInfo job, String destHostname, int destPort, UnitServer unitServer) throws UnknownHostException {
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
		MongoCredential credential = MongoCredential.createMongoCRCredential("hive-dataserver", "hive-dataserver", "hive-dataserver".toCharArray());
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
		int oldID = (int) record.get("seq");
		int newID = oldID + 1;		
		record.put("seq", newID);
		countersCollection.update(new BasicDBObject("_id", "package"), record);
		
		return newID;
	}

}
