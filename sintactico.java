import java.util.List;

public class sintactico {
    //FUNCION PRINCIPAL
    public static boolean isSintactico(List<lexemaObj> listaTokens) {
        if (isEncabezado() && isDecalracion() && isCuerpo()){
            return true;
        }
        return false;
        
    }

    //FUNCION POR SECCION(Encabezado, declaracion variables, cuerpo)
    public static boolean isEncabezado() {
        return true;
    }

    public static boolean isDecalracion() {
        return true;
    }

    public static boolean isCuerpo() {
        return true;
    }

    //FUNCIONES EN BASE A DIAGRAMAS
    //Virgilio


    //Jhoana


    //Alan

}


