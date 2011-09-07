package com.googlecode.tawus.addons.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.PropertyAdapter;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.util.AbstractSelectModel;

public class SelectSupport<T> extends AbstractSelectModel implements ValueEncoder<T> {
    private final List<T> items;
    private Map<String, PropertyAdapter> adapterMap = new HashMap<String, PropertyAdapter>();
    private String label;
    private PropertyAdapter indexPropertyAdapter;
    private TypeCoercer typeCoercer;
    private final static Pattern PROPERTY_PATTERN = Pattern.compile("\\$\\{([\\w.$]+)\\}");

    public SelectSupport(final List<T> items, final String label, String indexProperty,
            final Class<?> valueType, final PropertyAccess access, TypeCoercer typeCoercer) {
        this.items = items;
        this.label = label;
        indexPropertyAdapter = access.getAdapter(valueType).getPropertyAdapter(indexProperty);
        if (indexPropertyAdapter == null) {
            throw new RuntimeException(String.format("No property '%s' exists for class '%s'",
                    indexProperty, valueType.getCanonicalName()));
        }

        Matcher matcher = PROPERTY_PATTERN.matcher(label);
        this.typeCoercer = typeCoercer;
        while (matcher.find()) {
            adapterMap.put(matcher.group(0),
                    access.getAdapter(valueType).getPropertyAdapter(matcher.group(1)));
        }
    }

    public List<OptionGroupModel> getOptionGroups() {
        return null;
    }

    public List<OptionModel> getOptions() {
        final List<OptionModel> options = new ArrayList<OptionModel>();

        if (items != null) {
            for (T item : items) {
                options.add(new OptionModelImpl(toLabel(item), item));
            }
        }

        return options;
    }

    private String toLabel(Object object) {
        String label = this.label;
        for (String key : adapterMap.keySet()) {
            label = label.replace(key, adapterMap.get(key).get(object).toString());
        }
        return label;
    }

    /**
     * {@inheritDoc}
     */
    public String toClient(T value) {
        return typeCoercer.coerce(getIndex(value), String.class);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public T toValue(String clientValue) {
        for (T value : items) {
            if (getIndex(value).equals(
                    typeCoercer.coerce(clientValue, indexPropertyAdapter.getType()))) {
                return value;
            }
        }

        return null;
    }

    /**
     * Gets the index value for a particular item
     * 
     * @param value
     */
    private Object getIndex(Object value) {
        Object fieldValue = indexPropertyAdapter.get(value);
        if (fieldValue == null) {
            throw new RuntimeException("Index property cannot be null");
        }
        return fieldValue;
    }
}
