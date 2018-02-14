package com.mvcFramework.dispatcher;

import com.mvcFramework.controllerAction.ControllerActionPair;
import com.mvcFramework.handlers.HandlerActionImpl;
import com.mvcFramework.handlers.HandlerMappingImpl;
import com.mvcFramework.interfaces.Dispatcher;
import com.mvcFramework.interfaces.HandlerAction;
import com.mvcFramework.interfaces.HandlerMapping;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet implements Dispatcher{

    private HandlerMapping handlerMapping;

    private HandlerAction handlerAction;

    public DispatcherServlet(){
        this.handlerMapping = new HandlerMappingImpl();
        this.handlerAction = new HandlerActionImpl();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.handleRequest(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       this.handleRequest(req, resp);
    }

    @Override
    public ControllerActionPair dispatchRequest(HttpServletRequest request) {
        ControllerActionPair controllerActionPair = null;
        try {
            controllerActionPair = this.handlerMapping.findController(request);


        } catch (ServletException
                | IOException
                | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return controllerActionPair;
    }

    @Override
    public String dispatchAction(HttpServletRequest request, ControllerActionPair controllerActionPair) {
        String view = null;

        try {
            view = this.handlerAction.executeControllerAction(request,controllerActionPair);
        } catch (InvocationTargetException
                | IllegalAccessException
                | InstantiationException
                | NoSuchMethodException e) {
            e.printStackTrace();
        }

        return view;
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response){
        ControllerActionPair controllerActionPair = this.dispatchRequest(request);

        String view = this.dispatchAction(request, controllerActionPair);



        try {
            request.getRequestDispatcher("/" + view + ".jsp").forward(request, response);

        } catch (ServletException
                | IOException e) {
            e.printStackTrace();
        }
    }

}
