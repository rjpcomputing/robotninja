/**
 * Main application entry point.
 * @author rpusztai
 */
public class NinjaBot
{
	private Navigation nav = null;
	private ObstacleAvoidance objectAvoid = null;
	private BTCommunication btComm = null;
	
	public NinjaBot()
	{
		nav = new Navigation();
		objectAvoid = new ObstacleAvoidance();
		btComm = new BTCommunication();
	}

	public Navigation GetNavigation()
	{
		return nav;
	}
	
	public ObstacleAvoidance GetObstacleAvoidance()
	{
		return objectAvoid;
	}
	
	public BTCommunication GetBTCommunication()
	{
		return btComm;
	}
	
	//	 Application entry point.
	public static void main( String[] args ) throws Exception
	{
		NinjaBot app = new NinjaBot();
		app.GetNavigation().StartMotors( Direction.FORWARD, 100 );
		Thread.sleep( 2000 );
	}
}
