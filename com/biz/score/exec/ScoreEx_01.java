package com.biz.score.exec;

import java.util.Scanner;

import com.biz.score.service.ScoreService;
import com.biz.score.service.ScoreServiceV1;

public class ScoreEx_01 {

	public static void main(String[] args) {

		Scanner scan = new Scanner(System.in);
		ScoreService scService = new ScoreServiceV1();
		// scService.loadScore();

		while (true) {
			System.out.println("===================================================");
			System.out.println("빛고을 대학 학사관리 시스템 V1");
			System.out.println("====================================================");
			System.out.println("1. 학생성적 등록");
			System.out.println("2. 성적일람표 출력");
			System.out.println("3. scoreList.txt파일");
			System.out.println("-----------------------------------------------------");
			System.out.println("QUIT. 업무종료");
			System.out.println("========================================================");
			System.out.print("업무선택>> ");
			String stMenu = scan.nextLine();
			if (stMenu.equals("QUIT")) {
				break;
			}
			int intMenu = 0;
			try {
				intMenu = Integer.valueOf(stMenu);
			} catch (Exception e) {
				System.out.println("메뉴는 숫자로만 선택할수 있음!!");
				continue;
			}
			if (intMenu == 1) {
				while (true) {
					if (!scService.inputscore()) {
						break;
					}
					;
				}
			} else if (intMenu == 2) {
				scService.calcsum();
				scService.calcavg();
				scService.scoreList();
			} else if (intMenu == 3) {
				scService.saveScore();
			}
		}
		System.out.println("업무종료!!!");
		System.out.println("야 퇴근이다!!!");
	}
}
