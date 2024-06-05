package com.ktdsuniversity.edu.batch.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktdsuniversity.edu.batch.beans.Beans;
import com.ktdsuniversity.edu.batch.service.LogoutService;

public class LogoutJob implements Job{
	
	public static final String JOB_NAME = "logoutJob";
	public static final String SCHEDULE = "00 20 18 * * ?"; // "초 분 시 일 월?"

	private static final Logger logger = LoggerFactory.getLogger(LogoutJob.class);

	private LogoutService logoutService;
	
	public LogoutJob() {
		logoutService= Beans.getInstance().getBean("logoutService");
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("LogoutJob Start");
//		당일 출근한 직원중 퇴근처리가 안된 리스트를 가져옴 
//		해당 리스트의 직원들을 일괄 퇴근처리 시킴 
//		그러고도 퇴근처리가 안된 직원이 있으면 다시 실행
		
		int empCnt =0;
		while(true){
			empCnt=logoutService.getCountEmpTodayNonLeave();//실행해야 하는 횟수
			if(logoutService.doAllEmployeeLeaveWork(empCnt)) {
				break;
		}	}
		
		logger.info("LogoutJob End");
	}
}
