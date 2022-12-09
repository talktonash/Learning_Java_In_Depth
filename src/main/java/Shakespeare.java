package src.main.java;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class Shakespeare {
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
        Function<String, Integer> score =  word -> word.chars().map(letter -> scrabbleENScore[letter - 'a'] ).sum();

        //Here auto boxing doesn't happen at all, as we are performing operations directly on primitive.
        ToIntFunction<String> intScore = word -> word.chars().map(letter -> scrabbleENScore[letter - 'a'] ).sum();

        System.out.println("Score of hello :" + intScore.applyAsInt("hello"));
        String best = shakespeareWords.stream()
                        .filter(word -> scrabbleWords.contains(word))
                        .max(Comparator.comparing(score)).get();
        System.out.println("Best word :" + best);

        IntSummaryStatistics intSummaryStatistics = shakespeareWords.stream().parallel()
                .filter(scrabbleWords::contains)
                .mapToInt(intScore)
                .summaryStatistics();

        System.out.println("Stats :" + intSummaryStatistics);
    }
}
