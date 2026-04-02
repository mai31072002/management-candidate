import React from "react";
import { Provider } from "react-redux";
import { BrowserRouter, Router } from "react-router-dom";

import history from "@history";
// import "app/i18n/i18n.config";
import Layout from "./layout/layout";

import AppContext from "./app_context";
import routes from "./configs/routes.config";
import store from "./store";
// import "moment/locale/vi";
import AuthConfig from "./main/auth/auth.config";
import Auth from "app/auth/auth";
import Authorization from "./auth/authorization";
import HomeThemeProvider from "./layout/home_theme_provider";
// import HomeConfig from "./main/home/home.config";
// import LoginPage from './main/auth/login';
import '../assets/style/style.css';

const App = (props) => 
    AuthConfig.guestPath.indexOf(props.path) === -1 ? (
        <AppContext.Provider value={{ routes }}>
            <Provider store={store}>
                <BrowserRouter>
                    <Auth>
                        <Router history={history}>
                            <Authorization>
                                <HomeThemeProvider>
                                    <Layout />
                                </HomeThemeProvider>
                            </Authorization>
                        </Router>
                    </Auth>
                </BrowserRouter>
            </Provider>
        </AppContext.Provider>
    ) : (
        <AppContext.Provider value={{ routes }}>
            <Provider store={store}>
                <BrowserRouter>
                    <Router history={history}>
                        <Layout />
                    </Router>
                    {/* <Switch>
                        <Route exact path="/" component={LoginPage}></Route>
                    </Switch> */}
                </BrowserRouter>
            </Provider>
        </AppContext.Provider>
    );

export default App;
