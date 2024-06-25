package com.midnight.cache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Stream;

public class MidnightCache {
    Map<String, CacheEntry<?>> map = new HashMap<>();

    // ===============  1. String start ===========

    public String get(String key) {
        CacheEntry<String> entry = (CacheEntry<String>) map.get(key);
        return entry.getValue();
    }

    public void set(String key, String value) {
        map.put(key, new CacheEntry<>(value));
    }

    public int del(String... keys) {
        return keys == null ? 0 : (int) Arrays.stream(keys).map(map::remove).filter(Objects::nonNull).count();
    }

    public int exists(String... keys) {
        return keys == null ? 0 : (int) Arrays.stream(keys).map(map::containsKey).filter(x -> x).count();
    }

    public String[] mget(String... keys) {
        return keys == null ? new String[0] : Arrays.stream(keys).map(map::get).toArray(String[]::new);
    }

    public void mset(String[] keys, String[] vals) {
        if (keys == null || keys.length == 0) return;
        for (int i = 0; i < keys.length; i++) {
            set(keys[i], vals[i]);
        }
    }

    public int incr(String key) {
        String str = get(key);
        int val = 0;
        try {
            if (str != null) {
                val = Integer.parseInt(str);
            }
            val++;
            set(key, String.valueOf(val));
        } catch (NumberFormatException nfe) {
            throw nfe;
        }
        return val;
    }

    public int decr(String key) {
        String str = get(key);
        int val = 0;
        try {
            if (str != null) {
                val = Integer.parseInt(str);
            }
            val--;
            set(key, String.valueOf(val));
        } catch (NumberFormatException nfe) {
            throw nfe;
        }
        return val;
    }

    public Integer strlen(String key) {
        return get(key) == null ? null : get(key).length();
    }

    // ===============  1. String end ===========


    // ===============  2. list start ===========
    public Integer lpush(String key, String... vals) {
        CacheEntry<LinkedList<String>> entry = (CacheEntry<LinkedList<String>>) map.get(key);
        if (entry == null) {
            entry = new CacheEntry<>(new LinkedList<>());
            this.map.put(key, entry);
        }
        LinkedList<String> exists = entry.getValue();
        Arrays.stream(vals).forEach(exists::addFirst);
        return vals.length;
    }

    public String[] lpop(String key, int count) {
        CacheEntry<LinkedList<String>> entry = (CacheEntry<LinkedList<String>>) map.get(key);
        if (entry == null) return null;
        LinkedList<String> exist = entry.getValue();
        if (exist == null) return null;

        int len = Math.min(count, exist.size());
        String[] ret = new String[len];
        int index = 0;
        while (index < len) {
            ret[index++] = exist.removeFirst();
        }
        return ret;
    }

    public Integer rpush(String key, String... vals) {
        CacheEntry<LinkedList<String>> entry = (CacheEntry<LinkedList<String>>) map.get(key);
        if (entry == null) {
            entry = new CacheEntry<>(new LinkedList<>());
            this.map.put(key, entry);
        }
        LinkedList<String> exists = entry.getValue();
        Arrays.stream(vals).forEach(exists::addLast);
        return vals.length;
    }

    public String[] rpop(String key, int count) {
        CacheEntry<LinkedList<String>> entry = (CacheEntry<LinkedList<String>>) map.get(key);
        if (entry == null) return null;
        LinkedList<String> exist = entry.getValue();
        if (exist == null) return null;

        int len = Math.min(count, exist.size());
        String[] ret = new String[len];
        int index = 0;
        while (index < len) {
            ret[index++] = exist.removeLast();
        }
        return ret;
    }

    public int llen(String key) {
        CacheEntry<LinkedList<String>> entry = (CacheEntry<LinkedList<String>>) map.get(key);
        if (entry == null) return 0;
        LinkedList<String> exist = entry.getValue();
        if (exist == null) return 0;
        return exist.size();
    }

    public String lindex(String key, int index) {
        CacheEntry<LinkedList<String>> entry = (CacheEntry<LinkedList<String>>) map.get(key);
        if (entry == null) return null;
        LinkedList<String> exist = entry.getValue();
        if (exist == null) return null;
        if (index >= exist.size()) return null;
        return exist.get(index);
    }

    public String[] lrange(String key, int start, int end) {
        CacheEntry<LinkedList<String>> entry = (CacheEntry<LinkedList<String>>) map.get(key);
        if (entry == null) return null;
        LinkedList<String> exist = entry.getValue();
        if (exist == null) return null;
        if (start >= exist.size()) return null;
        if (end >= exist.size()) end = exist.size() - 1;
        int len = Math.min(exist.size(), end - start + 1);
        String[] ret = new String[len];
        for (int i = 0; i < len; i++) {
            ret[i] = exist.get(start + i);
        }
        return ret;
    }

    // ===============  2. list end ===========


    // ===============  3. set  ===========
    public Integer sadd(String key, String[] vals) {
        CacheEntry<LinkedHashSet<String>> entry = (CacheEntry<LinkedHashSet<String>>) map.get(key);
        if (entry == null) {
            entry = new CacheEntry<>(new LinkedHashSet<>());
            map.put(key, entry);
        }
        LinkedHashSet<String> exist = entry.getValue();
        exist.addAll(Arrays.asList(vals));
        return vals.length;
    }

    public String[] smembers(String key) {
        CacheEntry<LinkedHashSet<String>> entry = (CacheEntry<LinkedHashSet<String>>) map.get(key);
        if (entry == null) {
            return null;
        }
        LinkedHashSet<String> exist = entry.getValue();
        return exist.toArray(String[]::new);
    }

    public Integer scard(String key) {
        CacheEntry<?> entry = map.get(key);
        if (entry == null) {
            return null;
        }
        LinkedHashSet<?> exist = (LinkedHashSet<?>) entry.getValue();
        return exist.size();
    }

    public Integer sismember(String key, String val) {
        CacheEntry<LinkedHashSet<String>> entry = (CacheEntry<LinkedHashSet<String>>) map.get(key);
        if (entry == null) {
            return 0;
        }
        LinkedHashSet<String> exist = entry.getValue();
        return exist.contains(val) ? 1 : 0;
    }

    public Integer srem(String key, String[] vals) {
        CacheEntry<LinkedHashSet<String>> entry = (CacheEntry<LinkedHashSet<String>>) map.get(key);
        if (entry == null) {
            return 0;
        }
        LinkedHashSet<String> exist = entry.getValue();
        return vals == null ? 0 : (int) Arrays.stream(vals).map(exist::remove).filter(x -> x).count();
    }

    Random random = new Random();

    public String[] spop(String key, int count) {
        CacheEntry<LinkedHashSet<String>> entry = (CacheEntry<LinkedHashSet<String>>) map.get(key);
        if (entry == null) {
            return null;
        }
        LinkedHashSet<String> exist = entry.getValue();
        if (exist == null) {
            return null;
        }

        int len = Math.min(exist.size(), count);
        String[] ret = new String[len];
        int index = 0;
        while (index < len) {
            String[] array = exist.toArray(String[]::new);
            String obj = array[random.nextInt(exist.size())];
            exist.remove(obj);
            ret[index++] = obj;
        }
        return ret;
    }

    // ===============  3. set end ===========

    // ===============  4. hash ===========
    public Integer hset(String key, String[] hkeys, String[] hvals) {
        if (hkeys == null || hkeys.length == 0) return 0;
        if (hvals == null || hvals.length == 0) return 0;

        CacheEntry<LinkedHashMap<String, String>> entry = (CacheEntry<LinkedHashMap<String, String>>) map.get(key);
        if (entry == null) {
            entry = new CacheEntry<>(new LinkedHashMap<>());
            map.put(key, entry);
        }

        LinkedHashMap<String, String> exist = entry.getValue();
        for (int i = 0; i < hkeys.length; i++) {
            exist.put(hkeys[i], hvals[i]);
        }

        return (int) Arrays.stream(hkeys).distinct().count();
    }

    public String hget(String key, String hkey) {
        CacheEntry<LinkedHashMap<String, String>> entry = (CacheEntry<LinkedHashMap<String, String>>) map.get(key);
        if (entry == null) {
            return null;
        }

        LinkedHashMap<String, String> exist = entry.getValue();
        return exist.get(hkey);
    }

    public String[] hgetAll(String key) {
        CacheEntry<LinkedHashMap<String, String>> entry = (CacheEntry<LinkedHashMap<String, String>>) map.get(key);
        if (entry == null) {
            return null;
        }

        LinkedHashMap<String, String> exist = entry.getValue();
        return exist.entrySet().stream().flatMap(e -> Stream.of(e.getKey(), e.getValue())).toArray(String[]::new);
    }

    public String[] hmget(String key, String[] hkeys) {
        CacheEntry<LinkedHashMap<String, String>> entry = (CacheEntry<LinkedHashMap<String, String>>) map.get(key);
        if (entry == null) {
            return null;
        }

        LinkedHashMap<String, String> exist = entry.getValue();
        return hkeys == null ? new String[0] :
                Arrays.stream(hkeys).map(exist::get).toArray(String[]::new);
    }

    public Integer hlen(String key) {
        CacheEntry<LinkedHashMap<String, String>> entry = (CacheEntry<LinkedHashMap<String, String>>) map.get(key);
        if (entry == null) {
            return null;
        }

        LinkedHashMap<String, String> exist = entry.getValue();
        return exist.size();
    }

    public Integer hexists(String key, String hkey) {
        CacheEntry<LinkedHashMap<String, String>> entry = (CacheEntry<LinkedHashMap<String, String>>) map.get(key);
        if (entry == null) {
            return null;
        }

        LinkedHashMap<String, String> exist = entry.getValue();
        return exist.containsKey(hkey) ? 1 : 0;
    }

    public Integer hdel(String key, String[] hkeys) {
        CacheEntry<LinkedHashMap<String, String>> entry = (CacheEntry<LinkedHashMap<String, String>>) map.get(key);
        if (entry == null) {
            return 0;
        }

        LinkedHashMap<String, String> exist = entry.getValue();

        return hkeys == null ? 0 :
                (int) Arrays.stream(hkeys).map(exist::remove).filter(Objects::nonNull).count();
    }
    // ===============  4. hash end ===========


    // ===============  5. zset end ===========
    public Integer zadd(String key, String[] values, double[] socres) {
        CacheEntry<LinkedHashSet<ZsetEntry>> entry = (CacheEntry<LinkedHashSet<ZsetEntry>>) map.get(key);
        if (entry == null) {
            entry = new CacheEntry<>(new LinkedHashSet<>());
            map.put(key, entry);
        }

        LinkedHashSet<ZsetEntry> exist = entry.getValue();
        for (int i = 0; i < values.length; i++) {
            exist.add(new ZsetEntry(values[i], socres[i]));
        }

        return values.length;
    }

    public Integer zcard(String key) {
        CacheEntry<?> entry = map.get(key);
        if (entry == null) {
            return 0;
        }

        LinkedHashSet<?> exist = (LinkedHashSet<?>) entry.getValue();

        return exist.size();
    }

    public Integer zcount(String key, double min, double max) {
        CacheEntry<?> entry = map.get(key);
        if (entry == null) {
            return 0;
        }

        LinkedHashSet<ZsetEntry> exist = (LinkedHashSet<ZsetEntry>) entry.getValue();

        return (int) exist.stream().filter(x -> x.getScore() >= min && x.getScore() <= max).count();
    }

    public Double zscore(String key, String val) {
        CacheEntry<?> entry = map.get(key);
        if (entry == null) {
            return null;
        }

        LinkedHashSet<ZsetEntry> exist = (LinkedHashSet<ZsetEntry>) entry.getValue();

        return exist.stream().filter(x -> x.getValue().equals(val)).map(ZsetEntry::getScore).findFirst().orElse(null);
    }

    public Integer zrank(String key, String val) {
        CacheEntry<?> entry = map.get(key);
        if (entry == null) {
            return null;
        }

        LinkedHashSet<ZsetEntry> exist = (LinkedHashSet<ZsetEntry>) entry.getValue();
        Double zscore = zscore(key, val);
        if (zscore == null) return null;
        return (int) exist.stream().filter(x -> x.getScore() < zscore).count();
    }

    public Integer zrem(String key, String[] vals) {
        CacheEntry<?> entry = map.get(key);
        if (entry == null) {
            return null;
        }

        LinkedHashSet<ZsetEntry> exist = (LinkedHashSet<ZsetEntry>) entry.getValue();

        return vals == null ? 0 :
                (int) Arrays.stream(vals).map(x -> exist.removeIf(y -> y.getValue().equals(x))).filter(x -> x).count();
    }
    // ===============  5. zset end ===========

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CacheEntry<T> {
        private T value;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ZsetEntry {
        private String value;
        private double score;
    }


}
