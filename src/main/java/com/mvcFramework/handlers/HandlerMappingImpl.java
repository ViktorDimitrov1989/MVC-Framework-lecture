package com.mvcFramework.handlers;
import com.mvcFramework.annotations.controller.Controller;
import com.mvcFramework.annotations.requests.GetMapping;
import com.mvcFramework.annotations.requests.PostMapping;
import com.mvcFramework.controllerAction.ControllerActionPair;
import com.mvcFramework.interfaces.HandlerMapping;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class HandlerMappingImpl implements HandlerMapping{


    @Override
    public ControllerActionPair findController(HttpServletRequest request) throws ClassNotFoundException {
        String urlPath = request.getRequestURI();
        String projectPath = request.getServletContext().getRealPath("/WEB-INF/classes");
        //TODO find all controllers
        List<Class> controllers = this.findAllControllers(projectPath);

        ControllerActionPair controllerActionPair = null;
        for (Class controller : controllers) {
            Method[] methods = controller.getDeclaredMethods();
            for (Method method : methods) {
                //TODO find method path
                String methodPath = this.findMethodPath(request, method);

                if (methodPath == null) {
                    continue;
                }

                //TODO If paths are matching return a new ActionPairController with the appropriate class, method and path variables
                if(this.isPathMatching(urlPath, methodPath)){
                    controllerActionPair = new ControllerActionPair(controller, method);
                    this.addPathVariables(controllerActionPair, urlPath, methodPath);
                }
            }
        }

        return controllerActionPair;
    }

    private void addPathVariables(ControllerActionPair controllerActionPair, String urlPath, String methodPath) {
        String[] uriTokens = urlPath.split("/");
        String[] methodTokens = methodPath.split("/");

        //TODO If there is path variable add it to the ControllerActionPair
        for (int i = 0; i < uriTokens.length; i++) {
            String currentUriToken = uriTokens[i];

            if(methodTokens[i].startsWith("{") && methodTokens[i].endsWith("}")){
                String key = methodTokens[i].replaceAll("[{}]", "");
                Object value = currentUriToken;
                controllerActionPair.addPathVariable(key, value);
            }

        }

    }

    private boolean isPathMatching(String urlPath, String methodPath) {
        boolean isPathMatching = true;
        String[] uriTokens = urlPath.split("/");
        String[] methodTokens = methodPath.split("/");

        if(uriTokens.length != methodTokens.length){
            isPathMatching = false;
            return isPathMatching;
        }

        for (int i = 0; i < uriTokens.length; i++) {
            String currentUriToken = uriTokens[i];
            String currentMethodToken = methodTokens[i];

            if(currentMethodToken.startsWith("{") && currentMethodToken.endsWith("}")){
                continue;
            }

            if(!currentUriToken.equals(currentMethodToken)){
                isPathMatching = false;
                return isPathMatching;
            }
        }

        return isPathMatching;
    }

    private String findMethodPath(HttpServletRequest request, Method method) {
        String methodType = request.getMethod();
        String path = null;
        switch (methodType){
            case "GET":
                if(method.isAnnotationPresent(GetMapping.class)){
                    GetMapping getMapping = method.getAnnotation(GetMapping.class);
                    path = getMapping.value();
                }
                break;
            case "POST":
                if(method.isAnnotationPresent(PostMapping.class)){
                    PostMapping postMapping = method.getAnnotation(PostMapping.class);
                    path = postMapping.value();
                }
                break;
                default:
                    break;
        }

        return path;
    }

    private List<Class> findAllControllers(String projectDirectory) throws
            ClassNotFoundException{
        List<Class> controllerClasses = new ArrayList<>();
        File directory = new File(projectDirectory);
        File[] files = directory.listFiles();

        for (File file : files) {
            if (file.isFile()) {
                Class<?> controllerClazz = this.getClass(file);

                if(controllerClazz != null){
                    if(controllerClazz.isAnnotationPresent(Controller.class)){
                        controllerClasses.add(controllerClazz);
                    }
                }

            } else if (file.isDirectory()) {
                String subDirectory = file.getAbsolutePath();

                controllerClasses.addAll(this.findAllControllers(subDirectory));
            }
        }
        return controllerClasses;
    }

    private Class getClass(File file) throws ClassNotFoundException {
        String absolutePath = file.getAbsolutePath();
        String className = absolutePath.split("classes\\\\")[1]
                .replaceAll("[\\\\]", ".")
                .replace(".class", "");

        Class currentClass = null;

        if (!className.endsWith("DispatcherServlet")) {
            currentClass = Class.forName(className);
        }

        return currentClass;
    }

}
