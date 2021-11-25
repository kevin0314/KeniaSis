<template>
  <div class="content">
    <div
      class="md-layout-item md-medium-size-100 md-xsmall-size-100 md-size-100"
    >
      <v-overlay :value="loader" :z-index="'99999999'">
        <v-progress-circular
          indeterminate
          size="80"
          color="light-blue lighten-1"
        ></v-progress-circular>
      </v-overlay>
      <v-card>
        <v-card-title
          v-text="
            action == 'add'
              ? 'Formulario de Registro de Pedidos'
              : action == 'upd'
              ? 'Actualización de Pedido realizado'
              : 'Detalle de Pedido'"
        >
          <div class="flex-row-1"></div>
        </v-card-title>
        <v-container fluid>
          <ValidationObserver v-slot="{ invalid }">
            <v-form
              ref="formPedido"
              v-model="validForm"
              :lazy-validation="true"
            >
              <v-row>
                <v-col cols="12" md="3">
                  <ValidationProvider name="date" rules="required">
                    <div slot-scope="{ errors }">
                      <date-picker
                        v-model="PedidoForm.fechaPedido"
                        class="mt-3"
                        :editable="false"
                        lang="es"
                        value-type="format"
                        format="DD-MM-YYYY"
                        input-class="form-control"
                        placeholder="Fecha Pedido"
                        style="width: 100% !important"
                        :disabled="action == 'detail'"
                      />
                      
                      <p class="text-danger">{{ errors[0] }}</p>
                    </div>
                  </ValidationProvider>
                </v-col>
                <v-col cols="12" md="6"></v-col>
                <v-col cols="12" md="3" v-if="action == 'upd'">
                  <ValidationProvider name="date" rules="required">
                    <div slot-scope="{ errors }">
                      <date-picker
                        v-model="PedidoForm.fechaDespacho"
                        class="mt-3"
                        :editable="false"
                        lang="es"
                        value-type="format"
                        format="DD-MM-YYYY"
                        input-class="form-control"
                        placeholder="Fecha Despacho"
                        style="width: 100% !important"
                        :disabled="action == 'detail'"
                      />
                      <p class="text-danger">{{ errors[0] }}</p>
                    </div>
                  </ValidationProvider>
                </v-col>
              </v-row>
              <v-row>
                <v-col cols="11">
                  <v-autocomplete
                    v-model="PedidoForm.cliente"
                    :items="arrayClientes"
                    :rules="[(v) => !!v || 'Cliente es requerido']"
                    required
                    label="Cliente"
                    item-text="nombre"
                    item-value="id"
                    :disabled="action == 'detail'"
                    clearable
                    :menu-props="{ closeOnClick: true }"
                    return-object
                  >
                  </v-autocomplete>
                </v-col>
                <!-- <v-col cols="1">-->
                <!-- inyectar componente para agregar cliente-->
                <!--<v-btn
                            elevation="10"
                            class="mt-4"
                            text
                            icon
                            color="primary"
                            @click="openModalMarca()"
                            dark>
                            <v-icon>library_add</v-icon>
                            </v-btn>
                          </v-col>-->
              </v-row>
              <v-row>
                <v-col cols="12">
                  <v-alert
                    :value="noPacientes"
                    type="error"
                    border="left"
                    transition="scale-transition"
                    dismissible
                    elevation="2"
                  >
                    Debe agregar uno o mas productos al pedido.
                  </v-alert>
                </v-col>
                <v-col cols="12">
                  <v-card outlined>
                    <v-card-title class="mb-4 font-weight-medium subtitle-1">
                      Detalle del Pedido
                      <div class="flex-grow-1"></div>
                      <v-text-field
                        v-model="searchDetail"
                        label="Buscar en Detalle"
                        hide-details
                      >
                      </v-text-field>
                    </v-card-title>
                    <v-card-text>
                      <v-data-table
                        :headers="headerTable"
                        :items="PedidoForm.detallePedido"
                        no-data-text="No hay productos agregados"
                        :items-per-page="10"
                        :search="searchDetail"
                        :footer-props="{
                          'items-per-page-options': [10, 20, 30],
                          showFirstLastPage: true,
                        }"
                      >
                        <template v-slot:top>
                          <v-toolbar flat color="white">
                            <div class="flex-grow-1"></div>
                            <!-- Modal para agregar productos al detalle -->
                            <v-dialog
                              v-model="modalPacientes"
                              persistent
                              max-width="1150px"
                              scrollable
                            >
                              <template v-slot:activator="{ on }">
                                <v-btn
                                  elevation="10"
                                  color="black"
                                  dark
                                  v-if="action != 'detail'"
                                  class="mb-2"
                                  v-on="on"
                                >
                                  Agregar Productos al pedido&nbsp;
                                  <v-icon>library_add</v-icon>
                                </v-btn>
                              </template>
                              <v-card>
                                <v-card-title
                                  class="headline blue lighten-5"
                                  primary-title
                                >
                                  <span
                                    class="headline"
                                    v-text="formTitle"
                                  ></span>
                                </v-card-title>
                                <v-card-text>
                                  <!-- inyectar componente de lista de producto -->
                                  <PacienteList v-on:created = "getItemsOfProductosOfPedidos()"
                                  @added="onAddedItem"
                                  ref="paciente"
                                  />
                                </v-card-text>
                                <v-divider></v-divider>
                                <v-card-actions>
                                  <div class="flex-grow-1"></div>
                                  <v-btn
                                    color="#FF0000"
                                    text
                                    @click="modalPacientes = false"
                                    >Cerrar
                                  </v-btn>
                                  <v-btn
                                    color="info darken-1"
                                    text
                                    @click="getPacienteFromChild()"
                                    v-text="'Agregar a Pedido'"
                                  >
                                  </v-btn>
                                </v-card-actions>
                              </v-card>
                            </v-dialog>
                          </v-toolbar>
                        </template>
                        <!-- fin modal para Productos -->
                        <template v-slot:item="props">
                          <tr :key="props.item.id">
                            <td v-text="props.item.paciente.nombre" />
                            <td class="text-center">
                              {{ props.item.paciente.precio }}
                            </td>
                            <td class="text-center">
                              <number-input
                                class="mt-2"
                                center
                                size="small"
                                :inputtable="true"
                                :disabled="action == 'detail'"
                                v-model="props.item.cantidad"
                                :return-value.sync="props.item.cantidad"
                                :min="1"
                                inline
                                controls
                              />
                            </td>
                            <td class="text-center">
                              {{
                                cleanSimbolsOfPrice(
                                  props.item.paciente.precio
                                ) * cleanSimbolsOfPrice(props.item.cantidad)
                              }}
                            </td>
                            <!--currency -->
                            <td class="text-center">
                              <v-btn
                                color="#FF0000"
                                class="mx-1"
                                elevation="8"
                                v-if="action != 'detail'"
                                small
                                dark
                                @click="deletePacienteFromPedido(props.item)"
                              >
                                <v-icon>delete</v-icon>
                              </v-btn>
                            </td>
                          </tr>
                        </template>
                        <template v-slot:body.append>
                          <tr class="white">
                            <td>
                              <v-row no-gutters style="flex-wrap: nowrap">
                                <v-col cols="12" md="10">
                                  <v-card
                                    class="
                                      pa-1
                                      grey
                                      lighten-5
                                      font-weight-bold
                                      title
                                    "
                                    outlined
                                    title
                                    >Total {{ PedidoForm.total }}</v-card
                                  >
                                  <!--currency -->
                                </v-col>
                              </v-row>
                            </td>
                            <td colspan="5">
                               <v-row v-if="action != 'detail'">
                                <v-col
                                  cols="12"
                                  class="d-flex flex-row-reverse"
                                  md="12"
                                >
                                  <v-btn
                                    :disabled="!validForm || invalid"
                                    @click="saveOrUpdatePedido()"
                                    large
                                    color="#64DD17"
                                  >
                                    {{
                                      action == "add"
                                        ? "Guardar Pedido"
                                        : action == "upd"
                                        ? "Actualizar Pedido"
                                        : ""
                                    }}&nbsp;
                                    <v-icon>save</v-icon>
                                  </v-btn>
                                </v-col>
                              </v-row>
                            </td>
                          </tr>
                        </template>                        
                      </v-data-table>
                    </v-card-text>
                  </v-card>
                </v-col>
              </v-row>
            </v-form>
          </ValidationObserver>
        </v-container>
      </v-card>
    </div>
  </div>
</template>
<script>
import PacienteList from "@/components/Modals/PacienteList.vue";
export default {
  name: "PedidosForm",
  props: ["pedido", "action"],
  data: () => ({
    loader: false,
    //fechaPedido: null,
    PedidoForm: {
      id: null,
      fechaPedido: null,
      fechaDespacho: null,
      total: new Number("0").toFixed(2),
      estado: "R",
      cliente: null,
      usuario: null,
      detallePedido: [],
    },
    arrayClientes: [],
    headerTable: [
      { text: "Nombre", value: "paciente.name", align: "left" },
      {
        text: "Precio ",
        value: "paciente.precio",
        align: "center",
        sortable: false,
      },
      { text: "Cantidad", value: "cantidad", align: "center", sortable: false },
      { text: "SubTotal", value: "total", align: "center", sortable: false },
    ],
    modalPacientes: false,
    validForm: true,
    noPacientes: false,
    searchDetail: "",
    formTitle: "Agregar items al pedido",
  }),  
 watch:{
    PedidoForm:{
      handler(){
        //PROCESO QUE CONTABILIZA EL TOTAL
        let me = this;
        if (me.PedidoForm.detallePedido.length > 0){
          let total = this.PedidoForm.detallePedido
            .map((item) => this.sumCantidadTotal(item))
            .reduce((prev,curr) => Number(prev) + new Number(curr), 0);
          me.PedidoForm.total = new Number(total).toFixed(2);
        }else{
          me.validForm = true;
          me.noPacientes = false;
          me.PedidoForm.total = 0.0;
        }
      },
      deep: true,
    },
  },
  computed: {},
  methods: {
    //metodo para limpiar simbolos del precio
    cleanSimbolsOfPrice(price) {
      if (String(price).indexOf("$") > -1) {
        price = price.replace("$", "");
        if (String(price).indexOf(",") > -1) {
          price = price.replace(/,/g, "");
        }
      }
      return price;
    },
    getItemsOfPacientesOfPedidos(){
        let me = this, pacientes = [];
        me.PedidoForm.detallePedido.map((detalle)=> 
        pacientes.push(detalle.paciente)
        );
        me.$refs.paciente.item = pacientes; 
    },
    onAddedItem(value){
        let me = this;
        if(value.length>0){
            me.formTitle = "Agregar items a Pedido";
        }
    },
    getPacienteFromChild(){
        let me = this;
        me.$refs.paciente.item.forEach(function(paciente){
            if(!me.containsObject(paciente,me.PedidoForm.detallePedido)){
                me.PedidoForm.detallePedido.push({
                    id : null,
                    cantidad: 1,
                    precio: paciente.precio,
                    paciente:paciente
                });
            }
        });
        me.modalPacientes= false;
    },
    containsObject(obj,list){
        return list.some(
            (item) => JSON.stringify(item.paciente) === JSON.stringify(obj)
        );
    },
    sumCantidadTotal(item){
      return new Number(new Number(item.cantidad)* 
      new Number(this.cleanSimbolsOfPrice(item.paciente.precio))).toFixed(2);
    },
    deletePacienteFromPedido(item){
      let me = this, index;
      index = me.PedidoForm.detallePedido.indexOf(item);
      me.PedidoForm.detallePedido.splice(index, 1);
    },
    verificarAccionDato(pedido, statusCode, accion) {
      let me = this;
      
      const Toast = me.$swal.mixin({
        toast: true,
        position: "bottom-end",
        showConfirmButton: true,
        timer: 3000,
      });
      if(statusCode == 200 && accion =="add"){
        me.$swal({
          position:"center",
          icon:"succes",
          title:"Pedido Registrado con Exito!",
          showConfirmButton: true,
          allowOutSideClick:false,
        }).then((result)=>{
          if(result.value){
            me.$router.push("/pedidos/listado")
          }
        });
        me.loader = false;
      }else if (statusCode == 200 && accion == 'upd'){
        me.$swal({
          position:"center",
          icon:"succes",
          title:"Pedido Actualizado con Exito!",
          showConfirmButton: true,
          allowOutSideClick:false,
        }).then((result)=>{
          if(result.value){
            me.$router.push("/pedidos/listado")
          }
        });
        me.loader = false;
      }else{
        me.loader= false;
        Toast.fire({
          type:"error",
          title: "Ocurrio un Error Intente Nuevamente"
        });
      }      
    },
    getUserAuth(){
      let user={"id":"1","nombre":"Usuario Fijo"}
      return user;
    },
    saveOrUpdatePedido(){
      let me = this;
      if(me.$refs.formPedido.validate()){
        if(me.PedidoForm.detallePedido.length==0){
          me.noPacientes= true;
          me.validForm = false;
        }else{
          me.loader = true;
          //setear el usuario
          me.PedidoForm.usuario = this.getUserAuth();
          //haciendo peticion para guardar al backend para guardar pedido
          me.$http.post(`${me.$url}/pedidos/save`,me.PedidoForm).then(function(response){
            me.verificarAccionDato(response.data,response.status,me.action);
          })
          .catch(function(error){
            me.loader= false;
            me.$swal("Error","Ocurrió un Error Intente de Nuevo","error");
            console.log(error);
          })
        }
      }
    },
    fetchClientes() {
      let me = this;
      me.loader = true;
      me.$http
        .get(`${me.$url}/clientes/all`)
        .then(function (response) {
          me.arrayClientes = response.data;
          me.loader = false;
        })
        .catch(function (error) {
          me.loader = false;
          console.log(error);
        });
    },
    fetchPedidosRecibidos() {
      let me = this,
        url = "";
      if (me.constructFecha() != null) {
        url;
        let fecha = me.mes + "-" + me.anio;
        return me.$moment(fecha, "DD-MM-YYYY").toDate();
      } else {
        return null;
      }
    },
    constructFecha() {
      let me = this;
      if (me.mes != null && me.anio != null) {
        let fecha = me.mes + "-" + me.anio;
        return me.$moment(fecha, "DD-MM-YYYY").toDate();
      } else {
        return null;
      }
    },
  },
  components:{
      PacienteList
  },  
  mounted() {
    let me = this;
    me.fetchPedidosRecibidos();
    if (!me.pedido) {
      me.$router.push("/pedidos/listado");
    } else {
      if (me.action == "add") {
        console.log("Registrar Nuevo Pedido");
        Object.assign(me.pedido, {
          id: null,
          fechaPedido: null,
          fechaDespacho: null,
          total: 0.0,
          estado: "R",
          cliente: null,
          usuario: null,
          detallePedido: [],
        });
      } else if (me.action == "upd") {
        console.log("Editar Pedido");
        Object.assign(me.PedidoForm, me.pedido);
      } else {
        console.log("Mostrar Detalle Pedido");
        Object.assign(me.PedidoForm, me.pedido);
      }
    }
    me.fetchClientes();
  },
};
</script>
