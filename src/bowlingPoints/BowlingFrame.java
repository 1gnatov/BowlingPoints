package bowlingPoints;

public class BowlingFrame {
	private Integer[] bowls;
	private boolean isStrike = false;
	private boolean isSpare = false;
	private BowlingFrame nextFrame;
	private int frameNum;

	public BowlingFrame(Integer[] bowls, int frameNum) throws RulesException {
		this.bowls = bowls;
		
		if (bowls.length >= 1 && bowls[0] == 10) {
			isStrike = true;
		} else if (bowls.length >= 2 && (bowls[0] + bowls[1]) == 10) {
			isSpare = true;
		}
		
		this.frameNum = frameNum;
		
		checkRules();			// в случае ошибки, BowlingFrame будет partially initialized, хоть и кинет RulesException
	}					// наверное, для этой программы не критично, но мне кажется, что в целом это антипаттерн
	
	public void setNextFrame(BowlingFrame nextFrame) {
		this.nextFrame = nextFrame;
	}

	public int getPoints() {
		
		if (isStrike) {
			return getStrikePoints();
		}
		
		if (isSpare) {
			return getSparePoints();
		}
		
		return getSumOfBowls();
	}

	public int getFirstKickPoints() {
		if (bowls.length >= 1) {
			return bowls[0];
		} else {
			return 0;	
		}
	}

	public int getSecondKickPoints() {
		if (bowls.length >= 2) {
			return bowls[1];
		} else if (nextFrame != null) {
			return nextFrame.getFirstKickPoints();
		} else {
			return 0;
		}
	}

	private int getStrikePoints() {
		int sum = getSumOfBowls();
		if (nextFrame != null) {
			sum += nextFrame.getFirstKickPoints() + nextFrame.getSecondKickPoints();
		}
		return sum;
	}

	private int getSparePoints() {
		int sum = getSumOfBowls();
		if (nextFrame != null) {
			sum += nextFrame.getFirstKickPoints();
		} 
		return sum;
	}

	private int getSumOfBowls() {
		int sum = 0;
		for (int i = 0; i < bowls.length; i++) {
			sum += bowls[i];
		}
		return sum;
	}


	
	

	private void checkRules() throws RulesException {
		checkRulesFrameNum();
		checkRulesKickCount();
		checkRulesBowlCount();
	}
	
	private void checkRulesFrameNum() throws RulesException {
		if (frameNum > 10) {
			throw new RulesException("Count of frames cannot be more than 10");
		}
	}
	 
	private void checkRulesKickCount() throws RulesException {
		if (bowls.length > 2 && frameNum != 10) {
			throw new RulesException("Count of kick in one frame cannot be more than 2");
		}
		
		if (bowls.length > 2 && frameNum == 10 && !isSpare && !isStrike) {
			throw new RulesException("Count of kick in one frame cannot be more than 2");
		}

		if (bowls.length > 3 && frameNum == 10) {
			throw new RulesException("Count of kick in 10-th frame cannot be more than 3");
		}
	}
	
	private void checkRulesBowlCount() throws RulesException {
		int sum = 0; 
		for(int i = 0; i < bowls.length; i++) {
			if (bowls[i] > 10) {
				throw new RulesException("Count of bowls in one kick cannot be more than 10 (frame: " + frameNum + ", kick: " + (i + 1) + ")");
			}
			if (bowls[i] < 0) {
				throw new RulesException("Count of bowls cannot be less than 0 (frame: " + frameNum + ", kick: " + (i + 1) + ")");
			}
			sum += bowls[i];
		}
		
		if (sum > 10 && frameNum != 10)  {
			throw new RulesException("Bowls in one frame cannot be more than 10");
		}
		if (sum > 30 && frameNum == 10) {
			throw new RulesException("Bowls in 10-th frame cannot be more than 30");
		}
	}
}
