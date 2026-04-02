import _ from "lodash";

class EventEmitter {
  constructor() {
    this.events = {};
  }

  _getEventListByName(eventName) {
    if (typeof this.events[eventName] === "undefined") {
      this.events[eventName] = new Set();
    }
    return this.events[eventName];
  }

  on(eventName, fn) {
    this._getEventListByName(eventName).add(fn);
  }

  once(eventName, fn) {
    const self = this;

    const onceFn = (...args) => {
      self.removeListener(eventName, onceFn);
      fn.apply(self, args);
    };
    this.on(eventName, onceFn);
  }

  emit(eventName, ...args) {
    this._getEventListByName(eventName).forEach(
      // eslint-disable-next-line func-names
      (fn) => {
        fn.apply(this, args);
      }
    );
  }

  removeListener(eventName, fn) {
    this._getEventListByName(eventName).delete(fn);
  }
}

class Utils {
    static EventEmitter = EventEmitter;
    
    static setRoutes(config, defaultAuth) {
        let routes = [...config.routes];
        // console.log("routes setRoutes", routes);
        
        routes = routes.map((route) => {
            let auth =
                config.auth || config.auth === null ? config.auth : defaultAuth || null;
            auth = route.auth || route.auth === null ? route.auth : auth;
            const settings = _.merge({}, config.settings, route.settings);

            return {
                ...route,
                settings,
                auth,
            };
        });

        return [...routes];
    }

    static generateRoutesFromConfigs(configs, defaultAuth) {
        // console.log("generateRoutesFromConfigs config", configs);
        // console.log("generateRoutesFromConfigs defaultAuth", defaultAuth);
        
        let allRoutes = [];
        configs.forEach((config) => {
            allRoutes = [...allRoutes, ...this.setRoutes(config, defaultAuth)];
        });

        // console.log("allRoutes", allRoutes);
        
        return allRoutes;
    } 

    static hasPermission(authArr, userRole) {
        /**
         * If auth array is not defined
         * Pass and allow
         */
        if (authArr === null || authArr === undefined) {
            // console.info("auth is null || undefined:", authArr);
            return true;
        }
        if (authArr.length === 0) {
            /**
             * if auth array is empty means,
             * allow only user role is guest (null or empty[])
             */
            // console.info("auth is empty[]:", authArr);
            return !userRole || userRole.length === 0;
        }
        /**
         * Check if user has grants
         */
        // console.info("auth arr:", authArr);
        /*
                Check if user role is array,
                */
        if (userRole && Array.isArray(userRole)) {
            return authArr.some((r) => userRole.indexOf(r) >= 0);
        }

        /*
                Check if user role is string,
                */
        return authArr.includes(userRole);
    }
}

export default Utils;