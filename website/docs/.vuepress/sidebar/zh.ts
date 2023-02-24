import { sidebar } from "vuepress-theme-hope";

export const zhSidebar = sidebar({
  "/": [
    "",
    {
      icon: "creative",
      text: "模块介绍",
      prefix: "info/",
      link: "info/",
      children: "structure",
    },
    {
      icon: "creative",
      text: "快速入门",
      prefix: "use/",
      link: "use/",
      children: "structure",
    },
    {
      icon: "creative",
      text: "模块功能",
      prefix: "guide/",
      link: "guide/",
      collapsible: true,
      children: "structure",
    },
    {
      icon: "creative",
      text: "反馈流程",
      prefix: "feedback/",
      link: "feedback/",
      children: "structure",
    },
    {
      icon: "creative",
      text: "捐赠渠道",
      prefix: "donate/",
      link: "donate/",
      children: "structure",
    },
    // {
    //   icon: "creative",
    //   text: "文档案例",
    //   prefix: "help/",
    //   link: "help/",
    //   collapsible: true,
    //   children: "structure",
    // },
  ],
});
