<script setup>
    import { inject } from 'vue'
    const b50Data = inject('b50Data')
    const b35Data = b50Data.params.b35List
    const b15Data = b50Data.params.b15List
</script>

<template>
    <div class="best35">
        <h1>Best50</h1>
        <el-table :data="b35Data">
            <el-table-column v-for="(item, index) in b35Data" :key="index">
                <template slot-scope="scope">
                <el-card>
                    <img :src=item.coverUrl alt="image">
                    <el-button :type="scope.row.buttonColor" round>{{ scope.row.buttonText }}</el-button>
                    <div>{{ scope.row.text }}</div>
                </el-card>
                </template>
            </el-table-column>
        </el-table>
    </div>
    <div class="best15">

    </div>
</template>

<style>
@media (min-width: 1024px) {
  .about {
    min-height: 100vh;
    display: flex;
    align-items: center;
  }
}
</style>
