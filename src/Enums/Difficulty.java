package Enums;

import config.GameConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum que representa as dificuldades disponíveis no jogo.
 * Cada dificuldade possui um valor inteiro associado e configurações específicas.
 */
public enum Difficulty {

    Starter(1, new GameConfiguration(9, 9, 10)), // Dificuldade inicial
    Pro(2, new GameConfiguration(10, 10, 12)),   // Dificuldade intermediária
    Guru(3, new GameConfiguration(12, 12, 20));  // Dificuldade avançada

    private int value; // Valor inteiro associado à dificuldade
    private GameConfiguration settings; // Configurações da dificuldade
    private static Map<Integer, Difficulty> map = new HashMap<>(); // Mapa para mapear valores inteiros para enumerações

    /**
     * Construtor do enum que associa um valor e configurações a cada dificuldade.
     *
     * @param value    Valor inteiro da dificuldade.
     * @param settings Configurações associadas à dificuldade.
     */
    Difficulty(int value, GameConfiguration settings) {
        this.value = value;
        this.settings = settings;
    }

    // Bloco estático para inicializar o mapa de valores
    static {
        for (Difficulty difficulty : Difficulty.values()) {
            map.put(difficulty.value, difficulty);
        }
    }

    /**
     * Retorna a instância de Difficulty correspondente ao valor inteiro.
     *
     * @param difficulty Valor inteiro da dificuldade.
     * @return Instância de Difficulty correspondente, ou null se não existir.
     */
    public static Difficulty valueOfInt(int difficulty) {
        return map.get(difficulty);
    }

    /**
     * Verifica se uma dificuldade com o valor especificado existe.
     *
     * @param value Valor inteiro da dificuldade.
     * @return true se a dificuldade existir, false caso contrário.
     */
    public static boolean exists(int value) {
        return map.containsKey(value);
    }

    /**
     * Retorna o valor inteiro associado à dificuldade.
     *
     * @return Valor inteiro da dificuldade.
     */
    public int getValue() {
        return value;
    }

    /**
     * Retorna as configurações associadas à dificuldade.
     *
     * @return Configurações da dificuldade.
     */
    public GameConfiguration getSettings() {
        return this.settings;
    }

    /**
     * Retorna uma representação em string da dificuldade.
     *
     * @return Nome da dificuldade.
     */
    @Override
    public String toString() {
        return switch (this) {
            case Starter -> "Starter";
            case Pro -> "Pro";
            case Guru -> "Guru";
        };
    }
}