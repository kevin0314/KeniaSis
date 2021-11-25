<template>
  <div class="content">
    <div
      class="md-layout-item md-medium-size-100 md-xsmall-size-100 md-size-100"
    >
      <v-overlay :value="loader" :z-index="'99999999'">
        <v-progress-circular
          indeterminate
          size="80"
          color="#E3F2FD"
        ></v-progress-circular>
      </v-overlay>
      <v-card>
        <v-card-title>
          Pedidos recibidos
          <div class="flex-grow-1"></div>
          <v-text-field
            v-model="searchPedido"
            append-icon="search"
            label="Buscar"
            single-line
            hide-details
          ></v-text-field>
        </v-card-title>
        <v-data-table
          :headers="hTBPedidos"
          :items="arrayPedidos"
          :footer-props="{
            'items-per-page-options': [10, 20, 30, 40],
            'items-per-page-text': 'Registros por Página',
          }"
          :items-per-page="10"
          :search="searchPedido"
          multi-sort
          class="elevation-1"
        >
          <template v-slot:top>
            <v-toolbar flat color="white">
              <div class="flex-grow-1"></div>
              <v-btn
                elevation="10"
                :to="{
                  name: 'pedidos.form',
                  params: { pedido: {}, action: 'add' },
                }"
                color="black"
                dark
                class="mb-2 white--text"
              >
                Nuevo Pedido&nbsp;
                <v-icon>library_add</v-icon>
              </v-btn>
            </v-toolbar>
          </template>
          <!-- Template para dar formato a la variable total -->
          <template v-slot:item.total="{ item }">{{ item.total }}</template>

          <!-- Template que va en la tabla en la columna de Acciones (Editar,Desactivar) -->
          <template v-slot:item.action="{ item }">
            <v-tooltip top>
              <template v-slot:activator="{ on }">
                <v-btn
                  elevation="10"
                  color="secundary"
                  :to="{
                    name: 'pedidos.form',
                    params: { pedido: item, action: 'detail' },
                  }"
                  class="white--text mx-1"
                  small
                  dark
                  :disabled="item.id < 0"
                  v-on="on"
                >
                  <v-icon>pageview</v-icon>
                </v-btn>
              </template>
              <span>Ver detalles</span>
            </v-tooltip>
            <v-tooltip top>
              <template v-slot:activator="{ on }">
                <v-btn
                  elevation="8"
                  color="#64DD17"
                  :to="{
                    name: 'pedidos.form',
                    params: { pedido: item, action: 'upd' },
                  }"
                  class="white--text mx-1"
                  small
                  dark
                  :disabled="item.id < 0"
                  v-on="on"
                >
                  <v-icon>edit</v-icon>
                </v-btn>
              </template>
              <span>Editar Pedido</span>
            </v-tooltip>
            <v-tooltip top>
              <template v-slot:activator="{ on }">
                <v-btn
                  elevation="10"
                  color="info"
                  class="white--text mx-1"
                  small
                  v-on="on"
                  @click="CambiarEstado(item, 'D')"
                >
                  <v-icon>done</v-icon>
                </v-btn>
              </template>
              <span>Despachar pedido</span>
            </v-tooltip>
            <v-tooltip top>
              <template v-slot:activator="{ on }">
                <v-btn
                  elevation="10"
                  color="#FF0000"
                  class="white--text mx-1"
                  small
                  v-on="on"
                  @click="CambiarEstado(item, 'A')"
                >
                  <v-icon>delete</v-icon>
                </v-btn>
              </template>
              <span>Anular pedido</span>
            </v-tooltip>
          </template>
        </v-data-table>
      </v-card>
    </div>
  </div>
</template>

<script>
export default {
  name: "ListadoPedidos",
  data: () => ({
    arrayPedidos: [],
    hTBPedidos: [
      { text: "Fecha Pedido", value: "fechaPedido" },
      { text: "Cliente", value: "cliente.nombre" },
      { text: "Total", value: "total" },
      { text: "Acciones", value: "action", sortable: false, align: "center" },
    ],
    loader: false,
    searchPedido: "",
    detallePedido: -1,
    mes: null,
    anio: null,
  }),
  methods: {
    fetchPedidosRecibidos() {
      let me = this,
        url = "";
      if (me.constructFecha() != null) {
        url = `${me.$url}/pedidos/all?fecha=${me.ConstructFecha()}`;
      } else {
        url = `${me.$url}/pedidos/all`;
      }
      me.loader = true;
      me.$http
        .get(url)
        .then(function(response) {
          console.log(response.data);
          me.arrayPedidos = response.data;
          me.loader = false;
        })
        .catch(function(error) {
          console.log(error);
          me.loader = false;
        });
    },
    constructFecha() {
      let me = this;
      if ((me.mes != null) & (me.anio != null)) {
        let fecha = me.mes + "-" + me.anio;
        return me.$moment(fecha, "DD-MM-YYY").toDate();
      } else {
        return null;
      }
    },
    CambiarEstado(pedido, accion) {
      let me = this;
      me.deletedPedido = me.arrayPedidos.indexOf(pedido);
      let msj =
        accion == "D"
          ? "Esta seguro(a) de cambiar a despachado el pedido"
          : "Esta seguro(a) de anular el pedido";
      me.$swal({
        title: "Confirmación de operación",
        text: msj,
        type: "question",
        showCancelButton: true,
        confirmButtonText: "Si",
        cancelButtonText: "No",
        reverseButtons: true,
        focusConfirm: true,
        focusCancel: false,
        showCloseButton: true,
      }).then((result) => {
        if (result.value) {
          me.loader = true;
          me.$http
            .post(`${me.$url}/pedidos/change-state?estado=${accion}`, pedido)
            .then(function(response) {
              me.verificarAccionDato(response.data, response.status, accion);
              me.loader = false;
            })
            .catch(function(error) {
              console.log(error);
              me.loader = false;
            });
        }
      });
    },
    verificarAccionDato(pedido, statusCode, accion) {
      let me = this;
      const Toast = me.$swal.mixin({
        toast: true,
        position: "bottom-end",
        showConfirmButton: true,
        timer: 3000,
      });

      if(accion=="D") {
          Toast.fire({
            icon: "success",
            title: "Pedido registrado como Despachado",
          });
      }else if(accion=="A"){
        Toast.fire({
            icon: "success",
            title: "Pedido anulado...!!!",
        });
      }else{
        Toast.fire({
            icon: "error",
            title: "Ocurrio un error intente nuevamente",
        });
      }
      if(statusCode == 200){
        me.arrayPedidos.splice(me.deletedPedido, 1);
      }

    }
 
  },
  mounted() {
    let me = this;
    me.fetchPedidosRecibidos();
  },
};
</script>