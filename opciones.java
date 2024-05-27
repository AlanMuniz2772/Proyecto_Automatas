import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

//AQUI SOLO DEBE HABER LAS OPCIONES DIRECTAS DEL MENU
public class opciones {

	// Esta clase maneja las opciones de los archivos

	private static File archivoEntrada;
	private static File archivoSalida;
	public static ArrayList<String> listaDeResultados = new ArrayList<>();

	public static void mostrarResultados(ArrayList<String> lResultados, String sTitulo) {
		try {
			if (lResultados.size() > 0) {
				String sResultados = "";
				for (String result : lResultados) {
					sResultados += result;
				}
				JOptionPane.showMessageDialog(null, sResultados, sTitulo, JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "Lista vacia", sTitulo, JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error en la función mostrarResultados: " + e.getMessage());
		}
	}

	public static void leerArchivo() {
		try {
			boolean bExists = false;
			boolean bSintactico = false;
			List<lineaObj> lLineas = null;

			if (archivoEntrada == null) {
				bExists = setArchivoEntrada();
			} else {
				bExists = true;
			}
			if (!bExists) {
				JOptionPane.showMessageDialog(null, "Primero tienes que definir tu archivo de entrada");
			} else {
				listaDeResultados.clear();
			
				lLineas = lexico.getLexico(archivoEntrada);
				
				if(lLineas != null){
					List<lexemaObj> listaTokens = new ArrayList<>();

					for (lineaObj linea : lLineas) {
						listaDeResultados.add(linea.toString());
						listaTokens.addAll(linea.lexemas);
					}

					bSintactico = sintactico.isSintactico(listaTokens);
				}

				if(bSintactico){
					JOptionPane.showMessageDialog(null, "El archivo es sintacticamente correcto");
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error en la función leerArchivo: " + e.getMessage());
		}
	}

	public static void escribirEnArchivo() {
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
					// Eliminar el contenido del archivo de salida
					FileWriter escritor = new FileWriter(archivoSalida);
					escritor.write(""); // Escribir una cadena vacía para eliminar el contenido
					escritor.close();
					// Crear un objeto FileWriter que permite escribir en el archivo
					escritor = new FileWriter(archivoSalida, true); // El segundo parámetro "true" indica agregar al final
					
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
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error en la función escribirEnArchivo: " + e.getMessage());
		}
	}


	//Define el archivo de texto que sera de lectura
    public static boolean setArchivoEntrada() {
    	try {
    		String sPath = JOptionPane.showInputDialog("Ingrese direccion de archivo de entrada (Sin .txt): ");
    		
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
        } catch (Exception e) {
            // Manejo de excepciones en caso de error de escritura
            JOptionPane.showMessageDialog(null, "Error en la función setArchivoEntrada: " + e.getMessage());
            return false;
        }	
    }
    
    //Define el archivo de texto que sera de escritura para los resultados obtenidos
    public static boolean setArchivoSalida() {
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
        } catch (Exception e) {
            // Manejo de excepciones en caso de error de escritura
            JOptionPane.showMessageDialog(null, "Error en la función setArchivoSalida: " + e.getMessage());
            return false;
        }	
    }

	//Ingreso de una sola linea
	public static void ingresoUnico () {
		try {
			String linea = JOptionPane.showInputDialog("Ingrese linea: ");
			lineaObj line = lexico.getLineaObj(linea, 1);
			JOptionPane.showMessageDialog(null, line.toString(), "Analisis Lexico", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error en la función ingresoUnico: " + e.getMessage());
		}
		
	}

	//Limpia la lista de resultados
	public static void limpiarResultados() {
		try {
			listaDeResultados.clear();
			JOptionPane.showMessageDialog(null, "Lista de resultados limpiada");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error en la función limpiarResultados: " + e.getMessage());
		}
		
	}
}

