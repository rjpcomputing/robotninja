public class CartesianCoordinates {
	private int x;
	private int y;
	private int k;
	private int r;
	
	public CartesianCoordinates(int pX, int pY, int pR, int pK)
	{
		x = pX;
		y = pY;
		k = pK;
		r = pR;
	}
	
	public CartesianCoordinates()
	{
		x = 0;
		y = 0;
		k = 0;
		r = 0;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getK()
	{
		return k;
	}
	
	public int getR()
	{
		return r;
	}
	
	public void setX(int pX)
	{
		x = pX;
	}
	
	public void setY(int pY)
	{
		y = pY;
	}
	
	public void setK(int pK)
	{
		k = pK;
	}
	
	public void setR(int pR)
	{
		r = pR;
	}
}
