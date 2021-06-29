package DrNim;

import java.util.InputMismatchException;

public class NimGame extends CombGame{

	public NimGame(String name) {
		super(name);
	}
	
	public void initDialog(){
		boolean loop=true;
		int pileCount=0; //the amount of piles
		while(loop){
			loop=false;
			System.out.println("How many piles do you want?");
			boolean valid=true;
	        do {
	            try {
	            	valid=true;
	                pileCount = Main.reader.nextInt(); 
	            } catch (InputMismatchException e) {
	                Main.reader.next();
	                valid=false;
	                System.out.println("You have to input an integer.");
	            }
	        } while (valid == false);
			if(pileCount<0){
				System.out.println("Dr. Nim: I don't know how to play with a negative amount piles");
				loop=true;
			}
		}// end while(loop)
		for(int i=1; i<pileCount+1;i++){
			add(new SinglePileNim(name));
		}
	}
}
