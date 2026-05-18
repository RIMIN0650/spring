<template>
  <div class="login-container">
    <h2>스프링 시큐리티 로그인 테스트 (Vue)</h2>
    <div class="form-group">
      <input 
        type="text" 
        v-model="loginForm.email" 
        placeholder="이메일 입력" 
      />
    </div>
    <div class="form-group">
      <input 
        type="password" 
        v-model="loginForm.password" 
        placeholder="비밀번호 입력" 
        @keyup.enter="handleLogin"
      />
    </div>
    <button @click="handleLogin">로그인</button>
    
    <p v-if="message" class="status-message">{{ message }}</p>
  </div>
</template>

<script setup>
import { ref } from 'vue';

// 이벤트 정의
const emit = defineEmits(['login-success']);

// 로그인 폼 데이터 상태 관리
const loginForm = ref({
  email: '',
  password: ''
});

const message = ref('');

const handleLogin = async () => {
  if (!loginForm.value.email || !loginForm.value.password) {
    message.value = '이메일과 비밀번호를 모두 입력해주세요.';
    return;
  }

  try {
    // 스프링 부트 서버 주소 (실제 환경에 맞게 포트 번호 등을 수정하세요)
    const targetUrl = 'http://localhost:8080/login';

    const response = await fetch(targetUrl, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      // ⚠️ 크롬 브라우저가 교차 출처(CORS) 상황에서도 쿠키(Set-Cookie)를 수용하고 저장하도록 강제하는 핵심 옵션
      credentials: 'include', 
      body: JSON.stringify({
        email: loginForm.value.email,
        password: loginForm.value.password
      })
    });

    if (response.ok) {
      message.value = '로그인 요청 성공! 크롬 개발자 도구(Application -> Cookies)를 확인하세요.';
      // 로그인 성공 이벤트 발생
      emit('login-success');
    } else {
      message.value = `로그인 실패 (상태 코드: ${response.status})`;
    }
  } catch (error) {
    console.error('에러 발생:', error);
    message.value = '서버 연결 실패 또는 CORS 에러가 발생했습니다.';
  }
};
</script>

<style scoped>
.login-container {
  max-width: 400px;
  margin: 50px auto;
  padding: 30px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-family: sans-serif;
  text-align: center;
}

.form-group {
  margin-bottom: 15px;
}

input {
  width: 100%;
  padding: 10px;
  box-sizing: border-box;
  border: 1px solid #ccc;
  border-radius: 4px;
}

button {
  width: 100%;
  padding: 10px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
}

button:hover {
  background-color: #35495e;
}

.status-message {
  margin-top: 20px;
  font-size: 14px;
  color: #333;
  word-break: break-all;
}
</style>