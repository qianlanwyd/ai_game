

package controllers.Astar;

import java.util.ArrayList;
import java.util.Queue;
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
     * after a*,find and save the actions.
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
    protected Stack<Node> closedStack;
    protected Stack<ACTIONS> finalActions;
    protected ArrayList<Node> openArray;
    protected ArrayList<StateObservation> visitArray;
    protected Node finalNode;
    private boolean win;


    public Agent(StateObservation stateObs, ElapsedCpuTimer elapsedTimer){
        grid = stateObs.getObservationGrid();
        block_size = stateObs.getBlockSize();
        closedStack=new Stack<>();
        openArray=new ArrayList<>();
        visitArray=new ArrayList<>();
        finalActions=new Stack<>();
        win=false;
        Node startNode=new Node(stateObs,null, ACTIONS.ACTION_NIL);
        finalNode=startNode;
        AStar(startNode);
        while(finalNode!=startNode){//回溯
            finalActions.push(finalNode.getAction());
            finalNode=finalNode.getParent();
        }
    }
    /**
     * return action which is already saved.
     * @param stateObs Observation of the current state.
     * @param elapsedTimer Timer when the action returned is due.
     * @return 	ACTION_NIL all the time
     */
    @Override
    public ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
       // System.out.println(finalActions.peek());
        return finalActions.pop();
    }

    private Node getMin(){
        int min_i=0;
        for(int i=0;i<openArray.size();i++){
            if(openArray.get(i).getF()<openArray.get(min_i).getF()){
                min_i=i;
            }
        }
        return openArray.get(min_i);
    	//return openArrary.get(openArrary.size()-1);
    }
    
    private int alreadyExist(StateObservation e){
        for(int i=openArray.size()-1;i>=0;i--){
            if(openArray.get(i).getState().equalPosition(e))
                return i;
        }
        return -1;

    }

    public void AStar(Node startNode){
        closedStack.clear();
        openArray.clear();
        visitArray.clear();
        openArray.add(startNode);
        visitArray.add(startNode.getState());

        while(win==false && (!openArray.isEmpty())){
            Node curNode=getMin();
            visitArray.remove(curNode.getState());
            openArray.remove(getMin());
            closedStack.push(curNode);
            //System.out.print(curNode.getAction());
            if(curNode.getState().getGameWinner()==Types.WINNER.PLAYER_WINS){
                win=true;
                finalNode=curNode;
                break;
            }
            ArrayList<Types.ACTIONS> actions = curNode.getState().getAvailableActions();

            for (Types.ACTIONS actionTry : actions){
                StateObservation stCopy = curNode.getState().copy();
                stCopy.advance(actionTry);
                Node temp=new Node(stCopy,curNode,actionTry);
                if(temp.getState().getGameWinner()==Types.WINNER.PLAYER_WINS){
                    win=true;
                    finalNode=temp;
                    break;
                }
                int flag=alreadyExist(stCopy);
                if(flag!=-1){
                    if(openArray.get(flag).getCost()>temp.getCost()){//更新cost值
                        openArray.remove(flag);
                        openArray.add(flag,temp);
                    }
                }
                else{
                    openArray.add(temp);
                    visitArray.add(temp.getState());
                }

            }
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


