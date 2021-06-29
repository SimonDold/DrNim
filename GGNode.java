package DrNim;

public class GGNode { //node in a GraphGame
	GGNode[] successors;
	int sg;  //the SG-vale of this node.  -1 means not labeled yet
	int number; //1 based counting
	
	
	public GGNode(){
		successors=new GGNode[0];
	}
	
	public void calcSG(){ //complexity:  O(#this.successors^2)   
		int k=0;
		if(successors.length==0){
			//do nothing
		}else{
			boolean loop=true;
			while(loop){
				for(int i=0;i<successors.length;i++){
					if(k==successors[i].getSG()){
						k++;
						loop=true;
						break;
					}else{ 
					
						loop=false;;
					}
				}
			}//end while(loop)
		}
		sg=k;
	}
	
	public int getSG() {
		return sg;
	}

	public void addSuc(GGNode suc){
		if(isAlreadyIn(suc.getNumber())){
			//do nothing
		}else{
			GGNode[] z=new GGNode[successors.length+1];
			for(int i=0;i<successors.length;i++){
				z[i]=successors[i];
			}
			z[successors.length]=suc;
			successors=z;
		}
	}

	private int getNumber() {
		return number;
	}

	public boolean isAlreadyIn(int num) { //returns true iff this GGNode has a successor with number=='num'
		boolean b=false;
		if(successors.length==0){
			//do nothing
		}else{
			for(int i=0;i<successors.length;i++){
				if(successors[i].getNumber()==num){
					b=true;
					break;
				}
			}
		}
		return b;
	}

	public void setNumber(int i) {
		number=i;
	}

	public void setSG(int i) {
		sg=i;
	}
}
