<template>
  <div>
    <div class="content-wrapper" v-loading="state.loading">
      <div class="classList" v-if="!state.selectedClassNo">
        <div class="submenu-title">녹화된 영상</div>
        <div class="recordedvideo-card-section">
          <el-table
            id="recordedvideo-card-content"
            :data="state.classList"
            style="width: 100%; font-size: 17px;"
            @row-click="mvVideoList"
          >
            <el-table-column prop="classTitle" label="클래스명">
            </el-table-column>
            <el-table-column
              prop="classTeacherName"
              label="강사명"
              fixed="right"
              width="200px"
            >
            </el-table-column>
          </el-table>
        </div>
      </div>
      <div class="videoList" v-else>
        <el-page-header @back="goBack" :content="state.selectedTitle">
        </el-page-header>
        <videos :classNo="state.selectedClassNo" />
        <!-- <router-link to="/video?classNo=${{state.selectedClassNo}}" /> -->
      </div>
    </div>
  </div>
</template>

<script>
import "video.js/dist/video-js.min.css";
import "video.js/dist/video.min.js";
import videos from "./components/videos.vue";
import { reactive } from "@vue/reactivity";
import { useStore } from "vuex";
import { onMounted } from "vue";
import { useRouter } from "vue-router";
import { eventNames } from 'process';

export default {
  name: "recordedVideoTest",
  components: {
    videos
  },
  setup() {
    const store = useStore();
    const router = useRouter();

    const state = reactive({
      selectedClassNo: null,
      selectedTitle: null,
      classList: [],
      loading: true
    });

    onMounted(() => {
      // 수강중 클래스
      store
        .dispatch("root/getRegisterClassList")
        .then(function(result) {
          state.classList = result.data;
        })
        .catch(function(err) {
          console.log(err);
        });

      // 수강완료 클래스
      store
        .dispatch("root/getFinishedClassList")
        .then(function(result) {
          for (var i = 0; i < result.data.length; i++)
            state.classList.push(result.data[i]);
        })
        .catch(function(err) {
          //alert(err.response);
          console.log(err);
        });

      state.loading = false;
    });

    const mvVideoList = function(prop) {
      //console.log(prop.classid);
      state.selectedClassNo = prop.classNo;
      state.selectedTitle = prop.classTitle;
      //router.push("/video?classNo=" + state.selectedClassNo);
    };

    const goBack = function() {
      state.selectedClassNo = null;
    };

    window.addEventListener('focusout', event => {
      console.log(event)
    })

    return { mvVideoList, goBack, state };
  }
};
</script>

<style>
.recordedvideo-card-section {
  display: flex;
  flex-wrap: wrap;
}

.finishedclass-card-content {
  height: 170px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 10px;
}

.finishedclass-card-content-bottom {
  display: flex;
  flex-direction: row;
  justify-content: center;
}
</style>
