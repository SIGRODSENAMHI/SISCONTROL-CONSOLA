package pe.gob.senamhi.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.quartz.impl.StdSchedulerFactory;

import pe.gob.senamhi.enums.QuartzJobEnum;
import pe.gob.senamhi.model.QuartzModel;
import pe.gob.senamhi.util.Constantes;
import pe.gob.senamhi.util.QuartzUtil;

public class QuartzListener extends QuartzInitializerListener implements ServletContextListener {
	Scheduler scheduler = null;
	private static final Logger LOGGER = Logger.getLogger(QuartzListener.class);
    public QuartzListener() {
    }

	public void contextDestroyed(ServletContextEvent arg0) {
         try {
        	 scheduler.shutdown();
         } catch (SchedulerException e) {
        	 e.printStackTrace();
         }
    }

	public void contextInitialized(ServletContextEvent sce) {
    	super.contextInitialized(sce);
        try {
        	ServletContext ctx = sce.getServletContext();
        	StdSchedulerFactory factory = (StdSchedulerFactory) ctx.getAttribute(QUARTZ_FACTORY_KEY);
            scheduler = factory.getScheduler();
            scheduler.start();
            
            QuartzModel quartzModel = new QuartzModel();
            
            for (QuartzJobEnum quartzJobEnum : QuartzJobEnum.values()) {
            	if(quartzJobEnum.getQuartzBean().getJobControl() == Constantes.QUARTZ_START){
            		
            		quartzModel.liberarJob(quartzJobEnum.codigo());
            		QuartzUtil.registrarTrigger(scheduler, quartzJobEnum);
            		   
            	}
			}
        } catch (SchedulerException e) {
                e.printStackTrace();
        } catch(Exception e){
        	LOGGER.error("Registrar Exception " + e.getMessage());
        	e.printStackTrace();
        }
    }
	
}
