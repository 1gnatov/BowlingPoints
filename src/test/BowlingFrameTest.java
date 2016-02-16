package test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import bowlingPoints.BowlingFrame;
import bowlingPoints.RulesException;


@RunWith(Theories.class)
public class BowlingFrameTest {

	@DataPoints({"firstkicks"})
	public static Object[] getFirstKickPointData = new Object[][]{			
		{new Integer[] {5, 4}, 	1, 		5,		"open frame"},   // что за пробелы? что такое 1? номер датапоинта?? 
		{new Integer[] {10}, 	2, 		10,		"strike frame"},
		{new Integer[] {}, 		3, 		0,		"empty frame"}, 
		{new Integer[] {4, 6, 3}, 10,	4,		"last frame"},		// номера датапоинтов поехали и получается вырвиглаз
		{new Integer[] {0, 0}, 	5, 		0,		"zero frame"},
		{new Integer[] {0, 10}, 6,	 	0,		"spare frame"},
	};
	
	@Theory
	public void testGetFirstKickPoint(@FromDataPoints("firstkicks") Object... testData) throws RulesException
	{
		BowlingFrame frame;
		frame = new BowlingFrame((Integer[])testData[0], (int)testData[1]);
		assertTrue((String)testData[3], (int)testData[2] == frame.getFirstKickPoints()); //за testData[i] прячется логика, чтобы понять ее надо лезть 
												//в структуру datapoints и запоминать расположение аргументов
												//Как документацию к коду такие тесты использовать нельзя))
	}
	
	@DataPoints({"secondkicks1"})
	public static Object[] getSecondKickPointData1 = new Object[][]{
		{new Integer[] {5, 4}, 	1,		4,		"open frame"},
		{new Integer[] {4, 6, 3}, 10,	6,		"last frame"},
		{new Integer[] {0, 0}, 	3, 		0,		"zero frame"},
		{new Integer[] {0, 10}, 4, 		10,		"spare frame"},
		{new Integer[] {}, 		5, 		0,		"empty"},
	};
	
	@Theory
	public void testGetSecondKickPoint(@FromDataPoints("secondkicks1") Object... testData) throws RulesException
	{
		BowlingFrame frame = new BowlingFrame((Integer[])testData[0], (int)testData[1]);
		assertTrue((String)testData[3], (int)testData[2] == frame.getSecondKickPoints());
	}
	
	@DataPoints({"secondkicks2"})
	public static Object[] getSecondKickPointData2 = new Object[][]{
		{new Integer[] {}, 		1, 		4,		"empty frame"}, // что такое 4?
		{new Integer[] {10}, 	10,		6,		"strike frame"},	// что такое 6?
	};
	
	@Theory
	public void testGetSecondKickPointNextFrame(@FromDataPoints("secondkicks2") Object... testData) throws RulesException
	{
		int points = (int)testData[2];
		BowlingFrame nextFrame = mock(BowlingFrame.class);
		when(nextFrame.getFirstKickPoints()).thenReturn(points);
		BowlingFrame frame = new BowlingFrame((Integer[])testData[0], (int)testData[1]);
		frame.setNextFrame(nextFrame);
		assertTrue((String)testData[3], points == frame.getSecondKickPoints());
	}
	
	@DataPoints({"getpoints1"})
	public static Object[] getPointsData1 = new Object[][]{
		{new Integer[] {4, 3}, 	1, 		1,  2,	7,		"open frame"},
		{new Integer[] {0, 0}, 	2,		0,  2,	0,		"zero frame"},
		{new Integer[] {0, 10},	3, 		10, 2,	20,		"spare frame"},
		{new Integer[] {4, 6}, 	4,		2,  7,	12,		"spare frame 2"},
		{new Integer[] {10}, 	5,		3,  4,	17,		"strike frame"},
		{new Integer[] {10}, 	6,		10, 4,	24,		"double"},
		{new Integer[] {10}, 	7,		10, 10,	30,		"turkey"},
		{new Integer[] {}, 		8,		10, 10,	0,		"empty"},
	};
	
	@Theory
	public void testGetPointsNotLast(@FromDataPoints("getpoints1") Object... testData) throws RulesException
	{
		BowlingFrame nextFrame = mock(BowlingFrame.class);
	
		when(nextFrame.getFirstKickPoints()).thenReturn((int)testData[2]);
		when(nextFrame.getSecondKickPoints()).thenReturn((int)testData[3]);

		BowlingFrame frame = new BowlingFrame((Integer[])testData[0], (int)testData[1]);
		frame.setNextFrame(nextFrame);

		assertTrue((String)testData[5], (int)testData[4] == frame.getPoints());
	}

	
	@DataPoints({"getpoints2"})
	public static Object[] getPointsData2 = new Object[][]{
		{new Integer[] {4, 3}, 		1, 		7,		"open frame"},
		{new Integer[] {0, 0}, 		2, 		0,		"zero frame"},
		{new Integer[] {0, 10,3}, 	10,		13,		"spare frame"},
		{new Integer[] {10,5, 4}, 	10, 	19,		"strike frame"},
		{new Integer[] {10,10,4}, 	10, 	24,		"double"},
		{new Integer[] {10,10,10}, 	10, 	30,		"turkey"},
		{new Integer[] {}, 			5,		0,		"empty"},
	};
	
	@Theory
	public void testGetPointsLast(@FromDataPoints("getpoints2") Object... testData) throws RulesException
	{
		BowlingFrame frame = new BowlingFrame((Integer[])testData[0], (int)testData[1]);
		assertTrue((String)testData[3], (int)testData[2] == frame.getPoints());
	}
	
	@DataPoints({"exceptions"})
	public static Object[] getExceptionData = new Object[][]{
		{new Integer[] {9, 2}, 		1}, 	//big sum points
		{new Integer[] {8, 1, 1}, 	1}, 	//3 kicks in frame
		{new Integer[] {8, 2, 0, 0},10}, 	//4 kicks in 10-th frame
		{new Integer[] {-4, 2}, 	1}, 	//negative bowls during kick
		{new Integer[] {8, 0, 2}, 	10}, 	//3 kicks in 10-th frame without spare or strike
		{new Integer[] {8, 0}, 		11}, 	//more than 10 frames
		{new Integer[] {11, 0}, 	10}, 	//more than 10 bowls in one kick
	};
	
	@Test(expected=RulesException.class)
	@Theory
	public void testBowlingFrameExceptions(@FromDataPoints("exceptions") Object... testData) throws RulesException {
		BowlingFrame frame = new BowlingFrame((Integer[])testData[0], (int)testData[1]);
	}

	
}
