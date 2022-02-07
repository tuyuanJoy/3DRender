package renderer.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.event.RowSorterEvent.Type;

import renderer.RenderType;
import renderer.point.MyPoint;
import renderer.point.MyVector;

public class Polyhedron
{
	protected MyPolygon[ ] polygons;
	private Color color;
	private MyPoint selectedPoint;

	public Polyhedron( Color color,ArrayList<int[]>faces, ArrayList<MyPoint>vertexs )
	{
		this.color = color;
		this.polygons = loadPolygons(faces, vertexs);
		this.setPolygonColor( );
		this.selectedPoint = new MyPoint(Double.MIN_VALUE,Double.MIN_VALUE,Double.MIN_VALUE);
	}

	private MyPolygon[] loadPolygons(ArrayList<int[]> faces, ArrayList<MyPoint> vertexs){
        MyPolygon[] polys = new MyPolygon[faces.size()];
        for(int i=0; i < faces.size(); i++){
            int[] face = faces.get(i);
            MyPoint a, b, c;
            a = vertexs.get(face[0]);
            b = vertexs.get(face[1]);
            c = vertexs.get(face[2]);
            polys[i] = new MyPolygon(color, new MyPoint[]{a,b,c});
        }
        return polys;
    }

	public void render( Graphics g , RenderType type)
	{
		for ( MyPolygon poly : this.polygons )
		{
			poly.render( g , type );
		}
	}

	public void findSelectedPoint(double x, double y){
	
		for ( MyPolygon poly : this.polygons )
		{
			selectedPoint = poly.findSeletedp(x, y, selectedPoint);
		}
		//System.out.println("Seleteced :" + selectedPoint.x +"  : "+ selectedPoint.y +" :  "+selectedPoint.z);
		
	}

	public void translate( double x, double y, double z )
	{
		for ( MyPolygon p : this.polygons )
		{
			p.translate( x, y, z );
		}
		this.sortPolygons( );
	}

	public void rotate( boolean CW, double xDegrees, double yDegrees, double zDegrees, MyVector lightVector )
	{
		for ( MyPolygon p : this.polygons )
		{
			p.rotate( CW, xDegrees, yDegrees, zDegrees, lightVector );
		}
		this.sortPolygons( );
	}

	public void setLighting( MyVector lightVector )
	{
		for ( MyPolygon p : this.polygons )
		{
			p.setLighting( lightVector );
		}
	}

	private void sortPolygons( )
	{
		MyPolygon.sortPolygons( this.polygons );
	}

	private void setPolygonColor( )
	{
		for ( MyPolygon poly : this.polygons )
		{
			poly.setColor( this.color );
		}
	}

}
