package etf.backgammon.mm140457d.GUI;

public class Branch {
	int diceOne;
	int diceTwo;
	
	public Branch(int one, int two){
		diceOne = one;
		diceTwo = two;
	}
	
	public String toString(){
		if(diceOne == 0 && diceTwo == 0)
			return "";
		return diceOne+", "+diceTwo;
	}
}
