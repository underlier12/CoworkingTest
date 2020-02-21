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
		System.out.println("���� ���� ����" + sessions);
		System.out.println("============================");
	}
	
	// ������ �������� �ؽ�Ʈ �޽����� �����Ǹ� ȣ��Ǵ� �޼ҵ�
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
		
		for (WebSocketSession sess : sessions) {
			System.out.println("�޼��� �۽�");
			TextMessage echoMessage = new TextMessage(session.getId() + " : " + message.getPayload());
			sess.sendMessage(echoMessage);
		}
		System.out.println("============================");
	}
	
    // ������ ������ Ŭ���̾�Ʈ�� �����ϸ� ȣ��Ǵ� �޼ҵ�
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		addSession(session);
		System.out.println("���� �߰� : " + session.getId());
		System.out.println("���� �� ���� : " + session);
		
        super.afterConnectionEstablished(session);
        System.out.println("Ŭ���̾�Ʈ ���ӵ�");

        showSessions();
        
        System.out.println("============================");
    }
 
    // Ŭ���̾�Ʈ�� ������ �����ϸ� ȣ��Ǵ� �޼ҵ�
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    	removeSession(session);
    	System.out.println("���� ���� : " + session.getId());
    	
        super.afterConnectionClosed(session, status);
        System.out.println("Ŭ���̾�Ʈ ��������");
        
        showSessions();
        
        System.out.println("============================");
    }
 
    // �޽��� ���۽ó� ���������� ������ �߻��� �� ȣ��Ǵ� �޼ҵ�
    @Override
    public void handleTransportError(WebSocketSession session,
            Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
        System.out.println("���ۿ��� �߻�");
        System.out.println("============================");
    }

}
