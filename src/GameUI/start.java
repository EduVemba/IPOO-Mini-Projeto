package GameUI;

import config.GameManager;

public class Start {

    private static final int LAST_GAME_AMOUNT = 10; // Quantidade de últimos jogos a serem armazenados
    private static GameManager gameManager;

    public static void main(String[] args) {
        gameManager = new GameManager(LAST_GAME_AMOUNT); // Inicializa o GameManager
        gameManager.displayStartMenu(); // Exibe o menu inicial
    }
}