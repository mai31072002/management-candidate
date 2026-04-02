import React, { useContext } from "react";
import { renderRoutes, matchRoutes } from "react-router-config";
import AppContext from "app/app_context";
import { useLocation } from "react-router-dom";

import Suspense from "./component/suspense";
// import AuthLayout from "./auth";
import AmdinLayout from "./admin";
// import AdminLayoutSpecial from "./admin_special";
// import LandingPage from "./home_page";
import LayoutHome from "./layout_home";
// import LayoutSpecial from "./layout_special";

function Layout(props) {

    // console.log("AppContext: ", AppContext);
    
    const appContext = useContext(AppContext);
    // console.log("appContext: ", appContext);

    const { routes } = appContext;
    const location = useLocation();
    const { pathname } = location;
    // console.log("pathname: ", pathname);
    
    const matched = matchRoutes(routes, pathname)[0]; // tìm pathname trong routes
    // console.log("matched: ", matched); 

    let layout = "auth";
    if (matched && matched.route.settings) {
        const routeSettings = matched.route.settings;
        if (routeSettings.layout) {
            layout = routeSettings.layout;
        }
    }

    return (
        <div>
        <Suspense>
            {
                layout === "admin" ? (
                    <AmdinLayout {...props}>{renderRoutes(routes)}</AmdinLayout>
                ) : (
                    <LayoutHome>{renderRoutes(routes)}</LayoutHome>
                )
                //     layout === "layout-home" ? (
                //   <LayoutHome>{renderRoutes(routes)}</LayoutHome>
                // ) : (
                //   <AuthLayout>{renderRoutes(routes)}</AuthLayout>
                // )
            }
        </Suspense>

        {props.children}
        </div>
    );
}

export default React.memo(Layout);
