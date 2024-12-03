/// usr/bin/env jbang "$0" "$@" ; exit $?


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

public class day_3 {

    static String exampleInput = """
        xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))""";

    static Pattern pattern1 = Pattern.compile("(mul\\((\\d{1,3}),(\\d{1,3})\\))");
    static Pattern pattern2 = Pattern.compile("(mul\\((\\d{1,3}),(\\d{1,3})\\))|(don't\\(\\))|(do\\(\\))");

    static Path input = Path.of("day3_1_input");

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
        var result = 0L;
        for (String line : lines) {
            var matcher = pattern1.matcher(line);
            while (matcher.find()) {
                var first = Long.parseLong(matcher.group(2));
                var second = Long.parseLong(matcher.group(3));
                result += first * second;
            }
        }
        // 168539636
        System.out.println("Answer 1: " + result);
    }

    private static void part2(List<String> lines) {
        var result = 0L;
        var doMul = true; // enabled by default
        for (String line : lines) {
            var matcher = pattern2.matcher(line);
            while (matcher.find()) {
                if (matcher.group(0).startsWith("mul")) {
                    var first = Long.parseLong(matcher.group(2));
                    var second = Long.parseLong(matcher.group(3));
                    if (doMul) {
                        result += first * second;
                    }
                } else {
                    doMul = "do()".equals(matcher.group(0));
                }
            }
        }
        // 168539636
        System.out.println("Answer 2: " + result);
    }
}
