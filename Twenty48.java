package org.cis1200.twenty48;

import java.util.Random;
import java.util.Stack;
public class Twenty48 {

    private int[][] board;
    private Stack<int[][]> stack = new Stack<>();
    private int numTurns;
    private boolean player1;
    private boolean gameOver;
    private int score;

    /**
     * Constructor sets up game state.
     */
    public Twenty48() {
        reset();
    }

    public void gen() {
        Random random = new Random();
        int row = random.nextInt(4);
        int col = random.nextInt(4);
        while (board[row][col] != 0) {
            row = random.nextInt(4);
            col = random.nextInt(4);
        }
        board[row][col] = 2;
    }

    public void left() {
        boolean moved = false;
        for (int r = 0; r < board.length; r++) {
            int c = 0;
            for (c = 0; c < board.length; c++) {
                if (board[r][c] != 0) {
                    break;
                }
            }
            for (int c2 = c + 1; c2 < board.length; c2++) {
                if (board[r][c2] == 0) {
                    continue;
                }
                if (board[r][c] == board[r][c2]) {
                    board[r][c] *= 2;
                    score += board[r][c];
                    board[r][c2] = 0;
                    c = c2 + 1;
                    c2 = c;
                    moved = true;
                } else {
                    if (c2 == board.length - 1) {
                        c++;
                        c2 = c;
                    }
                }
            }
            int empty = 0;
            for (c = 0; c < 4; c++) {
                if (board[r][c] == 0) {
                    empty++;
                } else if (empty != 0) {
                    board[r][c - empty] = board[r][c];
                    board[r][c] = 0;
                    moved = true;
                }
            }
        }
        if (moved) {
            gen();
            add();
        }
    }

    public void up() {
        boolean moved = false;
        for (int c = 0; c < board.length; c++) {
            int r = 0;
            for (r = 0; r < board.length; r++) {
                if (board[r][c] != 0) {
                    break;
                }
            }
            for (int r2 = r + 1; r2 < board.length; r2++) {
                if (board[r2][c] == 0) {
                    continue;
                }
                if (board[r][c] == board[r2][c]) {
                    board[r][c] *= 2;
                    board[r2][c] = 0;
                    r = r2 + 1;
                    r2 = r;
                    moved = true;
                } else {
                    r++;
                    r2 = r;
                }
            }
            int empty = 0;
            for (r = 0; r < 4; r++) {
                if (board[r][c] == 0) {
                    empty++;
                } else if (empty != 0) {
                    board[r - empty][c] = board[r][c];
                    board[r][c] = 0;
                    moved = true;
                }
            }
        }
        if (moved) {
            gen();
            add();
        }
    }

    public void right() {
        boolean moved = false;
        for (int r = 0; r < board.length; r++) {
            int c = 3;
            for (c = 3; c >= 0; c--) {
                if (board[r][c] != 0) {
                    break;
                }
            }
            for (int c2 = c - 1; c2 >= 0; c2--) {
                if (board[r][c2] == 0) {
                    continue;
                }
                if (board[r][c] == board[r][c2]) {
                    board[r][c] *= 2;
                    score += board[r][c];
                    board[r][c2] = 0;
                    c = c2 - 1;
                    c2 = c;
                    moved = true;
                } else {
                    c--;
                    c2 = c;
                }
            }
            int empty = 0;
            for (c = 3; c >= 0; c--) {
                if (board[r][c] == 0) {
                    empty++;
                } else if (empty != 0) {
                    board[r][c + empty] = board[r][c];
                    board[r][c] = 0;
                    moved = true;
                }
            }
        }
        if (moved) {
            gen();
            add();
        }
    }

    public void down() {
        boolean moved = false;
        for (int c = 0; c < board.length; c++) {
            int r = 3;
            for (r = 3; r >= 0; r--) {
                if (board[r][c] != 0) {
                    break;
                }
            }
            for (int r2 = r - 1; r2 >= 0; r2--) {
                if (board[r2][c] == 0) {
                    continue;
                }
                if (board[r][c] == board[r2][c]) {
                    board[r][c] *= 2;
                    score += board[r][c];
                    board[r2][c] = 0;
                    r = r2 - 1;
                    r2 = r;
                    moved = true;
                } else {
                    r--;
                    r2 = r;
                }
            }
            int empty = 0;
            for (r = 3; r >= 0; r--) {
                if (board[r][c] == 0) {
                    empty++;
                } else if (empty != 0) {
                    board[r + empty][c] = board[r][c];
                    board[r][c] = 0;
                    moved = true;
                }
            }
        }
        if (moved) {
            gen();
            add();
        }
    }

    public boolean checkGameOver() {
        // Check for empty cells
        for (int[] row : board) {
            for (int value : row) {
                if (value == 0) {
                    return false; // Not game over, there is still room
                }
            }
        }

        // Check for possible merges
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if ((r > 0 && board[r][c] == board[r - 1][c]) ||
                        (r < board.length - 1 && board[r][c] == board[r + 1][c]) ||
                        (c > 0 && board[r][c] == board[r][c - 1]) ||
                        (c < board[r].length - 1 && board[r][c] == board[r][c + 1])) {
                    return false;
                }
            }
        }

        return true;
    }

    public void add() {
        int[][] temp = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                temp[i][j] = board[i][j];
            }
        }
        stack.push(temp);
    }

    /**
     * printGameState prints the current game state
     * for debugging.
     */
    public int[][] getBoard() {
        return board;
    }

    public void print(int[][] arr) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(arr[i][j]);
            }
            System.out.println();
        }
    }

    public void set(int r, int c, int value) { // testing purposes
        board[r][c] = value;
    }

    public int get(int r, int c) { // testing purposes
        return board[r][c];
    }

    public int getCell(int c, int r) {
        return board[r][c];
    }

    public Stack<int[][]> getStack() { // testing purposes
        return stack;
    }

    public void clear() {
        stack.clear();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j] = 0;
            }
        }
    }

    public boolean contains(int r, int c) {
        return board[r][c] > 0;
    }

    public boolean check(int r, int c, int val) {
        return board[r][c] == val;
    }

    /**
     * reset (re-)sets the game state to start a new game.
     */
    public void load() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j] = stack.peek()[i][j];
            }
        }
    }

    public void reset() {
        board = new int[4][4];
        gen();
        gen();
        stack = new Stack<>();
        add();
        // numTurns = 0;
        // player1 = true;
        // gameOver = false;
    }

    public void undo() {
        if (stack.size() > 1) {
            stack.pop();
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    board[i][j] = stack.peek()[i][j];
                }
            }
        }
    }

    public int getScore() {
        return score;
    }

    public static void main(String[] args) {
        Twenty48 t = new Twenty48();
    }
}
