
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//EN ESTE ARCHIVO SE PUEDE TENER CUALQUIER VARIEDAD DE FUNCIONES Y CLASES, QUE NO PERTENEZCAN A LOS OTROS ARCHVOS
public class general {
    //Esta lista tiene los tokens y sus componetes lexicos cprrespondientes que utilizaremos
    //esto fue recopilado del archivo de texto que se encuentra en la carpeta del proyecto
    public static List<compLexico> compLexicoList = getCompLexicosFromFile("numeros_token.txt");

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

