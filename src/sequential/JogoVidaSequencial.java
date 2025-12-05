package sequential;

import java.util.Random;

public class JogoVidaSequencial {

    public static int[][] executar(int linhas, int colunas, int iteracoes) {
        // 1. Cria a matriz inicial (tabuleiro)
        int[][] tabuleiro = new int[linhas][colunas];
        Random random = new Random();

        // 2. Preenche com vida aleatória inicial (50% de chance)
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                tabuleiro[i][j] = random.nextBoolean() ? 1 : 0;
            }
        }

        // 3. Começa a simulação do tempo (Gerações)
        for (int k = 0; k < iteracoes; k++) {
            int[][] proximoTabuleiro = new int[linhas][colunas];

            for (int i = 0; i < linhas; i++) {
                for (int j = 0; j < colunas; j++) {
                    // Conta quantos vizinhos vivos essa célula tem
                    int vizinhosVivos = contarVizinhos(tabuleiro, i, j);

                    // Aplica as Regras do Jogo da Vida:
                    if (tabuleiro[i][j] == 1) {
                        // Se está viva:
                        if (vizinhosVivos < 2) {
                            proximoTabuleiro[i][j] = 0; // Morre de solidão
                        } else if (vizinhosVivos > 3) {
                            proximoTabuleiro[i][j] = 0; // Morre de superpopulação
                        } else {
                            proximoTabuleiro[i][j] = 1; // Sobrevive (2 ou 3 vizinhos)
                        }
                    } else {
                        // Se está morta:
                        if (vizinhosVivos == 3) {
                            proximoTabuleiro[i][j] = 1; // Nasce (reprodução)
                        } else {
                            proximoTabuleiro[i][j] = 0; // Continua morta
                        }
                    }
                }
            }
            // Atualiza o tabuleiro para a próxima geração
            tabuleiro = proximoTabuleiro;
        }

        return tabuleiro;
    }

    // Função auxiliar para olhar as 8 células ao redor
    private static int contarVizinhos(int[][] tabuleiro, int linha, int coluna) {
        int vizinhos = 0;
        int linhas = tabuleiro.length;
        int colunas = tabuleiro[0].length;

        // Verifica os 8 vizinhos 
        for (int i = Math.max(0, linha - 1); i <= Math.min(linha + 1, linhas - 1); i++) {
            for (int j = Math.max(0, coluna - 1); j <= Math.min(coluna + 1, colunas - 1); j++) {
                
                if (i != linha || j != coluna) {
                    vizinhos += tabuleiro[i][j];
                }
            }
        }
        return vizinhos;
    }
}