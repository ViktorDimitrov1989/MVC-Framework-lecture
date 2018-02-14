package com.mvcFramework.handlers;

import com.mvcFramework.annotations.parameters.PathVariable;
import com.mvcFramework.annotations.parameters.RequestParam;
import com.mvcFramework.controllerAction.ControllerActionPair;
import com.mvcFramework.interfaces.HandlerAction;
import com.mvcFramework.model.Model;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class HandlerActionImpl implements HandlerAction {




    @Override
    public String executeControllerAction(HttpServletRequest request, ControllerActionPair controllerActionPair)
            throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        //TODO Get the controller and it respective method to execute
        Class controller = controllerActionPair.getController();
        Method method = controllerActionPair.getActionMethod();

        Parameter[] parameters = method.getParameters();
        List<Object> arguments = new ArrayList<>();

        for (Parameter parameter : parameters) {
            Object currentArgument = null;

            if(parameter.isAnnotationPresent(PathVariable.class)){
                //TODO Set the path variable value
                PathVariable pathVariableAnnotation = parameter.getAnnotation(PathVariable.class);
                String pathVariableKey = pathVariableAnnotation.value();

                Object pathVariable = controllerActionPair.getPathVariable(pathVariableKey);

                currentArgument = this.convertArgument(parameter, pathVariable.toString());
            }
            if(parameter.isAnnotationPresent(RequestParam.class)){
                //TODO Set the request parameter value
                RequestParam requestParamAnnotation = parameter.getAnnotation(RequestParam.class);
                String parameterName = requestParamAnnotation.value();

                String value = request.getParameter(parameterName);

                currentArgument = this.convertArgument(parameter, value);

            }
            if(parameter.getType().isAssignableFrom(Model.class)){
                //TODO Pass the model values to the view
                Constructor constructor = parameter.getType().getConstructor(HttpServletRequest.class);

                currentArgument = constructor.newInstance(request);
            }

            arguments.add(currentArgument);
        }

        //TODO Finally, Invoke the method
        Object controllerInstance = controller.getConstructor().newInstance();

        String view = (String) method.invoke(controllerInstance, (Object[]) arguments.toArray());

        return view;
    }
/*
    private <T> T getPathVariableValue(Parameter parameter, PathVariable
            pathVariableAnnotation, ControllerActionPair controllerActionPair) {
            //TODO Find path variable value
        return null;
    }

    private <T> T getParameterValue(Parameter parameter, RequestParam
            requestParamAnnotationClass, HttpServletRequest request) throws IllegalAccessException,
            InstantiationException {
            //TODO Find request parameter value
        return null;
    }*/

    private <T> T convertArgument(Parameter parameter, String pathVariable){
        Object object = null;

        String parameterType = parameter.getType().getSimpleName();

        switch (parameterType){
            case "Integer":
                object = Integer.valueOf(pathVariable);
                break;
            case "int":
                 object = Integer.parseInt(pathVariable);
                break;
            case "Long":
                object = Long.valueOf(pathVariable);
                break;
            case "long":
                object = Long.parseLong(pathVariable);
                break;
                default:
                    break;
        }

        return (T)object;
    }

}
