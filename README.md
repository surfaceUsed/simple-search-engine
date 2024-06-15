# simple-search-engine
Search engine that takes a search text and queries a list of data for a matching result

Hyperskill project "Simple search engine (Java)" (Java Developer). Stage 6/6.

The application takes a text string (word or whole sentence, case- and space insensitive) as input, and 
searches a text-file for a match. It uses an "Inverted index" that maps each word in the search input to
the line in the file which the word occurs. 

The user can choose a startegy for what kind of search to be performed:

"ALL" -> The program prints all lines in the text that matches every word in the query-text.

"ANY" -> The program prints all lines in the text that contains at least one word from the query.

"NONE" -> The program prints all the lines in the text that does not contain words from the query at all. 


