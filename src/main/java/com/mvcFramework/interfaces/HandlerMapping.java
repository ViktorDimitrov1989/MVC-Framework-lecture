package com.mvcFramework.interfaces;

import com.mvcFramework.controllerAction.ControllerActionPair;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface HandlerMapping {

    ControllerActionPair findController(HttpServletRequest request) throws ServletException, IOException, ClassNotFoundException;
}
