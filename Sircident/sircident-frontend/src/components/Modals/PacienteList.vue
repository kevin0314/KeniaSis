<template>
  <v-container>
    <v-row>
      <div class="flex-grow-1"></div>
      <v-text-field
        v-model="searchPacientes"
        label="Buscar Producto"
        hide-details
      />
      <v-col cols="12" md="12" sm="12" lg="12">
        <v-data-table
          :headers="hTBPacientes"
          :items.sync="arrayPacientes"
          :items-per-page="10"
          :footer-props="{
            'items-per-page-options': [5, 15, 20, 30],
          }"
          :search="searchPacientes"
          :single-select="false"
          show-select
          v-model="item"
          :dense="true"
          multi-sort
        >
          <!-- template para formatear los decimales del precio de venta -->
          <template v-slot:item.precio="{ item }">{{ item.precio }}</template>
        </v-data-table>
      </v-col>
    </v-row>
  </v-container>
</template>
<script>
export default {
  data: () => ({
    item: {},
    arrayPacientes: [],
    hTBPacientes: [
      {
        text: "Nombre",
        value: "nombre",
      },
      {
        text: "Descripción",
        value: "descripcion",
      },
      {
        text: "Precio",
        value: "precio",
      },
      {
        text: "Expediente",
        value: "expediente.nombre",
        align: "center",
      },
      {
        text: "Tratamiento",
        value: "tratamiento.nombre",
      },
    ],

    loader: false,
    loadData: false,
  }),
  watch: {
    item: function() {
      this.$emit("added", this.item);
    },
  },
  computed: {},
  methods: {
    fetchPacientes() {
      let me = this;
      return me.$http.get(`${me.$url}/pacientes/all`);
    },
    fetchData() {
      let me = this;
      me.loader = true;
      me.$http
        .all([me.fetchPacientes()])
        .then(
          me.$http.spread((pacientes) => {
            me.arrayPacientes = pacientes.data;
            me.loader = false;
          })
        )
        .catch(function(error) {
          console.log(error);
          me.loader = false;
          me.$swal("Error", "Ocurrio Un Error Intente Nuevamente", "error");
        });
    },
  },
  mounted() {
    let me = this;
    me.fetchData();
    me.$emit("created");
  },
};
</script>