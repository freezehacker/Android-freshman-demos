package com.me.customeventbus.eventbus;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by sjk on 17-6-8.
 *
 * 模拟事件总线
 */

public class EventBus {

    private static Map<String, EventBus> instanceMap = new HashMap<>();
    private List<Object> subscribers;

    public static EventBus getDefault() {
        return get("");
    }

    public static EventBus get(String id) {
        EventBus ret = instanceMap.get(id);
        if (ret == null) {
            synchronized (EventBus.class) {
                if (ret == null) {
                    ret = new EventBus();
                    instanceMap.put(id, ret);
                }
            }
        }
        return ret;
    }

    private EventBus() {
        subscribers = new ArrayList<>();
    }

    public void register(Object object) {
        if (subscribers.indexOf(object) == -1) {
            subscribers.add(object);
        }
    }

    public void unregister(Object object) {
        subscribers.remove(object);
    }

    public void unregisterAll() {
        subscribers.clear();
    }

    public void post(Object event) {
        try {
            Class eventClazz = event.getClass();    // event类型
            for (Object subscriber : subscribers) {
                Class clazz = subscriber.getClass();
                Method[] publicMethods = clazz.getMethods();    // 找出其所有public方法
                for (Method publicMethod : publicMethods) {
                    if (publicMethod.isAnnotationPresent(Subscribe.class)) {    // 如果该方法存在注解@Subscribe
                        Class[] parameterTypes = publicMethod.getParameterTypes();
                        if (parameterTypes.length == 1
                                && eventClazz.getName().equals(parameterTypes[0].getName())) {  // 参数个数只有1个并且与event类型相等
                            publicMethod.invoke(subscriber, event); // 满足条件，进行回调
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }




}
