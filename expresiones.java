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

    // Verifica que la cadena dada pertenece a la expresion regular de
    // identificadores en Java
    public static String isIdentificadorJava(String sCadena) {
            // Patrón: Una letra o un guion bajo seguido de cero o más letras, números o
            // guiones bajos
            String patronIdentificador = "\\$*_*[a-zA-Z]+[a-zA-Z0-9_$]*";

            if (Pattern.matches(patronIdentificador, sCadena)) {
                return "\nLa cadena es un identificador";
            }

            return "\nLa cadena NO es un identificador";
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
