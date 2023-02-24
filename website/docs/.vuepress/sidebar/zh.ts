import { sidebar } from "vuepress-theme-hope";

export const zhSidebar = sidebar({
  "/": [
    "",
    {
      icon: "discover",
      text: "案例",
      prefix: "help/demo/",
      link: "help/demo/",
      children: "structure",
    },
    {
      text: "文档",
      icon: "note",
      prefix: "help/guide/",
      children: "structure",
    },
    "help/slides",
  ],
});
