package pe.gob.senamhi.controller;

import java.util.List;

import pe.gob.senamhi.bean.DatosBean;
import pe.gob.senamhi.bean.FlagBean;
import pe.gob.senamhi.bean.UmbralBean;
import pe.gob.senamhi.bean.VariableBean;
import pe.gob.senamhi.dao.DatosDao;
import pe.gob.senamhi.dao.UmbralDao;
import pe.gob.senamhi.dao.VariableDao;
import pe.gob.senamhi.dao.impl.DatosDaoImpl;
import pe.gob.senamhi.dao.impl.UmbralDaoImpl;
import pe.gob.senamhi.dao.impl.VariableDaoImpl;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		VariableDao variableDao = new VariableDaoImpl();
//		DatosDao datosDao = new DatosDaoImpl();
//		UmbralDao umbralDao  = new UmbralDaoImpl();
//		
//		try {
//			List<VariableBean> lista = variableDao.listarVariables("212605");
//			DatosBean dato =datosDao.obtDatosVar("N_NIV_INST_00", "212605", "17/03/2022", "10:00:00");
//			UmbralBean umbral  = umbralDao.obtenerUmbrales("LNN", "N_NIVELAGUA", "212605", "NULL", "NULL", "NULL");
//			FlagBean flag = datosDao.obtCodigoFlag("LimitesNacional", "N_NIVELAGUA", "R1");
//			
//			System.out.println(lista.get(3).getNomvar() + " " + dato.getDato1() + " " + umbral.getValmin1() + " " 
//								+ umbral.getValmax1() + " " + flag.getCodFlag());
//			
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}
