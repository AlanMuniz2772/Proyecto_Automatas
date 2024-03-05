import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

public class Main {
	
	private static File archivoEntrada;
	private static File archivoSalida;
	public static ArrayList<String> listaDeResultados = new ArrayList<>();
	
	//Menu
    public static void main(String[] args) {

        String menu = "1. Ingresar cadena" + //
                "\n2. Leer archivo de entrada"+ //lee el archivo de entrada con las cadenas a procesar
                "\n3. Mostrar resultados en memoria"+
                "\n4. Limpiar resultados en memoria"+
                "\n5. Guardar resultados en archivo de salida"+ //Unica manera de guardar resultados en archivo de texto
                "\n6. Definir direccion de archivo entrada"+
                "\n7. Definir direccion de archivo salida";

        char opcion;
        
         //Almacena los resultados obtenidos de las cadenas procesadas
        
        String input;
        
        do {
             input = JOptionPane.showInputDialog(null, menu);

            // Si el usuario hace clic en "Cancelar" o cierra la ventana, salimos del programa
            if (input == null) {
                System.exit(0);
            }

            opcion = input.charAt(0);

            switch (opcion) {
            	case '1':
            		String sCadena = JOptionPane.showInputDialog("Ingrese cadena: ");
            		String sResult = getResult(sCadena);
            		listaDeResultados.add(sResult);
            		JOptionPane.showMessageDialog(null, sResult);
            		break;
            		
                case '2':
                	leerArchivo();
                	
                    break;
                case '3':
                	mostrarResultados();
                 	break;
                case '4':
                	listaDeResultados.clear();
                	JOptionPane.showMessageDialog(null, "Lista de resultados limpiada");
                	break;
                case '5':
                	escribirEnArchivo();
                	break;
                case '6':
                	setArchivoEntrada();
                    break;
                case '7':
                	setArchivoSalida();
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Vuelve a intentar");
                    break;
            }
        } while (input != null);
    }
    
    //Lee el archivo de entrada y cada linea la procesa para definir a que lenguaje pertenece, el resultado String lo guarda en un arraylisr
    public static void leerArchivo() {
    	
    	boolean bExists = false;
    	
        try {
    		if (archivoEntrada== null) {
    			
	    		 bExists = setArchivoEntrada();
			}else {
				bExists = true;
			}
    		if (!bExists) {
    			JOptionPane.showMessageDialog(null, "Primero tienes que definir tu archivo de entrada");
    			
    		}else {
    			BufferedReader lector = new BufferedReader(new FileReader(archivoEntrada));
	            String linea;

	            // Leer cada línea del archivo y agregarla al ArrayList
	            while ((linea = lector.readLine()) != null) {
	            	
	            	if (linea.length() > 0) {
	            		listaDeResultados.add(getResult(linea));
	            	}
	            }
	            lector.close();
	            JOptionPane.showMessageDialog(null, "Cadenas recopiladas correctamente");
	            mostrarResultados();
    		}
    		
        } catch (IOException e) {
            // Manejar excepciones en caso de error de lectura
        	JOptionPane.showMessageDialog(null, "Error al intentar leer el archivo"+e);
        }
    }
    
    //Los resultados obtenidos durante l ejecucion se almcaenan en un archivo de texto
    public static void escribirEnArchivo() {
    	boolean bExists = false;
    	try {
    		if(listaDeResultados.size() > 0) {
    	    	if (archivoSalida== null) {
    	    		bExists = setArchivoSalida();
    			}else {
    				bExists = true;
    			}
    	    	if (!bExists) {
    	    		JOptionPane.showMessageDialog(null, "Primero tienes que definir tu archivo de salida");
    	    	}
    	    	else {
    	    		// Crear un objeto FileWriter que permite escribir en el archivo
    	    		 FileWriter escritor = new FileWriter(archivoSalida, true); // El segundo parámetro "true" indica agregar al final
    				

    	            // Crear un objeto BufferedWriter para escribir texto de manera eficiente
    	            BufferedWriter bufferEscritura = new BufferedWriter(escritor);

    	            // Iterar sobre la lista y escribir cada elemento en una nueva línea
    	            for (String elemento : listaDeResultados) {
    	                bufferEscritura.write(elemento);
    	                bufferEscritura.newLine();  // Agregar una nueva línea después de cada elemento
    	            }

    	            // Cerrar el BufferedWriter (esto también cierra el FileWriter)
    	            bufferEscritura.close();
    	            
    	            JOptionPane.showMessageDialog(null, "Resultados guardados exitosamente");
    	    		}
    	    }else {
        			JOptionPane.showMessageDialog(null, "No hay resultados para guardar");
        	}
    	}catch (IOException e) {
			// TODO Auto-generated catch block
    		JOptionPane.showMessageDialog(null, "Error al intentar escribir en archivo");
		}
    }
    
    //Muestra los resultados obtenidos en ejecucion con joptionepane
    public static void mostrarResultados() {
    	if (listaDeResultados.size() > 0) {
    		String sResultados = "";
        	for(String result: listaDeResultados) {
        		sResultados = sResultados + "\n"+ result;
        	}
        	JOptionPane.showMessageDialog(null, "Resultados: " +sResultados);
    	}else {
    		JOptionPane.showMessageDialog(null, "Lista de resultados vacia");
    	}
    }
    
    //Define el archivo de texto que sera de lectura
    public static boolean setArchivoEntrada() {
    	String sPath = JOptionPane.showInputDialog("Ingrese direccion de archivo de entrada: ");
    	if(sPath != null && sPath.length()>0) {
    		// Crea un objeto File que representa el archivo en la dirección especificada
    		archivoEntrada = new File(sPath);

    		// Crea el archivo si no existe
    		if (archivoEntrada.exists()) {
    		    JOptionPane.showMessageDialog(null, "Archivo encontrado y definido");
    		    return true;
    		}else {
    			JOptionPane.showMessageDialog(null, "El archivo especificado no existe");
    			archivoEntrada = null;
    		}
    	}else {
    		JOptionPane.showMessageDialog(null, "La direccion no es valida");
    	}
    	return false;
    }
    
    //Define el archivo de texto que sera de escritura para los resultados obtenidos
    public static boolean setArchivoSalida() {
    	try {
    		String sPath = JOptionPane.showInputDialog("Ingrese direccion de archivo de salida(o cree uno): ");
    		
    		if(sPath != null && sPath.length()>0) {
    			// Crea un objeto File que representa el archivo en la dirección especificada
        		archivoSalida = new File(sPath);
        		
                // Crea el archivo si no existe
                if (!archivoSalida.exists()) {
                	archivoSalida.createNewFile();
                    JOptionPane.showMessageDialog(null, "Archivo creado exitosamente");
                }else {
                	
                	JOptionPane.showMessageDialog(null, "Archivo definido");
                }
                return true;
    		}else {
    			JOptionPane.showMessageDialog(null, "La direccion no es valida");
    		}
            return false;
        } catch (IOException e) {
            // Manejo de excepciones en caso de error de escritura
            e.printStackTrace();
            return false;
        }	
    }
    
    //Busca cual expresion regular es la coincidencia pra la cadena dada
    public static String getResult(String sCadena) {
    	String sResultado = "'"+sCadena +"'";
    	if(lenguaje1(sCadena)) {
    		 sResultado= sResultado+ " es una cadena válida en la expresion regular [a-zA-Z]+[0-9]*_@[a-z]?";
    	}else if(lenguaje2(sCadena)) {
    		 sResultado = sResultado + " es una cadena válida en la expresion regular @[a-z]+[0,2,4,6,8]*[b]?";
    	}else if(lenguaje3(sCadena)) {
    		sResultado = sResultado + " es una cadena válida en la expresión regular [a-z]+[A-Z]*[0-9]?";
    	}
    	else {
    		sResultado = sResultado + " no pertenece a ningun lenguaje"; 
    	}
    	return sResultado;
    }
    
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
    
  }