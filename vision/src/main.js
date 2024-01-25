import './assets/main.css'

import { createApp, reactive } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

const app = createApp(App)

const messages = reactive([])
app.provide('messages',messages)

app.use(createPinia())
app.use(router)
app.use(ElementPlus)

app.mount('#app').$nextTick(() => {
    // 创建WebSocket连接
    const socket = new WebSocket('ws://localhost:8080/api/websocket')

    // 监听WebSocket的打开事件
    socket.addEventListener('open', function (event) {
        console.log('WebSocket is connected.')
    })

    // 监听WebSocket的消息事件
    socket.addEventListener('message', function (event) {
        console.log('Message from server: ', event.data)
        
        // 解析消息中的JSON串
        let message = JSON.parse(event.data)
        
        // 根据type作出反应
        switch(message.action) {
            case 'console':
                messages.push(message)
                break
            case 'type2':
                // 对type2的处理
                break
            // 其他类型...
            default:
                console.log('Unknown message type: ', message.type)
        }
    })
})
