import React from "react";
import history from "@history";
import { Row, Col, Typography, Button } from "antd";
import './index.scss';

const { Title, Paragraph } = Typography;

export default function ErrorPage() {

    const goHome = () => {
        history.push("/"); // redirects to homepage
    };

    return (
        <div className="page-error" >
            <Row justify="center">
                <Col>
                    <Title className="page-error-title">404</Title>
                    <Title level={2} className="page-error-text" >
                        Page not found : (
                    </Title>
                    <Paragraph className="page-error-decription">
                        Ooooups! Looks like you got lost.
                    </Paragraph>
                    <Button type="primary" onClick={goHome}>
                        Go to Homepage
                    </Button>
                </Col>
            </Row>
        </div>
    );
}
