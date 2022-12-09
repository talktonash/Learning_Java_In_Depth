package src.main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MainUsingCollectors {
    public static void main(String[] args) throws IOException {
        Set<String> shakespeareWords = Files.lines(Paths.get("src/main/resources/words.shakespear.txt"))
                .map(word -> word.toLowerCase())
                .collect(Collectors.toSet());
        Set<String> scrabbleWords = Files.lines(Paths.get("src/main/resources/ospd.txt"))
                .map(word -> word.toLowerCase())
                .collect(Collectors.toSet());

        System.out.println("# Words Of Shakespeare :" + shakespeareWords.size());
        System.out.println("# WOrds Of Scrabble :" + scrabbleWords.size());

        final int[] scrabbleENScore = {
                // a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p,  q, r, s, t, u, v, w, x, y,  z
                1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10} ;
        // Here Ascii code is used to calculate the points by identifying the position of the characters
        // assuming a to z is small and points are ordered naturally for a to z.
        // Example: Ascii code of z = 122, a = 97 hence, z -a = 25 then scrabbleENScore[25] = 10.
        // old trick from C :)

        //Here, int is auto boxed to Integer, which is costly operation, offcourse based on the size.
        //if size is small, can be negligible.
        Function<String, Integer> score = word -> word.toLowerCase().chars().map(letter -> scrabbleENScore[letter - 'a'] ).sum();

        Map<Integer, List<String>> histoWordsByScore = shakespeareWords.stream()
                .filter(words -> scrabbleWords.contains(words))
                .collect(Collectors.groupingBy(score));
        System.out.println("# histoWordsByScore " + histoWordsByScore.size());

        histoWordsByScore.entrySet().stream() // Set<Map.Entry<Inteeger, List<String>>>
                .sorted(Comparator.comparing(entry -> -entry.getKey()))
                .limit(3)
                .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue()));

        int [] scrabbleENDistribution = {
                // a, b, c, d,  e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z
                9, 2, 2, 1, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1} ;

        Function<String, Map<Integer,Long>> histtoWord = word ->word.chars().boxed()
                .collect(
                        Collectors.groupingBy(
                                letter -> letter, Collectors.counting()
                        )
                );

        Function<String,Long> nBlanks = word -> histtoWord.apply(word) // Map<letter, # of letter>
                .entrySet().stream() //Map.Entry<Integer, long>
                .mapToLong(
                        entry -> Long.max(entry.getValue() - (long)scrabbleENDistribution[entry.getKey() - 'a'],0L)
                )
                .sum();

        System.out.println("# no. of blanks for whizzing: " + nBlanks.apply("whizzing"));

        Function<String, Integer> score2 = word -> histtoWord.apply(word) // Map<letter, # of letter>
                .entrySet().stream() //Map.Entry<Integer, long>
                .mapToInt(
                        entry ->
                        scrabbleENScore[entry.getKey() - 'a'] * Integer.min(
                                entry.getValue().intValue(), scrabbleENDistribution[entry.getKey() -'a']
                        )
                )
        .sum();

        System.out.println("# score for whizzing : " + score.apply("whizzing"));
        System.out.println("# score2 for whizzing : " + score2.apply("whizzing"));

    }
}
