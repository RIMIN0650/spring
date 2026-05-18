<template>
  <div class="dashboard-container">
    <h2>결제 서비스</h2>
    <button @click="$emit('logout')" class="logout-btn">로그아웃</button>

    <div class="menu-section">
      <h3>결제 방식을 선택하세요</h3>
      
      <div class="button-group">
        <button @click="goToPayment" class="menu-btn payment-btn">
          <span class="btn-title">일반 결제</span>
          <span class="btn-description">강좌 선택 후 일시 결제</span>
        </button>
        
        <button @click="goToRecurring" class="menu-btn recurring-btn">
          <span class="btn-title">정기 결제</span>
          <span class="btn-description">정기적인 자동 결제</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
const emit = defineEmits(['go-to-payment', 'go-to-recurring', 'logout']);

const goToPayment = () => {
  emit('go-to-payment');
};

const goToRecurring = () => {
  emit('go-to-recurring');
};
</script>

<style scoped>
.dashboard-container {
  max-width: 600px;
  margin: 20px auto;
  padding: 30px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-family: sans-serif;
}

.logout-btn {
  float: right;
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
  clear: both;
  margin-top: 20px;
  text-align: center;
  color: #333;
}

.menu-section {
  margin-top: 40px;
}

.menu-section h3 {
  text-align: center;
  margin-bottom: 30px;
  color: #555;
}

.button-group {
  display: flex;
  gap: 20px;
  justify-content: center;
  flex-wrap: wrap;
}

.menu-btn {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 200px;
  height: 150px;
  border: 2px solid #ddd;
  border-radius: 8px;
  background-color: white;
  cursor: pointer;
  transition: all 0.3s ease;
  padding: 20px;
  text-align: center;
}

.menu-btn:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.menu-btn.payment-btn {
  border-color: #42b983;
}

.menu-btn.payment-btn:hover {
  background-color: #f0fdf4;
  border-color: #359268;
}

.menu-btn.recurring-btn {
  border-color: #2196F3;
}

.menu-btn.recurring-btn:hover {
  background-color: #f0f8ff;
  border-color: #1976D2;
}

.btn-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #333;
}

.btn-description {
  font-size: 14px;
  color: #999;
}
</style>
