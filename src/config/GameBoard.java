package config;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class GameBoard {
    private final char[][] displayGrid;
    private final int[][] adjacentMinesGrid;
    private final boolean[][] mineLocations;
    private final boolean[][] flagLocations;
    private final boolean[][] uncoveredCells;
    private final int numRows;
    private final int numCols;
    private final int totalMines;
    private int remainingFlags;
    private final long startTime;
    private boolean isCheatEnabled;

    /**
     * Constrói um tabuleiro com o número de linhas, colunas e minas especificado.
     *
     * @param numRows Número de linhas do tabuleiro.
     * @param numCols Número de colunas do tabuleiro.
     * @param totalMines Número total de minas no tabuleiro.
     */
    public GameBoard(int numRows, int numCols, int totalMines) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.totalMines = totalMines;
        remainingFlags = totalMines;

        startTime = System.currentTimeMillis();

        displayGrid = new char[numRows][numCols];
        mineLocations = new boolean[numRows][numCols];
        uncoveredCells = new boolean[numRows][numCols];
        flagLocations = new boolean[numRows][numCols];
        adjacentMinesGrid = new int[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                displayGrid[i][j] = '*';
            }
        }
        placeMines();
    }

    /**
     * Coloca minas aleatoriamente no tabuleiro.
     */
    private void placeMines() {
        Random rand = new Random();
        int minesPlaced = 0;

        while (minesPlaced < totalMines) {
            int row = rand.nextInt(numRows);
            int col = rand.nextInt(numCols);

            if (!mineLocations[row][col]) {
                mineLocations[row][col] = true;
                minesPlaced++;
            }
        }

        calculateAdjacentMines();
    }

    /**
     * Calcula o número de minas adjacentes para cada célula.
     */
    private void calculateAdjacentMines() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                adjacentMinesGrid[i][j] = countAdjacentMines(i, j);
            }
        }
    }

    /**
     * Conta o número de minas adjacentes a uma célula.
     *
     * @param row Linha da célula.
     * @param col Coluna da célula.
     * @return Número de minas adjacentes.
     */
    private int countAdjacentMines(int row, int col) {
        int count = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = row + i;
                int newCol = col + j;
                if (newRow >= 0 && newRow < numRows && newCol >= 0 && newCol < numCols && mineLocations[newRow][newCol]) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Revela a célula e as células vizinhas se não houver minas ou minas adjacentes.
     *
     * @param row Linha da célula.
     * @param col Coluna da célula.
     */
    public void revealCell(int row, int col) {
        if (!isValidCoordinate(row, col) || uncoveredCells[row][col] || mineLocations[row][col])
            return;

        uncoveredCells[row][col] = true;
        updateCellVisual(row, col);

        if (adjacentMinesGrid[row][col] == 0) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i == 0 && j == 0) continue;
                    revealCell(row + i, col + j);
                }
            }
        }
    }

    /**
     * Atualiza a visualização de uma célula.
     *
     * @param row Linha da célula.
     * @param col Coluna da célula.
     */
    private void updateCellVisual(int row, int col) {
        if (!uncoveredCells[row][col]) {
            displayGrid[row][col] = '■';
            if (isCheatEnabled) {
                if (mineLocations[row][col]) displayGrid[row][col] = '◆';
            }
            if (flagLocations[row][col]) displayGrid[row][col] = '▶';
            return;
        }

        displayGrid[row][col] = '□';
        if (adjacentMinesGrid[row][col] > 0) displayGrid[row][col] = (char) (adjacentMinesGrid[row][col] + '0');
        if (flagLocations[row][col]) displayGrid[row][col] = '▶';
        if (mineLocations[row][col]) displayGrid[row][col] = '◆';
    }

    /**
     * Alterna o status da bandeira em uma célula.
     *
     * @param row Linha da célula.
     * @param col Coluna da célula.
     * @return true se a bandeira foi colocada/removida com sucesso.
     */
    public boolean toggleFlag(int row, int col) {
        if (!flagLocations[row][col] && remainingFlags <= 0)
            return false;

        if (!flagLocations[row][col])
            remainingFlags--;
        else
            remainingFlags++;

        flagLocations[row][col] = !flagLocations[row][col];
        updateCellVisual(row, col);
        return true;
    }

    /**
     * Revela todas as minas no tabuleiro.
     *
     * @param row Linha da célula selecionada.
     * @param col Coluna da célula selecionada.
     */
    public void revealAllMines(int row, int col) {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                uncoveredCells[i][j] = true;
            }
        }
        updateBoardVisual();
        displayGrid[row][col] = '◈';
    }

    /**
     * Verifica se o jogador venceu o jogo.
     *
     * @return true se todas as minas estiverem marcadas ou todas as células seguras estiverem reveladas.
     */
    public boolean checkWin() {
        boolean allMinesFlagged = true;
        boolean allSafeCellsRevealed = true;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if ((mineLocations[i][j] && !flagLocations[i][j]))
                    allMinesFlagged = false;
                if ((!mineLocations[i][j] && !uncoveredCells[i][j]))
                    allSafeCellsRevealed = false;
            }
        }
        return allMinesFlagged || allSafeCellsRevealed;
    }

    /**
     * Obtém uma coordenada aleatória que não contém uma mina e não foi revelada.
     *
     * @return Coordenada aleatória.
     */
    public Coordinates getRandomSafeCoordinate() {
        Random rand = new Random();
        int row, col;
        do {
            row = rand.nextInt(numRows);
            col = rand.nextInt(numCols);
        } while (mineLocations[row][col] || uncoveredCells[row][col] || !isValidCoordinate(row, col));

        return new Coordinates(row, col);
    }

    /**
     * Verifica se uma célula contém uma mina.
     *
     * @param row Linha da célula.
     * @param col Coluna da célula.
     * @return true se a célula contiver uma mina.
     */
    public boolean isMine(int row, int col) {
        return mineLocations[row][col];
    }

    /**
     * Alterna o modo de trapaça.
     *
     * @return true se o modo de trapaça estiver ativado.
     */
    public boolean toggleCheat() {
        isCheatEnabled = !isCheatEnabled;
        updateBoardVisual();
        return isCheatEnabled;
    }

    /**
     * Verifica se uma coordenada é válida.
     *
     * @param row Linha da coordenada.
     * @param col Coluna da coordenada.
     * @return true se a coordenada estiver dentro dos limites.
     */
    public boolean isValidCoordinate(int row, int col) {
        return (row >= 0 && row < numRows && col >= 0 && col < numCols);
    }

    /**
     * Atualiza a visualização de todo o tabuleiro.
     */
    private void updateBoardVisual() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                updateCellVisual(i, j);
            }
        }
    }

    /**
     * Constrói uma representação em string do tabuleiro, com estatísticas.
     *
     * @return O tabuleiro como uma string.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("X⠀ ");
        for (int i = 0; i < numCols; i++) {
            sb.append((i)).append("  ");
        }
        sb.append('\n');
        for (int i = 0; i < numRows; i++) {
            sb.append((char) ('A' + i)).append("  ");
            for (int j = 0; j < numCols; j++) {
                sb.append(displayGrid[i][j]).append("  ");
            }
            sb.append('\n');
        }

        sb.append('\n').append("\tBandeiras Disponiveis: ").append(remainingFlags);

        long elapsedTime = System.currentTimeMillis() - startTime;
        String formattedTime = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(elapsedTime),
                TimeUnit.MILLISECONDS.toMinutes(elapsedTime) % 60,
                TimeUnit.MILLISECONDS.toSeconds(elapsedTime) % 60
        );

        sb.append('\n').append("\tTempo decorrido: ").append(formattedTime);
        sb.append('\n');
        return sb.toString();
    }
}