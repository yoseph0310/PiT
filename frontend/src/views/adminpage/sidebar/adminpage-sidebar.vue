<template>
  <el-menu
    :default-active="pathname"
    class="el-menu-vertical-demo"
    @open="handleOpen"
    @close="handleClose"
    active-text-color="#00C0D4"
    router="true"
  >
    <el-menu-item index="adminClass">
      <i class="el-icon-zoom-in"></i>
      <span>클래스 목록 조회</span>
    </el-menu-item>
  </el-menu>
</template>

<script>
export default {

  props: {
    pathname: String,
  },
  methods: {
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    }
  },

};
</script>

<style></style>
