<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<style>
    /* Helper Chat CSS */
    #chat-widget-fab {
        position: fixed;
        bottom: 30px;
        right: 30px;
        width: 60px;
        height: 60px;
        border-radius: 50%;
        background-color: #435ebe;
        color: white;
        display: flex;
        align-items: center;
        justify-content: center;
        box-shadow: 0 4px 12px rgba(0,0,0,0.15);
        cursor: pointer;
        z-index: 9999;
        transition: transform 0.2s;
    }
    #chat-widget-fab i {
        display: flex;
        align-items: center;
        justify-content: center;
    }
    #chat-widget-fab:hover {
        transform: scale(1.1);
    }
    #chat-widget-window {
        position: fixed;
        bottom: 100px;
        right: 30px;
        width: 350px;
        height: 500px;
        background: white;
        border-radius: 15px;
        box-shadow: 0 5px 20px rgba(0,0,0,0.2);
        z-index: 9999;
        display: none;
        flex-direction: column;
        overflow: hidden;
    }
    .chat-header {
        background: #435ebe;
        color: white;
        padding: 15px;
        font-weight: bold;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
    .chat-body {
        flex: 1;
        padding: 15px;
        overflow-y: auto;
        background: #f8f9fa;
        scrollbar-width: thin;
    }
    .chat-footer {
        padding: 10px;
        border-top: 1px solid #eee;
        display: flex;
        gap: 10px;
        background: white;
    }
    .message {
        margin-bottom: 10px;
        padding: 8px 12px;
        border-radius: 10px;
        max-width: 80%;
        word-wrap: break-word;
        font-size: 0.9rem;
    }
    .message.user {
        background: #435ebe;
        color: white;
        align-self: flex-end;
        margin-left: auto;
    }
    .message.bot {
        background: #e9ecef;
        color: #333;
        align-self: flex-start;
    }
</style>

<!-- Chat Widget UI -->
<div id="chat-widget-fab" onclick="toggleChat()">
    <i class="bi bi-chat-dots-fill" style="font-size: 1.5rem;"></i>
</div>

<div id="chat-widget-window">
    <div class="chat-header">
        <div class="d-flex align-items-center">
            <i class="bi bi-robot me-2"></i>
            <span>S2C Assistant</span>
        </div>
        <div class="d-flex align-items-center">
            <button type="button" class="btn btn-sm text-white me-2" onclick="clearChatHistory()" title="Xóa lịch sử">
                <i class="bi bi-trash"></i>
            </button>
            <button type="button" class="btn-close btn-close-white" onclick="toggleChat()"></button>
        </div>
    </div>
    <div class="chat-body d-flex flex-column" id="chat-messages">
        <!-- Messages will be injected here -->
        <sec:authorize access="isAuthenticated()">
            <div class="message bot">Xin chào! Tôi có thể giúp gì cho bạn hôm nay?</div>
        </sec:authorize>
        <sec:authorize access="!isAuthenticated()">
            <div class="message bot text-center">
                <strong>Đăng nhập để sử dụng chatbot</strong><br>
                <small class="text-muted">Vui lòng đăng nhập để sử dụng</small><br>
                <a href="${pageContext.request.contextPath}/auth/login" class="btn btn-sm btn-primary mt-2">Đăng nhập ngay</a>
            </div>
        </sec:authorize>
    </div>
    <sec:authorize access="isAuthenticated()">
        <div class="chat-footer">
            <input type="text" id="chat-input" class="form-control" placeholder="Hỏi về điểm danh..." onkeypress="handleEnter(event)">
            <button class="btn btn-primary" onclick="sendMessage()">
                <i class="bi bi-send-fill"></i>
            </button>
        </div>
    </sec:authorize>
    <sec:authorize access="!isAuthenticated()">
        <div class="chat-footer" style="background: #f8f9fa; border-top: 1px solid #dee2e6;">
            <input type="text" class="form-control" placeholder="Đăng nhập để sử dụng..." disabled>
            <button class="btn btn-secondary" disabled>
                <i class="bi bi-lock-fill"></i>
            </button>
        </div>
    </sec:authorize>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function() {
        // Restore chat history on load
        loadChatHistory();
    });

    function toggleChat() {
        var chatWindow = document.getElementById('chat-widget-window');
        if (chatWindow.style.display === 'none' || chatWindow.style.display === '') {
            chatWindow.style.display = 'flex';
            document.getElementById('chat-input').focus();
            
            // Scroll to bottom when opening
            var chatBody = document.getElementById('chat-messages');
            chatBody.scrollTop = chatBody.scrollHeight;
        } else {
            chatWindow.style.display = 'none';
        }
    }

    function handleEnter(e) {
        if (e.key === 'Enter') {
            sendMessage();
        }
    }

    // Save history to sessionStorage
    function saveMessageToHistory(text, sender) {
        var history = JSON.parse(sessionStorage.getItem('chatHistory') || '[]');
        history.push({ text: text, sender: sender });
        sessionStorage.setItem('chatHistory', JSON.stringify(history));
    }

    function loadChatHistory() {
        var history = JSON.parse(sessionStorage.getItem('chatHistory') || '[]');
        var chatBody = document.getElementById('chat-messages');
        
        // Keep the welcome message if history is empty
        if (history.length > 0) {
            // Clear existing default message if we have history
            chatBody.innerHTML = ''; 
            history.forEach(function(msg) {
                var msgDiv = document.createElement('div');
                msgDiv.className = 'message ' + msg.sender;
                msgDiv.innerText = msg.text;
                chatBody.appendChild(msgDiv);
            });
            chatBody.scrollTop = chatBody.scrollHeight;
        }
    }
    
    function clearChatHistory() {
        if(confirm('Bạn có chắc muốn xóa toàn bộ lịch sử chat?')) {
            sessionStorage.removeItem('chatHistory');
            document.getElementById('chat-messages').innerHTML = '<div class="message bot">Xin chào! Tôi có thể giúp gì cho bạn hôm nay?</div>';
        }
    }

    function appendMessage(text, sender) {
        var chatBody = document.getElementById('chat-messages');
        var msgDiv = document.createElement('div');
        msgDiv.className = 'message ' + sender;
        msgDiv.innerText = text;
        chatBody.appendChild(msgDiv);
        chatBody.scrollTop = chatBody.scrollHeight;
        
        // Save to storage
        saveMessageToHistory(text, sender);
    }

    function sendMessage() {
        var input = document.getElementById('chat-input');
        var question = input.value.trim();
        if (!question) return;

        appendMessage(question, 'user');
        input.value = '';
        
        // Show loading
        var loadingDiv = document.createElement('div');
        loadingDiv.className = 'message bot';
        loadingDiv.id = 'chat-loading';
        loadingDiv.innerText = 'Đang suy nghĩ...';
        document.getElementById('chat-messages').appendChild(loadingDiv);
        document.getElementById('chat-messages').scrollTop = document.getElementById('chat-messages').scrollHeight;

        // CSRF
        var csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");
        var csrfHeader = document.querySelector("meta[name='_csrf_header']").getAttribute("content");

        fetch('${pageContext.request.contextPath}/api/chat/ask', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [csrfHeader]: csrfToken
            },
            body: JSON.stringify({ question: question })
        })
        .then(response => response.json())
        .then(data => {
            var loading = document.getElementById('chat-loading');
            if(loading) loading.remove();
            appendMessage(data.answer, 'bot');
        })
        .catch(error => {
            var loading = document.getElementById('chat-loading');
            if(loading) loading.remove();
            appendMessage("Lỗi kết nối server.", 'bot');
            console.error(error);
        });
    }
</script>
