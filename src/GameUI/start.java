package GameUI;

import config.GameManager;


import java.util.Scanner;

public class Start {

    private static final int LAST_GAME_AMOUNT = 10; // Quantidade de últimos jogos a serem armazenados
    private static GameManager gameManager;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        gameManager = new GameManager(LAST_GAME_AMOUNT); // Inicializa o GameManager
        gameManager.displayStartMenu(); // Exibe o menu inicial
    }
}