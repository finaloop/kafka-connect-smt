package com.finaloop.kafka.connect.transforms;

import com.finaloop.kafka.connect.transforms.util.StructUtils;
import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.transforms.Transformation;
import org.apache.kafka.common.config.ConfigDef;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AddRecordTimestampField<R extends ConnectRecord<R>> implements Transformation<R> {

    private String fieldName;
    private Set<String> targetTopics;

    @Override
    public void configure(Map<String, ?> configs) {
        fieldName = (String) configs.get("field.name");
        String topics = (String) configs.get("topics");
        if (fieldName == null || topics == null) {
            throw new IllegalArgumentException("Missing required config: field.name or topics");
        }
        targetTopics = new HashSet<>(Arrays.asList(topics.split("\\s*,\\s*")));
    }

    @Override
    public R apply(R record) {
        if (!targetTopics.contains(record.topic()) || record.value() == null) return record;
        if (!(record.value() instanceof Struct)) return record;

        Struct value = (Struct) record.value();

        Struct newValue = StructUtils.addZonedTimestampField(
                record.valueSchema(),
                value,
                fieldName,
                Instant.ofEpochMilli(record.timestamp()) // record timestamp
        );

        return record.newRecord(
                record.topic(),
                record.kafkaPartition(),
                record.keySchema(),
                record.key(),
                newValue.schema(),
                newValue,
                record.timestamp()
        );
    }

    @Override
    public ConfigDef config() {
        return new ConfigDef()
                .define("field.name", ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "Name of the field to add")
                .define("topics", ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "Comma-separated list of target topics");
    }

    @Override
    public void close() {}
}
