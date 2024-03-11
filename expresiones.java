import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class expresiones {

    // Busca cual expresion regular es la coincidencia pra la cadena dada
    public String getResult(String sCadena) {
        String sResultado = "";

        String operadoresAritmeticos = aritmetico(sCadena);
        sResultado = sResultado + operadoresAritmeticos;

        String operadoresLogicos = logico(sCadena);
        sResultado = sResultado + operadoresLogicos;

        String operadoresComparativos = comparativo(sCadena);
        sResultado = sResultado + operadoresComparativos;

        return sResultado;
    }

    public static String aritmetico(String sCadena) {
        // Operadores aritméticos
        String aritmetico = "[\\+\\-\\*\\/\\%]"; // Expresión regular para identificar operadores aritméticos

        Pattern pattern = Pattern.compile(aritmetico);
        Matcher matcher = pattern.matcher(sCadena);

        StringBuilder operadores = new StringBuilder("\nOperadores aritméticos encontrados: ");
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
                    operadores.append("Multiplicación ");
                    break;
                case "/":
                    operadores.append("División ");
                    break;
                case "%":
                    operadores.append("Módulo ");
                    break;
            }
            operadores.append("(").append(operador).append(") ");
        }
        return operadores.toString();
    }

    public static String logico(String sCadena) {
        // Operadores lógicos
        String logico = "&&|\\|\\||!"; // Expresión regular para identificar operadores lógicos

        Pattern pattern = Pattern.compile(logico);
        Matcher matcher = pattern.matcher(sCadena);

        StringBuilder operadores = new StringBuilder("\nOperadores lógicos encontrados: ");
        while (matcher.find()) {
            String operador = matcher.group();
            switch (operador) {
                case "&&":
                    operadores.append("AND ");
                    break;
                case "||":
                    operadores.append("OR ");
                    break;
                case "!":
                    operadores.append("NOT ");
                    break;
            }
            operadores.append("(").append(operador).append(") ");
        }
        return operadores.toString();
    }

    public static String comparativo(String sCadena) {
        // Operadores comparativos
        String comparativo = "[<>]=?"; // Expresión regular para identificar operadores de comparación

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
            }
            operadores.append("(").append(operador).append(") ");
        }
        return operadores.toString();
    }
}
