<script >
export default {
    name: 'HomeComponent',
    props: {
      price:String,
      name:String,
      description:String,
      imageUrl:String,
      stock:String, // A faire en dernier n'est pas une priorité
      discount:String, // A faire en dernier n'est pas une priorité


    }
}

</script>

<template>
  <div class="w-full max-w-sm bg-gray-400 border border-gray-700 rounded-lg shadow dark:bg-gray-800 dark:border-gray-700 m-4">
    <a href="#">
      <img class="p-8 rounded-t-lg" src="@/assets/img/photo_clavier1.2.png" alt="product image" />
    </a>
    <div class="px-5 pb-5">
      <a href="#">
        <h5 class="text-xl font-semibold tracking-tight text-gray-900 dark:text-white">{{name}}</h5>
      </a>
      <div class="flex items-center mt-2.5 mb-5">
        <span class="bg-green-100 text-blue-800 text-xs font-semibold px-2.5 py-0.5 rounded dark:bg-blue-200 dark:text-blue-800 ms-3"> - {{discount}}</span>
      </div>
      <div class="flex items-center justify-between">
        <span class="text-3xl font-bold text-gray-900 dark:text-white">{{price}}</span>
        <a href="#" class="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">Add to cart</a>
      </div>
    </div>
  </div>

</template>

<style scoped>

</style>