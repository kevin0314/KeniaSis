import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Expediente from '../components/Expedientes.vue'
import Tratamiento from '../components/Tratamientos.vue'
import Paciente from '../components/Pacientes.vue'
import Cliente from '../components/Clientes.vue'


//importando componentes de pedido
import ReportesForm from '../components/Pedidos/PedidosForm.vue'
import ReportesList from '../components/Pedidos/PedidosList.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/about',
    name: 'About',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  },
  {
    path: '/expedientes',
    name: 'expedientes',
    component: Expediente
  },
  {
    path: '/tratamientos',
    name: 'tratamientos',
    component: Tratamiento
  },
  {
    path: '/pacientes',
    name: 'pacientes',
    component: Paciente
  },
  {
    path: '/pedidos/listado',
    name: 'pedidos.listado',
    component: ReportesList,
  },
  {
    path: '/pedidos/listado/Form',
    name: 'pedidos.form',
    component: ReportesForm,
    props: true
  },
  {
    path: '/clientes',
    name: 'clientes',
    component: Cliente
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router