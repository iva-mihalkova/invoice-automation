package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.List;

//Items manipulations
public class ItemAPI {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String ITEMS_ENDPOINT = "/items";
    private static final String ITEM_ENDPOINT = "/item";

    static {
        RestAssured.baseURI = "https://iva-test.inv.bg";
        RestAssured.basePath = "/RESTapi";
        RestAssured.authentication = RestAssured
                .preemptive().basic("iva_angelowa@abv.bg", "123456");
    }

    public static Response createItem(Item item){
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36")
                .log().all()
                .body(GSON.toJson(item))
                .when()
                .post(ITEM_ENDPOINT)
                .prettyPeek();
    }

    public static void deleteAllItems(){
        Response getItemsResp = getAllItems();
        if(!getItemsResp.asString().startsWith("[")){
            List<Integer> itemIds = JsonPath.read(getItemsResp.asString(), "$.items.[*].id");
            System.out.println(itemIds.toString());
            itemIds.forEach(id -> deleteItem(id));
        }

    }

    public static Response getAllItems(){
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36")
                .log().all()
                .when()
                .get(ITEMS_ENDPOINT)
                .prettyPeek();
    }

    public static Response deleteItem(int id){
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36")
                .log().all()
                .when()
                .delete(ITEM_ENDPOINT + "/" + id)
                .prettyPeek();
    }

    public static void main(String[] args) {
//        getAllItems();
//        Item item = new Item("API", "kg.", 10.24);
//        createItem(item);
        deleteAllItems();
    }
}
