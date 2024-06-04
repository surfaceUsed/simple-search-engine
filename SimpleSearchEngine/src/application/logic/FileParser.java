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
    private final Map<String, List<Integer>> personMapper;

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
            listMapper();

        } catch (IOException e) {
            System.out.println("Couldn't read file: " + e.getMessage());
        }
    }

    private void listMapper() {

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

        switch (strategy) {

            case ALL:
                searchAll(query);
                break;

            case ANY:
                searchAny(query);
                break;

            case NONE:
                searchNone(query);
                break;

            default:
                System.out.println("Invalid search strategy!");
        }
    }

    private void searchAll(String query) {
        String[] search = query.toLowerCase().split("\\s+");
        if (search.length != 0) {
            Map<Integer, Integer> indexMap = indexMapper(search);
            if (!indexMap.isEmpty()) {
                for (Integer numb : indexMap.keySet()) {
                    if (indexMap.get(numb) == search.length) {
                        System.out.println(this.personList.get(numb));
                    }
                }
            } else {
                System.out.println("No match found!");
            }
        } else {
            System.out.println("Query is empty!");
        }
    }

    private void searchAny(String query) {
        String[] search = query.toLowerCase().split("\\s+");
        if (search.length != 0) {
            Set<Integer> keys = indexMapper(search).keySet();
            if (!keys.isEmpty()) {
                for (Integer index : keys) {
                    System.out.println(this.personList.get(index));
                }
            } else {
                System.out.println("No match found!");
            }
        } else {
            System.out.println("Query is empty!");
        }
    }

    private void searchNone(String query) {
        String[] search = query.toLowerCase().split("\\s+");
        if (search.length != 0) {
            Set<Integer> keys = indexMapper(search).keySet();
            if (!keys.isEmpty()) {
                for (int i = 0; i < this.personList.size(); i++) {
                    if (!isMatchingIndex(keys, i)) {
                        System.out.println(this.personList.get(i));
                    }
                }
            } else {
                System.out.println("No match found!");
            }
        } else {
            System.out.println("Query is empty!");

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