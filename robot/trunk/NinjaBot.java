/******************************************************************************
* Copyright (C) 2008 Ryan Pusztai, Adam Parker All rights reserved.
*
* Permission is hereby granted, free of charge, to any person obtaining
* a copy of this software and associated documentation files (the
* "Software"), to deal in the Software without restriction, including
* without limitation the rights to use, copy, modify, merge, publish,
* distribute, sublicense, and/or sell copies of the Software, and to
* permit persons to whom the Software is furnished to do so, subject to
* the following conditions:
*
* The above copyright notice and this permission notice shall be
* included in all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
* EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
* MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
* IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
* CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
* TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
* SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
******************************************************************************/

/**
 * Main application entry point.
 * @author rpusztai
 */
public class NinjaBot
{
	private static Navigation nav = null;
	private static ObstacleAvoidance objectAvoid = null;
	private static BTCommunication btComm = null;
	
	public NinjaBot()
	{
		nav = new Navigation();
		objectAvoid = new ObstacleAvoidance();
		btComm = new BTCommunication();
	}

	public static Navigation GetNavigation()
	{
		return nav;
	}
	
	public static ObstacleAvoidance GetObstacleAvoidance()
	{
		return objectAvoid;
	}
	
	public static BTCommunication GetBTCommunication()
	{
		return btComm;
	}
	
	//	 Application entry point.
	public static void main( String[] args ) throws Exception
	{
		NinjaBot app = new NinjaBot();
		
		// Start the ObstacleAvoidance thread.
		app.GetObstacleAvoidance().start();
		
		while(true)
		{
			app.GetNavigation().Navigate(app.GetBTCommunication().ReadCommand());
		}
	}
}
