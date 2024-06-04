package application.logic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileParser {

    private final static String ALL = "all";
    private final static String ANY = "any";
    private final static String NONE = "none";

    private final String filePath;
    private final Map<String, List<Integer>> personMapper; // Inverted index data structure.

    private List<String> personList;

    FileParser(String filePath) {
        this.filePath = filePath;
        this.personMapper = new HashMap<>();
        this.personList = new ArrayList<>();
        initList();
    }

    private void initList() {
        try(Stream<String> fileStream = Files.lines(Paths.get(this.filePath))) {

            this.personList = fileStream.collect(Collectors.toList());
            initListMapper();

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    // Maps each word in the text file to the line which the word occurs, and adds each occurence to an ArrayList (the line number starts at index 0).
    private void initListMapper() {

        for (int i = 0; i < this.personList.size(); i++)  {
            String[] tab = this.personList.get(i).toLowerCase().split("\\s+");
            int index = 0;
            while (index < tab.length) {
                this.personMapper.putIfAbsent(tab[index], new ArrayList<>());
                if (this.personMapper.containsKey(tab[index])) {
                    this.personMapper.get(tab[index]).add(i);
                }
                index++;
            }
        }
    }

    void queryList(String strategy, String query) {

        String[] search = query.toLowerCase().split("\\s+");

        if (search.length != 0) {

            Map<Integer, Integer> indexMap = indexMapper(search);

            if (!indexMap.isEmpty()) {

                switch (strategy) {

                    case ALL:
                        searchAll(indexMap, search.length);
                        break;

                    case ANY:
                        searchAny(indexMap.keySet());
                        break;

                    case NONE:
                        searchNone(indexMap.keySet());
                        break;

                    default:
                        System.out.println("Invalid search strategy!");
                        break;
                }

            } else {
                System.out.println("No match found!");
            }

        } else {
            System.out.println("Query is empty!");
        }
    }

    // Search query needs to match all the words of a sentence in the text file to be valid. If one of the words in the query has no match 
    // then nothing will be printed. All the sentences in the text file that matches the search query will be printed. 
    private void searchAll(Map<Integer, Integer> indexMap, int wordsInSearch) {
        for (Integer numb : indexMap.keySet()) {
            if (indexMap.get(numb) == wordsInSearch) {
                System.out.println(this.personList.get(numb));
            }
        }
    }

    // Prints all the the sentences in the text file that matches with a single word from the search query. 
    private void searchAny(Set<Integer> keys) {
        for (Integer index : keys) {
            System.out.println(this.personList.get(index));
        }
    }

    // Prints all the sentences in the text file that produces no matches from the search query. 
    private void searchNone(Set<Integer> keys) {
        for (int i = 0; i < this.personList.size(); i++) {
            if (!isMatchingIndex(keys, i)) {
                System.out.println(this.personList.get(i));
            }
        }
    }

    private boolean isMatchingIndex(Set<Integer> setNumbers, int index) {
        for (Integer numb : setNumbers) {
            if (numb == index) {
                return true;
            }
        }
        return false;
    }

    private Map<Integer, Integer> indexMapper(String[] query) {
        Map<Integer, Integer> indexMap = new HashMap<>();
        for (String word : query) {
            if (this.personMapper.containsKey(word)) {
                List<Integer> indexList = this.personMapper.get(word);
                for (Integer index : indexList) {
                    indexMap.putIfAbsent(index, 0);
                    if (indexMap.containsKey(index)) {
                        int value = indexMap.get(index);
                        indexMap.put(index, ++value);
                    }
                }
            }
        }
        return indexMap;
    }

    public void printList() {
        this.personList
                .forEach(System.out::println);
        System.out.println();
    }
}
