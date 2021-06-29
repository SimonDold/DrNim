package DrNim;

import java.util.InputMismatchException;

public class SinglePileNim extends CombGame{
	int curSG;
	int chips; //amount of chips
	
	
	
	public void play(int f,int t) { //f:=who has to do a move ; t what SG-value does Dr.Nim want to create
		if(f==1){//human turn
			System.out.println("It is your turn");
			System.out.println("Choose an amount of chips to take");
			int take=0;
			boolean loop=true;
			while(loop){ //asking again if the input is not allowed
				loop=false;
				boolean valid=true;
		        do {
		            try {
		            	valid=true;
		                take = Main.reader.nextInt(); 
		            } catch (InputMismatchException e) {
		                Main.reader.next();
		                valid=false;
		                System.out.println("You have to input an integer.");
		            }
		        } while (valid == false);
		        if(take<1){
					System.out.println("Dr.Nim: Hey you are cheating! You have to take at least one chip");
					loop=true;
				}
				if(take>chips){
					System.out.println("Dr.Nim: Hey you are cheating! You can't take more chips than there are in the pile");
					loop=true;
				}
			}
			chips-=take;
			updateSG();
			updateTerminal();
		}else{ //turn==0 Dr.Nim move		
			System.out.println("It is the turn of Dr. Nim");
			System.out.print("Dr. Nim chooses to take "+(curSG-t)+" chip");
			if(curSG-t==1){
				System.out.println(".");
			}else{
				System.out.println("s.");
			}
			int take=curSG-t;
			chips-=take;
			updateSG();
			updateTerminal();		
			
		}
	}
	
	
	protected void updateTerminal() {
		if(chips==0){
			terminal=true;
			
		}else{
			terminal=false;
		}
	}
	
	boolean getTerminal() {
		return terminal;
	}
	
	void updateSG() {
		curSG=chips;
	}




	public SinglePileNim(String name){
		super(name);
	}
	
	
	public void initDialog(){
		boolean loop2=true;
		int chipCount=0;
		while(loop2){
			loop2=false;
			System.out.println("What should be the size of this pile?");
			boolean valid=true;
	        do {
	            try {
	            	valid=true;
	                chipCount = Main.reader.nextInt(); 
	            } catch (InputMismatchException e) {
	                Main.reader.next();
	                valid=false;
	                System.out.println("You have to input an integer.");
	            }
	        } while (valid == false);
			if(chipCount<0){
				System.out.println("Dr. Nim: I don't know how to play with negative chips");
				loop2=true;
			}
		}
		chips=chipCount;
		
	}
	
	public String toSimpleString(){
		String s=""+chips+"";
		return s;
	}
	
	int getSG() {	
		return curSG;
	}
	
}
