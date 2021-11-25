<template>
  <v-container>
    <v-row>
      <div class="flex-grow-1"></div>
      <v-text-field
        v-model="searchProductos"
        label="Buscar Producto"
        hide-details
      />
      <v-col cols="12" md="12" sm="12" lg="12">
        <v-data-table
          :headers="hTBProductos"
          :items.sync="arrayProductos"
          :items-per-page="10"
          :footer-props="{
            'items-per-page-options': [5, 15, 20, 30],
          }"
          :search="searchProductos"
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
    arrayProductos: [],
    hTBProductos: [
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
        text: "Marca",
        value: "marca.nombre",
        align: "center",
      },
      {
        text: "Categoria",
        value: "categoria.nombre",
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
    fetchProductos() {
      let me = this;
      return me.$http.get(`${me.$url}/productos/all`);
    },
    fetchData() {
      let me = this;
      me.loader = true;
      me.$http
        .all([me.fetchProductos()])
        .then(
          me.$http.spread((productos) => {
            me.arrayProductos = productos.data;
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
