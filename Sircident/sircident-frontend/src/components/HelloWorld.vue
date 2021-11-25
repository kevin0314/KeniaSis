<template>
  <v-carousel
    cycle
    height="464"
    hide-delimiter-background
    show-arrows-on-hover
  >
    <template v-slot:prev="{ on, attrs }">
      <v-btn
        color="black"
        v-bind="attrs"
        v-on="on"
      >Anterior</v-btn>
    </template>
    <template v-slot:next="{ on, attrs }">
      <v-btn
        color="black"
        v-bind="attrs"
        v-on="on"
      >Siguiente</v-btn>
    </template>
    <v-carousel-item
      v-for="(slide, i) in slides"
      :key="i"
    >
      <v-sheet
        :color="colors[white]"
        height="100%"
      >
        <v-row
          class="fill-height"
          align="center"
          justify="center"
        >
          <div class="font-weight-thin text-h2 " >
            {{ slide }} 
          </div>
        </v-row>
      </v-sheet>
    </v-carousel-item>
  </v-carousel>
</template>
<script>
  export default {
    data () {
      return {
        colors: [
          'black',
          'black',
          'black',
          'black',
          'black',
        ],
        slides: [
          'Bienvenido a SIRCIDENT',
          'A tu sitio web preferido',
          'Donde podrás hacer ordenes',
          'De tus productos lácteos preferidos',
          'Fácil y rápido'
        ],
      }
    },
  }
</script>