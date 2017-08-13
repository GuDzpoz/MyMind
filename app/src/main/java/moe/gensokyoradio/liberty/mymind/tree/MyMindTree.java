package moe.gensokyoradio.liberty.mymind.tree;
/*
 *     This file is part of MyMind.
 * 
 *     MyMind is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     MyMind is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with MyMind. If not, see <http://www.gnu.org/licenses/>.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class MyMindTree {
    private MyNode root;
    private MyMindTree() {
        // nothing
    }
    public MyMindTree(String title) {
        this.root = new MyNode(title);
    }

    public MyNode getRootNode() {
        return root;
    }

    public String toJSON() {
        Gson gson = new Gson();
        return gson.toJson(root);
    }

    public static MyMindTree fromJSON(String JSON) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(MyNode.class, new MyNodeDeserializer())
                .create();
        MyMindTree tree = new MyMindTree();
        tree.root = gson.fromJson(JSON, MyNode.class);
        return tree;
    }
}

class MyNodeDeserializer implements JsonDeserializer<MyNode> {
    @Override
    public MyNode deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject nodeObject = json.getAsJsonObject();

        String title = nodeObject.get("title").getAsString();
        MyNode node = new MyNode(title);

        Type childrenListType = new TypeToken<List<MyNode>>(){}.getType();
        Type attributesMapType = new TypeToken<Map<String, String>>(){}.getType();
        node.children = context.deserialize(nodeObject.get("children"), childrenListType);
        node.attributes = context.deserialize(nodeObject.get("attributes"), attributesMapType);

        for(MyNode child : node.children) {
            child.parent = node;
        }

        return node;
    }
}
