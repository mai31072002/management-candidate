import React, { Component } from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import PropTypes from "prop-types";

import * as userActions from "app/auth/store/actions";
import jwtService from "app/service/jwt";

function SplashScreen() {
  return (
    <div className="pageloader">
      <span className="title">Loading...</span>
    </div>
  );
}

class Auth extends Component {
  state = {
    waitAuthCheck: true,
  };

  componentDidMount() {
    return Promise.all([
      // Comment the lines which you do not use
      this.jwtCheck(),
    ]).then(() => {
      this.setState({ waitAuthCheck: false });
    });
  }

  jwtCheck = () =>
    new Promise((resolve) => {
      jwtService.on("onAutoLogin", () => {
        jwtService
          .signInWithToken()
          .then((user) => {
            this.props.setUserData({
              data: user.data,
              role: user.role,
            });
            resolve();
          })
          .catch(() => {
            resolve();
          });
      });

      jwtService.on("onAutoLogout", (message) => {
        if (message) {
          // this.props.showMessage({ message });
          // console.log(message);
        }

        this.props.logout();

        resolve();
      });

      jwtService.on("onNoAccessToken", () => {
        resolve();
      });

      jwtService.init();

      return Promise.resolve();
    });

  render() {
    return this.state.waitAuthCheck ? (
      <SplashScreen />
    ) : (
      <>{this.props.children}</>
    );
  }
}

function mapDispatchToProps(dispatch) {
  return bindActionCreators(
    {
      logout: userActions.logoutUser,
      setUserData: userActions.setUserData,
    },
    dispatch
  );
}

Auth.propTypes = {
  logout: PropTypes.func,
  setUserData: PropTypes.func,
  children: PropTypes.element,
};

export default connect(null, mapDispatchToProps)(Auth);
