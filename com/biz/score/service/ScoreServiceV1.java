package com.biz.score.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.biz.score.domain.ScoreVO;

public class ScoreServiceV1 implements ScoreService {

	private List<ScoreVO> scoreList;
	private Scanner scan;
	private String fileName;

	private String[] strSubjects;
	private Integer[] intScores;
	private int[] totalSum;
	private int[] totalAvg;

	public ScoreServiceV1() {
		scoreList = new ArrayList<ScoreVO>();
		scan = new Scanner(System.in);
		fileName = "src/com/biz/score/date/score.txt";

		strSubjects = new String[] { "국어", "영어", "수학", "음악" };
		intScores = new Integer[strSubjects.length];
		totalSum = new int[strSubjects.length];
		totalAvg = new int[strSubjects.length];

	}

	@Override
	public void loadScore() {
		FileReader fileReader = null;
		BufferedReader buffer = null;

		try {
			fileReader = new FileReader(this.fileName);
			buffer = new BufferedReader(fileReader);
			String reader = "";
			while (true) {
				reader = buffer.readLine();
				if (reader == null) {
					break;
				}
				String[] scores = reader.split(":");
				ScoreVO scoreVO = new ScoreVO();
				scoreVO.setNum(scores[0]);
				scoreVO.setKor(Integer.valueOf(scores[1]));
				scoreVO.setEng(Integer.valueOf(scores[2]));
				scoreVO.setMath(Integer.valueOf(scores[3]));
				scoreVO.setMusic(Integer.valueOf(scores[4]));
				scoreList.add(scoreVO);
			}

		} catch (Exception e) {
			System.out.println("파일을 읽을 수 없습니다.");
		}

	}

	private Integer scoreCheck(String sc_score) {

		if (sc_score.equals("END")) {
			return -1;
		}

		Integer intScore = null;
		try {
			intScore = Integer.valueOf(sc_score);
		} catch (Exception e) {
			System.out.println("점수는 숫자만 가능합니다.");
			System.out.println("입력한 문자열 : " + sc_score);
			return null;
		}

		if (intScore < 0 || intScore > 100) {
			System.out.println("점수는 0 ~ 100까지만 가능합니다. 다시 입력해주세요");
			return null;
		}

		return intScore;
	}

	@Override
	public boolean inputscore() {
		ScoreVO scoreVO = new ScoreVO();

		System.out.print("학번 (END : 끝) >> ");
		String strNum = scan.nextLine();

		if (strNum.equals("END")) {
			return false;
		}

		int intNum = 0;

		try {
			intNum = Integer.valueOf(strNum);
		} catch (Exception e) {
			System.out.println("학번은 숫자만 가능합니다.");
			System.out.println("입력한 문자열 : " + strNum);
			return true;
		}

		if (intNum < 1 || intNum > 99999) {
			System.out.println("학번은 1 ~ 99999 까지 가능합니다. 다시 입력해주세요");
			return true;
		}

		strNum = String.format("%05d", intNum);

		for (ScoreVO sVO : scoreList) {
			if (sVO.getNum().equals(strNum)) {
				System.out.println(strNum + "학생의 성적이 이미 등록되어있습니다.");
				return true;
			}
		}

		scoreVO.setNum(strNum);

		for (int i = 0; i < strSubjects.length; i++) {
			System.out.printf("%s 점수(END:끝) >> ", strSubjects[i]);
			String score = scan.nextLine();

			Integer intScore = this.scoreCheck(score);

			if (intScore == null) {
				i--;
				continue;
			} else if (intScore < 0) {
				return false;
			}
			intScores[i] = intScore;
		}

		scoreVO.setKor(intScores[0]);
		scoreVO.setEng(intScores[1]);
		scoreVO.setMath(intScores[2]);
		scoreVO.setMusic(intScores[3]);

		scoreList.add(scoreVO);
		this.saveScoreVO(scoreVO);

		return true;
	}

	@Override
	public void calcsum() {
		for (ScoreVO scoreVO : scoreList) {
			int sum = scoreVO.getKor();
			sum += scoreVO.getEng();
			sum += scoreVO.getMath();
			sum += scoreVO.getMusic();

			scoreVO.setSum(sum);
		}

	}

	@Override
	public void calcavg() {
		for (ScoreVO scoreVO : scoreList) {
			int sum = scoreVO.getSum();
			float avg = (float) sum / 4;
			scoreVO.setAvg(avg);
		}
	}

	@Override
	public void scoreList() {

		Arrays.fill(totalSum, 0);
		Arrays.fill(totalAvg, 0);

		System.out.println("성적 일람표");
		System.out.println("========================================================");
		System.out.println("학번\t|국어\t|영어\t|수학\t|음악\t|총점\t|평균\t|");
		System.out.println("--------------------------------------------------------");

		for (ScoreVO sVO : scoreList) {

			System.out.printf("%s\t|", sVO.getNum());
			System.out.printf("%d\t|", sVO.getKor());
			System.out.printf("%d\t|", sVO.getEng());
			System.out.printf("%d\t|", sVO.getMath());
			System.out.printf("%d\t|", sVO.getMusic());
			System.out.printf("%d\t|", sVO.getSum());
			System.out.printf("%5.2f\t|\n", sVO.getAvg());

			totalSum[0] += sVO.getKor();
			totalSum[1] += sVO.getEng();
			totalSum[2] += sVO.getMath();
			totalSum[3] += sVO.getMusic();
		}
		System.out.println("--------------------------------------------------------");
		System.out.print("과목총점|");
		int sumAndsum = 0;
		for (int sum : totalSum) {
			System.out.printf("%s\t|", sum);
			sumAndsum += sum;
		}
		System.out.printf("%s\t|\n", sumAndsum);

		System.out.print("과목평균|");
		float avgAndAvg = 0f;
		for (int sum : totalSum) {
			float avg = (float) sum / totalSum.length;
			System.out.printf("%5.2f\t|", avg);
			avgAndAvg += avg;
		}
		System.out.printf("\t|%5.2f\t|\n", (float) avgAndAvg / scoreList.size());
		System.out.println("========================================================");
	}

	public void saveScore() {
		PrintStream Stream = null;
		String savefile = "src/com/biz/score/date/scoreList.txt";

		try {

			Stream = new PrintStream(savefile);

			Arrays.fill(totalSum, 0);
			Arrays.fill(totalAvg, 0);

			Stream.println("성적 일람표");
			Stream.println("========================================================");
			Stream.printf("학번\t|국어\t|영어\t|수학\t|음악\t|총점\t|평균\t|\n");
			Stream.println("--------------------------------------------------------");

			for (ScoreVO sVO : scoreList) {

				Stream.printf("%s\t\t|", sVO.getNum());
				Stream.printf("%d\t\t|", sVO.getKor());
				Stream.printf("%d\t\t|", sVO.getEng());
				Stream.printf("%d\t\t|", sVO.getMath());
				Stream.printf("%d\t\t|", sVO.getMusic());
				Stream.printf("%d\t\t|", sVO.getSum());
				Stream.printf("%5.2f\t\t|\n", sVO.getAvg());

				totalSum[0] += sVO.getKor();
				totalSum[1] += sVO.getEng();
				totalSum[2] += sVO.getMath();
				totalSum[3] += sVO.getMusic();
			}
			Stream.println("--------------------------------------------------------");
			Stream.print("과목총점|");
			int sumAndSum = 0;
			for (int sum : totalSum) {
				Stream.printf("%s\t|", sum);
				sumAndSum += sum;
			}
			Stream.printf("%s\t|\n", sumAndSum);

			Stream.print("과목평균|");
			float avgAndAvg = 0f;
			for (int sum : totalSum) {
				float avg = (float) sum / totalSum.length;
				Stream.printf("%5.2f\t|", avg);
				avgAndAvg += avg;
			}
			Stream.printf("\t|%5.2f\t\t|\n", (float) avgAndAvg / scoreList.size());
			Stream.println("========================================================");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void saveScoreVO(ScoreVO scoreVO) {

		FileWriter fileWriter = null;
		PrintWriter pWriter = null;

		try {
			fileWriter = new FileWriter(this.fileName, true);
			pWriter = new PrintWriter(fileWriter);

			pWriter.printf("%s:", scoreVO.getNum());
			pWriter.printf("%d:", scoreVO.getKor());
			pWriter.printf("%d:", scoreVO.getEng());
			pWriter.printf("%d:", scoreVO.getMath());
			pWriter.printf("%d\n", scoreVO.getMusic());
			pWriter.flush();
			pWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
