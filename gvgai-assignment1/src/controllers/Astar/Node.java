
package controllers.Astar;

import core.game.StateObservation;
import core.game.Observation;
import ontology.Types;
import ontology.Types.ACTIONS;
import java.util.ArrayList;
import tools.Vector2d;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Node implements Comparable{

    private Node parent;
    private StateObservation state;
    private ACTIONS foreAction;
    private int f;
    private int cost;
    private int heu;
    private int steps;

    public Node(StateObservation s, Node p,ACTIONS a) {
        if(p==null){
            this.parent = p;
            this.state = s;
            foreAction=Types.ACTIONS.ACTION_NIL;
            cost=0;
            steps = 0;
            heu=heuristic(s);
            f=cost+heu;
        }
        else{
            this.parent=p;
            this.state=s;
            foreAction=a;
            steps=p.getStep()+1;
            cost=calCost(s);
            heu=heuristic(s);
            f=cost+heu;
        }

    }
    private int gridDist(Vector2d v0, Vector2d v1){
        return (int)(Math.abs(v0.x-v1.x)+Math.abs(v0.y-v1.y));
    }

    private int getStep() {
    	return steps;
    }
    
    private int calCost(StateObservation stateObs) {
    	cost= steps * 50;
    	ArrayList<Observation>[] fixedPositions = stateObs.getImmovablePositions();

        for (ArrayList<Observation> fixedPos : fixedPositions){
        	if(fixedPos.isEmpty())break;
            int iType = fixedPos.get(0).itype;
            switch (iType){
                case 2:
                    cost += 1000 * fixedPos.size();
                    break;
                case 5:
                    cost += 1000 * fixedPos.size();
                    break;
            }
        }
        return cost;
    }
    private int heuristic(StateObservation stateObs) {
        if (stateObs.getGameWinner() == Types.WINNER.PLAYER_WINS){
            return -9999999;
        }
        if (stateObs.getGameWinner() == Types.WINNER.PLAYER_LOSES){
            return 9999999;
        }
        ArrayList<Observation>[] fixedPositions = stateObs.getImmovablePositions();
        ArrayList<Observation>[] movingPositions = stateObs.getMovablePositions();
        Vector2d goalpos = new Vector2d(0,0) ;
        Vector2d avatarpos = stateObs.getAvatarPosition();


        int dist = 0;

        for (ArrayList<Observation> fixedPos : fixedPositions){
        	if(fixedPos.isEmpty())break;
            int iType = fixedPos.get(0).itype;
            switch (iType){
                case 7:
                    goalpos = fixedPos.get(0).position;
                    break;
            }
        }
        if (stateObs.getAvatarType()==1){//Î´ÄÃµ½Ô¿³×
            Vector2d keypos = movingPositions[0].get(0).position;
            if(movingPositions.length>=2) {
                for (int i = 0; i < movingPositions[1].size(); i++) {
                    if (movingPositions[1].get(i).position == keypos)//±ÜÃâ½«Ïä×ÓÍÆµ½Ô¿³×ÉÏ
                        return 99999999;
                }
            }
            dist=gridDist(avatarpos, keypos) + 2000;
            return dist;
        }else if(stateObs.getAvatarType()==4){//ÄÃµ½Ô¿³×
            dist = gridDist(avatarpos, goalpos);
            return dist;
        }
        return 9999999;
    }
    public Node getParent() {
        return parent;
    }
    public StateObservation getState() {
        return state;
    }
    public void changeActions(ACTIONS a) {
        foreAction=a;
    }
    public ACTIONS getAction() {
        return foreAction;
    }
    public int getCost(){
        return cost;
    }
    public void changeCost(int a){
        cost=a;
    }
    public  int getHeu(){
        return  heu;
    }
    public int getF(){
        return f;
    }
    public int compareTo(Object o) {
        return (int)(this.f - ((Node)o).f);
}
}

