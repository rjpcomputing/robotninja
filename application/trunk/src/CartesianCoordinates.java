/******************************************************************************
* Copyright (C) 2008 Jason Pell. All rights reserved.
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

/*****************************************************************************
 This class is used to store information related to a potential circle.
 (x,y) are the coordinates
 k is "likelihood" that it is actually a circle
 r is the radius of the "potential" circle
 
 @author Jason Pell
 *****************************************************************************/
public class CartesianCoordinates {
	private int x;
	private int y;
	private int k; // represents the count for the accumulator
	private int r; // represents the radius
	
	/**************************************************************************
	 * Constructor creates a set of coordinates (x,y) with radius r and 
	 * an accumulator value of k.
	 * @param pX the x value
	 * @param pY the y value
	 * @param pR the radius
	 * @param pK the accumulator count value
	 */
	public CartesianCoordinates(int pX, int pY, int pR, int pK)
	{
		x = pX;
		y = pY;
		k = pK;
		r = pR;
	}
	
	/**************************************************************************
	 * Constructor creates a set of coordinates (0,0) with radius 0 and an
	 * accumulator value k of 0.
	 *************************************************************************/
	public CartesianCoordinates()
	{
		x = 0;
		y = 0;
		k = 0;
		r = 0;
	}
	
	/**************************************************************************
	 * Getter for x.
	 * @return x
	 **************************************************************************/
	public int getX()
	{
		return x;
	}
	
	/**************************************************************************
	 * Getter for y.
	 * @return y
	 **************************************************************************/
	public int getY()
	{
		return y;
	}
	
	/**************************************************************************
	 * Getter for k.
	 * @return k
	 **************************************************************************/
	public int getK()
	{
		return k;
	}
	
	/**************************************************************************
	 * Getter for r.
	 * @return r
	 **************************************************************************/
	public int getR()
	{
		return r;
	}
	
	/**************************************************************************
	 * Setter for x.
	 * @param pX the value to become x
	 **************************************************************************/
	public void setX(int pX)
	{
		x = pX;
	}
	
	/**************************************************************************
	 * Setter for y.
	 * @param pY the value to become y
	 **************************************************************************/
	public void setY(int pY)
	{
		y = pY;
	}
	
	/**************************************************************************
	 * Setter for k.
	 * @param pK the value to become k
	 **************************************************************************/
	public void setK(int pK)
	{
		k = pK;
	}
	
	/**************************************************************************
	 * Setter for r.
	 * @param pR the value to become r
	 **************************************************************************/
	public void setR(int pR)
	{
		r = pR;
	}
}
