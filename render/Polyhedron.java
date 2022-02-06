package render;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import basicelement.NewVector;
import basicelement.Point3D;
import render.*;
public class Polyhedron {
    protected NewPolygon[] polygons;
    private Color color;
    

    public Polyhedron(Color color, boolean decayColor, ArrayList<int[]> faces, ArrayList<Point3D> vertexs){
        polygons = loadPolygons(faces, vertexs);
        this.color = color;

        if ( decayColor )
		{
			this.setDecayingPolygonColor( );
		}
		else
		{
			this.setPolygonColor( );
		}
		this.sortPolygons( );

    }

    private NewPolygon[] loadPolygons(ArrayList<int[]> faces, ArrayList<Point3D> vertexs){
        NewPolygon[] polys = new NewPolygon[faces.size()];
        for(int i=0; i < faces.size(); i++){
            int[] face = faces.get(i);
            Point3D a, b, c;
            a = vertexs.get(face[0]);
            b = vertexs.get(face[1]);
            c = vertexs.get(face[2]);
            polys[i] = new NewPolygon(color, new Point3D[]{a,b,c});
        }
        return polys;
    }


    public void render( Graphics g)
	{
		for ( NewPolygon poly : this.polygons )
		{
			poly.render( g );
		}
	}

    private void setPolygonColor() {
        for ( NewPolygon poly : this.polygons )
		{
			poly.setColor( this.color );
		}
    }

    private void setDecayingPolygonColor() {
        double decayFactor = 0.97;
		for ( NewPolygon poly : this.polygons )
		{
			poly.setColor( this.color );
			int r = ( int ) ( this.color.getRed( ) * decayFactor );
			int g = ( int ) ( this.color.getGreen( ) * decayFactor );
			int b = ( int ) ( this.color.getBlue( ) * decayFactor );
			this.color = new Color( r, g, b );
		}
    }

	public void translate( double x, double y, double z )
	{
		for ( NewPolygon p : this.polygons )
		{
			p.translate( x, y, z );
		}
		this.sortPolygons( );
	}
    public void rotate( boolean CW, double xDegrees, double yDegrees, double zDegrees, NewVector lightVector )
	{
		for ( NewPolygon p : this.polygons )
		{
			p.rotate( CW, xDegrees, yDegrees, zDegrees, lightVector );
		}
		this.sortPolygons( );
	}

    
	public void setLighting( NewVector lightVector )
	{
		for ( NewPolygon p : this.polygons )
		{
			p.setLighting( lightVector );
		}
	}

    public NewPolygon[ ] getPolygons( )
	{
		return this.polygons;
	}

    private void sortPolygons( )
	{
		NewPolygon.sortPolygons( this.polygons );
	}


    
}
