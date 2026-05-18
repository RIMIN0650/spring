<script setup>
import { ref } from 'vue'
import Login from './Login.vue'
import Dashboard from './Dashboard.vue'
import Payment from './Payment.vue'
import RecurringPayment from './RecurringPayment.vue'

const currentPage = ref('login') // 'login', 'dashboard', 'payment', 'recurring'

const handleLoginSuccess = () => {
  currentPage.value = 'dashboard'
}

const handleGoToPayment = () => {
  currentPage.value = 'payment'
}

const handleGoToRecurring = () => {
  currentPage.value = 'recurring'
}

const handleLogout = () => {
  currentPage.value = 'login'
}

const handleBackToDashboard = () => {
  currentPage.value = 'dashboard'
}
</script>

<template>
  <div id="app">
    <Login v-if="currentPage === 'login'" @login-success="handleLoginSuccess" />
    <Dashboard v-else-if="currentPage === 'dashboard'" @go-to-payment="handleGoToPayment" @go-to-recurring="handleGoToRecurring" @logout="handleLogout" />
    <Payment v-else-if="currentPage === 'payment'" @logout="handleLogout" @back="handleBackToDashboard" />
    <RecurringPayment v-else-if="currentPage === 'recurring'" @back="handleBackToDashboard" />
  </div>
</template>

<style scoped>
#app {
  min-height: 100vh;
  background-color: #f5f5f5;
}
</style>