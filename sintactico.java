import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

public class sintactico {
    // LISTAS DE TOKENS
    public static ArrayList<Integer> tokensPalabrasAsignacion = new ArrayList<Integer>(
            Arrays.asList(-11, -12, -13, -14));// tokens de palabras reservadas
    public static ArrayList<Integer> tokensIdentificadores = new ArrayList<Integer>(
            Arrays.asList(-51, -52, -53, -54, -55));// tokens de identificadores
    public static ArrayList<Integer> tokensConstantes = new ArrayList<Integer>(Arrays.asList(-61, -62, -63, -64, -65));// tokens de constantes
    public static ArrayList<Integer> tokensOperadores = new ArrayList<Integer>(
            Arrays.asList(-21, -22, -24, -25, -26, -31, -32, -33, -34, -35, -36, -41, -42, -43));// tokens de operadores                                                                                                                      

    // FUNCION PRINCIPAL
    // La lista de tokens principal se dividira en 3, encabezado, declaracion y
    // cuerpo, si su tamaño es mayor a 0 significa que la
    // estructura es correcta
    public static boolean isSintactico(List<lexemaObj> listaTokens) {
        try{
            int index = 0;
            
            index = isEncabezado(listaTokens);// Regresa el indice en la lista de tokens donde termina el encabezado

            if (index != 0) {// Si el encabezado existe podemos continuar buscando la declaracion de
                index = isDeclaracion(listaTokens, index);// Se usa la nueva lista de tokens actualizada
            }

            if (index != 0) {
                index = isBloque(listaTokens, index);
                if (index != 0) {
                    listaTokens = listaTokens.subList(index + 1, listaTokens.size());
                    if (listaTokens.size() != 0) {
                        mostrarErrorSintactico(listaTokens.get(0).numLinea, "Se esperaba fin de archivo");
                        return false;
                    }
                }
            }

            // Si las 3 secciones fueron encontradas sus tamaños deben ser diferente de 0 y
            // la estructura es correcta
            if (index != 0) {
                return true;
            }

            return false;// Si alguna seccion no fue encontrada o su tamaño es 0 la estructura es
                        // incorrecta
        }catch(Exception e){
            mostrarErrorSintactico("Error en la función isSintactico(): " + e.getMessage());
            return false;
        }
    }

    // FUNCIONES POR SECCION(Encabezado, declaracion variables, cuerpo)

    // Regresa el indice en la lista de tokens donde termina el encabezado
    public static int isEncabezado(List<lexemaObj> listaTokens) {
        try {
            int numLinea = listaTokens.get(0).numLinea;
            if (listaTokens.get(0).token == -1) { // Program
                if (listaTokens.get(1).token == -55) { // Identificador general termina en ?
                    if (listaTokens.get(2).token == -75) { // ;
                        return 3;
                    }
                    mostrarErrorSintactico(numLinea, "Se esperaba ;");
                    return 0;
                }
                mostrarErrorSintactico(numLinea, "Se esperaba identificador de programa");
                return 0;
            }
            mostrarErrorSintactico(numLinea, "Se esperaba palabra reservada program");
            return 0;
        } catch (IndexOutOfBoundsException e) {
            mostrarErrorSintactico("Faltan componentes en encabezado");
            return 0;
        }catch(Exception e){
            mostrarErrorSintactico("Error en la función isEncabezado(): " + e.getMessage());
            return 0;
        }
        
    }

    // Regresa el indice en la lista de tokens donde termina la declaracion de
    // variables, si es incorrecta la estrucutura o hay error regresa 0
    public static int isDeclaracion(List<lexemaObj> listaTokens, int index) {
        try {
            if (listaTokens.get(index).token == -15) {// var
                int i = index+1;
    
                for (i = index+1; i < listaTokens.size(); i++) { // Se recorre la lista de tokens en busca de cada declaracion
                    if (tokensPalabrasAsignacion.contains(listaTokens.get(i).token)) {// int, float, string, bool
                        if (listaTokens.get(i + 1).token == -77) { // :
                            i = getIndexVariablesDeclaradas(listaTokens, i + 2);// Regresa el indice donde termina la
                                                                                // declaracion para el tipo de variable
    
                            if (i == 0) {
                                return 0;// si getIndexVariablesDeclaradas regresa 0 significa que hubo un error y se
                                         // sale de la funcion
                            }
                        } else {
                            mostrarErrorSintactico(listaTokens.get(i).numLinea, "Se esperaba ':'");
                            return 0;
                        }
                    } else {// si no se encuentra una palabra reservada se termina la declaracion de
                            // variables
                        return i;
                    }
                }
                return i;// la seccion puede solo contener la palabra var y no tener declaraciones i = 1
            }
            mostrarErrorSintactico(listaTokens.get(index).numLinea, "Se esperaba palabra reservada var");
            return 0;
        }catch(IndexOutOfBoundsException e){
            mostrarErrorSintactico("Faltan componentes en declaracion de variables");
            return 0;
        }
        catch (Exception e) {
            mostrarErrorSintactico("Error en la función isDeclaracion(): " + e.getMessage());
            return 0;
        }
        
    }

    // Regresa el indice en la lista de tokens donde termina el bloque, si es
    // incorrecta la estrucutura o hay error regresa 0
    public static int isBloque(List<lexemaObj> listaTokens, int i) {
        try {
            if (listaTokens.get(i).token == -2) {// begin
                
                for (int index = i + 1; index < listaTokens.size(); index++) {
                    if (listaTokens.get(index).token == -3) {// end
                        return index;
                    } else if (listaTokens.get(index).token == -4) {// read
                        index = getReadOrWrite(listaTokens, index + 1);
                    } else if (listaTokens.get(index).token == -5) {// write
                        index = getReadOrWrite(listaTokens, index + 1);
                    } else if (listaTokens.get(index).token == -6) {// if
                        index = getIf(listaTokens, index + 1);
                    } else if (listaTokens.get(index).token == -8) {// while
                        index = getWhile(listaTokens, index + 1);
                    } else if (listaTokens.get(index).token == -9) {// repeat
                        index = getRepeat(listaTokens, index + 1);
                    } else if (tokensIdentificadores.contains(listaTokens.get(index).token)) {// identificador
                        index = getAsignacion(listaTokens, index + 1);
                    } else {
                        mostrarErrorSintactico(listaTokens.get(index).numLinea,
                                "Se esperaba palabra reservada 'End', 'Read', 'Write', 'If', 'While', 'Repeat' o identificador");
                        return 0;
                    }
    
                    if (index == 0) {
                        return 0;
                    }
                }
            } else {
                mostrarErrorSintactico(listaTokens.get(i).numLinea, "Se esperaba palabra reservada 'Begin'");
                return 0;
            }
            mostrarErrorSintactico(listaTokens.get(i).numLinea, "Se esperaba palabra reservada 'End'");
            return 0;
        } catch(IndexOutOfBoundsException e){
            mostrarErrorSintactico("Faltan componentes en bloque");
            return 0;
        }
        catch (Exception e) {
            mostrarErrorSintactico("Error en la función isBloque(): " + e.getMessage());
            return 0;
        }
    }

    // FUNCIONES EN BASE A DIAGRAMAS

    // VIRGILIO

    // Regresa el indice en la lista de tokens donde termina la estructura repeat, si
    // es incorrecta la estrucutura o hay error regresa 0
    public static int getRepeat(List<lexemaObj> listaTokens, int index) {
        try {
            index = isBloque(listaTokens, index);
            if (index == 0) {
                return 0;
            }
            if (listaTokens.get(index + 1).token != -10) {// until
                mostrarErrorSintactico(listaTokens.get(index + 1).numLinea, "Se esperaba palabra resevada 'until'");
                return 0;
            }
            if (listaTokens.get(index + 2).token != -73) {// (
                mostrarErrorSintactico(listaTokens.get(index + 2).numLinea, "Se esperaba '('");
                return 0;
            }
            index = getExpresion(listaTokens, index + 3);
            if (index == 0) {
                return 0;
            }
            if (listaTokens.get(index + 1).token != -74) {// )
                mostrarErrorSintactico(listaTokens.get(index + 1).numLinea, "Se esperaba ')'");
                return 0;
            }

            if (listaTokens.get(index + 2).token != -75) {// ;
                mostrarErrorSintactico(listaTokens.get(index + 1).numLinea, "Se esperaba ;");
                return 0;
            }
            return index + 2;
        }catch(IndexOutOfBoundsException e){
            mostrarErrorSintactico("Faltan componentes en estructura repeat");
            return 0;
        }catch (Exception e) {
            mostrarErrorSintactico("Error en la función getRepeat(): " + e.getMessage());
            return 0;
        }
    }

    // Regresa el indice en la lista de tokens donde terminan las variables declaradas para un tipo de dato
    // en la seccion de encabezado, si es incorrecta la estrucutura o hay error regresa 0
    public static int getIndexVariablesDeclaradas(List<lexemaObj> listaTokens, int index) {
        try {
            if (tokensIdentificadores.contains(listaTokens.get(index).token)) {// identificador de variable
                if (listaTokens.get(index + 1).token == -75) {// ;
                    return index + 1;
                } else if (listaTokens.get(index + 1).token == -76) {// ,
                    return getIndexVariablesDeclaradas(listaTokens, index + 2);// Se busca el siguiente identificador de
                                                                               // variable
                } else {
                    mostrarErrorSintactico(listaTokens.get(index).numLinea, "se esperaba ; o ,");
                    return 0;
                }
            }
            mostrarErrorSintactico(listaTokens.get(index).numLinea, "se esperaba identificador de variable");
            return 0;
        }catch(IndexOutOfBoundsException e){
            mostrarErrorSintactico("Faltan componentes en declaracion de variables");
            return 0;
        }catch (Exception e) {
            mostrarErrorSintactico("Error en la función getIndexVariablesDeclaradas(): " + e.getMessage());
            return 0;
        }
    }

    // JHOANA
    // Regresa el indice en la lista de tokens donde termina la estructura if, si es
    // incorrecta la estrucutura o hay error regresa 0
    public static int getIf(List<lexemaObj> listaTokens, int index) {
        try {
            if (listaTokens.get(index).token == -73) {// (
                index = getExpresion(listaTokens, index + 1);
                if (index == 0) {
                    return 0;
                }
                if (listaTokens.get(index + 1).token != -74) {// )
                    mostrarErrorSintactico(listaTokens.get(index + 1).numLinea, "Se esperaba ')'");
                    return 0;
                }
                if (listaTokens.get(index + 2).token != -16) {// then
                    mostrarErrorSintactico(listaTokens.get(index + 2).numLinea, "Se esperaba palabra resevada 'then'");
                    return 0;
                }
                index = isBloque(listaTokens, index + 3);
                if (index == 0) {
                    return 0;
                }
                if (listaTokens.get(index + 1).token == -7) {// else
                    index = isBloque(listaTokens, index + 2);
                    if (index == 0) {
                        return 0;
                    }
                }
                return index;
            }
            mostrarErrorSintactico(listaTokens.get(index).numLinea, "Se esperaba '('");
            return 0;
        }catch(IndexOutOfBoundsException e){
            mostrarErrorSintactico("Faltan componentes en estructura if");
            return 0;
        }catch (Exception e) {
            mostrarErrorSintactico("Error en la función getIf(): " + e.getMessage());
            return 0;
        }
    }

    // Regresa el indice en la lista de tokens donde termina la estructura while, si es
    // incorrecta la estrucutura o hay error regresa 0
    public static int getWhile(List<lexemaObj> listaTokens, int index) {
        try {
            if (listaTokens.get(index).token == -73) {// (
                index = getExpresion(listaTokens, index + 1);
                if (index == 0) {
                    return 0;
                }
                if (listaTokens.get(index + 1).token != -74) {// )
                    mostrarErrorSintactico(listaTokens.get(index + 1).numLinea, "Se esperaba ')'");
                    return 0;
                }
                if (listaTokens.get(index + 2).token != -17) {// do
                    mostrarErrorSintactico(listaTokens.get(index + 2).numLinea, "Se esperaba palabra resevada 'do'");
                    return 0;
                }
                index = isBloque(listaTokens, index + 3);
                if (index == 0) {
                    return 0;
                }
                return index;
            }
            return 0;
        }catch(IndexOutOfBoundsException e){
            mostrarErrorSintactico("Faltan componentes en estructura while");
            return 0;
        }catch (Exception e) {
            mostrarErrorSintactico("Error en la función getWhile(): " + e.getMessage());
            return 0;
        }
    }

    // Regresa el indice en la lista de tokens donde termina la asignacion, si es
    // incorrecta la estrucutura o hay error regresa 0
    public static int getAsignacion(List<lexemaObj> listaTokens, int index) {
        try {
            if (listaTokens.get(index).token == -26) {// :=
                index = getExpresion(listaTokens, index + 1);
                if (index == 0) {
                    return 0;
                }
                if (listaTokens.get(index + 1).token == -75) {// ;
                    return index + 1;
                }
                mostrarErrorSintactico(listaTokens.get(index).numLinea, "Se esperaba ';'");
                return 0;
    
            }
            mostrarErrorSintactico(listaTokens.get(index).numLinea, "Se esperaba :=");
            return 0;
        }catch(IndexOutOfBoundsException e){
            mostrarErrorSintactico("Faltan componentes en asignacion");
            return 0;
        }catch (Exception e) {
            mostrarErrorSintactico("Error en la función getAsignacion(): " + e.getMessage());
            return 0;
        }
    }

    // ALAN

    // Regresa el indice en la lista de tokens donde termina la expresion, si es
    // incorrecta la estrucutura o hay error regresa 0
    public static int getExpresion(List<lexemaObj> listaTokens, int index) {
        try {
            if(tokensIdentificadores.contains(listaTokens.get(index).token) || tokensConstantes.contains(listaTokens.get(index).token)) {// identificador o constante
                if(tokensOperadores.contains(listaTokens.get(index+1).token)) {// operador
                    return getExpresion(listaTokens, index + 2);// Se busca la siguiente expresion
                }
                return index;// si no hay operador se termina la expresion
            }else if(listaTokens.get(index).token == -73){//(
                index = getExpresion(listaTokens, index + 1);// Se busca la siguiente expresion
                if(index == 0){
                    return 0;
                }
                if(listaTokens.get(index + 1).token == -74){//)
                    if(tokensOperadores.contains(listaTokens.get(index+2).token)){// operador
                        return getExpresion(listaTokens, index + 3);// Se busca la siguiente expresion
                    }
                    return index+1;// si no hay operador se termina la expresion
                }
                mostrarErrorSintactico(listaTokens.get(index + 1).numLinea, "Se esperaba ')'");
                return 0;
            }
            mostrarErrorSintactico(listaTokens.get(index).numLinea, "Se esperaba identificador, constante o '('");
            return 0;
        }catch(IndexOutOfBoundsException e){
            mostrarErrorSintactico("Faltan componentes en expresion general");
            return 0;
        }
        catch (Exception e) {
            mostrarErrorSintactico("Error en la función getExpresion(): " + e.getMessage());
            return 0;
        }
    }

    // Regresa el indice en la lista de tokens donde termina la estructura read o
    // write, si es incorrecta la estrucutura o hay error regresa 0
    public static int getReadOrWrite(List<lexemaObj> listaTokens, int index) {
        try {
            if (listaTokens.get(index).token == -73) {// (
                if (tokensIdentificadores.contains(listaTokens.get(index + 1).token)
                        || tokensConstantes.contains(listaTokens.get(index + 1).token)) {// identificador o constante
                    if (listaTokens.get(index + 2).token == -74) { // )
                        if (listaTokens.get(index + 3).token == -75) {// ;
                            return index + 3;
                        }
                        mostrarErrorSintactico(listaTokens.get(index + 3).numLinea, "Se esperaba ;");
                        return 0;
                    }
                    mostrarErrorSintactico(listaTokens.get(index + 2).numLinea, "Se esperaba ')'");
                    return 0;
                }
                mostrarErrorSintactico(listaTokens.get(index).numLinea, "Se esperaba identificador o constante");
                return 0;
            }
            mostrarErrorSintactico(listaTokens.get(index).numLinea, "Se esperaba '('");
            return 0;
        } catch(IndexOutOfBoundsException e){
            mostrarErrorSintactico("Faltan componentes en estructura read o write");
            return 0;
        }catch (Exception e) {
            mostrarErrorSintactico("Error en la función getReadOrWrite(): " + e.getMessage());
            return 0;
        }
    }

    // FUNCIONES GENERALES
    public static void mostrarErrorSintactico(int numLinea, String mensaje) {
        JOptionPane.showMessageDialog(null, "Error sintactico en linea " + numLinea + ": " + mensaje, "Error Sintactico", JOptionPane.ERROR_MESSAGE);
    }
    public static void mostrarErrorSintactico(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error Sintactico", JOptionPane.ERROR_MESSAGE);
    }
}
