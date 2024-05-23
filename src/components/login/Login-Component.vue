<script>
import axios from "axios";
import Swal from "sweetalert2";
import {ApiUrl} from "@/main";
import {router} from "@/components/router/router";

export default {
    name: 'LoginComponent',
    components: {
    },
    data() {
        return {
            email: '',
            password: '',
            error: '',
            success: '',
            loading: false,
        }
    },
    methods:{
      onSubmit() {
        const formData = new FormData()
        formData.append('email', this.email)
        formData.append('password', this.password)

        axios.post(`${ApiUrl}/auth/login`, formData, {}).then((res) => {
          if (res.error === "Bad Request"){
            Swal.fire({
              title: 'Erreur',
              text: "Le formulaire n'est pas conforme",
              icon: 'error',
              confirmButtonText: 'Ok',
              background:"#181818"
            });
          }
          //console.log(res.data)
          if (res.data.status === "success"){
            let d = new Date();
            d.setTime(d.getTime() + 2592000000);
            let expires = "expires=" + d.toUTCString();
            console.log(res.data.token)
            document.cookie = "token=" + res.data.token + ";" + expires + ";path=/";
            router.push({name: 'HomeDashboardComponent'})
          }
          if (res.data.status === "error"){
            Swal.fire({
              title: 'Error!',
              text: `${res.data.message}`,
              icon: 'error',
              confirmButtonText: 'Ok',
              background:"#181818"
            });
          }

        });
      }
    }
}

</script>

<template>
  <p class="text-xl text-center text-black ">Page de connexion</p>
  <div class="flex justify-center items-center p-80">
  <div class="w-full max-w-sm p-4 border rounded-lg shadow sm:p-6 md:p-8 bg-gray-800 border-gray-700 text-white">
    <form @submit.prevent="onSubmit" class="space-y-6">
      <h5 class="text-xl font-medium text-white text-center">Connectez vous</h5>
      <div>
        <label for="email" class="block mb-2 text-sm font-medium ">Email</label>
        <input v-model="email" type="email" name="email" id="email" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white" placeholder="name@company.com" required>
      </div>
      <div>
        <label for="password" class="block mb-2 text-sm font-medium ">Password</label>
        <input v-model="password" type="password" name="password" id="password" placeholder="••••••••" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white" required>
      </div>
      <button type="submit" class="w-full text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">Login to your account</button>
    </form>
  </div>
  </div>
</template>

<style scoped>
body {
  background-color: #2152b7;
}

</style>