import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class expresiones {
    private static int numeroLinea = 1;

    // Busca cual expresion regular es la coincidencia pra la cadena dada
    public String getResult(String sCadena) {
        String sResultado = "";

        // Se prueba cada expresión regular y se llama al método correspondiente si hay
        // una coincidencia
        String operadoresAritmeticos = aritmetico(sCadena);
        sResultado += operadoresAritmeticos;
        String operadoresLogicos = logico(sCadena);
        sResultado += operadoresLogicos;
        String operadoresComparativos = comparativo(sCadena);
        sResultado += operadoresComparativos;
        String palabrasReservadas = reservadas(sCadena);
        sResultado += palabrasReservadas;
        String identificadorJava = isIdentificadorJava(sCadena);
        sResultado += identificadorJava;
        String identificadorNumeros = analizarNumeros(sCadena);
        sResultado += identificadorNumeros;

        numeroLinea++;
        return sResultado;
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
        String comparativo = "<=|>=|==|!=|<|>|:="; // Expresión regular para identificar operadores de comparación

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
                case ":=":
                    operadores.append(":=, -26, -1, " + numeroLinea + "\n");
                    break;
            }
        }
        return operadores.toString();
    }

    public static String reservadas(String sCadena) {
        // Operadores comparativos
        String palabrasReservadasRegex = "\\b(program|begin|end|read|write|if|else|while|repeat|until|int|real|string|bool|var|then|do)\\b";

        Pattern pattern = Pattern.compile(palabrasReservadasRegex);
        Matcher matcher = pattern.matcher(sCadena);

        StringBuilder operadores = new StringBuilder();
        while (matcher.find()) {
            String palabraReservada = matcher.group();
            switch (palabraReservada) {
                case "program":
                    operadores.append("program, -1, -1, " + numeroLinea + "\n");
                    break;
                case "begin":
                    operadores.append("begin, -2, -1, " + numeroLinea + "\n");
                    break;
                case "end":
                    operadores.append("end, -3, -1, " + numeroLinea + "\n");
                    break;
                case "read":
                    operadores.append("read, -4, -1, " + numeroLinea + "\n");
                    break;
                case "write":
                    operadores.append("write, -5, -1, " + numeroLinea + "\n");
                    break;
                case "if":
                    operadores.append("if, -6, -1, " + numeroLinea + "\n");
                    break;
                case "else":
                    operadores.append("else, -7, -1, " + numeroLinea + "\n");
                    break;
                case "while":
                    operadores.append("while, -8, -1, " + numeroLinea + "\n");
                    break;
                case "repeat":
                    operadores.append("repeat, -9, -1, " + numeroLinea + "\n");
                    break;
                case "until":
                    operadores.append("until, -10, -1, " + numeroLinea + "\n");
                    break;
                case "int":
                    operadores.append("int, -11, -1, " + numeroLinea + "\n");
                    break;
                case "real":
                    operadores.append("real, -12, -1, " + numeroLinea + "\n");
                    break;
                case "string":
                    operadores.append("string, -13, -1, " + numeroLinea + "\n");
                    break;
                case "bool":
                    operadores.append("bool, -14, -1, " + numeroLinea + "\n");
                    break;
                case "var":
                    operadores.append("var, -15, -1, " + numeroLinea + "\n");
                    break;
                case "then":
                    operadores.append("then, -16, -1, " + numeroLinea + "\n");
                    break;
                case "do":
                    operadores.append("do, -17, -1, " + numeroLinea + "\n");
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
