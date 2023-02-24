import { navbar } from "vuepress-theme-hope";

export const enNavbar = navbar([
  "/en/",
  {
    text: "Support",
    icon: "note",
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
        text: "Telegram Channel",
        icon: "creative",
        link: "https://t.me/LuckyTool",
      },
      {
        text: "Telegram Discussion Group",
        icon: "creative",
        link: "https://t.me/+F42pfv-c0h4zNDc9",
      },
      {
        text: "Vuepress v2",
        link: "https://v2.vuepress.vuejs.org/",
      },
      {
        text: "vuepress-theme-hope",
        link: "https://theme-hope.vuejs.press/",
      },
    ],
  },
]);
