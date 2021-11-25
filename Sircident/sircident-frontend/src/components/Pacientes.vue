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
          Listado de Pacientes
          <div class="flex-grow-1"></div>
          <v-text-field
            v-model="searchPacientes"
            append-icon="search"
            label="Buscar"
            single-line
            hide-details
          ></v-text-field>
        </v-card-title>
        <v-data-table
          :headers="hTBPacientes"
          :items="arrayPacientes"
          :footer-props="{
            'items-per-page-options': [10, 15, 20, 30],
            'items-per-page-text': 'Registros por Página',
          }"
          :items-per-page="10"
          :search="searchPacientes"
          multi-sort
          class="elevation-1"
        >
          <!-- Template para form modal para agregar o actualizar categorias -->
          <template v-slot:top>
            <v-toolbar flat color="white">
              <div class="flex-grow-1"></div>
              <v-dialog v-model="modalPaciente" persistent max-width="700px">
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
                  <v-card-title class="headline blue lighten-5" primary-titles>
                    <span class="headline" v-text="formTitle"></span>
                  </v-card-title>
                  <v-card-text>
                    <v-container>
                      <v-form
                        ref="formPaciente"
                        v-model="validForm"
                        :lazy-validation="true"
                      >
                        <v-text-field
                          append-icon="drive_file_rename_outline"
                          v-model="paciente.nombre"
                          @keyup="errorsNombre = []"
                          :rules="[(v) => !!v || 'Nombre Es Requerido']"
                          label="Nombre"
                          required
                          :error-messages="errorsNombre"
                        ></v-text-field>
                        <v-textarea
                          v-model="paciente.descripcion"
                          label="Descripcion"
                          rows="2"
                          required
                          :error-messages="errorsNombre" 
                        ></v-textarea>
                        <v-row>
                          <v-col cols="12" >
                            <v-text-field
                              append-icon="local_atm"
                              v-model="paciente.precio"
                              @keyup="errorsNombre = []"
                              :rules="[(v) => !!v || 'Precio Es Requerido']"
                              label="Precio"
                              required
                            > 
                            </v-text-field>
                          </v-col>
                        </v-row>
                        <v-row>
                          <v-col cols="12" md="5">
                            <v-autocomplete
                              v-model="paciente.expediente"
                              :items="arrayExpedientes"
                              :rules="[(v) => !!v || 'Expediente es requerido']"
                              required
                              label="Expediente"
                              item-text="nombre"
                              item-value="id"
                              return-object
                              clearable
                              :menu-props="{ closeOnClick: true }"
                            >
                            </v-autocomplete>
                          </v-col>
                          <v-col cols="12" md="1">
                            <Expediente @saved="onSavedExpediente"
                            ref="expediente"></Expediente>
                            <v-btn 
                            elevation="10"
                            class="mt-4"
                            text
                            icon
                            color="primary"
                            @click="openModalExpediente()"
                            dark
                            >
                              <v-icon>library_add</v-icon>
                            </v-btn>
                          </v-col>
                          <v-col cols="12" md="5">
                            <v-autocomplete
                              v-model="paciente.tratamiento"
                              :items="arrayTratamientos"
                              :rules="[(v) => !!v || 'Categoria es requerido']"
                              required
                              label="Tratamiento"
                              item-text="nombre"
                              item-value="id"
                              return-object
                              clearable
                              :menu-props="{ closeOnClick: true }"
                            >
                            </v-autocomplete>
                          </v-col>
                          <v-col cols="12" md="1">
                            <Tratamiento @saved="onSavedTratamiento"
                            ref="tratamiento"></Tratamiento>
                            <v-btn 
                            elevation="10"
                            class="mt-4"
                            text
                            icon
                            color="primary"
                            @click="openModalTratamiento()"
                            dark
                            >
                              <v-icon>library_add</v-icon>
                            </v-btn>
                          </v-col>
                        </v-row>
                        <v-row>

                        </v-row>
                        <!--Mensaje mostrado cuando se repita un producto-->
                        <v-col cols="12" md="12" v-if="alert">
                          <v-alert
                            :value="alert"
                            type="error"
                            border="left"
                            transition="scale-transition"
                            dismissible
                            elevation="2"
                          >
                            Los datos de producto a ingresar ya existen en la 
                            base de datos
                          </v-alert>
                        </v-col>
                      </v-form>
                    </v-container>
                  </v-card-text>
                  <v-divider></v-divider>
                  <v-card-actions>
                    <div class="flex-grow-1"></div>
                    <v-btn color="red darken-1" text @click="cerrarModal"
                      >Cerrar</v-btn
                    >
                    <v-btn
                      color="info darken-1"
                      :disabled="!validForm"
                      @click="savePaciente()"
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
                  @click="deletePaciente(item)"
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

import Tratamiento from "@/components/Modals/Tratamiento.vue";
import Expediente from "@/components/Modals/Expediente.vue";

export default {
    name:"Pacientes",
    data: ()=> ({ 
      arrayPacientes:[],
      arrayExpedientes:[],
      arrayTratamientos: [],
       hTBPacientes: [
        { text: "Nombre", value: "nombre" },
        { text: "Descripcion", value: "descripcion" },
        { text: "Precio", value: "precio" },
        { text: "Expediente", value: "expediente.nombre" },
        { text: "Tratamiento", value: "tratamiento.nombre" },
        { text: "Acciones", value: "action", sortable: false, align: "center" },
      ],
      loader: false,
      searchPacientes: "",
      modalPaciente: false,
      paciente: {
        id: null,
        nombre: "",
        descripcion: "",
        precio: "",
        tratamiento:null
      },
      imagenFile:[],
      imagenPacienteName:"",
      validForm: true,
      snackbar: false,
      msjSnackBar: "",
      errorsNombre: [],
      editedPaciente: -1,
      alert:false  
  }),
  computed: {
    formTitle() {
      return this.paciente.id == null
        ? "Agregar Producto"
        : "Actualizar Producto";
    },
    btnTitle() {
      return this.paciente.id == null ? "Guardar" : "Actualizar";
    },
  },
  methods: {
    fetchPacientes() {
      let me = this;
      me.loader = true;
      me.$http
        .get(`${me.$url}/pacientes/all`)
        .then(function(response) {
            console.log(response.data);
          me.arrayPacientes = response.data;
          me.loader = false;
        })
        .catch(function(error) {
          me.loader = false;
          console.log(error);
        });
    },
    cerrarModal() {
       let me = this;
      me.modalPaciente = false;
      setTimeout(() => {
        me.paciente = {
        id: null,
        nombre: "",
        descripcion: "",
        precio: "",
        tratamiento:null
        };
        me.resetValidation();
      }, 300);
    },
    resetValidation() {
      let me = this;
      me.errorsNombre = [];
      me.$refs.formPaciente.resetValidation();
    },
    fetchExpedientes() {
      let me = this;
      return me.$http.get(`${me.$url}/expedientes/all`);
    },


    fetchTratamientos(){
      let me = this;
      return me.$http.get(`${me.$url}/tratamientos/all`);
    },
    fetchData() {
      let me = this;
      me.loader = true;
      me.$http
        .all([me.fetchExpedientes(), me.fetchTratamientos()])
        .then(
          me.$http.spread((expedientes, tratamientos) => {
            me.arrayExpedientes = expedientes.data;
            me.arrayTratamientos = tratamientos.data;
            me.loader = false;
          })
        )
        .catch(function(error) {
          console.log(error);
          me.loader = false;
          me.$swal("Error", "Ocurrio un error intente nuevamente", "error");
        });      
    },
    showModalEditar(paciente) {
       let me = this;
      me.editedPaciente = me.arrayPacientes.indexOf(paciente);
      me.paciente = Object.assign({}, paciente);
      me.modalPaciente = true;
    },

   savePaciente() {
      let me = this;
      if (me.$refs.formPaciente.validate()) {
        let accion = me.paciente.id == null ? "add" : "upd";
        me.loader = true;
        me.$http
          .post(`${me.$url}/pacientes/save`, me.paciente)
          .then(function(response) {
            me.verificarAccionDato(response.data, response.status, accion);
            me.cerrarModal();
          })
          .catch(function(error) {
            console.log(error);
            //409 Conflicts Error (Proveedor Ya Existente En la BD)
            if (error.response.status == 409) {
              me.setMessageToSnackBar("Producto Ya Existe", true);
              me.errorsNombre = ["Nombre De Producto Existente"];
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
    deletePaciente(paciente) {
      let me = this;
      me.editedPaciente = me.arrayPacientes.indexOf(paciente);
       const Toast = me.$swal.mixin({
        toast: true,
        position: "bottom-end",
        showConfirmButton: true,
        timer: 3000
      });
      me.$swal({
        title: "Eliminar Producto?",
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
            .post(`${me.$url}/pacientes/delete`, paciente)
            .then(function(response) {
              me.verificarAccionDato(response.data, response.status, "del");
              me.loader = false;
            })
            .catch(function(error) {
              if(error.response.status  == 510){
                Toast.fire({
                icon: "error",
                title: "No se puede eliminar esta categoria, tiene productos registrados"
              });
              } 
              me.loader = false;
            });
        }
      });
    },
    verificarAccionDato(paciente, statusCode, accion) {
      let me = this;
      
      const Toast = me.$swal.mixin({
        toast: true,
        position: "bottom-end",
        showConfirmButton: true,
        timer: 3000
      });

      switch (accion) {
        case "add":
          //Agrego al array de Productos el objecto que devuelve el Backend
          me.arrayPacientes.unshift(paciente);
          Toast.fire({
            icon: "success",
            title: "Producto Registrado con Exito"
          });
          me.loader = false;
          break;

        case "upd":
      
          //Actualizo al array de categorias el objecto que devuelve el Backend ya con los datos actualizados
          Object.assign(me.arrayPacientes[me.editedPaciente], paciente);
          Toast.fire({
            icon: "success",
            title: "Producto Actualizado con Exito"
          });
          me.loader = false;
          break;
        case "del":
          if (statusCode == 200) {
            try {
              //Se elimina del array de Productos Activos si todo esta bien en el backend
              me.arrayPacientes.splice(me.editedPaciente, 1);
              //Se Lanza mensaje Final
              me.$swal.fire("Eliminado", "Registro de producto Eliminado", "success");
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
    },
    openModalTratamiento(){
      this.$refs.tratamiento.dialog=true;
    },
    onSavedTratamiento(value){
      let me = this;
      me.arrayTratamientos.unshift(value);
    },
    openModalExpediente(){
      this.$refs.expediente.dialog=true;
    },
    onSavedExpediente(value){
      let me = this;
      me.arrayExpedientes.unshift(value);
    }
  },
  components:{
    Tratamiento,
    Expediente
  },
  
  mounted() {
    let me = this;
    me.fetchPacientes();
    me.fetchData();
  },

};
</script>