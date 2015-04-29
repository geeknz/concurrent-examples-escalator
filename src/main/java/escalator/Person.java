package escalator;

public class Person implements Runnable {

	private final boolean direction;
	private final Escalator escalator;

	public Person( final boolean direction, final Escalator escalator ) {

		this.direction = direction;
		this.escalator = escalator;
	}

	@Override
	public void run() {

		try {

			if ( direction ) {
				escalator.rideUp();
			}

			else {
				escalator.rideDown();
			}

		} catch ( final InterruptedException ie ) {}
	}
}
