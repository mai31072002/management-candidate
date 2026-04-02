import React from "react";
// import i18next from "i18next";

import { authRoles } from "app/auth";
// import en from "./i18n/en";

import { isMobile } from "react-device-detect";
// i18next.addResourceBundle("en", "login", en);

const AuthConfig = {
  settings: { layout: "auth" },
  auth: authRoles.onlyGuest,
  routes: [
    // {
    //   path: "/",
    //   exact: true,
    //   component: React.lazy(() => import("./login")),
    // },
    {
      path: "/",
      exact: true,
      component: React.lazy(() =>
        !isMobile ? import("./login") : import("./login")
      ),
    },
    {
      path: "/auto-login",
      exact: true,
      component: React.lazy(() => import("./auto_login")),
    },
    {
      path: "/auth/logout",
      exact: true,
      component: React.lazy(() => import("./logout")),
    },
  ],
  // Don't need login
  guestPath: ["/", "/auth/logout", "/auto-login"],
};

export default AuthConfig;
