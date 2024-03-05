import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

public class general {
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
}
