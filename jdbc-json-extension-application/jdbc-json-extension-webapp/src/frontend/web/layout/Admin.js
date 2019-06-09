import React, { Component } from "react";
import PropTypes from "prop-types";
import { Switch, Route, Redirect } from "react-router-dom";



const switchRoutes = (routes, parent) => (
    <Switch>
        {routes.map((prop, key) => {
            if (parent === prop.layout) {
                return (
                    <Route
                        path={prop.layout + prop.path}
                        component={prop.component}
                        key={key}
                    />
                );
            }
        })}
    </Switch>
);

class AdminLayout extends Component {
    constructor(props) {
        super(props);
    }


}

