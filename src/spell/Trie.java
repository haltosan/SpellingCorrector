package spell;

import java.util.Locale;
import java.util.Objects;

public class Trie implements ITrie{

    private final Node root;
    private int nodeCount;
    private int wordCount;

    public Trie() {
        this.root = new Node();
        this.nodeCount = 1; //including root node
        this.wordCount = 0;
    }

    @Override
    public void add(String word) { //staging function to recursive add
        addRecursive(word.toLowerCase(), root);
    }

    private void addRecursive(String word, INode curNode){
        if(word.length() < 1){
            curNode.incrementValue();
            wordCount++;
            return;
        }
        char firstLetter = word.charAt(0);
        String newWord = word.substring(1);
        INode[] children = curNode.getChildren();
        if(children[firstLetter - 'a'] == null) {
            children[firstLetter - 'a'] = new Node();
            nodeCount++;
        }
        addRecursive(newWord, children[firstLetter - 'a']);
    }

    @Override
    public INode find(String word) {
        return recursiveFind(word.toLowerCase(), root);
    }

    public INode recursiveFind(String word, INode curNode){
        if(word.length() < 1){
            return null; //if for some reason, the string is lost/no node found
        }
        if(word.length() < 2){
            if(curNode.getChildren()[word.charAt(0) - 'a'].getValue() > 0) {
                return curNode.getChildren()[word.charAt(0) - 'a']; //the child node that matches firstLetter's index
                //charAt may be redundant, but it is a safe way to ensure we're doing char subtraction
            }
            else{
                return null;
            }
        }
        return recursiveFind(word.substring(1) /*pops first letter off*/, curNode.getChildren()[word.charAt(0) - 'a'] /*same as above*/);
    }

    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public int getNodeCount() {
        return nodeCount;
    }

    @Override
    public String toString() {
        return recursiveToString(root, "", "").substring(1); //the first char is \n; this fixes my lazy fence-post-less algorithm
    }

    private static String recursiveToString(INode node, String curWord, String returnWord){
        String localReturnWord = returnWord;
        for(int i = 0; i <  node.getChildren().length; i++){
            INode child = node.getChildren()[i];
            char curChar = (char)('a' + i);
            if(child == null){
                continue;
            }
            if(child.getValue() > 0){
                localReturnWord = localReturnWord + '\n' + curWord + curChar; //new word added to list
            }
            localReturnWord = recursiveToString(child, curWord + curChar, localReturnWord);

        }
        return localReturnWord;
    }

    @Override
    public boolean equals(Object o) {
        if(this.getClass() != o.getClass()){
            return false;
        }
        Trie tryMe = (Trie)o;
        try{
            return recursiveEquals(this.root, tryMe.root);
        }
        catch (Exception e){
            return false;
        }

    }

    private boolean recursiveEquals(INode myNode, INode otherNode) throws Exception {
        if(myNode.getValue() != otherNode.getValue()){
            throw new Exception("Not equal");
        }
        for(int i = 0; i < myNode.getChildren().length; i++){
            INode myChild = myNode.getChildren()[i];
            INode otherChild = otherNode.getChildren()[i];
            if(myChild == otherChild){ //tests if they're both null
                continue;
            }
            if(myChild == null){ //tests if only 1 is null
                throw new Exception("Not equal");
            }
            else if(otherChild == null){
                throw new Exception("Not equal");
            }
            else if(myNode.getChildren()[i].getValue() != otherNode.getChildren()[i].getValue()){ //if both are not null, check values
                throw new Exception("Not equal");
            }
            else{ //if equal, look at children
                recursiveEquals(myNode.getChildren()[i], otherNode.getChildren()[i]);
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int i;
        INode[] childs = root.getChildren();
        for(i = 0; i < childs.length; i++){
            if(childs[i] != null){
                break;
            }
        }
        return i * nodeCount * wordCount * 31;
    }

    public static void main(String[] args){
        Trie tre = new Trie();
        Trie b = new Trie();
        tre.add("zyx");
        b.add("zyx");
        System.out.println(tre.equals(b));
        System.out.println(b.equals(tre)+"\n");

        tre.add("zyx");
        System.out.println(tre.equals(b));
        System.out.println(b.equals(tre)+"\n");

    }
}
