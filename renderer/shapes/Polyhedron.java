package renderer.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import renderer.point.MyPoint;
import renderer.point.MyVector;

public class Polyhedron
{
	protected MyPolygon[ ] polygons;
	private Color color;

	public Polyhedron( Color color,ArrayList<int[]>faces, ArrayList<MyPoint>vertexs )
	{
		this.color = color;
		this.polygons = loadPolygons(faces, vertexs);
		this.setPolygonColor( );
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

	public void render( Graphics g )
	{
		for ( MyPolygon poly : this.polygons )
		{
			poly.render( g );
		}
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
