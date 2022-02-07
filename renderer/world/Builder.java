package renderer.world;  

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import renderer.point.*;

//read file and build shape
public class Builder{
    private String path;
    private ArrayList<MyPoint> vertexs;
    private ArrayList<int[]> faces;
    private int numOfVertexs;
    private int numOfFaces;
    private MyPoint minVer, maxVer;
    private double modelSize;
    

    public Builder(String path){
        this.path = path;
        minVer = new MyPoint( Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
        maxVer = new MyPoint(-Double.MIN_VALUE,-Double.MIN_VALUE,-Double.MIN_VALUE);
       //load();
    }
    public double ModelSize(){
        return modelSize;
    }
    public ArrayList<MyPoint> Vertexs(){
        return vertexs;
    }
    public ArrayList<int[]> Faces(){
        return faces;
    }
    public MyPoint MaxVer(){
        return maxVer;
    }
    public MyPoint MinVer(){
        return minVer;
    }

    public void load(){
        try{
            File Obj = new File(path);
            Scanner Reader = new Scanner(Obj);
            Reader.nextLine(); //read "OFF"
            //Store numbers data
            numOfVertexs = Reader.nextInt();
            numOfFaces = Reader.nextInt();
            Reader.nextInt();//Edge, 0 edge in these dataset
            loadVertex(Reader);
            loadFace(Reader);
            Reader.close();
        } catch (FileNotFoundException e){
            System.out.println(path + ": Not found");
            e.printStackTrace();
        }
    }

    private void loadVertex(Scanner reader) {
        vertexs = new ArrayList<MyPoint>();
        
        for(int i=0; i< numOfVertexs; i++){
            MyPoint point = new MyPoint(0,0,0);
            point.x = reader.hasNextDouble()? reader.nextDouble(): 0;
            point.y = reader.hasNextDouble()? reader.nextDouble(): 0;
            point.z = reader.hasNextDouble()? reader.nextDouble(): 0;       
            //Get max vertex and min vertex for scaling the object in view space
            minVer.x = Math.min(minVer.x, point.x);
            minVer.y = Math.min(minVer.y, point.y);
            minVer.z = Math.min(minVer.z, point.z);
            maxVer.x = Math.max(maxVer.x, point.x);
            maxVer.y = Math.max(maxVer.y, point.y);
            maxVer.z = Math.max(maxVer.z, point.z);
         
            vertexs.add(point);
        }
        reranglePoints();
    }

    public void reranglePoints(){
        //the longest edge
        modelSize = Math.max(maxVer.x-minVer.x, maxVer.y-minVer.y);
        modelSize = Math.max( modelSize, maxVer.z-minVer.z);
        MyPoint dataCenter = new MyPoint(minVer.x+ modelSize/2,minVer.y+ modelSize/2, minVer.z+modelSize/2);
        for(MyPoint point : vertexs){
            MyVector transitionVec = new MyVector(dataCenter,point);
            point.x += transitionVec.x ;
            point.y += transitionVec.y ;
            point.z += transitionVec.z ;
        }
    }

 
    private void loadFace(Scanner Reader) {
        faces = new ArrayList<int[]>();
        for(int i=0; i< numOfFaces; i++){
            int a, b, c;
            Reader.nextInt();
            a = Reader.hasNextInt()? Reader.nextInt(): 0;
            b = Reader.hasNextInt()? Reader.nextInt(): 0;
            c = Reader.hasNextInt()? Reader.nextInt(): 0;
            faces.add(new int[]{a,b,c});
        }
    }

}