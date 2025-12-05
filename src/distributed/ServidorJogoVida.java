package distributed;

import java.io.*;
import java.net.*;
import parallel.JogoVidaParalelo; // Importa a lógica rápida

public class ServidorJogoVida {

    public static void start(int port) {
        System.out.println("[Servidor] Iniciado na porta " + port + ". Aguardando conexões...");

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("[Servidor] Cliente conectado: " + clientSocket.getInetAddress());

                    // Nova thread para não travar o servidor
                    new Thread(() -> handleClient(clientSocket)).start();
                } catch (IOException e) {
                    System.err.println("Erro na conexão: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {
        try (DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            // 1. Recebe os dados do problema
            int linhas = in.readInt();
            int colunas = in.readInt();
            int iteracoes = in.readInt();
            
            // Usa o máximo de núcleos do servidor
            int threadsServidor = Runtime.getRuntime().availableProcessors();

            System.out.println("[Servidor] Calculando Jogo da Vida: " + linhas + "x" + colunas + ", " + iteracoes + " gerações.");

            long inicio = System.currentTimeMillis();

            // 2. Roda a versão PARALELA 
            int[][] resultado = JogoVidaParalelo.executar(linhas, colunas, iteracoes, threadsServidor);

            long fim = System.currentTimeMillis();
            System.out.println("[Servidor] Terminou em " + (fim - inicio) + "ms.");

            // 3. Conta quantas células vivas sobraram
            int celulasVivas = 0;
            for (int i = 0; i < linhas; i++) {
                for (int j = 0; j < colunas; j++) {
                    if (resultado[i][j] == 1) celulasVivas++;
                }
            }

            // 4. Envia a resposta
            out.writeInt(celulasVivas);
            System.out.println("[Servidor] Resposta enviada: " + celulasVivas + " vivas.");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { socket.close(); } catch (IOException e) {}
        }
    }

    public static void main(String[] args) {
        start(5000);
    }
}