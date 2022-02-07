package renderer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import renderer.point.MyVector;
import renderer.point.PointConverter;
import renderer.shapes.Polyhedron;
import renderer.world.*;
import renderer.input.*;

public class Display extends Canvas implements Runnable
{
	private Thread thread;
	private JFrame frame;
	private static String title = "3D Renderer";
	public static final int WIDTH = 800;
	public static final int HEIGHT = 800;
	private static boolean running = false;
	Polyhedron tetra;

	//mouse position, center
	private int initialX, initialY = WIDTH/2;
	private double mouseSensitivity = 2.5; //adjustable
	private Builder builder;//fileloader and move data center
	//Light
	private MyVector lightVector = MyVector.normalize( new MyVector( 1, 1, 1 ) );
	//user input
	private Mouse mouse;

	public Display( )
	{
		this.frame = new JFrame( );
		Dimension size = new Dimension( WIDTH, HEIGHT ); //800 800
		this.setPreferredSize( size );

		//For user input
		this.mouse = new Mouse();
		this.addMouseListener( this.mouse );
		this.addMouseMotionListener( this.mouse );
		this.addMouseWheelListener( this.mouse );
	}

	public static void main( String[ ] args )
	{
		Display display = new Display( );
		display.frame.setTitle( title );
		display.frame.add( display );
		display.frame.pack( );
		display.frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		display.frame.setLocationRelativeTo( null );
		display.frame.setResizable( false );
		display.frame.setVisible( true );

		display.start( );
	}

	public void init(){

		this.builder = new Builder(System.getProperty("user.dir")+ "\\InputData\\bunny.txt");
        this.builder.load();
		PointConverter.setScale(0.25* WIDTH/builder.ModelSize());
		System.out.println(0.25* WIDTH/builder.ModelSize());
        this.tetra = new Polyhedron(Color.GRAY, builder.Faces(), builder.Vertexs());
		tetra.setLighting(lightVector);
	}
	public synchronized void start( )
	{
		running = true;
		this.thread = new Thread( this, "Display" );
		this.thread.start( );
	}
	public synchronized void stop( )
	{
		running = false;
		try
		{
			this.thread.join( );
		}
		catch ( InterruptedException e )
		{
			e.printStackTrace( );
		}
	}
	
	@Override
	public void run( )
	{
		long lastTime = System.nanoTime( );
		long timer = System.currentTimeMillis( );
		final double ns = 1000000000.0 / 60; // limit
		double delta = 0;
		int frames = 0;
		init(); // Initialize data info and construct
		//Change frame in same amount of time, not depending on machine
		while ( running )
		{
			long now = System.nanoTime( );
			delta += ( now - lastTime ) / ns;
			lastTime = now;
			while ( delta >= 1 )
			{
				update( ); //Receive user input
				delta--;
				render( ); //Render according to user input
				frames++;
			}
			if ( System.currentTimeMillis( ) - timer > 1000 )
			{
				timer += 1000;
				frames = 0; //reset frame
			}
		}
		stop( );
	}
	private void render( )
	{
		//manage complex memory
		BufferStrategy bs = this.getBufferStrategy( );
		if ( bs == null )
		{
			this.createBufferStrategy( 3 );
			return;
		}
		Graphics g = bs.getDrawGraphics( );

		//change every frame, we need a background set it to black or white 
		g.setColor( Color.BLACK );
		g.fillRect( 0, 0, WIDTH * 2, HEIGHT * 2 );

		//tetra.render(g, RenderType.lines);
		//tetra.render(g, RenderType.flat);
		tetra.render(g, RenderType.lineAndFlat);

		g.dispose( );
		bs.show( );

	}
	private void update( )
	{
		//tetra.rotate( true, 0,  0, 1,  lightVector);
		//tetra.translate(0, 0, 1);
		int x = this.mouse.getX( );
		int y = this.mouse.getY( );
		if ( this.mouse.getButton( ) == ClickType.LeftClick )
		{
			tetra.findSelectedPoint(x,y);
			int xDif = x - initialX;
			int yDif = y - initialY;
			//translation
			tetra.translate( 0, xDif/(0.25* WIDTH/builder.ModelSize()),  -yDif/(0.25* WIDTH/builder.ModelSize()) );
			initialX = x;
			initialY= y;
		}
		else if ( this.mouse.getButton( ) == ClickType.RightClick )
		{
			int xDif = x - initialX;
			int yDif = y - initialY;
			//Rotation
			//tetra.rotate( true, -xDif / mouseSensitivity,  -yDif / mouseSensitivity, -xDif / mouseSensitivity , lightVector);
			tetra.rotate( true, 0,  yDif / mouseSensitivity, xDif / mouseSensitivity , lightVector);
			initialX = x;
			initialY= y;
		
		}else if (this.mouse.getButton() == ClickType.Unknown){
			initialX = WIDTH/2;
			initialY = WIDTH/2;
		}
		//Scale with mouse wheel
		if ( this.mouse.isScrollingUp( ) )
		{
			PointConverter.zoomIn( );
		}
		else if ( this.mouse.isScrollingDown( ) )
		{
			PointConverter.zoomOut( );
		}
		this.mouse.resetScroll();
	}

}
