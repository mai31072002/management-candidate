import React from "react";
import Utils from "../helpers/utils";
import appsConfigs from "../main/main.config";

const routeConfigs = [...appsConfigs];

const routes = [
  ...Utils.generateRoutesFromConfigs(routeConfigs, null),
  { component: React.lazy(() => import("../pages/error")) },
];

export default routes;
