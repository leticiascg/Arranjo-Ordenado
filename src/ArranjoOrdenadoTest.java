import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArranjoOrdenadoTest {

    @Test
    public void testInserirCrescente() {
        ArranjoOrdenado arr = new ArranjoOrdenado(10, true);
        arr.inserir(5);
        arr.inserir(2);
        arr.inserir(8);

        assertEquals(2, arr.getElemento(0));
        assertEquals(5, arr.getElemento(1));
        assertEquals(8, arr.getElemento(2));
    }

    @Test
    public void testInserirDecrescente() {
        ArranjoOrdenado arr = new ArranjoOrdenado(10, false);
        arr.inserir(5);
        arr.inserir(2);
        arr.inserir(8);

        assertEquals(8, arr.getElemento(0));
        assertEquals(5, arr.getElemento(1));
        assertEquals(2, arr.getElemento(2));
    }

    @Test
    public void testExcluir() {
        ArranjoOrdenado arr = new ArranjoOrdenado(10, true);
        arr.inserir(1);
        arr.inserir(3);
        arr.inserir(5);
        arr.excluir(3);

        assertEquals(2, arr.getTamanho());
        assertEquals(1, arr.getElemento(0));
        assertEquals(5, arr.getElemento(1));
    }

    @Test
    public void testBuscar() {
        ArranjoOrdenado arr = new ArranjoOrdenado(10, true);
        arr.inserir(10);
        arr.inserir(20);
        arr.inserir(30);

        assertEquals(1, arr.buscar(20)); //posição 1
        assertEquals(-1, arr.buscar(99)); //não existe
    }
}
