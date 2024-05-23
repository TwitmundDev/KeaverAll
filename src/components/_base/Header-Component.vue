<script>
import { FwbDropdown, FwbListGroup , FwbListGroupItem} from 'flowbite-vue'
import {ref} from "vue";
import DropDownComponent from "@/components/_base/DropDown-Component.vue";

export default {
  name: 'HeaderComponent',
  components: {DropDownComponent, FwbListGroupItem, FwbListGroup, FwbDropdown},
  setup(){
    let showMenu = ref(false);
    let show = ref(false);
    const toggleNav = () => (showMenu.value = !showMenu.value);
    return { showMenu, show, toggleNav };
  }
}

</script>

<template>
  <!--
  Tablette header
  -->
  <div class="tablet:hidden max-tablet:bg-purple-900 text-white py-2 grid grid-cols-2 content-start ">
    <div class="bg-green-500 mx-auto">
      <img src="@/assets/img/logo_header.png">
    </div>
    <DropDownComponent></DropDownComponent>
  </div>

  <div class="max-tablet:hidden bg-red-500 text-white py-2 grid grid-cols-5 content-start ">
    <div class="bg-green-100 text-black text-center text-3xl ">
      <a href="/">Accueil</a>
    </div>
    <div class="bg-green-300 text-center text-3xl ">
      <a href="/shop">Boutique</a>
    </div>
    <div class="bg-green-500 mx-auto">
      <img src="@/assets/img/logo_header.png">
    </div>
    <div class="tablet:bg-amber-600 desktop:bg-green-300 text-center text-3xl">
      <a href="/about">Qui sommes nous ?   </a>
    </div>
    <div class="flex justify-end">
      <DropDownComponent></DropDownComponent>
    </div>

  </div>
</template>

<style scoped>
</style>
