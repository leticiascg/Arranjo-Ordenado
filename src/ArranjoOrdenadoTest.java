import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArranjoOrdenadoTest {

    // ── INSERÇÃO CRESCENTE ───────────────────────────────────────────

    @Test
    public void testInserirCrescente_ordenacaoCorreta() {
        ArranjoOrdenado arr = new ArranjoOrdenado(10, true);
        arr.inserir(5);
        arr.inserir(2);
        arr.inserir(8);
        arr.inserir(1);
        assertEquals(1, arr.getElemento(0));
        assertEquals(2, arr.getElemento(1));
        assertEquals(5, arr.getElemento(2));
        assertEquals(8, arr.getElemento(3));
    }

    @Test
    public void testInserirCrescente_elementoNoInicio() {
        ArranjoOrdenado arr = new ArranjoOrdenado(5, true);
        arr.inserir(10);
        arr.inserir(1);
        assertEquals(1,  arr.getElemento(0));
        assertEquals(10, arr.getElemento(1));
    }

    @Test
    public void testInserirCrescente_elementoNoFim() {
        ArranjoOrdenado arr = new ArranjoOrdenado(5, true);
        arr.inserir(1);
        arr.inserir(99);
        assertEquals(99, arr.getElemento(1));
    }

    @Test
    public void testInserirCrescente_duplicado() {
        ArranjoOrdenado arr = new ArranjoOrdenado(5, true);
        arr.inserir(3);
        arr.inserir(3);
        assertEquals(2, arr.getTamanho());
        assertEquals(3, arr.getElemento(0));
        assertEquals(3, arr.getElemento(1));
    }

    // ── INSERÇÃO DECRESCENTE ─────────────────────────────────────────

    @Test
    public void testInserirDecrescente_ordenacaoCorreta() {
        ArranjoOrdenado arr = new ArranjoOrdenado(10, false);
        arr.inserir(5);
        arr.inserir(2);
        arr.inserir(8);
        arr.inserir(1);
        assertEquals(8, arr.getElemento(0));
        assertEquals(5, arr.getElemento(1));
        assertEquals(2, arr.getElemento(2));
        assertEquals(1, arr.getElemento(3));
    }

    @Test
    public void testInserirDecrescente_maiorVaiParaInicio() {
        ArranjoOrdenado arr = new ArranjoOrdenado(5, false);
        arr.inserir(3);
        arr.inserir(10);
        assertEquals(10, arr.getElemento(0));
        assertEquals(3,  arr.getElemento(1));
    }

    // ── INSERÇÃO BINÁRIA (Algoritmo 2) ───────────────────────────────

    @Test
    public void testInserirBinarioCrescente_ordenacaoCorreta() {
        ArranjoOrdenado arr = new ArranjoOrdenado(10, true);
        arr.inserirBinario(5);
        arr.inserirBinario(2);
        arr.inserirBinario(8);
        arr.inserirBinario(1);
        assertEquals(1, arr.getElemento(0));
        assertEquals(2, arr.getElemento(1));
        assertEquals(5, arr.getElemento(2));
        assertEquals(8, arr.getElemento(3));
    }

    @Test
    public void testInserirBinarioDecrescente_ordenacaoCorreta() {
        ArranjoOrdenado arr = new ArranjoOrdenado(10, false);
        arr.inserirBinario(5);
        arr.inserirBinario(2);
        arr.inserirBinario(8);
        arr.inserirBinario(1);
        assertEquals(8, arr.getElemento(0));
        assertEquals(5, arr.getElemento(1));
        assertEquals(2, arr.getElemento(2));
        assertEquals(1, arr.getElemento(3));
    }

    @Test
    public void testInserirBinario_mesmoResultadoQueInserirNormal() {
        ArranjoOrdenado arr1 = new ArranjoOrdenado(10, true);
        ArranjoOrdenado arr2 = new ArranjoOrdenado(10, true);
        int[] valores = {7, 3, 9, 1, 5};
        for (int v : valores) {
            arr1.inserir(v);
            arr2.inserirBinario(v);
        }
        // os dois devem produzir o mesmo array
        for (int i = 0; i < arr1.getTamanho(); i++) {
            assertEquals(arr1.getElemento(i), arr2.getElemento(i));
        }
    }

    // ── EXCLUSÃO ─────────────────────────────────────────────────────

    @Test
    public void testExcluirElementoDoMeio() {
        ArranjoOrdenado arr = new ArranjoOrdenado(10, true);
        arr.inserir(1); arr.inserir(3); arr.inserir(5);
        arr.excluir(3);
        assertEquals(2, arr.getTamanho());
        assertEquals(1, arr.getElemento(0));
        assertEquals(5, arr.getElemento(1));
    }

    @Test
    public void testExcluirPrimeiroElemento() {
        ArranjoOrdenado arr = new ArranjoOrdenado(10, true);
        arr.inserir(1); arr.inserir(3); arr.inserir(5);
        arr.excluir(1);
        assertEquals(2, arr.getTamanho());
        assertEquals(3, arr.getElemento(0));
    }

    @Test
    public void testExcluirUltimoElemento() {
        ArranjoOrdenado arr = new ArranjoOrdenado(10, true);
        arr.inserir(1); arr.inserir(3); arr.inserir(5);
        arr.excluir(5);
        assertEquals(2, arr.getTamanho());
        assertEquals(3, arr.getElemento(1));
    }

    @Test
    public void testExcluirInexistenteLancaExcecao() {
        ArranjoOrdenado arr = new ArranjoOrdenado(10, true);
        arr.inserir(1);
        assertThrows(RuntimeException.class, () -> arr.excluir(99));
    }

    @Test
    public void testExcluirDeVazioLancaExcecao() {
        ArranjoOrdenado arr = new ArranjoOrdenado(10, true);
        assertThrows(RuntimeException.class, () -> arr.excluir(1));
    }

    // ── BUSCA ────────────────────────────────────────────────────────

    @Test
    public void testBuscarExistente() {
        ArranjoOrdenado arr = new ArranjoOrdenado(10, true);
        arr.inserir(10); arr.inserir(20); arr.inserir(30);
        assertEquals(1, arr.buscar(20));
    }

    @Test
    public void testBuscarInexistente() {
        ArranjoOrdenado arr = new ArranjoOrdenado(10, true);
        arr.inserir(10); arr.inserir(20);
        assertEquals(-1, arr.buscar(99));
    }

    @Test
    public void testBuscarDecrescente() {
        ArranjoOrdenado arr = new ArranjoOrdenado(10, false);
        arr.inserir(30); arr.inserir(20); arr.inserir(10);
        assertEquals(1, arr.buscar(20));
    }

    // ── ESTADOS ──────────────────────────────────────────────────────

    @Test
    public void testEstaVazioAoCriar() {
        assertTrue(new ArranjoOrdenado(5, true).estaVazio());
    }

    @Test
    public void testNaoEstaVazioAposInsercao() {
        ArranjoOrdenado arr = new ArranjoOrdenado(5, true);
        arr.inserir(1);
        assertFalse(arr.estaVazio());
    }

    @Test
    public void testEstaCheia() {
        ArranjoOrdenado arr = new ArranjoOrdenado(3, true);
        arr.inserir(1); arr.inserir(2); arr.inserir(3);
        assertTrue(arr.estaCheia());
    }

    @Test
    public void testInserirCheioLancaExcecao() {
        ArranjoOrdenado arr = new ArranjoOrdenado(2, true);
        arr.inserir(1); arr.inserir(2);
        assertThrows(RuntimeException.class, () -> arr.inserir(3));
    }

    // ── TAMANHO ──────────────────────────────────────────────────────

    @Test
    public void testTamanhoAposInsercoes() {
        ArranjoOrdenado arr = new ArranjoOrdenado(10, true);
        arr.inserir(5); arr.inserir(3); arr.inserir(7);
        assertEquals(3, arr.getTamanho());
    }

    @Test
    public void testTamanhoAposExclusao() {
        ArranjoOrdenado arr = new ArranjoOrdenado(10, true);
        arr.inserir(5); arr.inserir(3);
        arr.excluir(5);
        assertEquals(1, arr.getTamanho());
    }
}