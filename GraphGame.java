package DrNim;

import java.util.InputMismatchException;

public class GraphGame extends CombGame{
	
	
	/* example GraphGame: 
	 * 
	  	1	2	3	4	5	6	7	8	9	10	11	12	13	14	15	16	
	1:		1	0	0	1	0	0	0	0	0	0	0	0	0	0	0	:1
	2:			1	0	0	1	0	0	0	0	0	0	0	0	0	0	:2
	3:				1	0	0	1	1	0	0	0	1	0	0	0	0	:3
	4:					0	0	0	0	0	0	0	0	0	0	0	0	:4
	5:						1	0	0	0	0	1	0	0	0	0	0	:5
	6:							1	0	0	1	0	0	0	0	0	0	:6
	7:								0	0	0	0	1	0	1	1	0	:7
	8:									1	0	0	1	0	0	0	0	:8
	9:										0	0	0	0	0	0	0	:9
	10:											1	0	0	1	0	0	:10
	11:												0	0	1	0	0	:11
	12:													1	0	1	0	:12
	13:														0	0	0	:13
	14:															1	0	:14
	15:																1	:15
	16:																	:16
	 * 
	 * 
	 * 
	 */
	
	
	GGNode[] nodes;
	GGNode curNode;
	String map;
	
	public GraphGame(String name){
		super(name);
	}
	
	
	public void initDialog(){
		boolean loop=true;
		int nodeCount=0;
		while(loop){
			loop=false;
			System.out.println("How many nodes do you want?");
			boolean valid=true;
	        do {
	            try {
	            	valid=true;
	                nodeCount = Main.reader.nextInt(); 
	            } catch (InputMismatchException e) {
	            	Main.reader.next();
	                valid=false;
	                System.out.println("You have to input an integer.");
	            }
	        } while (valid == false);
			if(nodeCount<1){
				System.out.println("Dr. Nim: We need at least one node!");
				loop=true;
			}
		}//end while(loop)
		//-------------------------------------------------------------------
		nodes=new GGNode[nodeCount];
		for(int i=nodes.length-1;i>-1; i--){
			nodes[i]=new GGNode();
			nodes[i].setNumber(i+1);
			nodes[i].setSG(-1);
		}
		curNode=nodes[0];
		for(int i=1; i<nodeCount+1;i++){ //i counts one based
			boolean loop2=true;
			int sucCount=0; //successors (counts 1 based)
			while(loop2){
				loop2=false;
				System.out.println("How many succesors of node #"+i+"?"); 
				System.out.println("remember only nodes with a greater number can be successors."); //this rule avoids cycles
				boolean valid=true;
		        do {
		            try {
		            	valid=true;
		                sucCount = Main.reader.nextInt(); 
		            } catch (InputMismatchException e) {
		                Main.reader.next();
		                valid=false;
		                System.out.println("You have to input an integer.");
		            }
		        } while (valid == false);
				if(sucCount<0){
					System.out.println("Dr. Nim: A node can not have less than zero succesors!");
					loop2=true;
				}
				if(sucCount>nodeCount-i){  //there are not enough different nodes you could put as successor
					System.out.println("Dr. Nim: There are not enough different nodes you could put as successor!");
					loop2=true;
				}
			}
			//loop3 for the successors
			for(int j=1; j<sucCount+1;j++){
					boolean loop3=true;
					int sucChoise=0; //successor choice
					while(loop3){
						loop3=false;
						System.out.println("What succesors of node #"+i+"?");
						System.out.println("remember only nodes with a greater number can be successors."); //this rule avoids cycles
						boolean valid3=true;
				        do {
				            try {
				            	valid3=true;
				                sucChoise = Main.reader.nextInt(); 
				            } catch (InputMismatchException e) {
				                Main.reader.next();
				                valid3=false;
				                System.out.println("You have to input an integer.");
				            }
				        } while (valid3 == false);
						if(sucChoise<=i){
							System.out.println("Dr. Nim: Remember only nodes with a greater number can be successors.");
							loop3=true;
						}
						if(sucChoise>nodeCount){
							System.out.println("Dr. Nim: This node does not exist.");
							loop3=true;
						}
						if(nodes[i-1].isAlreadyIn(sucChoise)){
							System.out.println("Dr. Nim: This node is already a successor of node#"+i+".");
							loop3=true;
						}
					}
					nodes[i-1].addSuc(nodes[sucChoise-1]);   //sucChoise-1 because we have to go back to zero based counting
				}
		 }
		update();
		calcMap();
	}
	
	public void calcSG(){  //complexity: O(#nodes*O(GGNode.calcSG))  = O(#nodes*(#avg_node.successors^2)) = O(#successors^2)
		for(int i=nodes.length-1;i>-1; i--){
			nodes[i].calcSG();
		}
		curSG=curNode.getSG();
	}

	public void update() {
		calcSG();
	}
	
	
	
	protected void updateTerminal() {
		if(curNode==null){
			//do nothing
		}else{
			if(curNode.successors.length==0){
				terminal=true;
			}else{
				terminal=false;
			}
		}
	}
	
	
	
	public void play(int f, int t) { //f:=who has to do a move ; t what SG-value does Dr.Nim want to create
		if(f==1){ //human turn
			System.out.println(map);
			System.out.println("It is your turn");
			System.out.println("Choose one of the successor");
			for(int i=0;i<curNode.successors.length;i++){
				System.out.println(""+(i+1)+": node#"+curNode.successors[i].number);	
			}
			int sucChoice=0;
			boolean loop=true;
			while(loop){ //asking again if the input is not allowed
				loop=false;
				boolean valid=true;
		        do {
		            try {
		            	valid=true;
		                sucChoice = Main.reader.nextInt(); 
		            } catch (InputMismatchException e) {
		                Main.reader.next();
		                valid=false;
		                System.out.println("You have to input an integer.");
		            }
		        } while (valid == false);
				if(sucChoice<1 || sucChoice>curNode.successors.length){
					System.out.println("Dr.Nim: Hey you are cheating! Choose a node from the list");
					loop=true;
				}
			}
			curNode=curNode.successors[sucChoice-1];
			updateSG();
			updateTerminal();
		}else{//f=0 Dr.Nim turn
			boolean found=false; //found a successor with the SG-value t
			int nextNode=0;
			for(int i=0;i<curNode.successors.length;i++){
				if(curNode.successors[i].getSG()==t){
					found=true;
					nextNode=i;
					break;
				}
			}
			if(found){
				System.out.println("Dr. Nim moves from node#"+curNode.number+" to node#"+curNode.successors[nextNode].number+".");
				curNode=curNode.successors[nextNode];
			}else{ //move to whatever node (Dr. Nim has to move from a position with SG-value 0)
				System.out.println("Dr. Nim moves from node#"+curNode.number+" to node#"+curNode.successors[0].number+".");
				curNode=curNode.successors[0];	
			}
			updateTerminal();
			updateSG();
		}
	}
	
	public void calcMap() { //calculates a String to represent the Graph (see example below)
		/*
		 * 	 1 2 3 4						
		 * 1 x 1 0 1	node 1 has as successors node 2 and node 4 (not node 3)
		 * 2 x x 1 0	node 2 has as successors node 3 (not node 4)					
		 * 3 x x x 1	node 3 has as successor node 4
		 * 4 x x x x
		 */
		
		String s="\t";
		for(int i=0;i<nodes.length;i++){
			s+=""+(i+1)+"\t";
		}
		s+="\n";
		for(int i=0;i<nodes.length;i++){
			s+=""+(i+1)+":\t";
			for(int j=0;j<nodes.length;j++){
				if(j<=i){
					s+="\t";
				}else{
					if(nodes[i].isAlreadyIn(j+1)){
						s+="1\t";
					}else{
						s+="0\t";
					}
				}
			}
			s+=":"+(i+1)+"\n";
		}
		map=s;
	}

	public void updateSG(){
		if(curNode==null){
			curSG=0;
		}else{
			curSG=curNode.getSG();
		}
	}
	
	public String toSimpleString(){
		String s=name;
		s+="("+curNode.number+"/"+nodes.length+")";
		return s;
	}
}
