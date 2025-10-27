package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class TrolleyPlanTest {

    @Test
    public void testConstructor() {
        // Criando um TrolleyPlan
        TrolleyPlan trolley = new TrolleyPlan(1, 100.0);

        // Verificando se a capacidade e o número do trolley estão corretos
        assertEquals(1, trolley.getTrolleyNo());
        assertEquals(100.0, trolley.getCapacityKg(), 0.0);
        assertEquals(0.0, trolley.getUsedWeight(), 0.0);  // Peso inicial deve ser 0
        assertTrue(trolley.getItems().isEmpty());  // Lista de itens deve estar vazia inicialmente
    }

    @Test
    public void testTryAddItemWithinCapacity() {
        TrolleyPlan trolley = new TrolleyPlan(1, 100.0);
        TrolleyItem item = new TrolleyItem("O001", "SKU123", 10, 20.0, 3, 4);

        // Tentando adicionar um item que cabe no trolley
        boolean added = trolley.tryAdd(item);

        // Verificando se o item foi adicionado
        assertTrue(added);
        assertEquals(20.0, trolley.getUsedWeight(), 0.0);
        assertEquals(1, trolley.getItems().size());
    }

    @Test
    public void testTryAddItemExceedingCapacity() {
        TrolleyPlan trolley = new TrolleyPlan(1, 100.0);
        TrolleyItem item1 = new TrolleyItem("O001", "SKU123", 10, 80.0, 3, 4);
        TrolleyItem item2 = new TrolleyItem("O002", "SKU456", 5, 30.0, 2, 3);

        // Adicionando o primeiro item que cabe
        trolley.tryAdd(item1);

        // Tentando adicionar o segundo item que excede a capacidade
        boolean added = trolley.tryAdd(item2);

        // Verificando se o segundo item não foi adicionado
        assertFalse(added);
        assertEquals(80.0, trolley.getUsedWeight(), 0.0);  // O peso não pode ultrapassar 100.0
        assertEquals(1, trolley.getItems().size());  // Apenas o primeiro item foi adicionado
    }

    @Test
    public void testGetUtilization() {
        TrolleyPlan trolley = new TrolleyPlan(1, 100.0);
        TrolleyItem item = new TrolleyItem("O001", "SKU123", 10, 30.0, 3, 4);

        // Adicionando um item ao trolley
        trolley.tryAdd(item);

        // Verificando a porcentagem de utilização
        assertEquals(30.0, trolley.getUtilization(), 0.0);  // 30 kg de 100 kg é 30%
    }

    @Test
    public void testToString() {
        TrolleyPlan trolley = new TrolleyPlan(1, 100.0);
        TrolleyItem item = new TrolleyItem("O001", "SKU123", 10, 20.0, 3, 4);
        trolley.tryAdd(item);

        // Atualizando a string esperada para usar a vírgula como separador decimal
        String expected = "Trolley #1 [20,0 / 100,0 kg = 20,0% full]";

        // Verificando se o método toString retorna o valor correto
        assertEquals(expected, trolley.toString());
    }

}