package render;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import basicelement.NewVector;
import basicelement.*;
import data.Builder;
import input.*;

public class Display extends Canvas implements Runnable {
    private Thread thread;
    private JFrame frame;
    private static String title = "3D viewer";
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;
    private static boolean running = false;
	private Mouse mouse;
    Polyhedron polys;
    Builder builder;

    
	public Display( )
	{
		this.frame = new JFrame( );

		Dimension size = new Dimension( WIDTH, HEIGHT );
		this.setPreferredSize( size );

		this.mouse = new Mouse();
		this.addMouseListener( this.mouse );
		this.addMouseMotionListener( this.mouse );
		this.addMouseWheelListener( this.mouse );
		
        /*
		this.userInput = new UserInput( );

		this.entityManager = new EntityManager( );

		
		this.addKeyListener( this.userInput.keyboard );
        */
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

    private void init(){
        this.builder = new Builder(System.getProperty("user.dir")+ "/InputData/bunny.txt");
        this.builder.load();
        //builder.reranglePoints(WIDTH/2, WIDTH);
        this.polys = new Polyhedron(Color.GRAY,false, builder.Faces(), builder.Vertexs());
		//polys.rotate(true, 0, 20, 20,  NewVector.normalize( new NewVector(1,1,1)));

    }
    @Override
    public void run() {
        long lastTime = System.nanoTime( );
		long timer = System.currentTimeMillis( );
		final double ns = 1000000000.0 / 60;
		double delta = 0;
		int frames = 0;
        init();
		//this.entityManager.init( this.userInput );

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

	
        polys.render(g);
	//	this.entityManager.render( g );

		g.dispose( );
		bs.show( );

	}

	ClickType prevMouse = ClickType.Unknown;
	int initialX, initialY;
    private void update( )
	{
		//System.out.println(this.mouse.getX());
		//this.polys.rotate(true, 15,1, 1,  NewVector.normalize( new NewVector(1,1,1)));
		
		int x = this.mouse.getX();
		int y = this.mouse.getY();
		if(this.mouse.GetButton() == ClickType.LeftClick){
			//if(prevMouse!= ClickType.Unknown){	System.out.println(x + "   " + y);
				int xDif = x -initialX;
				int yDif = y -initialY;
				System.out.println(yDif + "......" + yDif);
			
				this.polys.rotate(true, 0, -yDif, -xDif,  NewVector.normalize( new NewVector(1,1,1)));
			//}

			initialX = x;
			initialY = y;
		}
		else if(this.mouse.GetButton() == ClickType.RightClick){
			int xDif = x - initialX;
			this.polys.rotate(true, -xDif, 0, 0,  NewVector.normalize( new NewVector(1,1,1)));
		}
		if(this.mouse.isScrollingUp()){
			PointConverter.zoomIn();
		}else if(this.mouse.isScrollingDown()){
			PointConverter.zoomOut();
		}
	//	this.entityManager.update( );
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
}
