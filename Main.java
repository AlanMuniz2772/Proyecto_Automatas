import expresiones;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
    
    
    
    
    
    
    
  
    
  }