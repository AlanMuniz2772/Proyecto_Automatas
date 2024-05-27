
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

//AQUI SOLO DEBE HABER LAS FUNCIONES PARA IDENTIFICAR LEXEMAS, MAXIMO UNA FUNCION POR COMPONENTE LEXICO 
public class lexico {
    public static List<compLexico> compLexicoList = getCompLexicosFromFile("numeros_token.txt");

    public static List<lineaObj> getLexico(File archivoEntrada){
        try {
            ArrayList<String> lErrores = new ArrayList<>();
            BufferedReader lector = new BufferedReader(new FileReader(archivoEntrada));
            String linea;
            List<lineaObj> nuevoCodigo = new ArrayList<>();
            int numLinea = 0;

            // Leer cada línea del archivo y agregar la lineaObj a la lista
            while ((linea = lector.readLine()) != null) {
                numLinea++;
                nuevoCodigo.add(getLineaObj(linea, numLinea));
            }
            lector.close();

            //Agrega el resultado string a la lista de resultados temporal
            if(nuevoCodigo.size() > 0){
                for (lineaObj line : nuevoCodigo) {
                    List<lexemaObj> lexemas = line.lexemas;
                    for (lexemaObj lexema : lexemas) {
                        if (lexema.token == 0) {
                            lErrores.add(lexema.toString()+"\n");
                        }
                    }
                }
            }else{
                JOptionPane.showMessageDialog(null, "Archivo de entrada sin componentes lexicos");
                return null;
            }
            

            if(lErrores.size() > 0) {
                opciones.mostrarResultados(lErrores, "Errores lexicos encontrados: ");
                return null;
            }else{
                return nuevoCodigo;
            }
        }catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en la función lexica getLexico(): " + e.getMessage());
            return null;
        }
    }

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
            lexemas = deletePuntos(lexemas, lineNumber);
            lexemas = getNumEntero(lexemas, lineNumber);
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
                    strLexema = getString(cadenaTemp, listaTempCadenas);
                    if (strLexema != null) {
                        nuevaLinea.add(new lexemaObj(nuevaCadena, lineNumber));
                        nuevaCadena = "";
                        contenido = strLexema.substring(1, strLexema.length() - 1).trim();
                        if (!contenido.isEmpty()) {
                            nuevaLinea.add(new lexemaObj(strLexema, getCompLexicoValue("cCadena"), -1, lineNumber));
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
                    strLexema = getComment(cadenaTemp, listaTempCadenas);
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
                        int token = getCompLexicoValue(compLexico);//obtenemos el token correspondiente al caracter, desde el archivo txt
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
                            int token = getCompLexicoValue(compLexico);
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
                    token = getCompLexicoValue("iCadena");
                } else if (cadena.matches(expresion_entero)) {
                    token = getCompLexicoValue("iEntero");
                } else if (cadena.matches(expresion_logico)) {
                    token = getCompLexicoValue("iLogico");
                } else if (cadena.matches(expresion_programa)) {
                    token = getCompLexicoValue("iGral");
                } else if (cadena.matches(expresion_real)) {
                    token = getCompLexicoValue("iReal");
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
                        int token = getCompLexicoValue(compLexico);
                        if(!nuevaCadena.isEmpty()){
                            nuevaLinea.add(new lexemaObj(nuevaCadena, lineNumber));
                        }
                        nuevaLinea.add(new lexemaObj(String.valueOf(caracter), token, -1, lineNumber));
                        nuevaCadena = "";
                    }else if(caracter == '-'){
                        String cadenaTemp = cadena.substring(indexCaracter+1, cadena.length());
                        if(cadenaTemp.matches("\\d+")){
                            String compLexico = Character.toString(caracter);
                            int token = getCompLexicoValue(compLexico);
                            if(!nuevaCadena.isEmpty()){
                                nuevaLinea.add(new lexemaObj(nuevaCadena, lineNumber));
                            }
                            nuevaLinea.add(new lexemaObj(compLexico, token, -1, lineNumber));
                            nuevaCadena = "";
                            
                        }else if(cadenaTemp.matches("\\d+\\.\\d+")){
                            nuevaCadena += caracter;
                        }else{
                            
                            String compLexico = Character.toString(caracter);
                            int token = getCompLexicoValue(compLexico);
                            if(!nuevaCadena.isEmpty()){
                                nuevaLinea.add(new lexemaObj(nuevaCadena, lineNumber));
                            }
                            nuevaLinea.add(new lexemaObj(compLexico, token, -1, lineNumber));
                            nuevaCadena = "";
                            
                        }
                    }else if(caracter == ':'){
                        if(indexCaracter + 1 < cadena.length() && cadena.charAt(indexCaracter + 1) == '='){
                            String compLexico = ":=";
                            int token = getCompLexicoValue(compLexico);
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
                            int token = getCompLexicoValue(compLexico);
                            nuevaLinea.add(new lexemaObj(compLexico, token, -1, lineNumber));
                            indexCaracter++;
                        }else{
                            String compLexico = Character.toString(caracter);
                            int token = getCompLexicoValue(compLexico);
                            nuevaLinea.add(new lexemaObj(compLexico, token, -1, lineNumber));
                        }
                    
                    }else if((caracter == '='|| caracter == '!') && indexCaracter + 1 < cadena.length() && cadena.charAt(indexCaracter + 1) == '='){
                        if(!nuevaCadena.isEmpty()){
                            nuevaLinea.add(new lexemaObj(nuevaCadena, lineNumber));
                        }
                        nuevaCadena = "";
                        String compLexico = Character.toString(caracter)+"=";
                        int token = getCompLexicoValue(compLexico);
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
                            if(!nuevaCadena.isEmpty()){
                                nuevaLinea.add(new lexemaObj(nuevaCadena, lineNumber));
                            }
                            nuevaCadena = "";
                        }else{
                            if(!nuevaCadena.isEmpty()){
                                nuevaLinea.add(new lexemaObj(nuevaCadena, lineNumber));
                            }
                            nuevaCadena = "";
                            String compLexico = "&&";
                            int token = getCompLexicoValue(compLexico);
                            nuevaLinea.add(new lexemaObj(compLexico, token, -1, lineNumber));
                            indexCaracter++;
                        }
                           
                    }else if(caracter == '|' && indexCaracter + 1 < cadena.length() && cadena.charAt(indexCaracter + 1) == '|'){
                        if(!nuevaCadena.isEmpty()){
                            nuevaLinea.add(new lexemaObj(nuevaCadena, lineNumber));
                        }
                        nuevaCadena = "";
                        String compLexico = "||";
                        int token = getCompLexicoValue(compLexico);
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
                            int token = getCompLexicoValue(compLexico);
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
                    int token = getCompLexicoValue("cEntero");
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
                    int token = getCompLexicoValue("cReal");
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
                    int token = getCompLexicoValue("cTrue");
                    nuevaLinea.add(new lexemaObj(cadena, token, -1, lineNumber));
                }else if(cadena.equals("false")){
                    int token = getCompLexicoValue("cFalse");
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
                        token = getCompLexicoValue(palabra);
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

    //Esta funcion lee el archivo de texto que contiene los tokens y sus componentes lexicos
    //y los guarda en una lista de objetos compLexico
    //cada linea del archivo esta separada por un guion bajo "_" para separar el token de su componente lexico
    public static List<compLexico> getCompLexicosFromFile(String filePath) {
        List<compLexico> resultList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("_");
                String firstPart = parts[0].trim();
                int secondPart = Integer.parseInt(parts[1].trim());
                compLexico token = new compLexico(firstPart, secondPart); // Remove the reference to the outer class
                resultList.add(token);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultList;
    }   

    //Toma como argumento un string y lo busca en la lista de componentes lexicos previamnete cargada
    //si no enciuentra el componente lexico especificado regresa 0
    public static int getCompLexicoValue(String compLexico) {
        for (compLexico token : compLexicoList) {
            if (token.getComponente().equals(compLexico)) {
                return token.getValor();
            }
        }
        return 0;
    }

    public static String getComment(String cadenaTemp, String[] listaTempCadenas) {
        String lexema = findEndOfComment(cadenaTemp);
        if (lexema.length()> 2 && lexema.charAt(lexema.length() - 1) == '/' && lexema.charAt(lexema.length() - 2) == '/') {
            lexema = "//" + lexema;
            return lexema;
        }
        for (String cadena : listaTempCadenas) {
            lexema += (" " + findEndOfComment(cadena));
            if (lexema.charAt(lexema.length() - 1) == '/' && lexema.charAt(lexema.length() - 2) == '/') {
                lexema = "//" + lexema;
                return lexema;
            }
        }
        return null;
    }

    public static String findEndOfComment(String cadena) {
        String lexema = "";
        for (char caracter : cadena.toCharArray()) {
            lexema += caracter;
            if (caracter == '/' && cadena.indexOf(caracter) + 1 < cadena.length() && cadena.charAt(cadena.indexOf(caracter) + 1) == '/') {
                return lexema + '/';
            }
        }
        return lexema;
    }

    public static String getString(String cadenaTemp, String[] listaTempCadenas) {
        String lexema = findEndOfString(cadenaTemp);
        if (lexema.length() > 0 && lexema.charAt(lexema.length() - 1) == '"') {
            lexema = '"' + lexema;
            return lexema;
        }
        for (String cadena : listaTempCadenas) {
            lexema += (" " + findEndOfString(cadena));
            if (lexema.charAt(lexema.length() - 1) == '"') {
                lexema = '"' + lexema;
                return lexema;
            }
        }
        return null;
    }

    public static String findEndOfString(String cadena) {
        String lexema = "";
        for (char caracter : cadena.toCharArray()) {
            lexema += caracter;
            if (caracter == '"') {
                return lexema;
            }
        }
        return lexema;
    }
}

//calse lexema para instanciar objetos de tipo lexema
class lexemaObj {
    String valorCadena;//el lexema como tal
    int token;//token respecto a tabla de tokens
    int posicionTabla;//posicion tabla de simbolos, -2 para identificadores y -1 para cualquier otro
    int numLinea; //la linea en la que se encuentra el lexema respecto a su archivo de texto

    lexemaObj(String valorCadena, int numLinea) {
        this.valorCadena = valorCadena;
        this.numLinea = numLinea;
    }

    public lexemaObj(String valorCadena, int token, int posicionTabla, int numLinea) {
        this.valorCadena = valorCadena;
        this.token = token;
        this.posicionTabla = posicionTabla;
        this.numLinea = numLinea;
    }

    public void setLinea(int numLinea) {
        this.numLinea = numLinea;
    }
    
    @Override
    public String toString() {
        return (this.valorCadena+", "+this.token+", "+this.posicionTabla+", "+this.numLinea);
    }
}

//clase lineaObj para instanciar objetos de tipo linea
class lineaObj {
    int posicion; //su posicion en el archivo de texto
    List<lexemaObj> lexemas; //lexemas que contiene la linea

    lineaObj(int posicion, List<lexemaObj> lexemas) {
        this.posicion = posicion;
        this.lexemas = lexemas;
    }

    public void setLexemas(List<lexemaObj> lexemas) {
        this.lexemas = lexemas;
    }

    @Override
    public String toString() {
        String lexemaString = "";
        for(lexemaObj lexema : this.lexemas){
            lexemaString += lexema.toString() + "\n";
        }
        return lexemaString;
    }
}

//se usa para almacenar la informacion de los tokens y sus componentes lexicos respecto al archivo de texto
//donde se identifica el token de cada componente lexico respectivamente
class compLexico {
    private String componente;
    private int valor;

    public compLexico(String componente, int valor) {
        this.componente = componente;
        this.valor = valor;
    }

    public String getComponente() {
        return componente;
    }

    public int getValor() {
        return valor;
    }
    
}
