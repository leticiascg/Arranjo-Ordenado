import java.util.Arrays;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Experimento {

    static final int CAPACIDADE = 100_000;
    static final int EXECUCOES  = 100;
    static final Random RANDOM  = new Random(42);

    public static void main(String[] args) throws IOException {
        System.out.println("=== EXPERIMENTO — ArranjoOrdenado ===\n");

        String[] tipos = {"CRESCENTE", "DECRESCENTE", "ALEATORIA"};

        double[][] insC  = new double[3][2];
        double[][] insD  = new double[3][2];
        double[][] excC  = new double[3][2];
        double[][] excD  = new double[3][2];
        double[][] ins2C = new double[3][2]; // algoritmo 2 crescente
        double[][] ins2D = new double[3][2]; // algoritmo 2 decrescente

        // ── INSERÇÃO ALG 1 ───────────────────────────────────────────
        System.out.println("▶ INSERÇÃO — Algoritmo 1 (deslocamento):\n");
        for (int t = 0; t < tipos.length; t++) {
            insC[t] = rodarInsercao(tipos[t], true,  false);
            insD[t] = rodarInsercao(tipos[t], false, false);
            System.out.printf("  %-12s | Crescente   → %8.2f ± %.2f ms%n", tipos[t], insC[t][0]/1e6, insC[t][1]/1e6);
            System.out.printf("  %-12s | Decrescente → %8.2f ± %.2f ms%n", tipos[t], insD[t][0]/1e6, insD[t][1]/1e6);
        }

        // ── INSERÇÃO ALG 2 ───────────────────────────────────────────
        System.out.println("\n▶ INSERÇÃO — Algoritmo 2 (busca binária + deslocamento):\n");
        for (int t = 0; t < tipos.length; t++) {
            ins2C[t] = rodarInsercao(tipos[t], true,  true);
            ins2D[t] = rodarInsercao(tipos[t], false, true);
            System.out.printf("  %-12s | Crescente   → %8.2f ± %.2f ms%n", tipos[t], ins2C[t][0]/1e6, ins2C[t][1]/1e6);
            System.out.printf("  %-12s | Decrescente → %8.2f ± %.2f ms%n", tipos[t], ins2D[t][0]/1e6, ins2D[t][1]/1e6);
        }

        // ── EXCLUSÃO ─────────────────────────────────────────────────
        System.out.println("\n▶ EXCLUSÃO:\n");
        for (int t = 0; t < tipos.length; t++) {
            excC[t] = rodarExclusao(tipos[t], true);
            excD[t] = rodarExclusao(tipos[t], false);
            System.out.printf("  %-12s | Crescente   → %8.2f ± %.2f ms%n", tipos[t], excC[t][0]/1e6, excC[t][1]/1e6);
            System.out.printf("  %-12s | Decrescente → %8.2f ± %.2f ms%n", tipos[t], excD[t][0]/1e6, excD[t][1]/1e6);
        }

        // ── WILCOXON — compara alg1 vs alg2 para cada tipo ──────────
        System.out.println("\n▶ TESTE DE WILCOXON — Alg1 vs Alg2:\n");
        for (int t = 0; t < tipos.length; t++) {
            long[] alg1 = coletarTempos(tipos[t], true, false);
            long[] alg2 = coletarTempos(tipos[t], true, true);
            testeWilcoxon(alg1, alg2, "Alg1-" + tipos[t], "Alg2-" + tipos[t]);
        }

        // ── SALVAR CSV ───────────────────────────────────────────────
        salvarCSV(tipos, insC, insD, ins2C, ins2D, excC, excD);
        System.out.println("\n✅ Resultados salvos em resultados.csv");
    }

    // ================================================================
    // EXPERIMENTO DE INSERÇÃO
    // ================================================================

    static double[] rodarInsercao(String tipo, boolean crescente, boolean binario) {
        long[] tempos = new long[EXECUCOES];
        for (int exec = 0; exec < EXECUCOES; exec++) {
            int[] nums = gerarNumeros(tipo);
            ArranjoOrdenado arr = new ArranjoOrdenado(CAPACIDADE, crescente);

            long t1 = System.nanoTime();
            for (int n : nums) {
                if (binario) arr.inserirBinario(n);
                else         arr.inserir(n);
            }
            long t2 = System.nanoTime();

            tempos[exec] = t2 - t1;
        }
        return new double[]{ media(tempos), desvio(tempos) };
    }

    // ================================================================
    // EXPERIMENTO DE EXCLUSÃO
    // ================================================================

    static double[] rodarExclusao(String tipo, boolean crescente) {
        long[] tempos = new long[EXECUCOES];
        for (int exec = 0; exec < EXECUCOES; exec++) {
            int[] nums = gerarNumeros(tipo);

            // Preenche o arranjo FORA da medição
            ArranjoOrdenado arr = new ArranjoOrdenado(CAPACIDADE, crescente);
            for (int n : nums) arr.inserir(n);

            // Mede só a exclusão
            long t1 = System.nanoTime();
            for (int n : nums) arr.excluir(n);
            long t2 = System.nanoTime();

            tempos[exec] = t2 - t1;
        }
        return new double[]{ media(tempos), desvio(tempos) };
    }

    // ================================================================
    // COLETA TEMPOS BRUTOS (para o Wilcoxon)
    // ================================================================

    static long[] coletarTempos(String tipo, boolean crescente, boolean binario) {
        long[] tempos = new long[EXECUCOES];
        for (int exec = 0; exec < EXECUCOES; exec++) {
            int[] nums = gerarNumeros(tipo);
            ArranjoOrdenado arr = new ArranjoOrdenado(CAPACIDADE, crescente);

            long t1 = System.nanoTime();
            for (int n : nums) {
                if (binario) arr.inserirBinario(n);
                else         arr.inserir(n);
            }
            long t2 = System.nanoTime();

            tempos[exec] = t2 - t1;
        }
        return tempos;
    }

    // ================================================================
    // TESTE DE WILCOXON
    // ================================================================

    static void testeWilcoxon(long[] temposA, long[] temposB, String nomeA, String nomeB) {
        int n = temposA.length;

        // Passo 1: calcula as diferenças
        double[] diferencas = new double[n];
        for (int i = 0; i < n; i++) {
            diferencas[i] = temposA[i] - temposB[i];
        }

        // Passo 2: valor absoluto, ignora zeros
        double[] abs = new double[n];
        int validos = 0;
        for (double d : diferencas) {
            if (d != 0) abs[validos++] = Math.abs(d);
        }
        abs = Arrays.copyOf(abs, validos);
        Arrays.sort(abs);

        // Passo 3: calcula W+ e W-
        double wMais = 0, wMenos = 0;
        double[] difCopia = Arrays.copyOf(diferencas, n);
        for (int i = 0; i < validos; i++) {
            double rank = i + 1;
            for (int j = 0; j < n; j++) {
                if (Math.abs(difCopia[j]) == abs[i] && difCopia[j] != 0) {
                    if (difCopia[j] > 0) wMais  += rank;
                    else                 wMenos += rank;
                    difCopia[j] = 0; // marca como usado
                    break;
                }
            }
        }

        double W = Math.min(wMais, wMenos);

        // Passo 4: aproximação normal (para n > 10)
        double mediaW  = validos * (validos + 1) / 4.0;
        double desvioW = Math.sqrt(validos * (validos + 1) * (2 * validos + 1) / 24.0);
        double z = (W - mediaW) / desvioW;

        // Passo 5: p-valor bicaudal
        double p = 2 * (1 - cdfNormal(Math.abs(z)));

        System.out.println("  ── " + nomeA + " vs " + nomeB + " ──");
        System.out.printf("     W = %.2f | z = %.4f | p-valor ≈ %.4f%n", W, z, p);
        if (p < 0.05)
            System.out.println("     ✅ Diferença SIGNIFICATIVA (p < 0.05)");
        else
            System.out.println("     ❌ Diferença NÃO significativa (p >= 0.05)");
    }

    static double cdfNormal(double z) {
        return 0.5 * (1 + erf(z / Math.sqrt(2)));
    }

    static double erf(double x) {
        double t = 1.0 / (1.0 + 0.3275911 * Math.abs(x));
        double y = 1 - (((((1.061405429 * t - 1.453152027) * t)
                + 1.421413741) * t - 0.284496736) * t + 0.254829592) * t
                * Math.exp(-x * x);
        return x >= 0 ? y : -y;
    }

    // ================================================================
    // GERADOR DE NÚMEROS
    // ================================================================

    static int[] gerarNumeros(String tipo) {
        int[] nums = new int[CAPACIDADE];
        switch (tipo) {
            case "CRESCENTE":
                for (int i = 0; i < CAPACIDADE; i++) nums[i] = i;
                break;
            case "DECRESCENTE":
                for (int i = 0; i < CAPACIDADE; i++) nums[i] = CAPACIDADE - 1 - i;
                break;
            case "ALEATORIA":
                for (int i = 0; i < CAPACIDADE; i++) nums[i] = RANDOM.nextInt(CAPACIDADE);
                break;
        }
        return nums;
    }

    // ================================================================
    // ESTATÍSTICAS
    // ================================================================

    static double media(long[] t) {
        long soma = 0;
        for (long v : t) soma += v;
        return (double) soma / t.length;
    }

    static double desvio(long[] t) {
        double m = media(t), soma = 0;
        for (long v : t) soma += Math.pow(v - m, 2);
        return Math.sqrt(soma / t.length);
    }

    static void salvarCSV(String[] tipos,
                          double[][] insC,  double[][] insD,
                          double[][] ins2C, double[][] ins2D,
                          double[][] excC,  double[][] excD) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter("resultados.csv"))) {
            pw.println("Operacao,Algoritmo,TipoInsercao,Arranjo,Media_ms,DesvPad_ms");
            for (int t = 0; t < tipos.length; t++) {
                pw.printf("Insercao,Alg1,%s,Crescente,%.4f,%.4f%n",   tipos[t], insC[t][0]/1e6,  insC[t][1]/1e6);
                pw.printf("Insercao,Alg1,%s,Decrescente,%.4f,%.4f%n", tipos[t], insD[t][0]/1e6,  insD[t][1]/1e6);
                pw.printf("Insercao,Alg2,%s,Crescente,%.4f,%.4f%n",   tipos[t], ins2C[t][0]/1e6, ins2C[t][1]/1e6);
                pw.printf("Insercao,Alg2,%s,Decrescente,%.4f,%.4f%n", tipos[t], ins2D[t][0]/1e6, ins2D[t][1]/1e6);
                pw.printf("Exclusao,—,%s,Crescente,%.4f,%.4f%n",      tipos[t], excC[t][0]/1e6,  excC[t][1]/1e6);
                pw.printf("Exclusao,—,%s,Decrescente,%.4f,%.4f%n",    tipos[t], excD[t][0]/1e6,  excD[t][1]/1e6);
            }
        }
    }
}