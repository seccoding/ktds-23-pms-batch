package com.ktdsuniversity.edu.batch.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ktdsuniversity.edu.batch.dao.LogoutDao;
@Service
public class LogoutService {
	
	@Autowired
	private LogoutDao logoutDao;
		
	/**
	 * 당일 미퇴근 인원의 퇴근시간을 18:20으로 update 한다 
	 * @param empCnt 필요한 update 성공 횟수 
	 * @return empCnt만큼 성공하면 true 반환 , 다르면 false 반환
	 */
	public boolean doAllEmployeeLeaveWork(int empCnt) {
		int count =this.logoutDao.updateEmpLeave();
		
		return empCnt==count;
	}

	/**
	 * 당일 퇴근처리가 안된 직원의 수를 구한다
	 * @return 당일 퇴근처리가 안된 직원의 수
	 */
	public int getCountEmpTodayNonLeave() {
		return this.logoutDao.getCountEmpTodayNonLeave();
	}

}
