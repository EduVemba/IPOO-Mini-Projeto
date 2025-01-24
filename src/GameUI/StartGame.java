package GameUI;

import config.GameManager;

/**
 * Classe StartGame que faz a inicialização do jogo;
 * Define o numero de Jogos ganhos
 */

public class StartGame {

    private static final int LAST_GAMES = 10; // Quantidade de últimos jogos a serem armazenados

    public static void main(String[] args) {
        GameManager gameManager = new GameManager(LAST_GAMES); // Inicializa o GameManager
        gameManager.displayStartMenu(); // Exibe o menu inicial
    }
}