import React from 'react';
import { Layout } from 'antd';
import './index.scss'
const { Footer } = Layout;

const FooterBar = () => (
  <Footer style={{ textAlign: 'center' }}>Ant Design ©{new Date().getFullYear()} Created by Ant UED</Footer>
);

export default FooterBar;
