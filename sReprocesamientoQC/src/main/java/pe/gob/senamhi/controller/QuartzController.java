package pe.gob.senamhi.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

import pe.gob.senamhi.bean.QuartzBean;
import pe.gob.senamhi.enums.QuartzJobEnum;
import pe.gob.senamhi.listener.QuartzListener;
import pe.gob.senamhi.util.QuartzUtil;

public class QuartzController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(QuartzController.class);
	
    public QuartzController() {
        super();
    }

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = request.getServletPath();
		if(url.equals("/QuartzStop")){
			quartzStop(request, response);
		}else if(url.equals("/QuartzStart")){
			quartzStart(request, response);
		}else if(url.equals("/IrAControlQuartz")){
			irAControlQuartz(request, response);
		}
	}


	private void quartzStop(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		String rpta = "0";
		try {
			//DETENER FRECUENCIA DE TRIGGER DE JOB
			ServletContext ctx = this.getServletContext();
			StdSchedulerFactory factory = (StdSchedulerFactory) ctx.getAttribute(QuartzListener.QUARTZ_FACTORY_KEY);
			Scheduler scheduler = factory.getScheduler();
			
			//String codigo = request.getParameter("codigo");
			String enumName = request.getParameter("enumName");
//			LOGGER.info("codigo["+codigo+"] enumName["+enumName+"]");
			
			QuartzJobEnum quartzJobEnum = QuartzJobEnum.valueOf(enumName.trim());
			QuartzUtil.detenerTrigger(scheduler, quartzJobEnum);
			rpta = "1; Se detuvo el quartz...";
		} catch (Exception e) {
			rpta = "0;" + e.getMessage();
			LOGGER.error("Error QuartzController.quartzStop " + e.getMessage());
			e.printStackTrace();
		} finally {
			out.write(rpta);
			out.flush();
			out.close();
		}
	}
	
	private void quartzStart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		String rpta = "0";
		try {
			//EMPEZAR FRECUENCIA DE TRIGGER DE JOB
			ServletContext ctx = this.getServletContext();
			StdSchedulerFactory factory = (StdSchedulerFactory) ctx.getAttribute(QuartzListener.QUARTZ_FACTORY_KEY);
			Scheduler scheduler = factory.getScheduler();
			
			//String codigo = request.getParameter("codigo");
			String enumName = request.getParameter("enumName");
			//String frecuencia = request.getParameter("frecuencia");
//			LOGGER.info("codigo["+codigo+"] enumName["+enumName+"]");
			QuartzJobEnum quartzJobEnum = QuartzJobEnum.valueOf(enumName.trim());
			
			QuartzUtil.registrarTrigger(scheduler, quartzJobEnum);
			rpta = "1; Se actualizo el quartz...";
		} catch (Exception e) {
			rpta = "0;" + e.getMessage();
			LOGGER.error("Error QuartzController.quartzStart " + e.getMessage());
			e.printStackTrace();
		} finally {
			out.write(rpta);
			out.flush();
			out.close();
		}
	}
	
	private void irAControlQuartz(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String destino = "index.jsp";
		try {

			destino = "quartzTable.jsp";
			List<QuartzBean> list = new ArrayList<QuartzBean>();
			for (QuartzJobEnum quartzJobEnum : QuartzJobEnum.values()) {
//				LOGGER.info("QuartzJobEnum["+quartzJobEnum+"]");
				list.add(quartzJobEnum.getQuartzBean());
			}
			request.setAttribute("quartzList", list);
			
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", e.getMessage());
			destino = "view/vistError.jsp";
			LOGGER.error("Errorr QuartzController.irAControlQuartz " + e.getMessage());
		}
		RequestDispatcher rd = request.getRequestDispatcher(destino);
		rd.forward(request, response);
	}

}
