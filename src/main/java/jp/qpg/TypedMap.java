package jp.qpg;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * Typed map
 */
public class TypedMap {
    /**
     * Key
     *
     * @param <T> Value type
     */
    public static class Key<T> {
        /**
         * Key name
         */
        public final String name;
        /**
         * Default value
         */
        public final T value;
        /**
         * Value type class
         */
        public final Class<T> type;
        /**
         * Owner
         */
        public TypedMap owner;

        /**
         * @param owner Owner
         * @param name Key name
         * @param value Default value
         */
        @SuppressWarnings("unchecked")
        @SafeVarargs
        protected Key(TypedMap owner, String name, T... value) {
            this.owner = owner;
            this.name = name;
            this.value = value != null && value.length > 0 ? value[0] : null;
            this.type = (Class<T>) (value == null ? Object.class : value.getClass().getComponentType());
        }

        /**
         * @return Value
         */
        public T get() {
            return owner.get(this);
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return String.valueOf(get());
        }
    }

    /**
     * Value converters(Value class: convert from String)
     */
    public static final Map<Class<?>, Function<String, Object>> converters = new HashMap<>();
    static {
        converters.put(Boolean.class, Boolean::parseBoolean);
        converters.put(Byte.class, Byte::parseByte);
        converters.put(Short.class, Short::parseShort);
        converters.put(Integer.class, Integer::parseInt);
        converters.put(Character.class, s -> s.charAt(0));
        converters.put(Long.class, Long::parseLong);
        converters.put(Float.class, Float::parseFloat);
        converters.put(Double.class, Double::parseDouble);
        converters.put(BigInteger.class, BigInteger::new);
        converters.put(BigDecimal.class, BigDecimal::new);
        converters.put(LocalDate.class, LocalDate::parse);
        converters.put(LocalDateTime.class, LocalDateTime::parse);
        converters.put(LocalTime.class, LocalTime::parse);
        converters.put(Locale.class, Locale::forLanguageTag);
        converters.put(Charset.class, Charset::forName);
    }

    /**
     * Map
     */
    public final Map<Object, Object> map;
    /**
     * Map cache
     */
    protected final Map<Key<?>, Object> cache;

    /**
     * @param supplier Base map supplier
     */
    public TypedMap(Supplier<Map<Object, Object>> supplier) {
        map = supplier.get();
        cache = new HashMap<>();
    }

    /**
     * @param <T> Value type
     * @param key Key
     * @return Value
     */
    public <T> T get(Key<T> key) {
        return key.type.cast(cache.computeIfAbsent(key, __ -> {
            Object value = map.getOrDefault(key.name, key.value);
            if (value == null) {
                return key.value;
            }
            if (key.type.isAssignableFrom(value.getClass())) {
                return key.type.cast(value);
            }
            Function<String, Object> converter = converters.get(key.type);
            if (converter == null) {
                Logger.getGlobal().warning("not found converter: " + String.class + " -> " + key.type);
                return key.value;
            }
            try {
                return converter.apply(String.valueOf(value));
            } catch (Exception e) {
                Logger.getGlobal().warning(String.class + " `" + value + "` connot convert to " + key.type);
                return key.value;
            }
        }));
    }

    /**
     * @param <T> Value type
     * @param name Key name
     * @param value Value
     * @return Key
     */
    public <T> Key<T> key(String name, @SuppressWarnings("unchecked") T... value) {
        return new Key<>(this, name, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return map.toString();
    }
}
