<template>
  <div class="content-wrapper">
    <div class="submenu-title">찜한 클래스</div>
    <div v-if="state.list.length == 0">찜한 클래스가 없어요!</div>
    <div class="class-card-wrapper">
      <el-card
        class="class-card wrap"
        shadow="none"
        v-for="classItem in state.list"
        :key="classItem"
        :body-style="{
          padding: '0px',
          height: '400px',
          width: '300px'
        }"
        style="margin: 5px"
      >
        <i class="el-icon-close delete" @click="clickDeleteClassLikes"></i>
        <!-- <span class="delete" @click="testDelete">삭제버튼</span> -->
        <router-link
          :to="`/classdetail?classNo=${classItem.classNo}`"
          style="text-decoration: none; color: inherit;"
        >
          <el-image
            id="#elImage"
            :src="classItem.classThumbnail"
            fit="cover"
            style="width: 300px; height: 200px;"
          />
          <div class="class-card-content">
            <div>
              <div style="height: 80px;">
                <div>
                  {{ classItem.classTeacherName }}
                </div>
                <div class="favorite-title">{{ classItem.classTitle }}</div>
              </div>
              <div class="class-card-tag">
                <el-tag size="mini" color="#BEEDED">
                  {{ classItem.classStartDate }} ~
                  {{ classItem.classEndDate }}
                </el-tag>
              </div>
            </div>
            <div class="class-card-content-bottom">
              월 {{ classItem.classPrice }}원
            </div>
          </div>
        </router-link>
      </el-card>
    </div>
  </div>
</template>

<script>
import { useStore } from "vuex";
import { reactive } from "@vue/reactivity";
import { onMounted } from "@vue/runtime-core";

export default {
  name: "FavoriteClass",
  setup() {
    const store = useStore();
    const state = reactive({
      list: []
    });
    onMounted(() => {
      getClassLikesList();
    });

    const clickDeleteClassLikes = function(event) {
      let classTitle = event.target.parentElement.parentElement.querySelector(
        ".title"
      ).innerText;
      console.log("click classTitle : " + classTitle);

      let classNo;
      for (let i = 0; i < state.list.length; i++) {
        if (classTitle == state.list[i].classTitle) {
          classNo = state.list[i].classNo;
        }
      }
      console.log("click classNo : " + classNo);

      store
        .dispatch("root/deleteClassLikes", {
          classNo
        })
        .then(function() {
          alert("찜 목록에서 삭제되었습니다.");
          getClassLikesList();
        })
        .catch(function(err) {
          alert(err.response.data.message);
        });
      event.stopPropagation();
    };

    // 찜한 클래스 목록 조회하기 함수
    const getClassLikesList = function() {
      store
        .dispatch("root/getClassLikesList")
        .then(function(result) {
          state.list = result.data;
          // store.commit("root/setUserLikesClassList", result.data);
          // for (var i = 0; i < state.list.length; i++) {
          //   var startMonth = parseInt(
          //     result.data[i].classStartDate.split("-")[1]
          //   );
          //   var endMonth = parseInt(result.data[i].classEndDate.split("-")[1]);
          //   result.data[i].classPrice = Math.ceil(
          //     result.data[i].classPrice / (endMonth - startMonth + 1)
          //   );
          // }

          for (var i = 0; i < state.list.length; i++) {
            // 월단위 가격 계산
            var startMonth = parseInt(
              result.data[i].classStartDate.split("-")[1]
            );
            var endMonth = parseInt(result.data[i].classEndDate.split("-")[1]);
            var tmp = (
              Math.floor(
                result.data[i].classPrice / (endMonth - startMonth + 1) / 100
              ) * 100
            ).toString();

            var left = tmp.slice(0, -3);
            var right = tmp.slice(-3, tmp.length);

            result.data[i].classPrice = left + "," + right;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    };

    return {
      state,
      getClassLikesList,
      clickDeleteClassLikes
    };
  }
};
</script>
<style>
.delete {
  position: absolute;
  top: 10px;
  right: 10px;
  z-index: 100;
  font-size: 30px;
  font-weight: bold;
  opacity: 0;
  cursor: pointer;
}
.wrap {
  position: relative;
}
.wrap:hover .delete {
  opacity: 1;
}
.favorite-title{
  font-weight: bold;
  font-size: 18px;
  word-break: break-all;
  margin-top: 6px;
}
</style>
