import { navbar } from "vuepress-theme-hope";

export const zhNavbar = navbar([
  "/",
  { text: "指南", icon: "creative", link: "/help/demo/" },
  // {
  //   text: "指南",
  //   icon: "creative",
  //   prefix: "/guide/",
  //   children: [
  //     {
  //       text: "Bar",
  //       icon: "creative",
  //       prefix: "bar/",
  //       children: ["baz", { text: "...", icon: "more", link: "" }],
  //     },
  //     {
  //       text: "Foo",
  //       icon: "config",
  //       prefix: "foo/",
  //       children: ["ray", { text: "...", icon: "more", link: "" }],
  //     },
  //   ],
  // },
  {
    text: "支持",
    icon: "info",
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
