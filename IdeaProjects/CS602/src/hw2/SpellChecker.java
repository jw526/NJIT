package hw2;//package edu.njit.cs602.s2018.assignments;
//Jianchao Wang


import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class SpellChecker {

    public static final String PUNCTUATION_MARKS = ".;?";
    public static final int IGNORE_RULE_OPTION = 1;
    public static final int VALID_WORD_OPTION = 2;
    public static final int CORRECT_WORD_OPTION = 3;
    public static final int IGNORE_WORD_OPTION = 4;
    private SpellChecker.Dictionary dict;

    private class Dictionary {
        private final String dictionaryFile;
        private final Set<String> wordList = new HashSet<>();

        /**
         * Add word to dictionary
         * @param word
         */
        public void addWord(String word) {
            wordList.add(word);
        }

        /**
         * Is it a valid word ?
         * @param word
         * @return
         */
        public boolean isValid(String word) {
            return wordList.contains(word);
        }

        /**
         * Update the dictionary file
         */
        public void update() throws IOException {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(dictionaryFile));
                for (String word : wordList) {
                    writer.write(word);
                    writer.newLine();
                }
                writer.close();
            } catch(FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        /**
         * Construct dictionary from a dictionary file, one word per line
         * @param dictionaryFile
         * @throws IOException
         */
        public Dictionary(String dictionaryFile) throws IOException {
            this.dictionaryFile = dictionaryFile;
            BufferedReader reader = new BufferedReader(new FileReader(dictionaryFile));
            String line = null;
            while ((line=reader.readLine()) != null) {
                addWord(line.trim());
            }
            reader.close();
        }
    }

    /**
     * Constructs WordChecker with a dictionary given in dictionary file
     * @param dictionaryFile
     */
    public SpellChecker(String dictionaryFile) throws IOException {
        SpellChecker.Dictionary dict = new SpellChecker.Dictionary(dictionaryFile);
        this.dict = dict;
    }

    /**
     * Update dictionary file
     */
    public void updateDictionary() throws IOException {
        dict.update();
    }


    /**
     * Check words in the file targetFile and output the word in outputFile
     * @param targetFile input file
     * @param outputFile output file
     */
    public void checkWords(String targetFile, String outputFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(targetFile));
        BufferedWriter out = new BufferedWriter(new FileWriter(outputFile));
        String line = reader.readLine();
        String input = "";
        //read in target file, set it to be the input
        while(line != null){
            input = input + line + System.lineSeparator();
            line = reader.readLine();
        }
        reader.close();
        //separate the punctuation marks
        char[] punc = PUNCTUATION_MARKS.toCharArray();
        String[] tkn = input.split("[\\s]+");
        for(int i =0; i < tkn.length; i++){
            String new_tkn ="";
            for (int j = 0; j < punc.length; j++){
                //new_tkn is tkn without punctuation
                if(tkn[i].contains(String.valueOf(punc[j]))){
                   new_tkn = tkn[i].replace(String.valueOf(punc[j]),"");
                   break;
                }
                else new_tkn = tkn[i];
            }
            //Check if the word is in the dictionary. If it is, include it in the sentence to be output
            if (dict.isValid(new_tkn)){
                out.write(tkn[i]+ " ");
            }
            else {
                System.out.println("Enter option for word \""+ new_tkn +"\"\n1: Ignore Rule, 2: Valid Word, 3: Correct Word, 4: Ignore Word");
                Scanner s = new Scanner(System.in);
                int option = s.nextInt();
                switch (option) {
                    case IGNORE_RULE_OPTION:
                        out.write(tkn[i] + " ");
                        break;
                    case VALID_WORD_OPTION:
                        out.write(tkn[i] + " ");
                        dict.addWord(new_tkn);
                        break;
                    case CORRECT_WORD_OPTION:
                        System.out.println("Enter the correct word:");
                        String word = s.next();
                        //check for non alphabetic characters
                        for (int k = 0; k < word.length(); k++) {
                            if (word.toLowerCase().charAt(k) > 122 || word.toLowerCase().charAt(k) < 97) {
                                System.out.println("Enter a valid word with English characters only:");
                                word = s.next();
                                k = 0;
                            } else continue;
                        }
                        dict.addWord(word);
                        //if the original token has a punctuation mark, write the word + punctuation mark
                        if (PUNCTUATION_MARKS.contains(tkn[i].substring(new_tkn.length())))
                            out.write(word + tkn[i].substring(new_tkn.length()) + " ");
                        else
                            out.write(word + " ");
                        break;
                    case IGNORE_WORD_OPTION:
                        if (PUNCTUATION_MARKS.contains(tkn[i].substring(new_tkn.length()))) {
                            out.write(tkn[i].substring(new_tkn.length()) + " ");
                        }
                        break;
                    default:
                        System.out.println("Invalid input, try again");
                        i--;
                        break;
                }
            }
            //put a line break after 21 words
            if((i+1)%21 == 0) out.newLine();
        }
    out.close();
    }


    public static void main(String [] args) throws Exception {

        String dictionaryFile = args[0];
        SpellChecker checker = new SpellChecker(dictionaryFile);
        String inputFile = args[1];
        String outputFile = args[2];
        checker.checkWords(inputFile, outputFile);
        checker.updateDictionary();
    }
}
