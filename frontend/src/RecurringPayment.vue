<template>
  <div>
    <h2>카드 등록 (정기결제)</h2>
    <button @click="requestBillingKey">카드 등록하기</button>
  </div>
</template>

<script setup>
import PortOne from "@portone/browser-sdk/v2";
import axios from "axios";

async function requestBillingKey() {
  try {
    // 1. 포트원 SDK를 통해 빌링키 발급 요청
    const issueResponse = await PortOne.requestIssueBillingKey({
      storeId: "store-e41df7ff-0ba8-4ccc-b19f-7655c291bbcb",          
      channelKey: "channel-key-2d9ae7cc-7a3e-4638-9e88-6c4d308a0424", 
      billingKeyMethod: "CARD",
      // 필요 시 주석 해제하여 사용
      // issueId: "issue-" + Date.now(),     
      // issueName: "월간 구독 카드 등록",
      // customer: {
      //   customerId: "user-001",
      //   fullName: "홍길동",
      //   email: "user@example.com",
      //   phoneNumber: "010-1234-5678", 
      // },
    });

    // 발급 실패 처리
    if (issueResponse.code !== undefined) {
      alert("빌링키 발급 실패: " + issueResponse.message);
      return;
    }

    // 2. 발급받은 빌링키를 백엔드 서버로 전송
    // (결제 예약 및 스케줄링 로직은 백엔드 내부에서 PORTONE_API_SECRET을 사용해 처리)
    const response = await axios.post("http://localhost:8080/api/billing/register", {
      billingKey: issueResponse.billingKey,
    }, {
      withCredentials: true, // 쿠키 포함
    });

    if (response.status === 200) {
      alert("카드 등록 완료!");
    }
  } catch (error) {
    console.error("에러 발생:", error);
    alert("처리 중 에러가 발생했습니다: " + error.message);
  }
}


</script>