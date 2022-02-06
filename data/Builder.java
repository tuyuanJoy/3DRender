package data;
import java.util.ArrayList;
import java.io.File;  
import java.io.FileNotFoundException;
import java.util.Scanner;

import basicelement.*;
import render.Display;

//read file and build shape
public class Builder{
    private String path;
    private ArrayList<Point3D> vertexs;
    private ArrayList<int[]> faces;
    private int numOfVertexs;
    private int numOfFaces;
    private Point3D minVer, maxVer;
    private double scale;
    

    public Builder(String path){
        this.path = path;
        minVer = new Point3D( Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
        maxVer = new Point3D(-Double.MIN_VALUE,-Double.MIN_VALUE,-Double.MIN_VALUE);
       
    }
    public ArrayList<Point3D> Vertexs(){
        return vertexs;
    }
    public ArrayList<int[]> Faces(){
        return faces;
    }
    public Point3D MaxVer(){
        return maxVer;
    }
    public Point3D MinVer(){
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
            Reader.nextInt();
           // System.out.println(numOfVertexs +", " + numOfFaces);
            
            loadVertex(Reader);
            loadFace(Reader);
            Reader.close();
        } catch (FileNotFoundException e){
            System.out.println(path + ": Not found");
            e.printStackTrace();
        }
    }

    private void loadVertex(Scanner reader) {
        vertexs = new ArrayList<Point3D>();
        
        for(int i=0; i< numOfVertexs; i++){
            Point3D point = new Point3D();
            point.x = reader.hasNextDouble()? reader.nextDouble(): 0;
            point.y = reader.hasNextDouble()? reader.nextDouble(): 0;
            point.z = reader.hasNextDouble()? reader.nextDouble(): 0;
           
         
            //System.out.println("Read Point: X_Y_Z: " +point.x + " " + point.y + " " +point.z);
            
            //Get max vertex and min vertex
            minVer.x = Math.min(minVer.x, point.x);
            minVer.y = Math.min(minVer.y, point.y);
            minVer.z = Math.min(minVer.z, point.z);
            maxVer.x = Math.max(maxVer.x, point.x);
            maxVer.y = Math.max(maxVer.y, point.y);
            maxVer.z = Math.max(maxVer.z, point.z);
         
            vertexs.add(point);
        }
       // reranglePoints(400);
    }

    public void reranglePoints(int size){
        double modelSize = Math.max(maxVer.x-minVer.x, maxVer.y-minVer.y);
        modelSize = Math.max( modelSize, maxVer.z-minVer.z);
        Point3D dataCenter = new Point3D(minVer.x+ modelSize/2,minVer.y+ modelSize/2, minVer.z+modelSize/2);
        Point3D Center = new Point3D(Display.WIDTH/2, Display.WIDTH/2,Display.WIDTH/2);
        NewVector transitionVec = new NewVector(Center,dataCenter);
        for(Point3D point : vertexs){
            point.x += transitionVec.x ;
            point.y += transitionVec.y ;
            point.z += transitionVec.z ;
          //  System.out.println("Rerangled Point: "+ point.x + " "+ point.y +" " + point.z  );
        }
    }

    public void Scale(){
        
    //    System.out.println(maxVer.x+"  "+minVer.x);
        double modelSize = Math.max(maxVer.x-minVer.x, maxVer.y-minVer.y);
        modelSize = Math.max( modelSize, maxVer.z-minVer.z);
        scale = Display.WIDTH / modelSize;
       
    }
        
    private void loadFace(Scanner Reader) {
        faces = new ArrayList<int[]>();
        for(int i=0; i< numOfFaces; i++){
            int a, b, c;
            Reader.nextInt();
            a = Reader.hasNextInt()? Reader.nextInt(): 0;
            b = Reader.hasNextInt()? Reader.nextInt(): 0;
            c = Reader.hasNextInt()? Reader.nextInt(): 0;
           // System.out.println("1:2:3     " + a + " : "+b+" : "+c);
            faces.add(new int[]{a,b,c});
        }
    }

    public static void main(String args[]){
        Builder builder = new Builder(System.getProperty("user.dir")+ "/Input/bunny.txt");
        builder.load();
    }


   
  
    
}