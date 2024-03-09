import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class expresiones {

    // Busca cual expresion regular es la coincidencia pra la cadena dada
    public String getResult(String sCadena) {
        String sResultado = "'" + sCadena + "' ";

        String operadoresEncontrados = python(sCadena);
        sResultado = sResultado + operadoresEncontrados;
        return sResultado;
    }

    public static String python(String sCadena) {
        // Operadores aritméticos, lógicos y relacionales en PYTHON
        String python = "[\\+\\-\\*\\/\\%\\&\\&\\|\\|\\!\\=\\<\\>\\<\\=\\>]"; // Expresión regular para identificar
                                                                              // operadores

        Pattern pattern = Pattern.compile(python);
        Matcher matcher = pattern.matcher(sCadena);

        StringBuilder operadores = new StringBuilder("Operadores encontrados: ");
        while (matcher.find()) {
            operadores.append(matcher.group()).append(" ");
        }
        return operadores.toString();
    }
}
