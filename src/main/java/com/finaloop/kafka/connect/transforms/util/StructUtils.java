package com.finaloop.kafka.connect.transforms.util;

import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;

import java.time.Instant;

public class StructUtils {

    /**
     * Adds a field with ISO 8601 timestamp string to the Struct.
     *
     * @param oldSchema Original schema (may be null)
     * @param oldValue Original Struct value (may be null)
     * @param fieldName Name of the new field
     * @param timestamp Instant to store
     * @return New Struct with added timestamp field
     */
    public static Struct addZonedTimestampField(Schema oldSchema, Struct oldValue, String fieldName, Instant timestamp) {
        SchemaBuilder builder = SchemaBuilder.struct();
        if (oldSchema != null && oldSchema.name() != null) builder.name(oldSchema.name());
        if (oldSchema != null) {
            for (Field f : oldSchema.fields()) builder.field(f.name(), f.schema());
        }
        builder.field(fieldName, Schema.STRING_SCHEMA); // ISO 8601 string
        Schema newSchema = builder.build();

        Struct newStruct = new Struct(newSchema);
        if (oldSchema != null && oldValue != null) {
            for (Field f : oldSchema.fields()) newStruct.put(f.name(), oldValue.get(f));
        }
        newStruct.put(fieldName, timestamp.toString()); // e.g., "2025-11-07T18:45:00Z"
        return newStruct;
    }
}
