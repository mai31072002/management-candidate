import React, { useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { Row, Col } from 'antd';
import './index.scss';
// import imgLogoWp from "assets/icon/commont/icon_logo_WP.svg";
import iconUser from "assets/icon/commont/icon_user.svg";
// import iconNotify from "assets/icon/commont/icon_alert.svg";
import { useHistory } from "react-router-dom";
import { fetchUserDetail } from "app/auth/store/actions/auth.action";

// const { Search } = Input;

const HeaderBar = React.memo(() => {
    const history = useHistory();
    const dispatch = useDispatch();

    const username = useSelector((state) => state.auth.user?.data?.username);
    const userDetail = useSelector((state) => state.auth.user.userDetail?.data);

    useEffect(() => {
        if (!userDetail || Object.keys(userDetail).length === 0) {
            dispatch(fetchUserDetail());
        }
    }, [dispatch, userDetail]);

    // const onSearch = (value, _e, info) =>
    //   console.log(info === null || info === void 0 ? void 0 : info.source, value);

    return (
        <Row className="main-header fix-header">
            <Col span={5} className={"container-logo"}>
                <Row className={"cover-logo"}>
                    <Col span={24} className={"content-logo"}>
                        {/* <img
                            src={imgLogoWp}
                            alt={"logo-weatherplus"}
                            className={"image-logo-weatherplus"}
                        /> */}
                    </Col>
                </Row>
            </Col>
            <Col span={14} sm={10} md={10} lg={12} className={"content-main-header"}>
                <Row className={"cover-detail-main-header"}>
                    <Col span={24} className={"detail-content-main-header"}>
                        {/* <Search
                            placeholder="Tìm kiếm"
                            allowClear
                            onSearch={onSearch}
                            className={"header-search"}
                            // style={{ width: 304 }}
                        /> */}
                    </Col>
                </Row>
            </Col>
            <Col span={5} sm={9} md={9} lg={7} className={"container-account-user"}>
                <Row className={"content-account-user"}>
                    <Col span={16} className={"detail-account-user"}>
                        <span className={"username-text"}>
                            {username}
                        </span>
                        <img
                            src={iconUser}
                            alt={"icon-user"}
                            className={"header-icon-user"}
                            onClick={() => history.push("/account")}
                            style={{ cursor: "pointer" }}
                        />
                    </Col>
                    {/* <Col span={8} className={"container-notify"}>
                        <img
                            src={iconNotify}
                            alt={"icon-notify"}
                            className={"header-icon-notify"}
                        />
                    </Col> */}
                </Row>
            </Col>
        </Row>
    );
});

export default HeaderBar;
