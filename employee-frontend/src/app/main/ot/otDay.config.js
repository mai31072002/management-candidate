import React from "react";
// import i18next from "i18next";

import { authRoles } from "app/auth";
// import en from "./i18n/en";

// import { isMobile } from "react-device-detect";
// i18next.addResourceBundle("en", "login", en);

const OtDateConfig = {
  settings: { layout: "admin" },
  auth: authRoles.onlyGuest,
  routes: [
    {
      path: "/ot-date",
      exact: true,
      component: React.lazy(
        () => import("./OtDate"),
      ),
    },
  ],

  guestPath: ["/ot-date"],
};

export default OtDateConfig;
