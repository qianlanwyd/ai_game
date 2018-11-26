package controllers.LimitedDepthFirst;


import core.game.StateObservation;
import java.util.ArrayList;
import ontology.Types;
import ontology.Types.ACTIONS;

public class TreeNode {

	private TreeNode parent;
	private StateObservation state;
	private int depth;
	private boolean visited;
	private ACTIONS foreAction;
	private int heu;
	
	public TreeNode(StateObservation s, TreeNode p) {
		this.parent = p;
		this.state = s;
		if(p != null){
			this.depth = p.depth + 1;
		}
		else{
			this.depth = 0;
		}
		visited=false;
		foreAction=Types.ACTIONS.ACTION_NIL;
		heu=999999;
		
	}
	public TreeNode getParent() {
		return parent;
	}
	public StateObservation getState() {
		return state;
	}
	public boolean ifVisited() {
		return visited;
	}
	public void visit() {
		visited=true;
	}
	public void changeActions(ACTIONS a) {
		foreAction=a;
	}
	public ACTIONS getAction() {
		return foreAction;
	}
	public void changeheu(int a) {
		heu=a;
	}
	public int getheu() {
		return heu;
	}
	public int getDepth() {
		return depth;
	}
	public void changeDepth(int a) {
		depth=a;
	}
}


