import java.util.regex.Pattern;

public class expresiones {

    //Verifica que la cadena dada pertenece a la primer expresion regular
    public static boolean lenguaje1(String sCadena) {
    	// Patrón 1: Una o más letras seguidas de cero o más números y un guion bajo y un @.
        String patron1 = "[a-zA-Z]+[0-9]*_@[a-z]?";
        
        if (Pattern.matches(patron1, sCadena)) {
        	return true;
        }
        
        return false;
    }

    //Verifica que la cadena dada pertenece a la segunda expresion regular
    public static boolean lenguaje2(String palabra2) {
        boolean primerCaracterValido = false;
        boolean letrasValidas = false;
        boolean numerosValidos = true; // Inicialmente suponemos que no hay números o todos son pares
        boolean letraDespuesDeNumeroPar = false; // Flag para detectar letra después de un número par
        boolean caracterFinal = false;
        boolean comparacion = false;
        char ultimoCaracter = palabra2.charAt(palabra2.length() - 1);
        char penultimoCaracter = palabra2.charAt(palabra2.length() - 2);
        
       

        if (ultimoCaracter == 'b'
                && (Character.isDigit(penultimoCaracter) && Character.getNumericValue(penultimoCaracter) % 2 == 0)) {
            caracterFinal = true;
        }

        for (char caracter : palabra2.toCharArray()) {
            if (!primerCaracterValido) {
                if (caracter == '@') {
                    primerCaracterValido = true;
                }
            } else {
                if (esLetra(caracter)) {
                    if (Character.isLowerCase(caracter)) {
                        letrasValidas = true;
                        // Si encontramos una letra después de un número par, la cadena no es válida
                        if (letraDespuesDeNumeroPar && !caracterFinal) {
                            comparacion = true;
                        }
                    }
                } else if (Character.isDigit(caracter)) {
                    int numero = Character.getNumericValue(caracter);
                    if (numero % 2 != 0) {
                        // Si encontramos un número impar, la cadena no es válida
                        numerosValidos = false;
                        break;
                    } else {
                        // Si encontramos un número par, marcamos la bandera
                        letraDespuesDeNumeroPar = true;
                    }
                }
            }
        }

        if (primerCaracterValido && letrasValidas && numerosValidos && !comparacion) {
        	return true;
        } 
        
        return false;
        
    }

  //Verifica que la cadena dada pertenece a la tercera expresion regular
    public static boolean lenguaje3(String palabra2) {
        boolean letrasValidas = false;
        boolean numerosValidos = true;
        boolean primerCaracterValido = true;
        char ultimoCaracter = palabra2.charAt(palabra2.length() - 1);
        //char penultimoCaracter = palabra2.charAt(palabra2.length() - 2);

        for (char caracter : palabra2.toCharArray()) {
        	    if (!primerCaracterValido) {
        	        if (Character.isLowerCase(caracter)) {
        	            primerCaracterValido = false;
        	        }
        	    }else if (esLetra(caracter)) {
        	    	if (Character.isLowerCase(caracter) && !Character.isDigit(ultimoCaracter)) {
                    letrasValidas = true;// Si la letra es minuscula y no hay un número al final, es válido
                    //continue;
                } else if (Character.isLowerCase(caracter) && Character.isDigit(ultimoCaracter)) {
                    letrasValidas = true;// Si la letra es minúscula y hay numero al final es valido
                    //continue;
                    break;
//                } else if (Character.isUpperCase(caracter)) {
//                    letrasValidas = false;// Si la letra es mayúscula, es opcional
//                    //continue;
                   } else {
                    // En caso contrario, no es válido
                    letrasValidas = false;
                    break;
                }
            } else if (!Character.isDigit(caracter)) {
                // Si no es una letra ni un dígito, no es válido
                numerosValidos = false;
                break;
            }
        }

        if (primerCaracterValido && letrasValidas && numerosValidos) {
            return true;
        } 
        return false;
    }
    
    public static boolean esLetra(char caracter) {
        return (caracter >= 'a' && caracter <= 'z') || (caracter >= 'A' && caracter <= 'Z');
    }

    // public String getResult(String sCadena) {
    //     String sResultado = "";

    // boolean numeros = analizarNumeros(sCadena);
    //     sResultado = sResultado + numeros;

    //     return sResultado;
    // }

    public static String analizarNumeros(String sCadena) {
        // Expresión regular para identificar números del 0 al 9
        String regexNumeros = "[-?\\d+(\\.\\d+)?[dlf]";

        if(Pattern.matches(regexNumeros, sCadena)){
            return "\nLa cadena es un numero en lenguaje C";
        }
        return "\nLa cadena no es un numero en lenguaje C";;
    }

        
    
    
}

