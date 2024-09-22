package cab302softwaredevelopment.outbackweathertrackerapplication.controllers.widgets;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

public abstract class BaseWidgetController implements IConfigurableWidget {
    protected String widgetId;

    public void setWidgetId(String widgetId) {
        this.widgetId = widgetId;
    }
    @Override
    public final void applyConfig(Map<String, Object> config) {
        try {
            Method[] methods = this.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals("applyConfig")) {
                    if (method.getParameterCount() > 0) {
                        Parameter[] parameters = method.getParameters();
                        Object[] args = new Object[parameters.length];
                        for (int i = 0; i < parameters.length; i++) {
                            Parameter parameter = parameters[i];
                            String paramName = parameter.getName();
                            Class<?> paramType = parameter.getType();
                            Object value = config.get(paramName);
                            if (value == null) {
                                if (method.isVarArgs() && i == parameters.length - 1) {
                                    args[i] = new Object[0];
                                }
                            }
                            args[i] = castValue(value, paramType);
                        }
                        method.invoke(this, args);
                        return;
                    }
                }
            }
            throw new RuntimeException("No suitable applyConfig method found in " + this.getClass().getName());
        } catch (Exception e) {
            throw new RuntimeException("Error applying config in " + this.getClass().getName(), e);
        }
    }

    private Object castValue(Object value, Class<?> targetType) {
        if (value == null) {
            return null;
        }
        if (targetType.isInstance(value)) {
            return value;
        } else if (targetType == int.class || targetType == Integer.class) {
            if (value instanceof Number) {
                return ((Number) value).intValue();
            }
            return Integer.parseInt(value.toString());
        } else if (targetType == double.class || targetType == Double.class) {
            if (value instanceof Number) {
                return ((Number) value).doubleValue();
            }
            return Double.parseDouble(value.toString());
        } else if (targetType == boolean.class || targetType == Boolean.class) {
            if (value instanceof Boolean) {
                return value;
            }
            return Boolean.parseBoolean(value.toString());
        } else if (targetType == String.class) {
            return value.toString();
        } else {
            throw new IllegalArgumentException("Unsupported parameter type: " + targetType.getName());
        }
    }
}
