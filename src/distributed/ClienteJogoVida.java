package distributed;

import java.io.*;
import java.net.*;

public class ClienteJogoVida {

    public static int executar(String host, int port, int linhas, int colunas, int iteracoes) {
        try (Socket socket = new Socket(host, port);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             DataInputStream in = new DataInputStream(socket.getInputStream())) {

            // 1. Envia o pedido
            out.writeInt(linhas);
            out.writeInt(colunas);
            out.writeInt(iteracoes);
            out.flush();

            // 2. Espera a resposta (total de células vivas)
            return in.readInt();

        } catch (IOException e) {
            System.err.println("Erro no cliente: " + e.getMessage());
            return -1;
        }
    }
}