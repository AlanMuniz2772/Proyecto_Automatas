import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class expresiones {

    // Busca cual expresion regular es la coincidencia pra la cadena dada
    public String getResult(String sCadena) {
        String sResultado = "'" + sCadena + "' ";

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

        StringBuilder operadores = new StringBuilder("Operadores aritméticos encontrados: ");
        while (matcher.find()) {
            operadores.append(matcher.group()).append(" ");
        }
        return operadores.toString();
    }

    public static String logico(String sCadena) {
        // Operadores lógicos
        String logico = "[\\&\\|\\!\\=]"; // Expresión regular para identificar operadores lógicos

        Pattern pattern = Pattern.compile(logico);
        Matcher matcher = pattern.matcher(sCadena);

        StringBuilder operadores = new StringBuilder("Operadores lógicos encontrados: ");
        while (matcher.find()) {
            operadores.append(matcher.group()).append(" ");
        }
        return operadores.toString();
    }

    public static String comparativo(String sCadena) {
        // Operadores comparativos
        String comparativo = "[\\<\\>\\=]"; // Expresión regular para identificar operadores de comparación

        Pattern pattern = Pattern.compile(comparativo);
        Matcher matcher = pattern.matcher(sCadena);

        StringBuilder operadores = new StringBuilder("Operadores comparativos encontrados: ");
        while (matcher.find()) {
            operadores.append(matcher.group()).append(" ");
        }
        return operadores.toString();
    }
}
