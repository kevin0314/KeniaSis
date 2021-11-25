<template>
  <v-app id="inspire">
    <v-navigation-drawer
      v-model="drawer"
      color="#E3F2FD"
      :clipped="$vuetify.breakpoint.lgAndUp"
      app
    >
      <v-list dense>
        <template>
          <v-list-item link :to="{ path: '/' }">
            <v-list-item-action>
              <v-icon>home</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title>Inicio</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </template>
        <v-list-group>
          <!-- menu desplegable -->
          <template slot="activator">
            <v-list-item-action>
              <v-icon>menu</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title>Herramientas</v-list-item-title>
            </v-list-item-content>
          </template>
          <v-list-item link :to="{ name: 'tratamientos' }">
            <!-- menu de categorias-->
            <v-list-item-action>
              <v-icon>list_alt</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title>Tratamientos</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
          <v-list-item link :to="{ name: 'expedientes' }">
            <!-- menu de marcas-->
            <v-list-item-action>
              <v-icon>folder_shared</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title>Expedientes</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
           <v-list-item link :to="{name: 'clientes'}"> 
            <v-list-item-action>
              <v-icon>group_add</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title>Clientes</v-list-item-title>
            </v-list-item-content>
          </v-list-item>   
          <v-list-item link :to="{ name: 'pacientes' }">
            <!-- menu de productos-->
            <v-list-item-action>
              <v-icon>badge</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title>Pacientes</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        <template>
          <v-list-item link :to="{ name: 'pedidos.listado' }">
            <v-list-item-action>
              <v-icon>description</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title>Reportes</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </template>
        </v-list-group>
      </v-list>
    </v-navigation-drawer>

    <v-app-bar
      :clipped-left="$vuetify.breakpoint.lgAndUp"
      app
      color="light-blue lighten-1"
      dark
    >
      <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
      <v-toolbar-title style="width: 300px" class="ml-0 pl-4" color="light-blue lighten-1">
        <span class="hidden-sm-and-down">SIRCIDENT</span>
      </v-toolbar-title>
    </v-app-bar>
    <v-main>
      <v-container class="fill-height" fluid>
        <v-layout>
          <v-flex>
            <!--colocar el componente -->
            <router-view></router-view>
          </v-flex>
        </v-layout>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
export default {
  name: "App",
  props: {
    source: String,
  },
  data: () => ({
    drawer: null,
  }),
};
</script>