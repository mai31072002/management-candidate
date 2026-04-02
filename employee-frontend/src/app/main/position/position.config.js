import React from "react";
// import i18next from "i18next";

import { authRoles } from "app/auth";
// import en from "./i18n/en";

// import { isMobile } from "react-device-detect";
// i18next.addResourceBundle("en", "login", en);

const PositionConfig = {
  settings: { layout: "admin" },
  auth: authRoles.onlyGuest,
  routes: [
    {
      path: "/position",
      exact: true,
      component: React.lazy(
        () => import("./position"),
      ),
    },
    {
      path: "/lever",
      exact: true,
      component: React.lazy(
        () => import("./lever"),
      ),
    },
  ],

  guestPath: ["/lever"],
};

export default PositionConfig;
