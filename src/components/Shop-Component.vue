<script>
import CardComponent from "@/components/_base/Card-Component.vue";

export default {
    name: 'ShopComponent',
  components: {CardComponent},
}

</script>

<template>
  <h1 class="text-2xl text-center"> Boutique </h1>
  <div class="grid grid-cols-4 max-tablet:bg-purple-900 bg-gray-900 items-center p-4 m-0.5">
    <div class="p-7">
      <CardComponent
          price="100€"
          name="Clavier"
          description="Un clavier"
          imageUrl="@/assets/img/photo_clavier1.2.png"
          stock="10"
          discount="10%"></CardComponent>
    </div>
    <div class="p-7">
      <CardComponent
          price="100€"
          name="Clavier"
          description="Un clavier"
          imageUrl="@/assets/img/photo_clavier1.2.png"
          stock="10"
          discount="10%"></CardComponent>
    </div>
    <div class="p-7">
      <CardComponent
          price="100€"
          name="Clavier"
          description="Un clavier"
          imageUrl="@/assets/img/photo_clavier1.2.png"
          stock="10"
          discount="10%"></CardComponent>
    </div>
    <div class="p-7">
      <CardComponent
          price="100€"
          name="Clavier"
          description="Un clavier"
          imageUrl="@/assets/img/photo_clavier1.2.png"
          stock="10"
          discount="10%"></CardComponent>
    </div>



  </div>
</template>

<style scoped>

</style>