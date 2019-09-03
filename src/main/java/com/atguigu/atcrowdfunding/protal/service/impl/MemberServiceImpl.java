package com.atguigu.atcrowdfunding.protal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atcrowdfunding.bean.Cert;
import com.atcrowdfunding.bean.Datas;
import com.atcrowdfunding.bean.Member;
import com.atcrowdfunding.bean.Ticket;
import com.atguigu.atcrowdfunding.portal.dao.MemberDao;
import com.atguigu.atcrowdfunding.portal.service.MemberService;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberDao memberDao;

	public Member queryLoginMember(Member member) {
		return memberDao.queryLoginMember(member);
	}

	public void updateAccttype(Member member) {
		memberDao.updateAccttype(member);
	}

	public void insertTicket(Ticket ticket) {
		memberDao.insertTicket(ticket);
	}

	public Ticket queryTicket(Member loginMember) {
		return memberDao.queryTicket(loginMember);
	}

	public void updateTicketProcessStep(Ticket ticket) {
		memberDao.updateTicketProcessStep(ticket);
	}

	public void updateBasicInfo(Member loginMember) {
		memberDao.updateBasicInfo(loginMember);
	}

	public List<Cert> queryCertsByAccttype(String accttype) {
		return memberDao.queryCertsByAccttype(accttype);
	}

	public void insertMemberCerts(Datas ds) {
		memberDao.insertMemberCerts(ds);
	}

	public void updateEmail(Member loginMember) {
		memberDao.updateEmail(loginMember);
	}

	public void updateMemberAuthStatus(Member loginMember) {
		memberDao.updateMemberAuthStatus(loginMember);
	}

	public Member getAuthcode(Integer id) {
		return memberDao.getAuthcode(id);
	}
}
