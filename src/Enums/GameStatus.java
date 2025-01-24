package Enums;

public enum GameStatus {
    /**
     * Representa o estado onde o jogo está em andamento.
     */
    PLAYING,
    /**
     * Representa o estado onde o jogador venceu o jogo.
     */
    WON,
    /**
     * Representa o estado onde o jogador perdeu o jogo.
     */
    LOST;

    /**
     * Retorna uma representação em forma de string para cada estado do jogo.
     *
     * @return uma string correspondente ao estado do jogo:
     * <ul>
     *     <li>"Progresso" para {@code Playing}</li>
     *     <li>"Ganho" para {@code Won}</li>
     *     <li>"Perdido" para {@code Lost}</li>
     * </ul>
     */
    @Override
    public String toString(){
        return switch (this) {
            case PLAYING -> "Progresso";
            case WON -> "Ganho";
            case LOST -> "Perdido";
        };
    }
}
