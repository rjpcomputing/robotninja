import java.awt.Image;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;
import java.awt.*;
import java.util.ArrayList;

public class CircleDetection {
	private BufferedImage img;
	private ArrayList<CartesianCoordinates> circleCoords;
	private int[][][] accumulator;
	private int accumulatorX;
	private int accumulatorY;
	private int accumulatorZ;
	private int noCircles;
	private int lowR;
	private int highR;
	private int imgWidth;
	private int imgHeight;
	
	public CircleDetection(BufferedImage pImg, int pLowR, int pHighR, int pNoCircles)
	{
		img = pImg;
		noCircles = pNoCircles;
		lowR = pLowR;
		highR = pHighR;
		imgWidth = img.getWidth();
		imgHeight = img.getHeight();
		circleCoords = null;

		accumulatorX = imgWidth;
		accumulatorY = imgHeight;
		accumulatorZ = Math.abs(highR-lowR);

		accumulator = new int[accumulatorX][accumulatorY][accumulatorZ];

		// go through every vertical line in the picture
		for(int a = 0; a < imgWidth; a++)
		{
			// go through every horizontal line
			// (a, b) now represents a pixel
			for (int b = 0; b < imgHeight; b++)
			{
				// if the pixel is white, we have found an edge
				if (img.getRGB(a, b) == -1)
				{
					// add CartesianCoordinates by the different radius
					// keep the range between high radius and low radius
					// low or there will be problems!
					for (int r = lowR; r < highR; r++)
					{
						// go through every single value of
						// x for the circle and add it to the accumulator
						for (int x = (a - r); x < (a + r); x++)
						{
							int yOne = getYFromCircle(r, a, b, x, 1);
							int yTwo = getYFromCircle(r, a, b, x, -1);

							if (yOne > 0 && yOne < imgHeight && x > 0 && x < imgWidth)
							{
								accumulator[x][yOne][(r-lowR)]++;
							}

							if (yTwo > 0 && yTwo < imgHeight && x > 0 && x < imgWidth)
							{
								accumulator[x][yTwo][(r-lowR)]++;
							}
						}
					}
				}
			}
		}
		circleCoords = getCoordsFromAccumulator();
	}

	public ArrayList<CartesianCoordinates> getAllCoords()
	{
		return circleCoords;
	}
	
	public int getYFromCircle(int r, int a, int b, int x, int sign)
	{

		return (int)Math.round(sign * (Math.sqrt(Math.pow(r, 2) - Math.pow((x-a), 2))) + b);
	}

	public ArrayList<CartesianCoordinates> getCoordsFromAccumulator()
	{
		ArrayList<CartesianCoordinates> coords = new ArrayList(noCircles);

		for (int i = 0; i < accumulator.length; i++)
		{
			for (int j = 0; j < accumulator[0].length; j++)
			{
				for (int k = 0; k < accumulator[0][0].length; k++)
				{
					CartesianCoordinates newCoords = new CartesianCoordinates(i, j, k + lowR, accumulator[i][j][k]);
					attemptAddToArrayList(coords, newCoords);
				}
			}
		}

		return coords;
	}

	public void attemptAddToArrayList(ArrayList<CartesianCoordinates> coords, CartesianCoordinates newCoords)
	{
		CartesianCoordinates currMin = null;

		// if we don't have a current minimum, add to the arraylist
		// and leave the method
		if (coords.size() == 0 && noCircles != 0)
		{
			coords.add(newCoords);
			return;
		}
		
		// grab the current minimum coordinate pair
		currMin = getMinValFromArrayList(coords);
		
		// if the the new CartesianCoordinates don't have a larger
		// k than the current minimum, return from the method
		if (currMin.getK() >= newCoords.getK())
		{
			return;
		}

		// to add, we must make sure the same "circle" is not already there
		// if there is one, use the one with the larger k value
		for (int i = 0; i < coords.size(); i++)
		{
			CartesianCoordinates compCoords = (CartesianCoordinates)coords.get(i);
			if (Math.abs(compCoords.getX() - newCoords.getX()) < Math.max(newCoords.getR(), compCoords.getR()) &&
				Math.abs(compCoords.getY() - newCoords.getY()) < Math.max(newCoords.getR(), compCoords.getR()))
			{
				if (compCoords.getK() < newCoords.getK())
				{
					coords.remove(compCoords);
					coords.add(newCoords);
					return;
				}
				else
				{
					return;
				}
			}
		}

		if (coords.size() >= noCircles)
		{
			coords.remove(currMin);
			coords.add(newCoords);
		}
		else
		{
			coords.add(newCoords);
		}
	}

	public CartesianCoordinates getMinValFromArrayList(ArrayList<CartesianCoordinates> ar)
	{
		CartesianCoordinates min;

		if (ar.size() == 0)
		{
			return null;
		}

		min = (CartesianCoordinates)ar.get(0);

		for (int i = 1; i < ar.size(); i++)
		{
			CartesianCoordinates val = (CartesianCoordinates)ar.get(i);
			if (val.getK() < min.getK())
			{
				min = val;
			}
		}

		return min;
	}
}
