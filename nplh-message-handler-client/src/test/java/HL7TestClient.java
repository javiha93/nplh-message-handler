import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class HL7TestClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("127.0.0.1", 22220);
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true)) {

            out.println("MSH|^~\\&|Ventana|Lab|...|20210331||OML^O21|123|P|2.4");
            out.println("PID|||0001||Smith^John||19800101|M|");
            out.println(); // Línea vacía para finalizar lectura en el servidor

            System.out.println("Mensaje enviado.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}