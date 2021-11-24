package de.leantwi.cloudsystem.event;

import de.leantwi.cloudsystem.api.event.PacketListener;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventBus {

    private final Map<Class<?>, EventHandlerMethod[]> eventClasses = new ConcurrentHashMap<>();


    public void post(Object event) {
        System.out.println("Debug-Method: 90");
        this.eventClasses.entrySet().forEach(s -> System.out.println(s.getClass().getName()));
        EventHandlerMethod[] handlerMethods = this.eventClasses.get(event);
        System.out.println("Debug-Method: 91");

        if (handlerMethods != null) {
            System.out.println("Debug-Method: 92");

            for (EventHandlerMethod method : handlerMethods) {
                System.out.println("Debug-Method: 92");
                System.out.println("Debug-Method: 1");
                try {
                    System.out.println("Debug-Method: 92");
                    method.invoke(event);
                    System.out.println("Debug-Method: 93");
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void register(Object listener) {
        System.out.println("Debug-Method: 80");
        for (Method method : listener.getClass().getDeclaredMethods()) {
            System.out.println("Debug-Method: 81");
            PacketListener annotation = method.getAnnotation(PacketListener.class);
            if (annotation != null) {
                System.out.println("Debug-Method: 82");
                Class<?>[] parameters = method.getParameterTypes();
                System.out.println("Debug-Method: 83");
                if (parameters.length != 1) {
                    System.out.println("Debug-Method: 83.5");
                    return;
                }
                System.out.println("Debug-Method: 84");
                List<EventHandlerMethod> list = new ArrayList<>();
                EventHandlerMethod eventHandlerMethod = new EventHandlerMethod(listener, method);
                list.add(eventHandlerMethod);
                this.eventClasses.put(parameters[0], list.toArray(new EventHandlerMethod[list.size()]));
                System.out.println("Debug-Method: 85");
            }

        }

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


