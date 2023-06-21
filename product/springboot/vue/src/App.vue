<template>
  <div id="app">
    <router-view/>
  </div>
</template>
<style>
.el-dialog {
  display: flex;
  flex-direction: column;
  margin: 0 !important;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.el-dialog .el-dialog__body {
  flex: 1;
  overflow: auto;
}
</style>