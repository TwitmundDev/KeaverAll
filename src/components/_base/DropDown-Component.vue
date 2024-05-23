<script>
import { onMounted } from 'vue'
import { initFlowbite } from 'flowbite'
export default {
    name: 'DropDownComponent',
    components: {
        // DropDownComponent
    },
    mounted() {
        onMounted(() => {
            initFlowbite()
        })

    }

}

</script>

<template>
  <button id="dropdownHoverButton" data-dropdown-toggle="dropdownHover" data-dropdown-trigger="hover" class="text-white bg-white hover:bg-gray-400 focus:ring-4 focus:outline-none focus:ring-gray-200 font-medium rounded-lg text-sm px-5 py-2.5 text-center inline-flex items-center dark:bg-gray-600 dark:hover:bg-gray-700 dark:focus:ring-gray-800" type="button">
    <img src="@/components/icons/svg/IconMenuBuger.svg">
  </button>

  <!-- Dropdown menu -->
  <div id="dropdownHover" class="z-10 hidden bg-white divide-y divide-gray-100 rounded-lg shadow w-44 dark:bg-gray-700">
    <ul class="py-2 text-sm text-gray-700 dark:text-gray-200" aria-labelledby="dropdownHoverButton">
      <li>
        <a href="/shop" class="block px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white">Boutique</a>
      </li>
      <li>
        <a href="/" class="block px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white">Acceuil</a>
      </li>
      <li>
        <a href="/about" class="block px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white">A propos</a>
      </li>
      <li>
        <!--
        @TODO cart system
        -->
        <a href="#" class="block px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white">Mon panier</a>
      </li>
    </ul>
  </div>

</template>

<style scoped>

</style>