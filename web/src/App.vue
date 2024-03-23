<template>
  <div class="container">
    <el-tree class="tree" :data="treeData" :props="defaultProps" @node-click="handleNodeClick"></el-tree>
    <div id="chart" class="chart"></div>
  </div>
</template>

<script>
import { ref } from 'vue'
import { ElTree } from 'element-plus'
import * as echarts from 'echarts'
import axios from 'axios'

export default {
  components: {
    ElTree
  },
  setup() {
    const treeData = ref([
      { id: 1, label: '《舞萌DX》玩家总体水平分布统计' },
      { id: 2, label: '《舞萌DX》玩家高难度曲目总体平均达成率统计' },
      { id: 3, label: '《中二节奏》玩家总体水平分布统计' }
    ])

    const defaultProps = ref({
      children: 'children',
      label: 'label'
    })

    const handleNodeClick = async (data) => {
      let url;
      let xname;
      let yname;
      let categories = [];
      let set = [];
      let series = [];
      switch(data.id) {
        case 1:
          url = 'http://localhost:8080/api/mai/rating';
          data.label = '《舞萌DX》玩家总体水平分布统计';
          xname = 'RATING值/100';
          yname = '人数';
          categories = ['raBelow150', 'ra150', 'ra151', 'ra152', 'ra153', 'ra154', 'ra155', 'ra156', 'ra157', 'ra158', 'ra159', 'ra160', 'ra161', 'ra162', 'ra163', 'ra164', 'ra165'];
          set = ['低于150', '150-151', '151-152', '152-153', '153-154', '154-155', '155-156', '156-157', '157-158', '158-159', '159-160', '160-161', '161-162', '162-163', '163-164', '164-165', '高于165'];
          break;
        case 2:
          url = 'http://localhost:8080/api/mai/grade';
          data.label = '《舞萌DX》玩家高难度曲目总体平均达成率统计'
          xname = '难度';
          yname = '平均达成率';
          categories = ['sssPctAvg13Plus','sssPctAvg14','sssPctAvg14Plus','ssspPctAvg13Plus','ssspPctAvg14','ssspPctAvg14Plus'];
          set = ['13+', '14', '14+'];
          break;
        case 3:
          url = 'http://localhost:8080/api/chu/rating';
          data.label = '《中二节奏》玩家总体水平分布统计';
          xname = 'RATING值';
          yname = '人数';
          categories = ['raBelow1500', 'ra1500', 'ra1525', 'ra1550', 'ra1575', 'ra1600', 'ra1625', 'ra1650', 'ra1675', 'ra1700'];
          set = ['低于15.00', '15.00-15.25', '15.25-15.50', '15.50-15.75', '15.75-16.00', '16.00-16.25', '16.25-16.50', '16.50-16.75', '16.75-17.00', '高于17.00'];
          break;
      }
      const response = await axios.get(url)
      const chartData = response.data

      if (data.id === 1 || data.id === 3) {
        series = [{
            name: '人数',
            type: 'bar',
            data: categories.map(category => chartData[category])
          }];
      } else if (data.id === 2) {
        series = [{
            name: 'SSS评级平均达成率',
            type: 'bar',
            data: categories.slice(0,3).map(category => chartData[category])
          }, 
          {
            name: 'SSS+评级平均达成率',
            type: 'bar',
            data: categories.slice(3,6).map(category => chartData[category])
          }];
      }

      const chart = echarts.init(document.getElementById('chart'))
      chart.clear();
      const option = {
        title: { text: data.label },
        tooltip: {},
        xAxis: { 
          name: xname,
          data: set,
          axisLabel: {
            rotate: 45,
            interval: 0
          }
        },
        yAxis: {
          name: yname
        },
        series: series
      }
      chart.setOption(option)
    }

    return {
      treeData,
      defaultProps,
      handleNodeClick
    }
  }
}
</script>

<style scoped>
.container {
  display: flex;
  height: 100vh;
}

.tree {
  width: 30%;
  margin-right:20px;
}

.chart {
  width: 70%;
  height: 100%;
}

::v-deep .el-tree-node__label {
  font-size: 20px;
}
</style>





