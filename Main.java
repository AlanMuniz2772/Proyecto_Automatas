import javax.swing.JOptionPane;

public class Main {

    public static opciones archivo = new opciones();
    

    // Menu
    public static void main(String[] args) {

        String menu = "1. Ingresar cadena" + //
                "\n2. Leer archivo de entrada" + // lee el archivo de entrada con las cadenas a procesar
                "\n3. Mostrar resultados en memoria" +
                "\n4. Limpiar resultados en memoria" +
                "\n5. Guardar resultados en archivo de salida" + // Unica manera de guardar resultados en archivo de salida
                "\n6. Definir direccion de archivo entrada" +
                "\n7. Definir direccion de archivo salida";

        char opcion;

        // Almacena los resultados obtenidos de las cadenas procesadas

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
                    archivo.ingresoUnico();
                    break;

                case '2':
                    archivo.leerArchivo();
                    break;

                case '3':
                    archivo.mostrarResultados(opciones.listaDeResultados);
                    break;

                case '4':
                    archivo.limpiarResultados();
                    break;

                case '5':
                    archivo.escribirEnArchivo();
                    break;

                case '6':
                    archivo.setArchivoEntrada();
                    break;

                case '7':
                    archivo.setArchivoSalida();
                    break;
                    
                default:
                    JOptionPane.showMessageDialog(null, "Opcion no valida");
                    break;
            }
        } while (input != null);
    }

}

