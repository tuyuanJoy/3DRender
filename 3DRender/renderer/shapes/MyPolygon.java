package renderer.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import renderer.RenderType;
import renderer.point.MyPoint;
import renderer.point.MyVector;
import renderer.point.PointConverter;

public class MyPolygon
{

	private static final double AmbientLight = 0.05;

	protected MyPoint[ ] points;

	private Color baseColor, lightingColor;
	private boolean visible;

	public MyPolygon( Color color, MyPoint... points )
	{
		this.baseColor = this.lightingColor = color;
		this.createPointsArray( points );
		this.updateVisibility( );
	}

	public MyPolygon( MyPoint... points )
	{
		this.baseColor = this.lightingColor = Color.WHITE;
		this.createPointsArray( points );
		this.updateVisibility( );
	}

	public void render( Graphics g , RenderType type )
	{
		Polygon poly = new Polygon( );
		for ( int i = 0; i < this.points.length; i++ )
		{
			Point p = PointConverter.convertPoint( this.points[ i ] );
			poly.addPoint( p.x, p.y );
		}
		if(type == RenderType.flat || type == RenderType.lineAndFlat){
			if ( !this.visible )
			return;
			//Flat Shading
			g.setColor( this.lightingColor );
			g.fillPolygon( poly );
		}
		if(type == RenderType.lineAndFlat){
			g.setColor( Color.BLACK);
			g.drawPolygon(poly);
		}
	
		if(type == RenderType.lines){
			//Filled with Visiable edge
			g.setColor( Color.WHITE);
			g.drawPolygon(poly);
		}
		
	}

	public MyPoint findSeletedp(double x, double y, MyPoint selectedPoint ){
	
		for ( MyPoint p : points )
		{
			if(PointConverter.convertPoint(p).x == x ||PointConverter.convertPoint(p).y == y){
				selectedPoint = selectedPoint.x < p.x? p:selectedPoint;
			}
		}
		return selectedPoint;
	}
	
	public void translate( double x, double y, double z )
	{
		for ( MyPoint p : points )
		{
			p.xOffset += x;
			p.yOffset += y;
			p.zOffset += z;
		}
		this.updateVisibility( );
	}

	public void rotate( boolean CW, double xDegrees, double yDegrees, double zDegrees, MyVector lightVector )
	{
		for ( MyPoint p : points )
		{
			PointConverter.rawRotateAxisX( p, CW, xDegrees );
			PointConverter.rawRotateAxisY( p, CW, yDegrees );
			PointConverter.rawRotateAxisZ( p, CW, zDegrees );
		}
		this.updateVisibility( );
		this.setLighting( lightVector );
	}

	

	public void setColor( Color color )
	{
		this.baseColor = color;
	}

	public static MyPolygon[ ] sortPolygons( MyPolygon[ ] polygons )
	{
		List<MyPolygon> polygonsList = new ArrayList<MyPolygon>( );

		for ( MyPolygon poly : polygons )
		{
			polygonsList.add( poly );
		}

		Collections.sort( polygonsList, new Comparator<MyPolygon>( )
		{
			@Override
			public int compare( MyPolygon p1, MyPolygon p2 )
			{
				MyPoint p1Average = p1.getAveragePoint( );
				MyPoint p2Average = p2.getAveragePoint( );
				double p1Dist = MyPoint.dist( p1Average, MyPoint.origin );
				double p2Dist = MyPoint.dist( p2Average, MyPoint.origin );
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
			polygons[ i ] = polygonsList.get( i );
		}

		return polygons;
	}

	public void setLighting( MyVector lightVector )
	{
		//norm * light
		double dot = MyVector.dot( normVectorofFace(points), lightVector );
		double sign = dot < 0 ? -1 : 1;
		dot *= sign ;
		dot = ( dot + 1 ) / 2 * ( 1 - AmbientLight );
		double lightRatio = Math.min( 1, Math.max( 0, AmbientLight + dot ) );
		this.updateLightingColor( lightRatio );
	}

	public boolean isVisible( )
	{
		return this.visible;
	}

	protected void createPointsArray( MyPoint[ ] points )
	{
		this.points = new MyPoint[ points.length ];
		for ( int i = 0; i < points.length; i++ )
		{
			MyPoint p = points[ i ];
			this.points[ i ] = new MyPoint( p.x, p.y, p.z );
		}
	}
	private MyVector normVectorofFace(MyPoint[] points){
		MyVector v1 = new MyVector( this.points[ 0 ], this.points[ 1 ] );
		MyVector v2 = new MyVector( this.points[ 1 ], this.points[ 2 ] );
		MyVector normal = MyVector.normalize( MyVector.cross( v2, v1 ) );
	
		return normal;
	}
	private void updateVisibility( )
	{
		MyVector p_v = new MyVector(new MyPoint(400, 0, 0), points[0]);
		double dotProduct = MyVector.dot(p_v, normVectorofFace(points));
		//double dotProduct = MyVector.dot(new MyPoint(400, 0, 0), 
		this.visible = -dotProduct >=0;
		//this.visible = this.getAverageX( ) < 0;
	}
	public double getAverageX( )
	{
		double sum = 0;
		for ( MyPoint p : this.points )
		{
			sum += p.x + p.xOffset;
		}

		return sum / this.points.length;
	}
	private void updateLightingColor( double lightRatio )
	{
		int red = ( int ) ( this.baseColor.getRed( ) * lightRatio );
		int green = ( int ) ( this.baseColor.getGreen( ) * lightRatio );
		int blue = ( int ) ( this.baseColor.getBlue( ) * lightRatio );
		this.lightingColor = new Color( red, green, blue );
	}

	private MyPoint getAveragePoint( )
	{
		double x = 0;
		double y = 0;
		double z = 0;
		for ( MyPoint p : this.points )
		{
			x += p.x + p.xOffset;
			y += p.y + p.yOffset;
			z += p.z + p.zOffset;
		}

		x /= this.points.length;
		y /= this.points.length;
		z /= this.points.length;

		return new MyPoint( x, y, z );
	}

}
