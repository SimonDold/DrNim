package DrNim;

import java.util.InputMismatchException;

public class SubstGame extends CombGame{
	int chipCount; //current amount of chips
	int[] possibleMoves; //the possible amounts of chips to take from the pile in each turn
	int[] sgValues; //array with the SG-value for each possible amount of chips
	
	public SubstGame(String name){
		super(name);
	}
	
	public void initDialog(){
		boolean loop=true;
		int chipCount=0;
		while(loop){
			loop=false;
			System.out.println("How many chips do you want?");
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
				System.out.println("Dr. Nim: I don't know how to play with a negative amount chips.");
				loop=true;
			}
		}
		int amountMoves=0;
		boolean loop2=true;
		while(loop2){
			loop2=false;
			System.out.println("How many possible moves do you want?");
			boolean valid=true;
	        do {
	            try {
	            	valid=true;
	                amountMoves = Main.reader.nextInt(); 
	            } catch (InputMismatchException e) {
	            	Main.reader.next();
	                valid=false;
	                System.out.println("You have to input an integer.");
	            }
	        } while (valid == false);
			if(amountMoves<0){
				System.out.println("Dr. Nim: You can't have a negative amount of moves.");
				loop2=true;
			}
		}
		int[] possibleMoves=new int[amountMoves];
		for(int i=0;i<possibleMoves.length;i++){
			int moveChoice=0;
			boolean loop3=true;
			while(loop3){
				loop3=false;
				System.out.println("How many chips do you want to take with move#"+(i+1)+"?");
				boolean valid=true;
		        do {
		            try {
		            	valid=true;
		                moveChoice = Main.reader.nextInt(); 
		            } catch (InputMismatchException e) {
		            	Main.reader.next();
		                valid=false;
		                System.out.println("You have to input an integer.");
		            }
		        } while (valid == false);
				if(moveChoice<=0){
					System.out.println("Dr. Nim: In a move you have to take at least one chip.");
					loop3=true;
				}
			}
			possibleMoves[i]=moveChoice;
		}
		this.chipCount=chipCount;
		this.possibleMoves=possibleMoves;
		sgValues=new int[chipCount+1];
		calcAllSG();
		updateSG();
		updateTerminal();
	}
	
	
	
	private void calcAllSG() { //fills the array 'sgValues'      complexity: O((#initialChips+1)*#possibleMoves)
		for(int j=0;j<sgValues.length;j++){
			sgValues[j]=-1; //label each as unlabeled
		}
		for(int j=0;j<sgValues.length;j++){ //j iterates thru each possible position
			int sg=0;
			int[] successorSG=new int[possibleMoves.length];
			for(int i=0;i<possibleMoves.length;i++){ // i iterates thru each possible successor position 
				if(j-possibleMoves[i]>=0 && j-possibleMoves[i]<sgValues.length){
					successorSG[i]=sgValues[j-possibleMoves[i]]; 
				}else{
					successorSG[i]=-1; //this is only a 'theoretical successor' because it is practically not reachable
				}
			}// end iterating thru each possible successor position
			boolean again=true;
			while(again){ //increases 'sg' until it is different to all successor-SG-values
				again=false;
				for(int i=0;i<successorSG.length;i++){
					if(sg==successorSG[i]){
						again=true;
						sg++;
						break;
					}
				}
			}
			sgValues[j]=sg;
		}// end iterating thru each possible position
	}


	public void update(){
		curSG=sgValues[chipCount];
	}

	
	
	protected void updateTerminal() { //complexity: O(#possibleMoves)
		terminal=true;
			for(int i=0;i<possibleMoves.length;i++){
				if(possibleMoves[i]<=chipCount){//if there is at least one move that takes less or equal chips then 'chipCount' the game is not in a terminal position
					terminal=false;
					break;
				}
			}
	}
	
	public void updateSG(){
			curSG=sgValues[chipCount];
	}
	
	public String toSimpleString(){
		String s=name;
		s+="("+chipCount+"[";
		for(int i=0;i<possibleMoves.length;i++){
			s+=possibleMoves[i];
			if(i==possibleMoves.length-1){
				s+="]";
			}else{
				s+=", ";
			}
		}
		s+=")";
		return s;
	}
	
	
	public void play(int f, int t) { //f:=who has to do a move ; t what SG-value does Dr.Nim want to create
		if(f==1){ //human turn
			System.out.println("It is your turn");
			System.out.println("How many chips do you take?");
			for(int i=0;i<possibleMoves.length;i++){
				System.out.println(""+(i+1)+": "+possibleMoves[i]+" chips.");
			}
			int takeChoice=0;
			boolean loop=true;
			while(loop){ //asking again if the input is not allowed
				loop=false;
				boolean valid=true; 
		        do {
		            try {
		            	valid=true;
		                takeChoice = Main.reader.nextInt(); 
		            } catch (InputMismatchException e) {
		                Main.reader.next();
		                valid=false;
		                System.out.println("You have to input an integer.");
		            }
		        } while (valid == false);
		        if(takeChoice<1 || takeChoice>possibleMoves.length){
					System.out.println("Dr.Nim: Hey you are cheating! This is not an option.");
					loop=true;
				}else{
					 if(chipCount-possibleMoves[takeChoice-1]<0){
						System.out.println("Dr.Nim: Hey you are cheating! You are not allowed to take more chips than there are.");
						loop=true;
					 }
				}
			}
			chipCount-=possibleMoves[takeChoice-1];
			updateSG();
			updateTerminal();
		}else{ //Dr.Nim turn
			boolean found=false; //found a successor with the SG-value t
			int next=0;
			for(int i=0;i<possibleMoves.length;i++){
				if(chipCount-possibleMoves[i]>=0 && chipCount-possibleMoves[i]<=sgValues.length-1){
					if(sgValues[chipCount-possibleMoves[i]]==t){
						found=true;
						next=i;
						break;
					}
				}
			}
			if(found){
				System.out.println("Dr. Nim takes "+possibleMoves[next]+" chips.");
				chipCount-=possibleMoves[next];
			}else{ //do whatever move (Dr. Nim has to move from a position with SG-value 0)
				for (int i=0;i<possibleMoves.length;i++){ 
					if(chipCount-possibleMoves[i]>=0 && chipCount-possibleMoves[i]<=sgValues.length-1){
						System.out.println("Dr. Nim takes "+possibleMoves[0]+" chips.");
						chipCount-=possibleMoves[i];
						break;
					}
				}
			}
			updateTerminal();
			updateSG();
		}
	}
}
