package spell;

import java.io.IOException;
import java.io.File;  // Import the File class
import java.util.Scanner; // Import the Scanner class to read text files


public class SpellCorrector implements ISpellCorrector{

    //TODO: make private
    public Trie trie;

    public SpellCorrector() {
        this.trie = new Trie();
    }

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        File fBoi = new File(dictionaryFileName);
        Scanner scanner = new Scanner(fBoi);
        while(scanner.hasNext()){
            trie.add(scanner.next());
        }
        scanner.close();
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        return null;
    }

    private String[] oneEditWords(String word){
        String[] words = new String[word.length() * 53 + 25]; //computed as total from all __ distance functions
        return words;
    }

    private String[] deletion(String word){
        String[] words = new String[word.length()];
        words[0] = word.substring(1);
        for(int i = 1; i < word.length(); i++){
            words[i] = word.substring(0,i) + word.substring(i+1);
        }
        return words;
    }

    private String[] transpose(String word){
        String[] words = new String[word.length()-1];
        words[0] = String.valueOf(word.charAt(1)) + word.charAt(0) + word.substring(2);
        for(int i = 1; i < word.length()-1; i++){
            words[i] = word.substring(0, i) + word.charAt(i + 1) + word.charAt(i) + word.substring(i + 2);
        }
        return words;
    }

    private String[] alter(String word){
        String[] words = new String[word.length() * 25];
        int nonce = 0;
        for(int i = 0; i < word.length(); i++){
            for(char alpha = 'a'; alpha <= 'z'; alpha++){
                if(alpha == word.charAt(i)){
                    continue;
                }
                words[nonce] = word.substring(0,i) + alpha + word.substring(i+1);
                nonce++;
            }
        }
        return words;
    }

    private String[] insert(String word){
        String[] words = new String[(word.length() + 1) * 26];
        int nonce = 0;

        for(int i = 0; i < word.length() + 1; i++){
            for(char alpha = 'a'; alpha <= 'z'; alpha++){
                words[nonce] = word.substring(0, i) + alpha + word.substring(i);
                nonce++;
            }
        }
        return words;
    }

    public static void main(String[] args) throws Exception{


    }
}
