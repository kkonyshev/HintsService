package us.im360.hints.hintservice.util;


import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ResponseBuilder {
    public static final String RESULT_CODE_FIELD = "resultCode";
    public static final String RESULT_SUCCESS = "success";
    public static final String RESULT_FAIL = "fail";

    protected ObjectMapper mapper;
    protected ObjectNode objectNode;

    public static ResponseBuilder create(ObjectMapper mapper) {
        return new ResponseBuilder(mapper);
    }

    protected ResponseBuilder(ObjectMapper mapper) {
        this.mapper = mapper;
        this.objectNode = mapper.createObjectNode();
    }

    public ResponseBuilder success() {
        this.objectNode.put(RESULT_CODE_FIELD, RESULT_SUCCESS);
        return this;
    }

    public ResponseBuilder fail() {
        this.objectNode.put(RESULT_CODE_FIELD, RESULT_FAIL);
        return this;
    }

    public ResponseBuilder withPlainNode(JsonNode node) {
        Iterator<String> fieldIterator = node.getFieldNames();
        while (fieldIterator.hasNext()) {
            String fieldName = fieldIterator.next();
            this.objectNode.put(fieldName, node.get(fieldName));
        }
        return this;
    }

    public ResponseBuilder withArray(String fieldName, Collection<JsonNode> collection) {
        ArrayNode arr = new ArrayNode(mapper.getNodeFactory());
        arr.addAll(collection);
        objectNode.put(fieldName, arr);
        return this;
    }

    public ObjectNode build() {
        return this.objectNode;
    }
}
