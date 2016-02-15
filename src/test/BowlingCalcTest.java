package test;
import static org.junit.Assert.assertTrue;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import bowlingPoints.BowlingCalc;
import bowlingPoints.BowlingFrame;
import bowlingPoints.RulesException;

@RunWith(Theories.class)
public class BowlingCalcTest {

	@DataPoints
	public static int[][][] data = new int[][][]{
		{ {1}, 	{0},	}, // empty
		{ {1}, 	{0},	{}}, // empty 2
		{ {2}, 	{0}, 	{0, 0},	{0, 0},	{0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}	}, //zero
		{ {3}, 	{72},	{0, 3}, {4, 3}, {4, 3}, {4, 3}, {4, 3}, {4, 3}, {4, 3}, {4, 3}, {4, 3}, {4, 6, 3}	}, //typical + last spare
		{ {4}, 	{165},	{10}, {5, 4},	{5, 5}, {10}, {3,6}, {4,6}, {10}, {10}, {3, 3}, {10, 10, 4}	},	//typical + spare + strike + last strike
		{ {5}, 	{300},	{10}, {10}, {10}, {10}, {10}, {10}, {10}, {10}, {10}, {10, 10, 10}	}, //typical maximum
		{ {6}, 	{4},	{4}}, //begin of the game
		{ {7}, 	{47},	{4, 5}, {10}, {3,6}, {5, 5}}, //middle of the game
		
		//European Women Championships 2006 
		{ {8}, 	{214},	{9, 1}, {9, 1}, {10}, {6, 2}, {10}, {9, 1}, {10}, {10}, {10}, {10, 9, 1} }, // Catherine Durieux, Doppel, Lane 18, Game 11
		{ {9}, 	{207},	{9, 1}, {10}, {8, 2}, {10}, {10}, {9, 1}, {10}, {9, 0}, {9, 1}, {10, 10, 10} }, //Kirstene Horemans, Trio, Lane 8, Game 5
	};
	
	@Theory
	public void testBowlingCalc(Object... testData) throws RulesException
	{
		int count = testData.length - 2;
		int[][] ii = new int[count][];
		
		for(int i = 2; i < testData.length; i++) {
			ii[i-2] = (int[])testData[i]; 
		}
		
		int testNum = ((int[])testData[0])[0];
		int points = ((int[])testData[1])[0];
		
		BowlingCalc calc = new BowlingCalc();
		assertTrue("Error on test " + testNum, points == calc.calcPoints(ii));
	}

}
