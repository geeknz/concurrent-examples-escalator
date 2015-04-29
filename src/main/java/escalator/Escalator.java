package escalator;

import java.util.Random;

public class Escalator implements Runnable {


	private boolean direction = true;

	private int numberOfSpacesUp;
	private int numberOfSpacesDown;

	public Escalator( final int numberOfSpaces ) {
		this.numberOfSpacesUp = numberOfSpaces;
		this.numberOfSpacesDown = numberOfSpaces;
	}

	public synchronized void rideUp() throws InterruptedException {

		while( !direction || numberOfSpacesUp == 0 ){

			wait();
		}

		System.out.println( Thread.currentThread().getName() + " coming from the first floor, is entering the escalator" );
		numberOfSpacesUp--;

		wait( 1000 );

		System.out.println( Thread.currentThread().getName() + " coming from the first floor, is exiting the escalator" );
		numberOfSpacesUp++;

		notifyAll();
	}

	public synchronized void rideDown() throws InterruptedException {

		while( direction || numberOfSpacesDown == 0 ){

			wait();
		}

		System.out.println( Thread.currentThread().getName() + " coming from the second floor, is entering the escalator" );
		numberOfSpacesDown--;

		wait( 1000 );

		System.out.println( Thread.currentThread().getName() + " coming from the second floor, is exiting the escalator" );
		numberOfSpacesDown++;

		notifyAll();
	}

	private synchronized Escalator setDirection( boolean direction ) {

		this.direction = direction;
		notifyAll();
		return this;
	}

	@Override
	public void run() {

		try {

			while( true ) {

				Thread.sleep( 3000 );
				System.out.println( "Direction is about to change to allow travel from the second floor" );
				setDirection( false );

				Thread.sleep( 3000 );
				System.out.println( "Direction is about to change to allow travel from the first floor" );
				setDirection( true );
			}

		} catch ( InterruptedException ie ) {}

	}

	public static void main( final String... args ) {

		final Escalator escalator = new Escalator( 10 );
		final Random random = new Random();

		new Thread( escalator ).start();

		for ( int i = 0; i < 100; i++ ) {

			new Thread( new Person( random.nextBoolean(), escalator ) ).start();
		}
	}
}
