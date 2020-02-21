package com.old.websocket.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class EchoHandler extends TextWebSocketHandler {

	private List<WebSocketSession> sessions = new ArrayList<WebSocketSession>();
	
	synchronized void addSession(WebSocketSession sess) {
		this.sessions.add(sess);
	}
	
	synchronized void removeSession(WebSocketSession sess) {
		this.sessions.remove(sess);
	}
	
	public void showSessions() {
		System.out.println("============================");
		System.out.println("현재 접속 세션" + sessions);
		System.out.println("============================");
	}
	
	// 웹소켓 서버측에 텍스트 메시지가 접수되면 호출되는 메소드
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
		
		for (WebSocketSession sess : sessions) {
			System.out.println("메세지 송신");
			TextMessage echoMessage = new TextMessage(session.getId() + " : " + message.getPayload());
			sess.sendMessage(echoMessage);
		}
		System.out.println("============================");
	}
	
    // 웹소켓 서버에 클라이언트가 접속하면 호출되는 메소드
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		addSession(session);
		System.out.println("세션 추가 : " + session.getId());
		System.out.println("세션 상세 정보 : " + session);
		
        super.afterConnectionEstablished(session);
        System.out.println("클라이언트 접속됨");

        showSessions();
        
        System.out.println("============================");
    }
 
    // 클라이언트가 접속을 종료하면 호출되는 메소드
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    	removeSession(session);
    	System.out.println("세션 제거 : " + session.getId());
    	
        super.afterConnectionClosed(session, status);
        System.out.println("클라이언트 접속해제");
        
        showSessions();
        
        System.out.println("============================");
    }
 
    // 메시지 전송시나 접속해제시 오류가 발생할 때 호출되는 메소드
    @Override
    public void handleTransportError(WebSocketSession session,
            Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
        System.out.println("전송오류 발생");
        System.out.println("============================");
    }

}
