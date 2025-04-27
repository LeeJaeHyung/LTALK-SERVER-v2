package com.ltalk.server.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ltalk.server.controller.VoiceServerController;
import com.ltalk.server.dto.ChatRoomDTO;
import com.ltalk.server.dto.FriendDTO;
import com.ltalk.server.entity.*;
import com.ltalk.server.enums.FriendStatus;
import com.ltalk.server.enums.ProtocolType;
import com.ltalk.server.handler.WriteHandler;
import com.ltalk.server.repository.ChatRoomMemberRepository;
import com.ltalk.server.repository.ChatRoomRepository;
import com.ltalk.server.repository.FriendRepository;
import com.ltalk.server.repository.MemberRepository;
import com.ltalk.server.response.*;
import com.ltalk.server.util.LocalDateTimeAdapter;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.ltalk.server.Main.viewController;
import static com.ltalk.server.controller.ServerController.clients;
import static com.ltalk.server.controller.VoiceServerController.*;

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
            case READ_CHAT -> readChat();
            case CONNECT_VOICE_SERVER -> connectVoiceServer();
            case CRATE_VOICE_SERVER -> creatVoiceServer();
            case GET_VOICE_SERVER_IP -> getVoiceServerIP();
            case JOIN_VOICE_CHAT -> joinVoiceChat();
            case RESPONSE_CREATE_CHATROOM_MEMBER -> responseCreateChatRoomMember();
            case FRIEND_SEARCH -> friendSearch();
            case CAN_CREATE_CHAT_ROOM -> canCreateChatRoom();
            case CHATROOM_LIST -> chatRoomList();
        }
    }

    private void chatRoomList() {
        MemberRepository memberRepository = new MemberRepository();
        Member member = memberRepository.findMemberWithChatRooms(data.getChatRoomListRequest().getMemberId());
        //여기 수정 필요
    }

    private void canCreateChatRoom() {
        FriendRepository friendRepository = new FriendRepository();
        List<Friend> friendList = friendRepository.findMyFriendsWithoutPrivateChatRoom(data.getChatRoomCreationCheckRequest().getId());
        List<FriendDTO> friendDTOList = new ArrayList<>();
        for (Friend friend : friendList) {
            friendDTOList.add(new FriendDTO(friend));
        }
        send(new ServerResponse(new ChatRoomCreationCheckResponse(friendDTOList)));
    }

    private void friendSearch() {
        memberService = new MemberService(socketChannel);
        List<Member> friendList = memberService.findByFriend(data.getFriendSearchRequest().getFriend_name());
        send(new ServerResponse(new FriendSearchResponse(friendList)));
    }

    private void responseCreateChatRoomMember() {
        CreateVoiceMemberResponse response = data.getCreateVoiceMemberResponse();
        AsynchronousSocketChannel clientSocketChannel = clients.get(response.getMemberName()).getSocketChannel();
        send(new ServerResponse(response, true), clientSocketChannel);
    }

    private void joinVoiceChat() {
        sendVoiceServer(data);
    }

    private void creatVoiceServer() {
        voiceServerController.creatVoiceServerController();
        System.out.println("creatVoiceServer() 동작");
    }

    private void connectVoiceServer() throws IOException {
        new VoiceServerController(socketChannel);
    }


    private void readChat() {
        System.out.println("readChat()실행");
        chatService = new ChatService(socketChannel);
        chatService.readChat(data.getReadChatRequest());
    }

    private void creatChatRoom() {
        chatService = new ChatService(socketChannel);
        ChatRoom chatRoom = chatService.creatChatRoom(data.getChatRoomCreatRequest());
        if(chatRoom!=null){
            send(new ServerResponse(ProtocolType.CREATE_CHATROOM,new CreateChatRoomResponse(new ChatRoomDTO(chatRoom))));
        }
    }

    private void requestFriend() {
        memberService = new MemberService(socketChannel);
        friendService = new FriendService();
        String memberName = data.getFriendRequest().getMember();
        Member member = memberService.findByUserName(memberName);
        String friendName = data.getFriendRequest().getFriend();
        Member friendMember = memberService.findByUserName(friendName);
        LocalDateTime requestTime = data.getFriendRequest().getRequestTime();
        if(!friendService.checkRequest(memberName, friendName)){
            Friend friend1 = new Friend(member, friendMember, FriendStatus.ACCEPTED);
            Friend friend2 = new Friend(friendMember, member, FriendStatus.PENDING);
            member.getFriends().add(friend1);
            friendMember.getFriends().add(friend2);
            memberService.update(member);
            memberService.update(friendMember);
        }
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

    private void getVoiceServerIP(){
        System.out.println("getVoiceServerIP()");
        if(voiceServerIsRunning){
            System.out.println("voiceServerIP = "+voiceServerIp);
            send(new ServerResponse(new VoiceServerIPResponse(voiceServerIp,data.getJoinVoiceChatRequest().getChatRoomId()),true));
        }else{
            System.out.println("보이스 서버 닫혀있음");
            send(new ServerResponse(new VoiceServerIPResponse(null, null), false));
        }
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

    public void send(ServerResponse response, AsynchronousSocketChannel socketChannel) {
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
