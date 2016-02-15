package bowlingPoints;

public class BowlingCalc {
	
	/**
	 * Calculate bowling point by frames/kicks series
	 * 
	 * @param kicks
	 * @return
	 * @throws RulesException
	 */
	public int calcPoints(int[][] kicks) throws RulesException {
		BowlingFrame[] frames = getFramesArr(kicks);
		return getSumPoints(frames);		
	}
	
	/**
	 * Return point count of one frame 
	 * 
	 * @param frames
	 * @return
	 */
	private int getSumPoints(BowlingFrame[] frames) {
		int sum = 0;
		for (BowlingFrame frame: frames) {
			sum += frame.getPoints();
		}
		return sum;
	}
	
	/**
	 * Convert frames-kick array (int[][]) to BowlingFrame[] array
	 * 
	 * @param kicks
	 * @return
	 * @throws RulesException
	 */
	private BowlingFrame[] getFramesArr(int[][] kicks) throws RulesException {
		BowlingFrame[] frames = new BowlingFrame[kicks.length];

		BowlingFrame prevFrame = null;
		int i = 0;
		for (int[] x: kicks) {
			frames[i] = new BowlingFrame(convertIntArrToIntegerArr(x), i + 1);
			if (prevFrame != null) {
				prevFrame.setNextFrame(frames[i]);
			}
			prevFrame = frames[i];
			i++;
		}
		
		return frames;
	}
	
	/**
	 * Convert int[] to Integer[]
	 * 
	 * @param arr
	 * @return
	 */
	private Integer[] convertIntArrToIntegerArr(int[] arr) {
		Integer[] n = new Integer[arr.length];
		int i = 0;
		for (int value: arr) {
		    n[i] = Integer.valueOf(value);
		    i++;
		}		
		return n;
	}
	
	
}
