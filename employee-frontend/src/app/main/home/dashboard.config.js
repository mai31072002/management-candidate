import React from "react";
// import i18next from "i18next";

import { authRoles } from "app/auth";
// import en from "./i18n/en";

// import { isMobile } from "react-device-detect";
// i18next.addResourceBundle("en", "login", en);

const DashboardConfig = {
  settings: { layout: "admin" },
  auth: authRoles.onlyGuest,
  routes: [
    {
      path: "/home",
      exact: true,
      component: React.lazy(
        () => import("./dashboard"),
      ),
    },
  ],

  guestPath: ["/home"],
};

export default DashboardConfig;
