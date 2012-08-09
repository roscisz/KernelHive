package pl.gda.pg.eti.kernelhive.engine;

import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

/**
 * Session Bean implementation class TimerBean
 */
@Singleton
@LocalBean
public class TimerBean {

    /**
     * Default constructor. 
     */
    public TimerBean() {
    	
    }
    
    @Schedule(second="*/5", minute="*", hour="*", persistent=false)
    public void cleanup() {
    	HiveEngine.getInstance().cleanup();    	
    }  
}
