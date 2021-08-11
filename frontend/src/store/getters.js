// 플랫폼 관련 정보 가져오기
export function getIsDesktopPlatform(state) {
  return state.isDesktopPlatform;
}
// 메뉴 객체 가져오기
export function getMenus(state) {
  return state.menus;
}
// Active된 메뉴 인덱스 가져오기
export function getActiveMenuIndex(state) {
  const keys = Object.keys(state.menus);
  return keys.findIndex(item => item === state.activeMenu);
}

export function getIsLogined(state) {
  //console.log("fdf" + state.isLogined)
  return state.isLogined;
}

export function getProfileUrl(state) {
  return state.profileUrl;
}

export function getUserType(state) {
  console.log("getter 사용시 UserType : " + state.userType);
  return state.userType;
}
