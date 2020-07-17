package com.example.sessiondemo.service;

import com.example.sessiondemo.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class PipeLineHashOperations<K, HK, HV> {
    RedisTemplate<K, Object> template;
    HashMapper<Object, String, Object> mapper = new Jackson2HashMapper(false);


    public PipeLineHashOperations() {

    }


    public PipeLineHashOperations(RedisTemplate<K, ?> template) {
        this.template = (RedisTemplate<K, Object>)template;
    }

    @Autowired()
    @Qualifier("redisEmpTemplate")
    public void setTemplate(RedisTemplate<K, Object> template) {
        this.template = template;
    }

    private void putInternal(K key, Map<? extends HK, ? extends HV> m) {
        if (m.isEmpty()) {
            return;
        }

        byte[] rawKey = rawKey(key);

        Map<byte[], byte[]> hashes = new LinkedHashMap<>(m.size());

        for (Map.Entry<? extends HK, ? extends HV> entry : m.entrySet()) {
            hashes.put(rawHashKey(entry.getKey()), rawHashValue(entry.getValue()));
        }

//        RedisCallback<?> redisCallback = new RedisCallback<Object>() {
//            @Override
//            public Object doInRedis(RedisConnection connection) throws DataAccessException {
//                //connection.hashCommands().hMSet();
//                return null;
//            }
//        };
//        template.executePipelined(redisCallback);
    }
    public void putAll(List<Employee> employees) {
        RedisCallback<?> redisCallback = new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                for(Employee employee : employees) {

                    String key = "em:" + employee.getEmployeeNo();
                    byte[] rawKey = rawKey(key);

                    Map<String, Object> stringObjectMap = mapper.toHash(employee);
                    Map<byte[], byte[]> hashes = new LinkedHashMap<>(stringObjectMap.size());
                    for (Map.Entry<String, Object> entry : stringObjectMap.entrySet()) {
                        hashes.put(rawHashKey(entry.getKey()), rawHashValue(entry.getValue()));
                    }
                    connection.hashCommands().hMSet(rawKey, hashes);
                }
                return null;
            }
        };
        template.executePipelined(redisCallback);
    }

    RedisSerializer keySerializer() {
        return template.getKeySerializer();
    }

    RedisSerializer valueSerializer() {
        return template.getValueSerializer();
    }

    RedisSerializer hashKeySerializer() {
        return template.getHashKeySerializer();
    }

    RedisSerializer hashValueSerializer() {
        return template.getHashValueSerializer();
    }

    RedisSerializer stringSerializer() {
        return template.getStringSerializer();
    }

    byte[] rawKey(Object key) {

        Assert.notNull(key, "non null key required");

        if (keySerializer() == null && key instanceof byte[]) {
            return (byte[]) key;
        }

        return keySerializer().serialize(key);
    }

    <HK> byte[] rawHashKey(HK hashKey) {
        Assert.notNull(hashKey, "non null hash key required");
        if (hashKeySerializer() == null && hashKey instanceof byte[]) {
            return (byte[]) hashKey;
        }
        return hashKeySerializer().serialize(hashKey);
    }

    <HV> byte[] rawHashValue(HV value) {

        if (hashValueSerializer() == null & value instanceof byte[]) {
            return (byte[]) value;
        }
        return hashValueSerializer().serialize(value);
    }
}
