public class ArranjoOrdenado {
    private int[] dados;
    private int tamanho;
    private boolean crescente;

    //Construtor
    public ArranjoOrdenado(int capacidade, boolean crescente) {
        this.dados = new int[capacidade];
        this.tamanho = 0;
        this.crescente = crescente;
    }

    public boolean estaVazio() {
        return tamanho == 0; }
    public boolean estaCheio() {
        return tamanho == dados.length; }
    public int getTamanho() {
        return tamanho; }
    public int getElemento(int i) {
        return dados[i]; }

    //Inserção
    public void inserir(int valor) {
        if (estaCheio())
            throw new RuntimeException("Arranjo cheio!");

        int i = tamanho - 1;
        if (crescente) {
            //Empurra os maiores para a direita
            while (i >= 0 && dados[i] > valor) {
                dados[i + 1] = dados[i]; //descola para frente
                i--;
            }
        }
        else {
            //Empurra os menores para a direita
            while (i >= 0 && dados[i] < valor) {
                dados [i + 1] = dados[i];
                i--;
            }
        }

        dados[i + 1] = valor; //coloca o elemento no local correto
        tamanho++;
    }

    //Exclusão
    public void excluir(int valor) {
        if (estaVazio())
            throw new RuntimeException("Arranjo vazio.");

        int posicao = buscar(valor);
        if (posicao == -1)
            throw new RuntimeException("ELemento não encontrado.");

        //Puxa todos os elementos uma posição atrás
        for (int i = posicao; i < tamanho - 1; i++) {
            dados[i] = dados[i + 1];
        }
        tamanho--;
    }

    //Busca binária
    public int buscar(int valor) {
        int inicio = 0;
        int fim = tamanho - 1;

        while (inicio <= fim) {
            int meio = (inicio + fim) / 2;
            if (dados[meio] == valor) return meio;

            if (crescente) {
                if (dados[meio] < valor) inicio = meio + 1;
                else fim = meio - 1;
            }
            else {
                if (dados[meio] > valor) inicio = meio + 1;
                else fim = meio - 1;
            }
        }
        return -1;
    }

    public void exibir() {
        System.out.println("[");
        for (int i = 0; i < tamanho; i++) {
            System.out.println(dados[i]);
            if (i < tamanho - 1) System.out.print(",");
        }
        System.out.println("]");
    }
}
