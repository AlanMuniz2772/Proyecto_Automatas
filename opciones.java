import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

public class opciones {
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

    

}
