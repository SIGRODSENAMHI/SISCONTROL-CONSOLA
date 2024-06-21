package pe.gob.senamhi.util;

public class ValidarVariableUtil {

    final static String NIV_MEDIO = PropiedadesUtil.obtenerPropiedad("configuracion", "lista.estacion.medio");
    final static String NIV_INST_00 = PropiedadesUtil.obtenerPropiedad("configuracion", "lista.estacion.inst00");
    final static String NIV_INST_10 = PropiedadesUtil.obtenerPropiedad("configuracion", "lista.estacion.inst10");

    final static String[] ARR_ESTA_MEDIO = NIV_MEDIO.split("\\,");
    final static String[] ARR_ESTA_INST_00 = NIV_INST_00.split("\\,");
    final static String[] ARR_ESTA_INST_10 = NIV_INST_10.split("\\,");

    public static String obtenerNombreVariable(String codEstacion) {

        int val = 0;
        String varDinamica = "N_NIVELAGUA";

        for (int i = 0; i < ARR_ESTA_MEDIO.length; i++) {
            if (codEstacion.equals(ARR_ESTA_MEDIO[i])) {
                val = 1;
                varDinamica = "N_NIVELMEDIO";
                break;
            }
        }

        if (val == 0) {
            for (int i = 0; i < ARR_ESTA_INST_00.length; i++) {
                if(codEstacion.equals(ARR_ESTA_INST_00[i])) {
                    val = 1;
                    varDinamica = "N_NIV_INST_00";
                    break;
                }
            }
        }

        if(val == 0) {
            for (int i = 0; i < ARR_ESTA_INST_10.length; i++) {
                if(codEstacion.equals(ARR_ESTA_INST_10[i])) {
                    val = 1;
                    varDinamica = "N_NIV_INST_10";
                    break;
                }
            }
        }

        return varDinamica;
    }
}
