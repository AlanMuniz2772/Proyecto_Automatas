
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

//AQUI SOLO DEBE HABER LAS FUNCIONES PARA IDENTIFICAR LEXEMAS, MAXIMO UNA FUNCION POR COMPONENTE LEXICO 
public class expresiones {
    // regresa un objeto lineaObj con los lexemas de la linea
    public static lineaObj getLineaObj(String linea, int lineNumber) {
        try {
            List<lexemaObj> lexemas = getLineaCommentString(linea, lineNumber);
            //AQUI ABAJO AGREGUEN SUS FUNCIONES, PUEDEN MOVERLAS A COMO SEA NECESARIO
            lexemas = getCaracterEsp(lexemas, lineNumber);
            lexemas = getOpAritmetico(lexemas, lineNumber);
            lexemas = getOpRelacional(lexemas, lineNumber);
            lexemas = getOpLogico(lexemas, lineNumber);
            lexemas = getNumReal(lexemas, lineNumber);
            lexemas = getNumEntero(lexemas, lineNumber);
            lexemas = deletePuntos(lexemas, lineNumber);
            lexemas = getIdentificadores(lexemas, lineNumber);
            lexemas = getValorLogico(lexemas, lineNumber);
            lexemas = getPalabrasReservadas(lexemas, lineNumber);
            //AQUI ARRIBA AGREGUEN SUS FUNCIONES
            lineaObj line = new lineaObj(lineNumber, lexemas);
            return line;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en la función lexica getLineaObj(): " + e.getMessage());
            return null;
        }
        
    }
    
    //NO LE MUEVAN A ESTA FUNCION, CUANDO LA HICE SOLO DIOS Y YO SABIAMOS COMO FUNCIONA, AHORA  
    //NO CREO QUE NI DIOS LO SEPA, SI MANDA ERROR REZEN PARA QUE NO SE ROMPA EL SISTEMA
    //regresa una lista de lexemas con los comentarios y strings identificados
    public static List<lexemaObj> getLineaCommentString(String linea, int lineNumber) {
        try {
        List<lexemaObj> nuevaLinea = new ArrayList<>();
        String listaCadenas[] = linea.split(" ");
        for (int indexCadena = 0; indexCadena < listaCadenas.length; indexCadena++) {
            String cadena = listaCadenas[indexCadena];
            String strLexema;
            String nuevaCadena = "";
            String contenido = "";
            boolean foundLexema = false;
            for (int indexCaracter = 0; indexCaracter < cadena.length(); indexCaracter++){
                char caracter = cadena.charAt(indexCaracter);
                if (caracter == '"') {
                    String cadenaTemp = cadena.substring(indexCaracter+1, cadena.length()); 
                    String[] listaTempCadenas = Arrays.copyOfRange(listaCadenas, indexCadena+1, listaCadenas.length);
                    strLexema = general.getString(cadenaTemp, listaTempCadenas);
                    if (strLexema != null) {
                        nuevaLinea.add(new lexemaObj(nuevaCadena, lineNumber));
                        nuevaCadena = "";
                        contenido = strLexema.substring(1, strLexema.length() - 1).trim();
                        if (!contenido.isEmpty()) {
                            nuevaLinea.add(new lexemaObj(strLexema, general.getCompLexicoValue("cCadena"), -1, lineNumber));
                            foundLexema = true;
                            break;
                        }
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
                        nuevaCadena = "";
                        contenido = strLexema.substring(2, strLexema.length() - 2).trim();
                        if (!contenido.isEmpty()) {
                            nuevaLinea.add(new lexemaObj(strLexema, -78, -1, lineNumber));
                            foundLexema = true;
                            break;
                        }
                    }
                    nuevaCadena += caracter;
                }
                else {
                    nuevaCadena += caracter;
                }
            }
            if (foundLexema) {
                lexemaObj lexemaUltimo = nuevaLinea.get(nuevaLinea.size() - 1);
                String spliter = "";
                if (lexemaUltimo.token == -78) {
                    spliter = lexemaUltimo.valorCadena;
                    String[] lineaDividida = linea.split(spliter, 2);
                    if(lineaDividida.length >= 2){
                        nuevaLinea.addAll(getLineaCommentString(lineaDividida[1], lineNumber));
                    }
                }else{
                    spliter = lexemaUltimo.valorCadena;
                    String[] lineaDividida = linea.split(spliter, 2);
                    if(lineaDividida.length > 1){
                        nuevaLinea.addAll(getLineaCommentString(lineaDividida[1], lineNumber));
                    }
                }
                
                break;
            }
            nuevaLinea.add(new lexemaObj(nuevaCadena, lineNumber));
        }
        nuevaLinea.removeIf(lexema -> lexema.valorCadena.isEmpty());
        nuevaLinea.removeIf(lexema -> lexema.token == -78);
        
        return nuevaLinea;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en la función lexica getLineaCommentString(): " + e.getMessage());
            return null;
        }
    }

    //regresa una lista de lexemas con los caracteres especiales identificados
    public static List<lexemaObj> getCaracterEsp(List<lexemaObj> lexemas, int lineNumber){
        try{
        List<lexemaObj> nuevaLinea = new ArrayList<>();
        for(lexemaObj lexema: lexemas){
            if(lexema.token == 0){//si el token del lexema es 0, hay que intentar identificarlo
                String cadena = lexema.valorCadena; //llamamos y almacenamos el valor string de el lexema
                String nuevaCadena = "";
                for (int indexCaracter = 0; indexCaracter < cadena.length(); indexCaracter++){
                    char caracter = cadena.charAt(indexCaracter);
                    if(caracter == '(' || caracter == ')' || caracter == ';' || caracter == ','){
                        String compLexico = Character.toString(caracter);
                        int token = general.getCompLexicoValue(compLexico);//obtenemos el token correspondiente al caracter, desde el archivo txt
                        if(!nuevaCadena.isEmpty()){
                            nuevaLinea.add(new lexemaObj(nuevaCadena, lineNumber));//los caracteres que no se identificaron se alamcenan como un nuevo lexema sin identificar
                        }
                        //agregamos el caracter identificado a la lista de lexemas
                        nuevaLinea.add(new lexemaObj(String.valueOf(caracter), token, -1, lineNumber));
                        nuevaCadena = "";//reiniciamos la variable para seguir almacenando caracteres
                    }else if(caracter == ':'){
                        if(indexCaracter + 1 < cadena.length() && cadena.charAt(indexCaracter + 1) == '='){
                            nuevaCadena += caracter;
                        }else{
                            String compLexico = Character.toString(caracter);
                            int token = general.getCompLexicoValue(compLexico);
                            if(!nuevaCadena.isEmpty()){
                                nuevaLinea.add(new lexemaObj(nuevaCadena, lineNumber));
                            }
                            nuevaLinea.add(new lexemaObj(compLexico, token, -1, lineNumber));
                            nuevaCadena = "";
                        }
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en la función lexica getCaracterEsp(): " + e.getMessage());
            return null;
        }
    }

    //regresa una lista de lexemas con los identificadores identificados
    public static List<lexemaObj> getIdentificadores(List<lexemaObj> lexemas, int lineNumber){
        try{
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en la función lexica getIdentificadores(): " + e.getMessage());
            return null;
        }
    }

    public static List<lexemaObj> getOpAritmetico(List<lexemaObj> lexemas, int lineNumber){
        try{
        List<lexemaObj> nuevaLinea = new ArrayList<>();
        for(lexemaObj lexema: lexemas){
            if(lexema.token == 0){
                String cadena = lexema.valorCadena;
                String nuevaCadena = "";
                for (int indexCaracter = 0; indexCaracter < cadena.length(); indexCaracter++){
                    char caracter = cadena.charAt(indexCaracter);
                    if(caracter == '+' || caracter == '*' || caracter == '/'){
                        String compLexico = Character.toString(caracter);
                        int token = general.getCompLexicoValue(compLexico);
                        if(!nuevaCadena.isEmpty()){
                            nuevaLinea.add(new lexemaObj(nuevaCadena, lineNumber));
                        }
                        nuevaLinea.add(new lexemaObj(String.valueOf(caracter), token, -1, lineNumber));
                        nuevaCadena = "";
                    }else if(caracter == '-'){
                        String cadenaTemp = cadena.substring(indexCaracter+1, cadena.length());
                        if(cadenaTemp.matches("\\d+")){
                            String compLexico = Character.toString(caracter);
                            int token = general.getCompLexicoValue(compLexico);
                            if(!nuevaCadena.isEmpty()){
                                nuevaLinea.add(new lexemaObj(nuevaCadena, lineNumber));
                            }
                            nuevaLinea.add(new lexemaObj(compLexico, token, -1, lineNumber));
                            nuevaCadena = "";
                            
                        }else if(cadenaTemp.matches("\\d+\\.\\d+")){
                            nuevaCadena += caracter;
                        }else{
                            
                            String compLexico = Character.toString(caracter);
                            int token = general.getCompLexicoValue(compLexico);
                            if(!nuevaCadena.isEmpty()){
                                nuevaLinea.add(new lexemaObj(nuevaCadena, lineNumber));
                            }
                            nuevaLinea.add(new lexemaObj(compLexico, token, -1, lineNumber));
                            nuevaCadena = "";
                            
                        }
                    }else if(caracter == ':'){
                        if(indexCaracter + 1 < cadena.length() && cadena.charAt(indexCaracter + 1) == '='){
                            String compLexico = ":=";
                            int token = general.getCompLexicoValue(compLexico);
                            if(!nuevaCadena.isEmpty()){
                                nuevaLinea.add(new lexemaObj(nuevaCadena, lineNumber));
                            }
                            nuevaLinea.add(new lexemaObj(compLexico, token, -1, lineNumber));
                            nuevaCadena = "";
                            indexCaracter++;
                        }else{
                            nuevaCadena += caracter;
                        }
                    }else{
                        nuevaCadena += caracter;
                    }
                }
                if(!nuevaCadena.isEmpty()){
                    //volvemos a almacenar la cadena como lexema sin identificar
                    nuevaLinea.add(new lexemaObj(nuevaCadena, lineNumber));
                }
            }else{
                nuevaLinea.add(lexema);
            }
        }
        return nuevaLinea;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en la función lexica getOpAritmetico(): " + e.getMessage());
            return null;
        }
    }

    public static List<lexemaObj> getOpRelacional(List<lexemaObj> lexemas, int lineNumber){
        try{
        List<lexemaObj> nuevaLinea = new ArrayList<>();
        for(lexemaObj lexema: lexemas){
            if(lexema.token == 0){
                String cadena = lexema.valorCadena;
                String nuevaCadena = "";
                for (int indexCaracter = 0; indexCaracter < cadena.length(); indexCaracter++){
                    char caracter = cadena.charAt(indexCaracter);
                    if(caracter == '<' || caracter == '>'){
                        if(!nuevaCadena.isEmpty()){
                            nuevaLinea.add(new lexemaObj(nuevaCadena, lineNumber));
                        }
                        nuevaCadena = "";
                        if(indexCaracter + 1 < cadena.length() && cadena.charAt(indexCaracter + 1) == '='){
                            String compLexico = Character.toString(caracter) + "=";
                            int token = general.getCompLexicoValue(compLexico);
                            nuevaLinea.add(new lexemaObj(compLexico, token, -1, lineNumber));
                            indexCaracter++;
                        }else{
                            String compLexico = Character.toString(caracter);
                            int token = general.getCompLexicoValue(compLexico);
                            nuevaLinea.add(new lexemaObj(compLexico, token, -1, lineNumber));
                        }
                    
                    }else if((caracter == '='|| caracter == '!') && indexCaracter + 1 < cadena.length() && cadena.charAt(indexCaracter + 1) == '='){
                        if(!nuevaCadena.isEmpty()){
                            nuevaLinea.add(new lexemaObj(nuevaCadena, lineNumber));
                        }
                        nuevaCadena = "";
                        String compLexico = Character.toString(caracter)+"=";
                        int token = general.getCompLexicoValue(compLexico);
                        nuevaLinea.add(new lexemaObj(compLexico, token, -1, lineNumber));
                        indexCaracter++;
                    }
                    else{
                        nuevaCadena += caracter;
                    }
                }
                if(!nuevaCadena.isEmpty()){
                    //volvemos a almacenar la cadena como lexema sin identificar
                    nuevaLinea.add(new lexemaObj(nuevaCadena, lineNumber));
                }
            }else{
                nuevaLinea.add(lexema);
            }
        }
        return nuevaLinea;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en la función lexica getOpRelacional(): " + e.getMessage());
            return null;
        }
    }

    public static List<lexemaObj> getOpLogico(List<lexemaObj> lexemas, int lineNumber){
        try{
        List<lexemaObj> nuevaLinea = new ArrayList<>();
        for(lexemaObj lexema: lexemas){
            if(lexema.token == 0){
                String cadena = lexema.valorCadena;
                String nuevaCadena = "";
                for (int indexCaracter = 0; indexCaracter < cadena.length(); indexCaracter++){
                    char caracter = cadena.charAt(indexCaracter);
                    if(caracter == '&' && indexCaracter + 1 < cadena.length() && cadena.charAt(indexCaracter + 1) == '&'){
                        if(nuevaCadena.matches("^[a-zA-Z][a-zA-Z0-9_]*")){
                            nuevaCadena += caracter;
                        }else{
                            if(!nuevaCadena.isEmpty()){
                                nuevaLinea.add(new lexemaObj(nuevaCadena, lineNumber));
                            }
                            nuevaCadena = "";
                            String compLexico = "&&";
                            int token = general.getCompLexicoValue(compLexico);
                            nuevaLinea.add(new lexemaObj(compLexico, token, -1, lineNumber));
                            indexCaracter++;
                        }
                           
                    }else if(caracter == '|' && indexCaracter + 1 < cadena.length() && cadena.charAt(indexCaracter + 1) == '|'){
                        if(!nuevaCadena.isEmpty()){
                            nuevaLinea.add(new lexemaObj(nuevaCadena, lineNumber));
                        }
                        nuevaCadena = "";
                        String compLexico = "||";
                        int token = general.getCompLexicoValue(compLexico);
                        nuevaLinea.add(new lexemaObj(compLexico, token, -1, lineNumber));
                        indexCaracter++;
                        
                    }else if(caracter == '!'){
                        if(indexCaracter + 1 < cadena.length() && cadena.charAt(indexCaracter + 1) == '='){
                            nuevaCadena += caracter;
                        }else{
                            if(!nuevaCadena.isEmpty()){
                                nuevaLinea.add(new lexemaObj(nuevaCadena, lineNumber));
                            }
                            nuevaCadena = "";
                            String compLexico = Character.toString(caracter);
                            int token = general.getCompLexicoValue(compLexico);
                            nuevaLinea.add(new lexemaObj(compLexico, token, -1, lineNumber));
                        }
                    }
                    else{
                        nuevaCadena += caracter;
                    }
                }
                if(!nuevaCadena.isEmpty()){
                    //volvemos a almacenar la cadena como lexema sin identificar
                    nuevaLinea.add(new lexemaObj(nuevaCadena, lineNumber));
                }
            }else{
                nuevaLinea.add(lexema);
            }
        }
        return nuevaLinea;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en la función lexica getOpLogico(): " + e.getMessage());
            return null;
        }
    }

    public static List<lexemaObj> getNumEntero(List<lexemaObj> lexemas, int lineNumber){
        try{
        List<lexemaObj> nuevaLinea = new ArrayList<>();
        for(lexemaObj lexema: lexemas){
            if(lexema.token == 0){
                String cadena = lexema.valorCadena;
                if (cadena.matches("\\d+")) {
                    int token = general.getCompLexicoValue("cEntero");
                    nuevaLinea.add(new lexemaObj(cadena, token, -1, lineNumber));
                }else{
                    nuevaLinea.add(lexema);
                }
            }else{
                nuevaLinea.add(lexema);
            }
        }
        return nuevaLinea;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en la función lexica getNumEntero(): " + e.getMessage());
            return null;
        }
    }

    public static List<lexemaObj> getNumReal(List<lexemaObj> lexemas, int lineNumber){
        try{
        List<lexemaObj> nuevaLinea = new ArrayList<>();
        for(lexemaObj lexema: lexemas){
            if(lexema.token == 0){
                String cadena = lexema.valorCadena;
                if (cadena.matches("-?\\d+\\.\\d+")) {
                    int token = general.getCompLexicoValue("cReal");
                    nuevaLinea.add(new lexemaObj(cadena, token, -1, lineNumber));
                } else {
                    nuevaLinea.add(lexema);
                }
            }else{
                nuevaLinea.add(lexema);
            }
        }
        return nuevaLinea;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en la función lexica getNumReal(): " + e.getMessage());
            return null;
        }
    }

    public static List<lexemaObj> getValorLogico(List<lexemaObj> lexemas, int lineNumber){
        try{
        List<lexemaObj> nuevaLinea = new ArrayList<>();
        for(lexemaObj lexema: lexemas){
            if(lexema.token == 0){
                String cadena = lexema.valorCadena;
                if (cadena.equals("true")) {
                    int token = general.getCompLexicoValue("cTrue");
                    nuevaLinea.add(new lexemaObj(cadena, token, -1, lineNumber));
                }else if(cadena.equals("false")){
                    int token = general.getCompLexicoValue("cFalse");
                    nuevaLinea.add(new lexemaObj(cadena, token, -1, lineNumber));
                } else {
                    nuevaLinea.add(lexema);
                }
            }else{
                nuevaLinea.add(lexema);
            }
        }
        return nuevaLinea;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en la función lexica getValorLogico(): " + e.getMessage());
            return null;
        }
    }

    public static List<lexemaObj> getPalabrasReservadas(List<lexemaObj> lexemas, int lineNumber) {
        try{
        List<lexemaObj> nuevaLinea = new ArrayList<>();
        for (lexemaObj lexema : lexemas) {
            if (lexema.token == 0) {
                String cadena = lexema.valorCadena;
                int token = 0;
                
                // Comparar la cadena con las palabras reservadas
                List<String> palabrasReservadas = Arrays.asList("program", "begin", "end", "read", "write", "int", "real", "string", "bool", "if", "else", "then", "while", "do", "repeat", "until", "var");

                for (String palabra : palabrasReservadas) {
                    if (cadena.equals(palabra)) {
                        token = general.getCompLexicoValue(palabra);
                        break;
                    }
                }
                
                if (token != 0) {
                    nuevaLinea.add(new lexemaObj(cadena, token, -1, lineNumber));
                }else{
                    nuevaLinea.add(lexema);
                }
            } else {
                nuevaLinea.add(lexema);
            }
        }
        return nuevaLinea;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en la función lexica getPalabrasReservadas(): " + e.getMessage());
            return null;
        }
    }

    public static List<lexemaObj> deletePuntos(List<lexemaObj> lexemas, int lineNumber) {
        try{
        List<lexemaObj> nuevaLinea = new ArrayList<>();
        for (lexemaObj lexema : lexemas) {
            if (lexema.token == 0) {
                String cadena = lexema.valorCadena;
                String nuevaCadena = "";
                for (int indexCaracter = 0; indexCaracter < cadena.length(); indexCaracter++) {
                    char caracter = cadena.charAt(indexCaracter);
                    if (caracter == '.') {
                        
                        if (!nuevaCadena.isEmpty()) {
                            nuevaLinea.add(new lexemaObj(nuevaCadena, lineNumber));
                        }
                        
                        nuevaCadena = "";
                    } else {
                        nuevaCadena += caracter;
                    }
                }
                if(!nuevaCadena.isEmpty()){
                    //volvemos a almacenar la cadena como lexema sin identificar
                    nuevaLinea.add(new lexemaObj(nuevaCadena, lineNumber));
                }
            } else {
                nuevaLinea.add(lexema);
            }
        }
        return nuevaLinea;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en la función lexica getPuntos(): " + e.getMessage());
            return null;
        }
    }
}
