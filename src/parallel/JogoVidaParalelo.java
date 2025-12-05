package parallel;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class JogoVidaParalelo {

    // Classe interna que representa o "Trabalhador" (Thread)
    private static class Worker extends Thread {
        private int[][] atual;
        private int[][] proximo;
        private final int inicioLinha;
        private final int fimLinha;
        private final int colunas;
        private final int iteracoes;
        private final CyclicBarrier barreira;

        public Worker(int[][] atual, int[][] proximo, int inicio, int fim, int cols, int iter, CyclicBarrier bar) {
            this.atual = atual;
            this.proximo = proximo;
            this.inicioLinha = inicio;
            this.fimLinha = fim;
            this.colunas = cols;
            this.iteracoes = iter;
            this.barreira = bar;
        }

        @Override
        public void run() {
            try {
                for (int k = 0; k < iteracoes; k++) {
                    // 1. Calcula a parte 
                    for (int i = inicioLinha; i < fimLinha; i++) {
                        for (int j = 0; j < colunas; j++) {
                            int vizinhos = contarVizinhos(atual, i, j);
                            
                            // Regra do Jogo
                            if (atual[i][j] == 1) {
                                if (vizinhos < 2 || vizinhos > 3) proximo[i][j] = 0;
                                else proximo[i][j] = 1;
                            } else {
                                if (vizinhos == 3) proximo[i][j] = 1;
                                else proximo[i][j] = 0;
                            }
                        }
                    }

                    // 2. Espera terminar (Sincronização)
                    barreira.await();
                }
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }

        // Ler matriz compartilhada
        private int contarVizinhos(int[][] tabuleiro, int linha, int coluna) {
            int vizinhos = 0;
            int linhasTotal = tabuleiro.length;
            
            for (int i = Math.max(0, linha - 1); i <= Math.min(linha + 1, linhasTotal - 1); i++) {
                for (int j = Math.max(0, coluna - 1); j <= Math.min(coluna + 1, colunas - 1); j++) {
                    if (i != linha || j != coluna) {
                        vizinhos += tabuleiro[i][j];
                    }
                }
            }
            return vizinhos;
        }
    }

    public static int[][] executar(int linhas, int colunas, int iteracoes, int numThreads) {
        int[][] atual = new int[linhas][colunas];
        int[][] proximo = new int[linhas][colunas];
        Random random = new Random();

        // Inicializa aleatoriamente
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                atual[i][j] = random.nextBoolean() ? 1 : 0;
            }
        }

        // A Barreira que vai coordenar a troca de matrizes
        CyclicBarrier barreira = new CyclicBarrier(numThreads, () -> {

            for (int i = 0; i < linhas; i++) {
                System.arraycopy(proximo[i], 0, atual[i], 0, colunas);
            }
        });

        Worker[] workers = new Worker[numThreads];
        int fatia = linhas / numThreads;

        // Cria e inicia as Threads
        for (int t = 0; t < numThreads; t++) {
            int inicio = t * fatia;
            int fim = (t == numThreads - 1) ? linhas : inicio + fatia;
            
            workers[t] = new Worker(atual, proximo, inicio, fim, colunas, iteracoes, barreira);
            workers[t].start();
        }

        // Espera terminar
        for (int t = 0; t < numThreads; t++) {
            try { workers[t].join(); } catch (InterruptedException e) {}
        }

        return atual;
    }
}