import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class expresiones {

    // Busca cual expresion regular es la coincidencia pra la cadena dada
    public String getResult(String sCadena) {
        String sResultado = "\n" + sCadena;

        String operadoresAritmeticos = aritmetico(sCadena);
        sResultado = sResultado + operadoresAritmeticos;

        String operadoresLogicos = logico(sCadena);
        sResultado = sResultado + operadoresLogicos;

        String operadoresComparativos = comparativo(sCadena);
        sResultado = sResultado + operadoresComparativos;

        String identificadorJava = isIdentificadorJava(sCadena);
        sResultado = sResultado + identificadorJava;

        String identificadorNumeros = analizarNumeros(sCadena);
        sResultado = sResultado + identificadorNumeros;

        return sResultado;
    }

    public static String aritmetico(String sCadena) {
        // Operadores aritméticos
        String aritmetico = "[+\\-*/]"; // Expresión regular para identificar operadores aritméticos

        Pattern pattern = Pattern.compile(aritmetico);
        Matcher matcher = pattern.matcher(sCadena);

        StringBuilder operadores = new StringBuilder("\n");
        while (matcher.find()) {
            String operador = matcher.group();
            switch (operador) {
                case "+":
                    operadores.append("+, -24, -1, ");
                    break;
                case "-":
                    operadores.append("-, -25, -1, ");
                    break;
                case "*":
                    operadores.append("*, -21, -1, ");
                    break;
                case "/":
                    operadores.append("/, -22, -1, ");
                    break;
            }
            operadores.append("\n");
        }
        return operadores.toString();
    }

    public static String logico(String sCadena) {
        // Operadores lógicos
        String logico = "[&|]{2}|!"; // Expresión regular para identificar operadores lógicos

        Pattern pattern = Pattern.compile(logico);
        Matcher matcher = pattern.matcher(sCadena);

        StringBuilder operadores = new StringBuilder("\n");
        while (matcher.find()) {
            String operador = matcher.group();
            switch (operador) {
                case "&&":
                    operadores.append("&&, -41, -1, ");
                    break;
                case "||":
                    operadores.append("||, -42, -1, ");
                    break;
                case "!":
                    operadores.append("!, -43, -1, ");
                    break;
            }
            operadores.append("\n");
        }
        return operadores.toString();
    }

    public static String comparativo(String sCadena) {
        // Operadores comparativos
        String comparativo = "[<>!]=?"; // Expresión regular para identificar operadores de comparación

        Pattern pattern = Pattern.compile(comparativo);
        Matcher matcher = pattern.matcher(sCadena);

        StringBuilder operadores = new StringBuilder("\n");
        while (matcher.find()) {
            String operador = matcher.group();
            switch (operador) {
                case ">":
                    operadores.append(">, -33, -1, ");
                    break;
                case "<":
                    operadores.append("<, -31, -1, ");
                    break;
                case "==":
                    operadores.append("==, -35, -1, ");
                    break;
                case ">=":
                    operadores.append(">=, -34, -1, ");
                    break;
                case "<=":
                    operadores.append("<=, -32, -1, ");
                    break;
                case "!=":
                    operadores.append("!=, -36, -1, ");
                    break;
            }
            operadores.append("\n");
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

            return "\n";
    }

    public static String analizarNumeros(String sCadena) {
        // Expresión regular para identificar números del 0 al 9
        String regexNumeros = "-?\\d+(\\.\\d+)?[dlf]?";

        if(Pattern.matches(regexNumeros, sCadena)){
            return "\nLa cadena es un numero en lenguaje C";
        }
        return "\n";
    }
}
