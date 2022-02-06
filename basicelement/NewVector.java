package basicelement;

public class NewVector {
    public double x, y, z;

    public NewVector(){
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public NewVector(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public NewVector(Point3D p1, Point3D p2){
        this.x = p2.x = p1.x;
        this.y = p2.y - p1.y;
        this.z = p2.z - p1.z;
    }

    public static double dot(NewVector v1, NewVector v2){
        return v1.x*v2.x + v1.y*v2.y +v1.z*v2.z;
    }

    public static NewVector cross(NewVector v1, NewVector v2){
        return new NewVector(
            v1.y*v2.z - v1.z*v2.y,
            v1.z*v2.x - v1.x*v2.z,
            v1.x*v2.y - v1.y*v2.x);
    }

    public static NewVector normalize(NewVector vec){
        double magnitude = Math.sqrt(vec.x*vec.x + vec.y*vec.y + vec.z*vec.z);
        return new NewVector(vec.x/magnitude, vec.y/magnitude, vec.z/magnitude);
    }
}
