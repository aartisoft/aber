package com.mammutgroup.taxi.commons.service.remote.model;

import com.google.gson.*;
import com.mammutgroup.taxi.commons.service.remote.rest.api.user.model.User;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mushtu
 * @since 6/14/16.
 */
public class ResponseDeserializer implements JsonDeserializer<ApiResponse> {
    @Override
    public ApiResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject responseObject = json.getAsJsonObject();
        ApiResponse response = new ApiResponse();
        if(responseObject.get("data") == null)
        {
            response.setMessage(responseObject.get("message").getAsString());
            response.setStatusCode(responseObject.get("status_code").getAsString());
            return response;
        }

        if(responseObject.get("data").isJsonObject())
        {
            JsonObject dataObject = responseObject.get("data").getAsJsonObject();
            response.setSingleData(deserializeDataObject(dataObject,context));
            response.setDataIsSingleObject(true);

        }else if(responseObject.get("data").isJsonArray())
        {
            JsonArray dataArray = responseObject.get("data").getAsJsonArray();
            List<BaseDto> list = new ArrayList<BaseDto>();
            for(int i = 0;i<dataArray.size();i++)
                list.add(deserializeDataObject(dataArray.get(i).getAsJsonObject(),context));
            response.setDataIsSingleObject(false);
            response.setListData(list);
        }else
            throw new JsonParseException("Invalid json data");

        return response;
    }

    BaseDto deserializeDataObject(JsonObject dataObject,JsonDeserializationContext context)
    {
        if(dataObject.get("type") == null)
            throw new JsonParseException("no data type provided.");
        String id = dataObject.get("id").getAsString();
        switch (ObjectType.valueOf(dataObject.get("type").getAsString()))
        {
            case USER:
                User user = context.deserialize(dataObject.get("attributes"),User.class);
                user.setId(id);
                return user;
            case TRIP:
                //TODO
                break;
            case VEHICLE:
                //TODO
                break;
        }
        return null;

    }

}
