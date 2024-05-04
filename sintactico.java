import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

public class sintactico {
    //FUNCION PRINCIPAL
    //La lista de tokens principal se dividira en 3, encabezado, declaracion y cuerpo, si su tamaño es mayor a 0 significa que la
    //estructura es correcta
    public static boolean isSintactico(List<lexemaObj> listaTokens) {
        int numLexema = 0;
        List<lexemaObj> encabezado  = new ArrayList<>();
        List<lexemaObj> declaracion  = new ArrayList<>();
        List<lexemaObj> cuerpo  = new ArrayList<>();

        numLexema = isEncabezado(listaTokens);//Regresa el indice en la lista de tokens donde termina el encabezado
        if(numLexema != 0){//debe ser diferente de 0, si no es asi significa que hubo un error o que no existe la seccion
            encabezado = listaTokens.subList(0, numLexema);//Se guarda el encabezado de inicio a fin de este
            listaTokens = listaTokens.subList(numLexema, listaTokens.size());//Se elimina el encabezado de la lista de tokens
        }

        if(encabezado.size() != 0){//Si el encabezado existe podemos continuar buscando la declaracion de variables
            numLexema = isDeclaracion(listaTokens);//Se usa la nueva lista de tokens actualizada
            if(numLexema != 0){
                declaracion = listaTokens.subList(0, numLexema);
                listaTokens = listaTokens.subList(numLexema, listaTokens.size());
            }
        }

        if(declaracion.size() != 0){
            numLexema = isCuerpo(listaTokens);
            if(numLexema != 0){
                cuerpo = listaTokens.subList(0, numLexema);
                listaTokens = listaTokens.subList(numLexema, listaTokens.size());
            }
        }

        //Si las 3 secciones fueron encontradas sus tamaños deben ser diferente de 0 y la estructura es correcta
        if(encabezado.size() != 0 && declaracion.size() != 0 && cuerpo.size() != 0){
            return true;
        }

        return false;//Si alguna seccion no fue encontrada o su tamaño es 0 la estructura es incorrecta
    }

    //FUNCIONES POR SECCION(Encabezado, declaracion variables, cuerpo)

        //Regresa el indice en la lista de tokens donde termina el encabezado
        public static int isEncabezado(List<lexemaObj> listaTokens) {
            if(listaTokens.get(0).token == -1){ //Program
                if(listaTokens.get(1).token == -55){ //Identificador general termina en ?
                    if(listaTokens.get(2).token == -75){ //;
                        return 3;
                    }
                    mostrarErrorSintactico(listaTokens.get(2).numLinea, "Se esperaba ;");
                }
                mostrarErrorSintactico(listaTokens.get(1).numLinea, "Se esperaba identificador de programa");
            }
            mostrarErrorSintactico(listaTokens.get(0).numLinea, "Se esperaba palabra reservada program");
            return 0;
        }

        //Regresa el indice en la lista de tokens donde termina la declaracion de variables, si es incorrecta la estrucutura o hay error regresa 0
        public static int isDeclaracion(List<lexemaObj> listaTokens) {
            if(listaTokens.get(0).token == -15){//var
                
                ArrayList<Integer> tokensPalabrasReservadas = new ArrayList<Integer>(Arrays.asList(-11,-12,-13,-14));//tokens de palabras reservadas
                int i = 1;//se inicia en 1 porque el token 0 es la palabra reservada var

                for(i = 1; i < listaTokens.size(); i++){         //Se recorre la lista de tokens en busca de cada declaracion
                    if(tokensPalabrasReservadas.contains(listaTokens.get(i).token)){//int, float, string, bool
                        if(listaTokens.get(i+1).token == -77){      //:
                            i = getIndexVariablesDeclaradas(listaTokens, i+2);//Regresa el indice donde termina la declaracion para el tipo de variable 
                            
                            if(i == 0){
                                return 0;//si getIndexVariablesDeclaradas regresa 0 significa que hubo un error y se sale de la funcion
                            }
                        }else{
                            mostrarErrorSintactico(listaTokens.get(i).numLinea, "Se esperaba ':'");
                            return 0;
                        }
                    }else{//si no se encuentra una palabra reservada se termina la declaracion de variables
                        return i;
                    } 
                }
                return i;//la seccion puede solo contener la palabra var y no tener declaraciones   i = 1
            }
            mostrarErrorSintactico(listaTokens.get(0).numLinea, "Se esperaba palabra reservada var");
            return 0;
        }

        public static int isCuerpo(List<lexemaObj> listaTokens) {
            return 0;
        }

    //FUNCIONES EN BASE A DIAGRAMAS
        //Virgilio


        //Jhoana


        //Alan
        public static int getIndexVariablesDeclaradas(List<lexemaObj> listaTokens, int index){
            ArrayList<Integer> tokensIdentificadores = new ArrayList<Integer>(Arrays.asList(-51,-52,-53,-54, -55));//tokens de identificadores

            if(tokensIdentificadores.contains(listaTokens.get(index).token)){//identificador de variable
                if(listaTokens.get(index+1).token == -75){//;
                    return index+1;
                }else if(listaTokens.get(index+1).token == -76){//,
                    return getIndexVariablesDeclaradas(listaTokens, index+2);//Se busca el siguiente identificador de variable
                }else{
                    mostrarErrorSintactico(listaTokens.get(index).numLinea, "se esperaba ; o ,");
                    return 0;
                }
            }
            mostrarErrorSintactico(listaTokens.get(index).numLinea, "se esperaba identificador de variable");
            return 0;
        }

    //FUNCIONES GENERALES
    public static void mostrarErrorSintactico(int numLinea, String mensaje){
        JOptionPane.showMessageDialog(null, "Error sintactico en linea " + numLinea + ": " + mensaje);
    }
}


