package basicelement;
import java.awt.Point;

import render.Display;


public class PointConverter {
    //zoom in zoom out
    private static double scale = 4;
    private static final double ZoomFactor = 1.2; 
     
    public static void zoomIn( )
	{
		scale *= ZoomFactor;
	}

	public static void zoomOut( )
	{
		scale /= ZoomFactor;
	}

    public static Point convertPoint(Point3D point3d){
      // System.out.println(scale + " xyz  "+ point3d.getAdjustedX() + " , " + point3d.getAdjustedY()+" , " +point3d.getAdjustedZ());
        double x3d = point3d.getAdjustedZ() * scale;
        double y3d = point3d.getAdjustedY() * scale;
        double depth = point3d.getAdjustedX() * scale;
        double[] newValue = scale(x3d, y3d, depth);
        int x2d = ( int ) ( Display.WIDTH / 2  + newValue[ 0 ] );
		int y2d = ( int ) ( Display.HEIGHT/1.2  - newValue[ 1 ] );
		Point point2D = new Point( x2d, y2d );
      //  System.out.println( " after  "+ x2d + " , " +y2d+" , " );
		return point2D;
    }
   
    private static double[] scale(double x3d, double y3d, double depth) {
        double dist = Math.sqrt( x3d * x3d + y3d * y3d );
		double theta = Math.atan2( y3d, x3d );
        // camera pos
		double depth2 = 300 - depth;
        // After experiments
		double localScale = Math.abs( 1400 / ( depth2 + 1400 ) );
		dist *= localScale;
		double[ ] newVal = new double[ 2 ];
		newVal[ 0 ] = dist * Math.cos( theta );
		newVal[ 1 ] = dist * Math.sin( theta );
		return newVal;
    }

    public static void rotateAxisX(Point3D p, boolean clockwise, double degrees){
        
        //System.out.println(p.y + " Before" + p.z );
        double radius = Math.sqrt( p.y * p.y + p.z * p.z);
		double theta = Math.atan2( p.z, p.y );
		theta += 2 * Math.PI/360 * degrees * ( clockwise ? -1 : 1 );
		p.y = radius * Math.cos( theta );
		p.z = radius * Math.sin( theta );
        
        //System.out.println(p.y + " After" + p.z );
    }
    public static void rotateAxisY(Point3D p, boolean clockwise, double degrees){
     
        double radius = Math.sqrt( p.x* p.x + p.z * p.z );
		double theta = Math.atan2( p.x, p.z );
		theta += 2 * Math.PI/360 * degrees * ( clockwise ? -1 : 1 );
		p.y = radius * Math.sin( theta );
		p.z = radius * Math.cos( theta );
    }
    public static void rotateAxisZ(Point3D p, boolean clockwise, double degrees){
      
        double radius = Math.sqrt( p.y * p.y + p.x * p.x );
		double theta = Math.atan2( p.y, p.x );
		theta += 2 * Math.PI/360 * degrees * ( clockwise ? -1 : 1 );
		p.y = radius * Math.sin( theta );
		p.z = radius * Math.cos( theta );
    }
}
