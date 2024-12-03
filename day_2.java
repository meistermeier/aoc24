/// usr/bin/env jbang "$0" "$@" ; exit $?


import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.lang.System.out;

public class day_2 {
    static String exampleInput = """
        7 6 4 2 1
        1 2 7 8 9
        9 7 6 2 1
        1 3 2 4 5
        8 6 4 4 1
        1 3 6 7 9""";

    static Path input1 = Path.of("day2_1_input");

    public static void main(String... args) throws Exception {
        try (
//            var inputReader = new StringReader(exampleInput);
            var inputReader = new FileReader(input1.toFile());
            var bufferedReader = new BufferedReader(inputReader)) {
            part1(bufferedReader.lines());
        }
        try (
//            var inputReader = new StringReader(exampleInput);
            var inputReader = new FileReader(input1.toFile());
            var bufferedReader = new BufferedReader(inputReader)) {
            part2(bufferedReader.lines());
        }
    }

    private static void part1(Stream<String> lines) {
        var safeReports = lines.filter(lineCheck(false)).count();
        out.println("Answer 1: " + safeReports);
    }

    private static void part2(Stream<String> lines) {
        var safeReports = lines.filter(lineCheck(true)).count();
        out.println("Answer 2: " + safeReports);
    }

    private static Predicate<String> lineCheck(boolean thisIsFine) {
        return line -> {
            List<Integer> lineItems = Arrays.stream(line.split(" ")).map(Integer::parseInt).toList();
            var result = new ArrayList<>(lineItems).stream().reduce(Integer.MAX_VALUE, isLineOk(), Integer::sum) >= 0;
            if (result) {
                return true;
            } else if (thisIsFine) {
                for (var i = 0; i < lineItems.size(); i++) {
                    var tooMuchHeadacheTodayToMakeThingsNice = new ArrayList<>(lineItems);
                    tooMuchHeadacheTodayToMakeThingsNice.remove(i);
                    if (tooMuchHeadacheTodayToMakeThingsNice.stream().reduce(Integer.MAX_VALUE, isLineOk(), Integer::sum) >= 0) {
                        return true;
                    }
                }
            }
            return false;
        };
    }

    private static BiFunction<Integer, Integer, Integer> isLineOk() {
        AtomicInteger trend = new AtomicInteger(0);
        return (i1, i2) -> {
            if (Integer.MAX_VALUE == i1) {
                return i2;
            }
            if (trend.get() == 0) {
                trend.set(i2.compareTo(i1));
                if (trend.get() == 0) {
                    return Integer.MIN_VALUE;
                }
            }
            var localTrend = i2.compareTo(i1);
            var diff = Math.abs(i1 - i2);
            return localTrend == trend.get() && diff < 4D && diff != 0 ? i2 : Integer.MIN_VALUE;
        };
    }

}
