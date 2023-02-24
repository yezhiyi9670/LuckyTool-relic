import { navbar } from "vuepress-theme-hope";

export const enNavbar = navbar([
  "/en/",
  {
    text: "Support",
    icon: "note",
    children: [
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
