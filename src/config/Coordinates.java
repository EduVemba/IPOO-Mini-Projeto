package config;

public class Coordinates {
    /**
     * Linhas de cordenada
     */
    private final int x;
    private final int y;

    /**
     * Construtor que inicializa as coordenadas com os valores fornecidos.
     *
     * @param x Linha da coordenada.
     * @param y Coluna da coordenada.
     */
    public Coordinates(int x,int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("%c %d", (char)('A' + x), y);
    }
}
