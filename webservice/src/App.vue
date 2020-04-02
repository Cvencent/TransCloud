<template>
  <div id="app">
    <router-view  v-if="isRouterAlive"/>
  </div>
</template>


<script>
axios.defaults.withCredentials = true;
export default {
  name: 'App',
  provide(){
    return{
      reload:this.reload
    }
  },
  data(){
    return{
      isRouterAlive:true
    }
  },

  methods: {
    reload (){
       this.isRouterAlive = false
       this.$nextTick(function(){
          this.isRouterAlive = true
       })
    }
  }

}
</script>


<style>

</style>
