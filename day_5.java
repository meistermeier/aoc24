/// usr/bin/env jbang "$0" "$@" ; exit $?


import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.out;

public class day_5 {
    static String exampleInput = """
        47|53
        97|13
        97|61
        97|47
        75|29
        61|13
        75|53
        29|13
        97|29
        53|29
        61|53
        97|53
        61|29
        47|13
        75|47
        97|75
        47|61
        75|61
        47|29
        75|13
        53|13
        
        75,47,61,53,29
        97,61,53,29,13
        75,29,13
        75,97,47,61,53
        61,13,29
        97,13,75,29,47""";

    static Path input = Path.of("day5_1_input");

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

    static void part1(List<String> lines) {
        // parse rules
        List<Rule> rules = new ArrayList<>();
        var i = 0;
        var line = lines.get(i);
        while (!line.isBlank()) {
            rules.add(Rule.from(line));
            line = lines.get(++i);
        }
        i++; // jump over blank line
        List<Update> updates = new ArrayList<>();
        for (var u = i; u < lines.size(); u++) {
            updates.add(Update.from(lines.get(u)));
        }

        var result = updates.stream().filter(update -> update.isSorted(rules)).map(Update::getMiddleNumber)
            .reduce(Integer::sum);
        out.println(result);
    }

    static void part2(List<String> lines) {
        // parse rules
        List<Rule> rules = new ArrayList<>();
        var i = 0;
        var line = lines.get(i);
        while (!line.isBlank()) {
            rules.add(Rule.from(line));
            line = lines.get(++i);
        }
        i++; // jump over blank line
        List<Update> updates = new ArrayList<>();
        for (var u = i; u < lines.size(); u++) {
            updates.add(Update.from(lines.get(u)));
        }

        var result = updates.stream()
            .filter(update -> !update.isSorted(rules))
            .map(update -> update.sortBy(rules))
            .map(Update::getMiddleNumber)
            .reduce(Integer::sum);
        // x > 5883
        out.println(result);
    }

    record Rule(int first, int second) {
        static Rule from(String line) {
            var parts = line.split("\\|");
            return new Rule(Integer.parseInt(parts[0].trim()), Integer.parseInt(parts[1].trim()));
        }
    }

    record RuleSet(List<Rule> rules) {
        List<Integer> getStrictOrder() {
            List<Integer> sortedNumbers = new ArrayList<>();
            boolean correct = false;
            while (!correct) {
                for (Rule rule : rules) {
                    var firstNumber = rule.first;
                    var secondNumber = rule.second;

                    if (sortedNumbers.contains(firstNumber) && sortedNumbers.contains(secondNumber)) {
                        int secondNumberPosition = sortedNumbers.indexOf(secondNumber);
                        if (sortedNumbers.indexOf(firstNumber) < secondNumberPosition) {
                            continue; // nothing to do, repetition
                        }
                        sortedNumbers.remove((Integer) firstNumber);
                        sortedNumbers.add(secondNumberPosition, firstNumber);
                    } else if (sortedNumbers.contains(firstNumber)) {
                        var firstNumberPosition = sortedNumbers.indexOf(firstNumber);
                        if (!sortedNumbers.contains(secondNumber)) {
                            sortedNumbers.add(firstNumberPosition + 1, secondNumber);
                        }
                    } else if (sortedNumbers.contains(secondNumber)) {
                        var secondNumberPosition = sortedNumbers.indexOf(secondNumber);
                        sortedNumbers.add(secondNumberPosition, firstNumber);
                    } else if (!sortedNumbers.contains(firstNumber) && !sortedNumbers.contains(secondNumber)) {
                        sortedNumbers.add(rule.first);
                        sortedNumbers.add(rule.second);
                    }
                }
                correct = isCorrect(sortedNumbers);
            }
            return sortedNumbers;
        }

        private boolean isCorrect(List<Integer> sortedNumbers) {
            for (Rule rule : rules) {
                if (sortedNumbers.indexOf(rule.first) > sortedNumbers.indexOf(rule.second)) {
                    return false;
                }
            }
            return true;
        }
    }

    record Update(List<Integer> numbers) {
        static Update from(String line) {
            List<Integer> numbers = Arrays.stream(line.split(","))
                .map(String::trim).map(Integer::parseInt).toList();
            return new Update(numbers);
        }

        int getMiddleNumber() {
            return numbers.get(numbers.size() / 2); // rounding, but not in a nice way ;)
        }

        boolean isSorted(List<Rule> rules) {
            List<Rule> rulesToApply = rules.stream()
                .filter(rule -> this.numbers.contains(rule.first) && this.numbers.contains(rule.second))
                .toList();
            for (Rule rule : rulesToApply) {
                if (numbers.indexOf(rule.first) > numbers.indexOf(rule.second)) {
                    return false;
                }
            }
            return true;
        }


        Update sortBy(List<Rule> rules) {
            List<Rule> rulesToApply = rules.stream()
                .filter(rule -> this.numbers.contains(rule.first) && this.numbers.contains(rule.second))
                .toList();
           
            return new Update(new RuleSet(rulesToApply).getStrictOrder());
        }

    }


}
