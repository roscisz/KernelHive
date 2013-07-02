package pl.gda.pg.eti.kernelhive.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.clusterService.Device;
import pl.gda.pg.eti.kernelhive.engine.interfaces.IKnapsackItem;
import pl.gda.pg.eti.kernelhive.engine.interfaces.IKnapsackSolver;
import pl.gda.pg.eti.kernelhive.engine.optimizers.GreedyKnapsackSolver;
import pl.gda.pg.eti.kernelhive.engine.optimizers.KnapsackItemStub;

public class KnapsackTester {
	public static class DeviceSim {
		public float tNeeded;
		public float tDone;
		public DeviceSim(float tNeeded) {
			this.tNeeded = tNeeded;
			this.tDone = tNeeded;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {	
		List<IKnapsackItem> items = new ArrayList<IKnapsackItem>();
				
//		/* data3: */
//		// Des std
//		for(int i = 0; i != 6; i++) {
//			items.add(new KnapsackItemStub(75, 86, "Corei7_1"));
//			items.add(new KnapsackItemStub(100, 3623, "GTS450"));
//		}
//		
//		// Des03
//		items.add(new KnapsackItemStub(75, 86, "Corei7_2"));
//		items.add(new KnapsackItemStub(175, 4545, "GTX480"));
//		
//		// Des04
//		items.add(new KnapsackItemStub(75, 86, "Corei7_2"));
//		items.add(new KnapsackItemStub(100, 3623, "GTS450"));
//		items.add(new KnapsackItemStub(175, 4545, "GTX480"));
//		
//		// Cuda1
//		items.add(new KnapsackItemStub(165, 2272, "GTX560Ti"));
//		items.add(new KnapsackItemStub(150, 1677, "Tesla"));
//		items.add(new KnapsackItemStub(60, 503, "Quadro"));
		
		// data4:		
		// Des std
		for(int i = 0; i != 16; i++) {
			items.add(new KnapsackItemStub(75, 1, "Corei7_1"));
			items.add(new KnapsackItemStub(100, 127, "GTS450"));
		}
		
//		// Des03
//		items.add(new KnapsackItemStub(75, 4, "Corei7_2"));
//		items.add(new KnapsackItemStub(175, 244, "GTX480"));
		
//		// Des04
//		items.add(new KnapsackItemStub(75, 4, "Corei7_2"));
//		items.add(new KnapsackItemStub(100, 194, "GTS450"));
//		items.add(new KnapsackItemStub(175, 244, "GTX480"));
//		
//		// Cuda1
//		items.add(new KnapsackItemStub(165, 122, "GTX560Ti"));
//		items.add(new KnapsackItemStub(150, 90, "Tesla"));
//		items.add(new KnapsackItemStub(60, 27, "Quadro"));						
		
		Collections.sort(items);
		int maxLimit = 0, minLimit = 31331331;
		for(IKnapsackItem item : items) {
			maxLimit += item.getWeight();
			minLimit = Math.min(minLimit, item.getWeight());
		}
		
		int multiplier = 1000000;
		//float tStartup = 0.0f;
		//System.out.println("tStartup = " + tStartup);
		int nPackages;
		IKnapsackSolver solver = new GreedyKnapsackSolver();
		
//		for(int limit = 100; limit <= 2075; limit++) {
//			boolean[] result = solver.solve(items, limit);
//			
//			
//			List<DeviceSim> devices = new ArrayList<DeviceSim>();
//			
//			int nDevices = 0;
//			for(int i = 0; i != result.length; i++) {
//				if(result[i]) {
//					nDevices++;					
//				}
//			}
//			nPackages = nDevices * 20;
//			//System.out.println("pm " + packageMultiplier + "np " + nPackages);
//			
//			int sumCost = 0;
//			int sumPerf = 0;
//			for(int i = 0; i != result.length; i++) {
//				if(result[i]) {				
//					sumCost += items.get(i).getWeight();
//					sumPerf += items.get(i).getValue();
//					devices.add(new DeviceSim((float)multiplier/(float)items.get(i).getValue()/(float)nPackages));
//				}
//			}
//			
//			// Simulation
//			int jobsLeft = nPackages;
//			float time = 0, dtime = 0;
//			float maxt = 0;
//			while(jobsLeft > 0) {
//				//System.out.println("Simulator in time " + time);
//				float mint = 3133133;
//				maxt = 0;
//				
//				for(DeviceSim ds : devices) {
//					ds.tDone += dtime;
//					if(ds.tDone >= ds.tNeeded) {
//						ds.tDone = 0;
//						jobsLeft--;
//						//System.out.println("In time " + time + " job done by " + ds.tNeeded);
//					}		
//					mint = Math.min(mint, ds.tNeeded - ds.tDone + tStartup);
//					maxt = Math.max(maxt, ds.tNeeded - ds.tDone + tStartup);
//				}		
//				if(jobsLeft > 0)
//					dtime = mint;
//				else dtime = maxt;
//				time += dtime;
//			}
//			System.out.println((int)time);
//			//System.out.println( Math.round((5954.2/time)*100.0) /100.0);
//			//System.out.println(limit+"");
//			//System.out.println((int) ( (float)multiplier/(float)sumPerf));
//		}		
		
		

		int limit = 31331332;
		for(int packageMultiplier = 5; packageMultiplier != 101; packageMultiplier++) {

			boolean[] result = solver.solve(items, limit);
			
			List<DeviceSim> devices = new ArrayList<DeviceSim>();
			
			int nDevices = 0;
			for(int i = 0; i != result.length; i++) {
				if(result[i]) {
					nDevices++;					
				}
			}
			nPackages = nDevices * packageMultiplier;
			float tStartup = 15.0f;// * nDevices;
			//System.out.println("pm " + packageMultiplier + "np " + nPackages);
			
			int sumCost = 0;
			int sumPerf = 0;
			for(int i = 0; i != result.length; i++) {
				if(result[i]) {				
					sumCost += items.get(i).getWeight();
					sumPerf += items.get(i).getValue();
					//System.out.println("Package multiplier " + packageMultiplier);
					//System.out.println("Device " + ((KnapsackItemStub)items.get(i)).getName() + " tNeeded = " + (float)multiplier/(float)items.get(i).getValue()/(float)nPackages);
					devices.add(new DeviceSim((float)multiplier/(float)items.get(i).getValue()/(float)nPackages));					
				}
			}
			
			
			// Simulation
			int jobsLeft = nPackages;
			float time = 0, dtime = 0;
			float maxt = 0;
			while(jobsLeft > 0) {
				//System.out.println("Simulator in time " + time);
				float mint = 3133133;
				maxt = 0;
				
				for(DeviceSim ds : devices) {
					ds.tDone += dtime;
					if(ds.tDone >= ds.tNeeded) {
						ds.tDone = 0;
						jobsLeft--;
						//System.out.println("In time " + time + " job done by " + ds.tNeeded);
					}		
					mint = Math.min(mint, ds.tNeeded - ds.tDone + tStartup);
					maxt = Math.max(maxt, ds.tNeeded - ds.tDone + tStartup);
				}		
				if(jobsLeft > 0)
					dtime = mint;
				else dtime = maxt;
				time += dtime;
			}
			System.out.println((int)time);
			//System.out.println(packageMultiplier);
			//System.out.println( Math.round((5954.2/time)*100.0) /100.0);
			//System.out.println(limit+"");
			//System.out.println((int) ( (float)multiplier/(float)sumPerf));
		}		
	}

}
