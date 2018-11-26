package controllers.DepthFirst;

import java.util.ArrayList;
import java.util.Stack;
import java.util.*;  
import core.game.StateObservation;
import core.game.Observation;
import core.player.AbstractPlayer;
import ontology.Types;
import ontology.Types.ACTIONS;
import tools.ElapsedCpuTimer;
import java.awt.*;
import java.awt.Graphics2D;

public class Agent extends AbstractPlayer{


	/**
	 * after dfs,find and save the actions.
	 * @param stateObs Observation of the current state.
     * @param elapsedTimer Timer when the action returned is due.
	 */
	
    /**
     * Observation grid.
     */
    protected ArrayList<Observation> grid[][];

    /**
     * block size
     */
    protected int block_size;
	protected Stack<TreeNode> dfsStack;
    protected Stack<Types.ACTIONS> finalActions;
    protected ArrayList<StateObservation> stateArrary;
    private boolean win;
  
	public Agent(StateObservation stateObs, ElapsedCpuTimer elapsedTimer){
        finalActions = new Stack<>();
        dfsStack = new Stack<>();
        stateArrary = new ArrayList<>();
        grid = stateObs.getObservationGrid();
        block_size = stateObs.getBlockSize();
        win=false;
        TreeNode root=new TreeNode(stateObs,null);
        dfs(root);
		
	}	
	/**
	 * return action which is already saved. 
	 * @param stateObs Observation of the current state.
     * @param elapsedTimer Timer when the action returned is due.
	 * @return 	ACTION_NIL all the time
	 */
	@Override
	public ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {

		return finalActions.pop();
	}
	
	private void dfs(TreeNode curNode) {
		dfsStack.push(curNode);
		TreeNode f=curNode;
		while(!dfsStack.empty()) {
			dfsStack.peek().visit();
			stateArrary.add(dfsStack.peek().getState());
			TreeNode temp=dfsStack.peek();
			dfsStack.pop();//取出栈顶并储存信息
			if(temp.getState().getGameWinner()== Types.WINNER.PLAYER_WINS) {
				f=temp;
				win=true;
				break;
			}
			 int unVisited=-1;
			 ArrayList<Types.ACTIONS> actions = temp.getState().getAvailableActions();
			 for (int i = 0; i < actions.size(); i++) {//遍历相邻节点并入栈
				 unVisited=-1;
				 StateObservation stCopy = temp.getState().copy();
		         stCopy.advance(actions.get(i));
		         if(stCopy.equalPosition(curNode.getState()))continue;
		         boolean exitLoop=false;
				 for(int j = 0; j < stateArrary.size(); j++) {
					 if(stateArrary.get(j).equalPosition(stCopy)==true) {//判断是否形成回路
						 exitLoop=true;
						 break;
					 }
				 }
				 if(exitLoop==false) {
					unVisited=i;
				 }
				 if(unVisited!=-1) {//若未形成回路并可以移动，将该节点入栈
					 StateObservation stCopy2 = temp.getState().copy();
			         stCopy2.advance(actions.get(unVisited));
					 TreeNode w=new TreeNode(stCopy2,temp);
					 w.changeActions(actions.get(unVisited));
					 w.visit();
					 stateArrary.add(w.getState());
					 dfsStack.push(w);
					 if(w.getState().getGameWinner() == Types.WINNER.PLAYER_WINS) {
						 System.out.println("win");
						 f=w;
						 win=true;
						 break;
					 }
				 }
			 }
			 if(win==true)break;
		}
		while(f!=curNode) {//回溯胜利节点，并将其动作压栈，方便act函数调用
			finalActions.push(f.getAction());
			f=f.getParent();
		}
	}

    /**
     * Gets the player the control to draw something on the screen.
     * It can be used for debug purposes.
     * @param g Graphics device to draw to.
     */
    public void draw(Graphics2D g)
    {
        int half_block = (int) (block_size*0.5);
        for(int j = 0; j < grid[0].length; ++j)
        {
            for(int i = 0; i < grid.length; ++i)
            {
                if(grid[i][j].size() > 0)
                {
                    Observation firstObs = grid[i][j].get(0); //grid[i][j].size()-1
                    //Three interesting options:
                    int print = firstObs.category; //firstObs.itype; //firstObs.obsID;
                    g.drawString(print + "", i*block_size+half_block,j*block_size+half_block);
                }
            }
        }
    }
}


