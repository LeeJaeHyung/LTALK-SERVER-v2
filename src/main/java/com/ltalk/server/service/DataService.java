package com.ltalk.server.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ltalk.server.entity.Data;
import com.ltalk.server.entity.Friend;
import com.ltalk.server.entity.Member;
import com.ltalk.server.entity.ServerResponse;
import com.ltalk.server.enums.FriendStatus;
import com.ltalk.server.handler.WriteHandler;
import com.ltalk.server.repository.FriendRepository;
import com.ltalk.server.util.LocalDateTimeAdapter;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

import static com.ltalk.server.Main.viewController;
import static com.ltalk.server.controller.ServerController.clients;

@Getter
@Setter
public class DataService {

    private Data data;
    private final AsynchronousSocketChannel socketChannel;
    private MemberService memberService;
    private FriendService  friendService;
    private ChatService chatService;
    public static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public DataService(AsynchronousSocketChannel socketChannel, Data data) throws NoSuchAlgorithmException, IOException {
       this.socketChannel = socketChannel;
       this.data = data;
       interpret();
    }

    public DataService(AsynchronousSocketChannel socketChannel){
        this.socketChannel = socketChannel;
    }

    private void interpret() throws NoSuchAlgorithmException, IOException {
        switch(data.getProtocolType()){
            case LOGIN -> login();
            case CHAT -> chat();
            case SIGNUP -> signup();
            case DISCONNECT -> disconnect();
            case REQUEST_FRIEND -> requestFriend();
            case CREATE_CHATROOM -> creatChatRoom();
        }
    }

    private void creatChatRoom() {
        chatService = new ChatService(socketChannel);
        chatService.creatChatRoom(data.getChatRoomCreatRequest());
    }

    private void requestFriend() {
        memberService = new MemberService(socketChannel);
        friendService = new FriendService();
        String membername = data.getFriendRequest().getMember();
        Member member = memberService.findByUserName(membername);
        String friendname = data.getFriendRequest().getFriend();
        Member friendMember = memberService.findByUserName(friendname);
        LocalDateTime requestTime = data.getFriendRequest().getRequestTime();
        Friend friend1 = new Friend(member, friendMember, FriendStatus.ACCEPTED);
        Friend friend2 = new Friend(friendMember, member, FriendStatus.ACCEPTED);
        member.getFriends().add(friend1);
        friendMember.getFriends().add(friend2);
        memberService.update(member);
        memberService.update(friendMember);
//        friendService.addFriend(friend1, friend2);

    }

    private void disconnect() throws IOException {
        if(data.getDisconnectRequest().getKey()!=null){
            clients.remove(data.getDisconnectRequest().getKey());
        }else{
            System.out.println("key : "+socketChannel.getRemoteAddress());
            clients.remove(socketChannel.getRemoteAddress().toString());
            System.out.println("else들어옴");
        }
        viewController.addText("[클라이언트 종료 : "+socketChannel.getRemoteAddress()+"]");// 수정 필요 로그인 한 사용자의 경우
        socketChannel.close();
        viewController.addText("참여 인원 : "+clients.mappingCount());
    }

    private void login() throws NoSuchAlgorithmException, IOException {
        memberService = new MemberService(socketChannel);
        ServerResponse loginResponse = memberService.login(new Member(data.getLoginRequest()));
        send(loginResponse);
    }
    private void chat() throws NoSuchAlgorithmException, IOException {
        chatService = new ChatService(socketChannel);
        chatService.chat(data.getChatRequest());
    }
    private void signup() throws NoSuchAlgorithmException, IOException {
        memberService = new MemberService(socketChannel);
        ServerResponse signupResponse = memberService.signup(new Member(data.getSignupRequest()));
        send(signupResponse);
    }

    public void send(ServerResponse response) {
        String json = gson.toJson(response);
        byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8);
        int messageLength = jsonBytes.length;

        // 전송할 데이터 크기 출력
        System.out.println("[응답 전송 : " + json + "]");
        System.out.println("[응답 사이즈 : " + messageLength + "]");

        // 길이(4바이트) + JSON 데이터를 포함하는 ByteBuffer 생성
        ByteBuffer responseBuffer = ByteBuffer.allocate(4 + messageLength);
        responseBuffer.putInt(messageLength); // 4바이트 길이 정보 추가
        responseBuffer.put(jsonBytes);        // 본문 데이터 추가
        responseBuffer.flip(); // 버퍼를 읽을 수 있도록 준비

        // 클라이언트에게 응답 전송 (길이 포함된 데이터)
        socketChannel.write(responseBuffer, responseBuffer, new WriteHandler(socketChannel));
    }




}
