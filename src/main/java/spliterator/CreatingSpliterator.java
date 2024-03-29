package src.main.java.spliterator;

import src.main.java.spliterator.model.Person;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CreatingSpliterator {
    public static void main(String[] args) {
        Path path = Paths.get("src/main/ resources/People.txt");
        try (Stream<String> lines = Files.lines(path);){
           Spliterator<String> lineSpliterator = lines.spliterator();
            Spliterator<Person> peopleSpliterator = new PersonSpliterator(lineSpliterator);

           Stream<Person> people= StreamSupport.stream(peopleSpliterator,false);
            people.forEach(System.out::println);
        }
        catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
}
