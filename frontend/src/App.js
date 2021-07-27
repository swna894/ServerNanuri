import React from "react";
import { HashRouter as Router, Switch, Route } from "react-router-dom";

import LandingPage from "./components/ordering/LandingPage";
import SigninPage from "./components/signin/SigninPage";
import SignupPage from "./components/signin/SignupPage";
import HistroyPage from "./components/history/HistoryPage";
import Auth from "./hoc/Auth";

function App() {
  return (
    <Router>
      <div>
        <Switch>
          <Route exact path="/" component={Auth(SigninPage, false)} />
          <Route exact path="/signin" component={Auth(SigninPage, false)} />
          <Route exact path="/signup" component={Auth(SignupPage, false)} />
          <Route exact path="/order" component={Auth(LandingPage, true)} />
          <Route exact path="/history" component={Auth(HistroyPage, false)} />
          <Route component={SigninPage} />
        </Switch>
      </div>
    </Router>
  );
}

export default App;
