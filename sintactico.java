import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

public class sintactico {
    //LISTAS DE TOKENS
    public static ArrayList<Integer> tokensPalabrasAsignacion = new ArrayList<Integer>(Arrays.asList(-11,-12,-13,-14));//tokens de palabras reservadas
    public static ArrayList<Integer> tokensIdentificadores = new ArrayList<Integer>(Arrays.asList(-51,-52,-53,-54, -55));//tokens de identificadores
    public static ArrayList<Integer> tokensConstantes = new ArrayList<Integer>(Arrays.asList(-61,-62,-63,-64, -65));//tokens de constantes

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
            numLexema = isBloque(listaTokens, 0);
            if(numLexema != 0){
                cuerpo = listaTokens.subList(0, numLexema+1);
                listaTokens = listaTokens.subList(numLexema+1, listaTokens.size());
                if(listaTokens.size() != 0){
                    mostrarErrorSintactico(listaTokens.get(numLexema+1).numLinea, "Se esperaba fin de archivo");
                    return false;
                }
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
                int i = 1;
                

                for(i = 1; i < listaTokens.size(); i++){         //Se recorre la lista de tokens en busca de cada declaracion
                    if(tokensPalabrasAsignacion.contains(listaTokens.get(i).token)){//int, float, string, bool
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

        public static int isBloque(List<lexemaObj> listaTokens, int i) {
            if(listaTokens.get(i).token == -2){//begin
                
                for(int index = i+1; index < listaTokens.size(); index++){
                    if(listaTokens.get(index).token == -3){//end
                        return index;
                    }else if(listaTokens.get(index).token == -4){//read
                        index = getReadOrWrite(listaTokens, index+1);
                    }else if(listaTokens.get(index).token == -5){//write
                        index = getReadOrWrite(listaTokens, index+1);
                    }else if(listaTokens.get(index).token == -6){//if
                        index = getIf(listaTokens, index+1);
                    }else if(listaTokens.get(index).token == -8){//while
                        index = getWhile(listaTokens, index+1);
                    }else if(listaTokens.get(index).token == -9){//repeat
                        index = getRepeat(listaTokens, index+1);
                    }else if(tokensIdentificadores.contains(listaTokens.get(index).token)){//identificador
                        index = getAsignacion(listaTokens, index+1);
                    }else{
                        mostrarErrorSintactico(listaTokens.get(index).numLinea, "Se esperaba palabra reservada 'End', 'Read', 'Write', 'If', 'While', 'Repeat' o identificador");
                        return 0;
                    }

                    if(index == 0){
                        return 0;
                    }
                }
            }else{
                mostrarErrorSintactico(listaTokens.get(i).numLinea, "Se esperaba palabra reservada 'Begin'");
                return 0;
            }
            mostrarErrorSintactico(listaTokens.get(i).numLinea, "Se esperaba palabra reservada 'End'");
            return 0;
            
        }

    //FUNCIONES EN BASE A DIAGRAMAS
        //Virgilio
        public static int getExpresion(List<lexemaObj> listaTokens, int index){
            int token = listaTokens.get(index).token;

            // Verificar si el token en el índice es uno de los operadores especificados
            if (token == -21 || token == -22 || token == -24 || token == -25 || 
                token == -26 || token == -31 || token == -32 || token == -33 || 
                token == -34 || token == -35 || token == -36 || token == -41 || 
                token == -42 || token == -43) {
                return index + 1; // Avanzar al siguiente token
            } else {
                // Mostrar mensaje de error si el token no es uno de los operadores esperados
                mostrarErrorSintactico(listaTokens.get(index).numLinea, "Se esperaba uno de los operadores permitidos");
                return 0;
            }
        }

        public static int getRepeat(List<lexemaObj> listaTokens, int index){
            index = isBloque(listaTokens, index);
            if(index == 0){
                return 0;
            }
            if(listaTokens.get(index+1).token != -10){//until
                mostrarErrorSintactico(listaTokens.get(index+1).numLinea, "Se esperaba palabra resevada 'until'");
                return 0;
            }
            if(listaTokens.get(index+2).token != -73){//(
                mostrarErrorSintactico(listaTokens.get(index+2).numLinea, "Se esperaba '('");
                return 0;
            }
            index = getExpresion(listaTokens, index+3);
            if(index == 0){
                return 0;
            }
            if(listaTokens.get(index+1).token != -74){//)
                mostrarErrorSintactico(listaTokens.get(index+1).numLinea, "Se esperaba ')'");
                return 0;
            }
            
            if(listaTokens.get(index+2).token != -75){//;
                mostrarErrorSintactico(listaTokens.get(index+1).numLinea, "Se esperaba ;");
                return 0;
            }
            return index+2;
        }
            

        //Jhoana
        public static int getIf(List<lexemaObj> listaTokens, int index){
            if(listaTokens.get(index).token == -73){//(
                index = getExpresion(listaTokens, index+1);
                if(index == 0){
                    return 0;
                }
                if(listaTokens.get(index+1).token != -74){//)
                    mostrarErrorSintactico(listaTokens.get(index+1).numLinea, "Se esperaba ')'");
                    return 0;
                }
                if(listaTokens.get(index+2).token != -16){//then
                    mostrarErrorSintactico(listaTokens.get(index+2).numLinea, "Se esperaba palabra resevada 'then'");
                    return 0;
                }
                index = isBloque(listaTokens, index+3);
                if(index == 0){
                    return 0;
                }
                if(listaTokens.get(index+1).token == -7){//else
                    index = isBloque(listaTokens, index+2);
                    if(index == 0){
                        return 0;
                    }
                }
                return index;
            }
            mostrarErrorSintactico(listaTokens.get(index).numLinea, "Se esperaba '('");
            return 0;
        }

        //Alan
        public static int getWhile(List<lexemaObj> listaTokens, int index){
            if(listaTokens.get(index).token == -73){//(
                index = getExpresion(listaTokens, index+1);
                if(index == 0){
                    return 0;
                }
                if(listaTokens.get(index+1).token != -74){//)
                    mostrarErrorSintactico(listaTokens.get(index+1).numLinea, "Se esperaba ')'");
                    return 0;
                }
                if(listaTokens.get(index+2).token != -17){//do
                    mostrarErrorSintactico(listaTokens.get(index+2).numLinea, "Se esperaba palabra resevada 'do'");
                    return 0;
                }
                index = isBloque(listaTokens, index+3);
                if(index == 0){
                    return 0;
                }
                return index;
            }
            return 0;
        }

        public static int getAsignacion(List<lexemaObj> listaTokens, int index){
            if(listaTokens.get(index).token == -26){//:=
                index = getExpresion(listaTokens, index+1);
                if(index == 0){
                    return 0;
                }
                if(listaTokens.get(index+1).token == -75){//;
                    return index+1;
                }
                mostrarErrorSintactico(index, "Se esperaba ';'");
                return 0;
                
            }
            mostrarErrorSintactico(listaTokens.get(index).numLinea, "Se esperaba :=");
            return 0;
        }

        public static int getReadOrWrite(List<lexemaObj> listaTokens, int index){
            if(listaTokens.get(index).token == -73){//(
                if(tokensIdentificadores.contains(listaTokens.get(index+1).token) || tokensConstantes.contains(listaTokens.get(index+1).token)){//identificador o constante
                    if(listaTokens.get(index+2).token == -74){ //)
                        if(listaTokens.get(index+3).token == -75){//;
                            return index+3;
                        }
                        mostrarErrorSintactico(listaTokens.get(index+3).numLinea, "Se esperaba ;");
                        return 0;
                    }
                    mostrarErrorSintactico(listaTokens.get(index+2).numLinea, "Se esperaba ')'");
                    return 0;
                }
                mostrarErrorSintactico(listaTokens.get(index).numLinea, "Se esperaba identificador o constante");
                return 0;
            }
            mostrarErrorSintactico(listaTokens.get(index).numLinea, "Se esperaba '('");
            return 0;
        }

        public static int getIndexVariablesDeclaradas(List<lexemaObj> listaTokens, int index){

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


