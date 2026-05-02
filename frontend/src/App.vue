<script setup>
import {ref, reactive, onMounted} from 'vue'
import axios from "axios";
import PortOne from "@portone/browser-sdk/v2"

const courseList = ref([])
const totalPrice = ref(0)
const selected = ref([])
const isPaymentProcessing = ref(false)

// 결제 상태
const paymentStatus = ref({
  status: "",
  message: ""
});

const getCourseList = async () => {
  try {
    const res = await axios.get('http://localhost:8080/course/list')
    // 백엔드가 List를 직접 반환하므로 res.data를 바로 할당합니다.
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
  paymentStatus.value = {status: "", message: ""}

  let ordersIdx = null

  const firstItem = selected.value[0]
  const courseIdxList = selected.value.map(course => course.idx)
  const orderName = selected.value.length === 1
      ? firstItem.title
      : `${firstItem.title} 외 ${selected.value.length - 1}건`

  // 주문 생성
  const createResponse = await axios.post('http://localhost:8080/orders/create', {
        paymentPrice: totalPrice.value,
        courseIdxList
      }, {
        withCredentials: true
      }
  )
  
  
  if (!createResponse?.success || !createResponse?.results?.ordersIdx) {
    paymentStatus.value = {status: "FAILED", message: '주문 생성에 실패하였습니다. 잠시 후 다시 시도해주세요.'}
    isPaymentProcessing.value = false
  }


  ordersIdx = createResponse.data.result.ordersIdx
  paymentStatus.value = {status: "IDLE" , message : '결제 진행 중...'}


  const paymentId = Math.floor(Math.random() * 101);
  // 결제창 띄우기
  const payment = await PortOne.requestPayment({
    storeId: "store-e41df7ff-0ba8-4ccc-b19f-7655c291bbcb",
    channelKey: "channel-key-40356538-ccfe-4420-a5a9-d9190bde73cf",
    paymentId: "imp_14421y5ifdg7ig" + paymentId,
    orderName: orderName,
    totalAmount: totalPrice.value,
    currency: 'KRW',
    payMethod: "CARD",
    customData: {ordersIdx, courseIdxList}
  }).then((res) => {
    return res;
  }).catch((error) => {
    paymentStatus.value = {status: "FAILED", message: '결제 시도가 실패하였습니다. 잠시 후 다시 시도해주세요.'}
  });

  // 3. 결제 검증
  const verifyResponse = await axios.post('/orders/verify', {paymentId: payment.paymentId})
}

onMounted(async () => {
  
  await getCourseList()
})
</script>

<template>

  <div v-for="course in courseList" :key="course.idx">
    <div>
      <input type="checkbox" :checked="selected.includes(course.idx)"
             @change="toggleSelected(course)">
      <span>{{ course.idx }}</span> <span>{{ course.title }}</span> <span>{{ course.price }}</span>
    </div>
  </div>
  <button @click="onPayment">결제하기</button>

</template>

<style scoped>


</style>