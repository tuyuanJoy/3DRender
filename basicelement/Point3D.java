package basicelement;

public class Point3D {
    public double x, y, z;
    public double xOffset, yOffset, zOffset;

    public Point3D(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
        this.xOffset = 0;
        this.yOffset = 0;
        this.zOffset = 0;
    }
    public Point3D(){
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.xOffset = 0;
        this.yOffset = 0;
        this.zOffset = 0;
    }
    
	public double getAdjustedX( )
	{
		return this.x + this.xOffset;
	}

	public double getAdjustedY( )
	{
		return this.y + this.yOffset;
	}

	public double getAdjustedZ( )
	{
		return this.z + this.zOffset;
	}

    public static double dist( Point3D p1, Point3D p2 )
	{
		double x2 = Math.pow( p1.x - p2.x, 2 );
		double y2 = Math.pow( p1.y - p2.y, 2 );
		double z2 = Math.pow( p1.z - p2.z, 2 );
		return Math.sqrt( x2 + y2 + z2 );
	}
}
