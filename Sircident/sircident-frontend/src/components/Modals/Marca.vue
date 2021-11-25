<template>
  <v-row justify="center">
    <v-dialog v-model="dialog" persistent max-width="700px">
      <v-card>
        <v-card-title class="headline blue lighten-5" primary-titles>
          <span class="headline" v-text="'Agregar Marca'"></span>
        </v-card-title>
        <v-progress-linear
          indeterminate
          v-if="loader" 
          color="yellow darken-2" 
        >
        </v-progress-linear>
        <v-card-text>
          <v-container>
            <v-form
              ref="formMarca"
              v-model="validForm"
              :lazy-validation="true"
            >
              <v-text-field
                append-icon="folder_open"
                v-model="marca.nombre"
                @keyup="errorsNombre = []"
                :rules="[(v) => !!v || 'Nombre Es Requerido']"
                label="Nombre"
                required
                :error-messages="errorsNombre"
              ></v-text-field>
            </v-form>
          </v-container>
        </v-card-text>
        <v-divider></v-divider>
        <v-card-actions>
          <div class="flex-grow-1"></div>
          <v-btn color="blue darken-1" text @click="cerrarModal">Cerrar</v-btn>
          <v-btn
            color="info darken-1"
            :disabled="!validForm"
            @click="saveMarca()"
            text
            v-text="'Guardar'"
          ></v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>
<script>
export default {
  data() {
    return {
      dialog:false,  
      loader: false,
      marca: {
        id: null,
        nombre: "",
      },
      validForm: true,
      snackbar: false,
      msjSnackBar: "",
      errorsNombre: []
    };
  },
  methods: {
    cerrarModal() {
      let me = this;
      me.dialog = false;
      setTimeout(() => {
        me.marca = {
          id: null,
          nombre: "",
        };
        me.resetValidation();
      }, 300);
    },
    resetValidation() {
      let me = this;
      me.errorsNombre = [];
      me.$refs.formMarca.resetValidation();
    },
    saveMarca() {
      let me = this;
      if (me.$refs.formMarca.validate()) {
        let accion = me.marca.id == null ? "add" : "upd";
        me.loader = true;
        me.$http
          .post(`${me.$url}/expedientes/save`, me.marca)
          .then(function(response) {
            me.verificarAccionDato(response.data, response.status, accion);
            me.cerrarModal();
          })
          .catch(function(error) {
            console.log(error);
            //409 Conflicts Error (Proveedor Ya Existente En la BD)
            if (error.response.status == 409) {
              me.setMessageToSnackBar("Marca Ya Existe", true);
              me.errorsNombre = ["Nombre de Marca Existente"];
            } else {
              me.$swal("Error", "Ocurrio Un Error Intente Nuevamente", "error");
            }
            me.loader = false;
          });
      }
    },
    setMessageToSnackBar(msj, estado) {
      let me = this;
      me.snackbar = estado;
      me.msjSnackBar = msj;
    },
    verificarAccionDato(marca, statusCode, accion) {
      let me = this;
      
      const Toast = me.$swal.mixin({
        toast: true,
        position: "bottom-end",
        showConfirmButton: true,
        timer: 3000
      });

      switch (accion) {
        case "add":
          //Agrego al array de categorias el objecto que devuelve el Backend
          this.$emit("saved",marca);
          Toast.fire({
            icon: "success",
            title: "Marca Registrada con Exito"
          });
          me.loader = false;
          break;
 
      }
    }
  },
};
</script>