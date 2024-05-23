<script>
import { onMounted } from 'vue'
import { initFlowbite } from 'flowbite'

export default {
  name: 'TestComponent',
  components: {
    // TestComponent
  },
  mounted() {
    onMounted(() => {
      initFlowbite()
    })

  }
}

</script>

<template>
<h1 class="text-2xl text-center"> Test Component </h1>
</template>

<style scoped>

</style>