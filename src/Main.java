import config.GameManager;

/**
 * Classe Main para Usar e Testar o Projeto
 */

public class Main {
    public static void main(String[] args) {
        GameManager gameManager = new GameManager(10);
        gameManager.displayStartMenu();
    }

}