package com.ktdsuniversity.edu.batch.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LogoutDao extends SqlSessionDaoSupport {
	
	
	@Autowired
	@Override
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}
	
	/**
	 * 당일 퇴근처리가 안된 직원의 수를 구한다
	 * @return 당일 퇴근처리가 안된 직원의 수
	 */
	public int getCountEmpTodayNonLeave() {
		return this.getSqlSession().selectOne("getCountEmpTodayNonLeave");
	}
	/**
	 * 당일 퇴근처리가 안된 직원의 EmpId 를 구한다
	 * @return  EmpId List
	 */
	public List<String> getEmpIdEmpTodayNonLeave() {
		return this.getSqlSession().selectList("getEmpIdEmpTodayNonLeave");
	}
	
	/**
	 * 당일 미퇴근인원의 퇴근시간을 18:20으로 update 한다
	 * @return update 성공횟수
	 */
	public int updateEmpLeave() {
		
		return this.getSqlSession().update("updateEmpLeave");
	}

	

	
}
