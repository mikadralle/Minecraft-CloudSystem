package de.leantwi.cloudsystem.event;

import de.leantwi.cloudsystem.api.event.Listener;
import de.leantwi.cloudsystem.api.event.PacketListener;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventBus {

    private final Map<Class<?>, List<EventHandlerMethod>> eventClasses = new HashMap<>();


    public void post(Object event) {

        List<EventHandlerMethod> handlerMethods = this.eventClasses.get(event.getClass());
        if (handlerMethods != null) {
            for (EventHandlerMethod method : handlerMethods) {
                try {
                    System.out.println("ListenerInClass: " + method.listenerClass.getClass().getName());
                    method.invoke(event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Map<Class<?>, List<Method>> findHandlers(Object listener) {

        Map<Class<?>, List<Method>> handlerList = new HashMap<>();

        for (Method method : listener.getClass().getDeclaredMethods()) {

            PacketListener annotation = method.getAnnotation(PacketListener.class);

            if (annotation != null) {

                Class<?>[] parameters = method.getParameterTypes();
                if (parameters.length != 1) {
                    System.out.println("The parameters count is wrong");
                }

                List<Method> methodList = handlerList.get(parameters[0]);
                if (methodList == null) {
                    methodList = new ArrayList<>();
                }
                methodList.add(method);
                handlerList.put(parameters[0], methodList);

            }
        }

        return handlerList;

    }

    public void register(Listener listener) {


        Map<Class<?>, List<Method>> handlerList = findHandlers(listener);

        handlerList.keySet().forEach(eventName -> {
            List<EventHandlerMethod> list = this.eventClasses.get(eventName);
            if(list == null){
                list = new ArrayList<>();
            }

            for(Method method : handlerList.get(eventName)){
                System.out.println("ClassName: " + listener.getClass().getName());
                EventHandlerMethod eventHandlerMethod = new EventHandlerMethod(listener, method);
                list.add(eventHandlerMethod);
            }

            this.eventClasses.put(eventName, list);

        });

    }

    @AllArgsConstructor
    private class EventHandlerMethod {

        private Object listenerClass;

        private Method method;

        public void invoke(Object event) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            method.invoke(listenerClass, event);
        }


    }

}


