package Assignment_5;
public class Symbol {
        
        String character;//Inner node's character is null.
        int weight;
        double low;
        double high;
        double probability;
        
        Symbol(String character, int weight){
                this.character = character;
                this.weight = weight;
        }

        Symbol(String character, double p, double low, double high){
                this.character = character;
                this.probability = p;
                this.low = low;
                this.high = high;
        }
        
      
        
        public String toString(){
                return character + " " + String.valueOf(weight);
        }
}