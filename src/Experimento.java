import java.util.Random;

public class Experimento {

    static final int CAPACIDADE = 100_000;
    static final int EXECUCOES = 100;

    public static void main(String[] args){
        System.out.println("--- Experimento de Inserção ---\n");

        //Inserção no arranjo crescente
        rodarInsercao(true, "Crescente", "Arranjo Crescente");
        rodarInsercao(true, "Decrescente", "Arranjo Crescente");
        rodarInsercao(true, "Aleatoria", "Arranjo Crescente");

        //Inserção no arranjo decrescente
        rodarInsercao(false, "Crescente", "Arranjo Decrescente");
        rodarInsercao(false, "Decrescente", "Arranjo Decrescente");
        rodarInsercao(false, "Aleatoria", "Arranjo Decrescente");
    }

    static void rodarInsercao(boolean arrCrescente, String tipoInsercao, String label) {
        long[] tempos = new long[EXECUCOES];
        Random random = new Random();

        for (int exec = 0; exec < EXECUCOES; exec++) {
            ArranjoOrdenado arr = new ArranjoOrdenado(CAPACIDADE, arrCrescente);

            //Preparar os números, antes de medir o tempo
            int[] numeros = new int[CAPACIDADE];
            for (int i = 0; i < CAPACIDADE; i++) {
                if (tipoInsercao.equals("Crescente"))
                    numeros[i] = i;
                if (tipoInsercao.equals("Decrescente"))
                    numeros[i] = CAPACIDADE - i;
                if (tipoInsercao.equals("Aleatoria"))
                    numeros[i] = random.nextInt(CAPACIDADE);
            }

            long t1 = System.nanoTime();
            for (int i = 0; i < CAPACIDADE; i++) {
                arr.inserir(numeros[i]);
            }

            long t2 = System.nanoTime();
            tempos[exec] = t2 - t1;
        }

        double media = calcularMedia(tempos);
        double desvio = calcularDesvio(tempos, media);

        System.out.printf("%-15s | %-20s | Média: %8.2f ms | Desvio: %6.2f ms%n", tipoInsercao, label, media / 1_000_000, desvio / 1_000_000);
    }

    static double calcularMedia(long[] tempos) {
        long soma = 0;
        for (long t : tempos) soma += t;
        return (double) soma / tempos.length;
    }

    static double calcularDesvio(long[] tempos, double media) {
        double soma = 0;
        for (long t : tempos) soma += Math.pow(t - media, 2);
        return Math.sqrt(soma / tempos.length);
    }
}
