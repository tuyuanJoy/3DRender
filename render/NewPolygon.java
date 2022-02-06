package render;
import basicelement.*;
import javafx.scene.transform.Scale;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NewPolygon {

    //Light
    private static final double AmbientLight = 0.05;
    protected Point3D[] points;
    //Color of itself and color of the light
    private Color baseColor, lightingColor;
    //If the polygon is hiden
    private boolean visible;

    public NewPolygon(Color color, Point3D[] points){
        this.baseColor  = color;
        this.lightingColor = Color.white;
        this.createPointsArray(points);
        this.updateVisibility();
        
    }
    public boolean isVisible( )
	{
		return this.visible;
	}
	protected void createPointsArray( Point3D[] points )
	{
		this.points = new Point3D[ points.length ];
		for ( int i = 0; i < points.length; i++ )
		{
			Point3D p = points[i];
			this.points[i] = new Point3D(p.x, p.y, p.z);
		}
	}

    public void render(Graphics g){
        if(!this.visible) return;
        Polygon poly = new Polygon();
        for(int i =0; i< this.points.length; i++){
            Point p = PointConverter.convertPoint( this.points[ i ]);
			poly.addPoint( p.x, p.y );
           // System.out.println( " scale  "+ p.x + "  conver  " + p.y);
        }
       // System.out.println(lightingColor + "Color?? " + baseColor);
        g.setColor(this.baseColor);
        
		g.drawPolygon( poly );
    }

    /*
	public void shift( )
	{
		for ( Point3D p : points )
		{
			p.shift( );
		}
	}*/

    public void translate( double x, double y, double z )
	{
		for ( Point3D p : points )
		{
			p.xOffset += x;
			p.yOffset += y;
			p.zOffset += z;
		}
		this.updateVisibility( );
	}
    public void rotate( boolean CW, double xDegrees, double yDegrees, double zDegrees, NewVector lightVector )
	{
		for ( Point3D p : points )
		{
			PointConverter.rotateAxisX( p, CW, xDegrees );
			PointConverter.rotateAxisY( p, CW, yDegrees );
			PointConverter.rotateAxisZ( p, CW, zDegrees );

		}
		this.updateVisibility( );
		this.setLighting( lightVector );
	}

    private void updateVisibility( )
	{
		this.visible = this.getAverageX( ) < 0;
	}
    public void setColor( Color color )
	{
		this.baseColor = color;
	}
	public double getAverageX( )
	{
		double sum = 0;
		for ( Point3D p : this.points )
		{
			sum += p.x + p.xOffset;
		}

		return sum / this.points.length;
	}

    public static NewPolygon[ ] sortPolygons( NewPolygon[] polygons )
	{
        List<NewPolygon> polygonsList = new ArrayList<NewPolygon>( );

		for ( NewPolygon poly : polygons )
		{
			polygonsList.add( poly );
		}
		
		Collections.sort( polygonsList, new Comparator<NewPolygon>( )
		{
			@Override
			public int compare( NewPolygon p1, NewPolygon p2 )
			{
				Point3D p1Average = p1.getAveragePoint( );
				Point3D p2Average = p2.getAveragePoint( );
				double p1Dist = Point3D.dist( p1Average, new Point3D() );
				double p2Dist = Point3D.dist( p2Average, new Point3D() );
				double diff = p1Dist - p2Dist;
				if ( diff == 0 )
				{
					return 0;
				}
				return diff < 0 ? 1 : -1;
			}
		} );

		for ( int i = 0; i < polygons.length; i++ )
		{
			polygons[i] = polygonsList.get(i);
		}

		return polygons;
	}
	private Point3D getAveragePoint( )
	{
		double x = 0;
		double y = 0;
		double z = 0;
		for ( Point3D p : this.points )
		{
			x += p.x + p.xOffset;
			y += p.y + p.yOffset;
			z += p.z + p.zOffset;
		}

		x /= this.points.length;
		y /= this.points.length;
		z /= this.points.length;

		return new Point3D( x, y, z );
	}

    public void setLighting( NewVector lightVector )
	{
		if ( this.points.length < 3 )
		{
			return;
		}

		NewVector v1 = new NewVector( this.points[ 0 ], this.points[ 1 ] );
		NewVector v2 = new NewVector( this.points[ 1 ], this.points[ 2 ] );
		NewVector normal = NewVector.normalize( NewVector.cross( v2, v1 ) );
		double dot = NewVector.dot( normal, lightVector );
		double sign = dot < 0 ? -1 : 1;
		dot = sign * dot * dot;
		dot = ( dot + 1 ) / 2 * ( 1 - AmbientLight );

		double lightRatio = Math.min( 1, Math.max( 0, AmbientLight + dot ) );
		this.updateLightingColor( lightRatio );
	}

	private void updateLightingColor( double lightRatio )
	{
		int red = ( int ) ( this.baseColor.getRed( ) * lightRatio );
		int green = ( int ) ( this.baseColor.getGreen( ) * lightRatio );
		int blue = ( int ) ( this.baseColor.getBlue( ) * lightRatio );
       // System.out.println(red + "  " + green+"  " + blue);
		this.lightingColor = new Color( red, green, blue );
	}


}
