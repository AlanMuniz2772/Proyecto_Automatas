import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class expresiones {
    private static int numeroLinea = 1;

    // Busca cual expresion regular es la coincidencia pra la cadena dada
    public String getResult(String sCadena) {
        String sResultado = sCadena;

        // Expresiones regulares
        String aritmeticoRegex = "[+\\-*/]";
        String logicoRegex = "[&|]{2}|!"; // Aquí iría tu expresión regular para operadores lógicos
        String comparativoRegex = "[<>!]=?"; // Aquí iría tu expresión regular para operadores comparativos
        String identificadorJavaRegex = "\\$*_*[a-zA-Z]+[a-zA-Z0-9_$]*"; // Aquí iría tu expresión regular para
                                                                         // identificadores de Java
        String numerosRegex = "-?\\d+(\\.\\d+)?[dlf]?"; // Aquí iría tu expresión regular para números

        // Se prueba cada expresión regular y se llama al método correspondiente si hay
        // una coincidencia
        if (matchRegex(sCadena, aritmeticoRegex)) {
            String operadoresAritmeticos = aritmetico(sCadena);
            sResultado += operadoresAritmeticos;
        }
        if (matchRegex(sCadena, logicoRegex)) {
            String operadoresLogicos = logico(sCadena);
            sResultado += operadoresLogicos;
        }
        if (matchRegex(sCadena, comparativoRegex)) {
            String operadoresComparativos = comparativo(sCadena);
            sResultado += operadoresComparativos;
        }
        if (matchRegex(sCadena, identificadorJavaRegex)) {
            String identificadorJava = isIdentificadorJava(sCadena);
            sResultado += identificadorJava;
        }
        if (matchRegex(sCadena, numerosRegex)) {
            String identificadorNumeros = analizarNumeros(sCadena);
            sResultado += identificadorNumeros;
        }
        numeroLinea++;
        return sResultado;
    }

    // Método para verificar si hay una coincidencia entre la cadena y la expresión
    // regular
    private boolean matchRegex(String sCadena, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sCadena);
        return matcher.find();
    }

    public static String aritmetico(String sCadena) {
        // Operadores aritméticos
        String aritmetico = "[+\\-*/]"; // Expresión regular para identificar operadores aritméticos

        Pattern pattern = Pattern.compile(aritmetico);
        Matcher matcher = pattern.matcher(sCadena);

        StringBuilder operadores = new StringBuilder();
        while (matcher.find()) {
            String operador = matcher.group();
            switch (operador) {
                case "+":
                    operadores.append("+, -24, -1, " + numeroLinea + "\n");
                    break;
                case "-":
                    operadores.append("-, -25, -1, " + numeroLinea + "\n");
                    break;
                case "*":
                    operadores.append("*, -21, -1, " + numeroLinea + "\n");
                    break;
                case "/":
                    operadores.append("/, -22, -1, " + numeroLinea + "\n");
                    break;
            }
        }
        return operadores.toString();
    }

    public static String logico(String sCadena) {
        // Operadores lógicos
        String logico = "[&|]{2}|!"; // Expresión regular para identificar operadores lógicos

        Pattern pattern = Pattern.compile(logico);
        Matcher matcher = pattern.matcher(sCadena);

        StringBuilder operadores = new StringBuilder();
        while (matcher.find()) {
            String operador = matcher.group();
            switch (operador) {
                case "&&":
                    operadores.append("&&, -41, -1, " + numeroLinea + "\n");
                    break;
                case "||":
                    operadores.append("||, -42, -1, " + numeroLinea + "\n");
                    break;
                case "!":
                    operadores.append("!, -43, -1, " + numeroLinea + "\n");
                    break;
            }
        }
        return operadores.toString();
    }

    public static String comparativo(String sCadena) {
        // Operadores comparativos
        String comparativo = "[<>!]=?"; // Expresión regular para identificar operadores de comparación

        Pattern pattern = Pattern.compile(comparativo);
        Matcher matcher = pattern.matcher(sCadena);

        StringBuilder operadores = new StringBuilder();
        while (matcher.find()) {
            String operador = matcher.group();
            switch (operador) {
                case ">":
                    operadores.append(">, -33, -1, " + numeroLinea + "\n");
                    break;
                case "<":
                    operadores.append("<, -31, -1, " + numeroLinea + "\n");
                    break;
                case "==":
                    operadores.append("==, -35, -1, " + numeroLinea + "\n");
                    break;
                case ">=":
                    operadores.append(">=, -34, -1, " + numeroLinea + "\n");
                    break;
                case "<=":
                    operadores.append("<=, -32, -1, " + numeroLinea + "\n");
                    break;
                case "!=":
                    operadores.append("!=, -36, -1, " + numeroLinea + "\n");
                    break;
            }
        }
        return operadores.toString();
    }

    // Verifica que la cadena dada pertenece a la expresion regular de
    // identificadores en Java
    public static String isIdentificadorJava(String sCadena) {
        // Patrón: Una letra o un guion bajo seguido de cero o más letras, números o
        // guiones bajos
        String patronIdentificador = "\\$*_*[a-zA-Z]+[a-zA-Z0-9_$]*";

        if (Pattern.matches(patronIdentificador, sCadena)) {
            return "\nLa cadena es un identificador";
        }

        return "";
    }

    public static String analizarNumeros(String sCadena) {
        // Expresión regular para identificar números del 0 al 9
        String regexNumeros = "-?\\d+(\\.\\d+)?[dlf]?";

        if (Pattern.matches(regexNumeros, sCadena)) {
            return "\nLa cadena es un numero en lenguaje C";
        }
        return "";
    }
}
