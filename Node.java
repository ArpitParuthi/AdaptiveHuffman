package Assignment_5;
   public class Node{
      String character;   //character stored in this node
      int weight;       //frequency of this character
      int place;        //node number in reverse-level order
      Node parent;      //reference to parent of this node
      Node left;
      Node right;
      char label;       //edge-label associated with this character (leftchild = 0, rightchild = 1)
      boolean leaf;     //Node is a leaf
     
      public Node(int w, String c){
         weight = w;
         character = c;
         leaf = true;
      }


      public Node(int w){
         weight = w;
      }


      public String toString(){
         return "" + character + " " + weight + " " + place + "\n" + parent;
      }
   }