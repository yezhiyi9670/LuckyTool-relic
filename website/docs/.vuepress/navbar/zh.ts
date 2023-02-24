import { navbar } from "vuepress-theme-hope";

export const zhNavbar = navbar([
  "/",
  {
    text: "介绍",
    icon: "creative",
    link: "/info",
    activeMatch: "^/info",
  },
  {
    text: "入门",
    icon: "creative",
    link: "/use",
    activeMatch: "^/use",
  },
  {
    text: "功能",
    icon: "creative",
    link: "/guide/",
    activeMatch: "^/function",
  },
  {
    text: "反馈",
    icon: "creative",
    link: "/feedback",
    activeMatch: "^/feedback",
  },
  {
    text: "捐赠",
    icon: "creative",
    link: "/donate",
    activeMatch: "^/donate",
  },
  {
    text: "链接",
    icon: "creative",
    children: [
      {
        text: "Github Repo",
        icon: "creative",
        link: "https://github.com/luckyzyx/LuckyTool",
      },
      {
        text: "LSPosed Repo",
        icon: "creative",
        link: "https://github.com/Xposed-Modules-Repo/com.luckyzyx.luckytool",
      },
      {
        text: "Telegram 频道",
        icon: "creative",
        link: "https://t.me/LuckyTool",
      },
      {
        text: "Telegram 讨论群",
        icon: "creative",
        link: "https://t.me/+F42pfv-c0h4zNDc9",
      },
      {
        text: "Vuepress v2",
        icon: "creative",
        link: "https://v2.vuepress.vuejs.org/",
      },
      {
        text: "vuepress-theme-hope",
        icon: "creative",
        link: "https://theme-hope.vuejs.press/",
      },
    ],
  },
]);
