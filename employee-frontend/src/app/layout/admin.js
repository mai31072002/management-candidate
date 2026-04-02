import React, { useState } from 'react';
import PropTypes from "prop-types";
import routes from "app/configs/routes.config";
import { Route, Switch } from "react-router-dom";
import "perfect-scrollbar/css/perfect-scrollbar.css";
import { Layout } from 'antd';
import Header from 'components/Header/header';
import Sidebar from 'components/Sidebar/sidebar';
import Footer from 'components/Footer/footer';
import './admin.scss';

const { Content } = Layout;

// console.log('routes', routes);

const AppLayout = () => {
    const [collapsed, setCollapsed] = useState(false);
    const loading = (
        <div
            style={{
                height: "100%",
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
            }}
        >
        Loading...
        </div>
    );

    return (
        <>
            <Layout style={{ minHeight: '100vh' }}>
                <Sidebar collapsed={collapsed} onCollapse={setCollapsed} />
                <Layout className='main-layout-content'>
                    <Header />

                    <Content className="main-layout">
                        <React.Suspense fallback={loading}>
                            <Switch>
                                {routes.map((route, idx) =>
                                    route.component ? (
                                        <Route
                                            key={String(idx)}
                                            path={route.path}
                                            exact={route.exact}
                                            name={route.name}
                                            render={(renderProps) => (
                                                <route.component {...renderProps} />
                                            )}
                                        />
                                    ) : null
                                )}
                            </Switch>
                        </React.Suspense>
                    </Content>

                    {/* <Footer /> */}
                </Layout>
            </Layout>
        </>
    );
};

AppLayout.propTypes = {
  children: PropTypes.element,
};

export default AppLayout;