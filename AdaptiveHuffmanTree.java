package Assignment_5;
import java.util.ArrayList;

public class AdaptiveHuffmanTree {

        private Node NYT;
        private Node root;
        private char[] codeStr;
        private ArrayList<Character> alreadyExist;
        ArrayList<Node> nodeList;
        private String tempCode = "";
        static StringBuffer res = new StringBuffer();
        public AdaptiveHuffmanTree(char[] codeStr){
                this.codeStr = codeStr;
                alreadyExist = new ArrayList<Character>();
                nodeList = new ArrayList<Node>();
                
                NYT = new Node(0,"NEW");
                NYT.parent = null;
                root = NYT;
                nodeList.add(NYT);
        }
        
        private void update(char c){
               
                Node toBeAdd = null;
                if ( !characterInTree(c) ){
                        Node innerNode = new Node(1);
                        Node newNode = new Node(1,String.valueOf(c)); //stores symbol
                        
                        innerNode.left = NYT;
                        innerNode.right = newNode;
                        innerNode.parent = NYT.parent;
                        if ( NYT.parent!=null ) 
                                NYT.parent.left = innerNode;
                        else {
                                root = innerNode;
                        }
                        NYT.parent = innerNode;
                        newNode.parent = innerNode;

                        nodeList.add(1, innerNode);
                        nodeList.add(1, newNode);
                        alreadyExist.add(c);
                        toBeAdd = innerNode.parent;
                } else {
                        toBeAdd = findNode(c);
                }
                
                //Loop until all parent nodes are incremented.
                while ( toBeAdd!=null ) {
                        Node bigNode = findBigNode(toBeAdd.weight);
                        if ( toBeAdd!=bigNode && toBeAdd.parent!=bigNode && bigNode.parent!=toBeAdd)
                                swap(toBeAdd, bigNode );
                        toBeAdd.weight++;
                        toBeAdd = toBeAdd.parent;
                }
        }


        private void swap(Node u, Node v) {
                //u<v; Swap the position in the list
                int i = nodeList.indexOf(u);
                int j = nodeList.indexOf(v);
                nodeList.remove(u);
                nodeList.remove(v);
                nodeList.add(i,v);
                nodeList.add(j,u);
                
                //Swap the position in the tree
                Node p = u.parent;
                Node q = v.parent;
                //If the two nodes have different parent node.
                if ( p!=q ) {
                        if ( p.left==u ) {
                                p.left = v;
                        } else {
                                p.right = v;
                        }

                        if ( q.left==v ) {
                                q.left = u;
                        } else {
                                q.right = u;
                        }
                } else {
                        p.left = v;
                        q.right = u;
                }
                u.parent = q;
                v.parent = p;
        
        }

        private boolean characterInTree(char c) {
                for ( int i=0; i<alreadyExist.size(); i++ ) {
                        if ( c==alreadyExist.get(i) )
                                return true;
                }
                return false;
        }
        
        private void getCodeWordForNYT(Node node, String character, String code) {

            if ( node.left==null && node.right==null ) {
                    if ( node.character!=null && node.character.equals(character) )
                            tempCode = code;
            } else {
                    if ( node.left!=null ) {
                    	getCodeWordForNYT(node.left, character, code + "0");
                    }
                    if ( node.right!=null ) {
                    	getCodeWordForNYT(node.right, character, code + "1");
                    }
            }
        }
        
        private String getCodeWordFor(char c){
                tempCode = "";

                getCodeWordForNYT(root, String.valueOf(c), "");
                String result = tempCode;
                if (result=="") {
                	getCodeWordForNYT(root, "NEW", "");
                        result = tempCode;
                        result += toBinary( (int)(c) );
                }
                res.append(result);
                return result; 
        }
        
        public static String toBinary(int decimal){
                String result = "";
                for ( int i=0; i<8; i++ ) {
                        if ( decimal%2==0 )
                                result = "0" + result;
                        else 
                                result = "1" + result;
                        decimal /= 2;
                }
                return result;
        }
        
        //Find the existing node in the tree
        private Node findNode(char c) {
                String temp = String.valueOf(c);
                Node tempNode = null;
                for ( int i=0; i<nodeList.size(); i++ ) {
                        tempNode = nodeList.get(i);
                        if ( tempNode.character!=null && tempNode.character.equals(temp) )
                                return tempNode;
                }
                return null;
        }
        
        //Finds highest Node in block
        private Node findBigNode(int weight) {
                Node temp = null;
                for ( int i=nodeList.size()-1; i>=0; i--) {
                        temp = nodeList.get(i);
                        if ( temp.weight==weight )
                                break;
                }
                return temp;
        }
             
        public ArrayList<String> encode(){
                ArrayList<String> result = new ArrayList<String>();
                char temp = 0;
                for ( int i=0; i<codeStr.length; i++ ) {
                        temp = codeStr[i];
                        result.add(getCodeWordFor(temp));
                        update(temp);
                }
                return result;
        }
        
        public double calCompRate(String text, ArrayList<String> code){
            double compRate = 0;
            double textbit = 8*text.length();
            double codebit= 0;
            for ( String s: code) {
                    codebit += s.length();
            }
            StdOut.println("Size: "+text.length());
            compRate = 1- codebit/textbit;
            StdOut.println("Bits read: " + (int)textbit + " bits.");
            StdOut.println("Bits Transmitted: " + (int)codebit + " bits.");
            StdOut.println("Compression ratio: " + compRate);
            return compRate;
        }
        
        private void getStatistics() {
        	ArrayList<Symbol> symbolList = new ArrayList<Symbol>();
        	preOrder(root, symbolList);
        	calRange(symbolList);
        	StdOut.println(symbolList);
        }
    
        public String decode(){
        	String result = "";
        	String symbol = null;
        	char temp = 0;
        	Node p = root;
        
        //The first symbol is of course NEW, so find it by ASCII
        	symbol = getByAsc(0);
        	result += symbol;
        	update( symbol.charAt(0) );
        	p = root;
        
        	for ( int i=9; i<codeStr.length; i++ ) {
                temp = codeStr[i];
                
                if ( temp=='0' ){
                        p = p.left;
                }
                else 
                        p = p.right;
                
                symbol = visit(p);
                //If reach a leaf
                if ( symbol!=null ){
                        if ( symbol=="NEW" ){
                                symbol = getByAsc(i);
                                i+=8;
                        }
                        result+=symbol;
                        update( symbol.charAt(0) );
                        p = root;
                }
        }
        
        return result;
    }

        private String getByAsc(int index) {

    	int asc = 0;
    	int tempInt = 0;
    	for ( int i=7; i>=0; i-- ) {
            tempInt = codeStr[++index] - 48;
            asc += tempInt * Math.pow(2, i);
    	}
    
    	char ret = (char) asc;
    	return String.valueOf(ret);
    	}
  
    	private String visit(Node p) {
        if ( p.character!=null ){
                //The symbol has been found.
                return p.character;
        } 
        return null;
    	}
    
    	public static void preOrder(Node node, ArrayList<Symbol> symbolList){
                if( node!=null ){
                        if ( node.character!=null && (!node.character.equals("NEW")) ) {
                                Symbol tempSymbol = new Symbol(node.character,node.weight);
                                symbolList.add(tempSymbol);
                        }
                        preOrder(node.left, symbolList);
                        preOrder(node.right, symbolList);
                }
        }
       
        private void calRange(ArrayList<Symbol> symbolList) {
                int total = codeStr.length;
                double low = 0;
                
                for ( Symbol tempSymbol: symbolList ){
                        tempSymbol.probability = tempSymbol.weight/ (double)total;
                        tempSymbol.low = low;
                        tempSymbol.high = low + tempSymbol.probability;
                        low += tempSymbol.probability;
                }
        }

        @SuppressWarnings("deprecation")
		public static void main(String[] args) {
             if(args[0].equals("+")) {
            	 
             String[] text = In.readStrings(args[1]);
             AdaptiveHuffmanTree aht = new AdaptiveHuffmanTree(text[0].toCharArray());
             aht.encode();
             StdOut.println("Encoded message: "+res);
             ArrayList<String> code = new ArrayList<String>();
             code.add(res.toString());
             aht.getStatistics();
             aht.calCompRate(text[0], code);
             
             Out out;
             out = new Out("D:/Java Files/Assignment_5/encoded1.txt");
             out.println(res);
             }
             
             //if(args[0].equals("-")){
            // String[] text = In.readStrings(args[1]);
             //AdaptiveHuffmanTree aht = new AdaptiveHuffmanTree(text[0].toCharArray());
             //String result=aht.decode();
            // StdOut.println(result);       
            // }

        }
}