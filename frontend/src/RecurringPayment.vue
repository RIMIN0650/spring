<template>
  <div style="padding: 20px; font-family: sans-serif;">
    <h2>카드 등록 및 정기결제</h2>
    
    <!-- 사용자 정보 표시 -->
    <div v-if="userInfo" style="background-color: #f5f5f5; padding: 15px; border-radius: 8px; margin-bottom: 20px;">
      <p><strong>사용자명:</strong> {{ userInfo.name }}</p>
      <p><strong>역할:</strong> {{ userInfo.role }}</p>
      <p><strong>빌링키:</strong> <span v-if="userInfo.billingKey" style="color: green;">✓ 등록됨</span><span v-else style="color: red;">✗ 미등록</span></p>
    </div>

    <!-- 에러 메시지 -->
    <div v-if="errorMessage" style="color: red; background-color: #ffe6e6; padding: 10px; border-radius: 5px; margin-bottom: 15px;">
      ⚠️ {{ errorMessage }}
    </div>

    <!-- 버튼 -->
    <div style="display: flex; gap: 10px;">
      <button 
        @click="requestBillingKey" 
        :disabled="isLoading"
        style="padding: 10px 20px; background-color: #007bff; color: white; border: none; border-radius: 5px; cursor: pointer; font-size: 14px;"
        @mouseover="$event.target.style.backgroundColor='#0056b3'"
        @mouseout="$event.target.style.backgroundColor='#007bff'"
      >
        {{ isLoading ? '처리 중...' : '카드 등록하기' }}
      </button>
      
      <button 
        @click="scheduleRecurringPayment" 
        :disabled="!userInfo?.billingKey || isLoading"
        style="padding: 10px 20px; background-color: #28a745; color: white; border: none; border-radius: 5px; cursor: pointer; font-size: 14px;"
        @mouseover="$event.target.style.backgroundColor='#1e7e34'"
        @mouseout="$event.target.style.backgroundColor='#28a745'"
      >
        {{ isLoading ? '처리 중...' : '정기결제 예약하기' }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import PortOne from "@portone/browser-sdk/v2";
import axios from "axios";

// ============================================
// 상태 변수 (State)
// ============================================
const userInfo = ref(null);
const isLoading = ref(false);
const errorMessage = ref('');

// ============================================
// 1. 사용자 정보 조회 (GET /me)
// 백엔드의 /me 엔드포인트를 호출하여 현재 로그인한 사용자 정보를 조회합니다.
// ============================================
const fetchUserInfo = async () => {
  try {
    isLoading.value = true;
    errorMessage.value = '';
    
    const response = await axios.get('http://localhost:8080/me', {
      withCredentials: true, // 쿠키 포함 (JWT 토큰 등)
    });
    
    userInfo.value = response.data;
    console.log('사용자 정보 조회 성공:', userInfo.value);
  } catch (error) {
    console.error('사용자 정보 조회 실패:', error);
    errorMessage.value = '사용자 정보를 가져오는 데 실패했습니다.';
  } finally {
    isLoading.value = false;
  }
};

// ============================================
// 2. 빌링키 발급 (카드 등록)
// 포트원 SDK를 사용하여 사용자 카드 정보를 수집하고 빌링키를 발급받습니다.
// ============================================
async function requestBillingKey() {
  try {
    isLoading.value = true;
    errorMessage.value = '';

    // 2-1. 포트원 SDK를 통해 빌링키 발급 요청
    const issueResponse = await PortOne.requestIssueBillingKey({
      storeId: "store-e41df7ff-0ba8-4ccc-b19f-7655c291bbcb",          
      channelKey: "channel-key-2d9ae7cc-7a3e-4638-9e88-6c4d308a0424", 
      billingKeyMethod: "CARD",
    });

    // 2-2. 발급 실패 처리
    if (issueResponse.code !== undefined) {
      errorMessage.value = `빌링키 발급 실패: ${issueResponse.message}`;
      console.error(errorMessage.value);
      return;
    }

    console.log('포트원에서 빌링키 발급 성공:', issueResponse.billingKey);

    // 2-3. 발급받은 빌링키를 백엔드 서버에 저장
    const response = await axios.post("http://localhost:8080/api/billing/register", {
      billingKey: issueResponse.billingKey,
    }, {
      withCredentials: true,
    });

    if (response.status === 200) {
      alert("✓ 카드 등록이 완료되었습니다!");
      
      // 2-4. 사용자 정보 다시 조회하여 UI 업데이트
      await fetchUserInfo();
    }
  } catch (error) {
    console.error("카드 등록 에러:", error);
    errorMessage.value = `에러: ${error.message}`;
    alert("카드 등록 중 에러가 발생했습니다: " + error.message);
  } finally {
    isLoading.value = false;
  }
}

// ============================================
// 3. 정기결제 예약
// 등록된 빌링키를 사용하여 정기결제를 예약합니다.
// 백엔드에서 포트원 API(PORTONE_API_SECRET 사용)를 호출하여 결제를 예약합니다.
// ============================================
async function scheduleRecurringPayment() {
  try {
    // 3-1. 빌링키 확인
    if (!userInfo.value?.billingKey) {
      errorMessage.value = '먼저 카드를 등록해주세요.';
      alert("먼저 카드를 등록해주세요.");
      return;
    }

    isLoading.value = true;
    errorMessage.value = '';

    // 3-2. 주문 정보 생성
    const orderId = `order_${userInfo.value.idx}_${Date.now()}`;
    
    // 3-3. 30일 뒤로 결제 예약 시간 설정
    const nextMonth = new Date();
    nextMonth.setDate(nextMonth.getDate() + 30);
    const timeToPay = nextMonth.toISOString();

    console.log('정기결제 예약 정보:', {
      orderId,
      billingKey: userInfo.value.billingKey,
      timeToPay,
    });

    // 3-4. 백엔드에 정기결제 예약 요청
    // 백엔드는 이 정보를 받아서 포트원 API의 /payments/{paymentId}/schedule 엔드포인트를 호출합니다.
    const response = await axios.post(
      "http://localhost:8080/api/billing/schedule",
      {
        billingKey: userInfo.value.billingKey,
        orderId: orderId,
        orderName: "월간 이용권 정기결제",
        amount: 8900, // 결제 금액 (원)
        timeToPay: timeToPay, // ISO 8601 형식의 시간
      },
      {
        withCredentials: true,
      }
    );

    if (response.status === 200) {
      alert(`✓ 정기결제가 예약되었습니다.\n예약 시간: ${nextMonth.toLocaleDateString('ko-KR', { year: 'numeric', month: 'long', day: 'numeric' })}`);
      console.log('정기결제 예약 성공:', response.data);
    }
  } catch (error) {
    console.error("정기결제 예약 실패:", error);
    const errorMsg = error.response?.data?.message || error.message;
    errorMessage.value = `정기결제 예약 실패: ${errorMsg}`;
    alert("정기결제 예약에 실패했습니다: " + errorMsg);
  } finally {
    isLoading.value = false;
  }
}

// ============================================
// 컴포넌트 마운트 시 사용자 정보 조회
// ============================================
onMounted(() => {
  fetchUserInfo();
});
</script>
