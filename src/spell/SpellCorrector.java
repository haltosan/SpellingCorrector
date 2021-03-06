package spell;

import java.io.IOException;
import java.io.File;  // Import the File class
import java.util.*;


public class SpellCorrector implements ISpellCorrector{

    private final Trie trie;

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
        inputWord = inputWord.toLowerCase();
        //find the word exactly
        INode canFind = trie.find(inputWord);
        if(canFind != null){
            return inputWord;
        }
        //look at one edit distance
        TreeSet<String> oneEdits = new TreeSet<>(oneEditWords(inputWord));
        Dictionary<String, Integer> found = getFound(oneEdits);
        if(!found.isEmpty()){
            Set<String> ties = getBestMatch(found);
            return (String)ties.toArray()[0]; //I hope this gives the 1st item alphabetically; let's see
        }

        //look at 2 edit distance words
        TreeSet<String> twoEdits = new TreeSet<>();
        for(String oneEditWord : oneEdits){
            twoEdits.addAll(oneEditWords(oneEditWord)); //I think this works...
        }
        Dictionary<String, Integer> found2 = getFound(twoEdits);
        if(!found2.isEmpty()){
            Set<String> ties = getBestMatch(found2);
            return (String)ties.toArray()[0];
        }
        return null;
    }

    private Set<String> getBestMatch(Dictionary<String, Integer> found) {
        int maxScore = -1;
        Enumeration<String> enumeration = found.keys();
        Set<String> ties = new TreeSet<>();
        while(enumeration.hasMoreElements()){
            String potentialWord = enumeration.nextElement(); //don't ask why, but this is how I'm iterating through this
            if(found.get(potentialWord) > maxScore){
                ties = new TreeSet<>(); //reset the ties
                maxScore = found.get(potentialWord);
            }
            if(found.get(potentialWord) == maxScore){
                ties.add(potentialWord);
            }
        }
        return ties;
    }

    private Dictionary<String, Integer> getFound(Set<String> nEdits) {
        Dictionary<String, Integer> found = new Hashtable<>();
        for(String potentialWord : nEdits){
            INode lookup = trie.find(potentialWord);
            if(lookup != null){
                found.put(potentialWord, lookup.getValue());
            }
        }
        return found;
    }

    private TreeSet<String> oneEditWords(String word){
        //n + (n-1) + 25n + 26(n+1) = 53n + 25
        TreeSet<String> words = new TreeSet<>();
        String[] deletionWords = word.length() < 2 ? new String[0] : deletion(word);
        String[] transposeWords = word.length() < 2 ? new String[0] : transpose(word);
        String[] alterWords = alter(word);
        String[] insertWords = insert(word);


        words.addAll(Arrays.asList(deletionWords));
        words.addAll(Arrays.asList(transposeWords));
        words.addAll(Arrays.asList(alterWords));
        words.addAll(Arrays.asList(insertWords));

        return words;
    }

    private String[] deletion(String word){
        String[] words = new String[word.length()];
        if(word.length() < 1){
            return new String[0];
        }
        words[0] = word.substring(1);
        for(int i = 1; i < word.length(); i++){
            words[i] = word.substring(0,i) + word.substring(i+1);
        }
        return words;
    }

    private String[] transpose(String word){
        if(word.length() < 2){
            return new String[0];
        }
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

    public static void main(String[] args) {
    }
}
