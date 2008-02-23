/**
 * This class is just a data holder for an enumeration. This is a work
 * around because, only recently, Java implimented enums. This is more
 * compatible.
 * @author rpusztai
 *
 */
public final class Direction
{
	public static final Direction BACKWARD = new Direction( -1 );
	public static final Direction FORWARD = new Direction( 1 );

	private final int direction;
	
	private Direction( int direct )
	{
		this.direction = direct;
	}
}
