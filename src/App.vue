<script>

import {defineComponent} from "vue";
import HeaderComponent from "@/components/_base/Header-Component.vue";
import FooterComponent from "@/components/_base/Footer-Component.vue";

export default defineComponent({
  components: {FooterComponent, HeaderComponent}
})
</script>

<template>
  <header>
    <HeaderComponent v-if="$route.meta.requireHeader"></HeaderComponent>
  </header>

  <router-view></router-view>
  <footer>
    <FooterComponent v-if="$route.meta.requireFooter"></FooterComponent>
  </footer>

</template>

<style scoped>

</style>