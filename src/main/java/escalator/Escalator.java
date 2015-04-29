package escalator;

import java.util.Random;

public class Escalator implements Runnable {

	private boolean direction = true;

	private boolean block = false;

	private int numberOfSpaces;

	public Escalator( final int numberOfSpaces ) {
		this.numberOfSpaces = numberOfSpaces;
	}

	public synchronized void rideUp() throws InterruptedException {

		while( block || !direction || numberOfSpaces == 0 ){

			wait();
		}

		System.out.println( Thread.currentThread().getName() + " coming from the first floor, is entering the escalator" );
		numberOfSpaces--;

		wait( 1000 );

		System.out.println( Thread.currentThread().getName() + " coming from the first floor, is exiting the escalator" );
		numberOfSpaces++;

		notifyAll();
	}

	public synchronized void rideDown() throws InterruptedException {

		while( block || direction || numberOfSpaces == 0 ){

			wait();
		}

		System.out.println( Thread.currentThread().getName() + " coming from the second floor, is entering the escalator" );
		numberOfSpaces--;

		wait( 1000 );

		System.out.println( Thread.currentThread().getName() + " coming from the second floor, is exiting the escalator" );
		numberOfSpaces++;

		notifyAll();
	}

	private synchronized Escalator setDirection( boolean direction ) throws InterruptedException {

		block = true;

		while( numberOfSpaces != 10 ) {

			wait();
		}

		this.direction = direction;

		block = false;

		notify();
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

		for ( int i = 0; i < 10000; i++ ) {

			new Thread( new Person( random.nextBoolean(), escalator ) ).start();
		}
	}
}
