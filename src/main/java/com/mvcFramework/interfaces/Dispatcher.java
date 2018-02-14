package com.mvcFramework.interfaces;

import com.mvcFramework.controllerAction.ControllerActionPair;

import javax.servlet.http.HttpServletRequest;

public interface Dispatcher {

    //find desired controller
    ControllerActionPair dispatchRequest(HttpServletRequest request);

    //return the view
    String dispatchAction(HttpServletRequest request, ControllerActionPair controllerActionPair);
}
