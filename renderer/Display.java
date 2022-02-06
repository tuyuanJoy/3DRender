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
	private static final long serialVersionUID = 1L;

	private Thread thread;
	private JFrame frame;
	private static String title = "3D Renderer";
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	private static boolean running = false;
	Polyhedron tetra;
	private int initialX, initialY = 500;
	private double mouseSensitivity = 2.5;
	private Builder builder;
	double centerX = 0;
	double centerY = 0;
	double centerZ = 0;
	double size = 200;
	double moveSpeed = 20;
	private MyVector lightVector = MyVector.normalize( new MyVector( 1, 1, 1 ) );

	private Mouse mouse;

	public Display( )
	{
		this.frame = new JFrame( );

		Dimension size = new Dimension( WIDTH, HEIGHT );
		this.setPreferredSize( size );

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

		this.builder = new Builder(System.getProperty("user.dir")+ "\\InputData\\Doraemon.txt");
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
		final double ns = 1000000000.0 / 60;
		double delta = 0;
		int frames = 0;

		//this.entityManager.init( this.userInput );
		init();
		while ( running )
		{
			long now = System.nanoTime( );
			delta += ( now - lastTime ) / ns;
			lastTime = now;
			while ( delta >= 1 )
			{
				update( );
				delta--;
				render( );
				frames++;
			}

			if ( System.currentTimeMillis( ) - timer > 1000 )
			{
				timer += 1000;
				this.frame.setTitle( title + " | " + frames + " fps" );
				frames = 0;
			}
		}

		stop( );
	}

	private void render( )
	{
		BufferStrategy bs = this.getBufferStrategy( );
		if ( bs == null )
		{
			this.createBufferStrategy( 3 );
			return;
		}

		Graphics g = bs.getDrawGraphics( );

		g.setColor( Color.BLACK );
		g.fillRect( 0, 0, WIDTH * 2, HEIGHT * 2 );

		tetra.render(g);
		//this.entityManager.render( g );

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
			int xDif = x - initialX;
			int yDif = y - initialY;
			System.out.println(initialX + " : " +xDif + " ... " + yDif);
			tetra.translate( 0, xDif/10,  -yDif/10 );
			initialX = x;
			initialY= y;
		}
		else if ( this.mouse.getButton( ) == ClickType.RightClick )
		{
			int xDif = x - initialX;
			int yDif = y - initialY;

			tetra.rotate( true, -xDif / mouseSensitivity,  -yDif / mouseSensitivity, -xDif / mouseSensitivity , lightVector);
			initialX = x;
			initialY= y;
		
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
