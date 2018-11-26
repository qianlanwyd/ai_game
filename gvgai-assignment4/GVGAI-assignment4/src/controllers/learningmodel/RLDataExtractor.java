/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.learningmodel;

import tools.*;
import core.game.Observation;
import core.game.StateObservation;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;

import ontology.Types;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author yuy
 */
public class RLDataExtractor {
    public FileWriter filewriter;
    public static Instances s_datasetHeader = datasetHeader();

    public RLDataExtractor(String filename) throws Exception{

        filewriter = new FileWriter(filename+".arff");
        filewriter.write(s_datasetHeader.toString());
        /*
                // ARFF File header
        filewriter.write("@RELATION AliensData\n");
        // Each row denotes the feature attribute
        // In this demo, the features have four dimensions.
        filewriter.write("@ATTRIBUTE gameScore  NUMERIC\n");
        filewriter.write("@ATTRIBUTE avatarSpeed  NUMERIC\n");
        filewriter.write("@ATTRIBUTE avatarHealthPoints NUMERIC\n");
        filewriter.write("@ATTRIBUTE avatarType NUMERIC\n");
        // objects
        for(int y=0; y<14; y++)
            for(int x=0; x<32; x++)
                filewriter.write("@ATTRIBUTE object_at_position_x=" + x + "_y=" + y + " NUMERIC\n");
        // The last row of the ARFF header stands for the classes
        filewriter.write("@ATTRIBUTE Class {0,1,2}\n");
        // The data will recorded in the following.
        filewriter.write("@Data\n");*/

    }

    public static Instance makeInstance(double[] features, int action, double reward){
        features[s_datasetHeader.numAttributes()-2] = action;
        features[s_datasetHeader.numAttributes()-1] = reward;
        Instance ins = new Instance(1, features);
        ins.setDataset(s_datasetHeader);
        return ins;
    }

    public static double[] featureExtract(StateObservation obs){

        double[] feature = new double[s_datasetHeader.numAttributes()];  // 868 + 4 + 1(action) + 1(Q)

        Vector2d avatarPos=obs.getAvatarPosition();
        int i=3;
        LinkedList<Observation> allobj = new LinkedList<>();

        //敌人状态
        if( obs.getNPCPositions()!=null )
            for(ArrayList<Observation> l : obs.getNPCPositions()) allobj.addAll(l);
        for(Observation o : allobj){
            feature[i]=o.itype;
            i++;
        }
        ArrayList<Observation> allGhost = new ArrayList<>();
        if( obs.getPortalsPositions()!=null ) {
            for (ArrayList<Observation> l : obs.getPortalsPositions()) {
                allGhost.addAll(l);
            }
        }

        // 4 states
        feature[0] = obs.getGameTick();
        //feature[869] = obs.getAvatarSpeed();
        feature[1] = obs.getAvatarHealthPoints();
        feature[2] = obs.getAvatarType();

        i=7;

        //敌人距离
        for(Observation o:allGhost) {
            feature[i] =o.position.dist(avatarPos);
            i++;
        }
        for(Observation o:allGhost) {
            double delta_x=o.position.x-avatarPos.x;
            double delta_y=o.position.y-avatarPos.y;
            double dist=o.position.dist(avatarPos);
            feature[i] = Math.acos(delta_x/dist)+(delta_y<0?Math.PI:0);
            i++;
        }

        //钻石距离
        LinkedList<Observation> allobj2= new LinkedList<>();
        if( obs.getResourcesPositions()!=null )
            for(ArrayList<Observation> l : obs.getResourcesPositions()) allobj2.addAll(l);
        for(Observation o : allobj2){
            Vector2d p = o.position;
            feature[i]=p.dist(avatarPos);
            i++;
        }
        //feature[i]=obs.getAvatarLastAction().hashCode();
        //System.out.println(feature[i]);
        return feature;
    }

    public static Instances datasetHeader(){

        if (s_datasetHeader!=null)
            return s_datasetHeader;

        FastVector attInfo = new FastVector();
        // 448 locations
       /* for(int y=0; y<28; y++){
            for(int x=0; x<31; x++){
                Attribute att = new Attribute("object_at_position_x=" + x + "_y=" + y);
                attInfo.addElement(att);
            }
        }*/
        Attribute att = new Attribute("GameTick" ); attInfo.addElement(att);
        //att = new Attribute("AvatarSpeed" ); attInfo.addElement(att);
        att = new Attribute("AvatarHealthPoints" ); attInfo.addElement(att);
        att = new Attribute("AvatarType" ); attInfo.addElement(att);
        att = new Attribute("type1" ); attInfo.addElement(att);
        att = new Attribute("type2" ); attInfo.addElement(att);
        att = new Attribute("type3" ); attInfo.addElement(att);
        att = new Attribute("type4" ); attInfo.addElement(att);
        att = new Attribute("dis1" ); attInfo.addElement(att);
        att = new Attribute("dis2" ); attInfo.addElement(att);
        att = new Attribute("dis3" ); attInfo.addElement(att);
        att = new Attribute("dis4" ); attInfo.addElement(att);
        att = new Attribute("dir1" ); attInfo.addElement(att);
        att = new Attribute("dir2" ); attInfo.addElement(att);
        att = new Attribute("dir3" ); attInfo.addElement(att);
        att = new Attribute("dir4" ); attInfo.addElement(att);
        att = new Attribute("dim1" ); attInfo.addElement(att);
        att = new Attribute("dim2" ); attInfo.addElement(att);
        att = new Attribute("dim3" ); attInfo.addElement(att);
        att = new Attribute("dim4" ); attInfo.addElement(att);

        //action
        FastVector actions = new FastVector();
        actions.addElement("0");
        actions.addElement("1");
        actions.addElement("2");
        actions.addElement("3");
        att = new Attribute("actions", actions);
        attInfo.addElement(att);
        // Q value
        att = new Attribute("Qvalue");
        attInfo.addElement(att);

        Instances instances = new Instances("PacmanQdata", attInfo, 0);
        instances.setClassIndex( instances.numAttributes() - 1);

        return instances;
    }

}
