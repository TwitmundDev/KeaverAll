
import {router} from "@/components/router/router";
import axios from "axios";
import {ApiUrl} from "@/main";
import Cookies from "vue-cookie";

export function authGuard(to){
    let token= Cookies.get('token');
    //console.log(token)
    if (token === null){
        router.push("/")
        return false;
    }

    const formData = new FormData()
    formData.append('token', token)

    axios.post(`${ApiUrl}/users/authorize`, formData, {}).then((res) => {
       if(res.data.status === 'success') {
           console.log('success')
           router.push(to)
           return true;
       }
       if (res.data.status === 'error') {
           console.log('error')
           router.push("/")
           return  false;
       }
    });
}