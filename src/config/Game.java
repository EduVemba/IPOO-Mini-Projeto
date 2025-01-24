package config;

import Enums.GameStatus;

/**
 * A classe {@code Game} representa o estado de um jogo, incluindo informações
 * sobre o status do jogo, o apelido do jogador e o tabuleiro.
 */
public class Game {
    private GameStatus gameStatus;
    private String nickname;
    private String board;

    /**
     * Construtor para criar uma nova instância de {@code Game}.
     *
     * @param gameStatus o status atual do jogo, representado por {@link GameStatus}.
     * @param nickname o apelido do jogador.
     * @param board o estado do tabuleiro do jogo.
     */
    public Game(GameStatus gameStatus, String nickname, String board) {
        this.gameStatus = gameStatus;
        this.nickname = nickname;
        this.board = board;
    }

    /**
     * Retorna uma representação em forma de string do objeto {@code Game}.
     *
     * @return uma string formatada contendo o apelido do jogador, o status do jogo
     * e o estado do tabuleiro.
     */
    @Override
    public String toString(){
        return "\tAlcunha: " + nickname + "\n" + "\tResultado: " + gameStatus + "\n\n" + board;
    }
}
