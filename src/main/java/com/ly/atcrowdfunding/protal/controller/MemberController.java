package com.ly.atcrowdfunding.protal.controller;

import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.ibatis.mapping.ParameterMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.atcrowdfunding.bean.Cert;
import com.atcrowdfunding.bean.Datas;
import com.atcrowdfunding.bean.Member;
import com.atcrowdfunding.bean.MemberCert;
import com.atcrowdfunding.bean.Ticket;
import com.atguigu.atcrowdfunding.common.BaseController;
import com.atguigu.atcrowdfunding.common.Const;
import com.atguigu.atcrowdfunding.portal.service.MemberService;
import com.atguigu.atcrowdfunding.util.StringUtil;

@Controller
@RequestMapping("/member")
public class MemberController extends BaseController{

	@Autowired
	private MemberService memberService;
	
	/**
	 * 完成实名认证
	 * @param authcode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/finishApply")
	public Object finishApply(String authcode,HttpSession session) {
		start();
		
		try {
			Member loginMember = (Member)session.getAttribute(Const.LOGIN_MEMBER);
			//更新邮箱地址
			 memberService.getAuthcode(loginMember.getId());
			System.out.println("用户的验证码是："+memberService.getAuthcode(loginMember.getId()));
			if ( authcode.equals("2333")) {
			loginMember.setAuthstatus("1");
			memberService.updateMemberAuthStatus(loginMember);
			success();
			
			}else {
				error("验证码错误，请重新输入");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		return end();
	}
	
	@ResponseBody
	@RequestMapping("/startProcess")
	public Object startProcess(String email,HttpSession session) {
		start();
		
		try {
			//获取登录会员信息
			Member loginMember = (Member)session.getAttribute(Const.LOGIN_MEMBER);
			
			//更新邮箱地址
			loginMember.setEmail(email);
			memberService.updateEmail(loginMember);
			
			//更新步骤
			Ticket ticket = memberService.queryTicket(loginMember);
			ticket.setPstep("checkemail");
			memberService.updateTicketProcessStep(ticket);
			success();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		return end();
	}
	
	
	@ResponseBody
	@RequestMapping("/uploadFile")
	public Object uploadFile( HttpSession session, Datas ds ) {
		start();
		
		try {
			Member loginMember = (Member)session.getAttribute(Const.LOGIN_MEMBER);
			ServletContext application = session.getServletContext();
			
			//处理上传的文件
			for (MemberCert mc : ds.getMcs()) {
				MultipartFile mF = mc.getCertImg();
				mc.setMemberid(loginMember.getId());
				String name = mF.getOriginalFilename();
				
				String filename = UUID.randomUUID().toString() + name.substring(name.lastIndexOf("."));
				File file = new File(application.getRealPath("pics")+"/member/");
				if ( !file.exists() ) {
					file.mkdirs();
				}
				mF.transferTo(new File(file, filename));
				mc.setIconpath(filename);
			}
			memberService.insertMemberCerts(ds);
			
			Ticket ticket = memberService.queryTicket(loginMember);
			ticket.setPstep("uploadfile");
			memberService.updateTicketProcessStep(ticket);
			success();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
		return end();
	}

	@RequestMapping("/apply")
	public String apply( HttpSession session,Model model) {
		
		Member loginMember = (Member)session.getAttribute(Const.LOGIN_MEMBER);
		//获取流程审批单
		Ticket ticket = memberService.queryTicket(loginMember);
		if (ticket == null) {
			ticket = new Ticket();
			ticket.setMemberid(loginMember.getId());
			ticket.setStatus("0");
			memberService.insertTicket(ticket);
		}else {
			if (StringUtil.isEmpty(ticket.getPstep())) {
				return "member/apply";
			}else if ("accttype".equals(ticket.getPstep())) {
				return "member/basicinfo";
			}else if ("basicinfo".equals(ticket.getPstep())) {
				
				//获取上传证明文件的数据
				List<Cert> certs = memberService.queryCertsByAccttype(loginMember.getAccttype());
				model.addAttribute("certs",certs);
				return "member/uploadfile";
			}else if ( "uploadfile".equals(ticket.getPstep() ) ) {
				return "member/checkemail";
			}else if("checkemail".equals(ticket.getPstep() ) ) {
				return "member/checkcode";
			}
		}
		return "member/apply";
	}
	
//	@RequestMapping("/basicinfo")
//	public String basicinfo() {
//		return "member/basicinfo";
//	}
	
	@ResponseBody
	@RequestMapping("/updateBasicInfo")
	public Object updateBasicInfo(HttpSession session, Member member ) {
		start();
		
		try {
			
			//更新会员的基本信息
			Member loginMember  = (Member)session.getAttribute(Const.LOGIN_MEMBER);
			loginMember.setRealname(member.getRealname());
			loginMember.setCardnum(member.getCardnum());
			loginMember.setTel(member.getTel());
			memberService.updateBasicInfo(loginMember);
			//更新流程审批单的步骤
			Ticket ticket = memberService.queryTicket(loginMember);
			ticket.setPstep("basicinfo");
			memberService.updateTicketProcessStep(ticket);
			
			success();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		return end();
		
	}
	 
	@ResponseBody
	@RequestMapping("/updateAccttype")
	public Object updateAccttype( HttpSession session, Member member) {
		start();
		
		try {
			
			Member loginMember = (Member)session.getAttribute(Const.LOGIN_MEMBER);
			loginMember.setAccttype(member.getAccttype());
			memberService.updateAccttype(loginMember);
			
			Ticket ticket = memberService.queryTicket(loginMember);
			ticket.setPstep("accttype");
			memberService.updateTicketProcessStep(ticket);
			
			
 			success();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		return end();
	}
}