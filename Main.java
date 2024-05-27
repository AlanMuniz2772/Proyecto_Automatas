import javax.swing.JOptionPane;
//esto es un comentario de prueba con git
public class Main {
    
    public static opciones archivo = new opciones();

    // AQUI SOLO SE DEBE TENER LA ESTRUCTURA DEL MENU DE LA APLIACION
    public static void main(String[] args) {
        try{
            String menu = "1. Analizar lexico de cadena" + //
                    "\n2. Leer archivo de entrada" + // lee el archivo de entrada con las cadenas a procesar
                    "\n3. Mostrar tabla tokens" +
                    "\n4. Limpiar tabla tokens" +
                    "\n5. Guardar tabla de tokens en archivo de salida" + // Unica manera de guardar resultados en archivo de salida
                    "\n6. Definir direccion de archivo entrada" +
                    "\n7. Definir direccion de archivo salida";

                char opcion;
                
                String input;
    
                do {
                    input = JOptionPane.showInputDialog(null, menu);

                    // Si el usuario hace clic en "Cancelar" o cierra la ventana, salimos del
                    // programa
                    if (input == null) {
                        System.exit(0);
                    }
                    
                    opcion = input.charAt(0);
                    
                switch (opcion) {
                    case '1':
                        opciones.ingresoUnico();
                        break;
                        
                    case '2':
                        opciones.leerArchivo();
                        break;

                    case '3':
                        opciones.mostrarResultados(opciones.listaDeResultados, "Resultados lexicos");
                        break;

                    case '4':
                        opciones.limpiarResultados();
                        break;

                    case '5':
                        opciones.escribirEnArchivo();
                        break;

                    case '6':
                        opciones.setArchivoEntrada();
                        break;

                    case '7':
                        opciones.setArchivoSalida();
                        break;
                    
                    default:
                        JOptionPane.showMessageDialog(null, "Opcion no valida");
                        break;
                }
            } while (input != null);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error en la funcion main: " + e.getMessage());
        }
    }
}

