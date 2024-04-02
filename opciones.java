import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

//AQUI SOLO DEBE HABER LAS OPCIONES DIRECTAS DEL MENU
public class opciones {

	// Esta clase maneja las opciones de los archivos

	private static File archivoEntrada;
	private static File archivoSalida;
	public static ArrayList<String> listaDeResultados = new ArrayList<>();

	public void mostrarResultados(ArrayList<String> lResultados) {
		try {
			if (lResultados.size() > 0) {
				String sResultados = "";
				for (String result : lResultados) {
					sResultados += result;
				}
				JOptionPane.showMessageDialog(null, sResultados, "Resultados", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "Lista de resultados vacia", "Resultados", JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error en la función mostrarResultados: " + e.getMessage());
		}
	}

	public void leerArchivo() {
		try {
			boolean bExists = false;
			ArrayList<String> lResultaodsTemp = new ArrayList<>();

			if (archivoEntrada == null) {
				bExists = setArchivoEntrada();
			} else {
				bExists = true;
			}
			if (!bExists) {
				JOptionPane.showMessageDialog(null, "Primero tienes que definir tu archivo de entrada");
			} else {
				BufferedReader lector = new BufferedReader(new FileReader(archivoEntrada));
				String linea;
				List<lineaObj> nuevoCodigo = new ArrayList<>();
				int numLinea = 0;
				// Leer cada línea del archivo y agregar la lineaObj a la lista
				while ((linea = lector.readLine()) != null) {
					numLinea++;
					nuevoCodigo.add(expresiones.getLineaObj(linea, numLinea));
				}
				lector.close();
				//Agrega el resultado string a la lista de resultados temporal
				for (lineaObj line : nuevoCodigo) {
					lResultaodsTemp.add(line.toString());
				}
				listaDeResultados.addAll(lResultaodsTemp);
				mostrarResultados(lResultaodsTemp);
				
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error en la función leerArchivo: " + e.getMessage());
		}
	}

	public void escribirEnArchivo() {
		try {
			boolean bExists = false;
			if (listaDeResultados.size() > 0) {
				if (archivoSalida == null) {
					bExists = setArchivoSalida();
				} else {
					bExists = true;
				}
				if (!bExists) {
					JOptionPane.showMessageDialog(null, "Primero tienes que definir tu archivo de salida");
				} else {
					// Crear un objeto FileWriter que permite escribir en el archivo
					FileWriter escritor = new FileWriter(archivoSalida, true); // El segundo parámetro "true" indica agregar al final

					// Crear un objeto BufferedWriter para escribir texto de manera eficiente
					BufferedWriter bufferEscritura = new BufferedWriter(escritor);
					
					// Iterar sobre la lista y escribir cada elemento en una nueva línea
					for (String elemento : listaDeResultados) {
						bufferEscritura.write(elemento);
					}
					
					// Cerrar el BufferedWriter (esto también cierra el FileWriter)
					bufferEscritura.close();

					JOptionPane.showMessageDialog(null, "Resultados guardados exitosamente");
				}
			} else {
				JOptionPane.showMessageDialog(null, "No hay resultados para guardar");
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error en la función escribirEnArchivo: " + e.getMessage());
		}
	}


	//Define el archivo de texto que sera de lectura
    public boolean setArchivoEntrada() {
    	try {
    		String sPath = JOptionPane.showInputDialog("Ingrese direccion de archivo de entrada: ");
    		
    		if(sPath != null && sPath.length()>0) {
    			// Crea un objeto File que representa el archivo en la dirección especificada
        		archivoEntrada = new File(sPath + ".txt");
        		
                // Crea el archivo si no existe
                if (!archivoEntrada.exists()) {
                	archivoEntrada.createNewFile();
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
            JOptionPane.showMessageDialog(null, "Error en la función setArchivoEntrada: " + e.getMessage());
            return false;
        }	
    }
    
    //Define el archivo de texto que sera de escritura para los resultados obtenidos
    public boolean setArchivoSalida() {
    	try {
    		String sPath = JOptionPane.showInputDialog("Ingrese direccion de archivo de salida(o cree uno): ");
    		
    		if(sPath != null && sPath.length()>0) {
    			// Crea un objeto File que representa el archivo en la dirección especificada
        		archivoSalida = new File(sPath + ".txt");
        		
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
            JOptionPane.showMessageDialog(null, "Error en la función setArchivoSalida: " + e.getMessage());
            return false;
        }	
    }

	//Ingreso de una sola linea
	public static void ingresoUnico () {
		String linea = JOptionPane.showInputDialog("Ingrese linea: ");
		lineaObj line = expresiones.getLineaObj(linea, 1);
		listaDeResultados.add(line.toString());
		JOptionPane.showMessageDialog(null, line.toString());
	}

	//Limpia la lista de resultados
	public void limpiarResultados() {
		listaDeResultados.clear();
		JOptionPane.showMessageDialog(null, "Lista de resultados limpiada");
	}
}

