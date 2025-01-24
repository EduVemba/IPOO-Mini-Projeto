package config;

import Enums.Difficulty;
import Enums.GameStatus;

import java.util.Scanner;

public class GameManager {

    private final int LAST_GAME_AMOUNT;
    private GameBoard gameBoard;
    private int gameCount = 1;
    private int wonGameCount = 0;
    private GameStatus currentGameStatus; // Usando o Enum GameStatus
    private Game[] lastWonGames;
    private String playerNickname;
    private boolean isGameRunning = false;
    private Scanner inputScanner = new Scanner(System.in);

    /**
     * Construtor da classe GameManager.
     *
     * @param lastGameAmount Quantidade de últimos jogos ganhos a serem armazenados.
     */
    public GameManager(int lastGameAmount) {
        this.LAST_GAME_AMOUNT = lastGameAmount;
        this.lastWonGames = new Game[LAST_GAME_AMOUNT]; // Inicializa o array de últimos jogos
    }

    /**
     * Exibe o menu inicial e gerencia as escolhas do usuário.
     */
    public void displayStartMenu() {
        int choice;
        while (true) {
            System.out.println("#### MENU ####");
            System.out.println("1. Começar Jogo");
            System.out.println("2. Mostrar últimos " + LAST_GAME_AMOUNT + " jogos");
            System.out.println("3. Sair");
            System.out.print("Escolha uma opção: ");
            choice = inputScanner.nextInt();
            inputScanner.nextLine(); // Consome a nova linha

            switch (choice) {
                case 1:
                    chooseDifficulty();
                    break;
                case 2:
                    displayLastGames();
                    break;
                case 3:
                    System.out.println("Obrigado por jogar Campo Minado!");
                    System.exit(0);
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }
        }
    }

    /**
     * Permite ao usuário escolher a dificuldade do jogo.
     */
    private void chooseDifficulty() {
        int choice = 0;
        do {
            System.out.print("\n=== Escolha a dificuldade ===");
            for (int i = 1; i <= Difficulty.values().length; i++) {
                System.out.printf("\n%d. %s", i, Difficulty.valueOfInt(i).toString());
            }
            System.out.print("\nEscolha uma opção: ");
            choice = inputScanner.nextInt();
            inputScanner.nextLine(); // Consome a nova linha

            if (Difficulty.exists(choice)) {
                GameConfiguration settings = Difficulty.valueOfInt(choice).getSettings();
                startGame(settings);
            } else {
                choice = 0; // Escolha inválida
                System.out.println("Dificuldade inválida. Por favor, escolha uma dificuldade válida.");
            }
        } while (choice < 1);
    }

    /**
     * Inicia um novo jogo com as configurações de dificuldade escolhidas.
     *
     * @param settings Configurações de dificuldade do jogo.
     */
    public void startGame(GameConfiguration settings) {
        System.out.print("Insira a alcunha: ");
        playerNickname = inputScanner.nextLine();
        if (playerNickname.isEmpty()) {
            playerNickname = "Anonymous " + gameCount; // Define um nome padrão se o campo estiver vazio
        }

        gameBoard = new GameBoard(settings.rows(), settings.cols(), settings.mines());
        currentGameStatus = GameStatus.PLAYING;// Reinicializa o estado do jogo

        System.out.println(gameBoard);
        isGameRunning = true;
        interpretCommands();

        // Verifica o estado do jogo após o término
        if (currentGameStatus == GameStatus.LOST) {
            System.out.println("Jogo perdido. Voltando ao menu principal...");
        } else if (currentGameStatus == GameStatus.WON) {
            System.out.println("Jogo ganho. Voltando ao menu principal...");
        }

        displayStartMenu(); // Retorna ao menu inicial
    }

    /**
     * Exibe os últimos jogos ganhos ou perdidos.
     */
    public void displayLastGames() {
        if (wonGameCount == 0 && gameCount == 1) {
            System.out.println("Nenhum jogo registrado ainda.");
            return;
        }

        System.out.println("=== ÚLTIMOS " + LAST_GAME_AMOUNT + " JOGOS ===");
        for (int i = 0; i < LAST_GAME_AMOUNT; i++) {
            Game game = lastWonGames[i];
            if (game != null) {
                System.out.println("==========================================");
                System.out.println(game);
            }
        }
    }


    /**
     * Interpreta e executa os comandos inseridos pelo usuário durante o jogo.
     */
    private void interpretCommands() {
        while (isGameRunning) {
            System.out.println("[Escreva /help para ver os comandos]");
            System.out.print("Comando > ");
            String command = inputScanner.nextLine();
            String[] commandParts = command.split(" ");

            int row = -1;
            int col = -1;
            if (commandParts.length > 1) {
                row = convertAscii(commandParts[1].toUpperCase().charAt(0));
                col = Integer.parseInt(commandParts[2]);

                // Verifica se as coordenadas são válidas
                if (!gameBoard.isValidCoordinate(row, col)) {
                    System.out.println("Coordenadas inválidas!");
                    continue;
                }
            }

            System.out.print("\n");
            boolean toggled;
            switch (commandParts[0].substring(1)) {
                case "help":
                    displayHelp();
                    break;
                case "open":
                    openCell(row, col);
                    break;
                case "flag":
                    toggled = gameBoard.toggleFlag(row, col);
                    System.out.println("Bandeira " + (toggled ? "colocada" : "removida"));
                    break;
                case "hint":
                    Coordinates safeCoord = gameBoard.getRandomSafeCoordinate();
                    System.out.println("Dica: A célula " + safeCoord + " não tem mina.");
                    break;
                case "cheat":
                    toggled = gameBoard.toggleCheat();
                    System.out.println("Modo de batota " + (toggled ? "ativado" : "desativado"));
                    break;
                case "quit":
                    System.out.print("Tem a certeza que deseja sair? (y/n): ");
                    char choice = inputScanner.next().toUpperCase().charAt(0);
                    inputScanner.nextLine(); // Consome a nova linha
                    if (choice == 'Y') {
                        isGameRunning = false;
                        System.out.println("Jogo terminado. Voltando ao menu principal...");
                        return;
                    }
                    break;
                default:
                    System.out.println("Comando inválido. Use /help para ver os comandos disponíveis.");
                    break;
            }

            // Verifica se o jogador venceu
            if (gameBoard.checkWin()) {
                currentGameStatus = GameStatus.WON; // Define o estado do jogo como Won
                saveGame();
                System.out.println("Parabéns, você venceu!");
                isGameRunning = false;
                return;
            }

            System.out.println(gameBoard);
        }
    }

    /**
     * Abre uma célula no tabuleiro e verifica se o jogador acertou uma mina.
     *
     * @param row Linha da célula.
     * @param col Coluna da célula.
     */
    private void openCell(int row, int col) {
        if (gameBoard.isMine(row, col)) {
            gameBoard.revealAllMines(row, col);
            System.out.println(gameBoard);
            System.out.println("Você acertou uma mina! Fim de jogo.");
            currentGameStatus = GameStatus.LOST; // Define o estado do jogo como Lost
            saveGame();
            isGameRunning = false;
            return;
        }
        gameBoard.revealCell(row, col);
    }

    /**
     * Salva o jogo na lista de últimos jogos (ganhos ou perdidos).
     * Se o array estiver cheio, substitui o jogo mais antigo.
     */
    private void saveGame() {
        Game game = new Game(currentGameStatus, playerNickname, gameBoard.toString());

        // Adiciona o jogo ao array (substitui o mais antigo se necessário)
        int index = (wonGameCount + gameCount - 1) % LAST_GAME_AMOUNT;
        lastWonGames[index] = game;

        if (currentGameStatus == GameStatus.WON) {
            wonGameCount++;
        }

        gameCount++;
        System.out.println("Jogo salvo com sucesso!");
    }

    /**
     * Method displayHelp
     * Exibe a lista de comandos disponíveis.
     */
    private void displayHelp() {
        System.out.println("\n=== COMANDOS DISPONÍVEIS ===");
        System.out.println("/help : Apresenta a lista de comandos, a sua função e utilização.\n");
        System.out.println("/open <linha> <coluna> : Abre a célula nas coordenadas de tabuleiro - linha/coluna, e.g., /open A 2.\n");
        System.out.println("/flag <linha> <coluna> : Marca a célula nas coordenadas de tabuleiro linha/coluna com uma bandeira. Se já existir uma bandeira nessa célula, remove-a.\n");
        System.out.println("/hint : Sugere de forma aleatória, uma célula que não contém minas.\n");
        System.out.println("/cheat : Comuta o jogo para modo de “batota”, onde as minas são reveladas a cada mostragem do tabuleiro.\n");
        System.out.println("/quit : Termina o jogo e volta para o menu principal. Um jogo assim terminado não entra na lista de vitórias.\n");
    }

    /**
     * Converte um caractere (A-Z ou 0-9) em um número.
     *
     * @param character Caractere a ser convertido.
     * @return Número correspondente ao caractere.
     */
    private int convertAscii(int character) {
        if (character >= 65) // Converte letras (A-Z) para números (0-25)
            character -= 65;
        else // Converte números (0-9) para inteiros
            character = (character - '0');
        return character;
    }
}