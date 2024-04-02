
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//AQUI SOLO DEBE HABER LAS FUNCIONES PARA IDENTIFICAR LEXEMAS, MAXIMO UNA FUNCION POR COMPONENTE LEXICO 
public class expresiones {
    // regresa un objeto lineaObj con los lexemas de la linea
    public static lineaObj getLineaObj(String linea, int lineNumber) {

        List<lexemaObj> lexemas = getLineaCommentString(linea, lineNumber);
        //AQUI ABAJO AGREGUEN SUS FUNCIONES, PUEDEN MOVERLAS A COMO SEA NECESARIO
        lexemas = getCaracterEsp(lexemas, lineNumber);
        lexemas = getIdentificadores(lexemas, lineNumber);
        //AQUI ARRIBA AGREGUEN SUS FUNCIONES
        lineaObj line = new lineaObj(lineNumber, lexemas);
        return line;            
    }

    //regresa una lista de lexemas con los comentarios y strings identificados
    public static List<lexemaObj> getLineaCommentString(String linea, int lineNumber) {
        List<lexemaObj> nuevaLinea = new ArrayList<>();
        String listaCadenas[] = linea.split(" ");
        for (int indexCadena = 0; indexCadena < listaCadenas.length; indexCadena++) {
            String cadena = listaCadenas[indexCadena];
            String strLexema;
            String nuevaCadena = "";
            boolean foundLexema = false;
            for (int indexCaracter = 0; indexCaracter < cadena.length(); indexCaracter++){
                char caracter = cadena.charAt(indexCaracter);
                if (caracter == '"') {
                    String cadenaTemp = cadena.substring(indexCaracter+1, cadena.length()); 
                    String[] listaTempCadenas = Arrays.copyOfRange(listaCadenas, indexCadena+1, listaCadenas.length);
                    strLexema = general.getString(cadenaTemp, listaTempCadenas);
                    if (strLexema != null) {
                        nuevaLinea.add(new lexemaObj(nuevaCadena, lineNumber));
                        nuevaLinea.add(new lexemaObj(strLexema, general.getCompLexicoValue("stringLinea"), -1, lineNumber));
                        foundLexema = true;
                        break;
                    }
                    nuevaCadena += caracter;
                }
                else if(caracter == '/' && 
                indexCaracter + 1 < cadena.length() && 
                cadena.charAt(indexCaracter + 1) == '/'){
                    String cadenaTemp = cadena.substring(indexCaracter+2, cadena.length());
                    String[] listaTempCadenas = Arrays.copyOfRange(listaCadenas, indexCadena+1, listaCadenas.length);
                    strLexema = general.getComment(cadenaTemp, listaTempCadenas);
                    if (strLexema != null) {
                        nuevaLinea.add(new lexemaObj(nuevaCadena, lineNumber));
                        nuevaLinea.add(new lexemaObj(strLexema, general.getCompLexicoValue("comentarioLinea"), -1, lineNumber));
                        foundLexema = true;
                        break;
                    }
                    nuevaCadena += caracter;
                }
                else {
                    nuevaCadena += caracter;
                }
            }
            if (foundLexema) {
                String spliter = nuevaLinea.get(nuevaLinea.size() - 1).valorCadena;
                String[] lineaDividida = linea.split(spliter, 2);
                if(lineaDividida.length > 1){
                    nuevaLinea.addAll(getLineaCommentString(lineaDividida[1], lineNumber));
                }
                break;
            }
            nuevaLinea.add(new lexemaObj(nuevaCadena, lineNumber));
        }
        nuevaLinea.removeIf(lexema -> lexema.valorCadena.isEmpty());

        
        return nuevaLinea;

    }

    //regresa una lista de lexemas con los caracteres especiales identificados
    public static List<lexemaObj> getCaracterEsp(List<lexemaObj> lexemas, int lineNumber){
        List<lexemaObj> nuevaLinea = new ArrayList<>();
        for(lexemaObj lexema: lexemas){
            if(lexema.token == 0){//si el token del lexema es 0, hay que intentar identificarlo
                String cadena = lexema.valorCadena; //llamamos y almacenamos el valor string de el lexema
                String nuevaCadena = "";
                for (int indexCaracter = 0; indexCaracter < cadena.length(); indexCaracter++){
                    char caracter = cadena.charAt(indexCaracter);
                    if(caracter == '(' || caracter == ')' || caracter == ';' || caracter == ',' || caracter == ':'){
                        String compLexico = Character.toString(caracter);
                        int token = general.getCompLexicoValue(compLexico);//obtenemos el token correspondiente al caracter, desde el archivo txt
                        if(!nuevaCadena.isEmpty()){
                            nuevaLinea.add(new lexemaObj(nuevaCadena, lineNumber));//los caracteres que no se identificaron se alamcenan como un nuevo lexema sin identificar
                        }
                        //agregamos el caracter identificado a la lista de lexemas
                        nuevaLinea.add(new lexemaObj(String.valueOf(caracter), token, -1, lineNumber));
                        nuevaCadena = "";//reiniciamos la variable para seguir almacenando caracteres
                    }
                    else{
                        nuevaCadena += caracter;//si no se identifico con los caracteres, se sigue creando la cadena
                    }
                }
                if(!nuevaCadena.isEmpty()){
                    //volvemos a almacenar la cadena como lexema sin identificar
                    nuevaLinea.add(new lexemaObj(nuevaCadena, lineNumber));
                }
            }else{
                //si el token es diferente de 0, no se le hace nada al lexema y solo se vuelve a agregar a la lista
                nuevaLinea.add(lexema);
            }
        }
        return nuevaLinea;
    }

    //regresa una lista de lexemas con los identificadores identificados
    public static List<lexemaObj> getIdentificadores(List<lexemaObj> lexemas, int lineNumber){
        List<lexemaObj> nuevaLinea = new ArrayList<>();
        String expresion_cadena = "^[a-zA-Z][a-zA-Z0-9_]*#$";
        String expresion_real = "^[a-zA-Z][a-zA-Z0-9_]*%$";
        String expresion_entero = "^[a-zA-Z][a-zA-Z0-9_]*&$";
        String expresion_logico = "^[a-zA-Z][a-zA-Z0-9_]*[$]$";
        String expresion_programa = "^[a-zA-Z][a-zA-Z0-9_]*[?]$";

        for(lexemaObj lexema: lexemas){
            //si el token del lexema es 0, hay que intentar identificarlo
            if(lexema.token == 0){
                String cadena = lexema.valorCadena;
                int token = 0;
                
                //se compara la cadena con las expresiones regulares para identificar la cadena
                if (cadena.matches(expresion_cadena)) {
                    token = general.getCompLexicoValue("iCadena");
                } else if (cadena.matches(expresion_entero)) {
                    token = general.getCompLexicoValue("iEntero");
                } else if (cadena.matches(expresion_logico)) {
                    token = general.getCompLexicoValue("iLogico");
                } else if (cadena.matches(expresion_programa)) {
                    token = general.getCompLexicoValue("iGral");
                } else if (cadena.matches(expresion_real)) {
                    token = general.getCompLexicoValue("iReal");
                } else{
                    //si no se identifica, se agrega a la lista como un lexema sin identificar
                    nuevaLinea.add(lexema);
                }
                
                if(token != 0){
                    nuevaLinea.add(new lexemaObj(cadena, token, -2, lineNumber));
                }
                
            }else{
                //si el token es diferente de 0, no se le hace nada al lexema y solo se vuelve a agregar a la lista
                nuevaLinea.add(lexema);
            }
        }
        return nuevaLinea;
    }

    public static String aritmetico(String sCadena) {
        // Operadores aritméticos
        String aritmetico = "\\*\\*|//|[+\\-%\\*\\/]"; // Expresión regular para identificar operadores aritméticos

        Pattern pattern = Pattern.compile(aritmetico);
        Matcher matcher = pattern.matcher(sCadena);

        StringBuilder operadores = new StringBuilder("\nOperadores aritmeticos encontrados: ");
        while (matcher.find()) {
            String operador = matcher.group();
            switch (operador) {
                case "+":
                    operadores.append("Suma ");
                    break;
                case "-":
                    operadores.append("Resta ");
                    break;
                case "*":
                    operadores.append("Multiplicacion ");
                    break;
                case "/":
                    operadores.append("Division ");
                    break;
                case "%":
                    operadores.append("Modulo ");
                    break;
                case "**":
                    operadores.append("Exponente ");
                    break;
                case "//":
                    operadores.append("Cociente ");
                    break;
            }
            operadores.append("(").append(operador).append(") ");
        }
        return operadores.toString();
    }

    public static String logico(String sCadena) {
        // Operadores lógicos
        String logico = "AND|OR|NOT"; // Expresión regular para identificar operadores lógicos

        Pattern pattern = Pattern.compile(logico);
        Matcher matcher = pattern.matcher(sCadena);

        StringBuilder operadores = new StringBuilder("\nOperadores logicos encontrados: ");
        while (matcher.find()) {
            String operador = matcher.group();
            switch (operador) {
                case "AND":
                    operadores.append("AND ");
                    break;
                case "OR":
                    operadores.append("OR ");
                    break;
                case "NOT":
                    operadores.append("NOT ");
                    break;
            }
            operadores.append("(").append(operador).append(") ");
        }
        return operadores.toString();
    }

    public static String comparativo(String sCadena) {
        // Operadores comparativos
        String comparativo = "[<>!]=?"; // Expresión regular para identificar operadores de comparación

        Pattern pattern = Pattern.compile(comparativo);
        Matcher matcher = pattern.matcher(sCadena);

        StringBuilder operadores = new StringBuilder("\nOperadores comparativos encontrados: ");
        while (matcher.find()) {
            String operador = matcher.group();
            switch (operador) {
                case ">":
                    operadores.append("Mayor que ");
                    break;
                case "<":
                    operadores.append("Menor que ");
                    break;
                case "==":
                    operadores.append("Igual ");
                    break;
                case ">=":
                    operadores.append("Mayor o igual que ");
                    break;
                case "<=":
                    operadores.append("Menor o igual que ");
                    break;
                case "!=":
                    operadores.append("Diferente ");
                    break;
            }
            operadores.append("(").append(operador).append(") ");
        }
        return operadores.toString();
    }


    public static String analizarNumeros(String sCadena) {
        // Expresión regular para identificar números del 0 al 9
        String regexNumeros = "-?\\d+(\\.\\d+)?[dlf]";

        if(Pattern.matches(regexNumeros, sCadena)){
            return "\nLa cadena es un numero en lenguaje C";
        }
        return "\nLa cadena NO es un numero en lenguaje C";
    }
}
