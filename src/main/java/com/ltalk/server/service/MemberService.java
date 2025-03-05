package com.ltalk.server.service;

import com.ltalk.server.dto.ChatRoomDTO;
import com.ltalk.server.dto.FriendDTO;
import com.ltalk.server.entity.*;
import com.ltalk.server.enums.ProtocolType;
import com.ltalk.server.enums.UserRole;
import com.ltalk.server.repository.MemberRepository;
import com.ltalk.server.response.LoginResponse;
import com.ltalk.server.response.SignupResponse;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.ltalk.server.controller.ServerController.clients;

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
                Set<Friend> friendSet = targetMember.getFriends();
                List<FriendDTO> friendList = new ArrayList<>() ;
                for(Friend friend : friendSet){
                    System.out.println("친구 있음");
                    System.out.println(friend.getFriend().getUsername());
                    friendList.add(new FriendDTO(friend));
                }
                Set<ChatRoomMember> chatRooms = targetMember.getChatRooms();
                List<ChatRoomDTO> chatList = new ArrayList<>();
                for(ChatRoomMember chatRoomMember : chatRooms){
                    System.out.println("채팅방 존재");
                    System.out.println(chatRoomMember.getChatRoomMemberId());
                    ChatRoom chatRoom = chatRoomMember.getChatRoom();
                    System.out.println(chatRoom.getChatRoomId());
                    System.out.println(chatRoom.getName());
                    chatList.add(new ChatRoomDTO(chatRoom));
                }


                Client client = clients.get(socketChannel.getRemoteAddress().toString());
                client.setMember(targetMember);//멤버셋팅
                client.setUserRole(UserRole.USER);//유저 권한 변경
                clients.remove(socketChannel.getRemoteAddress().toString());//클라이언트 제거
                clients.put(targetMember.getUsername(), client);//변경된 클라이언트 추가
                System.out.println("로그인 성공 전송");
                return new ServerResponse(ProtocolType.LOGIN, true, new LoginResponse(member, friendList, chatList, "로그인 성공"));
            }else{
                System.out.println("비밀 번호 불일치");
                return new ServerResponse(ProtocolType.LOGIN, false, new LoginResponse("비밀번호를 확인해 주세요"));
            }
        }else{
            System.out.println("해당 멤버 없음");
            return new ServerResponse(ProtocolType.LOGIN, false, new LoginResponse("아이디를 확인해 주세요"));
        }
    }

    public Member findByUserName(String username){
        return memberRepository.findByUserName(username);
    }

    public void save(Member member){
        memberRepository.save(member);
    }

    public void update(Member member) {
        memberRepository.update(member);
    }
}
