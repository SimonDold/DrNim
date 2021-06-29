package DrNim;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
	static CombGame g; //the game that is the combination of all the chosen games
	static Scanner reader = new Scanner(System.in); 
	
	
	public static void main(String[] args) {
		boolean mainloop=true;
		while(mainloop){
			g=new CombGame("THE Game");
			int f=init2(); //'f' whos turn? 0 human    1 Dr. Nim
			g.updateTerminal(); //is the game over?
			g.updateSG(); //what is the current SG-value
			mainloop=play(f); //decides the winner (if the winner is not decided yet player 'f' does a move)
		}//end while(mainloop)
	}
	
	
	
	private static boolean play(int f) {
		boolean mainloop=true;
		boolean valid;
		boolean loop=true;
		if(g.terminal){ //this is only the case if g starts in a terminal position
			System.out.println("!Game over!");
			if(f==0){
				System.out.println("The human is the winner.");
			}else{
				System.out.println("Dr. Nim is the winner.");
			}
			System.out.println("Dr. Nim: This was a very short game. Do you want to play again?");
			System.out.println("0: No");
			System.out.println("1: Yes");	
			valid=true;
	        int re = 0;
	        do {
	            try {
	            	valid=true;
	                re = reader.nextInt(); 
	            } catch (InputMismatchException e) {
	                reader.next();
	                valid=false;
	                System.out.println("You have to input an integer.");
	            }
	        } while (valid == false);
			if(re==1){
				System.out.println("Let's go!");
				mainloop=true;
			}else{
				System.out.println("Dr. Nim: Bye!");
				mainloop=false;
			}
			loop=false;
		}
		while(loop){
			System.out.println(g.toSimpleString() + "SG-value: "+g.curSG);
			g.play(f,0);
			f--;
			f=f*f;
			if(g.terminal){
				System.out.println("Game over!");
				if(f==0){
					System.out.println("The human is the winner.");
					System.out.println("Dr. Nim: I have to go now... this game sucks anyway.");
					mainloop=false;
				}else{
					System.out.println("Dr. Nim is the winner.");
					System.out.println("Dr. Nim: This was fun. Do you want to play again?");
					System.out.println("0: No");
					System.out.println("1: Yes");	
					valid=true;
			        int re = 0;
			        do {
			            try {
			            	valid=true;
			                re = reader.nextInt(); 
			            } catch (InputMismatchException e) {
			                reader.next();
			                valid=false;
			                System.out.println("You have to input an integer.");
			            }
			        } while (valid == false);
					if(re==1){
						System.out.println("Let's go!");
						mainloop=true;
					}else{
						System.out.println("Dr. Nim: Bye!");
						mainloop=false;
					}
				}
				loop=false;
			}
		}
		return mainloop;
	}



	



	private static int init2() { //who moves first?
		System.out.println("Do you want to do the first move?");
		System.out.println("0: No");
		System.out.println("1: Yes");	
		boolean valid=true;
        int f = 0;
        do {
            try {
            	valid=true;
                f = reader.nextInt(); 
            } catch (InputMismatchException e) {
                reader.next();
                valid=false;
                System.out.println("You have to input an integer.");
            }
        } while (valid == false);
		if(f==0 || f==1){
		}else{//if the user can't decide Dr. Nim helps him ;)
			System.out.print("Dr. Nim: I count that as a "); 
			if(g.getSG()==0){
				System.out.println("Yes.");
				f=1;
			}else{
				System.out.println("No.");
				f=0;
			}
		}
		return f;
	}
}
