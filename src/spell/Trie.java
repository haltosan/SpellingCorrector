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
        return recursiveEquals(this.root, tryMe.root);
    }

    private boolean recursiveEquals(INode myNode, INode otherNode) {
        if(myNode.getValue() != otherNode.getValue()){
            System.out.println("false on values");
            return false;
        }
        INode[] myChildren = myNode.getChildren();
        INode[] otherChildren = myNode.getChildren();
        for(int i = 0; i < myChildren.length; i++){
            if(myChildren[i] != null && otherChildren[i] != null){
                System.out.println("recurse "+' '+i);
                if(!recursiveEquals(myChildren[i], otherChildren[i])){ //any false's should go up the chain
                    System.out.println("chain");
                    return false;
                }
            }
            if(myChildren[i] == null && otherChildren[i] != null){
                System.out.println("null");
                return false;
            }
            if(myChildren[i] != null && otherChildren[i] == null){
                System.out.println("null");
                return false;
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
        tre.add("abcde");
        System.out.println(tre.equals(b));
        System.out.println(b.equals(tre));
    }
}
