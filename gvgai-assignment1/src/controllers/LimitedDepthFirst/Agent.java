

package controllers.LimitedDepthFirst;

import java.util.ArrayList;
import java.util.Stack;

import controllers.LimitedDepthFirst.TreeNode;

import java.util.*;
import core.game.StateObservation;
import core.game.Observation;
import core.player.AbstractPlayer;
import ontology.Types;
import ontology.Types.ACTIONS;
import tools.ElapsedCpuTimer;
import java.awt.*;
import java.awt.Graphics2D;
import tools.Vector2d;

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
	protected Stack<ACTIONS> tempActions;
	protected Stack<Types.ACTIONS> finalActions;
	protected Types.ACTIONS resultAction;
	protected ArrayList<StateObservation> stateArray;
	protected TreeNode finalNode;
	protected TreeNode startNode;
	private int heuValue;
	private int depthlimit;
	private boolean win;


	public Agent(StateObservation stateObs, ElapsedCpuTimer elapsedTimer){
		tempActions = new Stack<>();
		stateArray = new ArrayList<>();
		finalActions=new Stack<>();
		grid = stateObs.getObservationGrid();
		block_size = stateObs.getBlockSize();
		win=false;
		TreeNode root=new TreeNode(stateObs,null);
		finalNode=root;
		startNode=root;
		depthlimit=5;
	}
	/**
	 * return action which is already saved.
	 * @param stateObs Observation of the current state.
	 * @param elapsedTimer Timer when the action returned is due.
	 * @return 	ACTION_NIL all the time
	 */
	@Override
	public ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
		if (win==true) {
			return finalActions.pop();
		}
		heuValue = 99999;
		tempActions.clear();
		stateArray.clear();
		TreeNode w=new TreeNode(stateObs,null);
		startNode=w;
		dfs(w, depthlimit);
		if (win==true) {
			return finalActions.pop();
		}
		return resultAction;
	}

	private int gridDist(Vector2d v0, Vector2d v1){
		return (int)(Math.abs(v0.x-v1.x)+Math.abs(v0.y-v1.y));
	}


	private int heuristic(StateObservation stateObs){
		if (stateObs.getGameWinner() == Types.WINNER.PLAYER_WINS){
			return -99999;
		}
		if (stateObs.getGameWinner() == Types.WINNER.PLAYER_LOSES){
			return 99999;
		}
		ArrayList<Observation>[] fixedPositions = stateObs.getImmovablePositions();
		ArrayList<Observation>[] movingPositions = stateObs.getMovablePositions();
		Vector2d goalpos = fixedPositions[1].get(0).position;
		Vector2d avatarpos = stateObs.getAvatarPosition();


		int dist=0;
		if (stateObs.getAvatarType()==1){//没拿到钥匙

			Vector2d keypos = movingPositions[0].get(0).position;
			for(int i=0;i<movingPositions[1].size();i++){
				if(movingPositions[1].get(i).position==keypos)
					return 99999;
			}
			dist = gridDist(avatarpos, keypos)+1000;
			return dist;
		}else if(stateObs.getAvatarType()==4){//拿到了钥匙
			dist = gridDist(avatarpos, goalpos);
			return dist;
		}
		return 99999;
	}

	private void dfs(TreeNode curNode,int depth) {
		if(win==true)return;
		if (depth == 0){
			if (heuristic(curNode.getState())<=heuValue) {
				heuValue = heuristic(curNode.getState());
				resultAction=tempActions.elementAt(0);//取当前最佳动作
			}
			return;
		}
		stateArray.add(curNode.getState());
		ArrayList<Types.ACTIONS> actions = curNode.getState().getAvailableActions();
		for (Types.ACTIONS actionTry: actions) {
			StateObservation stCopy = curNode.getState().copy();
			stCopy.advance(actionTry);
			if(stCopy.equalPosition(curNode.getState()))continue;
			tempActions.push(actionTry);
			if (stCopy.getGameWinner()==Types.WINNER.PLAYER_WINS){//若胜利，停止递归
				win = true;
				heuValue = -99999;
				TreeNode w=new TreeNode(stCopy,curNode);
				w.changeActions(actionTry);
				finalNode=w;
				finalActions.clear();
				while(finalNode!=startNode){
					finalActions.push(finalNode.getAction());
					finalNode=finalNode.getParent();
				}

				return;
			}
			boolean isLoop = false;
			for (StateObservation s: stateArray){//判断是否形成回路
				if (s.equalPosition(stCopy)){
					isLoop = true;
					break;
				}
			}
			if (!isLoop){
				TreeNode w=new TreeNode(stCopy,curNode);//若未形成回路，继续递归
				w.changeActions(actionTry);
				dfs(w, depth -1);
			}
			if(win!=true)
				tempActions.pop();
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


