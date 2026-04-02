import React from "react";
import { Col, Row } from "antd";
import "./header.scss";
// import logoKTHK from "assets/icon/layoutHeader/logoKTHK.png";

const HeaderHome = () => {
  return (
    <Row className={"container-detail-header-home"}>
      {/* <Col xl={6} xs={0}></Col> */}
      <Col xl={24} xs={24} className={"content-detail"}>
        <Row>
          <Col xl={2} xs={5}>
            {/* <img src={logoKTHK} alt={"logoKTHK"} className={"logo-header"} /> */}
          </Col>
          <Col xl={22} xs={19} className={"title-header-home"}>
            <span>Hệ thống nhân viên</span>
          </Col>
        </Row>
      </Col>
    </Row>
  );
};
export default HeaderHome;
