package com.biz.score.service;

import com.biz.score.domain.ScoreVO;

public interface ScoreService {
  
	

	
	public void loadScore();	
	public boolean inputscore();
	public void calcsum();
	public void calcavg();
	public void scoreList();
	void saveScore();
	void saveScoreVO(ScoreVO scoreVO);
	
}
