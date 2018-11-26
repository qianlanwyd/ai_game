
package controllers.DepthFirst;

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
	
	public TreeNode(StateObservation state, TreeNode parent) {
		this.parent = parent;
		this.state = state;
		if(parent != null){
			this.depth = parent.depth + 1;
		}
		else{
			this.depth = 0;
		}
		visited=false;
		foreAction=Types.ACTIONS.ACTION_NIL;
		
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
}
