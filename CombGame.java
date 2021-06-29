package DrNim;

import java.util.InputMismatchException;

public class CombGame {
	String name; //e.g. Nim#2 
	int curSG; //current SG-value
	CombGame[] innerGames; //e.g. Nim#2 has the innerGames: Nim#2.1 , Nim#2.2 and Nim#2.3
	boolean terminal; //true if there is no legal move left
	
	
	public CombGame(String name){
		this.name=name;
		innerGames=new CombGame[0];
		initDialog(); 
		updateSG(); 
		updateTerminal();
	}
	
	public void initDialog(){ //users decides the properties of the game
		int a=amountGames();  //amount of sub-games
		for(int i=1; i<a+1;i++){
			boolean loop2=true;
			while(loop2){
				loop2=false;
				System.out.println("What do you want to play as game #"+i+"?");
				System.out.println("1: Nim");
				System.out.println("2: SubstractionGame");
				System.out.println("3: GraphGame");
				System.out.println("4: Combinations of Game");
				boolean valid=true;
		        int c = 0; //choice of game
		        do {
		            try {
		            	valid=true;
		                c = Main.reader.nextInt(); 
		            } catch (InputMismatchException e) {
		            	Main.reader.next();
		                valid=false;
		                System.out.println("You have to input an integer.");
		            }
		        } while (valid == false);
				switch (c) {
	            	case 1:  
	            		add(new NimGame("Nim"));
	                    break;
	            	case 2:  
	            		add(new SubstGame("Subst"));
	                    break;
	            	case 3:  
	            		add(new GraphGame("Graph"));
	                    break;   
	            	case 4:  
	            		add(new CombGame("CombGame"));
	                    break;
	            	default: 
	            		System.out.println("Dr. Nim: I do not know this game... Choose a game from the list.");
	                    loop2=true;
	            		break;
				}
			}//end while(loop2))
		}
		System.out.println(toSimpleString() +" SG-value:"+curSG);
	}
	
	private int amountGames() {
		int a=0; //the chosen amount of sub-games
		boolean loop=true;
		while(loop){
			loop=false;
			System.out.println("How many games do you want to play at once?");
			boolean valid=true;
	        do {
	            try {
	            	valid=true;
	                a = Main.reader.nextInt(); 
	            } catch (InputMismatchException e) {
	                Main.reader.next();
	                valid=false;
	                System.out.println("You have to input an integer.");
	            }
	        } while (valid == false);
			if(a<0){
				System.out.println("Dr. Nim: A neative amount of games is negative fun! I do not want that.");
				loop=true;
			}
		}//end while(loop)
		System.out.print("you chose to play "+a+" game");
		if(a==1){
			System.out.println("");
		}else{
			System.out.println("s");
		}
		return a;
	}

	public void add(CombGame h){
		CombGame[] z=new CombGame[innerGames.length+1];
		for(int i=0;i<innerGames.length;i++){
			z[i]=innerGames[i];
		}
		z[innerGames.length]=h;
		innerGames=z;
		updateSG();
		updateTerminal();
	}
	
	int getSG() {
		return curSG;
	}
	
	/*
	public String toString(){
		String s=name;
		for(int i=0;i<innerGames.length;i++){
			if(i==0){
				s+="[";
			}
			s=s+innerGames[i].toString();
			if(i==innerGames.length-1){
				s+="]";
			}else{
				s+=",";
			}
		}
		return s;
	}
	*/
	
	public String toSimpleString() {
		String s=name+"(";
		for(int i=0; i<innerGames.length;i++){
			s+=innerGames[i].toSimpleString();
			if(i==innerGames.length-1){
				s+=")";
			}else{
				s+=",";
			}
		}
		return s;
	}

	public void play(int f, int t) { //f:=who has to do a move ; t what SG-value does Dr.Nim want to create
		if(f==1){ //human turn
			System.out.println("It is your turn");
			System.out.println("Choose a sub-game");
			for(int i=0; i<innerGames.length;i++){
				System.out.println(""+(i+1)+": "+innerGames[i].toSimpleString()+" SG-value: "+innerGames[i].getSG() );
			}
			int h=0; //choice of a sub-game
			boolean loop=true;
			while(loop){ //asking again if the input is not allowed
				loop=false;
				boolean valid=true;
		        do {
		            try {
		            	valid=true;
		                h = Main.reader.nextInt(); 
		            } catch (InputMismatchException e) {
		                Main.reader.next();
		                valid=false;
		                System.out.println("You have to input an integer.");
		            }
		        } while (valid == false);
				h--;// back to zero based counting
				if(h>=innerGames.length || h<0){
					System.out.println("Dr. Nim: Hey this game does not exist! Choose a game from the list.");
					loop=true;
				}else{
					if(innerGames[h].getTerminal()){
						System.out.println("DR. Nim: Hey this game is already in a terminal position! You have to choose a different game.");
						loop=true;
					}
				}
			}
			innerGames[h].play(f,0);
		}else{ //f==0  turn of Dr. Nim
			if(curSG==0){//do whatever move...
				int c=-1;
				for(int i=0;i<innerGames.length;i++){
					if(innerGames[i].terminal==false){
						c=i;
						break;
					}
				}
				if(c==-1){ //This is not possible because c==-1 means all games are in a terminal position but if this were the case the game would have already stopped 
					System.out.println("error c==-1");
				}else{
					innerGames[c].play(f,0); //does not matter what t is since Dr.Nim can't move to a position with SG-value 0
				}
			}else{ //choose sub-game with a good SG-value and do a move to a position with SG-value t
				
				//a,b,c are SG-values and d is the SG-value of the combined game (d=curSG) and t is the target SG-value
				//a^b^c=d
				//a^b^c^d=0
				//a^b^c^d^t=t
				//d^t=:q
				
				//if (a^q<=a) good enough  
				
				int q=t^curSG;
				int arg=0;
				for(int i=0;i<innerGames.length;i++){
					if ((innerGames[i].getSG()^q)<=innerGames[i].getSG()){
						arg=i;
						break;
					}
				}
				t=curSG^innerGames[arg].getSG()^t; //target SG-value changes in the inner game. e.g. Nim(3,4,5) has target 0, Dr.Nim chose to modify the 1st pile and there is the target 1=2^3^0										
				innerGames[arg].play(f,t);		
			}
		}
		updateSG();
		updateTerminal();	
	}

	
	boolean getTerminal() {
		return terminal;
	}

	
	void updateSG() {
		curSG=0;
		for(int i=0; i<innerGames.length;i++){
			curSG=curSG^innerGames[i].getSG();	//calculate the Nim-sum of all sub-games
		}
	}
	
	
	protected void updateTerminal() {
		terminal=true;
		for(int i=0; i<innerGames.length;i++){
			if(innerGames[i].terminal==false){ //if at least one sub-game is not in a terminal position the combination is not in a terminal position
				terminal=false;
				break;
			}
		}
	}
}
