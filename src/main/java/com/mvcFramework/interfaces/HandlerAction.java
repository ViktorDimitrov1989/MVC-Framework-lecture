package com.mvcFramework.interfaces;

import com.mvcFramework.controllerAction.ControllerActionPair;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;

public interface HandlerAction {
    String executeControllerAction(HttpServletRequest request, ControllerActionPair
            controllerActionPair) throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException;
}