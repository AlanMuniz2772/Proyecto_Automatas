import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;
//esto es un comentario de prueba con git
public class Main {
    private static File archivoEntrada = new File("entrada.txt");

	public static List<compLexico> compLexicoList = new ArrayList<>();
    
    static {
        compLexicoList.add(new compLexico("if", -1));
        compLexicoList.add(new compLexico("else", -2));
        compLexicoList.add(new compLexico("read", -3));
        compLexicoList.add(new compLexico("readln", -4));
        compLexicoList.add(new compLexico("for", -5));
        compLexicoList.add(new compLexico("then", -6));
        compLexicoList.add(new compLexico("id", -21));
        compLexicoList.add(new compLexico("iEntero", -22));
        compLexicoList.add(new compLexico("iReal", -23));
        compLexicoList.add(new compLexico("iCadena", -24));
        compLexicoList.add(new compLexico("cEntero", -41));
        compLexicoList.add(new compLexico("cReal", -42));
        compLexicoList.add(new compLexico("cCadena", -43));
        compLexicoList.add(new compLexico("+", -51));
        compLexicoList.add(new compLexico("-", -52));
        compLexicoList.add(new compLexico("*", -53));
        compLexicoList.add(new compLexico("/", -54));
        compLexicoList.add(new compLexico(">", -61));
        compLexicoList.add(new compLexico("<", -62));
        compLexicoList.add(new compLexico(">=", -63));
        compLexicoList.add(new compLexico("<=", -64));
        compLexicoList.add(new compLexico("=", -65));
        compLexicoList.add(new compLexico("(", -71));
        compLexicoList.add(new compLexico(")", -72));
        compLexicoList.add(new compLexico("[", -73));
        compLexicoList.add(new compLexico("]", -74));
        compLexicoList.add(new compLexico("and", -81));
        compLexicoList.add(new compLexico("or", -82));
        compLexicoList.add(new compLexico("not", -83));
    }

    // AQUI SOLO SE DEBE TENER LA ESTRUCTURA DEL MENU DE LA APLIACION
    public static void main(String[] args) {
        try{
            leerArchivo();
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error en la funcion main: " + e.getMessage());
        }
    }

    public static void leerArchivo() {
		try {
			
			List<lexemaObj> lLexemas = null;
        
            lLexemas = getLexico(archivoEntrada);
			if (lLexemas != null) {
                for (lexemaObj lexema : lLexemas) {
                    System.out.println(lexema.toString());
                }
            }

			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error en la función leerArchivo: " + e.getMessage());
		}
	}

    public static void mostrarResultados(ArrayList<String> lResultados, String sTitulo) {
		try {
			if (lResultados.size() > 0) {
				String sResultados = "";
				for (String result : lResultados) {
					sResultados += result;
				}
				JOptionPane.showMessageDialog(null, sResultados, sTitulo, JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "Lista vacia", sTitulo, JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error en la función mostrarResultados: " + e.getMessage());
		}
	}

    public static List<lexemaObj> getLexico(File archivoEntrada){
        try {
            BufferedReader lector = new BufferedReader(new FileReader(archivoEntrada));
            String linea;
            List<lexemaObj> nuevoCodigo = new ArrayList<>();
            int numLinea = 0;

            // Leer cada línea del archivo y agregar la lineaObj a la lista
            while ((linea = lector.readLine()) != null) {
                numLinea++;
                nuevoCodigo.addAll(getLexemas(linea, numLinea));
            }
            lector.close();

            //Agrega el resultado string a la lista de resultados temporal
            if(nuevoCodigo.size() == 0){
                
                JOptionPane.showMessageDialog(null, "Archivo de entrada sin componentes lexicos");
                return null;
            }
            
            return nuevoCodigo;
            
        }catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en la función lexica getLexico(): " + e.getMessage());
            return null;
        }
    }

    // regresa un objeto lineaObj con los lexemas de la linea
    public static List<lexemaObj> getLexemas(String linea, int lineNumber) {
        try {
            List<lexemaObj> lexemas = getLineaCommentString(linea, lineNumber);
            //AQUI ABAJO AGREGUEN SUS FUNCIONES, PUEDEN MOVERLAS A COMO SEA NECESARIO
            lexemas = getCaracterEsp(lexemas, lineNumber);
            lexemas = getOpAritmetico(lexemas, lineNumber);
            lexemas = getOpRelacional(lexemas, lineNumber);
            lexemas = getOpLogico(lexemas, lineNumber);
            lexemas = deletePuntos(lexemas, lineNumber);
            lexemas = getNumEntero(lexemas, lineNumber);
            lexemas = getPalabrasReservadas(lexemas, lineNumber);
            
            for(lexemaObj lexema: lexemas){
                if(lexema.token == 0){
                    lexema.token = -21;
                    if(lexema.valorCadena.length() > 1){
                        lexema.posicionTabla = -2;
                    }else{
                        lexema.posicionTabla = -1;
                    }
                }
            }

            return lexemas;
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
                else {
                    nuevaCadena += caracter;
                }
            }
            if (foundLexema) {
                lexemaObj lexemaUltimo = nuevaLinea.get(nuevaLinea.size() - 1);
                String spliter = "";
                
                spliter = lexemaUltimo.valorCadena;
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
                    if(caracter == '(' || caracter == ')' || caracter == ';' || caracter == ',' || caracter == ':' || caracter == '[' || caracter == ']'){
                        String compLexico = Character.toString(caracter);
                        int token = getCompLexicoValue(compLexico);//obtenemos el token correspondiente al caracter, desde el archivo txt
                        if(!nuevaCadena.isEmpty()){
                            nuevaLinea.add(new lexemaObj(nuevaCadena, lineNumber));//los caracteres que no se identificaron se alamcenan como un nuevo lexema sin identificar
                        }
                        //agregamos el caracter identificado a la lista de lexemas
                        nuevaLinea.add(new lexemaObj(String.valueOf(caracter), token, -1, lineNumber));
                        nuevaCadena = "";//reiniciamos la variable para seguir almacenando caracteres
                    }else{
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
    

    public static List<lexemaObj> getOpAritmetico(List<lexemaObj> lexemas, int lineNumber){
        try{
        List<lexemaObj> nuevaLinea = new ArrayList<>();
        for(lexemaObj lexema: lexemas){
            if(lexema.token == 0){
                String cadena = lexema.valorCadena;
                String nuevaCadena = "";
                for (int indexCaracter = 0; indexCaracter < cadena.length(); indexCaracter++){
                    char caracter = cadena.charAt(indexCaracter);
                    if(caracter == '+' || caracter == '*' || caracter == '/' || caracter == '-'){
                        String compLexico = Character.toString(caracter);
                        int token = getCompLexicoValue(compLexico);
                        if(!nuevaCadena.isEmpty()){
                            nuevaLinea.add(new lexemaObj(nuevaCadena, lineNumber));
                        }
                        nuevaLinea.add(new lexemaObj(String.valueOf(caracter), token, -1, lineNumber));
                        nuevaCadena = "";
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
                    if(caracter == '<' || caracter == '>' || caracter == '='){
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
                if(cadena.equals("and") || cadena.equals("or") || cadena.equals("not")){
                    int token = getCompLexicoValue(cadena);
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


    public static List<lexemaObj> getPalabrasReservadas(List<lexemaObj> lexemas, int lineNumber) {
        try{
        List<lexemaObj> nuevaLinea = new ArrayList<>();
        for (lexemaObj lexema : lexemas) {
            if (lexema.token == 0) {
                String cadena = lexema.valorCadena;
                int token = 0;
                
                // Comparar la cadena con las palabras reservadas
                List<String> palabrasReservadas = Arrays.asList("if","else","read","readln", "for", "then");

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
        return ("Token: "+this.valorCadena+", Codigo: "+this.token+", Identificador: "+this.posicionTabla+", Linea: "+this.numLinea);
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

