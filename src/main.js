//import './assets/main.css'
import './assets/css/base.css'

import { createApp } from 'vue'
import HeaderComponent from "@/components/_base/Header-Component.vue";
import FooterComponent from "@/components/_base/Footer-Component.vue";
import {router} from "@/components/router/router.js";
import App from './App.vue'
import axios from "axios";

export const ApiUrl = 'http://localhost:8080/api/v1'

createApp(App).use(router).mount('#app')

//createApp(HeaderComponent).mount('#header')
//createApp(FooterComponent).mount('#footer')
