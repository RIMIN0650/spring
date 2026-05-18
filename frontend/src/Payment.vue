<template>
  <div class="payment-container">
    <h2>결제 페이지</h2>
    <div class="button-group">
      <button @click="$emit('back')" class="back-btn">돌아가기</button>
      <button @click="$emit('logout')" class="logout-btn">로그아웃</button>
    </div>
    
    <div class="courses-section">
      <h3>수강 신청</h3>
      <div v-for="course in courseList" :key="course.idx" class="course-item">
        <input 
          type="checkbox" 
          :checked="selected.includes(course)"
          @change="toggleSelected(course)"
        />
        <span>{{ course.idx }}</span> 
        <span>{{ course.title }}</span> 
        <span>{{ course.price }}원</span>
      </div>
    </div>

    <div class="total-section">
      <h4>총 금액: {{ totalPrice }}원</h4>
      <button @click="onPayment" :disabled="selected.length === 0 || isPaymentProcessing">
        {{ isPaymentProcessing ? '결제 진행 중...' : '결제하기' }}
      </button>
    </div>

    <div v-if="paymentStatus.status" class="payment-status" :class="paymentStatus.status">
      {{ paymentStatus.message }}
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from "axios"
import PortOne from "@portone/browser-sdk/v2"

defineEmits(['back', 'logout'])

const courseList = ref([])
const totalPrice = ref(0)
const selected = ref([])
const isPaymentProcessing = ref(false)

// 결제 상태
const paymentStatus = ref({
  status: "",
  message: ""
})

const getCourseList = async () => {
  try {
    const res = await axios.get('http://localhost:8080/course/list')
    if (res.data) {
      courseList.value = res.data
    }
  } catch (error) {
    console.error("데이터 로드 실패:", error)
  }
}

const toggleSelected = (course) => {
  const index = selected.value.indexOf(course)
  if (index > -1) {
    selected.value.splice(index, 1)
  } else {
    selected.value.push(course)
  }

  totalPrice.value = selected.value.reduce((acc, cur) => acc + cur.price, 0)
}

const onPayment = async () => {
  if (selected.value.length === 0) return
  if (isPaymentProcessing.value) return

  isPaymentProcessing.value = true
  paymentStatus.value = { status: "", message: "" }

  let ordersIdx = null

  const firstItem = selected.value[0]
  const courseIdxList = selected.value.map(course => course.idx)
  const orderName = selected.value.length === 1
      ? firstItem.title
      : `${firstItem.title} 외 ${selected.value.length - 1}건`

  try {
    // 주문 생성
    const createResponse = await axios.post('http://localhost:8080/orders/create', {
      paymentPrice: totalPrice.value,
      courseIdxList
    }, {
      withCredentials: true
    })

    if (!createResponse?.data?.result?.ordersIdx) {
      paymentStatus.value = { status: "FAILED", message: '주문 생성에 실패하였습니다. 잠시 후 다시 시도해주세요.' }
      isPaymentProcessing.value = false
      return
    }

    ordersIdx = createResponse.data.result.ordersIdx
    paymentStatus.value = { status: "IDLE", message: '결제 진행 중...' }

    const paymentId = `order-${new Date().getTime()}-${Math.floor(Math.random() * 1000)}`
    
    // 결제창 띄우기
    const payment = await PortOne.requestPayment({
      storeId: "store-e41df7ff-0ba8-4ccc-b19f-7655c291bbcb",
      channelKey: "channel-key-40356538-ccfe-4420-a5a9-d9190bde73cf",
      paymentId: paymentId,
      orderName: orderName,
      totalAmount: totalPrice.value,
      currency: 'KRW',
      payMethod: "CARD",
      customData: { ordersIdx, courseIdxList }
    }).catch((error) => {
      paymentStatus.value = { status: "FAILED", message: '결제 시도가 실패하였습니다. 잠시 후 다시 시도해주세요.' }
    })

    if (payment?.paymentId) {
      alert(payment.paymentId)
      // 결제 검증
      const verifyResponse = await axios.post('http://localhost:8080/orders/verify', { paymentId: payment.paymentId })
      paymentStatus.value = { status: "SUCCESS", message: '결제가 완료되었습니다!' }
    }
  } catch (error) {
    console.error("결제 오류:", error)
    paymentStatus.value = { status: "FAILED", message: '결제 처리 중 오류가 발생했습니다.' }
  } finally {
    isPaymentProcessing.value = false
  }
}

onMounted(async () => {
  await getCourseList()
})
</script>

<style scoped>
.payment-container {
  max-width: 600px;
  margin: 20px auto;
  padding: 30px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-family: sans-serif;
}

.button-group {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.back-btn {
  flex: 1;
  padding: 8px 15px;
  background-color: #808080;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.back-btn:hover {
  background-color: #606060;
}

.logout-btn {
  flex: 1;
  padding: 8px 15px;
  background-color: #f44336;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.logout-btn:hover {
  background-color: #da190b;
}

h2 {
  margin-top: 0;
}

.courses-section {
  margin-bottom: 30px;
}

.course-item {
  padding: 10px;
  margin: 10px 0;
  border: 1px solid #eee;
  border-radius: 4px;
  display: flex;
  align-items: center;
  gap: 15px;
}

.course-item input[type="checkbox"] {
  cursor: pointer;
}

.course-item span {
  flex: 1;
}

.total-section {
  padding: 20px;
  background-color: #f5f5f5;
  border-radius: 4px;
  text-align: center;
  margin-bottom: 20px;
}

.total-section button {
  width: 100%;
  padding: 12px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  margin-top: 15px;
}

.total-section button:hover:not(:disabled) {
  background-color: #359268;
}

.total-section button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.payment-status {
  padding: 15px;
  border-radius: 4px;
  text-align: center;
  margin-top: 15px;
}

.payment-status.SUCCESS {
  background-color: #d4edda;
  color: #155724;
  border: 1px solid #c3e6cb;
}

.payment-status.FAILED {
  background-color: #f8d7da;
  color: #721c24;
  border: 1px solid #f5c6cb;
}

.payment-status.IDLE {
  background-color: #d1ecf1;
  color: #0c5460;
  border: 1px solid #bee5eb;
}
</style>
