public class ArranjoOrdenado {

    private int[] dados;
    private int tamanho;
    private boolean crescente;

    public ArranjoOrdenado(int capacidade, boolean crescente) {
        this.dados = new int[capacidade];
        this.tamanho = 0;
        this.crescente = crescente;
    }

    public boolean estaVazio()   { return tamanho == 0; }
    public boolean estaCheia()   { return tamanho == dados.length; }
    public int getTamanho()      { return tamanho; }
    public int getCapacidade()   { return dados.length; }
    public boolean isCrescente() { return crescente; }

    public int getElemento(int i) {
        if (i < 0 || i >= tamanho)
            throw new IndexOutOfBoundsException("Índice inválido: " + i);
        return dados[i];
    }

    // ----------------------------------------------------------------
    // Algoritmo 1 — Inserção por deslocamento
    // Compara e desloca no mesmo loop — O(n)
    // ----------------------------------------------------------------
    public void inserir(int valor) {
        if (estaCheia())
            throw new RuntimeException("Arranjo cheio!");

        int i = tamanho - 1;

        if (crescente) {
            while (i >= 0 && dados[i] > valor) {
                dados[i + 1] = dados[i];
                i--;
            }
        } else {
            while (i >= 0 && dados[i] < valor) {
                dados[i + 1] = dados[i];
                i--;
            }
        }

        dados[i + 1] = valor;
        tamanho++;
    }

    // ----------------------------------------------------------------
    // Algoritmo 2 — Inserção com busca binária + deslocamento
    // Acha a posição em O(log n), depois desloca em O(n)
    // ----------------------------------------------------------------
    public void inserirBinario(int valor) {
        if (estaCheia())
            throw new RuntimeException("Arranjo cheio!");

        // Passo 1: busca binária para encontrar a posição de inserção
        int inicio = 0;
        int fim = tamanho - 1;
        int pos = tamanho; // padrão = inserir no fim

        if (crescente) {
            while (inicio <= fim) {
                int meio = (inicio + fim) / 2;
                if (dados[meio] > valor) {
                    pos = meio;
                    fim = meio - 1;
                } else {
                    inicio = meio + 1;
                }
            }
        } else {
            while (inicio <= fim) {
                int meio = (inicio + fim) / 2;
                if (dados[meio] < valor) {
                    pos = meio;
                    fim = meio - 1;
                } else {
                    inicio = meio + 1;
                }
            }
        }

        // Passo 2: desloca para abrir espaço na posição encontrada
        for (int i = tamanho; i > pos; i--) {
            dados[i] = dados[i - 1];
        }

        dados[pos] = valor;
        tamanho++;
    }

    // ----------------------------------------------------------------
    // Exclusão
    // ----------------------------------------------------------------
    public void excluir(int valor) {
        if (estaVazio())
            throw new RuntimeException("Arranjo vazio!");

        int posicao = buscar(valor);
        if (posicao == -1)
            throw new RuntimeException("Elemento não encontrado: " + valor);

        for (int i = posicao; i < tamanho - 1; i++) {
            dados[i] = dados[i + 1];
        }
        tamanho--;
    }

    // ----------------------------------------------------------------
    // Busca binária — O(log n)
    // ----------------------------------------------------------------
    public int buscar(int valor) {
        int inicio = 0;
        int fim = tamanho - 1;

        while (inicio <= fim) {
            int meio = (inicio + fim) / 2;

            if (dados[meio] == valor) return meio;

            if (crescente) {
                if (dados[meio] < valor) inicio = meio + 1;
                else                     fim   = meio - 1;
            } else {
                if (dados[meio] > valor) inicio = meio + 1;
                else                     fim   = meio - 1;
            }
        }
        return -1;
    }

    // ----------------------------------------------------------------
    // Utilitários
    // ----------------------------------------------------------------
    public void exibir() {
        System.out.print("[");
        for (int i = 0; i < tamanho; i++) {
            System.out.print(dados[i]);
            if (i < tamanho - 1) System.out.print(", ");
        }
        System.out.println("]");
    }
}