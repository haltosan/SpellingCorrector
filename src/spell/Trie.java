package spell;

import java.util.Locale;

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
            return null; //for some reason, the string is lost/no node found
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
        /*
        for node:
            if node.count > 0:
                returnWord += curWord + indexToChar
            if node is null:
                pass
            else:
                recurse(node, curWord + indexToChar, returnWord
         */
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

    public static void main(String[] args){
        Trie tre = new Trie();
        tre.add("hi");
        tre.add("this");
        tre.add("is");
        tre.add("a");
        tre.add("bucket");
        tre.add("ho");
        System.out.print('|'+tre.toString()+'|');
    }
}
