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
          Listado de Tratamiento
          <div class="flex-grow-1"></div>
          <v-text-field
            v-model="searchTratamientos"
            append-icon="search"
            label="Buscar"
            single-line
            hide-details
          ></v-text-field>
        </v-card-title>
        <v-data-table
          :headers="hTBTratamientos"
          :items="arrayTratamientos"
          :footer-props="{
            'items-per-page-options': [5, 10, 15, 20, 30],
            'items-per-page-text': 'Registros por Página',
          }"
          :items-per-page="5"
          :search="searchTratamientos"
          multi-sort
          class="elevation-1"
        >
          <!-- Template para form modal para agregar o actualizar categorias -->
          <template v-slot:top>
            <v-toolbar flat color="white">
              <div class="flex-grow-1"></div>
              <v-dialog v-model="modalTratamiento" persistent max-width="700px">
                <template v-slot:activator="{ on }">
                  <v-btn
                    elevation="10"
                    color="black"
                    dark
                    class="mb-2"
                    v-on="on"
                  >
                    Agregar&nbsp;
                    <v-icon>library_add</v-icon>
                  </v-btn>
                </template>
                <v-card>
                  <v-card-title class="headline light-blue lighten-5" primary-titles>
                    <span class="headline" v-text="formTitle"></span>
                  </v-card-title>
                  <v-card-text>
                    <v-container>
                      <v-form
                        ref="formTratamiento"
                        v-model="validForm"
                        :lazy-validation="true"
                      >
                        <v-text-field
                          append-icon="medication"
                          v-model="tratamiento.nombre"
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
                    <v-btn color="#FF0000" text @click="cerrarModal"
                      >Cerrar</v-btn
                    >
                    <v-btn
                      color="info darken-1"
                      :disabled="!validForm"
                      @click="saveTratamiento()"
                      text
                      v-text="btnTitle"
                    ></v-btn>
                  </v-card-actions>
                </v-card>
              </v-dialog>
            </v-toolbar>
          </template>

          <!-- Template que va en la tabla en la columna de Acciones (Editar,Desactivar) -->
          <template v-slot:item.action="{ item }">
            <v-tooltip top>
              <template v-slot:activator="{ on }">
                <v-btn
                  color="#64DD17"
                  elevation="8"
                  small
                  dark
                  :disabled="item.id < 0"
                  v-on="on"
                  @click="showModalEditar(item)"
                >
                  <v-icon>create</v-icon>
                </v-btn>
              </template>
              <span>Actualizar Datos</span>
            </v-tooltip>
            <v-tooltip top>
              <template v-slot:activator="{ on }">
                <v-btn
                  color="#FF0000"
                  class="mx-1"
                  elevation="8"
                  small
                  dark
                  :disabled="item.id < 0"
                  v-on="on"
                  @click="deleteTratamiento(item)"
                >
                  <v-icon>delete</v-icon>
                </v-btn>
              </template>
              <span>Eliminar Registro</span>
            </v-tooltip>
          </template>
        </v-data-table>
        <v-snackbar v-model="snackbar">
          {{ msjSnackBar }}
          <v-btn color="red" text @click="snackbar = false">Cerrar</v-btn>
        </v-snackbar>
      </v-card>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      arrayTratamientos: [],
      hTBTratamientos: [
        { text: "Nombre", value: "nombre" },
        { text: "Acciones", value: "action", sortable: false, align: "center" },
      ],
      loader: false,
      searchTratamientos: "",
      modalTratamiento: false,
      tratamiento: {
        id: null,
        nombre: "",
      },
      validForm: true,
      snackbar: false,
      msjSnackBar: "",
      errorsNombre: [],
      editedTratamiento: -1,
    };
  },
  computed: {
    formTitle() {
      return this.tratamiento.id == null
        ? "Agregar tratamiento"
        : "Actualizar tratamiento";
    },
    btnTitle() {
      return this.tratamiento.id == null ? "Guardar" : "Actualizar";
    },
  },
  methods: {
    fetchTratamientos() {
      let me = this;
      me.loader = true;
      me.$http
        .get(`${me.$url}/tratamientos/all`)
        .then(function(response) {
          me.arrayTratamientos = response.data;
          me.loader = false;
        })
        .catch(function(error) {
          me.loader = false;
          console.log(error);
        });
    },
    cerrarModal() {
      let me = this;
      me.modalTratamiento = false;
      setTimeout(() => {
        me.tratamiento = {
          id: null,
          nombre: "",
        };
        me.resetValidation();
      }, 300);
    },
    resetValidation() {
      let me = this;
      me.errorsNombre = [];
      me.$refs.formTratamiento.resetValidation();
    },
    showModalEditar(cat) {
      let me = this;
      me.editedTratamiento = me.arrayTratamientos.indexOf(cat);
      me.tratamiento = Object.assign({}, cat);
      me.modalTratamiento = true;
    },
    saveTratamiento() {
      let me = this;
      if (me.$refs.formTratamiento.validate()) {
        let accion = me.tratamiento.id == null ? "add" : "upd";
        me.loader = true;
        me.$http
          .post(`${me.$url}/tratamientos/save`, me.tratamiento)
          .then(function(response) {
            me.verificarAccionDato(response.data, response.status, accion);
            me.cerrarModal();
          })
          .catch(function(error) {
            console.log(error);
            //409 Conflicts Error (Proveedor Ya Existente En la BD)
            if (error.response.status == 409) {
              me.setMessageToSnackBar("Tratamiento Ya Existe", true);
              me.errorsNombre = ["Nombre Del Tratamiento Existente"];
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
    deleteTratamiento(tratamiento) {
      let me = this;
      me.editedTratamiento = me.arrayTratamientos.indexOf(tratamiento);
       const Toast = me.$swal.mixin({
        toast: true,
        position: "bottom-end",
        showConfirmButton: true,
        timer: 3000
      });
      me.$swal({
        title: "Eliminar Tratamiento ?",
        text: "Una vez realizada la acción no se podra revertir ",
        icon: "question",
        showCancelButton: true,
        confirmButtonText: "Si",
        cancelButtonText: "No",
        reverseButtons: true,
        focusConfirm: false,
        focusCancel: true,
        showCloseButton: true
      }).then(result => {
        if (result.value) {
          me.loader = true;
          me.$http
            .post(`${me.$url}/tratamientos/delete`, tratamiento)
            .then(function(response) {
              me.verificarAccionDato(response.data, response.status, "del");
              me.loader = false;
            })
            .catch(function(error) {
              if(error.response.status  == 510){
                Toast.fire({
                icon: "error",
                title: "No se puede eliminar este tratamiento, tiene registros"
              });
              } 
              me.loader = false;
            });
        }
      });
    },
    verificarAccionDato(tratamiento, statusCode, accion) {
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
          me.arrayTratamientos.unshift(tratamiento);
          Toast.fire({
            icon: "success",
            title: "Tratamiento Registrada con Exito"
          });
          me.loader = false;
          break;

        case "upd":
          //Actualizo al array de categorias el objecto que devuelve el Backend ya con los datos actualizados
          Object.assign(me.arrayTratamientos[me.editedTratamiento], tratamiento);
          Toast.fire({
            icon: "success",
            title: "Tratamiento Actualizada con Exito"
          });
          me.loader = false;
          break;
        case "del":
          if (statusCode == 200) {
            try {
              //Se elimina del array de Categorias Activos si todo esta bien en el backend
              me.arrayTratamientos.splice(me.editedTratamiento, 1);
              //Se Lanza mensaje Final
              me.$swal.fire("Eliminado", "Tratamiento Eliminado", "success");
            } catch (error) {
              console.log(error);
            }
          } else {
            Toast.fire({
              icon: "error",
              title: "Ocurrio Un Error Intente Nuevamente"
            });
          }
          break;
      }
    }
  },
  mounted() {
    let me = this;
    me.fetchTratamientos();
  },
};
</script>