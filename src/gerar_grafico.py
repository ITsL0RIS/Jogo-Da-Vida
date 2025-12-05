import matplotlib.pyplot as plt
import numpy as np


# ÁREA DE DADOS:
# Cenário 1 (1000x1000) - 200 Iterações, Cenário 2 (2000x2000) - 500 Iterações, Cenário 3 (3000x3000) - 500 Iterações
# Threads Paralelo = 8
tempo_sequencial  = [ 3819, 39080, 87139] 
tempo_paralelo    = [ 2353, 23987, 48409] 
tempo_distribuido = [ 2159, 20441, 53606] 



labels = ['Pequeno\n(1000x1000)', 'Médio\n(2000x2000)', 'Grande\n(3000x3000)']
x = np.arange(len(labels))
width = 0.25

fig, ax = plt.subplots(figsize=(10, 6))

rects1 = ax.bar(x - width, tempo_sequencial, width, label='Sequencial', color='#ff9999')
rects2 = ax.bar(x, tempo_paralelo, width, label='Paralelo (8 Threads)', color='#66b3ff')
rects3 = ax.bar(x + width, tempo_distribuido, width, label='Distribuído', color='#99ff99')

ax.set_ylabel('Tempo de Execução (ms)')
ax.set_title('Análise de Escalabilidade: Jogo da Vida')
ax.set_xticks(x)
ax.set_xticklabels(labels)
ax.legend()
ax.grid(axis='y', linestyle='--', alpha=0.7)

# Adiciona os valores em cima das barras
ax.bar_label(rects1, padding=3, fmt='%d')
ax.bar_label(rects2, padding=3, fmt='%d')
ax.bar_label(rects3, padding=3, fmt='%d')

fig.tight_layout()
plt.savefig('grafico_escalabilidade.png')
print("Gráfico gerado com sucesso: grafico_escalabilidade.png")
plt.show()