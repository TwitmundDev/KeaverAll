import { createRouter, createWebHistory } from 'vue-router'
import HomeComponent from "@/components/Home-Component.vue";
import TestComponent from "@/components/Test-Component.vue";
import NotFoundComponent from "@/components/NotFound-Component.vue";
import AboutComponent from "@/components/About-Component.vue";
import ShopComponent from "@/components/Shop-Component.vue";
import HomeDashboardComponent from "@/components/dashboard/HomeDashboard-Component.vue";
import HeaderComponent from "@/components/_base/Header-Component.vue";
import LoginComponent from "@/components/login/Login-Component.vue";
import {authGuard} from "@/components/_helpers/auth-guard";
import FetchUserDashboardComponent from "@/components/dashboard/FetchUserDashboard-Component.vue";
import ProductManagementComponent from "@/components/dashboard/product/ProductManagement-Component.vue";

export const router = createRouter({
    history: createWebHistory(),
    routes: [
        {path: "/", name: 'HomeComponent', component:HomeComponent, meta:{ requireHeader:true , requireFooter:true}},
        {path: "/about", name: 'AboutComponent', component:AboutComponent, meta:{ requireHeader:true , requireFooter:true}},
        {path: "/test", name: 'TestComponent', component:TestComponent, meta:{ requireHeader:true , requireFooter:true}},
        {path: "/shop", name: 'ShopComponent', component:ShopComponent, meta:{ requireHeader:true , requireFooter:true}},

        {
            path: "/admin/dashboard",
            name: 'HomeDashboardComponent',
            component:HomeDashboardComponent,
            meta:{
                requireHeader:false,
                requireFooter:false},
                beforeEnter: authGuard
        },
        {
            path: "/admin/fetch-user",
            name: 'FetchUserDashboardComponent',
            component:FetchUserDashboardComponent,
            meta:{
                requireHeader:false,
                requireFooter:false},
            beforeEnter: authGuard
        },
        {
            path: "/admin/productManagement",
            name: 'ProductManagementComponent',
            component:ProductManagementComponent,
            meta:{
                requireHeader:false,
                requireFooter:false},
            beforeEnter: authGuard
        },



        {path: "/login", name: 'LoginComponent', component:LoginComponent, meta:{ requireHeader:false , requireFooter:false}},
        {path: '/:pathMatch(.*)*', name: 'NotFound', component: NotFoundComponent , meta:{ requireHeader:true , requireFooter:true}},
    ],
})