package com.ltalk.server.service;

import com.ltalk.server.entity.Friend;
import com.ltalk.server.entity.Member;
import com.ltalk.server.entity.ServerResponse;
import com.ltalk.server.entity.User;
import com.ltalk.server.enums.ProtocolType;
import com.ltalk.server.repository.MemberRepository;
import com.ltalk.server.response.LoginResponse;
import com.ltalk.server.response.SignupResponse;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

public class MemberService {
    MemberRepository memberRepository;
    AsynchronousSocketChannel socketChannel;
    public MemberService(AsynchronousSocketChannel socketChannel) {
        memberRepository = new MemberRepository();
        this.socketChannel = socketChannel;
    }

    public ServerResponse signup(Member member) throws NoSuchAlgorithmException, IOException {
        ServerResponse serverResponse = null;
        serverResponse = duplication(member);
        if(serverResponse == null){
            if(memberRepository.save(member)){
                return new ServerResponse(ProtocolType.SIGNUP,true, new SignupResponse("회원가입 성공"));
            }else{
                return new ServerResponse(ProtocolType.SIGNUP, false, new SignupResponse("회원가입 실패"));
            }
        }
        return serverResponse;
    }

    private ServerResponse duplication(Member member) throws IOException {
        boolean nameCheck = memberRepository.usernameExists(member.getUsername());
        System.out.println("아이디 존재? "+nameCheck);
        boolean emailCheck = false;
        if(nameCheck==false){
            emailCheck = memberRepository.emailExists(member.getEmail());
            System.out.println("이메일 존재? "+emailCheck);
            if(emailCheck){
                return new ServerResponse(ProtocolType.SIGNUP, false, new SignupResponse("중복된 이메일이 이미 존재합니다."));
            }
        }else{
            return new ServerResponse(ProtocolType.SIGNUP, false, new SignupResponse("중복된 아이디가 이미 존재합니다."));
        }
        return null;
    }


    public ServerResponse login(Member member) throws IOException {
        System.out.println("로그인 요청 IP: "+socketChannel.getRemoteAddress().toString());
        if(memberRepository.usernameExists(member.getUsername())){
            System.out.println("멤버 존재");
            Member targetMember = memberRepository.findByUserName(member.getUsername());
            System.out.println("타겟 멤버 가져옴");
            if(targetMember.getPassword().equals(member.getPassword())){
                System.out.println("비밀번호 일치");
                Set<Friend> freindSet = targetMember.getFriends();
                for(Friend friend : freindSet){
                    System.out.println(friend.getFriend().getUsername());
                }
                System.out.println("로그인 성공 전송");
                return new ServerResponse(ProtocolType.LOGIN, true, new LoginResponse(member, "로그인 성공"));
            }else{
                System.out.println("비밀 번호 불일치");
                return new ServerResponse(ProtocolType.LOGIN, false, new LoginResponse("비밀번호를 확인해 주세요"));
            }
        }else{
            System.out.println("해당 멤버 없음");
            return new ServerResponse(ProtocolType.LOGIN, false, new LoginResponse("아이디를 확인해 주세요"));
        }
    }

}
