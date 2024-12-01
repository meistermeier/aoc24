///usr/bin/env jbang "$0" "$@" ; exit $?

import static java.lang.System.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class day_1 {

	static String exampleInput = """
			3   4
			4   3
			2   5
			1   3
			3   9
			3   3""";

	static Path input1 = Path.of("day1_1_input");

	static List<Integer> leftSide = new ArrayList<>();
	static List<Integer> rightSide = new ArrayList<>();

	public static void main(String... args) throws Exception {
//		var inputReader = new StringReader(exampleInput);
		var inputReader = new FileReader(input1.toFile());
		try (var bufferedReader = new BufferedReader(inputReader)) {
			bufferedReader.lines().forEach(line -> {
				if (!line.isBlank()) {
					var lineItems = line.trim().split("[ ]+");
					var leftItem = lineItems[0].trim();
					var rightItem = lineItems[1].trim();
					leftSide.add(Integer.parseInt(leftItem));
					rightSide.add(Integer.parseInt(rightItem));
				}
			});
			leftSide.sort((o1, o2) -> o1.compareTo(o2));
			rightSide.sort((o1, o2) -> o1.compareTo(o2));

			Integer totalDistance = 0;
			for (int i = 0; i < leftSide.size(); i++) {
				var localDistance = Math.abs(leftSide.get(i) - rightSide.get(i));
				totalDistance += localDistance;
			}

			out.println("Answer 1: " + totalDistance);

			Map<Integer, Integer> cache = new HashMap<>();
			var answer2 = 0;
			for (int i = 0; i < leftSide.size(); i++) {
				var leftItem = leftSide.get(i);
				answer2 += cache.computeIfAbsent(leftItem, left -> {
					var total = 0;
					for (Integer rightItem : rightSide) {
						if (leftItem.equals(rightItem)) {
							total += 1;
						}
					}
					return total * leftItem;
				});
			}
			out.println("Answer 2: " + answer2);
		}
	}
}
