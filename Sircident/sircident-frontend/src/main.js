import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify'
import Vuex from 'vuex'


import axios from 'axios';
import VueSweetalert2 from 'vue-sweetalert2';
import DatePicker from 'vue2-datepicker';
import VueNumberInput from '@chenfengyuan/vue-number-input';

import vueFilePond from 'vue-filepond';
import FilePondPluginImagePreview from 'filepond-plugin-image-preview';
import FilePondPluginFileValidateType from 'filepond-plugin-file-validate-type';
import FilePondPluginFileValidateSize from 'filepond-plugin-file-validate-size';

//importacion de archivos js
import url from "./config/config"
vueFilePond(FilePondPluginFileValidateType, FilePondPluginImagePreview, 
  FilePondPluginFileValidateSize);

import { ValidationProvider,extend, ValidationObserver } from 'vee-validate';

Vue.config.productionTip = false

//importacion de archivos css
import 'sweetalert2/dist/sweetalert2.min.css';
import 'vue2-datepicker/index.css';
//para la internacionalizacion del datePicker
import 'vue2-datepicker/locale/es/es';

import 'filepond/dist/filepond.min.css';
import 'filepond-plugin-image-preview/dist/filepond-plugin-image-preview.min.css';

//seteando las variables de vue
Vue.use(Vuex);
Vue.use(VueSweetalert2);
Vue.use(VueNumberInput);
Vue.component('date-picker',DatePicker);

extend("required", {
  validate: value => !!value,
  message: "Este Campo Es Requerido"
}); 
//registro global
Vue.component('ValidationProvider',ValidationProvider);
Vue.component('ValidationObserver',ValidationObserver);

//Importando archivos relacionados a VUEX
import "./config/vee-validate";

//seteando time zone de toda la app
const moment = require('moment')
require('moment/locale/es')

Vue.use(require('vue-moment'),{
  moment
})

//creando a nivel global de la App, las variables axios y url del backend

Vue.prototype.$url = url;
Vue.prototype.$http = axios;

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount('#app')