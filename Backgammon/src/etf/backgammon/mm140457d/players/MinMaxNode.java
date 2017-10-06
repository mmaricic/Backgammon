package etf.backgammon.mm140457d.players;

public class MinMaxNode extends Node{
	
	private int dice1Value;
	private int dice2Value;
	private Node chosen;
	
	public MinMaxNode(Type type, int [] state, int d1, int d2, int barEnemy, int barMe, int outEnemy, int outMe){
		super(type, state, barEnemy, barMe, outEnemy, outMe);
		dice1Value = d1;
		dice2Value = d2;
	}
	
	
	@Override
	public int getDiceOne(){
		return dice1Value;
	}
	
	@Override
	public int getDiceTwo(){
		return dice2Value;
	}
	
	
	public boolean dicesEqual() {
		return dice1Value == dice2Value;
	}
	
	public void setChosen(Node c){
		chosen = c;
	}
	
	public Node getChosen(){
		return chosen;
	}

	
}
