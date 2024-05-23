<script>
import SidebarDashboard from "@/components/dashboard/_base/SidebarDashboard-Component.vue";
import {ApiUrl} from "@/main";
import axios from "axios";
import Cookies from "vue-cookie";
import Swal from "sweetalert2";
import {initFlowbite} from "flowbite";
import {onMounted} from "vue";

export default {
    name: 'FetchUserDashboardComponent',
  components: {SidebarDashboard},
  data() {
    return {
      customers: {},
      userSend: {
        username: "",
        firstName: "",
        lastName: "",
        email: "",
        password: "",
      }
    }
  },
  created() {
    this.fetchData();
  },
  mounted() {
    onMounted(() => {
      initFlowbite();

    })
  },

  methods: {
    sleep(ms) {
      return new Promise(resolve => setTimeout(resolve, ms));
    },
    fetchData() {
      let token= Cookies.get('token');
      const formData = new FormData()
      formData.append('token', token)

      axios.post(`${ApiUrl}/users/fetch`, formData, {}).then((res) => {
            //console.log(response.data);
            //this.res = JSON.stringify(response.data);
            //this.products = JSON.stringify(response.data);
            this.customers = res.data.users;
            console.log("Customer : " + this.customers[0].username);
          })
          .catch((errors) => {
            console.log(errors); // Errors
          });
    },
    redirectTo(str) {
      location.assign(`/modify/${str}`);
    },
    sendUser(){
      let token= Cookies.get('token');
      const formData = new FormData()
      formData.append('token', token)
      formData.append('email', this.userSend.email)
      formData.append('firstName',  this.userSend.firstName )
      formData.append('username',  this.userSend.username )
      formData.append('lastName', this.userSend.lastName)
      formData.append('password', this.userSend.password)

      axios.post(`${ApiUrl}/users/create`, formData, {}).then((res) => {
        if (res.error === "Bad Request"){
          Swal.fire({
            title: 'Erreur',
            text: "Le formulaire n'est pas conforme",
            icon: 'error',
            confirmButtonText: 'Ok',
            background:"#181818"
          });
        }
        if (res.data.status === "error") {
          Swal.fire({
            title: 'Error!',
            text: `${res.data.message}`,
            icon: 'error',
            confirmButtonText: 'Ok',
            background:"#181818"
          });
        }else {
          Swal.fire({
            icon: 'success',
            title: "L'utilisateur a bien été créé",
            showConfirmButton: false,
            background:"#181818",
            timer: 1500
          })
          this.sleep(2000).then(() => {
            this.fetchData()
            window.location.reload();
          });
        }
        this.userSend.email = ""
        this.userSend.firstName = ""
        this.userSend.lastName = ""
        this.userSend.password = ""
        this.userSend.username = ""
      })

    }
  }
}

</script>

<template>
  <h1 class="text-2xl text-center"> FetchDashboard </h1>
  <SidebarDashboard></SidebarDashboard>
  <div class="relative overflow-x-auto shadow-md sm:rounded-lg m-4 p-4">
    <table class="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400">
      <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
      <tr>
        <th scope="col" class="px-6 py-3">
          Username
        </th>
        <th scope="col" class="px-6 py-3">
          First Name
        </th>
        <th scope="col" class="px-6 py-3">
          Last Name
        </th>
        <th scope="col" class="px-6 py-3">
          Email
        </th>
        <th scope="col" class="px-6 py-3">
          Role
        </th>
        <th scope="col" class="px-6 py-3">
          <span class="sr-only">Edit</span>
        </th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="customer in customers" class="bg-white border-b dark:bg-gray-800 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-600">
        <th scope="row" class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white">
          {{ customer.username}}
        </th>
        <td class="px-6 py-4">
          {{ customer.firstName}}
        </td>
        <td class="px-6 py-4">
          {{ customer.lastName}}
        </td>
        <td class="px-6 py-4">
          {{ customer.email}}
        </td>
        <td class="px-6 py-4">
          <div v-if="customer.role === 'ADMIN'">
            <span class="inline-flex items-center bg-green-100 text-green-800 text-xs font-medium px-2.5 py-0.5 rounded-full dark:bg-green-900 dark:text-green-300">
                  <span class="w-2 h-2 me-1 bg-green-500 rounded-full"></span>
                       {{customer.role}}
            </span>
          </div>
          <div v-else-if="customer.role === 'USER'">
            <span class="inline-flex items-center bg-purple-100 text-purple-800 text-xs font-medium px-2.5 py-0.5 rounded-full dark:bg-purple-900 dark:text-purple-300">
                <span class="w-2 h-2 me-1 bg-purple-500 rounded-full"></span>
                {{customer.role}}
            </span>
          </div>
        </td>
        <td class="px-6 py-4 text-right">
          <button type="button" id="updateProductButton"
                  data-drawer-target="drawer-update-product-default"
                  data-drawer-show="drawer-update-product-default"
                  aria-controls="drawer-update-product-default"
                  data-drawer-placement="right"
                  class="inline-flex items-center px-3 py-2 text-sm font-medium text-center text-white rounded-lg bg-primary-700 hover:bg-primary-800 focus:ring-4 focus:ring-primary-300 dark:bg-primary-600 dark:hover:bg-primary-700 dark:focus:ring-primary-800">
            <svg class="w-4 h-4 mr-2" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path d="M17.414 2.586a2 2 0 00-2.828 0L7 10.172V13h2.828l7.586-7.586a2 2 0 000-2.828z"></path><path fill-rule="evenodd" d="M2 6a2 2 0 012-2h4a1 1 0 010 2H4v10h10v-4a1 1 0 112 0v4a2 2 0 01-2 2H4a2 2 0 01-2-2V6z" clip-rule="evenodd"></path></svg>
            Edit c
          </button>
        </td>
      </tr>
      </tbody>
    </table>
    <button id="createProductButton"
            class="text-white bg-amber-950 hover:bg-primary-800 focus:ring-4 focus:ring-primary-300 font-medium rounded-lg text-sm px-5 py-2.5 border-b dark:bg-gray-600 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-600"
            type="button" data-drawer-target="drawer-create-product-default"
            data-drawer-show="drawer-create-product-default" aria-controls="drawer-create-product-default"
            data-drawer-placement="right">
      Add new User
    </button>
  </div>

  <!-- Drawer update User -->
  <div id="drawer-update-product-default"
       class="fixed top-0 right-0 z-40 w-full h-screen max-w-xs p-4 overflow-y-auto transition-transform translate-x-full bg-white dark:bg-gray-800"
       tabindex="-1" aria-labelledby="drawer-label" aria-hidden="true">
    <h5 id="drawer-label"
        class="inline-flex items-center mb-6 text-sm font-semibold text-gray-500 uppercase dark:text-gray-400">
      Update User
    </h5>
    <button type="button" data-drawer-dismiss="drawer-update-product-default"
            aria-controls="drawer-update-product-default"
            class="text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm p-1.5 absolute top-2.5 right-2.5 inline-flex items-center dark:hover:bg-gray-600 dark:hover:text-white">
      <svg aria-hidden="true" class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20"
           xmlns="http://www.w3.org/2000/svg">
        <path fill-rule="evenodd"
              d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
              clip-rule="evenodd"></path>
      </svg>
      <span class="sr-only">Close menu</span>
    </button>
    <form @submit.prevent="sendUser">
      <div class="space-y-4">
        <div>
          <label for="name" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">First Name</label>
          <input type="text" name="title" id="name" v-model="userSend.firstName"
                 class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                 placeholder="Type product name" required="">
        </div>

        <div>
          <label for="lastName" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Last Name</label>
          <input type="text" name="price" id="price" v-model="userSend.lastName"
                 class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                 placeholder="Last Name" required="">
        </div>
        <div>
          <label for="email" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Email</label>
          <input type="text" name="price" id="price" v-model="userSend.email"
                 class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                 required="">
        </div>
        <div>
          <label for="email" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Username</label>
          <input type="text" name="price" id="price" v-model="userSend.username"
                 class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                 required="">
        </div>
        <div>
          <label for="password"
                 class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Password</label>
          <input type="password" name="password" id="password" v-model="userSend.password"
                 class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                 required="">
        </div>
        <div class="bottom-0 left-0 flex justify-center w-full pb-4 space-x-4 md:px-4 md:absolute">
          <button type="submit"
                  class="text-white w-full justify-center bg-primary-700 hover:bg-primary-800 focus:ring-4 focus:outline-none focus:ring-primary-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-primary-600 dark:hover:bg-primary-700 dark:focus:ring-primary-800">
            Add product
          </button>
          <button type="submit" data-drawer-dismiss="drawer-update-product-default"
                  aria-controls="drawer-update-product-default"
                  class="inline-flex w-full justify-center text-gray-500 items-center bg-white hover:bg-gray-100 focus:ring-4 focus:outline-none focus:ring-primary-300 rounded-lg border border-gray-200 text-sm font-medium px-5 py-2.5 hover:text-gray-900 focus:z-10 dark:bg-gray-700 dark:text-gray-300 dark:border-gray-500 dark:hover:text-white dark:hover:bg-gray-600 dark:focus:ring-gray-600">
            <svg aria-hidden="true" class="w-5 h-5 -ml-1 sm:mr-1" fill="none" stroke="currentColor"
                 viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
            </svg>
            Cancel
          </button>
        </div>
      </div>
    </form>
  </div>











  <!-- Drawer create User -->

  <div id="drawer-create-product-default"
       class="fixed top-0 right-0 z-40 w-full h-screen max-w-xs p-4 overflow-y-auto transition-transform translate-x-full bg-white dark:bg-gray-800"
       tabindex="-1" aria-labelledby="drawer-label" aria-hidden="true">
    <h5 id="drawer-label"
        class="inline-flex items-center mb-6 text-sm font-semibold text-gray-500 uppercase dark:text-gray-400">
      Create User
    </h5>
    <button type="button" data-drawer-dismiss="drawer-create-product-default"
            aria-controls="drawer-create-product-default"
            class="text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm p-1.5 absolute top-2.5 right-2.5 inline-flex items-center dark:hover:bg-gray-600 dark:hover:text-white">
      <svg aria-hidden="true" class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20"
           xmlns="http://www.w3.org/2000/svg">
        <path fill-rule="evenodd"
              d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
              clip-rule="evenodd"></path>
      </svg>
      <span class="sr-only">Close menu</span>
    </button>
    <form @submit.prevent="sendUser">
      <div class="space-y-4">
        <div>
          <label for="name" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">First Name</label>
          <input type="text" name="title" id="name" v-model="userSend.firstName"
                 class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                 placeholder="Type product name" required="">
        </div>

        <div>
          <label for="lastName" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Last Name</label>
          <input type="text" name="price" id="price" v-model="userSend.lastName"
                 class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                 placeholder="Last Name" required="">
        </div>
        <div>
          <label for="email" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Email</label>
          <input type="text" name="price" id="price" v-model="userSend.email"
                 class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                 required="">
        </div>
        <div>
          <label for="email" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Username</label>
          <input type="text" name="price" id="price" v-model="userSend.username"
                 class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                 required="">
        </div>
        <div>
          <label for="password"
                 class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Password</label>
          <input type="password" name="password" id="password" v-model="userSend.password"
                 class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                 required="">
        </div>
        <div class="bottom-0 left-0 flex justify-center w-full pb-4 space-x-4 md:px-4 md:absolute">
          <button type="submit"
                  class="text-white w-full justify-center bg-primary-700 hover:bg-primary-800 focus:ring-4 focus:outline-none focus:ring-primary-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-primary-600 dark:hover:bg-primary-700 dark:focus:ring-primary-800">
            Add product
          </button>
          <button type="submit" data-drawer-dismiss="drawer-create-product-default"
                  aria-controls="drawer-create-product-default"
                  class="inline-flex w-full justify-center text-gray-500 items-center bg-white hover:bg-gray-100 focus:ring-4 focus:outline-none focus:ring-primary-300 rounded-lg border border-gray-200 text-sm font-medium px-5 py-2.5 hover:text-gray-900 focus:z-10 dark:bg-gray-700 dark:text-gray-300 dark:border-gray-500 dark:hover:text-white dark:hover:bg-gray-600 dark:focus:ring-gray-600">
            <svg aria-hidden="true" class="w-5 h-5 -ml-1 sm:mr-1" fill="none" stroke="currentColor"
                 viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
            </svg>
            Cancel
          </button>
        </div>
      </div>
    </form>
  </div>



</template>

<style scoped>

</style>