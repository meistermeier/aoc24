/// usr/bin/env jbang "$0" "$@" ; exit $?


import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.List;

import static java.lang.System.out;

public class day_4 {

    static String exampleInput = """
        MMMSXXMASM
        MSAMXMSMSA
        AMXSXMAAMM
        MSAMASMSMX
        XMASAMXAMM
        XXAMMXXAMA
        SMSMSASXSS
        SAXAMASAAA
        MAMMMXMMMM
        MXMXAXMASX""";

    static Path input = Path.of("day4_1_input");

    public static void main(String... args) throws Exception {
        try (
//            var inputReader = new StringReader(exampleInput);
            var inputReader = new FileReader(input.toFile());
            var bufferedReader = new BufferedReader(inputReader)) {
            part1(bufferedReader.lines().toList());
        }
        try (
//            var inputReader = new StringReader(exampleInput);
            var inputReader = new FileReader(input.toFile());
            var bufferedReader = new BufferedReader(inputReader)) {
            part2(bufferedReader.lines().toList());
        }

    }

    private static void part1(List<String> lines) {
        var puzzle = fillPuzzle(lines);

        int foundWords = 0;
        for (int rowIndex = 0; rowIndex < puzzle.length; rowIndex++) {
            for (int columnIndex = 0; columnIndex < puzzle[rowIndex].length; columnIndex++) {
                for (Direction direction : Direction.values()) {
                    if ("XMAS".equals(extractWord(puzzle, rowIndex, columnIndex, direction))) {
                        foundWords++;
                    }
                }
            }
        }
        out.println("Answer 1: " + foundWords);
    }

    private static void part2(List<String> lines) {
        var puzzle = fillPuzzle(lines);

        int foundWords = 0;
        for (int rowIndex = 0; rowIndex < puzzle.length; rowIndex++) {
            for (int columnIndex = 0; columnIndex < puzzle[rowIndex].length; columnIndex++) {
                if (xSameWord("MAS", puzzle, rowIndex, columnIndex)) {
                    foundWords++;
                }
            }
        }
        out.println("Answer 2: " + foundWords);
    }

    enum Direction {
        N(-1, 0),
        NE(-1, 1),
        E(0, 1),
        SE(1, 1),
        S(1, 0),
        SW(1, -1),
        W(0, -1),
        NW(-1, -1);

        private final int y;
        private final int x;

        Direction(int y, int x) {
            this.y = y;
            this.x = x;
        }
    }

    private static String extractWord(String[][] puzzle, int rowIndex, int columnIndex, Direction direction) {
        var first = puzzle[rowIndex][columnIndex];
        var second = isInBounds(puzzle, rowIndex + (direction.y * 1), columnIndex + (direction.x * 1)) ? puzzle[rowIndex + (direction.y * 1)][columnIndex + (direction.x * 1)] : "_";
        var third = isInBounds(puzzle, rowIndex + (direction.y * 2), columnIndex + (direction.x * 2)) ? puzzle[rowIndex + (direction.y * 2)][columnIndex + (direction.x * 2)] : "_";
        var fourth = isInBounds(puzzle, rowIndex + (direction.y * 3), columnIndex + (direction.x * 3)) ? puzzle[rowIndex + (direction.y * 3)][columnIndex + (direction.x * 3)] : "_";

        return first + second + third + fourth;
    }

    private static boolean xSameWord(String word, String[][] puzzle, int rowIndex, int columnIndex) {
        var secondFixed = puzzle[rowIndex][columnIndex];
        var firstCandidate = isInBounds(puzzle, rowIndex + Direction.NW.y, columnIndex + Direction.NW.x) ? puzzle[rowIndex + Direction.NW.y][columnIndex + Direction.NW.x] : "_";
        var thirdCandidate = isInBounds(puzzle, rowIndex + Direction.SE.y, columnIndex + Direction.SE.x) ? puzzle[rowIndex + Direction.SE.y][columnIndex + Direction.SE.x] : "_";
        var fourthCandidate = isInBounds(puzzle, rowIndex + Direction.NE.y, columnIndex + Direction.NE.x) ? puzzle[rowIndex + Direction.NE.y][columnIndex + Direction.NE.x] : "_";
        var fifthCandidate = isInBounds(puzzle, rowIndex + Direction.SW.y, columnIndex + Direction.SW.x) ? puzzle[rowIndex + Direction.SW.y][columnIndex + Direction.SW.x] : "_";

        var firstWord = firstCandidate + secondFixed + thirdCandidate;
        var secondWord = fourthCandidate + secondFixed + fifthCandidate;
        var thirdWord = thirdCandidate + secondFixed + firstCandidate;
        var fourthWord = fifthCandidate + secondFixed + fourthCandidate;
        return (word.equals(firstWord) && word.equals(secondWord))
               || (word.equals(thirdWord) && word.equals(fourthWord))
               || (word.equals(firstWord) && word.equals(fourthWord))
               || (word.equals(thirdWord) && word.equals(secondWord));
    }

    private static boolean isInBounds(String[][] puzzle, int rowIndex, int columnIndex) {
        return rowIndex >= 0 && columnIndex >= 0 && rowIndex < puzzle.length && columnIndex < puzzle[0].length;
    }

    private static String[][] fillPuzzle(List<String> lines) {
        var puzzle = new String[lines.get(0).length()][lines.size()];
        for (var i = 0; i < lines.size(); i++) {
            String[] line = lines.get(i).split("");
            System.arraycopy(line, 0, puzzle[i], 0, line.length);
        }
        return puzzle;
    }
}
