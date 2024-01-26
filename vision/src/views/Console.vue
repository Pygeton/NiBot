<script setup>
 import { inject } from 'vue'
 const messages = inject('messages')
</script>

<template>
  <div class="console">
    <h1>Console</h1>
    <ul>
      <li v-for="(message, index) in messages" :key="index">
        {{ message }}
      </li>
    </ul>
  </div>
</template>

<style>
@media (min-width: 1024px) {
  .console {
    min-height: 100vh;
    display: flex;
    align-items: border-left;
  }
}
</style>
