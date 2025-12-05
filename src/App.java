import java.util.Scanner;
import sequential.JogoVidaSequencial;
import parallel.JogoVidaParalelo;
import distributed.ClienteJogoVida;
import distributed.ServidorJogoVida;

public class App {
    
    // Função auxiliar para contar células vivas localmente
    private static int contarVivas(int[][] tabuleiro) {
        int count = 0;
        for (int[] linha : tabuleiro) {
            for (int celula : linha) {
                if (celula == 1) count++;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== JOGO DA VIDA DE CONWAY ===");
        
        while (true) {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Executar Sequencial");
            System.out.println("2. Executar Paralelo (Threads)");
            System.out.println("3. Executar Distribuído (Cliente)");
            System.out.println("4. Iniciar Servidor Distribuído");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");
            
            int op = sc.nextInt();
            if (op == 0) break;
            
            // Opção 4 inicia o servidor e fica esperando
            if (op == 4) {
                ServidorJogoVida.main(null); 
                continue;
            }

            System.out.print("Linhas da Matriz: ");
            int linhas = sc.nextInt();
            System.out.print("Colunas da Matriz: ");
            int colunas = sc.nextInt();
            System.out.print("Iterações (Gerações): ");
            int iter = sc.nextInt();

            long inicio = 0, fim = 0;
            int vivasFinais = 0;

            switch (op) {
                case 1:
                    System.out.println("Rodando Sequencial...");
                    inicio = System.currentTimeMillis();
                    int[][] resSeq = JogoVidaSequencial.executar(linhas, colunas, iter);
                    fim = System.currentTimeMillis();
                    vivasFinais = contarVivas(resSeq);
                    break;
                    
                case 2:
                    System.out.print("Número de Threads: ");
                    int numThreads = sc.nextInt();
                    System.out.println("Rodando Paralelo...");
                    inicio = System.currentTimeMillis();
                    int[][] resPar = JogoVidaParalelo.executar(linhas, colunas, iter, numThreads);
                    fim = System.currentTimeMillis();
                    vivasFinais = contarVivas(resPar);
                    break;
                    
                case 3:
                    System.out.print("IP do Servidor (use 'localhost'): ");
                    String ip = sc.next();
                    System.out.println("Enviando para o Servidor...");
                    inicio = System.currentTimeMillis();
                    // O cliente já recebe a contagem pronta do servidor
                    vivasFinais = ClienteJogoVida.executar(ip, 5000, linhas, colunas, iter);
                    fim = System.currentTimeMillis();
                    break;
                    
                default:
                    System.out.println("Opção inválida!");
                    continue;
            }

            System.out.println("\n[RESULTADO FINAL]");
            System.out.println("Tempo Total: " + (fim - inicio) + " ms");
            System.out.println("Células Vivas: " + vivasFinais);
        }
        sc.close();
    }
}