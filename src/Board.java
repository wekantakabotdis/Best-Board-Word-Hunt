public class Board {
    private char [][] map;
    private char letter;

    public Board (int size) {
        map = new char[size][size];
        for (int r = 0; r < map.length; r++) {
            for (int c = 0; c < map[0].length; c++) {
                double number = Math.random();
                if (number < 0.001962) {
                    letter = 'Q';
                }
                else if (number < 0.003927) {
                    letter = 'J';
                }
                else if (number < 0.006649) {
                    letter = 'Z';
                }
                else if (number < 0.009551) {
                    letter = 'X';
                }
                else if (number < 0.019625) {
                    letter = 'V';
                }
                else if (number < 0.030641) {
                    letter = 'K';
                }
                else if (number < 0.04354) {
                    letter = 'W';
                }
                else if (number < 0.061319) {
                    letter = 'Y';
                }
                else if (number < 0.07944) {
                    letter = 'F';
                }
                else if (number < 0.10016) {
                    letter = 'B';
                }
                else if (number < 0.124865) {
                    letter = 'G';
                }
                else if (number < 0.154899) {
                    letter = 'H';
                }
                else if (number < 0.185028) {
                    letter = 'M';
                }
                else if (number < 0.216699) {
                    letter = 'P';
                }
                else if (number < 0.250543) {
                    letter = 'D';
                }
                else if (number < 0.286851) {
                    letter = 'U';
                }
                else if (number < 0.332239) {
                    letter = 'C';
                }
                else if (number < 0.387132) {
                    letter = 'L';
                }
                else if (number < 0.444483) {
                    letter = 'S';
                }
                else if (number < 0.511027) {
                    letter = 'N';
                }
                else if (number < 0.580536) {
                    letter = 'T';
                }
                else if (number < 0.652171) {
                    letter = 'O';
                }
                else if (number < 0.727619) {
                    letter = 'I';
                }
                else if (number < 0.803428) {
                    letter = 'R';
                }
                else if (number < 0.888394) {
                    letter = 'A';
                }
                else if (number < 1.0000001) {
                    letter = 'E';
                }
                map[r][c] = letter;
            }
        }
    }

    public char[][] getMap () {
        return this.map;
    }

    public void printBoard () {
        for(int r = 0; r < map.length; r++) {
            for (int c = 0; c < map[0].length; c++) {
                System.out.print(map[r][c] + " ");
            }
            System.out.println();
        }
    }

}